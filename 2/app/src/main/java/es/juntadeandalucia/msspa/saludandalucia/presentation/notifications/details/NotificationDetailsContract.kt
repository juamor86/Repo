package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.details

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NotificationDetailsContract {
    interface View : BaseContract.View {
        fun showNotification(notificationEntity: NotificationEntity)
        fun initRemoveButton()
        fun back()
        fun showRemoveNotificationConfirmDialog()
    }

    interface Presenter : BaseContract.Presenter {
        fun onOptionsMenuCreated()
        fun onRemoveButtonClicked()
        fun removeNotification()
        fun setupView(notificationEntity: NotificationEntity)
    }
}
