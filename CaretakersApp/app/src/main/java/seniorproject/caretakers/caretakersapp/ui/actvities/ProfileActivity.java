package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.fragments.ProfileCaretakerFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.ProfilePatientFragment;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(AccountHandler.getInstance(this).getCurrentUser() instanceof Patient) {
            fragmentTransaction.add(R.id.fragment_holder, new ProfilePatientFragment());
        } else {
            fragmentTransaction.add(R.id.fragment_holder, new ProfileCaretakerFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_profile;
    }

}
