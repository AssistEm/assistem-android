package seniorproject.caretakers.caretakersapp.data.handlers;

import android.content.Context;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import seniorproject.caretakers.caretakersapp.tempdata.apis.BaseJsonResponseHandler;
import seniorproject.caretakers.caretakersapp.tempdata.apis.EventRestClient;
import seniorproject.caretakers.caretakersapp.tempdata.apis.NoNetworkException;
import seniorproject.caretakers.caretakersapp.tempdata.model.Event;

/**
 * Handler class for events. Offers a singleton class for executing requests for and receiving Event
 * objects.
 */
public class EventHandler {

    private static EventHandler mInstance;

    private EventHandler() {

    }

    /**
     * Singleton getInstance method. Either returns an existing instance of the EventHandler class
     * or constructs, sets and returns a new instance.
     * @return An instance of an EventHandler
     */
    public static EventHandler getInstance() {
        if(mInstance == null) {
            mInstance = new EventHandler();
        }
        return mInstance;
    }

    /**
     * Callback class for parsing the response from a request to get all the events from a
     * particular month. Includes an instance of an EventListener observer as well as the year and
     * integer month of the request.
     */
    private class GetEventsResponseHandler extends BaseJsonResponseHandler {
        EventListener mListener;
        int mYear;
        int mMonth;

        public GetEventsResponseHandler(EventListener listener, int year, int month) {
            mListener = listener;
            mYear = year;
            mMonth = month;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            List<Event> eventList = new ArrayList<>();
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject eventObject = response.getJSONObject(i);
                    eventList.add(Event.parseEvent(eventObject));
                }
                if(mListener != null) {
                    mListener.onEventsFetched(eventList, mYear, mMonth);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Callback class for parsing the response from a request to add an Event. Includes an instance
     * of an EventListener observer to notify.
     */
    private class AddEventsResponseHandler extends BaseJsonResponseHandler {
        EventListener mListener;

        public AddEventsResponseHandler(EventListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            if(mListener != null) {
                mListener.onEventAdded();
            }
        }
    }

    /**
     * Callback class for parsing the response from a request that updates an Event. Includes an
     * instance of an EventListener observer to notify.
     */
    private class UpdateEventsResponseHandler extends BaseJsonResponseHandler {
        EventListener mListener;

        public UpdateEventsResponseHandler(EventListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if(mListener != null) {
                mListener.onEventUpdated();
            }
        }
    }

    /**
     * Callback class for parsing the response from a request to volunteer for an Event.
     * Includes an EventListener observer to notify.
     */
    private class VolunteerEventResponseHandler extends BaseJsonResponseHandler {
        EventListener mListener;

        public VolunteerEventResponseHandler(EventListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if(mListener != null) {
                mListener.onEventVolunteered();
            }
        }
    }

    /**
     * Callback class for parsing the response from a request to delete an event.
     * Includes an EventListener observer to notify.
     */
    private class DeleteEventResponseHandler extends BaseJsonResponseHandler {
        EventListener mListener;

        public DeleteEventResponseHandler(EventListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if(mListener != null) {
                mListener.onEventDeleted();
            }
        }
    }

    /**
     * Public method to initiate a request to fetch the events for a particular year and month
     * @param context Context to execute the call in
     * @param year Year of the month
     * @param month Integer value of the month
     * @param listener EventListener to notify of the result
     */
    public void getEvents(Context context, int year, int month,
                          EventListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        Calendar calendar = Calendar.getInstance();
        int yearDiff = year - calendar.get(Calendar.YEAR);
        int monthDiff = month - calendar.get(Calendar.MONTH);
        int monthsAgo = (yearDiff * 12) + monthDiff;
        try {
            EventRestClient.getEvents(context, communityId, monthsAgo,
                    new GetEventsResponseHandler(listener, year, month));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to create an event.
     * @param context Context to execute the call in
     * @param title Title of the event
     * @param description Description of the event
     * @param location Location of the event
     * @param category Category of the event
     * @param priority Integer priority of the event
     * @param startTime Start time of the event as a Calendar object
     * @param endTime End time of the event as a Calendar object
     * @param listener EventListener to notify of the result
     */
    public void addEvent(Context context, String title, String description, String location,
                         String category, int priority, Calendar startTime, Calendar endTime,
                         EventListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String startString = format.format(startTime.getTime());
        String endString = format.format(endTime.getTime());
        try {
            EventRestClient.addEvent(context, communityId, title, description, location, category,
                    priority, startString, endString, new AddEventsResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to create a set of repeating events.
     * @param context Context to execute the call in
     * @param title Title of the event
     * @param description Description of the event
     * @param location Location of the event
     * @param category Category of the event
     * @param priority Integer priority of the event
     * @param startTime Start time of the event as a Calendar object
     * @param endTime End time of the event as a Calendar object
     * @param daysOfWeek List of integer valued days of the week
     * @param numberOfRepeats Number of weeks to repeat the event
     * @param listener EventListener to notify of the result
     */
    public void addRepeatingEvent(Context context, String title, String description, String location,
                                  String category, int priority, Calendar startTime, Calendar endTime,
                                  List<Integer> daysOfWeek, int numberOfRepeats,
                                  EventListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String startString = format.format(startTime.getTime());
        String endString = format.format(endTime.getTime());
        try {
            EventRestClient.addRepeatingEvent(context, communityId, title, description, location, category,
                    priority, startString, endString, daysOfWeek, numberOfRepeats, new AddEventsResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to update an event
     * @param context Context to initiate the call in
     * @param eventId Id of the event to update
     * @param title Title of the event
     * @param description Description of the event
     * @param location Location of the event
     * @param category Category of the event
     * @param priority Integer valued priority of the event
     * @param startTime Start time of the event as a Calendar object
     * @param endTime End time of the event as a Calendar object
     * @param listener EventListener to notify
     */
    public void updateEvent(Context context, String eventId, String title, String description,
                            String location, String category, int priority, Calendar startTime,
                            Calendar endTime, EventListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String startString = format.format(startTime.getTime());
        String endString = format.format(endTime.getTime());
        try {
            EventRestClient.updateEvent(context, communityId, eventId, title, description, location, category,
                    priority, startString, endString, new UpdateEventsResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to volunteer for an event.
     * @param context Context to execute the call in
     * @param eventId Id of the event to volunteer for
     * @param volunteer Boolean value to represent whether this is a volunteer or unvolunteer request
     * @param listener EventListener to notify
     */
    public void volunteerEvent(Context context, String eventId, boolean volunteer,
                               EventListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            EventRestClient.volunteerForEvent(context, communityId, eventId, volunteer,
                    new VolunteerEventResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to delete an event
     * @param context Context to execute the call in
     * @param eventId Id of the event to delete
     * @param deleteRepeating Boolean value to indicate if the user wishes to delete all the events
     *                        in a repeating series
     * @param listener EventListener to notify
     */
    public void deleteEvent(Context context, String eventId, boolean deleteRepeating,
                            EventListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            EventRestClient.deleteEvent(context, communityId, eventId, deleteRepeating,
                    new DeleteEventResponseHandler(listener));
        } catch(NoNetworkException e) {
            e.printStackTrace();
        }
    }

    //Observer class for the EventHandler
    public static abstract class EventListener {
        public void onEventsFetched(List<Event> events, int year, int month) { }
        public void onEventAdded() { }
        public void onEventVolunteered() { }
        public void onEventUpdated() { }
        public void onEventDeleted() { }
    }
}
