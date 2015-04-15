package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.os.Bundle;

import javax.inject.Inject;

import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.presenters.ProfilePresenter;
import seniorproject.caretakers.caretakersapp.ui.fragments.ProfileCaretakerFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.ProfilePatientFragment;
import seniorproject.caretakers.caretakersapp.views.ProfileView;

public class ProfileActivity extends BaseActivity implements ProfileView {

    @Inject
    ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CaretakersApplication app = (CaretakersApplication) this.getApplication();
        app.inject(this);
        presenter.setView(this);
        presenter.checkUserType();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_profile;
    }

    @Override
    public void onCaretaker() {
        this.getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_holder, new ProfileCaretakerFragment())
            .commit();
    }

    @Override
    public void onPatient() {
        this.getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_holder, new ProfilePatientFragment())
            .commit();

    }
}
