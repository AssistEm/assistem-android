package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.ui.fragments.LoginFragment;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

/**
 * Activity that presents the UI for logging into Assist'em
 */
public class LoginActivity extends ActionBarActivity {

    public final static int LOGGED_IN_REQUEST = 1;

    public final static int EXIT_APP_RESULT = 10;
    public final static int AUTHENTICATION_ERROR_RESULT = 20;
    public final static int LOGGED_OUT_RESULT = 30;

    AccountHandler mAccountHandler;

    /**
     * AccountListener for recieving the results of a Login or Registration request
     */
    AccountHandler.AccountListener mAccountListener = new AccountHandler.AccountListener() {
        @Override
        public void onLoggedIn() {
            loggedIn();
        }

        @Override
        public void onRegistered() {
            loggedIn();
        }
    };


    /**
     * Callback for when an activity is created. Attaches the AccountListener to the AccountHandler
     * to allow for callbacks for login or registration status
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccountHandler = AccountHandler.getInstance(this);
        mAccountHandler.addAccountListener(mAccountListener);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, new LoginFragment(), "login_fragment")
                    .commit();
        }
        if (mAccountHandler.getLocalLoggedIn()) {
            loggedIn();
        }
    }

    /**
     * Method executed when the Activity is done. Clears the AccountListeners attached to the
     * AccountHandler
     */
    @Override
    public void finish() {
        super.finish();
        if(mAccountHandler != null) {
            mAccountHandler.clearAccountListeners();
        }
    }

    /**
     * Activity result callback for when child activities finish. If the finished activity was
     * stopped because it was actually exited, exit this activity too.
     * @param requestCode Original request value for the activity that finished
     * @param resultCode Result value for the activity that finished
     * @param data Data in which the activity was started.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGGED_IN_REQUEST) {
            switch(resultCode) {
                case EXIT_APP_RESULT:
                    finish();
                    break;
            }
        }
    }

    /**
     * Private method for when the user is logged in. Launches the MainActivity.
     */
    private void loggedIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, LOGGED_IN_REQUEST);
    }
}