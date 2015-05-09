package seniorproject.caretakers.caretakersapp.tempdata.apis;

/**
 * Exception thrown when there is no network connection
 */
public class NoNetworkException extends Exception {

    public NoNetworkException() {}

    public NoNetworkException(String message) {
        super(message);
    }
}
