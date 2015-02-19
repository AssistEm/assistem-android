package seniorproject.caretakers.caretakersapp.apiservices;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpCookie;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

public class UserService extends BaseService {

    private static UserService mUserService;
    private static API REST_CLIENT;

    static {
        init();
    }

    private static void init() {
        REST_CLIENT = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setClient(new OkClient(getOkClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(API.class);
    }

    public static API get() {
        return REST_CLIENT;
    }

    public static boolean getLocalLoggedIn() {
        for(HttpCookie cookie : getCookies()) {
            Log.i("DOMAIN", cookie.getDomain());
            if(cookie.getDomain().equalsIgnoreCase("ca.jasonmsu.com")) {
                return true;
            }
        }
        return false;
    }

    public interface API {
        @FormUrlEncoded
        @POST("/user/login")
        void login(@Field("email") String email, @Field("password") String password,
                   Callback<Response> callback);

        @FormUrlEncoded
        @POST("/user/register")
        void patientRegister(@Field("email") String email, @Field("password") String password,
                             Callback<Response> callback);
        @FormUrlEncoded
        @POST("/user/register")
        void caretakerRegister(@Field("email") String email, @Field("password") String password,
                               Callback<Response> callback);

        @GET("/community/")
        void getCommunities();
    }
}
