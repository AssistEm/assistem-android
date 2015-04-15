package seniorproject.caretakers.caretakersapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Stephen on 4/10/2015.
 */
public class GroceryItem extends ApiItem implements Serializable {

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("quantity")
    String quantity;

    @SerializedName("location")
    String location;

    @SerializedName("urgency")
    Calendar urgency;

    @SerializedName("volunteer")
    User volunteer;

    public GroceryItem(String title, String description, String quantity, String location, Calendar urgency) {
        this.title = title;
        this.description = description;
        this.quantity = quantity;
        this.location = location;
        this.urgency = urgency;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public Calendar getUrgency() {
        return urgency;
    }

    public User getVolunteer() {
        return volunteer;
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
