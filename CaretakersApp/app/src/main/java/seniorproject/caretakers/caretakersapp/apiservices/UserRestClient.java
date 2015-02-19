package seniorproject.caretakers.caretakersapp.apiservices;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by Jason on 12/11/14.
 */
public class UserRestClient extends BaseRestClient {

    protected final static String USER = "user";

    public static void login(Context context, String email, String password,
                             JsonHttpResponseHandler handler) {
        String url = BASE_URL + USER + "/login";
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            Log.i("POST", json.toString());
            mClient.addHeader("content-type", "application/json");
            mClient.post(context, url,
                    new ByteArrayEntity(json.toString().getBytes()),
                    "application/json", handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void caretakerRegister(Context context, String email, String password,
                                         JsonHttpResponseHandler handler) {
        String url = BASE_URL + USER;
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            json.put("type", "caretaker");
            mClient.put(context, url,
                    new ByteArrayEntity(json.toString().getBytes(Charset.forName("UTF_8"))),
                    "application/json", handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void patientRegister(Context context, String email, String password,
                                         JsonHttpResponseHandler handler) {
        String url = BASE_URL + USER;
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            json.put("type", "patient");
            mClient.put(context, url,
                    new ByteArrayEntity(json.toString().getBytes(Charset.forName("UTF_8"))),
                    "application/json", handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
