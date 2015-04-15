package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Stephen on 4/10/2015.
 */
public class CaretakerInfo {

    @SerializedName("availability")
    List<Availability> availabilityList;

}
