package seniorproject.caretakers.caretakersapp.views;

/**
 * Created by Stephen on 4/15/2015.
 */
public interface ViewGroceryItemView extends ViewInterface {

    public void onGroceryItemVolunteered();

    public void onGroceryItemDeleted();

    public void onGroceryItemEdited();

    public void onError(String error);
}
