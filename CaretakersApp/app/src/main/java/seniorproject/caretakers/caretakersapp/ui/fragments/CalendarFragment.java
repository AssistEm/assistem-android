package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.data.handlers.EventHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Caretaker;
import seniorproject.caretakers.caretakersapp.tempdata.model.Event;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.actvities.AddEventActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.ViewEventActivity;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class CalendarFragment extends Fragment {

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int VIEW_EVENT_REQUEST = 2;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    WeekView mWeekView;

    AddFloatingActionButton mAddEventButton;

    HashMap<String, List<Event>> mEventsMap;

    HashSet<String> mEventCallsMade;

    EventHandler.EventListener mEventListener = new EventHandler.EventListener() {

        @Override
        public void onEventsFetched(List<Event> events, int year, int month) {
            String key = "" + year + " " + month;
            mEventsMap.put(key, events);
            mEventCallsMade.remove(key);
            mWeekView.notifyDatasetChanged();
        }
    };

    WeekView.MonthChangeListener mMonthChangeListener = new WeekView.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            List<WeekViewEvent> list = new ArrayList<>();
            if(Calendar.getInstance().get(Calendar.MONTH) == newMonth) {
                String key = "" + newYear + " " + newMonth;
                if(!mEventsMap.containsKey(key) && !mEventCallsMade.contains(key)) {
                    if (getActivity() != null) {
                        mEventCallsMade.add(key);
                        EventHandler.getInstance().getEvents(getActivity(), newYear, newMonth,
                                mEventListener);
                    }
                } else if(mEventsMap.containsKey(key)) {
                    list.addAll(mEventsMap.get(key));
                }
            }
            return list;
        }
    };

    WeekView.EventClickListener mEventClickListener = new WeekView.EventClickListener() {
        @Override
        public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {
            viewEvent((Event) weekViewEvent);
        }
    };

    WeekView.EmptyViewClickListener mEmptyClickListener = new WeekView.EmptyViewClickListener() {
        @Override
        public void onEmptyViewClicked(Calendar calendar) {
            addEvent(calendar, true);
        }
    };

    View.OnClickListener mAddEventClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addEvent(mWeekView.getFirstVisibleDay(), false);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventsMap = new HashMap<String, List<Event>>();
        mEventCallsMade = new HashSet<>();
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_calendar, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_calendar, viewGroup, false);
        mAddEventButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_event_button);
        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        mWeekView.setOnEventClickListener(mEventClickListener);
        mWeekView.setNumberOfVisibleDays(3);
        if(AccountHandler.getInstance(getActivity()).getCurrentUser() instanceof Caretaker) {
            mAddEventButton.setVisibility(View.GONE);
        } else if(AccountHandler.getInstance(getActivity()).getCurrentUser() instanceof Patient){
            mAddEventButton.setVisibility(View.VISIBLE);
            mAddEventButton.setOnClickListener(mAddEventClickListener);
            mWeekView.setEmptyViewClickListener(mEmptyClickListener);
        }
        mWeekView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                boolean moved = false;
                double currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                currHour += Calendar.getInstance().get(Calendar.MINUTE) / 60.0;
                while (!moved) {
                    try {
                        mWeekView.goToHour(currHour);
                        moved = true;
                    } catch (IllegalArgumentException e) {
                        moved = false;
                        currHour -= .10;
                    }
                }
                mWeekView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mEventsMap = null;
        mEventCallsMade = null;
    }

    public void refreshEvents() {
        if (mEventsMap != null) {
            mEventsMap.clear();
        }
        if (mEventCallsMade != null) {
            mEventCallsMade.clear();
        }
        if (mWeekView != null) {
            mWeekView.notifyDatasetChanged();
        }
    }

    private void addEvent(Calendar calendar, boolean timeSpecific) {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        intent.putExtra("calendar", calendar);
        intent.putExtra("specific", timeSpecific);
        startActivityForResult(intent, ADD_EVENT_REQUEST);
    }

    private void viewEvent(Event event) {
        Intent intent = new Intent(getActivity(), ViewEventActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("start_time", event.getStartTime());
        intent.putExtra("end_time", event.getEndTime());
        startActivityForResult(intent, VIEW_EVENT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CalendarFragment.ADD_EVENT_REQUEST) {
            switch (resultCode) {
                case AddEventActivity.EVENT_ADDED_RESULT:
                    refreshEvents();
                    break;
            }
        } else if(requestCode == CalendarFragment.VIEW_EVENT_REQUEST) {
            switch(resultCode) {
                case ViewEventActivity.EVENT_EDITED_RESULT:
                    refreshEvents();
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshEvents();
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
