package seniorproject.caretakers.caretakersapp.views;

import java.util.List;

import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;

/**
 * Created by Stephen on 4/15/2015.
 */
public interface GroceryView extends ViewInterface {

    public void onGroceryItemsFetched(List<GroceryItem> items);

    public void onError(String error);
}
