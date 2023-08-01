package es.juntadeandalucia.msspa.saludandalucia.domain.bus

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.base.BehaviorSubjectUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigator
import io.reactivex.Single
import timber.log.Timber

class SessionBus(
    var session: Session,
    private val getUserSession: GetUserSessionUseCase,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val removeSession: RemoveSessionUseCase
    ) :
    BehaviorSubjectUseCase<Session>() {

    lateinit var navigator: DynamicNavigator

    val msspaAuthenticationEntity: MsspaAuthenticationEntity? get() = session.msspaAuthenticationEntity

    fun createSession(msspaAuthenticationEntity: MsspaAuthenticationEntity) {
        session.createSession(msspaAuthenticationEntity)
        saveSession(session)
        publish(session)
    }

    fun buildSessionUser(): Session? = getUserSession.execute()

    fun isRefreshToken(): Boolean = getUserSession.execute()?.let {
        !it.msspaAuthenticationEntity.refreshToken.isNullOrEmpty() && !it.msspaAuthenticationEntity.totpSecretKey.isNullOrEmpty()
    } ?: false

    private fun saveSession(session: Session) {
        saveSessionUseCase.params(session).execute(onComplete = {
            Timber.d("User session save successfully")
        }, onError = {
            Timber.e("Error saving user session: ${it.message}")
        })
    }

    fun clearSession() {
        removeSession.execute(
            onComplete = { Timber.d("User session remove successfully") },
            onError = { Timber.e("Error removing user session: ${it.message}") }
        )
        session.clearSession()
        publish(session)
    }

    fun performValidateToken(validate: Boolean) {
        getUserSession.execute()?.let { session ->
            if (validate) {
                this.session = session
                App.session = session
                publish(session)
            } else {
                clearSession()
            }
        }
    }

    fun onUnauthorizedEvent(){
        session.clearSession()
        navigator.informSessionExpired()
    }

    fun showErrorAndHome(){
        navigator.informErrorAndHome()
    }
}
