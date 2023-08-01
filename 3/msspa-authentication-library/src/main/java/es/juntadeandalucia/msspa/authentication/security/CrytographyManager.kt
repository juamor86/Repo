package es.juntadeandalucia.msspa.authentication.security

import android.util.Base64
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import javax.crypto.Cipher

interface CrytographyManager {
    fun promptForEncryption(
            onSuccess: (Cipher?, Cipher?) -> Unit,
            onError: (Throwable) -> Unit,
            onErrorInt: (Int) -> Unit?,
            fragmentActivity: FragmentActivity,
            keyString: String,
            title: Int?,
            subtitle: Int?,
            negativeButtonText: Int? = null,
            encrypt: Boolean,
            isNeedCipher:Boolean
    )

    fun promptForDecryption(
            onSuccess: (Cipher) -> Unit,
            onError: (Throwable) -> Unit,
            onErrorInt: (Int) -> Unit?,
            fragmentActivity: FragmentActivity,
            keyString: String
    )

    fun promptForLogin(
        onSuccess: (Cipher?, Cipher?) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        keyString: String,
        frag: Fragment,
        title: Int,
        negativeButtonText: Int,
        encrypt: Boolean,
        isNeedCipher:Boolean
    )

    companion object {
        fun encryptData(
                plainText: ByteArray,
                cipher: Cipher
        ): String {
            val enc = cipher.doFinal(plainText)
            return Base64.encodeToString(enc, Base64.DEFAULT)
        }

        fun decryptData(encrypted: String, cipher: Cipher): String {
            return cipher.doFinal(
                    Base64.decode(encrypted, Base64.DEFAULT)
            ).toString(Charsets.UTF_8)
        }
    }

    fun getCipherForPinEncryption() : Cipher
    fun getCipherForEncryption(): Cipher
    fun getCipherForDecryption(): Cipher
}
