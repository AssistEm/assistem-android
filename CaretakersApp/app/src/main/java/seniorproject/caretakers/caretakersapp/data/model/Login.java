package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 2/18/2015.
 */
public class Login {

    @SerializedName("token")
    String auth;

    @SerializedName("user")
    User user;
}
