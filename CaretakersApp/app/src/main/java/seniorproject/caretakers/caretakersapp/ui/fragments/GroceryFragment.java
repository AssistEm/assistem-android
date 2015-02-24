package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import seniorproject.caretakers.caretakersapp.R;

public class GroceryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grocery, viewGroup, false);

        return rootView;
    }
}
