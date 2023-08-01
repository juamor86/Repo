package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.secondfactor

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract

class SecondFactorContract {

    interface View : BaseContract.View {
        fun setupView()
        fun enableAutoComplete(active: Boolean)
        fun enableContinueButton(active: Boolean)
        fun showTotpCountdown()
        fun showRefreshButton()
        fun hideTotpCountdown()
        fun hideRefreshButton()
        fun stopTotpCountDown()
        fun startTotpCountDown(expirationMillis: Long)
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(
            config: MsspaAuthenticationConfig,
            authorize: AuthorizeEntity,
            msspaAuthenticationUser: MsspaAuthenticationUserEntity,
            saveUser: Boolean
        )
        fun onValidateClicked(code: String)
        fun onCodeTextChanged(code: String)
        fun onCountdownFinish()
        fun onRefreshClick()
        fun onOtherAuthMethodClicked()
    }
}
