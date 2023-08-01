package es.juntadeandalucia.msspa.saludandalucia.security

import android.util.Base64
import androidx.fragment.app.FragmentActivity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import javax.crypto.Cipher

interface CrytographyManager {

    fun promptForEncryption(
        onSuccess: (Cipher, Cipher) -> Unit,
        onError: (String) -> Unit,
        fragmentActivity: FragmentActivity,
        keyString: String,
        unlockEntity: UnlockEntity? = null
    )

    fun promptForDecryption(
        onSuccess: (Cipher) -> Unit,
        onError: (String) -> Unit,
        fragmentActivity: FragmentActivity,
        keyString: String,
        unlockEntity: UnlockEntity? = null
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

    fun getCipherForEncryption(): Cipher
    fun getCipherForDecryption(): Cipher
}
