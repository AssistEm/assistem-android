package seniorproject.caretakers.caretakersapp.tempdata.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.json.JSONArray;

/**
 * Community class that represents the Community model
 */
public class Community {

    private String mId;
    private String mName;
    private String mPatientId;
    private String mPrimaryId;
    private ArrayList<String> mCaretakers;

    public Community(String id, String name, String patientId, String primaryId, ArrayList<String> caretakers) {
        mId = id;
        mName = name;
        mPatientId = patientId;
        mCaretakers = caretakers;
        mPrimaryId = primaryId;
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
        ArrayList<String> caretakers = new ArrayList<>();
        JSONArray jsonArray = communityObject.getJSONArray("caretakers");
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                caretakers.add(jsonArray.get(i).toString());
            }
        } else {
            caretakers.add("No caretakers");
        }
        String primaryId = "No primary caretaker";
        if (communityObject.has("primary_caretaker")) {
            primaryId = communityObject.getString("primary_caretaker");
        }
        return new Community(id, name, patientId, primaryId, caretakers);
    }

    public String getId() { return mId; }

    public String getName() {
        return mName;
    }

    public String getPatientId() { return mPatientId; }

    public String getPrimary() { return mPrimaryId; }

    public ArrayList<String> getCaretakers() { return mCaretakers; }
}