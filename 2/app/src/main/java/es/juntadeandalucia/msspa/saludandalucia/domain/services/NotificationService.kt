package es.juntadeandalucia.msspa.saludandalucia.domain.services

import android.app.Notification
import android.app.PendingIntent
import com.google.firebase.messaging.RemoteMessage

interface NotificationService {
    fun handleFCMMessage(message: RemoteMessage)
    fun handleHMSMessage(message: com.huawei.hms.push.RemoteMessage)
    fun createNotificationDelayed(title: Int, message: Int, graph: Int, dest: Int, delayTime: Long)
    fun createNofication(message: String, intent: PendingIntent): Notification
    fun cancelAllNotificationsFromStatusBar()
    fun getGoNotificationsIntent(): PendingIntent
    fun sendNotification(notification: Notification)
    fun getIntent(graph: Int, dest: Int): PendingIntent
}
