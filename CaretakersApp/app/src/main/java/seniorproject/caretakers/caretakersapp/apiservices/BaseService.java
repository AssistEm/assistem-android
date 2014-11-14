package seniorproject.caretakers.caretakersapp.apiservices;

import com.squareup.okhttp.OkHttpClient;

import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.client.OkClient;

public abstract class BaseService {

    protected final static String SERVER_URL = "http://ca.jasonmsu.com";

    protected static OkHttpClient mClient;

    protected static OkHttpClient getOkClient() {
        if(mClient == null) {
            mClient = new OkHttpClient();
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            mClient.setCookieHandler(cookieManager);
        }
        return mClient;
    }

}
