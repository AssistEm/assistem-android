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

import seniorproject.caretakers.caretakersapp.data.model.User;

public class GroceryItem implements Serializable {

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

    @Override
    public String toString() {
        return getTitle();
    }

    public final static Comparator<GroceryItem> ASC_NAME_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem.getTitle().compareToIgnoreCase(groceryItem2.getTitle());
        }
    };

    public final static Comparator<GroceryItem> DESC_NAME_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem2.getTitle().compareToIgnoreCase(groceryItem.getTitle());
        }
    };

    public final static Comparator<GroceryItem> DESC_URGENCY_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem.getUrgency().compareTo(groceryItem2.getUrgency());
        }
    };

    public final static Comparator<GroceryItem> ASC_URGENCY_COMPARE = new Comparator<GroceryItem>() {
        @Override
        public int compare(GroceryItem groceryItem, GroceryItem groceryItem2) {
            return groceryItem2.getUrgency().compareTo(groceryItem.getUrgency());
        }
    };

    public static interface FilterGroceryItem {
        public boolean isInFilter(GroceryItem item, User currentUser);
    }

    public final static FilterGroceryItem mAllItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return true;
        }
    };

    public final static FilterGroceryItem mTakenItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return item.getVolunteer() != null;
        }
    };

    public final static FilterGroceryItem mYourItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return item.getVolunteer() != null &&
                    item.getVolunteer().getId().equals(currentUser.getId());
        }
    };

    public final static FilterGroceryItem mNotTakenItems = new FilterGroceryItem() {
        @Override
        public boolean isInFilter(GroceryItem item, User currentUser) {
            return item.getVolunteer() == null;
        }
    };

    public final static FilterGroceryItem[] mFilters = {mAllItems, mNotTakenItems, mTakenItems, mYourItems};
}