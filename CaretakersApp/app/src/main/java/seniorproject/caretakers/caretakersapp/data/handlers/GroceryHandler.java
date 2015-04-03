package seniorproject.caretakers.caretakersapp.data.handlers;

import android.content.Context;
import android.util.Log;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import seniorproject.caretakers.caretakersapp.tempdata.apis.BaseJsonResponseHandler;
import seniorproject.caretakers.caretakersapp.tempdata.apis.GroceryRestClient;
import seniorproject.caretakers.caretakersapp.tempdata.apis.NoNetworkException;
import seniorproject.caretakers.caretakersapp.tempdata.model.Event;
import seniorproject.caretakers.caretakersapp.tempdata.model.GroceryItem;

public class GroceryHandler {

    static GroceryHandler mInstance;

    private GroceryHandler() {

    }

    public static GroceryHandler getInstance() {
        if(mInstance == null) {
            mInstance = new GroceryHandler();
        }
        return mInstance;
    }

    private class AddItemResponseHandler extends BaseJsonResponseHandler {
        GroceryListener mListener;

        public AddItemResponseHandler(GroceryListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if(mListener != null) {
                mListener.onGroceryItemAdded();
            }
        }
    }

    private class GetItemsResponseHandler extends BaseJsonResponseHandler {
        GroceryListener mListener;

        public GetItemsResponseHandler(GroceryListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            List<GroceryItem> items = new ArrayList<GroceryItem>();
            Log.i("RESPONSE", response.toString());
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject itemObj = response.getJSONObject(i);
                    items.add(GroceryItem.parseGroceryItem(itemObj));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(mListener != null) {
                mListener.onGroceryItemsFetched(items);
            }
        }
    }

    private class VolunteerItemResponseHandler extends BaseJsonResponseHandler {
        GroceryListener mListener;

        public VolunteerItemResponseHandler(GroceryListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onGroceryItemVolunteered();
            }
        }
    }

    private class DeleteItemResponseHandler extends BaseJsonResponseHandler {
        GroceryListener mListener;

        public DeleteItemResponseHandler(GroceryListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onGroceryItemDeleted();
            }
        }
    }

    private class EditItemResponseHandler extends BaseJsonResponseHandler {
        GroceryListener mListener;

        public EditItemResponseHandler(GroceryListener listener) {
            mListener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject object) {
            if(mListener != null) {
                mListener.onGroceryItemEdited();
            }
        }
    }

    public void getItems(Context context, GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            GroceryRestClient.getItems(context, communityId, new GetItemsResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void addItem(Context context, String title, String description, String quantity,
                        String location, Calendar urgency, GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String urgencyString = format.format(urgency.getTime());
        try {
            GroceryRestClient.addItem(context, communityId, title, description, location, quantity,
                    urgencyString, new AddItemResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(Context context, String id, GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            GroceryRestClient.deleteItem(context, communityId, id, new DeleteItemResponseHandler(listener));
        } catch(NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void volunteerItem(Context context, String id, boolean volunteer, Calendar deliveryTime,
                              GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String deliveryString = format.format(deliveryTime.getTime());
        try {
            GroceryRestClient.volunteerItem(context, communityId, id, volunteer, deliveryString,
                    new VolunteerItemResponseHandler(listener));
        } catch(NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public void editItem(Context context, String itemId, String title, String description, String quantity,
                         String location, Calendar time, GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String urgencyString = format.format(time.getTime());
        try {
            GroceryRestClient.editItem(context, communityId, itemId, title, description, location, quantity,
                    urgencyString, new EditItemResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    public static class GroceryListener {
        public void onGroceryItemsFetched(List<GroceryItem> items) { }
        public void onGroceryItemVolunteered() { }
        public void onGroceryItemAdded() { }
        public void onGroceryItemDeleted() { }
        public void onGroceryItemEdited() { }
    }
}
