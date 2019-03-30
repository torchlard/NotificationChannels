/*
* Copyright 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.notificationchannels;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
//import android.support.v4.app.RemoteInput;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
//import android.support.v4.app.NotificationCompat;


/**
 * Helper class to manage notification channels, and create notifications.
 */
class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String PRIMARY_CHANNEL = "default";
    public static final String SECONDARY_CHANNEL = "second";
    public static final String THIRD_CHANNEL = "Third";

    private static final String KEY_TEXT_REPLY = "key_text_reply";


    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param ctx The application context
     */
    public NotificationHelper(Context ctx) {
        super(ctx);

        NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
            getString(R.string.noti_channel_default), NotificationManager.IMPORTANCE_DEFAULT);
        chan1.setLightColor(Color.GREEN);
        chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(chan1);

        NotificationChannel chan2 = new NotificationChannel(SECONDARY_CHANNEL,
            getString(R.string.noti_channel_second), NotificationManager.IMPORTANCE_HIGH);
        chan2.setLightColor(Color.BLUE);
        chan2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(chan2);

        NotificationChannel chan3 = new NotificationChannel(THIRD_CHANNEL,
            getString(R.string.noti_channel3), NotificationManager.IMPORTANCE_DEFAULT);
        chan3.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(chan3);
    }

    /**
     * Get a notification of type 1
     *
     * Provide the builder rather than the notification it's self as useful for making notification
     * changes.
     *
     * @param title the title of the notification
     * @param body the body text for the notification
     * @return the builder as it keeps a reference to the notification (since API 24)
    */
    public Notification.Builder getNotification1(String title, String body) {
        return new Notification.Builder(getApplicationContext(), PRIMARY_CHANNEL)
                 .setContentTitle(title)
                 .setContentText(body)
                 .setSmallIcon(getSmallIcon())
                 .setAutoCancel(true);
    }

    /**
     * Build notification for secondary channel.
     *
     * @param title Title for notification.
     * @param body Message for notification.
     * @return A Notification.Builder configured with the selected channel and details
     */
    public Notification.Builder getNotification2(String title, String body) {
        return new Notification.Builder(getApplicationContext(), SECONDARY_CHANNEL)
                 .setContentTitle(title)
                 .setContentText(body)
                 .setSmallIcon(getSmallIcon())
                 .setAutoCancel(true);
    }

    public Notification.Builder getNotification3(String title, String body){
        Intent intent = new Intent(this, MainActivity.class);
//        preserve user's expected navigation experience
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//        Intent intent2 = new Intent(this, MainActivity.class);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent2.putExtra("text", "hello world");
//        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, intent2, 0);
//
//        Notification.Action action = new Notification.Action.Builder (
//            R.drawable.ic_assignment_black_24dp, "asssign" , pendingIntent2 ).build();

//        =================
        String replyLabel = "nice to reply u";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel).build();

        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
//            conversation.getConversationId(),
//            getMessageReplyIntent(conversation.getConversationId()),
            0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT );

        Notification.Action action2 = new Notification.Action.Builder(
            R.drawable.ic_assignment_black_24dp,
            "assigns", replyPendingIntent
        ).addRemoteInput(remoteInput).build();


        return new Notification.Builder(getApplicationContext(), THIRD_CHANNEL)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(getSmallIcon())
//            .setContentIntent(pendingIntent2)
            .addAction(action2)
            .setAutoCancel(false);
    }

    /**
     * Send a notification.
     *
     * @param id The ID of the notification
     * @param notification The notification object
     */
    public void notify(int id, Notification.Builder notification) {
        getManager().notify(id, notification.build());
    }

    /**
     * Get the small icon for this app
     *
     * @return The small icon resource id
     */
    private int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    /**
     * Get the notification manager.
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
