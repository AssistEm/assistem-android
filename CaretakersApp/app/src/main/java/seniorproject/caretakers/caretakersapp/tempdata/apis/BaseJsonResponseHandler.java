package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.net.ConnectException;

import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public abstract class BaseJsonResponseHandler extends JsonHttpResponseHandler {

    protected Context mContext;
    protected String mUrl;

    @Override
    public final void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
        if(response != null) {
            handleFailure(statusCode, headers, response.toString(), e);
        } else {
            handleFailure(statusCode, headers, "" , e);
        }
    }

    @Override
    public final void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
        handleFailure(statusCode, headers, response, e);
    }

    private final void handleFailure(int statusCode, Header[] headers, String response,
                                     Throwable e) {
        Log.i("ERROR", response);
        if(mContext == null) {
            return;
        }
        if(e instanceof ConnectException) {
            Toast.makeText(mContext, "Server error.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(statusCode == 401) {
            Toast.makeText(mContext, "Authentication error.", Toast.LENGTH_SHORT).show();
            AccountHandler.getInstance(mContext)
                    .triggerAuthenticationError();
            return;
        }

        onCaughtFailure(statusCode, headers, response, e);
    }

    public void onCaughtFailure(int statusCode, Header[] headers, String response,
                                Throwable e) { };

}
