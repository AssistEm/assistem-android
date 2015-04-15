package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;
import android.util.Log;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created by Jason on 4/10/15.
 */
public class PingRestClient extends CommunityRestClient {

    protected final static String PING = "pings";

    public static void initiatePing(Context context, String communityId, String title,
                                    String description, String time, BaseJsonResponseHandler handler)
            throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + PING + "/";
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
            body.put("description", description);
            body.put("time", time);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            Log.i("PING", body.toString());
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void respondToPing(Context context, String communityId, String pingId,
                                     int response, BaseJsonResponseHandler handler)
        throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + PING + "/" + pingId;
        JSONObject body = new JSONObject();
        try {
            body.put("response", response);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            Log.i("RESPONSE TO PING", url + " " + body.toString());
            mClient.put(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
