package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import io.reactivex.Completable
import io.reactivex.Flowable

interface NotificationsRepository {

    fun getNotifications(): Flowable<List<NotificationData>>

    fun saveNotification(notification: NotificationData): Completable

    fun removeNotification(notification: NotificationData): Completable

    fun removeAllNotifications(): Completable

    fun getNotReadedCount(): Flowable<Int>

    fun sendNotificationReceived(idNotification: String): Completable

    fun sendNotificationRead(idNotification: String): Completable
}
