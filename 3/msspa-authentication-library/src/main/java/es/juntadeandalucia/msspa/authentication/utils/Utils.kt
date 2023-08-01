package es.juntadeandalucia.msspa.authentication.utils

import android.text.InputFilter
import android.view.Window
import android.view.WindowManager
import es.juntadeandalucia.msspa.authentication.BuildConfig
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.HealthCardEntity
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import timber.log.Timber
import java.security.KeyStore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class Utils {
    companion object {
        private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun validateDni(dni: String): Boolean {
            var isValid = false
            val dniLetters = charArrayOf(
                'T',
                'R',
                'W',
                'A',
                'G',
                'M',
                'Y',
                'F',
                'P',
                'D',
                'X',
                'B',
                'N',
                'J',
                'Z',
                'S',
                'Q',
                'V',
                'H',
                'L',
                'C',
                'K',
                'E'
            )
            if (dni.length == 9 && dni[8].isLetter()) {
                var i = 0
                do {
                    val character = dni.codePointAt(i)
                    isValid = character in 48..57
                    i++
                } while (i < dni.length - 1 && isValid)
            }
            if (isValid) {
                val letter = dni[8].toUpperCase()
                val numbers = dni.substring(0, 8).toInt()
                val rest = numbers % 23
                isValid = letter == dniLetters[rest]
            }
            return isValid
        }

        fun validateNie(nie: String): Boolean {
            var nie = nie
            var isValid = false
            val dniLetters = charArrayOf(
                'T',
                'R',
                'W',
                'A',
                'G',
                'M',
                'Y',
                'F',
                'P',
                'D',
                'X',
                'B',
                'N',
                'J',
                'Z',
                'S',
                'Q',
                'V',
                'H',
                'L',
                'C',
                'K',
                'E'
            )
            if (nie.length == 9 &&
                nie[8].isLetter() &&
                (nie.substring(0, 1).toUpperCase() == "X" ||
                        nie.substring(0, 1).toUpperCase() == "Y" ||
                        nie.substring(0, 1).toUpperCase() == "Z")
            ) {
                var i = 1
                do {
                    val character = nie.codePointAt(i)
                    isValid = character in 48..57
                    i++
                } while (i < nie.length - 1 && isValid)
            }
            if (isValid && nie.substring(0, 1).toUpperCase() == "X") {
                nie = "0" + nie.substring(1, 9)
            } else if (isValid && nie.substring(0, 1).toUpperCase() == "Y") {
                nie = "1" + nie.substring(1, 9)
            } else if (isValid && nie.substring(0, 1).toUpperCase() == "Z") {
                nie = "2" + nie.substring(1, 9)
            }
            if (isValid) {
                val letter = nie[8].toUpperCase()
                val numbers = nie.substring(0, 8).toInt()
                val rest = numbers % 23
                isValid = letter == dniLetters[rest]
            }
            return isValid
        }

        fun stringToDate(birthDate: String): Date {
            return dateFormatter.parse(birthDate)
        }

        fun validateHealthCard(type: HealthCardEntity, healthCard: String): String {
            return when (type) {
                HealthCardEntity.NUSS -> {
                    formatNuss(healthCard)
                }
                HealthCardEntity.NUHSA -> {
                    formatNuhsa(healthCard)
                }
            }
        }

        private fun formatNuss(nuss: String): String =
            with(nuss) {
                when {
                    (length == MsspaAuthConsts.MAX_NUSS_LENGHT).and(!contains(MsspaAuthConsts.NUHSA_PREFIX)) -> {
                        this.trim()
                    }
                    (length == MsspaAuthConsts.MIN_NUSS_LENGHT).and(!contains(MsspaAuthConsts.NUHSA_PREFIX)) -> {
                        this
                    }
                    else -> {
                        ""
                    }
                }
            }

        private fun formatNuhsa(nuhsa: String): String =
            with(nuhsa) {
                when {
                    (length == MsspaAuthConsts.MIN_NUSHA_LENGHT).and(!contains(MsspaAuthConsts.NUHSA_PREFIX)) -> {
                        "${MsspaAuthConsts.NUHSA_PREFIX}${this}"
                    }
                    (length == MsspaAuthConsts.MAX_NUHSA_LENGTH).and(contains(MsspaAuthConsts.NUHSA_PREFIX)) -> {
                        this
                    }
                    else -> {
                        ""
                    }
                }
            }

        val whiteSpaceFilter =
            InputFilter { source, _, _, _, _, _ ->
                source.forEach{ if (Character.isSpaceChar(it)) return@InputFilter "" }
                null
            }

        val KEY_STORE_TYPE = "AndroidKeyStore"

        fun deleteAliasKeyStore(alias: String): Boolean {
            if(alias.isEmpty()){
                return false
            }
            try {
                val keyStore: KeyStore = KeyStore.getInstance(KEY_STORE_TYPE)
                keyStore.load(null)
                keyStore.deleteEntry(alias)
                return true
            } catch (e: Exception) {
                Timber.e(e)
                return false
            }
        }


        fun secureAgainstScreenshots(
            window: Window,
            authConfig: MsspaAuthenticationConfig.Environment
        ) {
            if (!BuildConfig.DEBUG && authConfig == MsspaAuthenticationConfig.Environment.PRODUCTION) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }
        }

        fun checkIsTheSameAuthorize(
            oldAuthorize: AuthorizeEntity?,
            newAuthorize: AuthorizeEntity
        ):Boolean {
            oldAuthorize?.let { authorizeOldEntity ->
                return authorizeOldEntity.sessionData == newAuthorize.sessionData && authorizeOldEntity.sessionId == newAuthorize.sessionId
            }
            return false
        }
    }
}
