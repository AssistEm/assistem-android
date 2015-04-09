package seniorproject.caretakers.caretakersapp.ui.interfaces;

import seniorproject.caretakers.caretakersapp.data.model.Event;

/**
 * Created by Stephen on 4/1/2015.
 */
public interface EventView extends ViewInterface {

    public void onError(String error);

    public void onEventUpdate();

    public void onPatient();

    public void onVolunteeringStatus(boolean isVolunteering);

    public void onVolunteer();
}
