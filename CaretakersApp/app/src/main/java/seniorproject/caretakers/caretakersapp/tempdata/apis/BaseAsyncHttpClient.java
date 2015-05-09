package seniorproject.caretakers.caretakersapp.tempdata.apis;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

/**
 * Extension of the Android Asynchronous Http Client class which implements support for the
 * BaseJsonResponseHandler.
 */
public class BaseAsyncHttpClient extends AsyncHttpClient {

    @Override
    public RequestHandle head(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, params, responseHandler);
        return super.head(context, url, params, responseHandler);
    }

    @Override
    public RequestHandle head(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, params, responseHandler);
        return super.head(context, url, headers, params, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, params, responseHandler);
        return super.get(context, url, params, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, params, responseHandler);
        return super.get(context, url, headers, params, responseHandler);
    }

    @Override
    public RequestHandle post(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, null, responseHandler);
        return super.post(context, url, entity, contentType, responseHandler);
    }

    @Override
    public RequestHandle post(Context context, String url, Header[] headers, RequestParams params, String contentType,
                              ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, params, responseHandler);
        return super.post(context, url, headers, params, contentType, responseHandler);
    }

    @Override
    public RequestHandle post(Context context, String url, Header[] headers, HttpEntity entity, String contentType,
                              ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, null, responseHandler);
        return super.post(context, url, headers, entity, contentType, responseHandler);
    }

    @Override
    public RequestHandle put(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, null, responseHandler);
        return super.put(context, url, entity, contentType, responseHandler);
    }

    @Override
    public RequestHandle put(Context context, String url, Header[] headers, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, null, responseHandler);
        return super.put(context, url, headers, entity, contentType, responseHandler);
    }

    @Override
    public RequestHandle delete(Context context, String url, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, null, responseHandler);
        return super.delete(context, url, responseHandler);
    }

    @Override
    public RequestHandle delete(Context context, String url, Header[] headers, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, null, responseHandler);
        return super.delete(context, url, headers, responseHandler);
    }

    @Override
    public RequestHandle delete(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler) {
        fillHandler(context, url, params, responseHandler);
        return super.delete(context, url, headers, params, responseHandler);
    }

    private void fillHandler(Context context, String url, RequestParams params, ResponseHandlerInterface handler) {
        if(handler != null) {
            if (handler instanceof BaseJsonResponseHandler) {
                BaseJsonResponseHandler baseHandler = (BaseJsonResponseHandler) handler;
                baseHandler.mContext = context;
                baseHandler.mUrl = url;
            }
        }
    }
}
