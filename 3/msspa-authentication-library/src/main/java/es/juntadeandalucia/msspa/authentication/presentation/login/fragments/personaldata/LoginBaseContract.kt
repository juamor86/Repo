package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata

import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager

class LoginBaseContract {
    interface View : BaseContract.View {
        fun setupView()
        fun showErrorIdentifier()
        fun showValidIdentifier()
        fun showErrorNIEIdentifier()
        fun showErrorDNIIdentifier()
        fun showErrorNuhsa()
        fun showValidNuhsa()
        fun clearIdentifier()
        fun setupIdTypesAdapter(idTypes: List<KeyValueEntity>)
        fun showValidBirthday()
        fun showErrorBirthday()
        fun showSavedUsers(msspaAuthenticationUsers: List<MsspaAuthenticationUserEntity>)
        fun hideUsersDialog()
        fun fillForm(msspaAuthenticationUser: MsspaAuthenticationUserEntity?)
        fun clearForm()
        fun initUsersButton()
        fun haveBiometricOrPin(): Boolean
        fun showSaveUserCheck()
        fun showUsersButton()
        fun hideUsersButton()
        fun hideSaveUserCheck()
        fun hideOverlay()
        fun showOverlay()
        fun showStoredUsersDataAlert()
        fun showSaveUserDataAlert()
        fun hideSaveUserDataAlert()
        fun hideStoredUsersDataAlert()
        fun enableLoginButton()
        fun disableLoginButton()
        fun showDialogNotPhoneSecured()
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(authorizeEntity: AuthorizeEntity)
        fun onSendClicked(
                saveUser: Boolean,
                nuss: String?,
                nuhsa: String?,
                idType: KeyValueEntity,
                identification: String,
                birthDate: String
        )

        fun validateIdentifier(idType: String, identification: String, birthDate: String): Boolean
        fun checkIdentifier(idType: String, identifier: String, birthDate: String)
        fun onIdentifierTypeSelected(type: String, identifier: String)
        fun checkBirthday(birthday: String, dni: String)
        fun validateBirthday(birthday: String): Boolean
        fun onSelectUser(msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity?)
        fun onRemoveUser(msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity, cryptoManager: CrytographyManager?)
        fun onUsersButtonClicked()
        fun onOptionsMenuCreated()
        fun showSaveUserDataAlertIfNeeded()
        fun handleError(exception: Throwable)
        fun hideAlerts()
        fun requestBiometrics()
    }
}