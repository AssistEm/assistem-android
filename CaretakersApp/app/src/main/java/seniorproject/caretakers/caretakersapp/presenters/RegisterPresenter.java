package seniorproject.caretakers.caretakersapp.presenters;

import android.util.Log;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.data.api.UserApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Login;
import seniorproject.caretakers.caretakersapp.data.model.User;
import seniorproject.caretakers.caretakersapp.ui.interfaces.RegisterView;

/**
 * Created by Stephen on 3/31/2015.
 */
public class RegisterPresenter extends AuthPresenter implements Presenter<RegisterView> {

    private static final String TAG = "RegisterPresenter";

    RegisterView view;
    boolean isCaretaker = true;

    public RegisterPresenter(UserApi api, LoginManager loginManager) {
        super(api, loginManager);
    }

    @Override
    public void setView(RegisterView view) {
        this.view = view;
    }

    public void register(String email, String password, String confirmPassword,
                         String firstName, String lastName, String phone,
                         String communitySearch, String communityName) {

        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || (communityName.isEmpty() && !isCaretaker)
                || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || (communitySearch.isEmpty() && isCaretaker) ) {
            view.onRegisterFailed("Please fill out all required fields");
            return;
        } else if(!password.equals(confirmPassword)) {
            view.onRegisterFailed("Passwords do not match");
            return;
        }

        User user = new User(email, password, phone, isCaretaker,
                             firstName, lastName, communitySearch, communityName);

        Log.d(TAG, "Creating user");
        api.create(user)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<Login>() {
                    @Override
                    public void call(Login login) {
                        loginManager.login(login);
                        view.onRegisterSuccess();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onRegisterFailed("Registration failed");
                    }
                }
        );

    }

    public void setCaretakerStatus(boolean isCaretaker) {
        this.isCaretaker = isCaretaker;
        view.onCaretakerStatusChanged(isCaretaker);
    }
}
