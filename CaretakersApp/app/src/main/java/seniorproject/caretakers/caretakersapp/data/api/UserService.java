package seniorproject.caretakers.caretakersapp.data.api;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;
import seniorproject.caretakers.caretakersapp.data.model.Login;
import seniorproject.caretakers.caretakersapp.data.model.LoginRequest;
import seniorproject.caretakers.caretakersapp.data.model.PasswordChange;
import seniorproject.caretakers.caretakersapp.data.model.Registration;
import seniorproject.caretakers.caretakersapp.data.model.User;

/**
 * Created by Stephen on 2/18/2015.
 */
public interface UserService {

    @GET("/user")
    public Observable<List<User>> users();

    @POST("/user")
    public Observable<Login> create(@Body Registration registration);

    @POST("/user/login")
    public Observable<Login> login(@Body LoginRequest request);

    @GET("/user/{id}")
    public Observable<User> user(@Path("id") int id);

    // These calls are all authenticated
    @DELETE("/user/{id}")
    public void delete(@Path("id") int id);

    @PUT("/user/{id}/password")
    public void changePassword(@Path("id") int id,
                               @Body PasswordChange passwordChange);

    @PUT("/user/me")
    public void editDetails(@Body User user);

    @GET("/user/me")
    public Observable<User> me();

}
