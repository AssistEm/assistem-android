package seniorproject.caretakers.caretakersapp.data.login;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import seniorproject.caretakers.caretakersapp.ui.actvities.BaseActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.MainActivity;
import seniorproject.caretakers.caretakersapp.ui.fragments.LoginFragment;

/**
 * Created by Stephen on 3/4/2015.
 */
@Module(complete = false,
        library = true,
        injects = {
                MainActivity.class,
                LoginFragment.class
        })
public class LoginModule {

    private Application app;

    public LoginModule(Application app) {
        this.app = app;
    }

    // TODO: Probably want to migrate this to another module in the near future (along with constructor)
    @Provides @Singleton
    public Application provideApp() {
        return app;
    }

    @Provides @Singleton
    public LoginManager provideLoginManager(Application app) {
        return new LoginManager(app);
    }
}
