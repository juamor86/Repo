package es.juntadeandalucia.msspa.saludandalucia.data.repository.persistence

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import es.juntadeandalucia.msspa.saludandalucia.data.persistence.NotificationDao
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NotificationsRepository
import io.reactivex.Completable
import io.reactivex.Flowable

open class NotificationsRepositoryDataBaseImpl(val notificationDao: NotificationDao) :
    NotificationsRepository {

    override fun getNotifications(): Flowable<List<NotificationData>> =
        notificationDao.getAllNotifications()

    override fun saveNotification(notification: NotificationData): Completable =
        notificationDao.insertOrUpdate(notification)

    override fun removeNotification(notification: NotificationData): Completable =
        notificationDao.deleteNotification(notification)

    override fun removeAllNotifications(): Completable =
        notificationDao.deleteAllNotifications()

    override fun getNotReadedCount(): Flowable<Int> = notificationDao.getNotReadedCount()

    override fun sendNotificationReceived(idNotification: String): Completable {
        TODO("Not yet implemented")
    }

    override fun sendNotificationRead(idNotification: String): Completable {
        TODO("Not yet implemented")
    }
}
