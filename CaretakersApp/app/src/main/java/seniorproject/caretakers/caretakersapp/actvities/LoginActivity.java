package seniorproject.caretakers.caretakersapp.actvities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.fragments.LoginFragment;
import seniorproject.caretakers.caretakersapp.handlers.AccountHandler;


public class LoginActivity extends ActionBarActivity {

    AccountHandler.AccountListener mAccountListener = new AccountHandler.AccountListener() {
        @Override
        public void onLoggedIn() {
            Log.i("LOGGED IN", "LOGGED IN");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
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
        AccountHandler.getInstance().addAccountListener(mAccountListener);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_holder, new LoginFragment(), "login_fragment")
                    .commit();
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
    public void onDestroy() {
        super.onDestroy();
        AccountHandler.getInstance().removeAccountListener(mAccountListener);
    }
}