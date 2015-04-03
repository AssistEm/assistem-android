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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.presenters.CalendarPresenter;
import seniorproject.caretakers.caretakersapp.ui.actvities.AddEventActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.ViewEventActivity;
import seniorproject.caretakers.caretakersapp.ui.interfaces.CalendarView;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class CalendarFragment extends Fragment implements CalendarView {

    @Inject
    CalendarPresenter presenter;

    public static final int ADD_EVENT_REQUEST = 1;
    public static final int VIEW_EVENT_REQUEST = 2;

    WeekView mWeekView;

    AddFloatingActionButton mAddEventButton;

    HashMap<String, List<Event>> mEventsMap;

    HashSet<String> mEventCallsMade;

    WeekView.MonthChangeListener mMonthChangeListener = new WeekView.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            List<WeekViewEvent> list = new ArrayList<>();
            if(Calendar.getInstance().get(Calendar.MONTH) == newMonth) {
                String key = "" + newYear + " " + newMonth;
                if(!mEventsMap.containsKey(key) && !mEventCallsMade.contains(key)) {
                    if (getActivity() != null) {
                        mEventCallsMade.add(key);
                        presenter.events(newYear, newMonth);
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
        mEventsMap = new HashMap<>();
        mEventCallsMade = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, viewGroup, false);
        mAddEventButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_event_button);
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
        intent.putExtra("event", Parcels.wrap(event));
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
    public void isCaretaker(boolean isCaretaker) {
        if(isCaretaker) {
            mAddEventButton.setVisibility(View.GONE);
        } else {
            mAddEventButton.setVisibility(View.VISIBLE);
            mAddEventButton.setOnClickListener(mAddEventClickListener);
        }
    }

    @Override
    public void onEventsReceived(List<Event> events, int year, int month) {
        String key = "" + year + " " + month;
        mEventsMap.put(key, events);
        mEventCallsMade.remove(key);
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public void onRetrieveEventsFailed(String error) {
        Crouton.makeText(this.getActivity(), error, Style.ALERT).show();
    }
}
