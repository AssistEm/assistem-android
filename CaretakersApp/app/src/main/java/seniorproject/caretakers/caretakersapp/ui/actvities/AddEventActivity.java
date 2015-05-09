package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.EventHandler;
import seniorproject.caretakers.caretakersapp.ui.dialogs.AddEventRepeatingDialog;

/**
 * Activity that presents the UI for adding an Event
 */
public class AddEventActivity extends BaseActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener {

    //Results for this activity that will be passed to calling activity
    public static final int EVENT_ADDED_RESULT = 10;
    public static final int CANCELED_RESULT = 20;

    EditText mTitleEdit;
    EditText mDescriptionEdit;
    EditText mStartDateEdit;
    EditText mEndDateEdit;
    EditText mStartTimeEdit;
    EditText mEndTimeEdit;
    EditText mCategoryEdit;
    EditText mLocationEdit;
    CheckBox mRepeatingCheck;
    Spinner mPrioritySpinner;

    Button mSubmitButton;

    //Currently selected start and end dates (may be null if none are selected)
    Integer mStartYear, mStartMonth, mStartDay;
    Integer mEndYear, mEndMonth, mEndDay;

    //Currently selected start and end times (may be null if none are selected)
    Integer mStartHour, mStartMinute;
    Integer mEndHour, mEndMinute;

    //If the event is set to be repeating
    boolean mRepeating;

    /**
     * EventListener for handling a successfully added event. Sets the result, notifies the user,
     * and ends the activity.
     */
    private EventHandler.EventListener mEventListener = new EventHandler.EventListener() {
        @Override
        public void onEventAdded() {
            Toast.makeText(AddEventActivity.this, getString(R.string.event_added_toast),
                    Toast.LENGTH_SHORT).show();
            setResult(EVENT_ADDED_RESULT);
            finish();
        }
    };

    /**
     * Listener for a focus change on a view. Used to open a date picker when the Date EditText is
     * selected
     */
    View.OnFocusChangeListener mDateFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                switch(view.getId()) {
                    case R.id.start_date:
                        openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                        break;
                    case R.id.end_date:
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

    /**
     * Listener for a focus change on a view. Used to open a time picker when the Time EditText is
     * selected
     */
    View.OnFocusChangeListener mTimeFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                switch(view.getId()) {
                    case R.id.start_time:
                        openTimePicker(view.getId(), mStartHour, mStartMinute);
                        break;
                    case R.id.end_time:
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

    /**
     * Listener for a click on a view. Used to open a date picker when the Date EditText is clicked.
     */
    View.OnClickListener mDateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.start_date:
                    openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                    break;
                case R.id.end_date:
                    if(mEndYear == null && mEndMonth == null && mEndDay == null) {
                        openDatePicker(view.getId(), mStartYear, mStartMonth, mStartDay);
                    } else {
                        openDatePicker(view.getId(), mEndYear, mEndMonth, mEndDay);
                    }
            }
        }
    };

    /**
     * Listener for a click on a view. Used to open a time picker when the Time EditText is clicked.
     */
    View.OnClickListener mTimeClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.start_time:
                    openTimePicker(view.getId(), mStartHour, mStartMinute);
                    break;
                case R.id.end_time:
                    if(mEndHour == null && mEndMinute == null) {
                        openTimePicker(view.getId(), mStartHour, mStartMinute);
                    } else {
                        openTimePicker(view.getId(), mEndHour, mEndMinute);
                    }
                    break;
            }
        }
    };

    /**
     * Click listener for the submit button. Validates the input, then either shows a repeating
     * dialog if the event is set to be repeating, or initiates a network request via the
     * EventHandler
     */
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
            if(mRepeating) {
                AddEventRepeatingDialog dialog = new AddEventRepeatingDialog();
                dialog.setRepeatingListener(mRepeatingListener);
                dialog.show(getSupportFragmentManager(), "repeating");
            } else {
                Calendar startTime = Calendar.getInstance();
                startTime.set(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute, 0);
                startTime.set(Calendar.MILLISECOND, 0);
                Calendar endTime = Calendar.getInstance();
                endTime.set(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute, 0);
                endTime.set(Calendar.MILLISECOND, 0);
                EventHandler.getInstance().addEvent(AddEventActivity.this, title, description,
                        location, category, priority, startTime, endTime, mEventListener);
            }
        }
    };

    /**
     * Click listener for the repeating checkbox. Toggles the boolean status of if the event is
     * repeating
     */
    View.OnClickListener mRepeatingClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mRepeating = !mRepeating;
        }
    };

    /**
     * Listener for when the RepeatingDialog is closed. Receives the result of the dialog.
     */
    AddEventRepeatingDialog.OnRepeatingListener mRepeatingListener =
            new AddEventRepeatingDialog.OnRepeatingListener() {
        @Override
        public void onRepeatingListener(List<Integer> days, int numberOfWeeks) {
            String title = mTitleEdit.getText().toString();
            String description = mDescriptionEdit.getText().toString();
            String location = mLocationEdit.getText().toString();
            String category = mCategoryEdit.getText().toString();
            int priority = mPrioritySpinner.getSelectedItemPosition() + 1;
            Calendar startTime = Calendar.getInstance();
            startTime.set(mStartYear, mStartMonth, mStartDay, mStartHour, mStartMinute, 0);
            startTime.set(Calendar.MILLISECOND, 0);
            Calendar endTime = Calendar.getInstance();
            endTime.set(mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute, 0);
            endTime.set(Calendar.MILLISECOND, 0);
            EventHandler.getInstance().addRepeatingEvent(AddEventActivity.this, title, description,
                    location, category, priority, startTime, endTime, days, numberOfWeeks, mEventListener);
        }
    };

    /**
     * Callback for when the activity is first created. Initializes and finds views.
     * @param savedInstanceState Saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(CANCELED_RESULT);
        setTitle(R.string.title_activity_add_event);
        mRepeating = false;
        mTitleEdit = (EditText) findViewById(R.id.title);
        mDescriptionEdit = (EditText) findViewById(R.id.description);
        mStartDateEdit = (EditText) findViewById(R.id.start_date);
        mStartDateEdit.setInputType(InputType.TYPE_NULL);
        mStartDateEdit.setOnFocusChangeListener(mDateFocusListener);
        mStartDateEdit.setOnClickListener(mDateClickListener);
        mEndDateEdit = (EditText) findViewById(R.id.end_date);
        mEndDateEdit.setInputType(InputType.TYPE_NULL);
        mEndDateEdit.setOnFocusChangeListener(mDateFocusListener);
        mEndDateEdit.setOnClickListener(mDateClickListener);
        mStartTimeEdit = (EditText) findViewById(R.id.start_time);
        mStartTimeEdit.setInputType(InputType.TYPE_NULL);
        mStartTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mStartTimeEdit.setOnClickListener(mTimeClickListener);
        mEndTimeEdit = (EditText) findViewById(R.id.end_time);
        mEndTimeEdit.setInputType(InputType.TYPE_NULL);
        mEndTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mEndTimeEdit.setOnClickListener(mTimeClickListener);
        mLocationEdit = (EditText) findViewById(R.id.location);
        mCategoryEdit = (EditText) findViewById(R.id.category);
        mRepeatingCheck = (CheckBox) findViewById(R.id.repeating_switch);
        mRepeatingCheck.setOnClickListener(mRepeatingClickListener);
        mPrioritySpinner = (Spinner) findViewById(R.id.priority);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPrioritySpinner.setAdapter(spinnerAdapter);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(mSubmitClickListener);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        boolean specific = extras.getBoolean("specific");
        Calendar calendar = (Calendar) extras.getSerializable("calendar");
        if(calendar != null) {
            if (specific) {
                onDialogDateSet(R.id.start_date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                onDialogTimeSet(R.id.start_time, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
                calendar.add(Calendar.HOUR, 1);
                onDialogDateSet(R.id.end_date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                onDialogTimeSet(R.id.end_time, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
            } else {
                onDialogDateSet(R.id.start_date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                onDialogDateSet(R.id.end_date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_event;
    }

    /**
     * Callback for when a DateDialog finishes. Receives the date set, and stores it and updates views
     * accordingly
     * @param reference Reference for the context in which the DateDialog was spawned
     * @param year Year selected
     * @param month Month selected
     * @param day Day selected
     */
    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        switch(reference) {
            case R.id.start_date:
                mStartDateEdit.setText((calendar.get(Calendar.MONTH) + 1) + "/"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
                mStartYear = year;
                mStartMonth = month;
                mStartDay = day;
                return;
            case R.id.end_date:
                mEndDateEdit.setText((calendar.get(Calendar.MONTH) + 1) + "/"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
                mEndYear = year;
                mEndMonth = month;
                mEndDay = day;
                return;
        }
    }

    /**
     * Callback for when a TimeDialog finishes. Receives the time set, and stores it and updates
     * views accordingly
     * @param reference Reference for the context in which the DateDialog was spawned
     * @param hour Hour selected
     * @param minute Minute selected
     */
    public void onDialogTimeSet(int reference, int hour, int minute) {
        Date date = new Date();
        date.setMinutes(minute);
        date.setHours(hour);
        switch(reference) {
            case R.id.start_time:
                mStartTimeEdit.setText(DateUtils
                        .formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mStartHour = hour;
                mStartMinute = minute;
                return;
            case R.id.end_time:
                mEndTimeEdit.setText(DateUtils
                        .formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mEndHour = hour;
                mEndMinute = minute;
                return;
        }
    }

    /**
     * Callback for the RadialTimePickerDialog library. Receives selected hour and minute and
     * passes them to the onDialogTimeSet method
     * @param radialTimePickerDialog Dialog reference
     * @param hour Hour selected
     * @param minute Minute selected
     */
    @Override
    public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hour, int minute) {
        Bundle bundle = radialTimePickerDialog.getArguments();
        int reference = bundle.getInt("reference");
        onDialogTimeSet(reference, hour, minute);
    }

    /**
     * Method to open the date picker dialog
     * @param reference Reference to use for this dialog
     * @param year Previously selected year
     * @param month Previously selected month
     * @param day Previously selected day
     */
    private void openDatePicker(int reference, Integer year, Integer month, Integer day) {
        DatePickerBuilder builder = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.DateTimePickers)
                .setReference(reference)
                .addDatePickerDialogHandler(AddEventActivity.this);
        if(year != null && month != null && day != null) {
            builder.setYear(year);
            builder.setMonthOfYear(month);
            builder.setDayOfMonth(day);
        }
        builder.show();
    }

    /**
     * Method to open the time picker dialog
     * @param reference Reference to use for this dialog
     * @param hour Previously selected hour
     * @param minute Previously selected minute
     */
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
}
