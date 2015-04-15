package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

/**
 * Created by Stephen on 4/10/2015.
 */
public class Day {

    @SerializedName("day_of_week")
    int dayOfWeek;

    @SerializedName("time")
    Calendar time;

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public Calendar getCalendar() {
        return time;
    }
}
