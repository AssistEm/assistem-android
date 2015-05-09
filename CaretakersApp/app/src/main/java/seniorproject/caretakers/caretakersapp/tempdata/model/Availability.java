package seniorproject.caretakers.caretakersapp.tempdata.model;

import android.content.Context;
import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Model which represents an Availability time period for a user.
 */
public class Availability {

    public int startDay;
    public int endDay;
    public int startHour;
    public int startMinute;
    public int endHour;
    public int endMinute;

    /**
     * Static method that parses an Availability object from JSON data
     * @param object JSONObject that should be parsed
     * @return The Availability object that was parsed out
     * @throws JSONException Thrown if the JSONObject does not match the expected format
     */
    public static Availability parseAvailability(JSONObject object) throws JSONException {
        Availability avail = new Availability();
        JSONObject startObject = object.getJSONObject("start");
        JSONObject endObject = object.getJSONObject("end");
        avail.startDay = startObject.getInt("day_of_week");
        avail.endDay = endObject.getInt("day_of_week");
        Calendar startTime = parseTime(startObject.getString("time"));
        Calendar endTime = parseTime(endObject.getString("time"));
        avail.startHour = startTime.get(Calendar.HOUR_OF_DAY);
        avail.startMinute = startTime.get(Calendar.MINUTE);
        avail.endHour = endTime.get(Calendar.HOUR_OF_DAY);
        avail.endMinute = endTime.get(Calendar.MINUTE);
        return avail;
    }

    /**
     * Parse a time string into a Calendar instance
     * @param time Time string to parse
     * @return A Calendar instance
     */
    private static Calendar parseTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(time));
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getStartDay() {
        return getDayFromInt(startDay);
    }

    public String getEndDay() {
        return getDayFromInt(endDay);
    }

    public String getStartTime(Context context) {
        return getTime(context, startHour, startMinute);
    }

    public String getEndTime(Context context) {
        return getTime(context, endHour, endMinute);
    }

    /**
     * Method to get a String representation of this object's day.
     * @return String representation of this object's day
     */
    public String getDisplayDay() {
        if(startDay == endDay) {
            return getStartDay();
        } else {
            return getStartDay() + " to " + getEndDay();
        }
    }

    /**
     * Method to get a String representation of this object's time.
     * @return String representation of this object's time
     */
    public String getDisplayTime(Context context) {
        return getStartTime(context) + " to " + getEndTime(context);
    }

    /**
     * Method to turn an Availability object into a JSONObject
     * @return A JSONObject representing the Availability object
     * @throws JSONException Should not be thrown
     */
    public JSONObject toJson() throws JSONException {
        JSONObject object = new JSONObject();
        JSONObject start = new JSONObject();
        JSONObject end = new JSONObject();
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMinute);
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMinute);
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        start.put("day_of_week", startDay);
        start.put("time", format.format(startTime.getTime()));
        end.put("day_of_week", endDay);
        end.put("time", format.format(endTime.getTime()));
        object.put("start", start);
        object.put("end", end);
        return object;
    }

    /**
     * Static method to get a formatted time string from an hour and minute
     * @param context Context in which to generate the time string
     * @param hour Hour of time
     * @param minute Minute of time
     * @return Time string representing the time
     */
    private static String getTime(Context context, int hour, int minute) {
        Date date = new Date();
        date.setMinutes(minute);
        date.setHours(hour);
        return DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
    }

    /**
     * Static method to get the String day of the week representation from the integer value
     * @param day Integer representation of the day of the week
     * @return The String value of the integer representation
     */
    private static String getDayFromInt(int day) {
        switch(day) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 0:
                return "Sunday";
            default:
                return "Sunday";
        }
    }

}
