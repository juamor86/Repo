package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.BeneficiaryListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.GreenPassCertData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasureHelperData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.UserCovidCertData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.UserRepository
import io.reactivex.Single

class UserRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    UserRepository {

    override fun getUserLogged(): Single<Boolean> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserCovidCert(accessToken: String?): Single<UserCovidCertData> =
        msspaApi.getUserCovidCert(
            action = ApiConstants.CovidCert.FORMAT_HEADER_INIT,
            accessToken = accessToken)

    override fun getUserCovidCertPdf(accessToken: String?): Single<UserCovidCertData> =
        msspaApi.getUserCovidCert(
            action = ApiConstants.CovidCert.FORMAT_HEADER_PRINT,
            accessToken = accessToken)

    override fun getMeasuresData(page: Int): Single<MeasurementsData> =
        msspaApi.getMeasurement(page = page)

    override fun getGreenPass(type: String, accessToken: String?): Single<GreenPassCertData> =
        msspaApi.getGreenPass(
            type = type,
            accessToken = accessToken
        )

    override fun getGreenPassPdf(format: String, type: String, accessToken: String?): Single<GreenPassCertData> =
        msspaApi.getGreenPass(
            format = format,
            type = type,
            accessToken = accessToken
        )

    override fun getUserBeneficiaries(): Single<BeneficiaryListData> = msspaApi.getUserReceipts()
    override fun getMeasureHelper(type: String): Single<MeasureHelperData> =
        msspaApi.getHelpMeasure(type = type)
}
