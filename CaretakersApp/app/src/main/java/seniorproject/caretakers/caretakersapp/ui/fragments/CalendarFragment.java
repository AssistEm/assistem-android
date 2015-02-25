package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, viewGroup, false);
        mAddEventButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_event_button);
        if(AccountHandler.getInstance(getActivity()).getCurrentUser() instanceof Caretaker) {
            mAddEventButton.setVisibility(View.GONE);
        } else if(AccountHandler.getInstance(getActivity()).getCurrentUser() instanceof Patient){
            mAddEventButton.setVisibility(View.VISIBLE);
            mAddEventButton.setOnClickListener(mAddEventClickListener);
        }
        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        mWeekView.setOnEventClickListener(mEventClickListener);
        mWeekView.setEmptyViewClickListener(mEmptyClickListener);
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
}
