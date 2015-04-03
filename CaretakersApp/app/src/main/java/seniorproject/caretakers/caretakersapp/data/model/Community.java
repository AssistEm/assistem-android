package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Stephen on 2/18/2015.
 */
public class Community extends ApiItem {

    @SerializedName("_id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("patient")
    String patientId;

    @SerializedName("caretakers")
    List<String> caretakers;

    @SerializedName("privacy")
    boolean privacy;

    @SerializedName("query")
    String query;

    public Community(String query) {
        this.query = query;
    }

    public Community(String name, boolean privacy) {
        this.name = name;
        this.privacy = privacy;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
