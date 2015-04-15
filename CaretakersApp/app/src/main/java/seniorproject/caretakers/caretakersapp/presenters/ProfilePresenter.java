package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.views.ProfileView;

/**
 * Created by Stephen on 4/9/2015.
 */
public class ProfilePresenter implements Presenter<ProfileView> {

    ProfileView view;
    LoginManager loginManager;

    @Override
    public void setView(ProfileView view) {
        this.view = view;
    }

    public void checkUserType() {
        if(loginManager.getUser().isPatient()) {
            view.onPatient();
        } else {
            view.onCaretaker();
        }
    }
}
