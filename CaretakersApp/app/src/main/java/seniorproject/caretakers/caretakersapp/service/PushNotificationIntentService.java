package seniorproject.caretakers.caretakersapp.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.PingHandler;
import seniorproject.caretakers.caretakersapp.ui.actvities.LoginActivity;

/**
 * IntentService that parses Pings as they arrive from GCM
 */
public class PushNotificationIntentService extends IntentService {

    public PushNotificationIntentService() {
        super("PushNotificationService");
    }

    public PushNotificationIntentService(String name) {
        super(name);
    }

    /**
     * Method called when the service is launched. Parses the type of message, and if it is
     * not an error, it sends the data to the PingHandler for parsing.
     * @param intent Intent with which the server is launched
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if(!extras.isEmpty()) {
            if(GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

            } else if(GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {

            } else if(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String message = extras.getString("message");
                try {
                    JSONObject messageObj = new JSONObject(message);
                    PingHandler.getInstance(this).pingReceived(this, messageObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
