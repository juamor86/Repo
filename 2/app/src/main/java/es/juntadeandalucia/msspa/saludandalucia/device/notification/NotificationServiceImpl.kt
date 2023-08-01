package es.juntadeandalucia.msspa.saludandalucia.device.notification

import android.annotation.SuppressLint
import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.*
import com.google.firebase.messaging.RemoteMessage
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.NotificationDao
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.NotificationsSubscriptionApi.NOTIFICATION_BODY
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.NotificationsSubscriptionApi.NOTIFICATION_CONFIRM_PAYLOAD
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.NotificationsSubscriptionApi.NOTIFICATION_ID
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.NotificationsSubscriptionApi.NOTIFICATION_TITLE
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants.NotificationsSubscriptionApi.NOTIFICATION_TYPE
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerApplicationComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.ApplicationModule
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.NotificationMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.services.NotificationService
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SendNotificationReceivedUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.UpdateNotificationUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import timber.log.Timber
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationServiceImpl(
    val context: Context,
    val notificationDao: NotificationDao,
    private val sendNotificationReceivedUseCase: SendNotificationReceivedUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
) :
    NotificationService {

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
            mChannel.lightColor = Color.GREEN
            mChannel.enableVibration(true)
            mChannel.enableLights(true)
            mChannel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            mChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun handleFCMMessage(message: RemoteMessage) {
        handleMessage(message.messageId, message.data)
    }

    override fun handleHMSMessage(message: com.huawei.hms.push.RemoteMessage) {
        handleMessage(message.messageId, message.dataOfMap)
    }

    @SuppressLint("CheckResult")
    override fun createNotificationDelayed(
        title: Int,
        message: Int,
        graph: Int,
        dest: Int,
        delayTime: Long
    ) {
        val notification =
            NotificationData(
                id = SecureRandom().nextLong().toString(),
                title = context.getString(title),
                description = context.getString(message),
                date = Calendar.getInstance().timeInMillis,
                readed = false
            )

        notificationDao.insertOrUpdate(notification).blockingGet()

        val data = with(Data.Builder()) {
            putInt(NotificationConstants.Notification.MESSAGE, message)
            putInt(NotificationConstants.Notification.INTENT_GRAPH, graph)
            putInt(NotificationConstants.Notification.INTENT_DEST, dest)
            build()
        }
        val notificationWorkRequest =
            OneTimeWorkRequestBuilder<DelayedNotificationWorker>().setInitialDelay(
                delayTime, TimeUnit.MILLISECONDS
            ).setInputData(data).build()

        WorkManager.getInstance(context).enqueue(notificationWorkRequest)
    }

    override fun cancelAllNotificationsFromStatusBar() {
        notificationManager.cancelAll()
    }

    override fun sendNotification(notification: Notification) {
        notificationManager.notify(
            SecureRandom().nextInt(), notification
        )
    }

    @SuppressLint("CheckResult")
    private fun handleMessage(messageId: String?, keyMap: MutableMap<String, String>) {

        if (keyMap.containsKey(NOTIFICATION_TYPE)) {
            when (keyMap[NOTIFICATION_TYPE].toString()) {
                NOTIFICATION_CONFIRM_PAYLOAD -> sendNotificationReceived(messageId)
            }
        } else launchNotification(messageId, keyMap)

    }

    private fun launchNotification(
        messageId: String?,
        keyMap: MutableMap<String, String>
    ) {
        val notificationData = NotificationData(id = messageId ?: keyMap[NOTIFICATION_ID].toString(),
            title = keyMap[NOTIFICATION_TITLE].toString(), description = keyMap[NOTIFICATION_BODY].toString(),
            date = Calendar.getInstance().timeInMillis, readed = false)
        sendNotificationReceived(notificationData.id)
        performNotification(notificationData)
    }

    private fun performNotification(notificationData: NotificationData) {
        notificationData.apply {
            updateNotificationUseCase
                .params(NotificationMapper.convert(this))
                .execute(
                    onComplete = {
                        val notification = if (title.toLowerCase(Locale.ROOT).contains(NotificationConstants.Notification.TOTP_TYPE)) {
                            createNotificationTotp(title = title, message = description)
                        } else {
                            createNotificationInternal(title = title, message = description, intent = getGoNotificationsIntent())
                        }
                        notificationManager.notify(Random(System.currentTimeMillis()).nextInt(), notification)
                    },
                    onError = {
                        Timber.e(it)
                    })
        }
    }

    /*private fun startVideoCall(payload: String) {
        Timber.d("Videocall_Payload: ($payload)")
        val videoCallIntent =
            Intent(context, MsspaVideoCall::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(Consts.ARG_PAYLOAD, payload)
            }
        context.startActivity(videoCallIntent)
    }*/

    private fun sendNotificationReceived(messageId: String?) {
        sendNotificationReceivedUseCase.params(messageId!!)
            .execute(
                onComplete = {
                    Timber.d("Notification received confirmation")
                }, onError = {
                    Timber.e(it)
                }
            )
    }

    private fun createNotificationInternal(
        title: String = context.getString(R.string.app_name),
        message: String,
        intent: PendingIntent?
    ): Notification {
        return NotificationCompat.Builder(
            context, context.getString(R.string.notification_channel_id)
        ).setSmallIcon(R.drawable.ic_launcher_round).setLargeIcon(
            BitmapFactory.decodeResource(
                context.resources, R.drawable.ic_launcher_round
            )
        ).setContentTitle(title)
            .setContentText(message).setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setChannelId(context.getString(R.string.notification_channel_id))
            .setContentIntent(intent)
            .build()
    }

    private fun createNotificationTotp(
        title: String = context.getString(R.string.app_name),
        message: String
    ): Notification {
        val totpCode = message.substring(message.length - 6)
        Intent().also { intent ->
            intent.action = MsspaAuthConsts.TOTP_ACTION
            intent.putExtra(MsspaAuthConsts.TOTP_CODE, totpCode)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        return NotificationCompat.Builder(
            context, context.getString(R.string.notification_channel_id)
        ).setSmallIcon(R.drawable.ic_launcher_round).setLargeIcon(
            BitmapFactory.decodeResource(
                context.resources, R.drawable.ic_launcher_round
            )
        ).setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setChannelId(context.getString(R.string.notification_channel_id)).apply {
                if (!isForeground()) {
                    setContentIntent(getGoNotificationsIntent())
                }
            }
            .build()
    }

    override fun createNofication(message: String, intent: PendingIntent): Notification {
        return createNotificationInternal(message = message, intent = intent)
    }

    private fun isForeground(): Boolean {
        val processInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(processInfo)
        return (processInfo.importance == IMPORTANCE_FOREGROUND || processInfo.importance == IMPORTANCE_VISIBLE)
    }

    override fun getGoNotificationsIntent(): PendingIntent {
        return NavDeepLinkBuilder(context).setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph).setDestination(
                R.id.notifications_dest
            ).createPendingIntent()
    }

    override fun getIntent(graph: Int, dest: Int): PendingIntent {
        return NavDeepLinkBuilder(context).setComponentName(MainActivity::class.java)
            .setGraph(graph).setDestination(
                dest
            ).createPendingIntent()
    }

    class DelayedNotificationWorker(
        private val context: Context,
        workerParams: WorkerParameters
    ) : Worker(context, workerParams) {

        @Inject
        lateinit var notificationService: NotificationService

        override fun doWork(): Result {
            if (applicationContext is App) {
                DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(applicationContext as App))
                    .build().inject(this)
            }

            try {
                val graph = inputData.getInt(NotificationConstants.Notification.INTENT_GRAPH, 0)
                val dest = inputData.getInt(NotificationConstants.Notification.INTENT_DEST, 0)
                val message = inputData.getInt(NotificationConstants.Notification.MESSAGE, 0)

                val intent = notificationService.getIntent(graph, dest)
                val notification = notificationService.createNofication(
                    message = context.getString(message),
                    intent = intent
                )
                notificationService.sendNotification(notification)
            } catch (e: Exception) {
                Result.failure()
            }
            return Result.success()
        }
    }
}
