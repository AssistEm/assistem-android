package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.accounts.Account;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.fragments.CalendarFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.DrawerFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.GroceryFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.GroceryTabsFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.PingFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.SettingsFragment;

public class MainActivity extends BaseActivity implements DrawerFragment.NavigationDrawerCallbacks {

    private DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(LoginActivity.EXIT_APP_RESULT);
        mDrawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!mDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        switch(position) {
            case 0:
                fragment = new CalendarFragment();
                break;
            case 1:
                fragment = new GroceryTabsFragment();
                break;
            case 2:
                if(AccountHandler.getInstance(this).getCurrentUser() instanceof Patient) {
                    fragment = new PingFragment();
                } else {
                    fragment = new SettingsFragment();
                }
                break;
            case 3:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new CalendarFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(!mDrawerFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
