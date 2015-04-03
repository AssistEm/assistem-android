package seniorproject.caretakers.caretakersapp.presenters;

import android.view.View;

import seniorproject.caretakers.caretakersapp.ui.interfaces.ViewInterface;

/**
 * Created by Stephen on 3/31/2015.
 */
public interface Presenter<T extends ViewInterface> {

    void setView(T view);

}
