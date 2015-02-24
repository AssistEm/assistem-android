package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seniorproject.caretakers.caretakersapp.R;

/**
 * Created by Jason on 2/22/15.
 */
public class SettingsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, viewGroup, false);

        return rootView;
    }
}
