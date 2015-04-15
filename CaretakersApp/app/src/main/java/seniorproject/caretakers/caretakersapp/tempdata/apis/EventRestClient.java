package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class EventRestClient extends CommunityRestClient {

    protected static final String EVENTS = "events";

    public static void getEvents(Context context, String communityId, int monthsAgo,
                                 BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + EVENTS;
        url += "?months=" + monthsAgo;
        Header[] headers = new Header[] {generateAuthHeader(context)};
        mClient.get(context, url, headers, new RequestParams(), handler);
    }

    public static void addEvent(Context context, String communityId, String title,
                                String description, String location, String category,
                                int priority, String startTime, String endTime,
                                BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + EVENTS;
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
            body.put("description", description);
            body.put("location", location);
            body.put("category", category);
            body.put("priority", priority);
            body.put("start_time", startTime);
            body.put("end_time", endTime);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void addRepeatingEvent(Context context, String communityId, String title,
                                         String description, String location, String category,
                                         int priority, String startTime, String endTime,
                                         List<Integer> daysOfWeek, int numberOfWeeks,
                                         BaseJsonResponseHandler handler)
            throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + EVENTS;
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
            body.put("description", description);
            body.put("location", location);
            body.put("category", category);
            body.put("priority", priority);
            body.put("start_time", startTime);
            body.put("end_time", endTime);
            JSONArray array = new JSONArray();
            for(Integer value : daysOfWeek) {
                array.put(value);
            }
            body.put("days_of_week", array);
            body.put("weeks_to_repeat", numberOfWeeks);
            Log.i("REPEATING EVENT ADD", url + " " + body.toString());
            Header[] headers = new Header[] {generateAuthHeader(context)};
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void updateEvent(Context context, String communityId, String eventId,
                                   String title, String description, String location,
                                   String category, int priority, String startTime, String endTime,
                                   BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + EVENTS + "/" + eventId;
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
            body.put("description", description);
            body.put("location", location);
            body.put("category", category);
            body.put("priority", priority);
            body.put("start_time", startTime);
            body.put("end_time", endTime);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void volunteerForEvent(Context context, String communityId, String eventId,
                                         boolean volunteer, BaseJsonResponseHandler handler)
            throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + EVENTS + "/" + eventId;
        JSONObject body = new JSONObject();
        try {
            body.put("volunteer", volunteer);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            mClient.put(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEvent(Context context, String communityId, String eventId,
                                   boolean deleteRepeating, BaseJsonResponseHandler handler)
            throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + EVENTS + "/" + eventId;
        url += "?delete_repeating=" + deleteRepeating;
        Log.i("DELETE", url);
        Header[] headers = new Header[] {generateAuthHeader(context)};
        mClient.delete(context, url, headers, handler);
        /*JSONObject body = new JSONObject();
        try {
            body.put("deleteRepeating", deleteRepeating);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            //mClient.delete
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}
