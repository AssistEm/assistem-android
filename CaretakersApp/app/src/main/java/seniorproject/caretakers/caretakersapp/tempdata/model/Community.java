package seniorproject.caretakers.caretakersapp.tempdata.model;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

/**
 * Community class that represents the Community model
 */
public class Community {

    private String mId;
    private String mName;
    private String mPatientId;
    private Patient mPatient;

    public Community(String id, String name, String patientId) {
        mId = id;
        mName = name;
        mPatientId = patientId;
    }

    /**
     * Static method that parses a JSON representation of a community object
     * @param communityObject The JSON representation of the community object
     * @return A Community instance from the parsed input
     * @throws JSONException Thrown if the input is not of the expected format
     */
    public static Community parseCommunity(JSONObject communityObject) throws JSONException {
        String id = communityObject.getString("_id");
        String name = communityObject.getString("name");
        String patientId = communityObject.getString("patient");
        Log.d("COMMUNITY", communityObject.getString("caretakers"));
        return new Community(id, name, patientId);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPatientId() {
        return mPatientId;
    }
}