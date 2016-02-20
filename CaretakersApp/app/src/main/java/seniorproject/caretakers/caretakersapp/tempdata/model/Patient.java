package seniorproject.caretakers.caretakersapp.tempdata.model;

import java.io.Serializable;

/**
 * Subclass of the User class which represents a Patient object
 */
public class Patient extends User implements Serializable {

    public final static String PATIENT_TYPE = "patient";

    public Patient(String id, String firstName, String lastName, String email, String phone) {
        super(id, firstName, lastName, email, phone);
    }

    @Override
    public String getType() {
        return PATIENT_TYPE;
    }
}