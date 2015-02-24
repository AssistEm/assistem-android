package seniorproject.caretakers.caretakersapp.ui.events;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

//import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
//import seniorproject.caretakers.caretakersapp.objects.Caretaker;
//import seniorproject.caretakers.caretakersapp.objects.User;

public class ScheduleEvent extends WeekViewEvent {

    /*private String mLocation;
    private String mCategory;
    private String mDescription;
    private int mPriority;
    private User mVolunteer;

    public ScheduleEvent() {

    }

    public ScheduleEvent(long id, String name, Calendar startTime, Calendar endTime, String location,
                         String category, String description, int priority, User volunteer) {
        super(id, name, startTime, endTime);
        mLocation = location;
        mCategory = category;
        mDescription = description;
        mPriority = priority;
        mVolunteer = volunteer;
    }

    @Override
    public String getName() {
        String text = super.getName();
        if(mLocation != null) {
            text += "\n" + mLocation;
        }
        if(mVolunteer != null) {
            text += "\n" + mVolunteer.getUserName();
        }
        return text;
    }

    @Override
    public int getColor() {
        User mCurrentUser = AccountHandler.getInstance().getCurrentUser();
        if(mCurrentUser instanceof Caretaker && mVolunteer != null && mVolunteer != mCurrentUser) {
            return Color.GRAY;
        } else {
            if(mPriority == 1) {
                return Color.RED;
            } else if(mPriority == 2) {
                return Color.YELLOW;
            } else if(mPriority == 3) {
                return Color.GREEN;
            } else {
                return super.getColor();
            }
        }
    }*/
}
