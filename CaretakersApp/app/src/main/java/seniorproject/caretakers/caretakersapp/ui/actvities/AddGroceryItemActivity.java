package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.EventHandler;
import seniorproject.caretakers.caretakersapp.data.handlers.GroceryHandler;

/**
 * Activity that presents the UI for adding a GroceryItem
 */
public class AddGroceryItemActivity extends BaseActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener {

    //Results for this activity that will be passed to calling activity
    public static final int GROCERY_ITEM_ADDED_RESULT = 10;

    EditText mTitleEdit;
    EditText mDescriptionEdit;
    EditText mQuantityEdit;
    EditText mLocationEdit;
    EditText mTimeEdit;
    EditText mDateEdit;

    Button mSubmitButton;

    //Currently selected start and end times and date (may be null if none are selected)
    Integer mHour, mMinute;
    Integer mYear, mMonth, mDay;

    /**
     * Listener for a focus change on a view. Used to open a date picker when the Date EditText is
     * selected
     */
    View.OnFocusChangeListener mDateFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                switch(view.getId()) {
                    case R.id.date:
                        openDatePicker(view.getId(), mYear, mMonth, mDay);
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
                    case R.id.time:
                        openTimePicker(view.getId(), mHour, mMinute);
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
                case R.id.date:
                    openDatePicker(view.getId(), mYear, mMonth, mDay);
                    break;
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
                    openTimePicker(view.getId(), mHour, mMinute);
                    break;
            }
        }
    };

    /**
     * Click listener for the submit button. Validates the input, the initiates a request to add
     * a grocery item via the GroceryHandler singleton
     */
    View.OnClickListener mSubmitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String title = mTitleEdit.getText().toString();
            String description = mDescriptionEdit.getText().toString();
            String location = mLocationEdit.getText().toString();
            String quantity = mQuantityEdit.getText().toString();
            if(title.isEmpty() || mYear == null || mHour == null) {
                return;
            }
            Calendar time = Calendar.getInstance();
            time.set(mYear, mMonth, mDay, mHour, mMinute, 0);
            time.set(Calendar.MILLISECOND, 0);
            GroceryHandler.getInstance().addItem(AddGroceryItemActivity.this, title, description,
                    quantity, location, time, mListener);
        }
    };

    /**
     * GroceryListener for receiving a successful GroceryItem addition request
     */
    GroceryHandler.GroceryListener mListener = new GroceryHandler.GroceryListener() {

        @Override
        public void onGroceryItemAdded() {
            setResult(GROCERY_ITEM_ADDED_RESULT);
            finish();
        }

    };

    /**
     * Callback for when the activity is first created. Initializes and finds views.
     * @param savedInstanceState Saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleEdit = (EditText) findViewById(R.id.title);
        mDescriptionEdit = (EditText) findViewById(R.id.description);
        mQuantityEdit = (EditText) findViewById(R.id.quantity);
        mLocationEdit = (EditText) findViewById(R.id.location);
        mTimeEdit = (EditText) findViewById(R.id.time);
        mTimeEdit.setInputType(InputType.TYPE_NULL);
        mTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mTimeEdit.setOnClickListener(mTimeClickListener);
        mDateEdit = (EditText) findViewById(R.id.date);
        mDateEdit.setInputType(InputType.TYPE_NULL);
        mDateEdit.setOnFocusChangeListener(mDateFocusListener);
        mDateEdit.setOnClickListener(mDateClickListener);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(mSubmitClickListener);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_add_grocery_item;
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
            case R.id.date:
                mDateEdit.setText((calendar.get(Calendar.MONTH) + 1) + "/"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
                mYear = year;
                mMonth = month;
                mDay = day;
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
            case R.id.time:
                mTimeEdit.setText(DateUtils
                        .formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mHour = hour;
                mMinute = minute;
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
                .addDatePickerDialogHandler(AddGroceryItemActivity.this);
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
