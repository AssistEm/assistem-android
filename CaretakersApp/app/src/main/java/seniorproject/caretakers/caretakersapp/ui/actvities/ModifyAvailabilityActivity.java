package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.model.Availability;
import seniorproject.caretakers.caretakersapp.presenters.ModifyAvailabilityPresenter;
import seniorproject.caretakers.caretakersapp.ui.adapters.AvailabilityAdapter;
import seniorproject.caretakers.caretakersapp.ui.dialogs.AddAvailabilityDialog;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;
import seniorproject.caretakers.caretakersapp.views.ModifyAvailabilityView;

public class ModifyAvailabilityActivity extends BaseActivity implements ModifyAvailabilityView {

    @Inject
    ModifyAvailabilityPresenter presenter;

    ListView mAvailabilityList;
    AvailabilityAdapter mAdapter;
    Button mSubmitButton;
    AddFloatingActionButton mAddAvailabilityButton;

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.setAvailability(mAdapter.getAvailability());
        }
    };

    private View.OnClickListener mAddAvailabilityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddAvailabilityDialog dialog = new AddAvailabilityDialog();
            dialog.show(ModifyAvailabilityActivity.this.getSupportFragmentManager(), "add_availability_dialog");
        }
    };

    /*
    private AddAvailabilityDialog.AddAvailabilityListener mAddListener =
            new AddAvailabilityDialog.AddAvailabilityListener() {
        @Override
        public void onAvailabilityAdded(Availability availability) {
            mAdapter.addAvailability(availability);
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CaretakersApplication app = (CaretakersApplication) this.getApplication();
        app.inject(this);
        mAvailabilityList = (ListView) findViewById(R.id.availability_list);
        mAdapter = new AvailabilityAdapter(this);
        mAvailabilityList.setAdapter(mAdapter);
        mAvailabilityList.setEmptyView(findViewById(R.id.empty_view));
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(mSubmitClickListener);
        mAddAvailabilityButton = (AddFloatingActionButton) findViewById(R.id.add_availability_button);
        mAddAvailabilityButton.setOnClickListener(mAddAvailabilityClickListener);
        presenter.retrieveAvailability();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_modify_availability;
    }

    @Override
    public void onReceiveAvailability(List<Availability> availabilities) {
        mAdapter.addAvailability(availabilities);
    }

    @Override
    public void onAvailabilitySet() {
        finish();
    }
}
