package seniorproject.caretakers.caretakersapp.handlers;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import seniorproject.caretakers.caretakersapp.apiservices.BaseService;
import seniorproject.caretakers.caretakersapp.apiservices.UserRestClient;
import seniorproject.caretakers.caretakersapp.apiservices.UserService;
import seniorproject.caretakers.caretakersapp.objects.User;

public class AccountHandler {

    static AccountHandler mInstance;

    User mCurrentUser;

    List<AccountListener> mListeners;

    private AccountHandler() {
        mListeners = new ArrayList<AccountListener>();
    }

    public static AccountHandler getInstance() {
        if(mInstance == null) {
            mInstance = new AccountHandler();
        }
        return mInstance;
    }

    Callback<Response> mLoginCallBack = new Callback<Response>() {
        @Override
        public void success(Response jsonObject, Response response) {
            Log.i("LOGGED IN", "LOGGED IN CALLBACK");
            for(AccountListener listener : mListeners) {
                //listener.onLoggedIn();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("ERROR", error.toString());
        }
    };

    JsonHttpResponseHandler mLoginHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                JSONObject user = response.getJSONObject("user");
                String firstName = user.getString("first_name");
                String lastName = user.getString("last_name");
                String phone = user.getString("phone");
                String type = user.getString("type");
                JSONObject loginInfo = user.getJSONObject("login_info");
                String email = loginInfo.getString("email");
                Log.i("LASTNAME", lastName);
                for(AccountListener listener : mListeners) {
                    listener.onLoggedIn(firstName, lastName, email, phone, type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
            Log.i("STATUS", String.valueOf(statusCode));

            Log.i("RESPONSE", response.toString());

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
        //UserService.get().login(email, password, mLoginCallBack);
        UserRestClient.login(context, email, password, mLoginHandler);
    }

    public void patientRegister(String email, String password) throws JSONException {
        UserService.get().patientRegister(email, password, mRegisterCallback);
    }

    public boolean getLocalLoggedIn() {
        return UserService.getLocalLoggedIn();
    }

    public User getCurrentUser() {
        return mCurrentUser;
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

    public static abstract class AccountListener {
        public void onLoggedIn(String firstName, String lastName, String email, String phone, String type) { }
        public void onRegistered() { }
    }
}
