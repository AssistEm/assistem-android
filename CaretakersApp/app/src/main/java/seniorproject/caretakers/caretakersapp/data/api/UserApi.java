package seniorproject.caretakers.caretakersapp.data.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;
import seniorproject.caretakers.caretakersapp.data.model.Login;
import seniorproject.caretakers.caretakersapp.data.model.LoginRequest;
import seniorproject.caretakers.caretakersapp.data.model.PasswordChange;
import seniorproject.caretakers.caretakersapp.data.model.PostRequest;
import seniorproject.caretakers.caretakersapp.data.model.User;

/**
 * Created by Stephen on 2/18/2015.
 */
public interface UserApi {

    @GET("/user")
    public Observable<List<User>> users();

    @POST("/user")
    public Observable<Login> create(@Body User user);

    @POST("/user/login")
    public Observable<Login> login(@Body LoginRequest request);

    @GET("/user/{id}")
    public Observable<User> user(@Path("id") int id);

    @DELETE("/user/{id}")
    public void delete(@Header("Authorization") String auth,
                       @Path("id") int id);

    @PUT("/user/{id}/password")
    public void changePassword(@Header("Authorization") String auth,
                               @Path("id") int id,
                               @Body PasswordChange passwordChange);

    @PUT("/user/me")
    public Observable<PostRequest> editDetails(@Header("Authorization") String auth,
                                               @Body User user);

    @GET("/user/me")
    public Observable<User> me(@Header("Authorization") String auth);

}
