package seniorproject.caretakers.caretakersapp.views;

import seniorproject.caretakers.caretakersapp.data.model.User;

/**
 * Created by Stephen on 4/1/2015.
 */
public interface DrawerView extends ViewInterface {

    public void onUserRetrieved(User user);
}