package seniorproject.caretakers.caretakersapp.presenters;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Community;
import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;
import seniorproject.caretakers.caretakersapp.views.AddGroceryItemView;

/**
 * Created by Stephen on 4/10/2015.
 */
public class AddGroceryPresenter implements Presenter<AddGroceryItemView>{

    AddGroceryItemView view;
    CommunitiesApi api;
    LoginManager loginManager;

    Community community;

    public AddGroceryPresenter(LoginManager loginManager, CommunitiesApi api) {
        this.loginManager = loginManager;
        this.api = api;
    }

    @Override
    public void setView(AddGroceryItemView view) {
        this.view = view;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public AddGroceryPresenter(CommunitiesApi api) {
        this.api = api;
    }

    public void addGroceryItem(final GroceryItem item) {
        api.groceryItems(loginManager.getToken(), community.getId())
        .flatMap(new Func1<List<GroceryItem>, Observable<List<GroceryItem>>>() {
            @Override
            public Observable<List<GroceryItem>> call(List<GroceryItem> items) {
                items.add(item);
                return api.addGroceryItem(loginManager.getToken(), community.getId(), items);
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<List<GroceryItem>>() {
                    @Override
                    public void call(List<GroceryItem> items) {
                        view.onGroceryItemAdded();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        view.onError("Could not add grocery item");
                    }
                }
        );
    }
}
