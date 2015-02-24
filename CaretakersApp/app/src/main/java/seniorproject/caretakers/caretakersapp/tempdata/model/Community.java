package seniorproject.caretakers.caretakersapp.tempdata.model;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static Community parseCommunity(JSONObject communityObject) throws JSONException {
        String id = communityObject.getString("_id");
        String name = communityObject.getString("name");
        String patientId = communityObject.getString("patient");
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