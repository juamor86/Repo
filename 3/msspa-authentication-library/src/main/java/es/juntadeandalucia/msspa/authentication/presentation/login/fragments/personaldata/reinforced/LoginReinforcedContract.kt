package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.reinforced

import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBaseContract

class LoginReinforcedContract {
    interface View : LoginBaseContract.View{
        fun showErrorPhoneNumber()
        fun showValidPhoneNumber()
        fun navigateToSMS(authorizeEntity: AuthorizeEntity, MsspaAuthenticationUserEntity: MsspaAuthenticationUserEntity, saveUser: Boolean)
        fun showPhoneHelp()
    }

    interface Presenter : LoginBaseContract.Presenter {
        fun onSendClicked(
            saveUser: Boolean,
            nuss: String?,
            nuhsa: String?,
            idType: KeyValueEntity,
            identification: String,
            birthDate: String,
            phone: String
        )
        fun checkPhoneNumber(phone: String)
        fun validatePhoneNumber(phone: String): Boolean
        fun onPhoneHelpClicked()
    }
}