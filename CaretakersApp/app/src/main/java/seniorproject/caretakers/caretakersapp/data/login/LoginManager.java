package seniorproject.caretakers.caretakersapp.data.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import seniorproject.caretakers.caretakersapp.data.model.Community;
import seniorproject.caretakers.caretakersapp.data.model.Login;
import seniorproject.caretakers.caretakersapp.data.model.User;

/**
 * Created by Stephen on 3/3/2015.
 */
public class LoginManager {

    private static final String KEY_COMMUNITY_ID = "community_id";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_MANAGER = "user_manager";

    private SharedPreferences prefs;
    private Gson gson;

    private String communityId;
    private String token;
    private User user;

    public LoginManager(Context context) {
        prefs = context.getSharedPreferences(KEY_USER_MANAGER, Context.MODE_PRIVATE);
        gson = new GsonBuilder().create();
    }

    private void clearPrefs() {
        prefs.edit().clear().apply();
    }

    public String getToken() {
        if(token == null && isTokenInSharedPrefs()) {
            token = prefs.getString(KEY_TOKEN, null);
        }
        return token;
    }

    public User getUser() {
        if(user == null && isUserInSharedPrefs()) {
            String userJson = prefs.getString(KEY_USER, null);
            user = gson.fromJson(userJson, User.class);
        }
        return user;
    }

    public String getCommunityId() {
        if(communityId == null && isCommunityIdInSharedPrefs()) {
            communityId = prefs.getString(KEY_COMMUNITY_ID, null);
        }
        return communityId;
    }

    private boolean isCommunityIdInSharedPrefs() {
        return prefs.getString(KEY_COMMUNITY_ID, null) != null;
    }

    public boolean isUserInSharedPrefs() {
        return prefs.getString(KEY_USER, null) != null;
    }

    public boolean isTokenInSharedPrefs() {
        return prefs.getString(KEY_TOKEN, null) != null;
    }

    public void login(Login login) {
        this.token = login.getToken();
        this.user = login.getUser();
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_USER, gson.toJson(user))
            .apply();
    }

    public void logout() {
        this.token = null;
        this.user = null;
        clearPrefs();
    }

    // TODO: Retrieve community id from api
    public void setCommunityId(String communityId) {
        // A user is only part of a single community, so we can save the id to shared prefs
        this.communityId = communityId;
        prefs.edit()
                .putString(KEY_COMMUNITY_ID, communityId)
                .apply();
    }
}
