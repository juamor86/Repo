package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.BeneficiaryListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.GreenPassCertData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasureHelperData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.UserCovidCertData
import io.reactivex.Single

interface UserRepository {
    fun getUserLogged(): Single<Boolean>
    fun getUserCovidCert(accessToken: String?): Single<UserCovidCertData>
    fun getUserCovidCertPdf(accessToken: String?): Single<UserCovidCertData>
    fun getMeasuresData(page: Int): Single<MeasurementsData>
    fun getMeasureHelper(type: String): Single<MeasureHelperData>
    fun getGreenPass(type: String, accessToken: String?): Single<GreenPassCertData>
    fun getGreenPassPdf(format: String, type: String, accessToken: String?): Single<GreenPassCertData>
    fun getUserBeneficiaries(): Single<BeneficiaryListData>
}
