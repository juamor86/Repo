package es.juntadeandalucia.msspa.authentication.domain.mappers

import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginResponseData
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginRefreshTokenResponseData
import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginTOTPResponseData
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity

class LoginMapper {
    companion object {
        fun convert(model: LoginResponseData, msspaAuthenticationUser: MsspaAuthenticationUserEntity?) = MsspaAuthenticationEntity(
            accessToken = model.access_token,
            tokenType = model.token_type,
            expiresIn = model.expires_in.toInt(),
            scope = MsspaAuthenticationManager.Scope.getScope(model.scope),
            msspaAuthenticationUser = msspaAuthenticationUser,
            authorizeEntity = null
        )


        fun convert(model: LoginTOTPResponseData) = MsspaAuthenticationEntity(
            accessToken = model.access_token,
            tokenType = model.token_type,
            expiresIn = model.expires_in.toInt(),
            refreshToken = model.refresh_token,
            totpSecretKey = model.totp_secret_key,
            scope = MsspaAuthenticationManager.Scope.getScope(model.scope),
            authorizeEntity = null
        )

        fun convert(model : LoginRefreshTokenResponseData) = MsspaAuthenticationEntity(
            accessToken = model.access_token,
            tokenType = model.token_type,
            expiresIn = model.expires_in.toInt(),
            refreshToken = model.refresh_token,
            scope = MsspaAuthenticationManager.Scope.getScope(model.scope),
            authorizeEntity = null
        )
    }
}
