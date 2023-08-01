package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.reinforced

import androidx.core.text.isDigitsOnly
import es.juntadeandalucia.msspa.authentication.domain.base.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.*
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBasePresenter
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import timber.log.Timber

class LoginReinforcedPresenter(private val loginReinforcedUseCase: LoginPersonalDataReinforcedUseCase,
                               loginPersonalDataUseCase: LoginPersonalDataBasicUseCase,
                               authorizeUseCase: AuthorizeUseCase,
                               saveUserUseCase: SaveUserUseCase,
                               getSavedUsersUseCase: GetSavedUsersUseCase,
                               removeUserUseCase: RemoveUserUseCase,
                               setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
                               setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
                               isSavedUserUseCase: IsSavedUserUseCase,
                               getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
                               getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
                               showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
                               showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
                               hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
                               hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase
) : LoginBasePresenter<LoginReinforcedContract.View>(loginPersonalDataUseCase, authorizeUseCase, saveUserUseCase, getSavedUsersUseCase, removeUserUseCase,
        setFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase,
        isSavedUserUseCase,
        getFirstLoadUserAdviceUseCase,
        getFirstSaveUserAdviceUseCase,
        showStoredUsersDataAlertUseCase,
        showSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase
), LoginReinforcedContract.Presenter {


    override fun onSendClicked(
        saveUser: Boolean,
        nuss: String?,
        nuhsa: String?,
        idType: KeyValueEntity,
        identification: String,
        birthDate: String,
        phone: String
    ) {
        val validIdentifier = validateIdentifier(idType.key, identification, birthDate)
        val validBirthday = validateBirthday(birthDate)
        val validPhone = validatePhoneNumber(phone)
        if (validIdentifier && validBirthday && validPhone) {
            view.showLoading()
            this.saveUser = saveUser
            msspaAuthenticationUser = MsspaAuthenticationUserEntity(
                nuss = nuss ?: "",
                nuhsa = nuhsa ?: "",
                identification = identification,
                phone = phone,
                idType = idType,
                birthDate = birthDate
            )
            if(!isTheSameAuthorize){
                tryToLogin(msspaAuthenticationUser, authorized, saveUser)
            }else{
                authorizeUseCase.execute(
                    onSuccess = { newAuthorizeEntity ->
                        tryToLogin(msspaAuthenticationUser, newAuthorizeEntity, saveUser)
                    },
                    onError = { error ->
                        view.enableLoginButton()
                        handleCommonErrors(error)
                    }
                )
            }
        }else{
            view.enableLoginButton()
        }
    }

    override fun tryToLogin(msspaAuthenticationUser: MsspaAuthenticationUserEntity?, authorizeEntity: AuthorizeEntity?, saveUser: Boolean) {
        msspaAuthenticationUser?.let { userEntity ->
            authorizeEntity?.let { authorize ->
                loginReinforcedUseCase.params(
                    msspaAuthenticationUser = userEntity,
                    authorize = authorize,
                    loginMethod = ApiConstants.LoginApi.LOGIN_METHOD_DATOS_SMS
                )
                loginReinforcedUseCase.execute(
                    onSuccess = { authorizeEntity ->
                        isTheSameAuthorize = false
                        view.apply {
                            enableLoginButton()
                            hideLoading()
                            navigateToSMS(
                                authorizeEntity,
                                userEntity,
                                saveUser
                            )
                        }
                    },
                    onError = {
                        view.enableLoginButton()
                        Timber.e(it)
                        handleError(it)
                    }
                )
            }
        }
    }


    override fun validatePhoneNumber(phone: String): Boolean =
            if (phone.isNotBlank() && phone.isDigitsOnly()) {
                if (phone.length == 9) {
                    view.showValidPhoneNumber()
                    true
                } else {
                    view.showErrorPhoneNumber()
                    false
                }
            } else {
                view.showErrorPhoneNumber()
                false
            }

    override fun checkPhoneNumber(phone: String) {
        validatePhoneNumber(phone)
    }

    override fun onPhoneHelpClicked() {
        view.showPhoneHelp()
    }


}