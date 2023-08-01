package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.basic

import es.juntadeandalucia.msspa.authentication.domain.base.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.*
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBasePresenter

class LoginBasicPresenter(
        private val loginBasicUseCase: LoginPersonalDataBasicUseCase,
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
) : LoginBasePresenter<LoginBasicContract.View>(loginPersonalDataUseCase, authorizeUseCase, saveUserUseCase, getSavedUsersUseCase, removeUserUseCase,
        setFirstLoadUserAdviceUseCase,
        setFirstSaveUserAdviceUseCase,
        isSavedUserUseCase,
        getFirstLoadUserAdviceUseCase,
        getFirstSaveUserAdviceUseCase,
        showStoredUsersDataAlertUseCase,
        showSaveUserDataAlertUseCase,
        hideStoredUsersDataAlertUseCase,
        hideSaveUserDataAlertUseCase
), LoginBasicContract.Presenter