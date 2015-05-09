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

/**
 * Handler class for Grocery items. Offers a singleton class for executing requests for and
 * receiving Grocery List objects.
 */
public class GroceryHandler {

    static GroceryHandler mInstance;

    private GroceryHandler() {

    }

    /**
     * Singleton getInstance method. Either returns an existing instance of the GroceryHandler class
     * or constructs, sets and returns a new instance.
     * @return An instance of an GroceryHandler
     */
    public static GroceryHandler getInstance() {
        if(mInstance == null) {
            mInstance = new GroceryHandler();
        }
        return mInstance;
    }

    /**
     * Callback class for parsing the response from a request that adds a Grocery List Item.
     * Includes an instance of an GroceryListener observer to notify.
     */
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

    /**
     * Callback class for parsing the response from a request that gets the Grocery List items.
     * Includes an instance of an GroceryListener observer to notify.
     */
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

    /**
     * Callback class for parsing the response from a request to volunteer for a grocery item.
     * Includes an instance of an GroceryListener observer to notify.
     */
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

    /**
     * Callback class for parsing the response from a request that deletes a grocery list item.
     * Includes an instance of an GroceryListener observer to notify.
     */
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

    /**
     * Callback class for parsing the response from a request that edits a grocery list item.
     * Includes an instance of an GroceryListener observer to notify.
     */
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

    /**
     * Public method to initiate a request for grocery list items
     * @param context Context to execute the call in
     * @param listener GroceryListener to notify
     */
    public void getItems(Context context, GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            GroceryRestClient.getItems(context, communityId, new GetItemsResponseHandler(listener));
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to add a grocery list item
     * @param context Context in which to execute the call
     * @param title Title of the grocery item
     * @param description Description of the grocery item
     * @param quantity Quantity of the grocery item
     * @param location Location of the grocery item
     * @param urgency Date and time by which the user needs the item
     * @param listener GroceryListener to notify
     */
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

    /**
     * Public method to initiate a request to delete an item
     * @param context Context in which to execute the call
     * @param id Id of the item to delete
     * @param listener GroceryListener to notify
     */
    public void deleteItem(Context context, String id, GroceryListener listener) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            GroceryRestClient.deleteItem(context, communityId, id, new DeleteItemResponseHandler(listener));
        } catch(NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a call to volunteer for an item
     * @param context Context in which to execute the call
     * @param id Id of the item to volunteer for
     * @param volunteer Boolean to represent the volunteer state, whether the request is to
     *                  volunteer or unvolunteer for
     * @param deliveryTime Calendar representation of when the item will be delivered
     * @param listener GroceryListener to notify
     */
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

    /**
     * Public method to initiate a call to edit an item
     * @param context Context in which to execute the call
     * @param itemId Id of the item to volunteer for
     * @param title Title of the item
     * @param description Description of the item
     * @param quantity Quantity of the item
     * @param location Location of the item
     * @param time Calendar representation of the time in which the user needs the item
     * @param listener GroceryListener to notify
     */
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

    //Observer class for GroceryHandler
    public static class GroceryListener {
        public void onGroceryItemsFetched(List<GroceryItem> items) { }
        public void onGroceryItemVolunteered() { }
        public void onGroceryItemAdded() { }
        public void onGroceryItemDeleted() { }
        public void onGroceryItemEdited() { }
    }
}
