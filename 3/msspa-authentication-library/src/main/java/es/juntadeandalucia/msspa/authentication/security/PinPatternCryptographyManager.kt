package es.juntadeandalucia.msspa.authentication.security

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.BiometricUtils
import es.juntadeandalucia.msspa.authentication.utils.exceptions.BiometricAuthenticationFailedException
import es.juntadeandalucia.msspa.authentication.utils.exceptions.CipherSecurityInvalidException
import timber.log.Timber
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class PinPatternCryptographyManager() :
    CrytographyManager, BiometricPrompt.AuthenticationCallback() {

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private val GCM_IV_LENGTH = 12
        private val GCM_TAG_LENGTH = 16
    }

    var iv = ByteArray(GCM_IV_LENGTH)

    private lateinit var success: (Cipher?, Cipher?) -> Unit
    private lateinit var successDecrypt: (Cipher) -> Unit
    private lateinit var errorCallback: (Throwable) -> Unit
    private lateinit var errorInt: (Int) -> Unit?

    private var activity: FragmentActivity? = null
    private var fragment: Fragment? = null
    private lateinit var keySt: String

    private lateinit var title: String
    private var subtitle: String? = null
    private var negativeButtonText: String? = null
    private var encrypt: Boolean = true
    private var isNeedCipher: Boolean = true

    override fun promptForEncryption(
        onSuccess: (Cipher?, Cipher?) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        fragmentActivity: FragmentActivity,
        keyString: String,
        title: Int?,
        subtitle: Int?,
        negativeButtonText: Int?,
        encrypt: Boolean,
        isNeedCipher:Boolean
    ) {
        success = onSuccess
        errorCallback = onError
        errorInt = onErrorInt
        keySt = keyString
        activity = fragmentActivity
        this.title = title?.let { activity!!.getString(it) } ?: ""
        this.subtitle = subtitle?.let { activity!!.getString(it) } ?: ""
        this.negativeButtonText = negativeButtonText?.let { activity!!.getString(it) }
        this.encrypt = encrypt
        this.isNeedCipher = isNeedCipher
        launchPromptForAuthenticate()
    }

    override fun promptForDecryption(
        onSuccess: (Cipher) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        fragmentActivity: FragmentActivity,
        keyString: String
    ) {
        successDecrypt = onSuccess
        errorCallback = onError
        errorInt = onErrorInt
        keySt = keyString
        activity = fragmentActivity
        title = activity!!.getString(R.string.msspa_auth_user_prompt_title)
        subtitle = activity!!.getString(R.string.msspa_auth_user_prompt_subtitle)
        encrypt = false

        launchPromptForAuthenticate()
    }

    override fun promptForLogin(
        onSuccess: (Cipher?, Cipher?) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        keyString: String,
        frag: Fragment,
        title: Int,
        negativeButtonText: Int,
        encrypt: Boolean,
        isNeedCipher:Boolean
    ) {
        fragment = frag
        this.title = frag.requireActivity().getString(title)
        this.negativeButtonText = frag.requireActivity().getString(negativeButtonText)
        this.encrypt = encrypt
        success = onSuccess
        errorCallback = onError
        errorInt = onErrorInt
        keySt = keyString
        this.isNeedCipher = isNeedCipher
        launchPromptForAuthenticate()
    }

    override fun getCipherForEncryption(): Cipher {
        val cipher = getCipher()
        val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey(keySt), ivGcmSpec)
        return cipher
    }

    override fun getCipherForDecryption(): Cipher {
        val cipher = getCipher()
        val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(keySt), ivGcmSpec)
        return cipher
    }

    override fun getCipherForPinEncryption(): Cipher {
        val cipher = getCipher()
        val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey(ApiConstants.KeyNameCipher.KEY_SAVED_PIN), ivGcmSpec)
        return cipher
    }

    private fun getCipher(): Cipher {
        val transformation = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
        return Cipher.getInstance(transformation)
    }

    private fun getOrCreateSecretKey(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance(Companion.ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed
        keyStore.getKey(keyName, null)?.let { return it as SecretKey }

        val paramsBuilder = KeyGenParameterSpec.Builder(
            keyName, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
        paramsBuilder.apply {
            setBlockModes(ENCRYPTION_BLOCK_MODE)
            setKeySize(256)
            setRandomizedEncryptionRequired(false)
            setEncryptionPaddings(ENCRYPTION_PADDING)
            val authRequired = Build.VERSION.SDK_INT != Build.VERSION_CODES.Q
            setUserAuthenticationRequired(authRequired)
            setUserAuthenticationValidityDurationSeconds(ApiConstants.General.KEY_DURATION)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, Companion.ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    private fun getBiometricInfo(): BiometricPrompt.PromptInfo {
        val biometricPrompt = BiometricPrompt.PromptInfo.Builder()
        biometricPrompt.setTitle(title).setSubtitle(subtitle)
        if (negativeButtonText != null) {
            var context: Context
            fragment?.let {
                context = it.requireContext()
                if (BiometricUtils.canAuthenticateBiometric(context)){
                    biometricPrompt.setNegativeButtonText(negativeButtonText!!)
                }else{
                    biometricPrompt.setDeviceCredentialAllowed(true)
                }
            }
        } else {
           biometricPrompt.setDeviceCredentialAllowed(true)
        }
        return biometricPrompt.build()
    }

    private fun launchPromptForAuthenticate() {
        val biometricPrompt = if (activity != null) {
            BiometricPrompt(
                activity!!,
                ContextCompat.getMainExecutor(activity),
                this
            )
        } else {
            BiometricPrompt(
                fragment!!,
                ContextCompat.getMainExecutor(fragment!!.requireContext()),
                this
            )
        }

        biometricPrompt.authenticate(getBiometricInfo())
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        negativeButtonText?.let {
            errorCallback(BiometricAuthenticationFailedException())
            activity?.let {
                Toast.makeText(it, R.string.msspa_auth_unlock_error, Toast.LENGTH_SHORT).show()
            }
        }
        errorInt(errorCode)
    }

    override fun onAuthenticationFailed() {
        errorCallback(BiometricAuthenticationFailedException())
        activity?.let {
            Toast.makeText(it, R.string.msspa_auth_unlock_error, Toast.LENGTH_SHORT).show()
        } ?: Toast.makeText(
            fragment!!.requireContext(),
            R.string.msspa_auth_unlock_error,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        try {
            if (encrypt) {
                var cypherEncrypt: Cipher? = null
                var cypherDecrypt: Cipher? = null
                if (isNeedCipher) {
                    cypherEncrypt = getCipherForEncryption()
                    cypherDecrypt = getCipherForDecryption()
                }
                success(cypherEncrypt, cypherDecrypt)
            } else {
                val cypherDecrypt = getCipherForDecryption()
                successDecrypt(cypherDecrypt)
            }
        } catch (e: Exception) {
            Timber.e(e)
            errorCallback(CipherSecurityInvalidException(keySt))
        }
    }
}
