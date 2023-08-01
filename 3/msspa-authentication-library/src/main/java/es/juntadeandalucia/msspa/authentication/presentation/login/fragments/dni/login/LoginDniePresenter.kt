package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login

import android.app.Activity
import es.gob.jmulticard.jse.provider.DnieProvider
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.usecases.AuthorizeUseCase
import es.juntadeandalucia.msspa.authentication.domain.usecases.LoginDniUseCase
import es.juntadeandalucia.msspa.authentication.presentation.base.BasePresenter
import timber.log.Timber
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

class LoginDniePresenter(
    val authorizeUseCase: AuthorizeUseCase,
    private val loginDniUseCase: LoginDniUseCase
) :
    BasePresenter<LoginDnieContract.View>(), LoginDnieContract.Presenter {

    override fun onViewCreated() {
        if(view.checkPhoneIsNFCCompatible()) {
            view.setupView()
        }else{
            view.showAlertNotNFCCompatible()
        }
    }

    override fun onContinueClicked() {
        view.navigateToReadDni()
    }


    override fun onDniReaded(dniKeystore: KeyStore, activity: Activity) {
        val certificate =
            dniKeystore.getCertificate(DnieProvider.AUTH_CERT_ALIAS) as X509Certificate
        try {
            certificate.checkValidity()
            tryToLogin(dniKeystore, activity)
        } catch (exception: CertificateException) {
            view.authenticateInProgress()
            handleCommonErrors(exception)
        }

    }


    private fun tryToLogin(
        dniKeystore: KeyStore, activity: Activity
    ) {
        view.authenticateInProgress()
        view.showLoading()
        authorizeUseCase.execute(
            onSuccess = {
                loginDniUseCase.params(dniKeystore, activity, MsspaAuthenticationUserEntity(), it)
                    .execute(onSuccess = {
                        Timber.d("Login dni exito %s", it)
                        view.hideLoading()
                        view.setResultSuccess(it)

                    }, onError = {
                        handleCommonErrors(CertificateException())
                        Timber.d(it, "Login dni ERROR")
                        view.hideLoading()
                    })
            },
            onError = { error ->
                handleCommonErrors(CertificateException())
                Timber.d(error, "Error en authorize on dnie")
                view.hideLoading()
            }
        )

    }

}