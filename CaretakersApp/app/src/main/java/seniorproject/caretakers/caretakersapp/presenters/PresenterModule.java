package seniorproject.caretakers.caretakersapp.presenters;

import dagger.Module;
import dagger.Provides;
import seniorproject.caretakers.caretakersapp.data.api.ApiModule;
import seniorproject.caretakers.caretakersapp.data.api.CommunitiesApi;
import seniorproject.caretakers.caretakersapp.data.api.UserApi;
import seniorproject.caretakers.caretakersapp.data.login.LoginManager;
import seniorproject.caretakers.caretakersapp.data.login.LoginModule;
import seniorproject.caretakers.caretakersapp.ui.fragments.CalendarFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.DrawerFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.LoginFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.RegisterFragment;

/**
 * Created by Stephen on 4/1/2015.
 */
@Module(includes = {
            ApiModule.class,
            LoginModule.class
        },
        injects = {
            CalendarFragment.class,
            DrawerFragment.class,
            LoginFragment.class,
            RegisterFragment.class
        },
        library = true)
public class PresenterModule {

    @Provides
    public CalendarPresenter provideCalendarPresenter(CommunitiesApi api, LoginManager loginManager) {
        return new CalendarPresenter(api, loginManager);
    }

    @Provides
    public DrawerPresenter provideDrawerPresenter(LoginManager loginManager) {
        return new DrawerPresenter(loginManager);
    }

    @Provides
    public LoginPresenter provideLoginPresenter(UserApi api, LoginManager loginManager) {
        return new LoginPresenter(api, loginManager);
    }

    @Provides
    public RegisterPresenter provideRegisterPresenter(UserApi api, LoginManager loginManager) {
        return new RegisterPresenter(api, loginManager);
    }
}
