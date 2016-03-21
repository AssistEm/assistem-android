package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CommunityRestClient extends RestClient {

    protected static final String COMMUNITIES = "communities";
    protected static final String PRIMARY = "primary_caretaker";
    public static void setPrimaryCaretaker(Context context, String communityId, String primaryId,
                                   BaseJsonResponseHandler handler) throws NoNetworkException {
        checkNetwork(context);
        String url = BASE_URL + COMMUNITIES + "/" + communityId;
        JSONObject body = new JSONObject();
        try {
            body.put("primary_caretaker", primaryId);
            Header[] headers = new Header[] {generateAuthHeader(context)};
            mClient.post(context, url, headers, jsonToEntity(body), CONTENT_TYPE, handler);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
