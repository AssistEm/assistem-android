package seniorproject.caretakers.caretakersapp;

import android.app.Application;

import seniorproject.caretakers.caretakersapp.tempdata.apis.RestClient;

public class CaretakersApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.init(this);
    }
}
