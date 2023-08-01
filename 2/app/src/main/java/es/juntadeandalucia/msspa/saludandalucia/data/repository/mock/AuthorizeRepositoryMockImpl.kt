package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AuthorizeRepository
import io.reactivex.Single

class AuthorizeRepositoryMockImpl() : AuthorizeRepository {

    override fun authorize(): Single<AuthorizeResponseData> = Single.error(TooManyRequestException())
}
