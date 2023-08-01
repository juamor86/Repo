package es.juntadeandalucia.msspa.authentication.data.factory.repository.mock

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginResponseData
import es.juntadeandalucia.msspa.authentication.domain.repository.AuthorizeRepository
import es.juntadeandalucia.msspa.authentication.utils.exceptions.TooManyRequestException
import io.reactivex.Single

class AuthorizeRepositoryMockImpl() : AuthorizeRepository {
    override fun authorize(msspaAuthenticationConfig: MsspaAuthenticationConfig): Single<AuthorizeResponseData> = Single.error(TooManyRequestException())
}
