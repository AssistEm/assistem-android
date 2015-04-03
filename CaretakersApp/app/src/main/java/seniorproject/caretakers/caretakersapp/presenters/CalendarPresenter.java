package seniorproject.caretakers.caretakersapp.presenters;

import java.util.Calendar;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.model.Event;
import seniorproject.caretakers.caretakersapp.ui.interfaces.CalendarView;

/**
 * Created by Stephen on 3/31/2015.
 */
public class CalendarPresenter implements Presenter<CalendarView> {

    CommunitiesApi api;
    LoginManager loginManager;

    CalendarView view;

    public CalendarPresenter(CommunitiesApi api, LoginManager loginManager) {
        this.api = api;
        this.loginManager = loginManager;
    }

    @Override
    public void setView(CalendarView view) {
        this.view = view;
    }

    public void events(final int year, final int month) {
        Calendar calendar = Calendar.getInstance();
        int yearDiff = year - calendar.get(Calendar.YEAR);
        int monthDiff = month - calendar.get(Calendar.MONTH);
        int monthsAgo = (yearDiff * 12) + monthDiff;
        api.events(loginManager.getUser().getCommunity().getId(), monthsAgo)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        view.onEventsReceived(events, year, month);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onRetrieveEventsFailed("Failed to retrieve events");
                    }
                }
        );
    }
}
