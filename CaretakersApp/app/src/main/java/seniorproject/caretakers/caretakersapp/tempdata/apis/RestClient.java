package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public abstract class RestClient {

    protected static BaseAsyncHttpClient mClient;
    protected static final String STAGING_BASE_URL = "http://sca.jasonmsu.com/api/";
    protected static final String PRODUCTION_BASE_URL = "http://ca.jasonmsu.com/api/";
    protected static String BASE_URL;

    protected static final String CONTENT_TYPE = "application/json";

    public static void init(Context context) {
        mClient = new BaseAsyncHttpClient();
        mClient.setEnableRedirects(true, true, true);
        BASE_URL = STAGING_BASE_URL;
    }

    protected static void checkNetwork(Context context) throws NoNetworkException {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo == null && !netInfo.isConnectedOrConnecting()) {
            throw new NoNetworkException();
        }
    }

    protected static StringEntity jsonToEntity(JSONObject object)
            throws UnsupportedEncodingException {
        StringEntity entity = new StringEntity(object.toString());
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        return entity;
    }
}
