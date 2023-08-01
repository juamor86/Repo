package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata

import androidx.core.text.isDigitsOnly
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.domain.base.GetSavedUsersUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.*
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.nonuhsa.LoginNoNuhsaFragment
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.utils.Analytics.PERSONAL_DATA_BASIC
import es.juntadeandalucia.msspa.authentication.utils.Analytics.buildEvent
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.Utils
import es.juntadeandalucia.msspa.authentication.utils.exceptions.LoginInvalidRepeatedException
import timber.log.Timber
import javax.crypto.Cipher

abstract class LoginBasePresenter<V : LoginBaseContract.View>(
    var loginPersonalDataUseCase: LoginPersonalDataBasicUseCase,
    val authorizeUseCase: AuthorizeUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val getSavedUsersUseCase: GetSavedUsersUseCase,
    private val removeUserUseCase: RemoveUserUseCase,
    private val setFirstLoadUserAdviceUseCase: SetFirstLoadUserAdviceUseCase,
    private val setFirstSaveUserAdviceUseCase: SetFirstSaveUserAdviceUseCase,
    private val isSavedUserUseCase: IsSavedUserUseCase,
    private val getFirstLoadUserAdviceUseCase: GetFirstLoadUserAdviceUseCase,
    private val getFirstSaveUserAdviceUseCase: GetFirstSaveUserAdviceUseCase,
    private val showStoredUsersDataAlertUseCase: ShowStoredUsersDataAlertUseCase,
    private val showSaveUserDataAlertUseCase: ShowSaveUserDataAlertUseCase,
    private val hideStoredUsersDataAlertUseCase: HideStoredUsersDataAlertUseCase,
    private val hideSaveUserDataAlertUseCase: HideSaveUserDataAlertUseCase
) : BasePresenter<V>(), LoginBaseContract.Presenter {


    protected var msspaAuthenticationUser: MsspaAuthenticationUserEntity? = null
    protected var authorized: AuthorizeEntity? = null
    protected var saveUser: Boolean = false
    protected var isTheSameAuthorize: Boolean = false

    override fun onViewCreated(authorizeEntity: AuthorizeEntity) {
        isTheSameAuthorize = Utils.checkIsTheSameAuthorize(authorized, authorizeEntity)
        authorized = authorizeEntity
        view.apply {
            setupView()
            setupIdTypesAdapter(MsspaAuthConsts.documentTypes)
        }
    }

    override fun checkIdentifier(idType: String, identifier: String, birthDate: String) {
        when (idType) {
            MsspaAuthenticationUserEntity.ID_TYPE_NUSS_NUHSA -> {
                if (identifier.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_NUSHA) {
                    validateHealthCard(identifier)
                }
            }
            MsspaAuthenticationUserEntity.ID_TYPE_DNI -> {
                if (identifier.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_IDENTIFIER) {
                    validateDNI(identifier, birthDate)
                }
            }
            MsspaAuthenticationUserEntity.ID_TYPE_NIE -> {
                if (identifier.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_IDENTIFIER) {
                    validateNIE(identifier)
                }
            }
            else -> {
                view.showValidIdentifier()
            }
        }
    }

    override fun onSendClicked(
        saveUser: Boolean,
        nuss: String?,
        nuhsa: String?,
        idType: KeyValueEntity,
        identification: String,
        birthDate: String
    ) {
        val validIdentifier = validateIdentifier(idType.key, identification, birthDate)
        val validBirthday = validateBirthday(birthDate)
        if (validIdentifier && validBirthday) {
            view.showLoading()
            this.saveUser = saveUser
            msspaAuthenticationUser = MsspaAuthenticationUserEntity(
                nuss = nuss ?: "",
                nuhsa = nuhsa ?: "",
                identification = identification,
                idType = idType,
                birthDate = birthDate
            )

            tryToLogin(msspaAuthenticationUser, authorized, saveUser)
        }
    }

    open fun tryToLogin(msspaAuthenticationUser: MsspaAuthenticationUserEntity?, authorizeEntity: AuthorizeEntity?, saveUser: Boolean) {
        msspaAuthenticationUser?.let { userEntity ->
            authorizeEntity?.let { authorize ->
                loginPersonalDataUseCase.params(
                    userEntity,
                    authorize = authorize,
                    loginMethod = ApiConstants.LoginApi.LOGIN_METHOD_DATOS
                )
                loginPersonalDataUseCase.execute(
                    onSuccess = { authEntity ->
                        view.hideLoading()
                        authEntity.authorizeEntity = authorizeEntity
                        authEntity.msspaAuthenticationUser = msspaAuthenticationUser
                        view.sendEvent(buildEvent(PERSONAL_DATA_BASIC, true))
                        if (saveUser) {
                            initSaveUser(authEntity)
                        } else {
                            view.setResultSuccess(authEntity)
                        }
                    },
                    onError = {
                        view.sendEvent(buildEvent(PERSONAL_DATA_BASIC, false))
                        handleError(it)
                    }
                )
            }
        }
    }

    override fun validateIdentifier(
        idType: String,
        identification: String,
        birthDate: String
    ): Boolean {
        return when (idType) {
            MsspaAuthenticationUserEntity.ID_TYPE_NUSS_NUHSA -> validateHealthCard(identification)
            MsspaAuthenticationUserEntity.ID_TYPE_DNI -> validateDNI(identification, birthDate)
            MsspaAuthenticationUserEntity.ID_TYPE_NIE -> validateNIE(identification)
            else -> validateOtherIdentifier(identification)
        }
    }

    override fun onIdentifierTypeSelected(type: String, identifier: String) {
        view.showValidIdentifier()

        if (identifier.isNotEmpty()) {
            validateIdentifier(type, identifier, "")
        }
    }

    override fun checkBirthday(birthday: String, dni: String) {
        if (birthday.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_BIRTHDAY) {
            validateBirthday(birthday)
            validateDNI(dni, birthday)
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

    private fun validateDNI(dni: String, birthDate: String): Boolean =
        if (dni.isBlank()) {
            if (birthDate.isNotEmpty() && checkUnder14(birthDate)) {
                view.showValidIdentifier()
                true
            } else {
                view.showErrorIdentifier()
                false
            }
        } else {
            if (Utils.validateDni(dni)) {
                view.showValidIdentifier()
                true
            } else {
                view.showErrorDNIIdentifier()
                false
            }
        }

    private fun checkUnder14(birthDate: String): Boolean {
        val date = Utils.stringToDate(birthDate)
        val millis14years: Long = 14 * 365 * 24 * 60 * 60 * 1000L
        return date.time + millis14years >= System.currentTimeMillis()
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

    private fun validateHealthCard(healthCard: String): Boolean {
        return if (healthCard.isNotBlank()) {
             when{
                ((healthCard.startsWith(MsspaAuthConsts.NUHSA_PREFIX)).or(healthCard.length == MsspaAuthConsts.MIN_NUSHA_LENGHT)) -> {
                    (validateNuhsa(healthCard))
                }
                (!healthCard.contains(MsspaAuthConsts.NUHSA_PREFIX) && (healthCard.length >= MsspaAuthConsts.MIN_NUSS_LENGHT) || (healthCard.length <= MsspaAuthConsts.MAX_NUSS_LENGHT)) -> {
                    view.showValidNuhsa()
                    true
                }
                (healthCard.all { it.isLetter() }) -> {
                    view.showErrorNuhsa()
                    false
                }
                else -> {
                    view.showErrorNuhsa()
                    false
                }
            }
        } else {
            view.showErrorNuhsa()
            false
        }
    }

    private fun validateNuhsa(healthCard: String): Boolean {
        var nuhsa = healthCard
        view.showValidNuhsa()

        var resultValue: Boolean = true

        if (nuhsa.length == MsspaAuthConsts.MIN_NUSHA_LENGHT && nuhsa.substring(0, 2) != MsspaAuthConsts.NUHSA_PREFIX) {
            nuhsa = "${MsspaAuthConsts.NUHSA_PREFIX}${healthCard}"
        }

        if (nuhsa.length != MsspaAuthConsts.MAX_NUHSA_LENGTH || nuhsa.substring( 0, 2) != MsspaAuthConsts.NUHSA_PREFIX || !nuhsa.substring(2).isDigitsOnly()) {
            view.showErrorNuhsa()
            resultValue = false
        }

        return resultValue
    }

    private fun validateOtherIdentifier(identifier: String): Boolean =
        if (identifier.isBlank()) {
            view.showErrorIdentifier()
            false
        } else {
            view.showValidIdentifier()
            true
        }

    protected fun initSaveUser(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        view.authenticateForEncryption(
            onSuccess = { cipherEncrypt: Cipher?, cipherDecrypt: Cipher? ->
                saveUserUseCase
                    .params(
                        msspaAuthenticationEntity.msspaAuthenticationUser!!,
                        cipherEncrypt!!,
                        cipherDecrypt!!
                    )
                    .execute(
                        onComplete = {
                            view.setResultSuccess(msspaAuthenticationEntity)
                            Timber.d("User saved successfully")
                        },
                        onError = {
                            view.setResultSuccess(msspaAuthenticationEntity)
                            Timber.e(it)
                        }
                    )
            }, onError = {
                view.setResultSuccess(msspaAuthenticationEntity)
                Timber.e(it)
            },
            title = R.string.msspa_auth_user_prompt_save_title,
            subtitle = R.string.msspa_auth_user_prompt_save_subtitle,
            encrypt = true,
            keyString = ApiConstants.KeyNameCipher.KEY_SAVED_USERS,
            isNeedCipher = true
        )
    }

    override fun onSelectUser(msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity?) {
        view.apply {
            clearForm()
            fillForm(msspaAuthenticationUserEntity)
            hideUsersDialog()
        }
    }

    private fun showSavedUsers(it: List<MsspaAuthenticationUserEntity>) {
        view.showSavedUsers(it)
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
        }, keyString = ApiConstants.KeyNameCipher.KEY_SAVED_USERS)
    }

    override fun onRemoveUser(
        msspaAuthenticationUserEntity: MsspaAuthenticationUserEntity,
        cryptoManager: CrytographyManager?
    ) {
        removeUserUseCase.execute(
            RemoveUserUseCase.Param.forRemoveUser(msspaAuthenticationUserEntity, cryptoManager),
            onSuccess = { emptyUsers ->
                if (emptyUsers) {
                    view.apply {
                        //TODO: Añadir la ocultación del botón en el action bar
                        //hideUsersButton()
                        hideUsersDialog()
                    }
                }
            },
            onError = {
                Timber.e(it)
            })
    }

    override fun onOptionsMenuCreated() {
        view.initUsersButton()
        checkSavedUsers()
    }

    private fun checkSavedUsers() {
        if (view.haveBiometricOrPin()) {
            if (view is LoginNoNuhsaFragment) {
                view.hideSaveUserCheck()
            } else {
                view.showSaveUserCheck()
            }

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

    private fun showSaveUserDataAlert() {
        showSaveUserDataAlertUseCase.execute(
            onCompleted = {
                view.apply {
                    showOverlay()
                    showSaveUserDataAlert()
                    this@LoginBasePresenter.hideSaveUserDataAlert()
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
                    this@LoginBasePresenter.hideStoredUsersDataAlert()
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

    override fun handleError(exception: Throwable) {
        if (exception is LoginInvalidRepeatedException) {
            authorizeUseCase.execute(
                onSuccess = { newAuthorizeEntity ->
                    tryToLogin(msspaAuthenticationUser, newAuthorizeEntity, saveUser)
                },
                onError = { error ->
                    view.enableLoginButton()
                    handleCommonErrors(error)
                }
            )
        } else {
            view.enableLoginButton()
            handleCommonErrors(exception)
        }
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

    override fun requestBiometrics() {
        view.apply {
            if(!haveBiometricOrPin()){
                showDialogNotPhoneSecured()
            }
        }
    }

    override fun unsubscribe() {
        authorizeUseCase.clear()
        loginPersonalDataUseCase.clear()
    }
}