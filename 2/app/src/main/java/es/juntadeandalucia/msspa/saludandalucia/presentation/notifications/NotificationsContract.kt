package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NotificationsContract {

    interface View : BaseContract.View {
        fun setupNotificationsRecycler()
        fun showNotifications(notifications: List<NotificationEntity>)
        fun openNotificationDetails(notification: NotificationEntity, itemView: android.view.View)
        fun hideRefreshing()
        fun initRemoveAllButton(visible: Boolean)
        fun requestConfirmationForRemoveAll()
        fun showRemoveAllButton()
        fun hideRemoveAllButton()
        fun setupAdapter()
        fun showNotificationsDisabledWarning(notificationsDisable: Boolean)
        fun showRemoveNotificationsErrorDialog()
        fun getNotificationsCount(): Int
        fun getNotificationTitle(): String
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate()
        fun refreshNotifications()
        fun onNotificationItemClick(it: NotificationEntity, itemView: android.view.View)
        fun onNotificationItemRemoved(it: NotificationEntity)
        fun onOptionsMenuCreated()
        fun onRemoveAllButtonClicked()
        fun removeAllNotifications()
    }
}
