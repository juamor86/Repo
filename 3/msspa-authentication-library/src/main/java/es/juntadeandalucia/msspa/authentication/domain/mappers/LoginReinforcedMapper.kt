package es.juntadeandalucia.msspa.authentication.domain.mappers

import es.juntadeandalucia.msspa.authentication.data.factory.entities.LoginReinforcedResponseData
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity

class LoginReinforcedMapper {
    companion object {
        fun convert(model: LoginReinforcedResponseData) = AuthorizeEntity(
            sessionId = model.sessionID,
            sessionData = model.sessionData
        )
    }
}