package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPALoginApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.LoginResponseData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.LoginRepository
import io.reactivex.Single

class LoginRepositoryNetworkImpl(
    private val msspaLoginApi: MSSPALoginApi
) : LoginRepository {

    override fun loginStep1(
        sessionId: String,
        sessionData: String,
        nuhsa: String,
        birthday: String,
        idType: String,
        identifier: String,
        phoneNumber: String
    ): Single<LoginResponseData> =
        msspaLoginApi.loginStep1(
            sessionId = sessionId,
            sessionData = sessionData,
            idType = idType,
            identifier = identifier,
            nuhsa = nuhsa,
            birthdate = birthday,
            phoneNumber = phoneNumber
        )

    override fun loginStep2(
        sessionId: String,
        sessionData: String,
        pinSMS: String
    ): Single<LoginResponseData> =

        msspaLoginApi.loginStep2(
            sessionId = sessionId,
            sessionData = sessionData,
            pin = pinSMS
        )
}
