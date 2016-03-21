package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.ui.actvities.ModifyPrimaryCaretakerActivity;

/**
 * Created by sarah on 3/10/16.
 */
public class CommunityPatientFragment extends CommunityFragment {
    Button mModifyPrimaryCaretaker;

    private View.OnClickListener mModifyPrimaryCaretakerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ModifyPrimaryCaretakerActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_community, group, false);
        mCaretakerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_caretaker_list,
                R.id.caretaker_name);
        findBaseViews(rootView);
        populateBaseViews();
        mCaretakers.setAdapter(mCaretakerArrayAdapter);
        mModifyPrimaryCaretaker = (Button) rootView.findViewById(R.id.modify_primary_caretaker);
        mModifyPrimaryCaretaker.setOnClickListener(mModifyPrimaryCaretakerClickListener);
        findBaseViews(rootView);
        populateBaseViews();
        return rootView;
    }
}