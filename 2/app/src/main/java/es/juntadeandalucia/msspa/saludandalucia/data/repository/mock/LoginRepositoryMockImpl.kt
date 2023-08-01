package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import es.juntadeandalucia.msspa.saludandalucia.data.entities.LoginResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.LoginRepository
import io.reactivex.Single

class LoginRepositoryMockImpl : LoginRepository {

    override fun loginStep1(
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginResponseData> = Single.error(TooManyRequestException())

    override fun loginStep2(
        sessionId: String,
        sessionData: String,
        pinSMS: String
    ): Single<LoginResponseData> = Single.error(TooManyRequestException())
}
