package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login

import android.util.Log
import androidx.core.text.isDigitsOnly
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.InvalidBDUData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.LoginRequiredException
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.ProtectedUserException
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.VerificationMaxAttemptsExceededException
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.AuthorizeUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetFirstLoadUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetFirstSaveUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetKeyValueListUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.HideSaveUserDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.HideStoredUsersDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.IsSavedUserUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.LoginStep1UseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.RemoveUserUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveQuizSessionUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveUserUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.SetFirstLoadUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.SetFirstSaveUserAdviceUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ShowSaveUserDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.ShowStoredUsersDataAlertUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import javax.crypto.Cipher
import retrofit2.HttpException
import timber.log.Timber

class LoginCovidPresenter(
    private val authorizeUseCase: AuthorizeUseCase,
    private val loginUseCase: LoginStep1UseCase,
    private val saveQuizSessionUseCase: SaveQuizSessionUseCase,
    private val isSavedUserUseCase: IsSavedUserUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val getSavedUsersUseCase: GetSavedUsersUseCase,
    private val removeUserUseCase: RemoveUserUseCase,
    private val getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
    private val getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
    private val setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
    private val setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
    private val showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
    private val showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
    private val hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase,
    private val hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
    private val getKeyValueListUseCase: GetKeyValueListUseCase
) : BasePresenter<LoginCovidContract.View>(), LoginCovidContract.Presenter {

    private var user: QuizUserEntity? = null

    companion object {
        private const val VALIDATION_MIN_LENGTH_NUSHA = 12
        private const val VALIDATION_MIN_LENGTH_BIRTHDAY = 10
        private const val VALIDATION_MIN_LENGTH_IDENTIFIER = 9
        private const val VALIDATION_MIN_LENGTH_SPANISH_PHONE = 9
    }

    override fun setupView() {
        view.setupView()

        getKeyValueListUseCase.params(R.raw.id_types).execute(
            onSuccess = { list ->
                view.setupIdTypesAdapter(list)
                user?.apply {
                    view.fillForm(this)
                } ?: view.setNuhsaAsIdentifier()
            },
            onError = { Timber.e(it) })
    }

    override fun getScreenNameTracking(): String? =
        Consts.Analytics.LOGIN_COVID_TRIAGE_SCREEN_ACCESS

    override fun onStop() {
        view.removeScrollListener()
    }

    override fun onOptionsMenuCreated() {
        view.initUsersButton()
        checkSavedUsers()
    }

    private fun checkSavedUsers() {
        if (view.isBiometricCompatible()) {
            view.showSaveUserCheck()
            val storedUsers = isSavedUserUseCase.execute()
            if (storedUsers) {
                view.showUsersButton()
                checkFirstLoadUserAdvice()
            } else {
                view.hideUsersButton()
            }
        } else {
            view.hideSaveUserCheck()
            view.hideUsersButton()
        }
    }

    override fun onIdentifierTypeSelected(type: String, identifier: String) {
        view.showValidIdentifier()
        if (type == QuizUserEntity.ID_TYPE_NUHSA) {
            view.setupNushaIdentifier()
        } else {
            view.setupIdentifier()
            if (identifier.isNotEmpty()) {
                if (identifier.startsWith(Consts.NUHSA_PREFIX)) {
                    view.apply {
                        clearIdentifier()
                    }
                } else {
                    validateIdentifier(type, identifier)
                }
            }
        }
    }

    private fun validateNuhsa(nuhsa: String): Boolean =
        if (nuhsa.isNotBlank()
                .and(nuhsa.length == Consts.MAX_NUHSA_LENGTH)
                .and(nuhsa.startsWith(Consts.NUHSA_PREFIX, true))
        ) {
            view.showValidIdentifier()
            true
        } else {
            view.showErrorNuhsa()
            false
        }

    override fun checkBirthday(birthday: String) {
        if (birthday.length >= VALIDATION_MIN_LENGTH_BIRTHDAY) {
            validateBirthday(birthday)
        }
    }

    override fun validateBirthday(birthday: String): Boolean =
        if (birthday.isNotBlank()) {
            view.showValidBirthday()
            true
        } else {
            view.showErrorBirthday()
            false
        }

    override fun checkIdentifier(idType: String, identifier: String) {
        when (idType) {
            QuizUserEntity.ID_TYPE_NUHSA -> {
                if (identifier.length >= VALIDATION_MIN_LENGTH_NUSHA) {
                    validateNuhsa(identifier)
                }
            }
            QuizUserEntity.ID_TYPE_DNI -> {
                if (identifier.length >= VALIDATION_MIN_LENGTH_IDENTIFIER) {
                    validateDNI(identifier)
                }
            }
            QuizUserEntity.ID_TYPE_NIE -> {
                if (identifier.length >= VALIDATION_MIN_LENGTH_IDENTIFIER) {
                    validateNIE(identifier)
                }
            }
            else -> {
                view.showValidIdentifier()
            }
        }
    }

    override fun validateIdentifier(idType: String, identifier: String): Boolean {
        return when (idType) {
            QuizUserEntity.ID_TYPE_NUHSA -> validateNuhsa(identifier)
            QuizUserEntity.ID_TYPE_DNI -> validateDNI(identifier)
            QuizUserEntity.ID_TYPE_NIE -> validateNIE(identifier)
            else -> validateOtherIdentifier(identifier)
        }
    }

    private fun validateDNI(dni: String): Boolean =
        if (dni.isBlank()) {
            view.showErrorIdentifier()
            false
        } else {
            if (Utils.validateDni(dni)) {
                view.showValidIdentifier()
                true
            } else {
                view.showErrorDNIIdentifier()
                false
            }
        }

    private fun validateNIE(nie: String): Boolean =
        if (nie.isBlank()) {
            view.showErrorIdentifier()
            false
        } else {
            if (Utils.validateNie(nie)) {
                view.showValidIdentifier()
                true
            } else {
                view.showErrorNIEIdentifier()
                false
            }
        }

    private fun validateOtherIdentifier(identifier: String): Boolean =
        if (identifier.isBlank()) {
            view.showErrorIdentifier()
            false
        } else {
            view.showValidIdentifier()
            true
        }

    override fun checkPhoneNumber(prefix: String, phone: String) {
        if (Consts.SPAIN_PREFIX == prefix) {
            if (phone.length >= VALIDATION_MIN_LENGTH_SPANISH_PHONE) {
                validatePhoneNumber(prefix, phone)
            }
        }
    }

    override fun validatePhonePrefix(prefix: String): Boolean =
        if (prefix.isNotBlank().and(prefix.contains(Consts.PLUS).and(prefix.length > 1))) {
            view.showValidPhonePrefix()
            true
        } else {
            view.showErrorPhonePrefix()
            false
        }

    override fun onUsersButtonClicked() {
        view.authenticateForDecryption(onSuccess = { cipherDecrypt: Cipher ->
            getSavedUsersUseCase.execute(GetSavedUsersUseCase.Param.forSavedUsers(cipherDecrypt),
                onSuccess = {
                    showSavedUsers(it)
                }, onError = {
                    Timber.e(it)
                })
        }, onError = {
            Timber.e("There was an error during authentication process: $it")
        })
    }

    override fun validatePhoneNumber(prefix: String, phone: String): Boolean =
        if (phone.isNotBlank() && phone.isDigitsOnly()) {
            if (Consts.SPAIN_PREFIX != prefix || (Consts.SPAIN_PREFIX == prefix && phone.length == 9)) {
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

    override fun onSendClicked(
        idType: KeyValueEntity,
        identification: String,
        birthDate: String,
        prefixPhone: String,
        phone: String,
        isHealthProf: Boolean,
        isSecurityProf: Boolean,
        isSpecialProf: Boolean,
        saveUser: Boolean
    ) {
        val validIdentifier = validateIdentifier(idType.key, identification)
        val validBirthday = validateBirthday(birthDate)
        val validPhonePrefix = validatePhonePrefix(prefixPhone)
        val validPhone = validatePhoneNumber(prefixPhone, phone)
        if (validIdentifier && validBirthday && validPhonePrefix && validPhone) {
            view.showLoading()
            authorizeUseCase.execute(
                onSuccess = {
                    tryToLogin(
                        it,
                        idType,
                        identification,
                        birthDate,
                        prefixPhone,
                        phone,
                        isHealthProf,
                        isSecurityProf,
                        isSpecialProf,
                        saveUser
                    )
                },
                onError = {
                    Timber.e(it)
                    view.hideLoading()
                    when (it) {
                        is TooManyRequestException -> view.showTooManyRequestDialog()
                        is HttpException -> {
                            if (it.code() >= 400) {
                                view.showLoginFieldsError()
                            } else {
                                handleUnauthorizedException(
                                    exception = it
                                )
                            }
                        }
                        is ProtectedUserException -> view.showProtectedUserError()
                        else -> handleUnauthorizedException(
                            exception = it
                        )
                    }
                })
        } else {
            view.showInputErrorsDialog()
        }
    }

    private fun tryToLogin(
        authorize: AuthorizeEntity,
        idType: KeyValueEntity,
        identification: String,
        birthDate: String,
        prefixPhone: String,
        phone: String,
        isHealthProf: Boolean,
        isSecurityProf: Boolean,
        isSpecialProf: Boolean,
        saveUser: Boolean
    ) {
        user = QuizUserEntity(
            idType = idType,
            nuhsa = if (idType.key == QuizUserEntity.ID_TYPE_NUHSA) identification else "",
            identification = if (idType.key != QuizUserEntity.ID_TYPE_NUHSA) identification else "",
            birthDate = birthDate,
            prefixPhone = prefixPhone,
            phone = phone,
            isHealthProf = isHealthProf,
            isSecurityProf = isSecurityProf,
            isSpecialProf = isSpecialProf
        )
        loginUseCase.params(user!!, authorize)
            .execute(
                onSuccess = {
                    if (it.token.isNullOrEmpty()) {
                        view.navigateToSecondFactor(authorize, user!!, saveUser)
                    } else {
                        user!!.name = it.userEntity.name
                        with(it) {
                            saveQuizSessionUseCase.params(QuizSession(tokenType = tokenType!!, accessToken = token!!)).execute(onComplete = {
                                Timber.d("Quiz session save successfully")
                                if (saveUser) {
                                    initSaveUser(user!!)
                                } else {
                                    view.navigateToQuiz(user!!)
                                }
                            }, onError = {
                                Timber.e("Error saving quiz session: ${it.message}")
                            })
                        }
                    }
                    view.hideLoading()
                },
                onError = {
                    Timber.e(it)
                    view.apply {
                        hideLoading()
                        when (it) {
                            is TooManyRequestException -> showTooManyRequestDialog()
                            is VerificationMaxAttemptsExceededException -> showMaxLoginsError()
                            is InvalidBDUData-> showNoBDUDataError()
                            is LoginRequiredException -> showLoginFieldsError()
                            is ProtectedUserException -> showProtectedUserError()
                            else -> showErrorDialog()
                        }
                    }
                })
    }

    override fun checkPhonePrefix(prefix: String) {
        view.showValidPhonePrefix()
    }

    override fun unsubscribe() {
        authorizeUseCase.clear()
        saveUserUseCase.clear()
        getSavedUsersUseCase.clear()
        removeUserUseCase.clear()
        showSaveUserDataAlertUseCase.clear()
        hideSaveUserDataAlertUseCase.clear()
        showStoredUsersDataAlertUseCase.clear()
        hideStoredUsersDataAlertUseCase.clear()
    }

    private fun initSaveUser(userEntity: QuizUserEntity) {
        view.authenticateForEncryption(
            onSuccess = { cipherEncrypt: Cipher, cipherDecrypt: Cipher ->
                saveUserUseCase
                    .params(userEntity, cipherEncrypt, cipherDecrypt)
                    .execute(
                        onComplete = {
                            Timber.d("User saved successfully")
                            view.navigateToQuiz(userEntity)
                        },
                        onError = {
                            Timber.e(it)
                        }
                    )
            }, onError = {
                Timber.e(it)
            }
        )
    }

    override fun onSelectUser(userEntity: QuizUserEntity?) {
        view.apply {
            clearIdentifier()
            fillForm(userEntity)
            hideUsersDialog()
        }
    }

    private fun showSavedUsers(it: List<QuizUserEntity>) {
        view.showSavedUsers(it)
    }

    override fun onRemoveUser(
        userEntity: QuizUserEntity,
        cryptoManager: CrytographyManager?
    ) {
        // FIXME: Instead of remove one user every time, it should remove the complete list of removed users in a row.
        removeUserUseCase.execute(
            RemoveUserUseCase.Param.forRemoveUser(userEntity, cryptoManager),
            onSuccess = { emptyUsers ->
                if (emptyUsers) {
                    view.apply {
                        hideUsersButton()
                        hideUsersDialog()
                    }
                }
            },
            onError = {
                Timber.e(it)
            })
    }

    private fun checkFirstLoadUserAdvice() {
        val firstTime = getFirstLoadUserAdviceUseCase.execute()
        if (firstTime) {
            showStoredUserDataAlert()
        }
    }

    private fun setSaveUserDataAlertShowed() {
        setFirstSaveUserAdviceUseCase.execute(
            onComplete = {},
            onError = { Timber.e(it) }
        )
    }

    private fun setStoredUsersDataAlertShowed() {
        setFirstLoadUserAdviceUseCase.execute(
            onComplete = {},
            onError = { Timber.e(it) }
        )
    }

    override fun showSaveUserDataAlertIfNeeded() {
        val isFirstTime = getFirstSaveUserAdviceUseCase.execute()
        if (isFirstTime) {
            showSaveUserDataAlert()
        }
    }

    override fun showSaveUserDataAlert() {
        showSaveUserDataAlertUseCase.execute(
            onCompleted = {
                view.apply {
                    showOverlay()
                    showSaveUserDataAlert()
                    this@LoginCovidPresenter.hideSaveUserDataAlert()
                }
            },
            onError = {
                view.hideOverlay()
                Timber.e("Error showing save user data alert: ${it.message}")
            }
        )
        setSaveUserDataAlertShowed()
    }

    private fun hideSaveUserDataAlert() {
        hideSaveUserDataAlertUseCase.execute(
            onCompleted = {
                view.apply {
                    hideOverlay()
                    hideSaveUserDataAlert()
                }
            },
            onError = {
                Timber.e("Error hiding save user data alert: ${it.message}")
                view.hideOverlay()
            }
        )
    }

    private fun showStoredUserDataAlert() {
        showStoredUsersDataAlertUseCase.execute(
            onCompleted = {
                view.apply {
                    showOverlay()
                    showStoredUsersDataAlert()
                    this@LoginCovidPresenter.hideStoredUsersDataAlert()
                }
            },
            onError = {
                Timber.e("Error showing stored user data alert: ${it.message}")
                view.hideOverlay()
            }
        )
        setStoredUsersDataAlertShowed()
    }

    private fun hideStoredUsersDataAlert() {
        hideStoredUsersDataAlertUseCase.execute(
            onCompleted = {
                view.apply {
                    hideOverlay()
                    hideStoredUsersDataAlert()
                }
            },
            onError = {
                Timber.e("Error hiding stored user data alert: ${it.message}")
                view.hideOverlay()
            }
        )
    }

    override fun hideAlerts() {
        view.apply {
            hideOverlay()
            hideSaveUserDataAlert()
            hideStoredUsersDataAlert()
        }
        hideStoredUsersDataAlertUseCase.clear()
        hideSaveUserDataAlertUseCase.clear()
    }
}
