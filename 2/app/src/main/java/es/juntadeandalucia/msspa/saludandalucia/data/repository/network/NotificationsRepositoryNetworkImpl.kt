package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.CommunicationData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.NotificationsRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class NotificationsRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    NotificationsRepository {
    override fun getNotifications(): Flowable<List<NotificationData>> {
        TODO("Not yet implemented")
    }

    override fun saveNotification(notification: NotificationData): Completable {
        TODO("Not yet implemented")
    }

    override fun removeNotification(notification: NotificationData): Completable {
        TODO("Not yet implemented")
    }

    override fun removeAllNotifications(): Completable {
        TODO("Not yet implemented")
    }

    override fun getNotReadedCount(): Flowable<Int> {
        TODO("Not yet implemented")
    }

    override fun sendNotificationReceived(idNotification: String): Completable =
        msspaApi.sendNotificationReceived(communication =
            CommunicationData(id = idNotification)
        )

    override fun sendNotificationRead(idNotification: String): Completable =
        msspaApi.sendNotificationRead(communication =
            CommunicationData(id = idNotification)
        )
}
