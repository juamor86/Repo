package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.phonevalidation

import androidx.core.text.isDigitsOnly
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.AuthorizeUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.LoginValidatePhoneUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.SaveUserUseCase
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.authentication.utils.exceptions.LoginInvalidRepeatedException

class PhoneValidationPresenter(
    private val authorizeUseCase: AuthorizeUseCase,
    private val loginValidatePhoneUseCase: LoginValidatePhoneUseCase,
    private val saveUserUseCase: SaveUserUseCase
) :
    BasePresenter<PhoneValidationContract.View>(), PhoneValidationContract.Presenter {

    lateinit var authorize: AuthorizeEntity
    lateinit var accessToken: String
    lateinit var msspaAuthenticationUser: MsspaAuthenticationUserEntity

    override fun onViewCreated(authEntity: MsspaAuthenticationEntity, authorizeEntity: AuthorizeEntity) {
        view.setupView()
        this.authorize = authorizeEntity
        this.accessToken = authEntity.accessToken
        this.msspaAuthenticationUser = authEntity.msspaAuthenticationUser!!
    }

    override fun onContinueClick( phone: String) {
        val validPhone = validatePhoneNumber(phone)
        if (validPhone) {
            tryToLogin( phone)
        } else{
            view.enableLoginButton()
        }
    }

    private fun tryToLogin(phoneNumber: String) {
        view.showLoading()

        msspaAuthenticationUser.let { userEntity ->
            userEntity.phone = phoneNumber
            loginValidatePhoneUseCase.params(
                msspaAuthenticationUser = userEntity,
                authorize = authorize,
                jwt = accessToken
            ).execute(
                onSuccess = {
                    view.apply {
                        hideLoading()
                        enableLoginButton()
                        navigateToSMS(it, userEntity)
                    }
                },
                onError = { error ->
                    view.enableLoginButton()
                    handleError(error)
                }
            )
        }
    }

    private fun handleError(exception: Throwable) {
        if (exception is LoginInvalidRepeatedException) {
            authorizeUseCase.execute(
                onSuccess = { newAuthorizeEntity ->
                    this.authorize = newAuthorizeEntity
                    tryToLogin(msspaAuthenticationUser.phone)
                },
                onError = ::handleCommonErrors
            )
        } else {
            handleCommonErrors(exception)
        }
    }

    override fun unsubscribe() {
        loginValidatePhoneUseCase.clear()
        saveUserUseCase.clear()
    }


    override fun validatePhoneNumber(phone: String): Boolean =
        if (phone.isNotBlank() && phone.isDigitsOnly()) {
            if ( phone.length == 9) {
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


    override fun checkPhoneNumber( phone: String) {
            if (phone.length >= MsspaAuthConsts.Validations.VALIDATION_MIN_LENGTH_SPANISH_PHONE) {
                validatePhoneNumber(phone)
            }
    }

    override fun onOtherAuthMethodClicked() {
        view.showWarning(MsspaAuthenticationWarning.OTHER_AUTH_METHODS, onCancel = {})
    }
}