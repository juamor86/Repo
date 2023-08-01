package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AuthorizeResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AuthorizeEntity

class AuthorizeMapper {

    companion object {
        fun convert(model: AuthorizeResponseData) = AuthorizeEntity(
            sessionData = model.sessionData,
            sessionId = model.sessionId
        )
    }
}
