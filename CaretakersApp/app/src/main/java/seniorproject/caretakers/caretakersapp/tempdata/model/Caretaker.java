package seniorproject.caretakers.caretakersapp.tempdata.model;

import java.io.Serializable;

/**
 * Subclass of the User class which represents a Caretaker object
 */
public class Caretaker extends User implements Serializable {

    //String server key for a Caretaker
    public final static String CARETAKER_TYPE = "caretaker";

    public Caretaker(String id, String firstName, String lastName, String email, String phone) {
        super(id, firstName, lastName, email, phone);
    }

    @Override
    public String getType() {
        return CARETAKER_TYPE;
    }

}
