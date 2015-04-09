package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;

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
