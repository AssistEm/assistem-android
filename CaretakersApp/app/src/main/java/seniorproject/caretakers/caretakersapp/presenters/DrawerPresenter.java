package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.ui.interfaces.DrawerView;

/**
 * Created by Stephen on 4/1/2015.
 */
public class DrawerPresenter implements Presenter<DrawerView> {

    LoginManager loginManager;
    DrawerView view;

    public DrawerPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Override
    public void setView(DrawerView view) {
        this.view = view;
    }

    public void retrieveUser() {
        view.onUserRetrieved(loginManager.getUser());
    }
}
