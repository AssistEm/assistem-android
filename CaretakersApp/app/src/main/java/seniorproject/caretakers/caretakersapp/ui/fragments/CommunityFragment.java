package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Community;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;

/**
 * Created by Sarah on 1/27/16.
 */
public class CommunityFragment extends Fragment {

    ListView mCaretakers;
    TextView mCommunityName;
    TextView mPatient;
    TextView mPrimary;
    ArrayAdapter<String> mCaretakerArrayAdapter;
    ArrayList<String> caretakers;

    protected void findBaseViews(View rootView) {
        mCommunityName = (TextView) rootView.findViewById(R.id.community_name);
        mPatient = (TextView) rootView.findViewById(R.id.patient);
        mPrimary = (TextView) rootView.findViewById(R.id.primary_caretaker);
        mCaretakers = (ListView) rootView.findViewById(R.id.caretaker_list);
    }

    protected void populateBaseViews() {
        Community currentCommunity = AccountHandler.getInstance(getActivity()).getCurrentCommunity();
        mCommunityName.setText(currentCommunity.getName());
        mPatient.setText(currentCommunity.getPatientId());
        caretakers = currentCommunity.getCaretakers();
        Log.i("CARETAKERS", caretakers.get(0));
        //User currentUser = AccountHandler.getInstance(getActivity()).getCurrentUser();
        mPrimary.setText("PRIMARY");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, group, false);
        findBaseViews(rootView);
        populateBaseViews();
        mCaretakerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_caretaker_list,
                R.id.caretaker_name, caretakers);
        mCaretakers.setAdapter(mCaretakerArrayAdapter);
        return rootView;
    }

}

