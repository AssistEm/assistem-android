package seniorproject.caretakers.caretakersapp.tempdata.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public abstract class User implements Serializable {

    protected String mId;
    protected String mFirstName;
    protected String mLastName;
    protected String mEmail;
    protected String mPhone;

    public User(String id, String firstName, String lastName, String email, String phone) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
        mPhone = phone;
    }

    public static User parseUser(JSONObject userObject) throws JSONException {
        Log.i("USEROBJECT", userObject.toString());
        String id = userObject.getString("_id");
        String firstName = userObject.getString("first_name");
        String lastName = userObject.getString("last_name");
        String phone = userObject.getString("phone");
        JSONObject loginObj = userObject.getJSONObject("login_info");
        String email = loginObj.getString("email");
        String type = userObject.getString("type");

        return parseUser(id, firstName, lastName, email, phone, type);
    }

    public static User parseUser(String id, String firstName, String lastName, String email, String phone,
                          String type) {
        switch (type) {
            case Caretaker.CARETAKER_TYPE:
                return new Caretaker(id, firstName, lastName, email, phone);
            case Patient.PATIENT_TYPE:
                return new Patient(id, firstName, lastName, email, phone);
            default:
                return null;
        }
    }

    public String getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getDisplayName() {
        return mFirstName + " " + mLastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhone() {
        return mPhone;
    }

    public abstract String getType();

}
