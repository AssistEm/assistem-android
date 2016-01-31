package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;


import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Community;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;

/**
 * Created by Sarah on 1/27/16.
 */
public class CommunityFragment extends Fragment {

    TextView mFirstName;
    TextView mLastName;
    TextView mEmail;
    TextView mPhone;
    TextView mCommunityName;
    TextView mPatient;

    protected void findBaseViews(View rootView) {
        mCommunityName = (TextView) rootView.findViewById(R.id.community_name);
        mPatient = (TextView) rootView.findViewById(R.id.patient);
        mFirstName = (TextView) rootView.findViewById(R.id.first_name);
        mLastName = (TextView) rootView.findViewById(R.id.last_name);
        mEmail = (TextView) rootView.findViewById(R.id.email);
        mPhone = (TextView) rootView.findViewById(R.id.phone);
    }

    protected void populateBaseViews() {
        Community currentCommunity = AccountHandler.getInstance(getActivity()).getCurrentCommunity();
        mCommunityName.setText(currentCommunity.getName());
        mPatient.setText(currentCommunity.getPatientId());
        User currentUser = AccountHandler.getInstance(getActivity()).getCurrentUser();
        mFirstName.setText(currentUser.getFirstName());
        mLastName.setText(currentUser.getLastName());
        mEmail.setText(currentUser.getEmail());
        mPhone.setText(currentUser.getPhone());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, group, false);
        findBaseViews(rootView);
        populateBaseViews();
        return rootView;
    }

}

