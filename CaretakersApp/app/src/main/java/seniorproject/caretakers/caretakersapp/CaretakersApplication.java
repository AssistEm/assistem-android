package seniorproject.caretakers.caretakersapp;

import android.app.Application;

import seniorproject.caretakers.caretakersapp.tempdata.apis.RestClient;

/**
 * This class is only used to initialize the RestClient class for networking purposes. The onCreate
 * is called when the Application as a whole is started.
 */
public class CaretakersApplication extends Application {

    /**
     * Called when the Application as a whole is started. Initalizes the RestClient class for
     * networking purposes.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.init(this);
    }
}


