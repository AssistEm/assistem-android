package seniorproject.caretakers.caretakersapp.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import seniorproject.caretakers.caretakersapp.tempdata.model.Ping;

/**
 * Created by Jason on 4/11/15.
 */
public class ResponsePingDialog extends DialogFragment {

    Ping mPing;

    @SuppressLint("ValidFragment")
    public ResponsePingDialog(Ping ping) {
        mPing = ping;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        return builder.create();
    }
}
