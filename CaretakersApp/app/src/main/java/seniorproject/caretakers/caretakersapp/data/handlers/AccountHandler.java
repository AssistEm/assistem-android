package seniorproject.caretakers.caretakersapp.data.handlers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import seniorproject.caretakers.caretakersapp.tempdata.apis.BaseJsonResponseHandler;
import seniorproject.caretakers.caretakersapp.tempdata.apis.NoNetworkException;
import seniorproject.caretakers.caretakersapp.tempdata.apis.UserRestClient;
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
                JSONObject community = response.getJSONObject("community");
                mCurrentCommunity = Community.parseCommunity(community);
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

    public boolean getLocalLoggedIn() {
        return mCurrentUser != null;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public Community getCurrentCommunity() {
        return mCurrentCommunity;
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

    public static abstract class AccountListener {
        public void onLoggedIn() { }
        public void onRegistered() { }
        public void onLogout() { }
        public void onAuthenticationError() { }
    }
}
