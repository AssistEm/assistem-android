package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.data.handlers.GroceryHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;

public class ViewGroceryItemActivity extends BaseActivity implements
        DatePickerDialogFragment.DatePickerDialogHandler,
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener {

    public final static int DONE_RESULT = 10;
    public final static int GROCERY_ITEM_EDITED_RESULT = 20;

    private final static int VOLUNTEER_TIME_REF = -1000;
    private final static int VOLUNTEER_DATE_REF = -2000;

    RelativeLayout mEditLayout;
    RelativeLayout mViewLayout;

    EditText mTitleEdit;
    EditText mDescriptionEdit;
    EditText mQuantityEdit;
    EditText mLocationEdit;
    EditText mTimeEdit;
    EditText mDateEdit;

    TextView mTitle;
    TextView mDescription;
    TextView mQuantity;
    TextView mLocation;
    TextView mTime;
    TextView mDate;
    TextView mVolunteer;

    Button mSubmitButton;
    Button mDeleteButton;
    Button mVolunteerButton;
    Button mEditButton;

    Integer mHour, mMinute;
    Integer mYear, mMonth, mDay;

    Integer mVolunteerHour, mVolunteerMinute;
    Integer mVolunteerYear, mVolunteerMonth, mVolunteerDay;

    GroceryItem mItem;

    boolean mEditOpen;

    GroceryHandler.GroceryListener mListener = new GroceryHandler.GroceryListener() {
        @Override
        public void onGroceryItemVolunteered() {
            setResult(GROCERY_ITEM_EDITED_RESULT);
            finish();
        }

        @Override
        public void onGroceryItemDeleted() {
            setResult(GROCERY_ITEM_EDITED_RESULT);
            finish();
        }

        @Override
        public void onGroceryItemEdited() {
            setResult(GROCERY_ITEM_EDITED_RESULT);
            finish();
        }
    };

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
            GroceryHandler.getInstance().editItem(ViewGroceryItemActivity.this, mItem.getId(),
                    title, description, quantity, location, time, mListener);
        }
    };

    View.OnClickListener mVolunteerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mItem.getVolunteer() == null || !
                    mItem.getVolunteer().getId()
                            .equalsIgnoreCase(AccountHandler
                                    .getInstance(ViewGroceryItemActivity.this)
                                    .getCurrentUser().getId())) {
                openTimePicker(VOLUNTEER_TIME_REF, null, null);
            } else {
                GroceryHandler.getInstance().volunteerItem(ViewGroceryItemActivity.this, mItem.getId(),
                        false, Calendar.getInstance(), mListener);
            }
        }
    };

    View.OnClickListener mDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GroceryHandler.getInstance().deleteItem(ViewGroceryItemActivity.this, mItem.getId(),
                    mListener);
        }
    };

    View.OnClickListener mEditClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mEditLayout.setVisibility(View.VISIBLE);
            mViewLayout.setVisibility(View.GONE);
            mEditOpen = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(DONE_RESULT);
        mEditOpen = false;
        mItem = (GroceryItem) getIntent().getSerializableExtra("item");
        mEditLayout = (RelativeLayout) findViewById(R.id.edit_layout);
        mViewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        mTitle = (TextView) findViewById(R.id.view_title);
        mTitle.setText(mItem.getTitle());
        mDescription = (TextView) findViewById(R.id.view_description);
        fillOrHideView(mDescription, mItem.getDescription());
        mQuantity = (TextView) findViewById(R.id.view_quantity);
        fillOrHideView(mQuantity, mItem.getQuantity());
        mLocation = (TextView) findViewById(R.id.view_location);
        fillOrHideView(mLocation, mItem.getLocation());
        Calendar time = mItem.getUrgency();
        mVolunteer = (TextView) findViewById(R.id.view_volunteer);
        String volunteerName = null;
        if(mItem.getVolunteer() != null) {
            volunteerName = mItem.getVolunteer().getDisplayName();
        } else {
            Log.i("VOLUNTEER NULL", "VOLUNTEER NULL");
        }
        fillOrHideView(mVolunteer, volunteerName);
        mTime = (TextView) findViewById(R.id.view_time);
        mTime.setText(DateUtils
                .formatDateTime(this, time.getTime().getTime(), DateUtils.FORMAT_SHOW_TIME));
        mDate = (TextView) findViewById(R.id.view_date);
        mDate.setText((time.get(Calendar.MONTH) + 1) + "/"
                + time.get(Calendar.DAY_OF_MONTH) + "/" + time.get(Calendar.YEAR));
        mEditButton = (Button) findViewById(R.id.edit_button);
        mEditButton.setOnClickListener(mEditClickListener);
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(mSubmitClickListener);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(mDeleteClickListener);
        mVolunteerButton = (Button) findViewById(R.id.volunteer_button);
        mVolunteerButton.setOnClickListener(mVolunteerClickListener);
        if(mItem.getVolunteer() == null) {
            mVolunteerButton.setVisibility(View.VISIBLE);
            mVolunteerButton.setText(getString(R.string.view_grocery_item_volunteer));
        } else if(mItem.getVolunteer().getId()
                .equalsIgnoreCase(AccountHandler
                        .getInstance(ViewGroceryItemActivity.this).getCurrentUser().getId())) {
            mVolunteerButton.setVisibility(View.VISIBLE);
            mVolunteerButton.setText(getString(R.string.view_grocery_item_unvolunteer));
        } else {
            mVolunteerButton.setVisibility(View.GONE);
        }
        if(AccountHandler.getInstance(this).getCurrentUser() instanceof Patient) {
            mEditButton.setVisibility(View.VISIBLE);
            mTitleEdit = (EditText) findViewById(R.id.title);
            mTitleEdit.setText(mItem.getTitle());
            mDescriptionEdit = (EditText) findViewById(R.id.description);
            mDescriptionEdit.setText(mItem.getDescription());
            mQuantityEdit = (EditText) findViewById(R.id.quantity);
            mQuantityEdit.setText(mItem.getQuantity());
            mLocationEdit = (EditText) findViewById(R.id.location);
            mTimeEdit = (EditText) findViewById(R.id.time);
            mTimeEdit.setInputType(InputType.TYPE_NULL);
            mTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
            mTimeEdit.setOnClickListener(mTimeClickListener);
            mTimeEdit.setText(DateUtils
                    .formatDateTime(this, time.getTime().getTime(), DateUtils.FORMAT_SHOW_TIME));
            mDateEdit = (EditText) findViewById(R.id.date);
            mDateEdit.setInputType(InputType.TYPE_NULL);
            mDateEdit.setOnFocusChangeListener(mDateFocusListener);
            mDateEdit.setOnClickListener(mDateClickListener);
            mDateEdit.setText((time.get(Calendar.MONTH) + 1) + "/"
                    + time.get(Calendar.DAY_OF_MONTH) + "/" + time.get(Calendar.YEAR));
            mHour = time.get(Calendar.HOUR);
            mMinute = time.get(Calendar.MINUTE);
            mMonth = time.get(Calendar.MONTH);
            mDay = time.get(Calendar.DAY_OF_MONTH);
            mYear = time.get(Calendar.DAY_OF_YEAR);
        } else {
            mEditButton.setVisibility(View.GONE);
        }
    }

    private void fillOrHideView(TextView view, String value) {
        if(value == null || value.isEmpty()) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(value);
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_view_grocery_item;
    }

    @Override
    public void onBackPressed() {
        if(mEditOpen) {
            mViewLayout.setVisibility(View.VISIBLE);
            mEditLayout.setVisibility(View.GONE);
            mEditOpen = false;
        } else {
            super.onBackPressed();
        }
    }

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
            case VOLUNTEER_DATE_REF:
                mVolunteerYear = year;
                mVolunteerMonth = month;
                mVolunteerDay = day;
                Calendar deliveryTime = Calendar.getInstance();
                deliveryTime.set(Calendar.HOUR, mVolunteerHour);
                deliveryTime.set(Calendar.MINUTE, mVolunteerMinute);
                deliveryTime.set(Calendar.YEAR, mVolunteerYear);
                deliveryTime.set(Calendar.MONTH, mVolunteerMonth);
                deliveryTime.set(Calendar.DAY_OF_MONTH, mVolunteerDay);
                if(deliveryTime.after(mItem.getUrgency())) {
                    Toast.makeText(this, "Delivery date cannot be after urgency!", Toast.LENGTH_SHORT).show();
                    return;
                }
                GroceryHandler.getInstance().volunteerItem(ViewGroceryItemActivity.this, mItem.getId(),
                        true, deliveryTime, mListener);
        }
    }

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
                break;
            case VOLUNTEER_TIME_REF:
                mVolunteerHour = hour;
                mVolunteerMinute = minute;
                openDatePicker(VOLUNTEER_DATE_REF, null, null, null);
                break;
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
                .addDatePickerDialogHandler(ViewGroceryItemActivity.this);
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
        /*TimePickerBuilder builder = new TimePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.DateTimePickers)
                .setReference(reference)
                .addTimePickerDialogHandler(AddEventActivity.this);
        if(hour != null && minute != null) {
            builder.
        }
        builder.show();*/
    }
}
