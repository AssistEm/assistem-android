package seniorproject.caretakers.caretakersapp.presenters;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.data.api.UserApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Login;
import seniorproject.caretakers.caretakersapp.data.model.LoginRequest;
import seniorproject.caretakers.caretakersapp.views.LoginView;

/**
 * Created by Stephen on 3/31/2015.
 */
public class LoginPresenter extends AuthPresenter implements Presenter<LoginView> {

    LoginView view;

    public LoginPresenter(UserApi api, LoginManager loginManager) {
        super(api, loginManager);
    }

    public void login(String username, String password) {
        if(!username.isEmpty() && !password.isEmpty()) {
            api.login(new LoginRequest(username, password))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    new Action1<Login>() {
                        @Override
                        public void call(Login login) {
                            loginManager.login(login);
                            view.onLoginSuccess();
                        }
                    },
                    new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            view.onLoginFailed();
                        }
                    }
            );
        } else {
            view.onLoginFailed();
        }
    }

    @Override
    public void setView(LoginView view) {
        this.view = view;
    }
}
