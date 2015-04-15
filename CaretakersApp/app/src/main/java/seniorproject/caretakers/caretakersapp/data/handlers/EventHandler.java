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

public class EventHandler {

    private static EventHandler mInstance;

    private EventHandler() {

    }

    public static EventHandler getInstance() {
        if(mInstance == null) {
            mInstance = new EventHandler();
        }
        return mInstance;
    }

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

    public static abstract class EventListener {
        public void onEventsFetched(List<Event> events, int year, int month) { }
        public void onEventAdded() { }
        public void onEventVolunteered() { }
        public void onEventUpdated() { }
        public void onEventDeleted() { }
    }
}
