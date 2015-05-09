package seniorproject.caretakers.caretakersapp.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import seniorproject.caretakers.caretakersapp.data.handlers.PingHandler;

/**
 * BroadcastReceiver for broadcasts generated when a user interacts with a notification in some
 * form. By using a BroadcastReceiver, the app does not need to be open for interactions for Pinging.
 */
public class PushResponseBroadcastReceiver extends WakefulBroadcastReceiver {

    /**
     * Method called when a broadcast is received. Passes the action onto the PingHandler for parsing.
     * @param context Context in which the broadcast is received
     * @param intent Intent with which the broadcast is made
     */
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
