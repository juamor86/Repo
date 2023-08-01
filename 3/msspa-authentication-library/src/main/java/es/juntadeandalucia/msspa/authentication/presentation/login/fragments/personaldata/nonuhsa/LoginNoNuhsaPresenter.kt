package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.nonuhsa

import es.juntadeandalucia.msspa.authentication.domain.base.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.*
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBasePresenter
import es.juntadeandalucia.msspa.authentication.utils.Analytics.PERSONAL_DATA_NO_CARD
import es.juntadeandalucia.msspa.authentication.utils.Analytics.buildEvent
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants

class LoginNoNuhsaPresenter(
        private val loginPersonalDataNoNuhsaUseCase: LoginPersonalDataNoNuhsaUseCase,
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
) : LoginBasePresenter<LoginNoNuhsaContract.View>(loginPersonalDataUseCase, authorizeUseCase, saveUserUseCase, getSavedUsersUseCase, removeUserUseCase,
        setFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase,
        isSavedUserUseCase,
        getFirstLoadUserAdviceUseCase,
        getFirstSaveUserAdviceUseCase,
        showStoredUsersDataAlertUseCase,
        showSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase
), LoginNoNuhsaContract.Presenter{

    override fun tryToLogin(msspaAuthenticationUser: MsspaAuthenticationUserEntity?, authorizeEntity: AuthorizeEntity?, saveUser: Boolean) {
        authorized = authorizeEntity
        msspaAuthenticationUser?.let { userEntity ->
            authorizeEntity?.let { authorize ->
                loginPersonalDataNoNuhsaUseCase.params(
                    userEntity,
                    authorize = authorize,
                    loginMethod = ApiConstants.LoginApi.LOGIN_METHOD_DATOS_NO_NUHSA
                )
                loginPersonalDataNoNuhsaUseCase.execute(
                    onSuccess = { authEntity ->
                        view.hideLoading()

                        authEntity.authorizeEntity = authorized
                        authEntity.msspaAuthenticationUser = msspaAuthenticationUser
                        view.sendEvent(buildEvent(PERSONAL_DATA_NO_CARD, true))
                        if (saveUser) {
                            initSaveUser(authEntity)
                        } else {
                            view.setResultSuccess(authEntity)
                        }
                    },
                    onError = {
                        view.sendEvent(buildEvent(PERSONAL_DATA_NO_CARD, false))
                        handleCommonErrors(it)
                    }
                )
            }

        }
    }
}