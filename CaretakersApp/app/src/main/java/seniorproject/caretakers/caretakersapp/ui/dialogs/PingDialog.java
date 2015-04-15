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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.PingHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Availability;
import seniorproject.caretakers.caretakersapp.tempdata.model.Ping;

public class PingDialog extends DialogFragment implements
        TimePickerDialogFragment.TimePickerDialogHandler,
        RadialTimePickerDialog.OnTimeSetListener {

    EditText mTitleEdit;
    EditText mDescriptionEdit;
    EditText mTimeEdit;
    CheckBox mNowCheck;

    Integer mTimeHour, mTimeMinute;

    boolean mNowChecked;

    private View.OnClickListener mTimeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.time_edit:
                    openTimePicker(R.id.edit_start_time, mTimeHour, mTimeMinute);
                    break;
            }
        }
    };

    View.OnFocusChangeListener mTimeFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus) {
                switch(view.getId()) {
                    case R.id.time_edit:
                        openTimePicker(view.getId(), mTimeHour, mTimeMinute);
                        break;
                }
            }
        }
    };

    View.OnClickListener mNowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mNowChecked) {
                mNowChecked = false;
                mTimeEdit.setVisibility(View.VISIBLE);
            } else {
                mNowChecked = true;
                mTimeEdit.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mNowChecked = false;
        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ping, null);
        mTitleEdit = (EditText) view.findViewById(R.id.title_edit);
        mDescriptionEdit = (EditText) view.findViewById(R.id.description_edit);
        mTimeEdit = (EditText) view.findViewById(R.id.time_edit);
        mNowCheck = (CheckBox) view.findViewById(R.id.check_now);
        mNowCheck.setOnClickListener(mNowClickListener);
        mTimeEdit.setInputType(InputType.TYPE_NULL);
        mTimeEdit.setOnFocusChangeListener(mTimeFocusListener);
        mTimeEdit.setOnClickListener(mTimeClickListener);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_ping, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ((mTimeHour == null || mTimeMinute == null) && !mNowChecked) {
                    Toast.makeText(getActivity(), "Time not set!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mTitleEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Title not set!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Ping ping = new Ping();
                ping.title = mTitleEdit.getText().toString();
                ping.description = mDescriptionEdit.getText().toString();
                Calendar cal = Calendar.getInstance();
                if(!mNowChecked && (cal.get(Calendar.HOUR_OF_DAY) > mTimeHour ||
                        (cal.get(Calendar.HOUR_OF_DAY) == mTimeHour
                                && cal.get(Calendar.MINUTE) > mTimeMinute))) {
                    cal.add(Calendar.DATE, 1);
                    cal.set(Calendar.HOUR_OF_DAY, mTimeHour);
                    cal.set(Calendar.MINUTE, mTimeMinute);
                }
                ping.time = cal;
                PingHandler.getInstance(getActivity()).initiatePing(getActivity(), ping);
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PingDialog.this.getDialog().cancel();
            }
        });
        builder.setTitle(R.string.dialog_ping);
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
            case R.id.time_edit:
                mTimeEdit.setText(DateUtils
                        .formatDateTime(getActivity(), date.getTime(), DateUtils.FORMAT_SHOW_TIME));
                mTimeHour = hour;
                mTimeMinute = minute;
                return;
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int hour, int minute) {
        Bundle bundle = radialTimePickerDialog.getArguments();
        int reference = bundle.getInt("reference");
        onDialogTimeSet(reference, hour, minute);
    }

}
