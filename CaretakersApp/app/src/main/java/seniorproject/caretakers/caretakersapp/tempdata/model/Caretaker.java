package seniorproject.caretakers.caretakersapp.tempdata.model;

import java.io.Serializable;

public class Caretaker extends User implements Serializable {

    public final static String CARETAKER_TYPE = "caretaker";

    public Caretaker(String id, String firstName, String lastName, String email, String phone) {
        super(id, firstName, lastName, email, phone);
    }

    @Override
    public String getType() {
        return CARETAKER_TYPE;
    }

}
