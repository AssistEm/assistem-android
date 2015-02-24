package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.ui.actvities.AddEventActivity;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class CalendarFragment extends Fragment {

    WeekView mWeekView;

    AddFloatingActionButton mAddEventButton;

    WeekView.MonthChangeListener mMonthChangeListener = new WeekView.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            List<WeekViewEvent> list = new ArrayList<>();
            if(Calendar.getInstance().get(Calendar.MONTH) == newMonth) {
            }
            return list;
        }
    };

    WeekView.EventClickListener mEventClickListener = new WeekView.EventClickListener() {
        @Override
        public void onEventClick(WeekViewEvent weekViewEvent, RectF rectF) {

        }
    };

    WeekView.EmptyViewClickListener mEmptyClickListener = new WeekView.EmptyViewClickListener() {
        @Override
        public void onEmptyViewClicked(Calendar calendar) {
            addEvent(calendar);
        }
    };

    View.OnClickListener mAddEventClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addEvent(mWeekView.getFirstVisibleDay());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, viewGroup, false);
        mAddEventButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_event_button);
        mAddEventButton.setOnClickListener(mAddEventClickListener);
        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        mWeekView.setOnEventClickListener(mEventClickListener);
        mWeekView.setEmptyViewClickListener(mEmptyClickListener);
        return rootView;
    }

    private void addEvent(Calendar calendar) {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        startActivity(intent);
    }
}
