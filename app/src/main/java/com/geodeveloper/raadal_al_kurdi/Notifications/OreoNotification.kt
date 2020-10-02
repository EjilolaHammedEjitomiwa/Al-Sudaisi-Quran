package com.geodeveloper.newtobetting.Notifications

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import com.geodeveloper.raadal_al_kurdi.R

class OreoNotification(base: Context) : ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null

    companion object {
        private const val CHANNEL_ID = "com.geodeveloper.raadal_al_kurdi"
        private const val CHANNEL_NAME = "Raadal Kurdi Quran"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.notif_sound_goes)
        val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        val channerl = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        channerl.enableLights(false)
        channerl.enableVibration(true)
        channerl.setSound(soundUri, audioAttributes);
        channerl.importance = NotificationManager.IMPORTANCE_HIGH
        channerl.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager!!.createNotificationChannel(channerl)
    }
    val getManager:NotificationManager? get(){
        if(notificationManager == null){
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }

    @TargetApi(Build.VERSION_CODES.O)
   fun getOreoNotification(title:String?, body:String?, pendingIntent:PendingIntent?,soundUri:Uri?, icon:String?):Notification.Builder{
        val yaseerDosariImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.sudaisi)
        val bigPicStyle = Notification.BigPictureStyle().bigPicture(yaseerDosariImage)
        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.notif_sound_goes)
       return Notification.Builder(applicationContext, CHANNEL_ID)
               .setContentIntent(pendingIntent)
               .setContentTitle(title)
               .setContentText(body)
               .setSmallIcon(icon!!.toInt())
               .setSound(soundUri)
               .setLargeIcon(yaseerDosariImage)
               .setPriority(Notification.PRIORITY_HIGH)
               .setAutoCancel(true)
   }
}