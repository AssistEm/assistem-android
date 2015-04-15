package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 4/14/2015.
 */
public class DeleteRepeating {

    @SerializedName("delete_repeating")
    boolean deleteRepeating;

    public DeleteRepeating(boolean deleteRepeating) {
        this.deleteRepeating = deleteRepeating;
    }
}
