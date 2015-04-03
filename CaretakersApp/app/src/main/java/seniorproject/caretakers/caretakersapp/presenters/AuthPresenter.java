package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.api.UserApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.ui.interfaces.LoginView;

/**
 * Created by Stephen on 3/31/2015.
 */
public class AuthPresenter {

    UserApi api;
    LoginManager loginManager;

    public AuthPresenter(UserApi api, LoginManager loginManager) {
        this.api = api;
        this.loginManager = loginManager;
    }
}
