package seniorproject.caretakers.caretakersapp.ui.actvities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import seniorproject.caretakers.caretakersapp.R;

public class MainActivity extends ActionBarActivity {

    TextView mType;
    TextView mFirstName;
    TextView mLastName;
    TextView mEmail;
    TextView mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mType = (TextView) findViewById(R.id.type);
        mFirstName = (TextView) findViewById(R.id.first_name);
        //mLastName = (TextView) findViewById(R.id.last_name);
        mEmail = (TextView) findViewById(R.id.email);
        mPhone = (TextView) findViewById(R.id.phone);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        type = type.substring(0,1).toUpperCase() + type.substring(1, type.length());
        mType.setText(type + " Settings");
        mFirstName.setText(intent.getStringExtra("first_name") + " " + intent.getStringExtra("last_name"));
        mEmail.setText(intent.getStringExtra("email"));
        mPhone.setText(intent.getStringExtra("phone"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
}
