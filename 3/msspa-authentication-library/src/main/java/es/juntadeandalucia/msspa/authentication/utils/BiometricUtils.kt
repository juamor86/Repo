package es.juntadeandalucia.msspa.authentication.utils

import android.app.KeyguardManager
import android.content.Context
import androidx.biometric.BiometricManager

class BiometricUtils {
    companion object {

        fun canAuthenticateBiometric(context: Context): Boolean {
            return (BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS)
        }

        fun isBiometricOrSecured(context: Context): Boolean {
            return (BiometricManager.from(context).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS).or(
                isPatternOrPassOrPinSet(context)
            )
        }

        private fun isPatternOrPassOrPinSet(context: Context): Boolean {
            val keyguardManager =
                context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager // api 16+
            return keyguardManager.isKeyguardSecure
        }
    }
}
