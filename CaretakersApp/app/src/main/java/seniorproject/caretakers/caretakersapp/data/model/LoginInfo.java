package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 3/4/2015.
 */
public class LoginInfo {

    @SerializedName("email")
    String email;

    public String getEmail() {
        return email;
    }

}
