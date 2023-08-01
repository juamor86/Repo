package es.inteco.conanmobile.presentation.legal

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import es.inteco.conanmobile.R
import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.dialog.BindableCustomFullScreenDialog
import kotlinx.android.synthetic.main.dialog_legal.*

/**
 * Legal dialog
 *
 * @property message
 * @constructor Create empty Legal dialog
 */
class LegalDialog(private val message: MessageEntity) : BindableCustomFullScreenDialog() {

    override fun bindData() {
        val termsAndConditions = message.formattedTermsAndConditions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            legal_text.text = Html.fromHtml(termsAndConditions, Html.FROM_HTML_MODE_COMPACT)
        } else {
            legal_text.text = Html.fromHtml(termsAndConditions)
        }
        legal_text.isClickable = true
        legal_text.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun bindContentLayout() = R.layout.dialog_legal
}