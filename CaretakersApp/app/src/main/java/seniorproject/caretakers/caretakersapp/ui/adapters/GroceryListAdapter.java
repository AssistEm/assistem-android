package seniorproject.caretakers.caretakersapp.ui.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;

public class GroceryListAdapter extends ArrayAdapter<GroceryItem> {

    List<GroceryItem> mGroceryItems;

    public GroceryListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public GroceryListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public GroceryListAdapter(Context context, int resource, GroceryItem[] objects) {
        super(context, resource, objects);
    }

    public GroceryListAdapter(Context context, int resource, int textViewResourceId, GroceryItem[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public GroceryListAdapter(Context context, int resource, List<GroceryItem> objects) {
        super(context, resource, objects);
    }

    public GroceryListAdapter(Context context, int resource, int textViewResourceId, List<GroceryItem> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}