package seniorproject.caretakers.caretakersapp.presenters;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.data.model.User;
import seniorproject.caretakers.caretakersapp.views.GroceryView;

/**
 * Created by Stephen on 4/15/2015.
 */
public class GroceryPresenter implements Presenter<GroceryView> {

    GroceryView view;
    LoginManager loginManager;
    CommunitiesApi api;

    public GroceryPresenter(LoginManager loginManager, CommunitiesApi api) {
        this.loginManager = loginManager;
        this.api = api;
    }

    @Override
    public void setView(GroceryView view) {
        this.view = view;
    }

    public void retrieveItems() {
        api.groceryItems(loginManager.getToken(), loginManager.getCommunityId())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<List<GroceryItem>>() {
                    @Override
                    public void call(List<GroceryItem> items) {
                        view.onGroceryItemsFetched(items);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        view.onError("Failed to obtain grocery items");
                    }
                }

        );
    }

    public User retrieveUser() {
        return loginManager.getUser();
    }
}
