package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;

/**
 * Created by Jason on 4/3/15.
 */
public abstract class ProfileBaseFragment extends Fragment {

    TextView mFirstName;
    TextView mLastName;
    TextView mEmail;
    TextView mPhone;

    protected void findBaseViews(View rootView) {
        mFirstName = (TextView) rootView.findViewById(R.id.first_name);
        mLastName = (TextView) rootView.findViewById(R.id.last_name);
        mEmail = (TextView) rootView.findViewById(R.id.email);
        mPhone = (TextView) rootView.findViewById(R.id.phone);
    }

    protected void populateBaseViews() {
        User currentUser = AccountHandler.getInstance(getActivity()).getCurrentUser();
        mFirstName.setText(currentUser.getFirstName());
        mLastName.setText(currentUser.getLastName());
        mEmail.setText(currentUser.getEmail());
        mPhone.setText(currentUser.getPhone());
    }
}
