package seniorproject.caretakers.caretakersapp.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import seniorproject.caretakers.caretakersapp.R;

public class DeleteRepeatingEventDialog extends DialogFragment {

    private OnRepeatingListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        builder.setTitle(R.string.delete_repeating_question);
        builder.setMessage(R.string.delete_repeating_question_full);
        builder.setNegativeButton(R.string.delete_single, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mListener != null) {
                    mListener.onDelete(false);
                }
            }
        });
        builder.setNeutralButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mListener != null) {
                    mListener.onCancel();
                }
            }
        });
        builder.setPositiveButton(R.string.delete_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mListener != null) {
                    mListener.onDelete(true);
                }
            }
        });
        return builder.show();
    }

    public void setRepeatingListener(OnRepeatingListener listener) {
        mListener = listener;
    }

    public void removeRepeatingListener() {
        mListener = null;
    }

    public interface OnRepeatingListener {
        public void onCancel();
        public void onDelete(boolean repeating);
    }
}
