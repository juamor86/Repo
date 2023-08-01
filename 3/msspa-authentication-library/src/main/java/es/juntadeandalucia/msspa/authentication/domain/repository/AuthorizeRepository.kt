package es.juntadeandalucia.msspa.authentication.domain.repository

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginResponseData
import io.reactivex.Single

interface AuthorizeRepository {

    fun authorize(msspaAuthenticationConfig: MsspaAuthenticationConfig): Single<AuthorizeResponseData>
}
