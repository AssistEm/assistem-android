package seniorproject.caretakers.caretakersapp.data.model;

import android.content.Context;
import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;

/**
 * Created by Stephen on 2/18/2015.
 */
public class Event extends WeekViewEvent {

    @SerializedName("_id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("category")
    String category;

    @SerializedName("location")
    String location;

    @SerializedName("start_time")
    Calendar startTime;

    @SerializedName("end_time")
    Calendar endTime;

    @SerializedName("priority")
    int priority;

    @SerializedName("days_of_week")
    List<Integer> daysOfWeek;

    @SerializedName("weeks_to_repeat")
    int weeksToRepeat;

    @SerializedName("volunteer")
    User volunteer;

    public Event(String id, String title, String description, String location,
                 String category, int priority, Calendar startTime, Calendar endTime) {
        super(1, title, startTime, endTime);
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.priority = priority;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public String getCategory() {
        return category;
    }

    public int getColor() {
        if(priority == 3) {
            return Color.parseColor("#F44336");
        } else if(priority == 2) {
            return Color.parseColor("#FFC107");
        } else if(priority == 1) {
            return Color.parseColor("#8BC34A");
        } else {
            return Color.parseColor("#000000");
        }
    }

    public String getDescription() {
        return description;
    }

    public String getApiId() {
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

    public void setApiId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}