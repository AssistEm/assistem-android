package seniorproject.caretakers.caretakersapp.presenters;

import java.util.List;

import seniorproject.caretakers.caretakersapp.data.api.UserApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Availability;
import seniorproject.caretakers.caretakersapp.views.ModifyAvailabilityView;

/**
 * Created by Stephen on 4/9/2015.
 */
public class ModifyAvailabilityPresenter implements Presenter<ModifyAvailabilityView> {

    ModifyAvailabilityView view;

    LoginManager loginManager;
    UserApi api;

    public ModifyAvailabilityPresenter(LoginManager loginManager, UserApi api) {
        this.loginManager = loginManager;
        this.api = api;
    }

    @Override
    public void setView(ModifyAvailabilityView view) {
        this.view = view;
    }

    // TODO: Implement once out of staging
    public void retrieveAvailability() {

    }

    public void setAvailability(List<Availability> availability) {

    }
}
