package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 2/18/2015.
 */
public class Login {

    @SerializedName("token")
    String token;

    @SerializedName("user")
    User user;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
