package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.views.MainView;

/**
 * Created by Stephen on 4/9/2015.
 */
public class MainPresenter implements Presenter<MainView>{

    MainView view;
    LoginManager loginManager;

    public MainPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Override
    public void setView(MainView view) {
        this.view = view;
    }

    public boolean isUserPatient() {
        return loginManager.getUser().isPatient();
    }
}
