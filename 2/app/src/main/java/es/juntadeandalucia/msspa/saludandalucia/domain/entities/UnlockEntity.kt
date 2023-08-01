package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.saludandalucia.R

enum class UnlockEntity(
    @StringRes val title: Int,
    @StringRes val subtitle: Int,
    val allowCredentials: Boolean = true,
    val encrypt: Boolean
) {
    SAVE_USER(
        title = R.string.user_prompt_save_title,
        subtitle = R.string.user_prompt_save_subtitle,
        encrypt = true
    ),
    RECOVERY_USER(
        title = R.string.user_promt_title,
        subtitle = R.string.user_promt_subtitle,
        encrypt = false
    ),
   SAVE_CERT(
       title = R.string.wallet_unlock_title,
       subtitle = R.string.wallet_unlock_subtitle,
       encrypt = true
   ),
    UNLOCK(
        title = R.string.wallet_unlock_title,
        subtitle = R.string.wallet_unlock_subtitle,
        encrypt = false
    )

}