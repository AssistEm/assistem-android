package seniorproject.caretakers.caretakersapp.apiservices;

import com.squareup.okhttp.OkHttpClient;

import org.apache.http.cookie.Cookie;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import retrofit.client.OkClient;

public abstract class BaseService {

    protected final static String SERVER_URL = "http://ca.jasonmsu.com";

    protected static OkHttpClient mClient;

    protected static CookieManager mCookieManager;

    protected static OkHttpClient getOkClient() {
        if (mClient == null) {
            mClient = new OkHttpClient();
            mCookieManager = new CookieManager();
            mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            mClient.setCookieHandler(mCookieManager);
        }
        return mClient;
    }

    protected static List<java.net.HttpCookie> getCookies() {
        return mCookieManager.getCookieStore().getCookies();
    }
}
