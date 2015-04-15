package seniorproject.caretakers.caretakersapp.views;

/**
 * Created by Stephen on 4/10/2015.
 */
public interface AddGroceryItemView extends ViewInterface {

    public void onGroceryItemAdded();

    void onError(String error);
}
