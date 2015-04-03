package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.data.model.User;
import seniorproject.caretakers.caretakersapp.ui.interfaces.EventView;

/**
 * Created by Stephen on 4/2/2015.
 */
public class ViewEventPresenter implements Presenter<EventView> {

    LoginManager loginManager;
    EventView view;
    Event event;

    public ViewEventPresenter(LoginManager loginManager) {
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
}
