package seniorproject.caretakers.caretakersapp.presenters;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.data.model.User;
import seniorproject.caretakers.caretakersapp.data.model.VolunteerRequest;
import seniorproject.caretakers.caretakersapp.ui.interfaces.EventView;

/**
 * Created by Stephen on 4/2/2015.
 */
public class ViewEventPresenter implements Presenter<EventView> {

    CommunitiesApi api;
    LoginManager loginManager;
    EventView view;
    Event event;

    public ViewEventPresenter(CommunitiesApi api, LoginManager loginManager) {
        this.api = api;
        this.loginManager = loginManager;
    }

    @Override
    public void setView(EventView view) {
        this.view = view;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void checkUserIsPatient() {
        User user = loginManager.getUser();
        if(user.isPatient()) {
            view.onPatient();
        }
    }

    public void checkEventVolunteered() {
        boolean isVolunteering = loginManager.getUser().getId().equals(event.getVolunteer().getId());
        view.onVolunteeringStatus(isVolunteering);
    }

    public void updateEvent(Event event) {
        api.updateEvent(loginManager.getCommunityId(), event.getApiId(), event)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        view.onEventUpdate();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        view.onError("Failed to update event");
                    }
                }
        );
    }

    public void volunteer(boolean isVolunteer) {
        api.volunteer(loginManager.getCommunityId(), event.getApiId(), new VolunteerRequest(isVolunteer))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<Event>() {
                    @Override
                    public void call(Event event) {
                        view.onVolunteer();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onError("Failed to volunteer for event");
                    }
                }
        );
    }
}
