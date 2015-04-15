package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.views.UserView;

/**
 * Created by Stephen on 4/10/2015.
 */
public class UserPresenter implements Presenter<UserView> {

    UserView view;
    LoginManager loginManager;

    @Override
    public void setView(UserView view) {
        this.view = view;
    }

    public UserPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public boolean isUserPatient() {
        return loginManager.getUser().isPatient();
    }

    public void retrieveUser() {
        view.onUser(loginManager.getUser());
    }
}
