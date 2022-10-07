package com.example.chat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")

public class pushNotificationService extends FirebaseMessagingService  {

    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String CHANNEL_ID ="MESSAGE";


        String title = Objects.requireNonNull(message.getNotification()).getTitle();
        String text=message.getNotification().getBody();




        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"message notification", NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this,CHANNEL_ID).setContentTitle(title).setContentText(text).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1,notification.build());
        super.onMessageReceived(message);
    }



}
