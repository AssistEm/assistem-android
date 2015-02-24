package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import seniorproject.caretakers.caretakersapp.tempdata.model.Caretaker;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;

public class UserRestClient extends RestClient {

    protected static final String USER = "user";

    protected static final String LOGIN = "login";
    protected static final String CHANGE_PASSWORD = "password";
    protected static final String ME = "me";

    public static void login(Context context, String email, String password,
                             BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + USER + "/" + LOGIN;
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("password", password);
            mClient.post(context, url, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void createPatient(Context context, String email, String password,
                                     String firstName, String lastName, String phone,
                                     String communityName, boolean privacy,
                                     BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + USER + "/";
        JSONObject body = new JSONObject();
        JSONObject user = new JSONObject();
        JSONObject community = new JSONObject();
        try {
            user.put("email", email);
            user.put("password", password);
            user.put("first_name", firstName);
            user.put("last_name", lastName);
            user.put("phone", phone);
            user.put("type", Patient.PATIENT_TYPE);
            community.put("name", communityName);
            community.put("privacy", privacy);
            body.put("user", user);
            body.put("community", community);
            mClient.post(context, url, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void createCaretaker(Context context, String email, String password,
                                     String firstName, String lastName, String phone,
                                     String query, BaseJsonResponseHandler handler)
            throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + USER + "/";
        JSONObject body = new JSONObject();
        JSONObject user = new JSONObject();
        JSONObject community = new JSONObject();
        try {
            user.put("email", email);
            user.put("password", password);
            user.put("first_name", firstName);
            user.put("last_name", lastName);
            user.put("phone", phone);
            user.put("type", Caretaker.CARETAKER_TYPE);
            community.put("query", query);
            body.put("user", user);
            body.put("community", community);
            mClient.post(context, url, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
