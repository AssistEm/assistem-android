package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.presenters.ViewEventPresenter;
import seniorproject.caretakers.caretakersapp.ui.interfaces.EventView;
import seniorproject.caretakers.caretakersapp.ui.views.FloatingActionButton;

public class ViewEventActivity extends BaseActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener,
        EventView {

    @Inject
    ViewEventPresenter presenter;

    public final static int DONE_RESULT = 10;
    public final static int EVENT_EDITED_RESULT = 20;

    RelativeLayout mViewLayout;
    RelativeLayout mEditLayout;

    TextView mTitleText;
    TextView mDescriptionText;
    TextView mLocationText;
    TextView mCategoryText;
    TextView mTimeText;
    TextView mDateText;
    TextView mVolunteerText;
    TextView mPriorityText;
    FloatingActionButton mEditButton;
    Button mVolunteerButton;

    EditText mTitleEdit;
    EditText mDescriptionEdit;
    EditText mStartDateEdit;
    EditText mEndDateEdit;
    EditText mStartTimeEdit;
    EditText mEndTimeEdit;
    EditText mCategoryEdit;
    EditText mLocationEdit;
    Spinner mPrioritySpinner;

    Button mSubmitButton;

    Integer mStartYear, mStartMonth, mStartDay;
    Integer mEndYear, mEndMonth, mEndDay;

    Integer mStartHour, mStartMinute;
    Integer mEndHour, mEndMinute;

    Event mEvent;

    View.OnFocusChangeListener mDateFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                switch(view.getId()) {
                    case R.id.edit_start_date:
                        openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                        break;
                    case R.id.edit_end_date:
                        if(mEndYear == null && mEndMonth == null && mEndDay == null) {
                            openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                        } else {
                            openDatePicker(view.getId(), mEndYear, mEndMonth, mEndDay);
                        }
                        break;
                }
            }
        }
    };

    View.OnFocusChangeListener mTimeFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                switch(view.getId()) {
                    case R.id.edit_start_time:
                        openTimePicker(view.getId(), mStartHour, mStartMinute);
                        break;
                    case R.id.edit_end_time:
                        if(mEndHour == null && mEndMinute == null) {
                            openTimePicker(view.getId(), mStartHour, mStartMinute);
                        } else {
                            openTimePicker(view.getId(), mEndHour, mEndMinute);
                        }
                        break;
                }
            }
        }
    };

    View.OnClickListener mDateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.edit_start_date:
                    openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                    break;
                case R.id.edit_end_date:
                    if(mEndYear == null && mEndMonth == null && mEndDay == null) {
                        openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                    } else {
                        openDatePicker(view.getId(), mEndYear, mEndMonth, mEndDay);
                    }
            }
        }
    };

    View.OnClickListener mTimeClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.edit_start_time:
                    openTimePicker(view.getId(), mStartHour, mStartMinute);
                    break;
                case R.id.edit_end_time:
                    if(mEndHour == null && mEndMinute == null) {
                        openTimePicker(view.getId(), mStartHour, mStartMinute);
                    } else {
                        openTimePicker(view.getId(), mEndHour, mEndMinute);
                    }
                    break;
            }
        }
    };

    View.OnClickListener mSubmitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String title = mTitleEdit.getText().toString();
            String description = mDescriptionEdit.getText().toString();
            String location = mLocationEdit.getText().toString();
            String category = mCategoryEdit.getText().toString();
            int priority = mPrioritySpinner.getSelectedItemPosition() + 1;
            if(title.isEmpty() || description.isEmpty() || location.isEmpty() || category.isEmpty()
                    || mStartYear == null || mStartHour == null || mEndYear == null
                    || mEndHour == null) {
                return;
            }
            Calendar startTime = Calendar.getInstance();
            startTime.set(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute, 0);
            startTime.set(Calendar.MILLISECOND, 0);
            Calendar endTime = Calendar.getInstance();
            endTime.set(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute, 0);
            endTime.set(Calendar.MILLISECOND, 0);
            // TODO: Be careful about the calendar toString here, it's probably wrong
            Event event = new Event(mEvent.getApiId(),
                    title, description, location, category, priority,
                    startTime, endTime);
            presenter.updateEvent(event);
        }
    };

    View.OnClickListener mVolunteerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isVolunteer = mEvent.getVolunteer() == null;
            presenter.volunteer(isVolunteer);
        }
    };

    View.OnClickListener mEditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mViewLayout.setVisibility(View.GONE);
            mEditLayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(DONE_RESULT);
        setTitle("");
        mViewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        mEditLayout = (RelativeLayout) findViewById(R.id.edit_layout);
        mEvent = Parcels.unwrap(getIntent().getParcelableExtra("event"));
        mEvent.setApiId("1");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mEvent.getColor()));
        mTitleText.setBackgroundColor(mEvent.getColor());

        mTitleText = (TextView) findViewById(R.id.title);
        mDescriptionText = (TextView) findViewById(R.id.description);
        mTimeText = (TextView) findViewById(R.id.time);
        mDateText = (TextView) findViewById(R.id.date);
        mLocationText = (TextView) findViewById(R.id.location);
        mCategoryText = (TextView) findViewById(R.id.category);
        mPriorityText = (TextView) findViewById(R.id.priority);
        mVolunteerText = (TextView) findViewById(R.id.volunteer);
        mVolunteerButton = (Button) findViewById(R.id.volunteer_event);
        presenter.checkUserIsPatient();
        fillFields();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_view_event;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_event, menu);
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

    private void fillFields() {
        mTitleText.setText(mEvent.getTitle());
        mDescriptionText.setText(mEvent.getDescription());
        mLocationText.setText(mEvent.getLocation());
        mCategoryText.setText(mEvent.getCategory());
        mVolunteerText.setText(mEvent.getVolunteer().getDisplayName());
        mPriorityText.setText(mEvent.getPriorityString(this));
        Calendar startTime = mEvent.getStartTime();
        Calendar endTime = mEvent.getEndTime();
        String timeString = DateUtils
                .formatDateTime(this, startTime.getTime().getTime(), DateUtils.FORMAT_SHOW_TIME)
                + " to " + DateUtils.formatDateTime(this, endTime.getTime().getTime(),
                DateUtils.FORMAT_SHOW_TIME);
        String dateString;
        if(startTime.get(Calendar.DAY_OF_YEAR) == endTime.get(Calendar.DAY_OF_YEAR)
                && startTime.get(Calendar.YEAR) == endTime.get(Calendar.YEAR)) {
            dateString = (startTime.get(Calendar.MONTH) + 1) + "/"
                    + startTime.get(Calendar.DAY_OF_MONTH) + "/" + startTime.get(Calendar.YEAR);
        } else {
            dateString = (startTime.get(Calendar.MONTH) + 1) + "/"
                    + startTime.get(Calendar.DAY_OF_MONTH) + "/" + startTime.get(Calendar.YEAR) + " to "
                    + (endTime.get(Calendar.MONTH) + 1) + "/"
                    + endTime.get(Calendar.DAY_OF_MONTH) + "/" + endTime.get(Calendar.YEAR);
        }
        mTimeText.setText(timeString);
        mDateText.setText(dateString);
        presenter.checkEventVolunteered();
        mVolunteerButton.setOnClickListener(mVolunteerClickListener);
    }

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        switch(reference) {
            case R.id.edit_start_date:
                mStartDateEdit.setText((calendar.get(Calendar.MONTH) + 1) + "/"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
                mStartYear = year;
                mStartMonth = month;
                mStartDay = day;
                return;
            case R.id.edit_end_date:
                mEndDateEdit.setText((calendar.get(Calendar.MONTH) + 1) + "/"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
                mEndYear = year;
                mEndMonth = month;
                mEndDay = day;
                return;
        }
    }

    public void onDialogTimeSet(int reference, int hour, int minute) {
        Date date = new Date();
        date.setMinutes(minute);
        date.setHours(hour);
        switch(reference) {
            case R.id.edit_start_time:
                mStartTimeEdit.setText(DateUtils
                        .formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mStartHour = hour;
                mStartMinute = minute;
                return;
            case R.id.edit_end_time:
                mEndTimeEdit.setText(DateUtils
                        .formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mEndHour = hour;
                mEndMinute = minute;
                return;
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hour, int minute) {
        Bundle bundle = radialTimePickerDialog.getArguments();
        int reference = bundle.getInt("reference");
        onDialogTimeSet(reference, hour, minute);
    }

    private void openDatePicker(int reference, Integer year, Integer month, Integer day) {
        DatePickerBuilder builder = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.DateTimePickers)
                .setReference(reference)
                .addDatePickerDialogHandler(ViewEventActivity.this);
        if(year != null && month != null && day != null) {
            builder.setYear(year);
            builder.setMonthOfYear(month);
            builder.setDayOfMonth(day);
        }
        builder.show();
    }

    private void openTimePicker(int reference, Integer hour, Integer minute) {
        int hourInt = hour == null ? 0 : hour;
        int minuteInt = minute == null ? 0 : minute;
        Bundle bundle = new Bundle();
        bundle.putInt("reference", reference);
        RadialTimePickerDialog dialog = RadialTimePickerDialog
                .newInstance(this, hourInt, minuteInt, DateFormat.is24HourFormat(this));
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "radial_dialog");
    }

    @Override
    public void onPatient() {

        mTitleEdit = (EditText) findViewById(R.id.edit_title);
        mEditButton = (FloatingActionButton) findViewById(R.id.edit_button);
        mEditButton.setVisibility(View.VISIBLE);
        mEditButton.setOnClickListener(mEditClickListener);
        mDescriptionEdit = (EditText) findViewById(R.id.edit_description);
        mStartDateEdit = (EditText) findViewById(R.id.edit_start_date);
        mStartDateEdit.setInputType(InputType.TYPE_NULL);
        mStartDateEdit.setOnFocusChangeListener(mDateFocusListener);
        mStartDateEdit.setOnClickListener(mDateClickListener);
        mEndDateEdit = (EditText) findViewById(R.id.edit_end_date);
        mEndDateEdit.setInputType(InputType.TYPE_NULL);
        mEndDateEdit.setOnFocusChangeListener(mDateFocusListener);
        mEndDateEdit.setOnClickListener(mDateClickListener);
        mStartTimeEdit = (EditText) findViewById(R.id.edit_start_time);
        mStartTimeEdit.setInputType(InputType.TYPE_NULL);
        mStartTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mStartTimeEdit.setOnClickListener(mTimeClickListener);
        mEndTimeEdit = (EditText) findViewById(R.id.edit_end_time);
        mEndTimeEdit.setInputType(InputType.TYPE_NULL);
        mEndTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mEndTimeEdit.setOnClickListener(mTimeClickListener);
        mLocationEdit = (EditText) findViewById(R.id.edit_location);
        mCategoryEdit = (EditText) findViewById(R.id.edit_category);
        mPrioritySpinner = (Spinner) findViewById(R.id.edit_priority);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPrioritySpinner.setAdapter(spinnerAdapter);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(mSubmitClickListener);

        mTitleEdit.setText(mEvent.getTitle());
        mDescriptionEdit.setText(mEvent.getDescription());
        Calendar mStartDate = mEvent.getStartTime();
        Calendar mEndDate = mEvent.getEndTime();
        onDialogDateSet(R.id.edit_start_date, mStartDate.get(Calendar.YEAR),
                mStartDate.get(Calendar.MONTH), mStartDate.get(Calendar.DAY_OF_MONTH));
        onDialogTimeSet(R.id.edit_start_time, mStartDate.get(Calendar.HOUR_OF_DAY),
                mStartDate.get(Calendar.MINUTE));
        onDialogDateSet(R.id.edit_end_date, mEndDate.get(Calendar.YEAR),
                mEndDate.get(Calendar.MONTH), mEndDate.get(Calendar.DAY_OF_MONTH));
        onDialogTimeSet(R.id.edit_end_time, mEndDate.get(Calendar.HOUR_OF_DAY),
                mEndDate.get(Calendar.MINUTE));
        mLocationEdit.setText(mEvent.getLocation());
        mCategoryEdit.setText(mEvent.getCategory());
        mPrioritySpinner.setSelection(mEvent.getPriority() - 1);
    }

    @Override
    public void onVolunteeringStatus(boolean isVolunteer) {
        if (isVolunteer) {
            mVolunteerButton.setText(getResources().getString(R.string.view_event_unvolunteer));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9E9E9E")));
            mTitleText.setBackgroundColor(Color.parseColor("#9E9E9E"));
        } else {
            mVolunteerButton.setVisibility(View.GONE);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mEvent.getColor()));
            mTitleText.setBackgroundColor(mEvent.getColor());

        }
    }

    @Override
    public void onVolunteer() {
        Toast.makeText(ViewEventActivity.this, getString(R.string.event_volunteered_toast),
                Toast.LENGTH_SHORT).show();
        setResult(EVENT_EDITED_RESULT);
        finish();
    }

    @Override
    public void onError(String error) {
        Crouton.makeText(this, error, Style.ALERT).show();
    }

    @Override
    public void onEventUpdate() {
        Toast.makeText(ViewEventActivity.this, getString(R.string.event_edited_toast),
                Toast.LENGTH_SHORT).show();
        setResult(EVENT_EDITED_RESULT);
        finish();
    }

}
