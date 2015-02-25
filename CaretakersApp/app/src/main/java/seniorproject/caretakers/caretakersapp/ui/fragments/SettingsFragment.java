package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.ui.actvities.LoginActivity;

/**
 * Created by Jason on 2/22/15.
 */
public class SettingsFragment extends Fragment {

    Button mLogoutButton;

    View.OnClickListener mLogoutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().setResult(LoginActivity.LOGGED_OUT_RESULT);
            AccountHandler.getInstance(getActivity()).logout();
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
}
