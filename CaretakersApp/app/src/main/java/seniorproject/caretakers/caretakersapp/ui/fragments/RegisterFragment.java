package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public class RegisterFragment extends Fragment {

    EditText mEmailEdit;
    EditText mPasswordEdit;
    EditText mConfirmPasswordEdit;
    EditText mFirstNameEdit;
    EditText mLastNameEdit;
    EditText mPhoneEdit;
    EditText mCommunityNameEdit;
    EditText mCommunityQueryEdit;

    RadioButton mCaretakerRadio;
    RadioButton mPatientRadio;

    TextView mCommunityText;

    boolean mCaretaker = true;

    View.OnClickListener mRadioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.button_caretaker:
                    mCaretaker = true;
                    break;
                case R.id.button_patient:
                    mCaretaker = false;
                    break;
            }
            if(mCaretaker) {
                mCommunityText.setText(getResources().getString(R.string.find_a_community));
                mCommunityNameEdit.setVisibility(View.GONE);
                mCommunityQueryEdit.setVisibility(View.VISIBLE);
            } else {
                mCommunityText.setText(getResources().getString(R.string.create_a_community));
                mCommunityQueryEdit.setVisibility(View.GONE);
                mCommunityNameEdit.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = mEmailEdit.getText().toString();
            String password = mPasswordEdit.getText().toString();
            String confirmPassword = mConfirmPasswordEdit.getText().toString();
            String firstName = mFirstNameEdit.getText().toString();
            String lastName = mLastNameEdit.getText().toString();
            String phone = mPhoneEdit.getText().toString();
            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                    || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                return;
            } else if(!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }
            String community;
            if(mCaretaker) {
                community = mCommunityQueryEdit.getText().toString();
            } else {
                community = mCommunityNameEdit.getText().toString();
            }
            if(community.isEmpty()) {
                return;
            }
            if(mCaretaker) {
                AccountHandler.getInstance(getActivity())
                        .caretakerRegister(getActivity(), email, password, firstName, lastName,
                                phone, community);
            } else {
                AccountHandler.getInstance(getActivity())
                        .patientRegister(getActivity(), email, password, firstName, lastName,
                                phone, community, false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, group, false);
        ((Button) view.findViewById(R.id.user_register_submit)).setOnClickListener(mOnClickListener);
        mEmailEdit = (EditText) view.findViewById(R.id.user_email);
        mPasswordEdit = (EditText) view.findViewById(R.id.user_password);
        mConfirmPasswordEdit = (EditText) view.findViewById(R.id.user_confirm_password);
        mFirstNameEdit = (EditText) view.findViewById(R.id.user_first_name);
        mLastNameEdit = (EditText) view.findViewById(R.id.user_last_name);
        mPhoneEdit = (EditText) view.findViewById(R.id.user_phone);
        mCommunityNameEdit = (EditText) view.findViewById(R.id.create_community_name);
        mCommunityQueryEdit = (EditText) view.findViewById(R.id.find_community_query);
        mCaretakerRadio = (RadioButton) view.findViewById(R.id.button_caretaker);
        mCaretakerRadio.setOnClickListener(mRadioButtonClickListener);
        mPatientRadio = (RadioButton) view.findViewById(R.id.button_patient);
        mPatientRadio.setOnClickListener(mRadioButtonClickListener);
        mCommunityText = (TextView) view.findViewById(R.id.community_title);
        return view;
    }
}
