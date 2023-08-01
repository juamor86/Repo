package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.LoginResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LoginEntity

class LoginMapper {

    companion object {
        fun convert(model: LoginResponseData) = LoginEntity(
            token = model.access_token,
            tokenType = model.token_type,
            expires = model.expires_in,
            scope = model.scope,
            active = model.active
        )
    }
}
