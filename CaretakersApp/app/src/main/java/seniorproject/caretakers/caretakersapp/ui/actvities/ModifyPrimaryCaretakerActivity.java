package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Community;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;
import seniorproject.caretakers.caretakersapp.ui.adapters.PrimaryCaretakerAdapter;
/**
 * Created by sarah on 3/10/16. An activity for the patient to modify the primary caretaker in the community tab
 */
public class ModifyPrimaryCaretakerActivity extends BaseActivity {

    ListView mCaretakerList;
    PrimaryCaretakerAdapter mPrimaryCaretakerAdapter;
    Button mSubmitButton;
    ArrayList<String> caretakers;

    AccountHandler.AccountListener mListener = new AccountHandler.AccountListener() {

        @Override
        public void onFullProfileUserFetched(JSONObject profile) {
            try {
                String type = profile.getString("type");
                if (type.equals("caretaker")) {
                    User caretaker = User.parseUser(profile);
                    if (mPrimaryCaretakerAdapter.getPosition(caretaker) == -1) {
                        mPrimaryCaretakerAdapter.addCaretaker(caretaker);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPrimaryCaretakerSet() {
            //Send the updated primary caretaker back to the fragment
            Intent data = new Intent();
            data.putExtra("primary", mPrimaryCaretakerAdapter.getPrimaryCaretaker().getId());
            int resultCode = 2;
            setResult(resultCode, data);
            //Close the activity
            finish();
        }
    };

    private View.OnClickListener mSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mPrimaryCaretakerAdapter.getPrimaryCaretaker() != null) {
                AccountHandler.getInstance(ModifyPrimaryCaretakerActivity.this)
                        .setPrimaryCaretaker(ModifyPrimaryCaretakerActivity.this, mPrimaryCaretakerAdapter.getPrimaryCaretaker().getId(),
                                mListener); //TODO: make sure happens before the notifyDataSetChanged code
                return;
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrimaryCaretakerAdapter = new PrimaryCaretakerAdapter(this);
        mCaretakerList = (ListView) findViewById(R.id.primary_caretaker_list);
        mCaretakerList.setAdapter(mPrimaryCaretakerAdapter);
        mSubmitButton = (Button) findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(mSubmitClickListener);
        mCaretakerList.setEmptyView(findViewById(R.id.primary_empty_view));
        AccountHandler handler = AccountHandler.getInstance(this);
        Community currentCommunity = handler.getCurrentCommunity();
        caretakers = new ArrayList<>();
        //get caretakers
        ArrayList<String> caretakerIds = currentCommunity.getCaretakerIds();
        for (int i = 0; i < caretakerIds.size(); i++) {
            String caretaker = caretakerIds.get(i);
            handler.getFullProfileUser(this, caretaker, mListener);
            caretakers.add(caretakerIds.get(i));
        }

    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_modify_primary_caretaker;
    }
}
