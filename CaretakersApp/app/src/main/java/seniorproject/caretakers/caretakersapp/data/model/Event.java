package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Stephen on 2/18/2015.
 */
public class Event {

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("start_time")
    String startTime;

    @SerializedName("end_time")
    String endTime;

    @SerializedName("days_of_week")
    List<Integer> daysOfWeek;

    @SerializedName("weeks_to_repeat")
    int weeksToRepeat;
}
