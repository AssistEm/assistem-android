package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.presenters.RegisterPresenter;
import seniorproject.caretakers.caretakersapp.ui.actvities.MainActivity;
import seniorproject.caretakers.caretakersapp.ui.interfaces.RegisterView;

public class RegisterFragment extends Fragment implements RegisterView {

    private static final String TAG = "RegisterFragment";

    @Inject
    RegisterPresenter presenter;

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

    View.OnClickListener mRadioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.button_caretaker:
                    presenter.setCaretakerStatus(true);
                    break;
                case R.id.button_patient:
                    presenter.setCaretakerStatus(false);
                    break;
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
            String communitySearch = mCommunityQueryEdit.getText().toString();
            String communityName = mCommunityNameEdit.getText().toString();
            Log.d(TAG, "Registering");
            presenter.register(email, password, confirmPassword,
                               firstName, lastName, phone,
                               communitySearch, communityName);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, group, false);
        CaretakersApplication app = (CaretakersApplication) this.getActivity().getApplication();
        app.inject(this);
        presenter.setView(this);
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

    @Override
    public void onRegisterSuccess() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    @Override
    public void onRegisterFailed(String error) {
        Toast.makeText(this.getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCaretakerStatusChanged(boolean isCaretaker) {
        if(isCaretaker) {
            mCommunityText.setText(getResources().getString(R.string.find_a_community));
            mCommunityNameEdit.setVisibility(View.GONE);
            mCommunityQueryEdit.setVisibility(View.VISIBLE);
        } else {
            mCommunityText.setText(getResources().getString(R.string.create_a_community));
            mCommunityQueryEdit.setVisibility(View.GONE);
            mCommunityNameEdit.setVisibility(View.VISIBLE);
        }
    }
}
