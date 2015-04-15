package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.User;
import seniorproject.caretakers.caretakersapp.presenters.UserPresenter;
import seniorproject.caretakers.caretakersapp.views.UserView;

/**
 * Created by Jason on 4/3/15.
 */
public class ProfileBaseFragment extends Fragment implements UserView {

    @Inject
    UserPresenter presenter;

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

    @Override
    public void onUser(User currentUser) {
        mFirstName.setText(currentUser.getFirstName());
        mLastName.setText(currentUser.getLastName());
        mEmail.setText(currentUser.getEmail());
        mPhone.setText(currentUser.getPhone());
    }
}
