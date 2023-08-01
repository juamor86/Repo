package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class NotificationsPresenter(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val removeNotificationUseCase: RemoveNotificationUseCase,
    private val removeAllNotificationsUseCase: RemoveAllNotificationsUseCase,
    private val getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
    private val removeAllNotificationsFromStatusUseCase: RemoveAllNotificationsFromStatusUseCase,
    private val sendNotificationReadUseCase: SendNotificationReadUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase
) : BasePresenter<NotificationsContract.View>(), NotificationsContract.Presenter {

    override fun onCreate() {
        view.setupAdapter()
    }

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.setupNotificationsRecycler()

        refreshNotifications()
        checkNotificationsEnabled()
        removeAllNotificationsFromStatusBar()
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.NOTIFICATIONS_SCREEN_ACCESS

    private fun checkNotificationsEnabled() {
        getNotificationsPhoneNumberUseCase.execute(
            onSuccess = { phoneNumber ->
                view.showNotificationsDisabledWarning(!phoneNumber.isNotEmpty())
            },
            onError = {
                Timber.e("Error checking if notifications are enabled")
            }
        )
    }

    override fun refreshNotifications() {
        loadNotifications(showLoading = false, isRefreshing = true)
    }

    private fun loadNotifications(showLoading: Boolean = true, isRefreshing: Boolean = false) {
        if (showLoading) {
            view.showLoading()
        }
        getNotificationsUseCase.execute(
            onNext = { notifications ->
                view.apply {
                    if (showLoading) {
                        hideLoading()
                    }

                    if (isRefreshing) {
                        hideRefreshing()
                    }

                    if (notifications.isEmpty()) {
                        showEmptyView()
                        hideRemoveAllButton()
                    } else {
                        showNotifications(notifications)
                        showRemoveAllButton()
                    }
                }
            },
            onError = {
                Timber.e(it)
                view.apply {
                    if (showLoading) {
                        hideLoading()
                    }

                    if (isRefreshing) {
                        hideRefreshing()
                    }
                    showErrorView()
                }
            }, onCompleted = {
            }
        )
    }

    override fun onNotificationItemClick(notification: NotificationEntity, itemView: View) {
        with(notification) {
            if (readed) {
                view.openNotificationDetails(notification = this, itemView = itemView)
            } else {
                sendNotificationRead(notification = this)
                updateNotification(notification = this, itemView = itemView)
            }
        }
    }

    override fun onNotificationItemRemoved(it: NotificationEntity) {
        removeNotificationUseCase.params(it).execute(onComplete = {}, onError = { Timber.e(it) })
    }

    override fun onOptionsMenuCreated() {
        view.initRemoveAllButton(view.getNotificationsCount() > 0)
    }

    override fun onRemoveAllButtonClicked() {
        view.requestConfirmationForRemoveAll()
    }

    override fun removeAllNotifications() {
        removeAllNotificationsUseCase.execute(
            onComplete = {},
            onError = { view.showRemoveNotificationsErrorDialog() })
    }

    private fun removeAllNotificationsFromStatusBar() {
        removeAllNotificationsFromStatusUseCase.execute(onComplete = {}, onError = { Timber.e(it) })
    }

    override fun unsubscribe() {
        getNotificationsUseCase.clear()
        removeAllNotificationsUseCase.clear()
        removeNotificationUseCase.clear()
    }

    private fun sendNotificationRead(notification: NotificationEntity) {
        if(notification.title != view.getNotificationTitle()){
            sendNotificationReadUseCase.params(notification.id).execute(
                onComplete = {},
                onError = { Timber.e(it) }
            )
        }
    }

    private fun updateNotification(notification: NotificationEntity, itemView: View) {
        notification.readed = true
        updateNotificationUseCase
            .params(notification)
            .execute(
                onComplete = {
                    view.openNotificationDetails(notification = notification, itemView = itemView)
                },
                onError = {
                    Timber.e(it)
                    view.openNotificationDetails(notification = notification, itemView = itemView)
                })
    }
}
