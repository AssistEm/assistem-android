package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.dialogs.PingDialog;
import seniorproject.caretakers.caretakersapp.ui.fragments.CalendarFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.CommunityFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.DrawerFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.GroceryTabsFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.LocationFragment;
import seniorproject.caretakers.caretakersapp.ui.fragments.SettingsFragment;

/**
 * Activity that presents the main UI of the app, including the navigation drawer
 */
public class MainActivity extends BaseActivity implements DrawerFragment.NavigationDrawerCallbacks {

    private DrawerFragment mDrawerFragment;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    /**
     * Callback for when the activity is started. Instantiates the DrawerFragment and sets it up.
     * Also checks for play services for registering to GCM for Pinging purposes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(LoginActivity.EXIT_APP_RESULT);
        mDrawerFragment = (DrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if(checkPlayServices()) {
            AccountHandler.getInstance(this).registerGCM();
        } else {
            Log.i("GCM", "No valid Google Play Services APK found.");
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    /**
     * Callback for when an item is selected from the navigation drawer. Swaps the currently shown
     * fragment out when necessary. This is hardcoded on the position that is selected and should
     * be updated when more tabs are created.
     * @param position Position selected
     * @return If the event was handled and consumed and if the UI should update accordingly
     */
    @Override
    public boolean onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;

        switch(position) {
            case 0:
                fragment = new CalendarFragment();
                break;
            case 1:
                fragment = new GroceryTabsFragment();
                break;
            case 2:
                fragment = new CommunityFragment();
                break;
            case 3:
                fragment = new LocationFragment();
                break;
            case 4:
                if(AccountHandler.getInstance(this).getCurrentUser() instanceof Patient) {
                    PingDialog dialog = new PingDialog();
                    dialog.show(getSupportFragmentManager(), "ping_dialog");
                    return false;
                } else {
                    fragment = new SettingsFragment();
                }
                break;
            case 5:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new CalendarFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        return true;
    }

    /**
     * Callback for when the user presses back. First check with the DrawerFragment to see
     * if the navigation drawer is open before deciding whether to default to ending this activity.
     */
    @Override
    public void onBackPressed() {
        if(!mDrawerFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    /**
     * Callback when when the user returns to the app. Checks for Google Play Services again.
     */
    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Private method that checks for Google Play Services
     * @return If Play Services is available
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
