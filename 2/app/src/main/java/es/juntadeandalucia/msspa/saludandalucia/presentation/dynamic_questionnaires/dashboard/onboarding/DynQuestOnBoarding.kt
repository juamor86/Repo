package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard.onboarding

import android.os.Bundle
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_wallet_on_boarding.*

class DynQuestOnBoarding (override val onDismiss: (() -> Any)? = null) : CustomFullScreenDialog() {

    override fun bindContentLayout() = R.layout.view_dyn_quest_onboarding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(resources) {
            /*on_boarding_title_tv.text = getText(R.string.wallet_onboarding_tittle).toString().uppercase()

            on_boarding_iv.setImageResource(R.drawable.ic_certificate)

            on_boarding_text_tv.text = getText(R.string.wallet_onboarding_description)*/
        }

        accept_btn.setOnClickListener {
            dialog!!.dismiss()
        }
    }
}