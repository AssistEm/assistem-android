package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.graphics.RectF;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class CalendarActivity extends ActionBarActivity {

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

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setMonthChangeListener(mMonthChangeListener);
        mWeekView.setOnEventClickListener(mEventClickListener);
        mWeekView.setEmptyViewClickListener(mEmptyClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
