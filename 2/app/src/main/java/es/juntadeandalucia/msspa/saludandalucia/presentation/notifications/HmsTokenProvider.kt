package es.juntadeandalucia.msspa.saludandalucia.presentation.notifications

import android.content.Context
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessaging
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetHmsGmsTokenUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsPhoneNumberUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveHmsGmsTokenUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.UpdateNotificationsSubscriptionUseCase
import timber.log.Timber

class HmsTokenProvider(
    private val saveHmsGmsTokenUseCase: SaveHmsGmsTokenUseCase,
    private val getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
    private val updateNotificationsSubscriptionUseCase: UpdateNotificationsSubscriptionUseCase,
    private val getHmsGmsTokenUseCase: GetHmsGmsTokenUseCase
){

    fun requestToken(context: Context) {
        val appId = AGConnectOptionsBuilder().build(context).getString("/client/app_id")
        Timber.d("Rapp_id: $appId")

        appId?.let {
            object : Thread() {
                override fun run() {
                    try {
                        val tokenHms = HmsInstanceId.getInstance(context).getToken(appId, "HCM")
                        tokenHms?.let {
                            if(it.isNotEmpty()){
                                performToken(it)
                            }
                        }
                    } catch (e: Exception) {
                        Timber.e("Error getting hmsToken: ${e.message}, for app_id: $appId")
                    }
                }
            }.start()
        }
    }

    fun performToken(tokenHms: String) {
        getHmsGmsTokenUseCase.execute(onSuccess = { tokenSaved ->
            if (tokenSaved.isEmpty() || tokenHms != tokenSaved) {
                NotificationsSubscriptionHandler(
                    saveHmsGmsTokenUseCase,
                    getNotificationsPhoneNumberUseCase,
                    updateNotificationsSubscriptionUseCase
                ).checkNotificationsEnabled(tokenHms)
            }
        }, onError = {
            Timber.e("Error checking token hms gms: ${it.message}")
        })
    }

    fun deleteToken(context: Context) {
        val appId = AGConnectOptionsBuilder().build(context).getString("/client/app_id")
        HmsInstanceId.getInstance(context).deleteToken(appId, HmsMessaging.DEFAULT_TOKEN_SCOPE)
    }
}