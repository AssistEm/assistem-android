package seniorproject.caretakers.caretakersapp.presenters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.data.model.User;
import seniorproject.caretakers.caretakersapp.views.ViewGroceryItemView;

/**
 * Created by Stephen on 4/15/2015.
 */
public class ViewGroceryItemPresenter implements Presenter<ViewGroceryItemView> {

    ViewGroceryItemView view;
    LoginManager loginManager;
    CommunitiesApi api;

    public ViewGroceryItemPresenter(LoginManager loginManager, CommunitiesApi api) {
        this.loginManager = loginManager;
        this.api = api;
    }


    @Override
    public void setView(ViewGroceryItemView view) {
        this.view = view;
    }

    public void deleteItem(GroceryItem item) {
        api.deleteGroceryItem(loginManager.getToken(), loginManager.getCommunityId(), item.getId())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<List<GroceryItem>>() {
                    @Override
                    public void call(List<GroceryItem> items) {
                        view.onGroceryItemDeleted();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        view.onError("Failed to delete item");
                    }
                }
        );

    }


    public void editItem(GroceryItem item) {
        List<GroceryItem> itemList = new ArrayList<>();
        itemList.add(item);
        api.updateGroceryItem(loginManager.getToken(), loginManager.getCommunityId(), item.getId(), itemList)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<List<GroceryItem>>() {
                    @Override
                    public void call(List<GroceryItem> items) {
                        view.onGroceryItemEdited();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        view.onError("Failed to edit item");
                    }
                }
        );
    }

    public void volunteerItem(GroceryItem item, Calendar deliveryTime) {
        // TODO: This.  Note that delivery is set within the volunteer JSON
    }

    public User getUser() {
        return loginManager.getUser();
    }

    public boolean isUserPatient() {
        return loginManager.getUser().isPatient();
    }
}
