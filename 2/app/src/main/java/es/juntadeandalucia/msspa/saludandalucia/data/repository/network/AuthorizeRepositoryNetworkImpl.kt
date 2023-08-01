package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPALoginApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AuthorizeRepository
import io.reactivex.Single

class AuthorizeRepositoryNetworkImpl(
    private val msspaLoginApi: MSSPALoginApi
) : AuthorizeRepository {

    override fun authorize(): Single<AuthorizeResponseData> {
        return msspaLoginApi.authorize()
    }
}
