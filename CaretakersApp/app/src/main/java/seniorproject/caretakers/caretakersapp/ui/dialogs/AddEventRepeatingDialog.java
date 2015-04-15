package seniorproject.caretakers.caretakersapp.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import java.util.ArrayList;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;

public class AddEventRepeatingDialog extends DialogFragment {

    CheckBox mMonday;
    CheckBox mTuesday;
    CheckBox mWednesday;
    CheckBox mThursday;
    CheckBox mFriday;
    CheckBox mSaturday;
    CheckBox mSunday;
    EditText mDaysOfWeek;

    boolean mM, mT, mW, mR, mF, mS, mU;

    OnRepeatingListener mListener;

    View.OnClickListener mWeekClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == mMonday) {
                mM = !mM;
            } else if (view == mTuesday) {
                mT = !mT;
            } else if (view == mWednesday) {
                mW = !mW;
            } else if (view == mThursday) {
                mR = !mR;
            } else if (view == mFriday) {
                mF = !mF;
            } else if (view == mSaturday) {
                mS = !mS;
            } else if (view == mSunday) {
                mU = !mU;
            }
        }
    };

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mM = false;
        mT = false;
        mW = false;
        mR = false;
        mF = false;
        mS = false;
        mU = false;
        View view = inflater.inflate(R.layout.dialog_repeating_event, null);
        mMonday = (CheckBox) view.findViewById(R.id.monday_check);
        mMonday.setOnClickListener(mWeekClickListener);
        mTuesday = (CheckBox) view.findViewById(R.id.tuesday_check);
        mTuesday.setOnClickListener(mWeekClickListener);
        mWednesday = (CheckBox) view.findViewById(R.id.wednesday_check);
        mWednesday.setOnClickListener(mWeekClickListener);
        mThursday = (CheckBox) view.findViewById(R.id.thursday_check);
        mThursday.setOnClickListener(mWeekClickListener);
        mFriday = (CheckBox) view.findViewById(R.id.friday_check);
        mFriday.setOnClickListener(mWeekClickListener);
        mSaturday = (CheckBox) view.findViewById(R.id.saturday_check);
        mSaturday.setOnClickListener(mWeekClickListener);
        mSunday = (CheckBox) view.findViewById(R.id.sunday_check);
        mSunday.setOnClickListener(mWeekClickListener);
        mDaysOfWeek = (EditText) view.findViewById(R.id.number_of_weeks_edit);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_submit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int times = 1;
                if(!mDaysOfWeek.getText().toString().isEmpty()) {
                    times = Integer.parseInt(mDaysOfWeek.getText().toString());
                }
                List<Integer> daysOfWeek = new ArrayList<Integer>();
                if(mM) {
                    daysOfWeek.add(1);
                }
                if(mT) {
                    daysOfWeek.add(2);
                }
                if(mW) {
                    daysOfWeek.add(3);
                }
                if(mR) {
                    daysOfWeek.add(4);
                }
                if(mF) {
                    daysOfWeek.add(5);
                }
                if(mS) {
                    daysOfWeek.add(6);
                }
                if(mU) {
                    daysOfWeek.add(0);
                }
                if(mListener != null) {
                    mListener.onRepeatingListener(daysOfWeek, times);
                }
            }
        });
        builder.setTitle(R.string.dialog_add_repeating);
        return builder.show();
    }

    public void setRepeatingListener(OnRepeatingListener listener) {
        mListener = listener;
    }

    public void removeRepeatingListener() {
        mListener = null;
    }

    public interface OnRepeatingListener {
        void onRepeatingListener(List<Integer> days, int numberOfWeeks);
    }
}
