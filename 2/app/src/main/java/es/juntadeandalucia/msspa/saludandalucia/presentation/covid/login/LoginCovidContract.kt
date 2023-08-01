package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import javax.crypto.Cipher

class LoginCovidContract {

    interface View : BaseContract.View {
        fun setupView()
        fun removeScrollListener()
        fun showErrorNuhsa()
        fun showValidBirthday()
        fun showErrorBirthday()
        fun showValidIdentifier()
        fun showErrorIdentifier()
        fun showErrorDNIIdentifier()
        fun showErrorNIEIdentifier()
        fun showValidPhoneNumber()
        fun showErrorPhoneNumber()
        fun showLoginFieldsError()
        fun navigateToQuiz(user: QuizUserEntity)
        fun showValidPhonePrefix()
        fun showErrorPhonePrefix()
        fun showInputErrorsDialog()
        fun showUsersButton()
        fun isBiometricCompatible(): Boolean
        fun showSaveUserCheck()
        fun hideSaveUserCheck()
        fun hideUsersButton()
        fun initUsersButton()
        fun fillForm(it: QuizUserEntity?)
        fun authenticateForEncryption(
            onSuccess: (Cipher, Cipher) -> Unit,
            onError: (String) -> Unit
        )
        fun authenticateForDecryption(onSuccess: (Cipher) -> Unit, onError: (String) -> Unit)
        fun showSavedUsers(users: List<QuizUserEntity>)
        fun hideUsersDialog()
        fun showSaveUserDataAlert()
        fun hideSaveUserDataAlert()
        fun showStoredUsersDataAlert()
        fun hideStoredUsersDataAlert()
        fun showOverlay()
        fun hideOverlay()
        fun showMaxLoginsError()
        fun navigateToSecondFactor(
            authorize: AuthorizeEntity,
            user: QuizUserEntity,
            saveUser: Boolean
        )
        fun setupIdTypesAdapter(idTypes: List<KeyValueEntity>)
        fun setNuhsaAsIdentifier()
        fun setupNushaIdentifier()
        fun setupIdentifier()
        fun clearIdentifier()
        fun showNoBDUDataError()
        fun showProtectedUserError()
    }

    interface Presenter : BaseContract.Presenter {
        fun setupView()
        fun onStop()
        fun checkBirthday(birthday: String)
        fun validateBirthday(birthday: String): Boolean
        fun checkPhonePrefix(prefix: String)
        fun validatePhonePrefix(prefix: String): Boolean
        fun checkPhoneNumber(prefix: String, phone: String)
        fun validatePhoneNumber(prefix: String, phone: String): Boolean
        fun checkIdentifier(idType: String, identifier: String)
        fun validateIdentifier(idType: String, identifier: String): Boolean
        fun onUsersButtonClicked()
        fun onOptionsMenuCreated()
        fun onSendClicked(
            idType: KeyValueEntity,
            identification: String,
            birthDate: String,
            prefixPhone: String,
            phone: String,
            isHealthProf: Boolean,
            isSecurityProf: Boolean,
            isSpecialProf: Boolean,
            saveUser: Boolean
        )
        fun onSelectUser(userEntity: QuizUserEntity?)
        fun onRemoveUser(userEntity: QuizUserEntity, cryptoManager: CrytographyManager?)
        fun showSaveUserDataAlertIfNeeded()
        fun showSaveUserDataAlert()
        fun hideAlerts()
        fun onIdentifierTypeSelected(type: String, identifier: String)
    }
}
