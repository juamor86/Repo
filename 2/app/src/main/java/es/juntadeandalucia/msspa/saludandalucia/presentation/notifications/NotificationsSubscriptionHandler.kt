package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsPhoneNumberUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveHmsGmsTokenUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.UpdateNotificationsSubscriptionUseCase
import timber.log.Timber

class NotificationsSubscriptionHandler(
    private val saveHmsGmsTokenUseCase: SaveHmsGmsTokenUseCase,
    private val getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
    private val updateNotificationsSubscriptionUseCase: UpdateNotificationsSubscriptionUseCase
) {
    fun checkNotificationsEnabled(token: String) {
        getNotificationsPhoneNumberUseCase.execute(onSuccess = { phoneNumber ->
            if (phoneNumber.isNotEmpty()) {
                updateNotificationsSubscription(token)
            } else {
                saveToken(token)
            }
        }, onError = {
            Timber.e("Error checking notifications enabled: ${it.message}")
        })
    }

    private fun saveToken(token: String) {
        saveHmsGmsTokenUseCase.params(token).execute(
            onComplete = {
                Timber.d("Token save successfully")
            },
            onError = {
                Timber.e("Error saving token: ${it.message}")
            }
        )
    }

    private fun updateNotificationsSubscription(token: String) {
        updateNotificationsSubscriptionUseCase.params(token).execute(
            onComplete = {
                Timber.d("Token has been updated successfully")
            },
            onError = {
                Timber.e("Error updating token: ${it.message}")
            }
        )
    }
}
