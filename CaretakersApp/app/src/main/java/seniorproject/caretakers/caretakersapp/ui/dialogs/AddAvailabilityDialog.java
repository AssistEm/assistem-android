package seniorproject.caretakers.caretakersapp.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import java.util.Date;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.Availability;
import seniorproject.caretakers.caretakersapp.views.AddAvailabilityView;

public class AddAvailabilityDialog extends DialogFragment implements
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener,
        AddAvailabilityView {

    Spinner mStartDaySpinner;
    Spinner mEndDaySpinner;
    EditText mStartTimeEdit;
    EditText mEndTimeEdit;

    Integer mStartHour, mStartMinute;
    Integer mEndHour, mEndMinute;

    private View.OnClickListener mTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.edit_start_time:
                    openTimePicker(R.id.edit_start_time, mStartHour, mStartMinute);
                    break;
                case R.id.edit_end_time:
                    openTimePicker(R.id.edit_end_time, mEndHour, mEndMinute);
                    break;
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_availability, null);
        mStartDaySpinner = (Spinner) view.findViewById(R.id.start_day_of_week_spinner);
        ArrayAdapter<CharSequence> startAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStartDaySpinner.setAdapter(startAdapter);
        mEndDaySpinner = (Spinner) view.findViewById(R.id.end_day_of_week_spinner);
        ArrayAdapter<CharSequence> endAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEndDaySpinner.setAdapter(endAdapter);
        mStartTimeEdit = (EditText) view.findViewById(R.id.edit_start_time);
        mStartTimeEdit.setInputType(InputType.TYPE_NULL);
        mStartTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mStartTimeEdit.setOnClickListener(mTimeClickListener);
        mEndTimeEdit = (EditText) view.findViewById(R.id.edit_end_time);
        mEndTimeEdit.setInputType(InputType.TYPE_NULL);
        mEndTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mEndTimeEdit.setOnClickListener(mTimeClickListener);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mStartHour == null || mEndHour == null) {
                    Toast.makeText(getActivity(), "Times not set!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Availability avail = new Availability(mStartHour, mStartMinute,
                                                      mEndHour, mEndMinute,
                                                      mStartDaySpinner.getSelectedItem(), mEndDaySpinner.getSelectedItem());
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AddAvailabilityDialog.this.getDialog().cancel();
            }
        });
        builder.setTitle(R.string.add_availability);
        return builder.create();
    }

    private void openTimePicker(int reference, Integer hour, Integer minute) {
        int hourInt = hour == null ? 0 : hour;
        int minuteInt = minute == null ? 0 : minute;
        Bundle bundle = new Bundle();
        bundle.putInt("reference", reference);
        RadialTimePickerDialog dialog = RadialTimePickerDialog
                .newInstance(this, hourInt, minuteInt, DateFormat.is24HourFormat(getActivity()));
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "radial_dialog");
    }

    @Override
    public void onDialogTimeSet(int reference, int hour, int minute) {
        Date date = new Date();
        date.setMinutes(minute);
        date.setHours(hour);
        switch(reference) {
            case R.id.edit_start_time:
                mStartTimeEdit.setText(DateUtils
                        .formatDateTime(getActivity(), date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mStartHour = hour;
                mStartMinute = minute;
                return;
            case R.id.edit_end_time:
                mEndTimeEdit.setText(DateUtils
                        .formatDateTime(getActivity(), date.getTime(), DateUtils.FORMAT_SHOW_TIME));
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

    @Override
    public void onAvailabilityAdded(Availability availability) {

    }
}
