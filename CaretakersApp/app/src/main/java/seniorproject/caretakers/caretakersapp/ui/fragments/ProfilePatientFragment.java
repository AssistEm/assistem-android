package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public class ProfilePatientFragment extends ProfileBaseFragment {

    ViewFlipper mIllnessFlipper;
    ViewFlipper mFamilyMemberFlipper;
    ViewFlipper mButtonFlipper;

    TextView mIllness;
    TextView mFamilyMember;

    EditText mIllnessEdit;
    EditText mFamilyMemberEdit;

    Button mEditButton;
    Button mSubmitButton;

    boolean mEditOpen;

    AccountHandler.AccountListener mListener = new AccountHandler.AccountListener() {
        @Override
        public void onFullProfileFetched(JSONObject profile) {
            try {
                JSONObject patientObject = profile.getJSONObject("patient_info");
                if(patientObject.has("illness_description") && !patientObject.isNull("illness_description")) {
                    String illness = patientObject.getString("illness");
                    mIllness.setText(illness);
                    mIllnessEdit.setText(illness);
                } else {
                    mIllness.setText("None");
                    mIllnessEdit.setText("");
                }
                if(patientObject.has("family_members") && !patientObject.isNull("family_members")) {
                    String familyMembers = patientObject.getString("family_members");
                    mFamilyMember.setText(familyMembers);
                    mFamilyMemberEdit.setText(familyMembers);
                } else {
                    mFamilyMember.setText("None");
                    mFamilyMemberEdit.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener mEditClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mEditOpen = true;
            mIllnessFlipper.showNext();
            mFamilyMemberFlipper.showNext();
            mButtonFlipper.showNext();
        }
    };

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            mEditOpen = false;
            mIllnessFlipper.showNext();
            mFamilyMemberFlipper.showNext();
            mButtonFlipper.showNext();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_patient, group, false);
        mEditOpen = false;
        findBaseViews(rootView);
        populateBaseViews();
        mIllnessFlipper = (ViewFlipper) rootView.findViewById(R.id.flip_illness);
        mIllness = (TextView) rootView.findViewById(R.id.illness_description);
        mIllness.setText("Loading");
        mIllnessEdit = (EditText) rootView.findViewById(R.id.edit_illness_description);
        mFamilyMemberFlipper = (ViewFlipper) rootView.findViewById(R.id.flip_members);
        mFamilyMember = (TextView) rootView.findViewById(R.id.family_members);
        mFamilyMember.setText("Loading");
        mFamilyMemberEdit = (EditText) rootView.findViewById(R.id.edit_family_members);
        mButtonFlipper = (ViewFlipper) rootView.findViewById(R.id.button_flipper);
        mSubmitButton = (Button) rootView.findViewById(R.id.submit_button);
        mEditButton = (Button) rootView.findViewById(R.id.edit_button);
        AccountHandler.getInstance(getActivity()).getFullProfile(getActivity(), mListener);
        return rootView;
    }
}
