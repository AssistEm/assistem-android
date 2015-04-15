package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Stephen on 4/10/2015.
 */
public class PostRequest {

    @SerializedName("status_code")
    int statusCode;

    public int getStatusCode() {
        return statusCode;
    }
}
