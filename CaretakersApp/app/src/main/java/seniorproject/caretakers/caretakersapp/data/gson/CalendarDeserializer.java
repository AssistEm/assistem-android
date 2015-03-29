package seniorproject.caretakers.caretakersapp.data.gson;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Stephen on 2/19/2015.
 */
public class CalendarDeserializer implements JsonDeserializer<Date> {

    // TODO: Deserialize into calendar

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String time = json.getAsString();

        return new Date(time);
    }
}
