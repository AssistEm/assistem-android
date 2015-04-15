package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.views.BaseView;

public abstract class BaseActivity extends ActionBarActivity implements BaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onLogout() {
        finish();
    }

    @Override
    public void onAuthError() {
        setResult(LoginActivity.AUTHENTICATION_ERROR_RESULT);
        finish();
    }

    public abstract int getLayoutResource();

}