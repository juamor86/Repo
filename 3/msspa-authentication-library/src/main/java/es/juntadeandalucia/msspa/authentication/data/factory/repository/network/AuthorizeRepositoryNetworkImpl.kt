package es.juntadeandalucia.msspa.authentication.data.factory.repository.network

import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.data.factory.api.MSSPAAuthorizeApi
import es.juntadeandalucia.msspa.authentication.data.factory.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.authentication.domain.repository.AuthorizeRepository
import io.reactivex.Single

class AuthorizeRepositoryNetworkImpl(
    private val msspaAuthorizeApi: MSSPAAuthorizeApi
) : AuthorizeRepository {

    override fun authorize(msspaAuthenticationConfig: MsspaAuthenticationConfig): Single<AuthorizeResponseData> {
        return msspaAuthorizeApi.authorize(
            clientId = msspaAuthenticationConfig.clientId,
            redirect = msspaAuthenticationConfig.redirectURI
        )
    }


}
