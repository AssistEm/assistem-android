package seniorproject.caretakers.caretakersapp.tempdata.model;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

/**
 * Class that represents the Event model
 */
public class Event extends WeekViewEvent implements Serializable {

    //Date format string for the ISO8601 standard for time strings
    public final static String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private String mId;
    private String mTitle;
    private String mLocation;
    private String mCategory;
    private String mDescription;
    private int mPriority;
    private User mVolunteer;
    private boolean mRepeating;

    private Event() {

    }

    public Event(String id, String title, Calendar startTime, Calendar endTime,
                 String location, String category, String description, int priority,
                 User volunteer, boolean repeating) {
        super(1, title, startTime, endTime);
        mId = id;
        mTitle = title;
        mLocation = location;
        mCategory = category;
        mDescription = description;
        mPriority = priority;
        mVolunteer = volunteer;
        mRepeating = repeating;
    }

    /**
     * Static method to parse a JSON object into an Event object
     * @param eventObject JSON representation of the Event object
     * @return Event representation of the parsed input
     * @throws JSONException Thrown when the input is not of the expected format
     */
    public static Event parseEvent(JSONObject eventObject) throws JSONException {
        Log.i("EVENT OBJECT", eventObject.toString());
        String id = eventObject.getString("_id");
        String title = eventObject.getString("title");
        String description = eventObject.getString("description");
        String category = eventObject.getString("category");
        String location = eventObject.getString("location");
        int priority = eventObject.getInt("priority");
        JSONObject timeObject = eventObject.getJSONObject("time");
        boolean repeating = timeObject.has("weeks_to_repeat");
        String startTime = timeObject.getString("start");
        String endTime = timeObject.getString("end");
        Calendar start = parseTime(startTime);
        Calendar end = parseTime(endTime);
        User user = null;
        if(eventObject.has("volunteer") && !eventObject.isNull("volunteer")) {
            JSONObject volunteerObject = eventObject.getJSONObject("volunteer");
            user = User.parseUser(volunteerObject);
        }
        return new Event(id, title, start, end, location, category, description, priority,
                user, repeating);
    }

    /**
     * Static method to parse a time string into a Calendar instance
     * @param time Time string to parse
     * @return Calendar instance from the time string
     */
    private static Calendar parseTime(String time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()) ;
        SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601DATEFORMAT, Locale.getDefault());
        try {
            Date date = dateFormat.parse(time);
            date.setHours(date.getHours());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Method to get the name of the event, used for the Android Week View library to display
     * titles for events
     * @return String for the name of the event
     */
    @Override
    public String getName() {
        String text = super.getName();
        if(mLocation != null) {
            text += "\n" + mLocation;
        }
        if(mVolunteer != null) {
            text += "\n" + mVolunteer.getDisplayName();
        }
        return text;
    }

    /**
     * Method to get the color of the event, used for the Android Week View library to display
     * the background color of the event
     * @return The integer color representation
     */
    @Override
    public int getColor() {
        if(AccountHandler.getExistingInstance() == null) {
            return super.getColor();
        }
        User mCurrentUser = AccountHandler.getExistingInstance().getCurrentUser();
        if(mCurrentUser instanceof Caretaker && mVolunteer != null && !mVolunteer.getId().equals(mCurrentUser.getId())) {
            return Color.parseColor("#9E9E9E");
        } else {
            if(mPriority == 3) {
                return Color.parseColor("#F44336");
            } else if(mPriority == 2) {
                return Color.parseColor("#FFC107");
            } else if(mPriority == 1) {
                return Color.parseColor("#8BC34A");
            } else {
                return super.getColor();
            }
        }
    }

    public String getStringId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getCategory() {
        return mCategory;
    }

    public int getPriority() {
        return mPriority;
    }

    public boolean getRepeating() {
        return mRepeating;
    }

    public String getPriorityString(Context context) {
        String[] priorities = context.getResources().getStringArray(R.array.priority_array);
        if(mPriority > 0 && mPriority <= priorities.length) {
            return priorities[mPriority - 1];
        }
        return "";
    }

    public User getVolunteer() {
        return mVolunteer;
    }

    public String getVolunteerName() {
        if(mVolunteer != null) {
            return mVolunteer.getDisplayName();
        } else {
            return "None";
        }
    }

}
