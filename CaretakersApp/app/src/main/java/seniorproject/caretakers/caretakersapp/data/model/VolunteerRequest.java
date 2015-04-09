package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 4/3/2015.
 */
public class VolunteerRequest {

    @SerializedName("volunteer")
    boolean isVolunteer;

    public VolunteerRequest(boolean isVolunteer) {
        this.isVolunteer = isVolunteer;
    }
}
