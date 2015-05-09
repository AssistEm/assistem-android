package seniorproject.caretakers.caretakersapp.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import seniorproject.caretakers.caretakersapp.data.handlers.PingHandler;

public class PushResponseBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String pingId = intent.getStringExtra("pingId");
        if(intent.hasExtra("response")) {
            Log.i("RESPONSE", intent.getExtras().toString());
            int response = intent.getIntExtra("response", 2);
            PingHandler.getInstance(context).pingRespondedTo(context, pingId, response);
        } else {
            Log.i("WHAT?", "WHAT?");
        }
        completeWakefulIntent(intent);
    }
}
