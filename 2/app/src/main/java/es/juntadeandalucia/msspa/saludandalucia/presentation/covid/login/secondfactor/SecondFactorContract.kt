package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.login.secondfactor

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import javax.crypto.Cipher

class SecondFactorContract {

    interface View : BaseContract.View {
        fun navigateToQuiz(user: QuizUserEntity)
        fun setupView()
        fun showLoginDefaultError()
        fun authenticateForEncryption(
            onSuccess: (Cipher, Cipher) -> Unit,
            onError: (String) -> Unit
        )

        fun enableContinueButton(active: Boolean)
        fun fillCodeAndContinue(code: String)
        fun showInvalidCodeError()
    }

    interface Presenter : BaseContract.Presenter {
        fun onContinueClick(code: String)
        fun onCodeTextChanged(code: String)
        fun setupView(authorize: AuthorizeEntity,
                      user: QuizUserEntity,
                      saveUser: Boolean)
    }
}
