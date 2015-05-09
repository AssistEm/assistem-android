package seniorproject.caretakers.caretakersapp.data.handlers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.tempdata.apis.BaseJsonResponseHandler;
import seniorproject.caretakers.caretakersapp.tempdata.apis.NoNetworkException;
import seniorproject.caretakers.caretakersapp.tempdata.apis.PingRestClient;
import seniorproject.caretakers.caretakersapp.tempdata.model.Event;
import seniorproject.caretakers.caretakersapp.tempdata.model.Ping;
import seniorproject.caretakers.caretakersapp.tempdata.model.User;
import seniorproject.caretakers.caretakersapp.ui.actvities.LoginActivity;

/**
 * Handler for Pinging. Offers a singleton class to initiate and process pings.
 */
public class PingHandler {

    //Broadcast ID of the PendingIntents for notification actions
    private static final String BROADCAST = "seniorproject.caretakers.caretakersapp.pushresponse";

    //Vibration pattern of the notification
    private static final long[] VIBRATE = new long[] {0,1000};

    //Notification id of the sent ping
    private static final int SENT_PING_NOTIFICATION_ID = 10;
    //Notification id fo the received ping
    private static final int RECEIVED_PING_NOTIFICATION_ID = 20;

    //Request IDs for different actions
    private static final int ACCEPT_REQUEST = 1000;
    private static final int DEFER_REQUEST = 2000;
    private static final int DENY_REQUEST = 3000;
    private static final int CLICK_REQUEST = 4000;

    private static PingHandler mInstance;

    Context mContext;

    private PingHandler(Context context) {
        mContext = context;
    }

    /**
     * Singleton getInstance method. Either returns an existing instance of the PingHandler class
     * or constructs, sets and returns a new instance.
     * @return An instance of a PingHandler
     */
    public static PingHandler getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new PingHandler(context);
            return mInstance;
        }
        return mInstance;
    }

    /**
     * Callback object for parsing the response from a request to initiate a ping.
     */
    private class InitiatePingResponseHandler extends BaseJsonResponseHandler {

        @Override
        public void onSuccess(int resultCode, Header[] headers, JSONObject response) {
            NotificationManager notificationManager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_ping)
                    .setContentTitle("Ping sent")
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Waiting for Caretaker responses..."))
                    .setContentText("Waiting for Caretaker responses...");
            builder.setVibrate(VIBRATE);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            notificationManager.notify(SENT_PING_NOTIFICATION_ID, builder.build());
        }
    }

    /**
     * Callback object for parsing the response for responding to a request to initiate a ping
     */
    private class ResponsePingResponseHandler extends BaseJsonResponseHandler {

        @Override
        public void onSuccess(int resultCode, Header[] headers, JSONObject response) {
            try {
                JSONObject dataObj = response.getJSONObject("data");
                JSONObject messageObj = dataObj.getJSONObject("message");
                NotificationManager notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(RECEIVED_PING_NOTIFICATION_ID);
                JSONObject pingObject = messageObj.getJSONObject("ping");
                Ping ping = Ping.parsePing(pingObject);
                String patientPhone = pingObject.getString("patient_phone");
                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
                style.setSummaryText("Tap to SMS " + ping.patientName + ".");
                if(ping.description != null && !ping.description.isEmpty()) {
                    style.bigText(ping.description);
                } else {
                    style.bigText(ping.title);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_ping)
                        .setContentTitle("Assigned to Ping.")
                        .setStyle(style);
                if(ping.description != null && !ping.description.isEmpty()) {
                    builder.setContentText(ping.description);
                } else {
                    builder.setContentText(ping.title);
                }
                Intent clickedIntent = new Intent(Intent.ACTION_VIEW);
                clickedIntent.setData(Uri.parse("sms:" + patientPhone));
                PendingIntent clickedPending = PendingIntent.getActivity(mContext, 0, clickedIntent, 0);
                builder.setContentIntent(clickedPending);
                builder.setVibrate(VIBRATE);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                notificationManager.notify(RECEIVED_PING_NOTIFICATION_ID, builder.build());
            } catch(JSONException | ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCaughtFailure(int statusCode, Header[] headers, String response,
                                    Throwable e) {
            if(statusCode == 404) {
                NotificationManager manager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(RECEIVED_PING_NOTIFICATION_ID);
                NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
                style.bigText("This ping has already been taken!");
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.ic_ping)
                        .setContentTitle("This ping has already been taken!")
                        .setStyle(style);
                builder.setVibrate(VIBRATE);
                builder.setPriority(NotificationCompat.PRIORITY_MAX);
                manager.notify(RECEIVED_PING_NOTIFICATION_ID, builder.build());
            }
        }
    }

    /**
     * Public method to initiate a request to initiate a ping
     * @param context Context in which to execute the call
     * @param ping Ping object with data to send
     */
    public void initiatePing(Context context, Ping ping) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        SimpleDateFormat format = new SimpleDateFormat(Event.ISO8601DATEFORMAT);
        String time = format.format(ping.time.getTime());
        try {
            PingRestClient.initiatePing(context, communityId, ping.title, ping.description, time,
                    new InitiatePingResponseHandler());
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method to initiate a request to respond to a ping
     * @param context Context in which to execute the call
     * @param pingId ID of the ping to respond to
     * @param response Response type of the request
     */
    public void pingRespondedTo(Context context, String pingId, int response) {
        String communityId = AccountHandler.getInstance(context).getCurrentCommunity().getId();
        try {
            PingRestClient.respondToPing(context, communityId, pingId, response, new ResponsePingResponseHandler());
        } catch (NoNetworkException e) {
            e.printStackTrace();
        }
    }

    /**
     * Public method that handles parsing and displaying a JSON ping object from the server. This
     * method will call other methods to parse particular types of pings.
     * @param context Context in which to parse the ping
     * @param message JSON data of the ping object
     */
    public void pingReceived(Context context, JSONObject message) {
        Log.i("PUSH", message.toString());
        try {
            if (message.getString("type").equalsIgnoreCase("request")) {
                initialPing(context, message);
            } else if(message.getString("type").equalsIgnoreCase("fulfilled")) {
                fulfilledPing(context, message);
            } else if(message.getString("type").equalsIgnoreCase("defer")) {
                deferredPing(context, message);
            } else if(message.getString("type").equalsIgnoreCase("response")) {
                responsePing(context, message);
            } else if(message.getString("type").equalsIgnoreCase("primary")) {
                primaryPing(context, message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method to parse and display a notification for an initial request for a ping
     * @param context Context in which to display the notification
     * @param message JSON data of the ping object
     */
    private void initialPing(Context context, JSONObject message) {
        try {
            JSONObject pingObj = message.getJSONObject("ping");
            Ping ping = Ping.parsePing(pingObj);
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(RECEIVED_PING_NOTIFICATION_ID);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            if(ping.description != null && !ping.description.isEmpty()) {
                style.setSummaryText(ping.title);
                style.bigText(ping.description);
            } else {
                style.bigText(ping.title);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_ping)
                    .setContentTitle(ping.patientName + " needs help!")
                    .setStyle(style);
            if(ping.description != null && !ping.description.isEmpty()) {
                builder.setContentText(ping.description);
            } else {
                builder.setContentText(ping.title);
            }
            Intent clickedIntent = new Intent(BROADCAST);
            clickedIntent.putExtra("pingId", ping.id);
            PendingIntent clickedPending = PendingIntent.getBroadcast(context, CLICK_REQUEST, clickedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(clickedPending);
            Intent acceptIntent = new Intent(BROADCAST);
            acceptIntent.putExtra("response", 1);
            acceptIntent.putExtra("pingId", ping.id);
            Intent deferIntent = new Intent(BROADCAST);
            deferIntent.putExtra("response", 3);
            deferIntent.putExtra("pingId", ping.id);
            Intent denyIntent = new Intent(BROADCAST);
            denyIntent.putExtra("response", 2);
            denyIntent.putExtra("pingId", ping.id);
            PendingIntent acceptPending = PendingIntent.getBroadcast(context, ACCEPT_REQUEST, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent deferPending = PendingIntent.getBroadcast(context, DEFER_REQUEST, deferIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent denyPending = PendingIntent.getBroadcast(context, DENY_REQUEST, denyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action.Builder acceptBuilder =
                    new NotificationCompat.Action.Builder(R.drawable.ic_ping_accept, "Accept", acceptPending);
            NotificationCompat.Action.Builder deferBuilder =
                    new NotificationCompat.Action.Builder(R.drawable.ic_ping_defer, "Defer", deferPending);
            NotificationCompat.Action.Builder denyBuilder =
                    new NotificationCompat.Action.Builder(R.drawable.ic_ping_deny, "Deny", denyPending);
            builder.addAction(acceptBuilder.build());
            builder.addAction(deferBuilder.build());
            builder.addAction(denyBuilder.build());
            builder.setVibrate(VIBRATE);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            manager.notify(RECEIVED_PING_NOTIFICATION_ID, builder.build());
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method to parse and display a notification that a ping was fufilled
     * @param context Context in which to display the notification
     * @param message JSON data of the ping object
     */
    private void fulfilledPing(Context context, JSONObject message) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(RECEIVED_PING_NOTIFICATION_ID);
    }

    /**
     * Private method to parse and display a notification that a ping was deferred and has
     * returned to this user.
     * @param context Context to display the notification in
     * @param message JSON data of the ping object
     */
    private void deferredPing(Context context, JSONObject message) {
        try {
            JSONObject pingObj = message.getJSONObject("ping");
            Ping ping = Ping.parsePing(pingObj);
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(RECEIVED_PING_NOTIFICATION_ID);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            if(ping.description != null && !ping.description.isEmpty()) {
                style.setSummaryText(ping.title);
                style.bigText(ping.description);
            } else {
                style.bigText(ping.title);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_ping)
                    .setContentTitle(ping.patientName + " still needs help!")
                    .setStyle(style);
            if(ping.description != null && !ping.description.isEmpty()) {
                builder.setContentText(ping.description);
            } else {
                builder.setContentText(ping.title);
            }
            Intent clickedIntent = new Intent(BROADCAST);
            clickedIntent.putExtra("pingId", ping.id);
            clickedIntent.putExtra("deferred", true);
            PendingIntent clickedPending = PendingIntent.getBroadcast(context, CLICK_REQUEST, clickedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(clickedPending);
            Intent acceptIntent = new Intent(BROADCAST);
            acceptIntent.putExtra("response", 1);
            acceptIntent.putExtra("pingId", ping.id);
            Intent denyIntent = new Intent(BROADCAST);
            denyIntent.putExtra("response", 2);
            denyIntent.putExtra("pingId", ping.id);
            PendingIntent acceptPending = PendingIntent.getBroadcast(context, ACCEPT_REQUEST, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent denyPending = PendingIntent.getBroadcast(context, DENY_REQUEST, denyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Action.Builder acceptBuilder =
                    new NotificationCompat.Action.Builder(R.drawable.ic_ping_accept, "Accept", acceptPending);
            NotificationCompat.Action.Builder denyBuilder =
                    new NotificationCompat.Action.Builder(R.drawable.ic_ping_deny, "Deny", denyPending);
            builder.addAction(acceptBuilder.build());
            builder.addAction(denyBuilder.build());
            builder.setVibrate(VIBRATE);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            manager.notify(RECEIVED_PING_NOTIFICATION_ID, builder.build());

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method to parse and display a notification that a ping was responded too
     * @param context Context in which to display the notification
     * @param message JSON data of the ping object
     */
    private void responsePing(Context context, JSONObject message) {
        try {
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(SENT_PING_NOTIFICATION_ID);
            JSONObject userObject = message.getJSONObject("user");
            User user = User.parseUser(userObject);
            String notification = user.getDisplayName();
            notification += " has volunteered to help.";
            Intent clickedIntent = new Intent(Intent.ACTION_VIEW);
            clickedIntent.setData(Uri.parse("sms:" + user.getPhone()));
            PendingIntent clickedPending = PendingIntent.getActivity(context, 0, clickedIntent, 0);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.setSummaryText("Tap to SMS.");
            style.bigText(notification);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_ping)
                    .setContentTitle(notification)
                    .setStyle(style)
                    .setContentText("Tap to SMS.")
                    .setContentIntent(clickedPending);
            builder.setVibrate(VIBRATE);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            manager.notify(SENT_PING_NOTIFICATION_ID, builder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private method to parse and display a notification that a ping has been defaulted to the
     * primary caretaker, which should be this user
     * @param context Context in which to display the notification
     * @param message JSON data containing the ping
     */
    private void primaryPing(Context context, JSONObject message) {
        try {
            NotificationManager manager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(RECEIVED_PING_NOTIFICATION_ID);
            JSONObject pingObject = message.getJSONObject("ping");
            Ping ping = Ping.parsePing(pingObject);
            String patientPhone = pingObject.getString("patient_phone");
            Intent clickedIntent = new Intent(Intent.ACTION_VIEW);
            clickedIntent.setData(Uri.parse("sms:" + patientPhone));
            PendingIntent clickedPending = PendingIntent.getActivity(context, 0, clickedIntent, 0);
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
            style.setSummaryText("No one was available.");
            if(ping.description != null && !ping.description.isEmpty()) {
                style.setSummaryText(ping.title);
                style.bigText(ping.description);
            } else {
                style.bigText(ping.title);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.ic_ping)
                    .setContentTitle(ping.patientName + " needs help!")
                    .setStyle(style);
            if(ping.description != null && !ping.description.isEmpty()) {
                builder.setContentText(ping.description);
            } else {
                builder.setContentText(ping.title);
            }
            builder.setVibrate(VIBRATE);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setContentIntent(clickedPending);
            manager.notify(RECEIVED_PING_NOTIFICATION_ID, builder.build());
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
