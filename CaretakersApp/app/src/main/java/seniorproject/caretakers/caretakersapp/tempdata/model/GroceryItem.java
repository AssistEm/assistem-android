package seniorproject.caretakers.caretakersapp.tempdata.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Filter;

/**
 * Class that represents the Grocery Item model
 */
public class GroceryItem implements Serializable {

    //Date format string for the ISO8601 standard for time strings
    public final static String ISO8601DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private String mId;
    private String mTitle;
    private String mDescription;
    private String mQuantity;
    private String mLocation;
    private Calendar mUrgency;
    private User mVolunteer;
    private Calendar mDeliveryDate;

    public GroceryItem(String id, String title, String description, String quantity,
                       String location, Calendar urgency, User volunteer, Calendar delivery) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mQuantity = quantity;
        mLocation = location;
        mUrgency = urgency;
        mVolunteer = volunteer;
        mDeliveryDate = delivery;
    }

    /**
     * Static method to parse a JSON object into an GroceryItem object
     * @param object JSON representation of the GroceryItem object
     * @return GroceryItem representation of the parsed input
     * @throws JSONException Thrown when the input is not of the expected format
     */
    public static GroceryItem parseGroceryItem(JSONObject object) throws JSONException {
        String id = object.getString("_id");
        String title = object.getString("title");
        String description = object.getString("description");
        String quantity = object.getString("quantity");
        String location = object.getString("location");
        String urgencyString = object.getString("urgency");
        Calendar urgency = parseTime(urgencyString);
        Calendar deliveryTime = null;
        User volunteer = null;
        if(object.has("volunteer") || !object.isNull("volunteer")) {
            JSONObject volunteerObject = object.getJSONObject("volunteer");
            JSONObject volunteerJson = volunteerObject.getJSONObject("volunteer_id");
            volunteer = User.parseUser(volunteerJson);
            String deliveryString = volunteerObject.getString("delivery_time");
            deliveryTime = parseTime(deliveryString);
        }
        return new GroceryItem(id, title, description, quantity, location, urgency, volunteer,
                deliveryTime);
    }

    /**
     * Static method to parse a time string into a Calendar instance
     * @param time Time string to parse
     * @return Calendar instance from the time string
     */
    private static Calendar parseTime(String time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()) ;
        SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601DATEFORMAT, Locale.getDefault());
        try {
            Date date = dateFormat.parse(time);
            date.setHours(date.getHours());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public String getLocation() {
        return mLocation;
    }

    public Calendar getUrgency() {
        return mUrgency;
    }

    public User getVolunteer() {
        return mVolunteer;
    }

    public Calendar getDeliveryDate() {
        return mDeliveryDate;
    }

    /**
     * Override of the toString method, in this case it returns just the title of the item.
     * @return
     */
    @Override
    public String toString() {
        return getTitle();
    }

    //Comparator that sorts GroceryItems by ascending name
    public final static Comparator<GroceryItem> ASC_NAME_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem.getTitle().compareToIgnoreCase(groceryItem2.getTitle());
        }
    };

    //Comparator that sorts GroceryItems by descending name
    public final static Comparator<GroceryItem> DESC_NAME_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem2.getTitle().compareToIgnoreCase(groceryItem.getTitle());
        }
    };

    //Comparator that sorts GroceryItems by descending urgency
    public final static Comparator<GroceryItem> DESC_URGENCY_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem.getUrgency().compareTo(groceryItem2.getUrgency());
        }
    };

    //Comparator that sorts GroceryItems by ascending urgency
    public final static Comparator<GroceryItem> ASC_URGENCY_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem2.getUrgency().compareTo(groceryItem.getUrgency());
        }
    };

    //Interface that is used to implement a filter on GroceryItems. Used to sort items into categories
    public static interface FilterGroceryItem {
        public boolean isInFilter(GroceryItem item, User currentUser);
    }

    //Instance of a FilterGroceryItem that includes all items
    public final static FilterGroceryItem mAllItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return true;
        }
    };

    //Instance of a FilterGroceryItem that only shows items that have already been taken
    public final static FilterGroceryItem mTakenItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return item.getVolunteer() != null;
        }
    };

    //Instance of a FilterGroceryItem that only shows items that the user has volunteered for
    public final static FilterGroceryItem mYourItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return item.getVolunteer() != null &&
                    item.getVolunteer().getId().equals(currentUser.getId());
        }
    };

    //Instance of a FilterGroceryItem that only shows items that have not been taken
    public final static FilterGroceryItem mNotTakenItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return item.getVolunteer() == null;
        }
    };

    //Static array of the filter instances
    public final static FilterGroceryItem[] mFilters = {mAllItems, mNotTakenItems, mTakenItems, mYourItems};
}