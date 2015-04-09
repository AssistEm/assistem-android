package seniorproject.caretakers.caretakersapp;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import seniorproject.caretakers.caretakersapp.data.DataModule;
import seniorproject.caretakers.caretakersapp.data.api.ApiModule;
import seniorproject.caretakers.caretakersapp.data.login.LoginModule;
import seniorproject.caretakers.caretakersapp.presenters.PresenterModule;

public class CaretakersApplication extends Application {


    public ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules());
    }

    private Object[] getModules() {
        return Arrays.asList(new ApiModule(),
                             new DataModule(),
                             new LoginModule(this),
                             new PresenterModule()).toArray();
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
