package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.NotificationDao
import es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence.NotificationsRepositoryDataBaseImpl
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class NotificationsRepositoryMockImpl(val context: Context, notificationDao: NotificationDao) :
    NotificationsRepositoryDataBaseImpl(notificationDao) {

    private var initialized = false

    override fun getNotifications(): Flowable<List<NotificationData>> {
        if (!initialized) {
            val notificationList = readFromJson()
            for (notification in notificationList) {
                saveNotification(notification).subscribeOn(Schedulers.io()).blockingGet()
            }
            initialized = true
        }
        return super.getNotifications()
    }

    override fun sendNotificationReceived(idNotification: String): Completable = Completable.complete()

    override fun sendNotificationRead(idNotification: String): Completable = Completable.complete()

    private fun readFromJson(): List<NotificationData> {
        try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.notifications)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson<Array<NotificationData>>(
                stringReader,
                Array<NotificationData>::class.java
            ).toList()
        } catch (e: Exception) {
            Timber.e("Error getting mock notifications: ${e.message}")
        }

        return emptyList()
    }
}
