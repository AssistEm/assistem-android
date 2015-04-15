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

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.presenters.AddEventPresenter;
import seniorproject.caretakers.caretakersapp.views.AddEventView;

public class AddEventActivity extends BaseActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener,
        AddEventView {

    @Inject
    AddEventPresenter presenter;

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
    Spinner mPrioritySpinner;

    Button mSubmitButton;

    Integer mStartYear, mStartMonth, mStartDay;
    Integer mEndYear, mEndMonth, mEndDay;

    Integer mStartHour, mStartMinute;
    Integer mEndHour, mEndMinute;

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
            // TODO: Watch the toString
            Event event = new Event(null, title, description, location, category,
                                    priority, startTime, endTime);
            presenter.addEvent(event);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(CANCELED_RESULT);
        setTitle(R.string.title_activity_add_event);
        CaretakersApplication app = (CaretakersApplication) this.getApplication();
        app.inject(this);
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
                .addDatePickerDialogHandler(AddEventActivity.this);
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
        /*
        TimePickerBuilder builder = new TimePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.DateTimePickers)
                .setReference(reference)
                .addTimePickerDialogHandler(AddEventActivity.this);
        if(hour != null && minute != null) {
            builder.
        }
        builder.show();
        */
    }

    @Override
    public void onEventAdded() {
        Toast.makeText(AddEventActivity.this, getString(R.string.event_added_toast),
                Toast.LENGTH_SHORT).show();
        setResult(EVENT_ADDED_RESULT);
        finish();
    }

    @Override
    public void onError(String error) {
        Crouton.makeText(this, error, Style.ALERT).show();
    }
}
