package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AuthorizeResponseData
import io.reactivex.Single

interface AuthorizeRepository {

    fun authorize(): Single<AuthorizeResponseData>
}
