package seniorproject.caretakers.caretakersapp.apiservices;

import com.loopj.android.http.AsyncHttpClient;

public class BaseRestClient {

    protected final static String BASE_URL = "http://ca.jasonmsu.com/api/";
    protected static AsyncHttpClient mClient;

    public final static void init() {
        if(mClient != null) {
            return;
        }
        mClient = new AsyncHttpClient();
    }

}
