package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications.details

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.RemoveNotificationUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class NotificationDetailsPresenter(
    private val removeNotificationUseCase: RemoveNotificationUseCase
) :
    BasePresenter<NotificationDetailsContract.View>(), NotificationDetailsContract.Presenter {

    private lateinit var notificationEntity: NotificationEntity

    override fun setupView(notificationEntity: NotificationEntity) {
        this.notificationEntity = notificationEntity
        view.showNotification(notificationEntity)
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.NOTIFICATION_DETAIL_SCREEN_ACCESS

    override fun onOptionsMenuCreated() {
        view.initRemoveButton()
    }

    override fun onRemoveButtonClicked() {
        view.showRemoveNotificationConfirmDialog()
    }

    override fun removeNotification() {
        removeNotificationUseCase.params(notificationEntity)
            .execute(onComplete = { view.back() }, onError = { Timber.e(it) })
    }

    override fun unsubscribe() {
        removeNotificationUseCase.clear()
    }
}
