package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 3/13/2015.
 */
public class Registration {

    @SerializedName("user")
    User user;

    @SerializedName("community")
    Community community;

    public Registration(User user, Community community) {
        this.user = user;
        this.community = community;
    }
}
