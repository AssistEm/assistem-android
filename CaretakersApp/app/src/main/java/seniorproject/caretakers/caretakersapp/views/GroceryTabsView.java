package seniorproject.caretakers.caretakersapp.views;

import seniorproject.caretakers.caretakersapp.data.model.GroceryItem;

/**
 * Created by Stephen on 4/15/2015.
 */
public interface GroceryTabsView extends ViewInterface {

    public void onGroceryItemSelected(GroceryItem item);

}
