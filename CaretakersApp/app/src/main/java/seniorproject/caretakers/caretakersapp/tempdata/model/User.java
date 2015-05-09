package seniorproject.caretakers.caretakersapp.tempdata.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Abstract class that represents an Assist'em user
 */
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

    /**
     * Static method to parse a JSON object into a User object
     * @param userObject JSON representation of the Use object
     * @return User instance representing the parsed input (will be of a subclass)
     * @throws JSONException Thrown when the input is not of the expected format
     */
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

    /**
     * Static method to parse input into an instance of a User, which will be of a subclass
     * @param id Id of the user
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param email Email of the user
     * @param phone Phone number of the user
     * @param type Type key of the user
     * @return User instance
     */
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
