package seniorproject.caretakers.caretakersapp.data.model;

import android.content.Context;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import seniorproject.caretakers.caretakersapp.R;

/**
 * Created by Stephen on 2/18/2015.
 */
@Parcel
public class Event extends ApiItem {

    public final static String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("category")
    String category;

    @SerializedName("location")
    String location;

    @SerializedName("start_time")
    String startTime;

    @SerializedName("end_time")
    String endTime;

    @SerializedName("priority")
    int priority;

    @SerializedName("days_of_week")
    List<Integer> daysOfWeek;

    @SerializedName("weeks_to_repeat")
    int weeksToRepeat;

    @SerializedName("volunteer")
    User volunteer;

    public Calendar getStartTime() {
        return parseTime(startTime);
    }

    public Calendar getEndTime() {
        return parseTime(endTime);
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public int getPriority() {
        return priority;
    }

    public String getPriorityString(Context context) {
        String[] priorities = context.getResources().getStringArray(R.array.priority_array);
        if(priority > 0 && priority <= priorities.length) {
            return priorities[priority - 1];
        }
        return "";
    }

    public String getTitle() {
        return title;
    }

    public User getVolunteer() {
        return volunteer;
    }

    private Calendar parseTime(String time) {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // TODO: Probably refactor this
    public WeekViewEvent toWeekViewEvent() {
        return new WeekViewEvent(1, title, getStartTime(), getEndTime());
    }

}