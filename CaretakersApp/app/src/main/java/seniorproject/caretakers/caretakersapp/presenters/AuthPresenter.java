package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.api.UserApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;

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
