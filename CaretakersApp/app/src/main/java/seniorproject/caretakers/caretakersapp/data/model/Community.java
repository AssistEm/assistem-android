package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Stephen on 2/18/2015.
 */
public class Community extends ApiItem {

    @SerializedName("name")
    String name;

    @SerializedName("patient")
    String patientId;

    @SerializedName("caretakers")
    List<String> caretakers;

    @SerializedName("privacy")
    boolean privacy;

    @SerializedName("primary")
    String caretakerId;
}
