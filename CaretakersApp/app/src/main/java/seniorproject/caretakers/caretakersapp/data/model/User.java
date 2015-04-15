package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Stephen on 2/18/2015.
 */
public class User extends ApiItem {

    private static final String CARETAKER_TYPE = "caretaker";
    private static final String PATIENT_TYPE = "patient";

    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    @SerializedName("phone")
    String phone;

    @SerializedName("type")
    String type;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("community")
    Community community;

    public User(String email, String password,
                String phone, boolean isCaretaker,
                String firstName, String lastName,
                String communitySearch, String communityName) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;

        if(isCaretaker) {
            type = CARETAKER_TYPE;
            community = new Community(communitySearch);
        } else  {
            type = PATIENT_TYPE;
            community = new Community(communityName, false);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Community getCommunity() {
        return community;
    }

    public String getDisplayName() {
        return firstName + " " + lastName;
    }

    public String getId() {
        return id;
    }

    public boolean isPatient() {
        return type.equals(PATIENT_TYPE);
    }
}
