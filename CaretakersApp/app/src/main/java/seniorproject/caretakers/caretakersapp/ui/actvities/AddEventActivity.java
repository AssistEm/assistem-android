package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateUtils;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import seniorproject.caretakers.caretakersapp.R;

public class AddEventActivity extends BaseActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        TimePickerDialogFragment.TimePickerDialogHandler {

    EditText mTitleEdit;
    EditText mDescriptionEdit;
    EditText mDateEdit;
    EditText mTimeEdit;
    EditText mLocationEdit;
    Spinner mPrioritySpinner;

    View.OnFocusChangeListener mDateFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                openDatePicker();
            }
        }
    };

    View.OnFocusChangeListener mTimeFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                openTimePicker();
            }
        }
    };

    View.OnClickListener mDateClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            openDatePicker();
        }
    };

    View.OnClickListener mTimeClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            openTimePicker();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_add_event);
        mTitleEdit = (EditText) findViewById(R.id.title);
        mDescriptionEdit = (EditText) findViewById(R.id.description);
        mDateEdit = (EditText) findViewById(R.id.date);
        mDateEdit.setInputType(InputType.TYPE_NULL);
        mDateEdit.setOnFocusChangeListener(mDateFocusListener);
        mDateEdit.setOnClickListener(mDateClickListener);
        mTimeEdit = (EditText) findViewById(R.id.time);
        mTimeEdit.setInputType(InputType.TYPE_NULL);
        mTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mTimeEdit.setOnClickListener(mTimeClickListener);
        mLocationEdit = (EditText) findViewById(R.id.location);
        mPrioritySpinner = (Spinner) findViewById(R.id.priority);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPrioritySpinner.setAdapter(spinnerAdapter);
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
        mDateEdit.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " "
                + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR));
    }

    @Override
    public void onDialogTimeSet(int reference, int hour, int minute) {
        Date date = new Date();
        date.setMinutes(minute);
        date.setHours(hour);
        mTimeEdit.setText(DateUtils
                .formatDateTime(this, date.getTime(), DateUtils.FORMAT_SHOW_TIME));
    }

    private void openDatePicker() {
        DatePickerBuilder builder = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.DateTimePickers)
                .addDatePickerDialogHandler(AddEventActivity.this);
        builder.show();
    }

    private void openTimePicker() {
        TimePickerBuilder builder = new TimePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.DateTimePickers)
                .addTimePickerDialogHandler(AddEventActivity.this);
        builder.show();
    }
}
