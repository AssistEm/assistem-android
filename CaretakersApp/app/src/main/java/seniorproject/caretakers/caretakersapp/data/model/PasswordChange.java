package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 2/18/2015.
 */
public class PasswordChange {

    @SerializedName("oldPass")
    String oldPassword;

    @SerializedName("newPass")
    String newPassword;
}
