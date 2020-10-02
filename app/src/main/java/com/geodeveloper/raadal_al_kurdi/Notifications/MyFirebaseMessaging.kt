package com.geodeveloper.newtobetting.Notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.geodeveloper.raadal_al_kurdi.HomeActivity
import com.geodeveloper.raadal_al_kurdi.ListentToQuranActivity
import com.geodeveloper.raadal_al_kurdi.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessaging : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val sented = p0.data["sented"]
        val user = p0.data["user"]
        val sharePref = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val currentOnlineUser = sharePref.getString("currentUser", "none")
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(p0)
                } else {
                    sendNotification(p0)
                }
        }
    }
    private fun sendNotification(mRemoteMessage: RemoteMessage) {
        val user = mRemoteMessage.data["user"]
        val icon = mRemoteMessage.data["icon"]
        val title = mRemoteMessage.data["title"]
        val body = mRemoteMessage.data["body"]

        val notification = mRemoteMessage.notification
        val j = user!!.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, HomeActivity::class.java)
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("body", body)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT)
//        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationSound = Uri.parse("android.resource://com.geodeveloper.raadal_al_kurdi/${com.geodeveloper.raadal_al_kurdi.R.raw.notif_sound_goes}")
        val yaseerDosariImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.sudaisi)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                .setSmallIcon(icon!!.toInt())
                .setContentTitle(title)
                .setContentText(body)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)
                .setLargeIcon(yaseerDosariImage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        val noti = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var i = 0
        if (j > 0) {
            i = j
        }
       noti.notify(i,builder.build())
    }

    private fun sendOreoNotification(mRemoteMessage: RemoteMessage) {
        val user = mRemoteMessage.data["user"]
        val icon = mRemoteMessage.data["icon"]
        val title = mRemoteMessage.data["title"]
        val body = mRemoteMessage.data["body"]

        val notification = mRemoteMessage.notification
        val j = user!!.replace("[\\D]".toRegex(), "").toInt()
        val intent = Intent(this, HomeActivity::class.java)
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("body", body)
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationSound = Uri.parse("android.resource://com.geodeveloper.raadal_al_kurdi/${R.raw.notif_sound_goes}")
        val oreoNotification = OreoNotification(this)
        val builder: Notification.Builder = oreoNotification.getOreoNotification(title, body, pendingIntent, notificationSound, icon)
        var i = 0
        if (j > 0) {
            i = j
        }
        oreoNotification.getManager!!.notify(i, builder.build())
    }
}