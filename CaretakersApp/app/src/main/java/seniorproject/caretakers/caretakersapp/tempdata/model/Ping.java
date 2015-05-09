package seniorproject.caretakers.caretakersapp.tempdata.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class which represents a Ping object
 */
public class Ping {

    public String id;
    public String title;
    public String description;
    public Calendar time;
    public String patientName;

    /**
     * Static method to parse a JSON object into a Ping object
     * @param obj JSON representation of the Ping object
     * @return Ping representation of the parsed input
     * @throws JSONException Thrown when the input is not of the expected format
     */
    public static Ping parsePing(JSONObject obj) throws JSONException, ParseException {
        Ping ping = new Ping();
        ping.id = obj.getString("ping_id");
        ping.title = obj.getString("title");
        ping.patientName = obj.getString("patient_name");
        if(obj.has("description") && !obj.isNull("description")) {
            String description = obj.getString("description");
            if(!description.isEmpty()) {
                ping.description = description;
            }
        }
        String timeString = obj.getString("time");
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(format.parse(timeString));
        ping.time = cal;
        return ping;
    }

}
