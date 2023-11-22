package com.example.fiebasephoneauth;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.fiebasephoneauth.Guardian.page.GuardianEventLogDetail;
import com.example.fiebasephoneauth.Guardian.page.GuardianMenuEventFragment;
import com.example.fiebasephoneauth.login.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("MSG",remoteMessage.getNotification().getBody());
        shownotification(remoteMessage.getNotification());

    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }
    public void shownotification(RemoteMessage.Notification message){


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.example.fiebasephoneauth"; //your app package name

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {           // 채널은 앱 target이 Oreo 이상이면 해야 함
            // 푸시 전용 채널 -> 해당 채널은 그룹핑할 수 있어 사용자가 직접 푸시 그룹들로 묶인 채널을 받을지 안받을지 제어할 수 있도록 하는 역할
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Techrush Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        //푸시를 클릭했을때 이동// -,
        Intent intent = new Intent(this, GuardianEventLogDetail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.emer)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setContentInfo("Info")
                .setContentIntent(pendingIntent); // 클릭 시 이동할 인텐트 추가


        // bulid()메소드를 통해 알림 생성, 생성된 알림을 사용자에게 보여주기 위해 notify() 메소드 사용
        notificationManager.notify(0,notificationBuilder.build());


    }

}





