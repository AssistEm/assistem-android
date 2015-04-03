package seniorproject.caretakers.caretakersapp.ui.interfaces;

import java.util.List;

import seniorproject.caretakers.caretakersapp.data.model.Event;

/**
 * Created by Stephen on 3/31/2015.
 */
public interface CalendarView extends ViewInterface {

    void isCaretaker(boolean isCaretaker);

    void onEventsReceived(List<Event> events, int year, int month);

    void onRetrieveEventsFailed(String error);

}
