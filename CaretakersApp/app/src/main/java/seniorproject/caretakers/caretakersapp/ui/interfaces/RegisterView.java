package seniorproject.caretakers.caretakersapp.ui.interfaces;

/**
 * Created by Stephen on 3/31/2015.
 */
public interface RegisterView extends ViewInterface {

    public void onRegisterSuccess();

    public void onRegisterFailed(String error);

    public void onCaretakerStatusChanged(boolean isCaretaker);
}
