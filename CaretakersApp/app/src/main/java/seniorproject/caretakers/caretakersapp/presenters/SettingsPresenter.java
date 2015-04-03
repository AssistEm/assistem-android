package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.ui.interfaces.SettingsView;

/**
 * Created by Stephen on 4/1/2015.
 */
public class SettingsPresenter implements Presenter<SettingsView> {

    LoginManager loginManager;
    SettingsView view;

    public SettingsPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Override
    public void setView(SettingsView view) {
        this.view = view;
    }

    public void logout() {
        loginManager.logout();
        view.onLogout();
    }
}
