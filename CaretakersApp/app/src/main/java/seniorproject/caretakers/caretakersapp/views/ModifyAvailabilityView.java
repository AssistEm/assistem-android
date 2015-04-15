package seniorproject.caretakers.caretakersapp.views;

import java.util.List;

import seniorproject.caretakers.caretakersapp.data.model.Availability;

/**
 * Created by Stephen on 4/9/2015.
 */
public interface ModifyAvailabilityView extends ViewInterface {

    public void onReceiveAvailability(List<Availability> availabilities);

    public void onAvailabilitySet();
}
