package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.secondfactor

import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.LoginRequiredException
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LoginEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.LoginStep2UseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveQuizSessionUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveUserUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.crypto.Cipher
import timber.log.Timber

class SecondFactorPresenter(
    private val loginStep2UseCase: LoginStep2UseCase,
    private val saveQuizSessionUseCase: SaveQuizSessionUseCase,
    private val saveUserUseCase: SaveUserUseCase
) :
    BasePresenter<SecondFactorContract.View>(), SecondFactorContract.Presenter {

    lateinit var authorize: AuthorizeEntity
    lateinit var user: QuizUserEntity
    var saveUser: Boolean = false
    lateinit var pinSms: String

    override fun setupView(
        authorize: AuthorizeEntity,
        user: QuizUserEntity,
        saveUser: Boolean
    ) {
        view.setupView()

        this.authorize = authorize
        this.user = user
        this.saveUser = saveUser
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.LOGIN_COVID_TRIAGE_SECOND_FACTOR_SCREEN_ACCESS

    override fun onContinueClick(code: String) {
        tryToLogin(code)
    }

    private fun tryToLogin(
        pinSms: String
    ) {
        loginStep2UseCase.params(pinSms, authorize!!)
            .execute(onSuccess = {
                if (it.token.isNullOrEmpty()) {
                    view.showLoginDefaultError()
                } else {
                    user.name = it.userEntity.name
                    with(it) {
                        saveQuizSessionUseCase.params(QuizSession(tokenType = tokenType!!, accessToken = token!!)).execute(onComplete = {
                            Timber.d("Quiz session saved successfully")
                            if (saveUser) {
                                initSaveUser(user)
                            } else {
                                view.navigateToQuiz(user)
                            }
                        }, onError = {
                            Timber.e("Error saving quiz session: ${it.message}")
                        })
                    }
                }
                view.hideLoading()
            }, onError = {
                Timber.e(it)
                view.apply {
                    hideLoading()
                    when (it) {
                        is TooManyRequestException -> showTooManyRequestDialog()
                        is LoginRequiredException -> showInvalidCodeError()
                        else -> showErrorDialog()
                    }
                }
            })
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

    override fun unsubscribe() {
        loginStep2UseCase.clear()
        saveUserUseCase.clear()
    }

    override fun onCodeTextChanged(code: String) {
        if (code.length == Consts.CODE_SMS_LENGTH) {
            view.enableContinueButton(true)
        } else {
            view.enableContinueButton(false)
        }
    }
}
