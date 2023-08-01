package es.juntadeandalucia.msspa.saludandalucia.utils

import android.os.Build
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import java.util.Locale

import timber.log.Timber

class Tracker {
    companion object {
        fun track(eventName: String, onComplete: () -> Unit = {}) {

            val formattedEventName: String =
                if (Consts.HUAWEI == Build.MANUFACTURER.lowercase(Locale.ROOT)) {
                    eventName + "_2"
                } else {
                    eventName + "_0"
                }
            Firebase.analytics.logEvent(formattedEventName, null)
            Timber.d("Event-> $formattedEventName")
            onComplete.invoke()
        }
    }
}