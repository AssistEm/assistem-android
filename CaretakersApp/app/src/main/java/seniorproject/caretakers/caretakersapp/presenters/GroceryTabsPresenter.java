package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.views.GroceryTabsView;

/**
 * Created by Stephen on 4/15/2015.
 */
public class GroceryTabsPresenter implements Presenter<GroceryTabsView> {

    LoginManager loginManager;
    GroceryTabsView view;

    public GroceryTabsPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public boolean isUserPatient() {
        return loginManager.getUser().isPatient();
    }

    @Override
    public void setView(GroceryTabsView view) {
        this.view = view;
    }
}
