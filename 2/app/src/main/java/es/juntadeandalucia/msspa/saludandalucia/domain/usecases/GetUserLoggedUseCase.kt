package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import javax.inject.Inject

class GetUserLoggedUseCase @Inject constructor(private val session: Session) :
    SynchronousUseCase<MsspaAuthenticationUserEntity?>() {

    // TODO: jtrueba - Get the logged user from preferences instead of the session in memory
    override fun execute(): MsspaAuthenticationUserEntity? = session.msspaAuthenticationEntity?.msspaAuthenticationUser
}
