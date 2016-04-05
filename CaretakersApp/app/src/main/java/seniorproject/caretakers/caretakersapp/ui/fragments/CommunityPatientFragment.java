package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.ui.actvities.ModifyPrimaryCaretakerActivity;

/**
 * Created by sarah on 3/10/16. A fragment for the patient's community tab.
 */
public class CommunityPatientFragment extends CommunityFragment {
    Button mModifyPrimaryCaretaker;

    private View.OnClickListener mModifyPrimaryCaretakerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ModifyPrimaryCaretakerActivity.class);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            FragmentActivity activity = getActivity();
            AccountHandler handler = AccountHandler.getInstance(activity);
            String newPrimaryId = data.getExtras().getString("primary");
            handler.getFullProfileUser(activity, newPrimaryId, mListener);
            handler.getCurrentCommunity().setPrimary(newPrimaryId);
            recreate();
        }

    }

    //Reload the layout with the correct primary caretaker
    public void recreate() {
        LinearLayout layout = (LinearLayout) mRootView.findViewById(R.id.community);
        layout.invalidate();
         layout.requestLayout();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_community_patient, group, false);
        mCaretakerArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_caretaker_list,
                R.id.caretaker_name);
        findBaseViews();
        populateBaseViews();
        mCaretakers.setAdapter(mCaretakerArrayAdapter);
        mModifyPrimaryCaretaker = (Button) mRootView.findViewById(R.id.modify_primary_caretaker);
        mModifyPrimaryCaretaker.setOnClickListener(mModifyPrimaryCaretakerClickListener);
        return mRootView;
    }


}