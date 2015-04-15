package seniorproject.caretakers.caretakersapp.presenters;

import seniorproject.caretakers.caretakersapp.views.ViewInterface;

/**
 * Created by Stephen on 3/31/2015.
 */
public interface Presenter<T extends ViewInterface> {

    void setView(T view);

}
