package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.qr


import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.AuthorizeUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.GetLoggingQRAttemptsUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.LoginQRUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.SetLoggingQRAttemptsUseCase
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.authentication.utils.Analytics
import es.juntadeandalucia.msspa.authentication.utils.Analytics.LOGIN_QR
import es.juntadeandalucia.msspa.authentication.utils.Analytics.buildEvent
import es.juntadeandalucia.msspa.authentication.utils.Utils
import es.juntadeandalucia.msspa.authentication.utils.exceptions.LoginInvalidRepeatedException
import timber.log.Timber

class LoginQRPresenter(
    val loginQRUseCase: LoginQRUseCase,
    val authorizeUseCase: AuthorizeUseCase,
    val getLoggingQRAttemptsUseCase: GetLoggingQRAttemptsUseCase,
    val setLoggingQRAttemptsUseCase: SetLoggingQRAttemptsUseCase
) :
    BasePresenter<LoginQRContract.View>(), LoginQRContract.Presenter {

    private lateinit var authorizeEntity: AuthorizeEntity

    override fun onViewCreated(authorizeEntity: AuthorizeEntity) {
        view.setupView()
        this.authorizeEntity = authorizeEntity
    }

    override fun onQRHelpClicked() {
        view.showQRHelp()
    }

    override fun onQRButtonClicked() {
        view.checkCameraPermission()
    }

    override fun checkIdentifier(idType: String, identifier: String) {
        when (idType) {
            MsspaAuthenticationUserEntity.ID_TYPE_NUSS_NUHSA -> {
                if (identifier.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_NUSHA) {
                    validateNuhsa(identifier)
                }
            }
            MsspaAuthenticationUserEntity.ID_TYPE_DNI -> {
                if (identifier.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_IDENTIFIER) {
                    validateDNI(identifier)
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

    override fun onIdentifierTypeSelected(key: String, id: String) {
        view.showValidIdentifier()

        if (id.isNotEmpty()) {
            validateIdentifier(key, id)
        }
    }

    override fun validateIdentifier(key: String, id: String): Boolean {
        return when (key) {
            MsspaAuthenticationUserEntity.ID_TYPE_NUSS_NUHSA -> validateNuhsa(id)
            MsspaAuthenticationUserEntity.ID_TYPE_DNI -> validateDNI(id)
            MsspaAuthenticationUserEntity.ID_TYPE_NIE -> validateNIE(id)
            else -> validateOtherIdentifier(id)
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

    private fun validateDNI(dni: String): Boolean =
        if (Utils.validateDni(dni)) {
            view.showValidIdentifier()
            true
        } else {
            view.showErrorDNIIdentifier()
            false
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

    private fun validateNuhsa(nuhsa: String): Boolean =
        if (nuhsa.isNotBlank()
                .and(nuhsa.length == MsspaAuthConsts.MAX_NUHSA_LENGTH)
                .and(nuhsa.startsWith(MsspaAuthConsts.NUHSA_PREFIX))
        ) {
            view.showValidNuhsa()
            true
        } else {
            view.showErrorNuhsa()
            false
        }

    override fun onLoginButtonClicked(qrCode: String, idType: String, id: String) {
        if (qrCode.isEmpty() || id.isEmpty()) {
            view.showWarning(R.string.missing_fields, onAccept = {})
            return
        }
        if (!validateIdentifier(idType, id)) {
            view.showWarning(R.string.message_invalid_login_data_qr, onAccept = {})
            return
        }
        if (!getLoggingQRAttemptsUseCase.execute()) {
            authorizeUseCase.execute(
                onSuccess = { newAuthorizeEntity ->
                    tryToLogin(newAuthorizeEntity, qrCode, idType, id)
                },
                onError = ::handleCommonErrors
            )
        }else{
            tryToLogin(authorizeEntity, qrCode, idType, id)
        }
    }

    private fun tryToLogin(
        authorize: AuthorizeEntity, qrCode: String, idType: String, id: String
    ) {
        view.showLoading()
        setLoggingQRAttemptsUseCase.params(false).execute(
            onComplete = {
                Timber.d("Logging QRAttempts false")
                loginQRUseCase.params(authorize = authorize, qr = qrCode, idType = idType, id = id)
                loginQRUseCase.execute(
                    onSuccess = { authEntity ->
                        view.apply {
                            sendEvent(buildEvent(LOGIN_QR, true))
                            hideLoading()
                            authEntity.authorizeEntity = authorize
                            navigateToPin(authEntity)
                        }
                    },
                    onError = {
                        view.apply {
                            sendEvent(buildEvent(LOGIN_QR, false))
                            hideLoading()
                            handleError(it, qrCode, idType, id)
                        }
                    }
                )
            },
            onError = {
                Timber.e(it)
            }
        )
    }

    private fun handleError(exception: Throwable, qrCode: String, idType: String, id: String) {
        if (exception is LoginInvalidRepeatedException) {
            authorizeUseCase.execute(
                onSuccess = { newAuthorizeEntity ->
                    tryToLogin(newAuthorizeEntity, qrCode, idType, id)
                },
                onError = ::handleCommonErrors
            )
        } else {
            handleCommonErrors(exception)
        }
    }

    override fun unsubscribe() {
        authorizeUseCase.clear()
    }
}