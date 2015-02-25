package seniorproject.caretakers.caretakersapp.tempdata.model;

import java.io.Serializable;

/**
 * Created by Jason on 2/24/15.
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