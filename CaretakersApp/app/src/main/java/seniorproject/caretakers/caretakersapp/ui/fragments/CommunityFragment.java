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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Community;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Sarah on 1/27/16.
 * Updates the community tab that allows the user to see who is in their community and change who is the primary caretaker
 */
public class CommunityFragment extends Fragment {

    ListView mCaretakers;
    TextView mCommunityName;
    TextView mPatient;
    TextView mPrimary;
    ArrayAdapter<String> mCaretakerArrayAdapter;
    ArrayList<String> caretakerIds;
    ArrayList<JSONObject> caretakersJSON;

    AccountHandler.AccountListener mListener = new AccountHandler.AccountListener() {
        @Override
        public void onFullProfileUserFetched(JSONObject profile) {
            try {
                String type = profile.getString("type");
                String firstName = profile.getString("first_name");
                String lastName = profile.getString("last_name");
                if (type.equals("patient")) {
                    mPatient.setText(firstName + " " + lastName);
                } else {
                    if (mCaretakerArrayAdapter.getPosition(firstName + " " + lastName) == -1) {
                        mCaretakerArrayAdapter.add(firstName +" "+ lastName);
                    } else {
                        mPrimary.setText(firstName + lastName);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    protected void findBaseViews(View rootView) {
        mCommunityName = (TextView) rootView.findViewById(R.id.community_name);
        mPatient = (TextView) rootView.findViewById(R.id.patient);
        mPrimary = (TextView) rootView.findViewById(R.id.primary_caretaker);
        mCaretakers = (ListView) rootView.findViewById(R.id.caretaker_list);
    }

    protected void populateBaseViews() {
        FragmentActivity activity = getActivity();
        AccountHandler handler = AccountHandler.getInstance(activity);

        Community currentCommunity = handler.getCurrentCommunity();
        mCommunityName.setText(currentCommunity.getName());

        String patientId = currentCommunity.getPatientId();
        handler.getFullProfileUser(activity, patientId, mListener);

        String primaryId = currentCommunity.getPrimary();
        handler.getFullProfileUser(activity, primaryId, mListener);

        caretakerIds = currentCommunity.getCaretakers();
        for (int i = 0; i < caretakerIds.size(); i++) {
            String caretaker = caretakerIds.get(i);
            handler.getFullProfileUser(activity, caretaker, mListener);
            //caretakers.add(caretakerIds.get(i));
        }
        if (mPatient.getText() == null) {
            mPatient.setText(patientId);
        }

        if (mPrimary.getText() == null) {
            mPrimary.setText(primaryId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, group, false);
        mCaretakerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_caretaker_list,
                R.id.caretaker_name);
        findBaseViews(rootView);
        populateBaseViews();
        mCaretakers.setAdapter(mCaretakerArrayAdapter);
        return rootView;
    }

}

