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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import seniorproject.caretakers.caretakersapp.tempdata.apis.BaseJsonResponseHandler;
import seniorproject.caretakers.caretakersapp.tempdata.apis.NoNetworkException;
import seniorproject.caretakers.caretakersapp.tempdata.apis.UserRestClient;
import seniorproject.caretakers.caretakersapp.tempdata.model.Availability;
import seniorproject.caretakers.caretakersapp.tempdata.model.Community;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;

public class AccountHandler {

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

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private static final String GCM_SENDER_ID = "202386854646";

    static AccountHandler mInstance;

    User mCurrentUser;
    Community mCurrentCommunity;
    String mToken;
    Context mApplicationContext;
    SharedPreferences mUserStore;

    List<AccountListener> mListeners;

    private AccountHandler(Context applicationContext) {
        mApplicationContext = applicationContext;
        mListeners = new ArrayList<AccountListener>();
        loadFromStore();
    }

    public static AccountHandler getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new AccountHandler(context.getApplicationContext());
        }
        return mInstance;
    }

    public static AccountHandler getExistingInstance() {
        return mInstance;
    }

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
                edit.putString(COMMUNITY_ID_PREFS, mCurrentCommunity.getId())
                        .putString(COMMUNITY_NAME_PREFS, mCurrentCommunity.getName())
                        .putString(COMMUNITY_PATIENT_ID_PREFS, mCurrentCommunity.getPatientId());
            }
            edit.commit();
        }
    }

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
                mCurrentCommunity = new Community(communityId, communityName, communityPatient);
            }
        }
    }

    private void clearStore() {
        if(mUserStore == null) {
            mUserStore = mApplicationContext.getSharedPreferences(USER_STORE_PREFS, Context.MODE_PRIVATE);
        }
        mUserStore.edit().clear().commit();
    }

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

    public void login(Context context, String email, String password) {
        try {
            UserRestClient.login(context, email, password, mLoginHandler);
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

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

    public void caretakerRegister(Context context, String email, String password, String firstName,
                                  String lastName, String phone, String emailOrName) {
        try {
            UserRestClient.createCaretaker(context, email, password, firstName, lastName, phone,
                    emailOrName, mRegisterHandler);
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void getFullProfile(Context context, AccountListener mListener) {
        try {
            UserRestClient.getFullProfile(context, new GetFullProfileInfoResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void getAvailability(Context context, AccountListener mListener) {
        try {
            UserRestClient.getFullProfile(context, new GetAvailabilityResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void setAvailability(Context context, List<Availability> availabilities,
                                AccountListener mListener) {
        try {
            UserRestClient.setAvailability(context, availabilities,
                    new SetAvailabilityResponseHandler(mListener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentAvailability(Context context, AccountListener mListener) {
        try {
            UserRestClient.getAvailable(context, new GetCurrentAvailabilityResponseHandler(mListener));
        } catch(NoNetworkException e) {

        }
    }

    public boolean getLocalLoggedIn() {
        return mCurrentUser != null;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public Community getCurrentCommunity() {
        return mCurrentCommunity;
    }

    public String getToken() {
        return mToken;
    }

    public void addAccountListener(AccountListener listener) {
        mListeners.add(listener);
    }

    public void removeAccountListener(AccountListener listener) {
        int index = mListeners.indexOf(listener);
        if(index >= 0) {
            mListeners.remove(index);
        }
    }

    public void clearAccountListeners() {
        mListeners.clear();
    }

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

    public void triggerAuthenticationError() {
        if(mListeners != null) {
            for(AccountListener listener : mListeners) {
                listener.onAuthenticationError();
            }
        }
        logout();
    }

    public void registerGCM() {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mApplicationContext);
        String regId = getRegistrationId(mApplicationContext);
        if(regId.isEmpty()) {
            registerInBackground();
        }
    }

    private void sendGcmId(Context context, String gcmId) {
        try {
            UserRestClient.setGcmId(context, gcmId, new GcmRegisterResponseHandler(gcmId));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

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

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences("gcm_prefs",
                Context.MODE_PRIVATE);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i("GCM", "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

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

    public static abstract class AccountListener {
        public void onLoggedIn() { }
        public void onRegistered() { }
        public void onLogout() { }
        public void onAuthenticationError() { }
        public void onFullProfileFetched(JSONObject profile) { }
        public void onAvailabilityFetched(List<Availability> availabilities) { }
        public void onAvailabilitySet() {}
        public void onCurrentAvailabilityFetched(boolean available) { }
    }
}
