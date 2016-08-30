package com.optomiadoctor.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.optomiadoctor.R;

/**
 * Created by vikas.patil on 5/28/2016.
 */
public class PushNotificationService extends GcmListenerService {
    Context objContext;
    int countNotification = 0;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String strMessage = data.getString("message");
        String strTitle = data.getString("title");
        System.out.println("DATA MESSAGE :::" + data.toString() + "  From :::" + from);

        // notifyUser(getApplicationContext(),strTitle,strMessage);
        sendNotification(strTitle, strMessage);
    }


    private void sendNotification(String strTitle, String message) {
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_disp_notoification)
                .setContentTitle(strTitle)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(countNotification++, notificationBuilder.build());
    }
}
