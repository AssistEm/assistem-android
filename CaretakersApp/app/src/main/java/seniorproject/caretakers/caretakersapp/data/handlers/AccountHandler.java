package seniorproject.caretakers.caretakersapp.data.handlers;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import seniorproject.caretakers.caretakersapp.tempdata.apis.BaseJsonResponseHandler;
import seniorproject.caretakers.caretakersapp.tempdata.apis.NoNetworkException;
import seniorproject.caretakers.caretakersapp.tempdata.apis.UserRestClient;
import seniorproject.caretakers.caretakersapp.tempdata.apis.CommunityRestClient;
import seniorproject.caretakers.caretakersapp.tempdata.model.Availability;
import seniorproject.caretakers.caretakersapp.tempdata.model.Community;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;

/**
 * Handler class for the user. Stores and unpacks data about the user, as well the authentication
 * token. Also handles any network requests relating to the user themselves, as well as GCM
 * registration for Pinging.
 */
public class AccountHandler {

    // Keys for various data that are used as keys to the device's permanent storage
    private final static String USER_STORE_PREFS = "user";
    private final static String TOKEN_KEY_PREFS = "token";
    private final static String ID_KEY_PREFS = "id";
    private final static String FIRST_NAME_KEY_PREFS = "first_name";
    private final static String LAST_NAME_KEY_PREFS = "last_name";
    private final static String EMAIL_KEY_PREFS = "email";
    private final static String PHONE_KEY_PREFS = "phone";
    private final static String TYPE_KEY_PREFS = "type";
    private final static String COMMUNITY_ID_PREFS = "community_id";
    private final static String COMMUNITY_NAME_PREFS = "community_name";
    private final static String COMMUNITY_PATIENT_ID_PREFS = "community_patient";
    private final static String COMMUNITY_CARETAKERS_PREFS = "community_caretakers";
    private final static String COMMUNITY_PRIMARY_ID_PREFS = "primary_caretaker";

    // Keys for data relating to GCM registration with the server for Pinging.
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    //Sender ID for the Assist'em app
    private static final String GCM_SENDER_ID = "595143937184";

    static AccountHandler mInstance;

    User mCurrentUser;
    Community mCurrentCommunity;
    String mToken;
    Context mApplicationContext;
    SharedPreferences mUserStore;

    List<AccountListener> mListeners;

    /**
     * Private constructor for the AccountHandler class. Is only used by the static getInstance
     * call to instantiate an instance for the singleton. Also loads data from the device's
     * permanent store.
     * @param applicationContext Context to instantate the class in.
     */
    private AccountHandler(Context applicationContext) {
        mApplicationContext = applicationContext;
        mListeners = new ArrayList<AccountListener>();
        loadFromStore();
    }

    /**
     * Singleton getInstance method. Either returns an existing instance of the AccountHandler class
     * or constructs, sets and returns a new instance.
     * @param context Context in which to get the Instance.
     * @return An instance of an AccountHandler
     */
    public static AccountHandler getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new AccountHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Non-contextual getInstance class. Used in areas where a Context is not available.
     * @return An instance of an AccountHandler
     */
    public static AccountHandler getExistingInstance() {
        return mInstance;
    }

    /**
     * Private call that sets all current data in memory about the user to the device's permanent
     * store.
     */
    private void setToStore() {
        if(mUserStore == null) {
            mUserStore = mApplicationContext.getSharedPreferences(USER_STORE_PREFS, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor edit = mUserStore.edit();
        if(mToken != null) {
            edit.putString(TOKEN_KEY_PREFS, mToken)
                    .putString(ID_KEY_PREFS, mCurrentUser.getId())
                    .putString(FIRST_NAME_KEY_PREFS, mCurrentUser.getFirstName())
                    .putString(LAST_NAME_KEY_PREFS, mCurrentUser.getLastName())
                    .putString(EMAIL_KEY_PREFS, mCurrentUser.getEmail())
                    .putString(PHONE_KEY_PREFS, mCurrentUser.getPhone())
                    .putString(TYPE_KEY_PREFS, mCurrentUser.getType());
            if(mCurrentCommunity != null) {
                Set<String> caretakerSet = new HashSet<>(mCurrentCommunity.getCaretakerIds());
                edit.putString(COMMUNITY_ID_PREFS, mCurrentCommunity.getId())
                        .putString(COMMUNITY_NAME_PREFS, mCurrentCommunity.getName())
                        .putString(COMMUNITY_PATIENT_ID_PREFS, mCurrentCommunity.getPatientId())
                        .putString(COMMUNITY_PRIMARY_ID_PREFS, mCurrentCommunity.getPrimary())
                        .putStringSet(COMMUNITY_CARETAKERS_PREFS, caretakerSet);
            }
            edit.commit();
        }
    }

    /**
     * Private call that loads into memory all the data about a user that is in the permanent store.
     * This include the authentication token used for network calls.
     */
    private void loadFromStore() {
        if(mUserStore == null) {
            mUserStore = mApplicationContext.getSharedPreferences(USER_STORE_PREFS, Context.MODE_PRIVATE);
        }
        if(mUserStore.contains(TOKEN_KEY_PREFS)) {
            mToken = mUserStore.getString(TOKEN_KEY_PREFS, "");
            if(mToken.isEmpty()) {
                return;
            }
            String id = mUserStore.getString(ID_KEY_PREFS, "");
            String firstName = mUserStore.getString(FIRST_NAME_KEY_PREFS, "");
            String lastName = mUserStore.getString(LAST_NAME_KEY_PREFS, "");
            String email = mUserStore.getString(EMAIL_KEY_PREFS, "");
            String phone = mUserStore.getString(PHONE_KEY_PREFS, "");
            String type = mUserStore.getString(TYPE_KEY_PREFS, "");
            mCurrentUser = User.parseUser(id, firstName, lastName, email, phone, type);
            if(mUserStore.contains(COMMUNITY_ID_PREFS)) {
                String communityId = mUserStore.getString(COMMUNITY_ID_PREFS, "");
                String communityName = mUserStore.getString(COMMUNITY_NAME_PREFS, "");
                String communityPatient = mUserStore.getString(COMMUNITY_PATIENT_ID_PREFS, "");
                String communityPrimary = mUserStore.getString(COMMUNITY_PRIMARY_ID_PREFS, "");
                Set<String> communityCaretakers = mUserStore.getStringSet(COMMUNITY_CARETAKERS_PREFS, new HashSet());
                ArrayList<String> caretakers = new ArrayList<>(communityCaretakers);
                mCurrentCommunity = new Community(communityId, communityName, communityPatient, communityPrimary, caretakers);
            }
        }
    }

    /**
     * Private method to clear the device's permanent store.
     */
    private void clearStore() {
        if(mUserStore == null) {
            mUserStore = mApplicationContext.getSharedPreferences(USER_STORE_PREFS, Context.MODE_PRIVATE);
        }
        mUserStore.edit().clear().commit();
    }

    //Callback object for parsing data from the server from a login. Notifies observers of status.
    BaseJsonResponseHandler mLoginHandler = new BaseJsonResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                mToken = response.getString("token");
                JSONObject user = response.getJSONObject("user");
                mCurrentUser = User.parseUser(user);
                JSONArray community = response.getJSONArray("community");
                if(community.length() > 0) {
                    mCurrentCommunity = Community.parseCommunity(community.getJSONObject(0));
                }
                setToStore();
                for(AccountListener listener : mListeners) {
                    listener.onLoggedIn();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //Callback object for parsing data from the server from a registration. Notifies observers of status.
    BaseJsonResponseHandler mRegisterHandler = new BaseJsonResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                mToken = response.getString("token");
                JSONObject user = response.getJSONObject("user");
                mCurrentUser = User.parseUser(user);
                JSONObject community = response.getJSONObject("community");
                mCurrentCommunity = Community.parseCommunity(community);
                setToStore();
                for(AccountListener listener : mListeners) {
                    listener.onRegistered();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Callback class for parsing the response from the server from a GCM device registration.
     * Includes an instance of the ID registered with for storing when the request successfully
     * completes.
     */
    private class GcmRegisterResponseHandler extends BaseJsonResponseHandler {
        String mGcmId;

        public GcmRegisterResponseHandler(String gcmId) {
            mGcmId = gcmId;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            storeRegistrationId(mApplicationContext, mGcmId);
        }
    }

    /**
     * Callback class for parsing the response from a request for the full profile of the user.
     * Includes an instance of an AccountListener observer to notify of the result of the request.
     */
    private class GetFullProfileInfoResponseHandler extends BaseJsonResponseHandler {
        AccountListener mListener;

        public GetFullProfileInfoResponseHandler(AccountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onFullProfileFetched(object);
            }
        }
    }


    /**
     * Callback class for parsing the response from a request for the full profile of the user.
     * Includes an instance of an AccountListener observer to notify of the result of the request.
     */
    private class GetFullProfileUserInfoResponseHandler extends BaseJsonResponseHandler {
        AccountListener mListener;

        public GetFullProfileUserInfoResponseHandler(AccountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onFullProfileUserFetched(object);
            }
        }
    }

    /**
     * Callback class for parsing the response from a request for the availability of the user.
     * Includes an instance of an AccountListener observer to notify the result of the request.
     */
    private class GetAvailabilityResponseHandler extends BaseJsonResponseHandler {
        AccountListener mListener;

        public GetAvailabilityResponseHandler(AccountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            List<Availability> availabilities = new ArrayList<>();
            if(object.has("caretaker_info") && !object.isNull("caretaker_info")) {
                try {
                    JSONObject caretakerObject = object.getJSONObject("caretaker_info");
                    if(caretakerObject.has("availability") && !caretakerObject.isNull("availability")) {
                        JSONArray availabilityArray = caretakerObject.getJSONArray("availability");
                        for(int i = 0; i < availabilityArray.length(); i++) {
                            availabilities.add(Availability.parseAvailability(availabilityArray.getJSONObject(i)));
                        }
                        if(mListener != null) {
                            mListener.onAvailabilityFetched(availabilities);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Callback class for parsing the response from a request for setting the availability of the
     * user. Again includes an instance of an AccountListener for notifying the result.
     */
    private class SetAvailabilityResponseHandler extends BaseJsonResponseHandler {
        AccountListener mListener;

        public SetAvailabilityResponseHandler(AccountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onAvailabilitySet();
            }
        }
    }

    /**
     * Callback class for parsing the response from a request for setting the primary caretaker of the
     * community. Again includes an instance of an AccountListener for notifying the result.
     */
    private class SetPrimaryCaretakerResponseHandler extends BaseJsonResponseHandler {
        AccountListener mListener;

        public SetPrimaryCaretakerResponseHandler(AccountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onPrimaryCaretakerSet();
            }
        }
    }

    /**
     * Callback class for fetching the current availability status of the user. AccountListener
     * included for notifying.
     */
    private class GetCurrentAvailabilityResponseHandler extends BaseJsonResponseHandler {
        AccountListener mListener;

        public GetCurrentAvailabilityResponseHandler(AccountListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            try {
                if (mListener != null) {
                    mListener.onCurrentAvailabilityFetched(object.getBoolean("is_available"));
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    Callback<Response> mRegisterCallback = new Callback<Response>() {
        @Override
        public void success(Response jsonObject, Response response) {
            for(AccountListener listener : mListeners) {
                listener.onRegistered();
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    /**
     * Public method for executing a login attempt.
     * @param context Context in which to execute the call
     * @param email Email of the user
     * @param password Password of the user
     */
    public void login(Context context, String email, String password) {
        try {
            UserRestClient.login(context, email, password, mLoginHandler);
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method for registering a patient user.
     * @param context Context in which to execute the call
     * @param email Email of the user
     * @param password Password of the user
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param phone Phone number of the user
     * @param communityName Name of community to create
     * @param privacy Privacy of the community
     */
    public void patientRegister(Context context, String email, String password, String firstName,
                                String lastName, String phone, String communityName,
                                boolean privacy) {
        try {
            UserRestClient.createPatient(context, email, password, firstName, lastName, phone,
                    communityName, privacy, mRegisterHandler);
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method for registering a caretaker user.
     * @param context Context in which to execute call
     * @param email Email of user
     * @param password Password of user
     * @param firstName First name of user
     * @param lastName Last name of user
     * @param phone Phone number of user
     * @param emailOrName Email the patient belonging to, or name of a community to join
     */
    public void caretakerRegister(Context context, String email, String password, String firstName,
                                  String lastName, String phone, String emailOrName) {
        try {
            UserRestClient.createCaretaker(context, email, password, firstName, lastName, phone,
                    emailOrName, mRegisterHandler);
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to get the full profile of the current user.
     * @param context Context to execute call in
     * @param mListener AccountListener observer to notify of result.
     */
    public void getFullProfile(Context context, AccountListener mListener) {
        try {
            UserRestClient.getFullProfile(context, new GetFullProfileInfoResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void getFullProfileUser(Context context, String databaseId, AccountListener mListener) {
        try {
            UserRestClient.getFullProfileUser(context, databaseId, new GetFullProfileUserInfoResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to get the current availability of the user.
     * @param context Context to execute call in
     * @param mListener AccountListener observer to notify of result.
     */
    public void getAvailability(Context context, AccountListener mListener) {
        try {
            UserRestClient.getFullProfile(context, new GetAvailabilityResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to set the availability of the user
     * @param context Context to execute call in
     * @param availabilities List of Availability objects which represent availability of the user
     * @param mListener AccountListener observer to notify of result
     */
    public void setAvailability(Context context, List<Availability> availabilities,
                                AccountListener mListener) {
        try {
            UserRestClient.setAvailability(context, availabilities,
                    new SetAvailabilityResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to set the primary caretaker of the user
     * @param context Context to execute call in
     * @param mListener AccountListener observer to notify of result
     *                  */
    public void setPrimaryCaretaker(Context context, String primaryCaretaker,
                                AccountListener mListener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            CommunityRestClient.setPrimaryCaretaker(context, communityId, primaryCaretaker,
                    new SetPrimaryCaretakerResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to get the current availability of the user
     * @param context Context to execute call in
     * @param mListener AccountListener observer to notify of result
     */
    public void getCurrentAvailability(Context context, AccountListener mListener) {
        try {
            UserRestClient.getAvailable(context, new GetCurrentAvailabilityResponseHandler(mListener));
        } catch(NoNetworkException e) {

        }
    }

    /**
     * Public method to check if a user is locally logged in (has an auth token and user information)
     * Does not guarantee logged in state on the server, as authentication token may be invalid.
     * @return If the user is locally logged in.
     */
    public boolean getLocalLoggedIn() {
        return mCurrentUser != null;
    }

    /**
     * Public method to get the current user object including their information
     * @return The current user object
     */
    public User getCurrentUser() {
        return mCurrentUser;
    }

    /**
     * Public method to get the community to which the current user belongs to.
     * @return The community object to which the current user belongs.
     */
    public Community getCurrentCommunity() {
        return mCurrentCommunity;
    }

    /**
     * Public method to get the authentication token of the user.
     * @return The current authentication token of the user.
     */
    public String getToken() {
        return mToken;
    }

    /**
     * Public method to attach an AccountListener observer to the AccountHandler class
     * @param listener AccountListener to attach
     */
    public void addAccountListener(AccountListener listener) {
        mListeners.add(listener);
    }

    /**
     * Public method to remove a previously attached AccountListener from the AccountHandler class
     * @param listener AccountListener to remove.
     */
    public void removeAccountListener(AccountListener listener) {
        int index = mListeners.indexOf(listener);
        if(index >= 0) {
            mListeners.remove(index);
        }
    }

    /**
     * Public method to remove all attached AccountListeners
     */
    public void clearAccountListeners() {
        mListeners.clear();
    }

    /**
     * Public method to logout of the app.
     */
    public void logout() {
        if(mCurrentUser != null) {
            mCurrentUser = null;
            mToken = null;
            clearStore();
        }
        for(AccountListener listener : mListeners) {
            listener.onLogout();
        }
    }

    /**
     * Public method to trigger an authentication error. Notifies attached observers, and logs out.
     */
    public void triggerAuthenticationError() {
        if(mListeners != null) {
            for(AccountListener listener : mListeners) {
                listener.onAuthenticationError();
            }
        }
        logout();
    }

    /**
     * Public method to begin the GCM registration process
     */
    public void registerGCM() {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mApplicationContext);
        String regId = getRegistrationId(mApplicationContext);
        if(regId.isEmpty()) {
            registerInBackground();
        }
        else {
            sendGcmId(mApplicationContext, regId);
        }
    }

    /**
     * Private method that sends the GCM registration id to the server
     * @param context Context to execute call in
     * @param gcmId GCM id to send to server
     */
    private void sendGcmId(Context context, String gcmId) {
        try {
            UserRestClient.setGcmId(context, gcmId, new GcmRegisterResponseHandler(gcmId));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method to generate a GCM registration id for the device
     * @param context Context to generate the id from
     * @return The id that is generated
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("GCM", "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i("GCM", "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Get the current app version number
     * @param context Context to generate the app version from
     * @return The current numerical app version number
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Method to get the SharedPreferences store for the GCM data
     * @param context Context to get the SharedPreferences from
     * @return The SharedPreferences object containing the GCM data
     */
    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences("gcm_prefs",
                Context.MODE_PRIVATE);
    }

    /**
     * Private method to store the GCM ID passed into the device's permanent store
     * @param context Context in which to store the id
     * @param regId GCM id to store
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("GCM", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Private method that attempts to register with GCM and receive a GCM ID.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mApplicationContext);
                    return gcm.register(GCM_SENDER_ID);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostExecute(String param) {
                sendGcmId(mApplicationContext, param);
            }

        }.execute(null, null, null);
    }

    //Observer class for the AccountHandler
    public static abstract class AccountListener {
        public void onLoggedIn() { }
        public void onRegistered() { }
        public void onLogout() { }
        public void onAuthenticationError() { }
        public void onFullProfileFetched(JSONObject profile) { }
        public void onFullProfileUserFetched(JSONObject profile) {}
        public void onAvailabilityFetched(List<Availability> availabilities) { }
        public void onAvailabilitySet() {}
        public void onPrimaryCaretakerSet() {}
        public void onCurrentAvailabilityFetched(boolean available) { }
    }
}
