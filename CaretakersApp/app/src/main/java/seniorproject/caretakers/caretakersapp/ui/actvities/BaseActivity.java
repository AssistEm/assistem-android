package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

/**
 * Base activity for all activities. Forces extension of the ActionBarActivity, and provides
 * support in all subclasses for the support Toolbar class. Also forces implementation of finishing
 * of activities when an authentication error occurs
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected AccountHandler mAccountHandler;

    /**
     * AccountListener that checks for either logging out, or authentication errors and finishes
     * accordingly
     */
    protected final AccountHandler.AccountListener mAccountListener =
            new AccountHandler.AccountListener() {
                @Override
                public void onLogout() {
                    finish();
                }

                @Override
                public void onAuthenticationError() {
                    setResult(LoginActivity.AUTHENTICATION_ERROR_RESULT);
                    finish();
                }
            };

    /**
     * Callback for when the activity is created. Loads the Toolbar support class and adds the
     * AccountListener instance into the AccountHandler
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
        mAccountHandler = AccountHandler.getInstance(this);
        mAccountHandler.addAccountListener(mAccountListener);
    }

    public abstract int getLayoutResource();

    /**
     * Callback for when the activity is destroyed. Removes the AccountListener from the
     * AccountHandler
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mAccountHandler.removeAccountListener(mAccountListener);
    }

}