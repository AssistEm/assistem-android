package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.ui.actvities.ModifyAvailabilityActivity;

public class ProfileCaretakerFragment extends ProfileBaseFragment {

    Button mModifyAvailability;

    private View.OnClickListener mModifyAvailabilityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ModifyAvailabilityActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_caretaker, group, false);
        CaretakersApplication app = (CaretakersApplication) this.getActivity().getApplication();
        app.inject(this);
        presenter.setView(this);
        findBaseViews(rootView);
        presenter.retrieveUser();
        mModifyAvailability = (Button) rootView.findViewById(R.id.modify_availability);
        mModifyAvailability.setOnClickListener(mModifyAvailabilityClickListener);
        return rootView;
    }
}
