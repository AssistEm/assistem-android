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


public class LoginActivity extends ActionBarActivity {

    public final static int LOGGED_IN_REQUEST = 1;

    public final static int EXIT_APP_RESULT = 10;
    public final static int AUTHENTICATION_ERROR_RESULT = 20;
    public final static int LOGGED_OUT_RESULT = 30;

    AccountHandler mAccountHandler;

    AccountHandler.AccountListener mAccountListener = new AccountHandler.AccountListener() {
        @Override
        public void onLoggedIn() {
            loggedIn();
        }

        @Override
        public void onRegistered() {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccountHandler = AccountHandler.getInstance(this);
        mAccountHandler.addAccountListener(mAccountListener);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, new LoginFragment(), "login_fragment")
                    .commit();
        }
        if(mAccountHandler.getLocalLoggedIn()) {
            loggedIn();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        if(mAccountHandler != null) {
            mAccountHandler.clearAccountListeners();
        }
    }

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

    private void loggedIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, LOGGED_IN_REQUEST);
    }
}