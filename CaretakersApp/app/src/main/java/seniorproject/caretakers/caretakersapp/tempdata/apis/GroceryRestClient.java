package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.conn.BasicEofSensorWatcher;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class GroceryRestClient extends CommunityRestClient {

    protected static final String GROCERIES = "groceries";

    public static void getItems(Context context, String communityId,
                                BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + GROCERIES;
        Header[] headers = new Header[]{generateAuthHeader(context)};
        mClient.get(context, url, headers, new RequestParams(), handler);
    }

    public static void addItem(Context context, String communityId, String title, String description,
                               String location, String quantity, String urgency,
                               BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + GROCERIES + "/";
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
            body.put("description", description);
            body.put("location", location);
            body.put("quantity", quantity);
            body.put("urgency", urgency);
            Header[] headers = new Header[]{generateAuthHeader(context)};
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(Context context, String communityId, String itemId,
                                  BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + GROCERIES + "/" + itemId;
        Header[] headers = new Header[] {generateAuthHeader(context)};
        mClient.delete(context, url, headers, handler);
    }

    public static void volunteerItem(Context context, String communityId, String itemId,
                                     boolean volunteer, String deliveryDate,
                                     BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + GROCERIES + "/" + itemId;
        JSONObject body = new JSONObject();
        try {
            body.put("volunteer", volunteer);
            if (volunteer) {
                body.put("delivery_time", deliveryDate);
            }
            Header[] headers = new Header[]{generateAuthHeader(context)};
            mClient.put(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void editItem(Context context, String communityId, String itemId, String title,
                                String description, String location, String quantity,
                                String urgency, BaseJsonResponseHandler handler)
            throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId + "/" + GROCERIES + "/" + itemId;
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
            body.put("description", description);
            body.put("location", location);
            body.put("quantity", quantity);
            body.put("urgency", urgency);
            Header[] headers = new Header[]{generateAuthHeader(context)};
            Log.i("JSON", body.toString());
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}