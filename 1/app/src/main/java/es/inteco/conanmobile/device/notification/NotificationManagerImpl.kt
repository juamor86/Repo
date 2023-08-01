package es.inteco.conanmobile.device.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import es.inteco.conanmobile.R
import es.inteco.conanmobile.presentation.main.MainActivity
import kotlin.random.Random


/**
 * Notification manager impl
 *
 * @property context
 * @constructor Create empty Notification manager impl
 */
class NotificationManagerImpl(
    val context: Context
) : es.inteco.conanmobile.device.notification.NotificationManager {
    private var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mChannel = NotificationChannel(
                context.getString(R.string.notification_channel_id),
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            mChannel.enableLights(true)
            mChannel.lightColor = Color.DKGRAY
            mChannel.enableVibration(true)
            mChannel.enableLights(true)
            mChannel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            )
            mChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun sendNotification(message: String) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            0
        }
        val intent = PendingIntent.getActivity(
            context, 0, notificationIntent, flags
        )
        notificationManager.notify(
            Random(System.currentTimeMillis()).nextInt(),
            createNotificationInternal(message = message, intent = intent)
        )
    }

    private fun createNotificationInternal(
        title: String = context.getString(R.string.app_name),
        message: String,
        intent: PendingIntent? = null
    ): Notification {
        return NotificationCompat.Builder(
            context, context.getString(R.string.notification_channel_id)
        )
            .setSmallIcon(R.drawable.logo_conan_mobile_big_test)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setChannelId(context.getString(R.string.notification_channel_id))
            .setContentIntent(intent)
            .build()
    }

}