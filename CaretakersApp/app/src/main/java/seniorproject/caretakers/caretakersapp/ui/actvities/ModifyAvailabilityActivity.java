package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Availability;
import seniorproject.caretakers.caretakersapp.ui.adapters.AvailabilityAdapter;
import seniorproject.caretakers.caretakersapp.ui.dialogs.AddAvailabilityDialog;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class ModifyAvailabilityActivity extends BaseActivity {

    ListView mAvailabilityList;
    AvailabilityAdapter mAdapter;
    Button mSubmitButton;
    AddFloatingActionButton mAddAvailabilityButton;

    AccountHandler.AccountListener mListener = new AccountHandler.AccountListener() {
        @Override
        public void onAvailabilityFetched(List<Availability> availabilities) {
            mAdapter.addAvailability(availabilities);
        }

        @Override
        public void onAvailabilitySet() {
            finish();
        }
    };

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AccountHandler.getInstance(ModifyAvailabilityActivity.this)
                    .setAvailability(ModifyAvailabilityActivity.this, mAdapter.getAvailability(),
                            mListener);
        }
    };

    private View.OnClickListener mAddAvailabilityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddAvailabilityDialog dialog = new AddAvailabilityDialog();
            dialog.setAddAvailabilityListener(mAddListener);
            dialog.show(ModifyAvailabilityActivity.this.getSupportFragmentManager(), "add_availability_dialog");
        }
    };

    private AddAvailabilityDialog.AddAvailabilityListener mAddListener =
            new AddAvailabilityDialog.AddAvailabilityListener() {
        @Override
        public void onAvailabilityAdded(Availability availability) {
            mAdapter.addAvailability(availability);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAvailabilityList = (ListView) findViewById(R.id.availability_list);
        mAdapter = new AvailabilityAdapter(this);
        mAvailabilityList.setAdapter(mAdapter);
        mAvailabilityList.setEmptyView(findViewById(R.id.empty_view));
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(mSubmitClickListener);
        mAddAvailabilityButton = (AddFloatingActionButton) findViewById(R.id.add_availability_button);
        mAddAvailabilityButton.setOnClickListener(mAddAvailabilityClickListener);
        AccountHandler.getInstance(this).getAvailability(this, mListener);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_modify_availability;
    }
}
