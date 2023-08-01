package es.juntadeandalucia.msspa.authentication.domain.mappers

import es.juntadeandalucia.msspa.authentication.data.factory.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity

class AuthorizeMapper {
    companion object {
        fun convert(model: AuthorizeResponseData) = AuthorizeEntity(
            sessionData = model.sessionData,
            sessionId = model.sessionId
        )
    }
}
