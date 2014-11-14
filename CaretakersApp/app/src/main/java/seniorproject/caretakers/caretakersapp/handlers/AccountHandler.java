package seniorproject.caretakers.caretakersapp.handlers;

import android.accounts.Account;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import seniorproject.caretakers.caretakersapp.apiservices.UserService;

public class AccountHandler {

    static AccountHandler mInstance;

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
                listener.onLoggedIn();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("ERROR", error.toString());
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

    public void login(String email, String password) {
        UserService.get().login(email, password, mLoginCallBack);
    }

    public void patientRegister(String email, String password) {
        UserService.get().patientRegister(email, password, mRegisterCallback);
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
        public void onLoggedIn() { }
        public void onRegistered() { }
    }
}
