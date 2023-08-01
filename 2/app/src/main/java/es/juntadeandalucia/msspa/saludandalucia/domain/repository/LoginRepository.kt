package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.LoginResponseData
import io.reactivex.Single

interface LoginRepository {

    fun loginStep1(
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginResponseData>

    fun loginStep2(
        sessionId: String,
        sessionData: String,
        pinSMS: String
    ): Single<LoginResponseData>
}
