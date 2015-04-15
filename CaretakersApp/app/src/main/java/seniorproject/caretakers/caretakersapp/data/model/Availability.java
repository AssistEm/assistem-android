package seniorproject.caretakers.caretakersapp.data.model;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Stephen on 4/10/2015.
 */
public class Availability {

    @SerializedName("start")
    Day startDay;

    @SerializedName("end")
    Day endDay;

    public String getDisplayDay() {
        int startDayNum = startDay.getCalendar().get(Calendar.DAY_OF_YEAR);
        int endDayNum =  endDay.getCalendar().get(Calendar.DAY_OF_YEAR);
        if(startDayNum == endDayNum) {
            return getDayFromInt(startDayNum);
        } else {
            return getDayFromInt(startDayNum) + " to " + getDayFromInt(endDayNum);
        }
    }

    public String getDisplayTime(Context context) {
        return getTime(context, startDay.getCalendar()) + " to " + getTime(context, endDay.getCalendar());
    }

    private static String getTime(Context context, Calendar calendar) {
        calendar.getTime();
        int minute = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        Date date = new Date();
        date.setMinutes(minute);
        date.setHours(hour);
        return DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
    }

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
