package seniorproject.caretakers.caretakersapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import dagger.Module;
import seniorproject.caretakers.caretakersapp.data.gson.CalendarSerializer;

/**
 * Created by Stephen on 2/19/2015.
 */
@Module(complete = false,
        library = true)
public class DataModule {

    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Calendar.class, new CalendarSerializer())
                .create();
    }
}
