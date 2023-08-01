package es.juntadeandalucia.msspa.saludandalucia.security

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.ByteArray
import kotlin.CharSequence
import kotlin.Exception
import kotlin.Int
import kotlin.String
import kotlin.Unit
import kotlin.apply
import kotlin.let
import kotlin.Byte

@RequiresApi(Build.VERSION_CODES.M)
class PinPatternCryptographyManager() :
    CrytographyManager, BiometricPrompt.AuthenticationCallback() {

    var iv = ByteArray(GCM_IV_LENGTH)

    private lateinit var success: (Cipher, Cipher) -> Unit
    private lateinit var successDecrypt: (Cipher) -> Unit
    private lateinit var errorCallback: (String) -> Unit
    private lateinit var typeUnlock: UnlockEntity

    private lateinit var activity: FragmentActivity
    private lateinit var keySt: String

    override fun promptForEncryption(
        onSuccess: (Cipher, Cipher) -> Unit,
        onError: (String) -> Unit,
        fragmentActivity: FragmentActivity,
        keyString: String,
        unlockEntity: UnlockEntity?
    ) {
        success = onSuccess
        errorCallback = onError
        keySt = keyString
        activity = fragmentActivity
        typeUnlock = unlockEntity ?: UnlockEntity.SAVE_USER
        launchPromptForAuthenticate()
    }

    override fun promptForDecryption(
        onSuccess: (Cipher) -> Unit,
        onError: (String) -> Unit,
        fragmentActivity: FragmentActivity,
        keyString: String,
        unlockEntity: UnlockEntity?
    ) {
        successDecrypt = onSuccess
        errorCallback = onError
        keySt = keyString
        activity = fragmentActivity

        typeUnlock = unlockEntity ?: UnlockEntity.RECOVERY_USER
        launchPromptForAuthenticate()
    }

    override fun getCipherForEncryption(): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keySt)
        val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivGcmSpec)
        return cipher
    }

    override fun getCipherForDecryption(): Cipher {
        val cipher = getCipher()
        val secretKey = getOrCreateSecretKey(keySt)
        val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivGcmSpec)
        return cipher
    }

    private fun getCipher(): Cipher {
        val transformation = encryptType
        return Cipher.getInstance(transformation)
    }

    // TODO: 29/12/2021 IMPROVE THIS
    
    companion object {
        private val ANDROID_KEYSTORE = "AndroidKeyStore"
        private val GCM_IV_LENGTH = 12
        private val GCM_TAG_LENGTH = 16
        private val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private var iv2 = ByteArray(GCM_IV_LENGTH)
        private var encryptType = "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"

        fun encryptAndSavePassword(strToEncrypt: String): String {
            val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)

            val cipher = Cipher.getInstance(encryptType)
            iv2[0]=1
            val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv2)
            val id = getOrCreateId(Consts.IDENTIFIER)
            cipher.init(Cipher.ENCRYPT_MODE, id, ivGcmSpec)
            val cipherText = cipher.doFinal(plainText)

            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(cipherText)
            val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))

            return strToSave
        }
        fun getDecryptedPassword(strToDecrypt: String): String {
            val bytes = android.util.Base64.decode(strToDecrypt, android.util.Base64.DEFAULT)
            val ois = ObjectInputStream(ByteArrayInputStream(bytes))
            val passwordToDecryptByteArray = ois.readObject() as ByteArray
            val decryptedPasswordByteArray = decrypt(passwordToDecryptByteArray)

            val decryptedPassword = StringBuilder()
            for (b in decryptedPasswordByteArray) {
                decryptedPassword.append(b.toChar())
            }

            return decryptedPassword.toString()
        }

        private fun decrypt(dataToDecrypt: ByteArray): ByteArray {
            val cipher = Cipher.getInstance(encryptType)
            iv2[0]=1
            val ivGcmSpec = GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE_BITS, iv2)
            val secretKey = getOrCreateId(Consts.IDENTIFIER)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivGcmSpec)
            val cipherText = cipher.doFinal(dataToDecrypt)

            return cipherText
        }

        private fun getOrCreateId(id: String): SecretKey {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null) // Keystore must be loaded before it can be accessed
            keyStore.getEntry(id, null)?.let {
                return (it as KeyStore.SecretKeyEntry).secretKey
            }

            val keygen = KeyGenerator.getInstance("AES")
            keygen.init(256)
            val secretKey = keygen.generateKey()
            val keyEntry = KeyStore.SecretKeyEntry(secretKey)

            keyStore.setEntry(
                id, keyEntry,
                KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setRandomizedEncryptionRequired(false)
                    .build()
            )
            return keygen.generateKey()
        }

    }

    private fun getOrCreateSecretKey(keyName: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                setUserAuthenticationParameters(Consts.KEY_DURATION,
                    KeyProperties.AUTH_DEVICE_CREDENTIAL
                            or KeyProperties.AUTH_BIOMETRIC_STRONG
                )
            } else {
                setUserAuthenticationValidityDurationSeconds(Consts.KEY_DURATION)
            }
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE
        )
        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    private fun getBiometricInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(typeUnlock.title))
            .setSubtitle(activity.getString(typeUnlock.subtitle))
            .setDeviceCredentialAllowed(typeUnlock.allowCredentials).build()
    }

    private fun launchPromptForAuthenticate() {

        val biometricPrompt = BiometricPrompt(
            activity, ContextCompat.getMainExecutor(activity.applicationContext), this

        )

        biometricPrompt.authenticate(getBiometricInfo())
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {

        errorCallback("Authentication failed")

        Toast.makeText(activity, R.string.unlock_error, Toast.LENGTH_SHORT)
    }

    override fun onAuthenticationFailed() {
        errorCallback("Authentication failed")

        Toast.makeText(activity, R.string.unlock_error, Toast.LENGTH_SHORT)
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        try {
            if (typeUnlock.encrypt) {
                val cypherEncrypt = getCipherForEncryption()
                val cypherDecrypt = getCipherForDecryption()
                success(cypherEncrypt, cypherDecrypt)

            } else {
                val cypherDecrypt = getCipherForDecryption()
                successDecrypt(cypherDecrypt)
            }

        } catch (e: Exception) {
            Toast.makeText(activity, R.string.unlock_error, Toast.LENGTH_SHORT).show()
        }
    }
}
