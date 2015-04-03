package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.inject.Inject;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.presenters.SettingsPresenter;
import seniorproject.caretakers.caretakersapp.ui.actvities.BaseActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.LoginActivity;
import seniorproject.caretakers.caretakersapp.ui.interfaces.SettingsView;

/**
 * Created by Jason on 2/22/15.
 */
public class SettingsFragment extends Fragment implements SettingsView {

    @Inject
    SettingsPresenter presenter;

    Button mLogoutButton;

    View.OnClickListener mLogoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().setResult(LoginActivity.LOGGED_OUT_RESULT);
            presenter.logout();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, viewGroup, false);
        mLogoutButton = (Button) rootView.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(mLogoutClickListener);
        return rootView;
    }

    @Override
    public void onLogout() {
        ((BaseActivity)(getActivity())).onLogout();
    }
}
