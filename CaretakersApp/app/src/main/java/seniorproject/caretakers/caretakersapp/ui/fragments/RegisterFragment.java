package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public class RegisterFragment extends Fragment {

    EditText mUsernameEdit;
    EditText mPasswordEdit;
    EditText mConfirmPasswordEdit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String username = mUsernameEdit.getText().toString();
            String password = mPasswordEdit.getText().toString();
            String confirmPassword = mConfirmPasswordEdit.getText().toString();
            if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                return;
            } else if(!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                AccountHandler.getInstance().patientRegister(username, password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, group, false);
        ((Button) view.findViewById(R.id.user_register_submit)).setOnClickListener(mOnClickListener);
        mUsernameEdit = (EditText) view.findViewById(R.id.user_email);
        mPasswordEdit = (EditText) view.findViewById(R.id.user_password);
        mConfirmPasswordEdit = (EditText) view.findViewById(R.id.user_confirm_password);
        return view;
    }
}
