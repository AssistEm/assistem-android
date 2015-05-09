package seniorproject.caretakers.caretakersapp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * BroadcastReceiver that receives GCM messages from the server and passes them on to the
 * PushNotificationIntentServer for handling
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("PUSH", "PUSH");
        ComponentName comp = new ComponentName(context.getPackageName(),
                PushNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
    }
}
