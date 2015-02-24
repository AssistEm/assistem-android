package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public abstract class BaseActivity extends ActionBarActivity {

    protected AccountHandler mAccountHandler;

    protected final AccountHandler.AccountListener mAccountListener =
            new AccountHandler.AccountListener() {
                @Override
                public void onLogout() {
                    finish();
                }

                @Override
                public void onAuthenticationError() {
                    setResult(LoginActivity.AUTHENTICATION_ERROR);
                    finish();
                }
            };

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAccountHandler.removeAccountListener(mAccountListener);
    }

}