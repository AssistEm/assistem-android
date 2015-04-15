package seniorproject.caretakers.caretakersapp.presenters;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.views.AddEventView;

/**
 * Created by Stephen on 4/3/2015.
 */
public class AddEventPresenter implements Presenter<AddEventView> {

    CommunitiesApi api;
    LoginManager loginManager;

    AddEventView view;

    public AddEventPresenter(CommunitiesApi api, LoginManager loginManager) {
        this.api = api;
        this.loginManager = loginManager;
    }

    @Override
    public void setView(AddEventView view) {
        this.view = view;
    }

    public void addEvent(Event event) {
        api.addEvent(loginManager.getCommunityId(), event)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        view.onEventAdded();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onError("Failed to add event");
                    }
                }
        );
    }
}
