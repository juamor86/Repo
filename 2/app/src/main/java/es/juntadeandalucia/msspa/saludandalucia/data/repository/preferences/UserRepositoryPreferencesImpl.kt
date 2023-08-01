package es.juntadeandalucia.msspa.saludandalucia.data.repository.preferences

import android.content.SharedPreferences
import es.juntadeandalucia.msspa.saludandalucia.data.entities.BeneficiaryListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.GreenPassCertData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasureHelperData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.UserCovidCertData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

class UserRepositoryPreferencesImpl : UserRepository {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun getUserLogged(): Single<Boolean> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserCovidCert(accessToken: String?): Single<UserCovidCertData> {
        TODO("Not yet implemented")
    }

    override fun getUserCovidCertPdf(accessToken: String?): Single<UserCovidCertData> {
        TODO("Not yet implemented")
    }

    override fun getMeasuresData(page: Int): Single<MeasurementsData> {
        TODO("Not yet implemented")
    }

    override fun getGreenPass(type: String, accessToken: String?): Single<GreenPassCertData> {
        TODO("Not yet implemented")
    }

    override fun getGreenPassPdf(format: String, type: String, accessToken: String?): Single<GreenPassCertData> {
        TODO("Not yet implemented")
    }

    override fun getUserBeneficiaries(): Single<BeneficiaryListData> {
        TODO("Not yet implemented")
    }

    override fun getMeasureHelper(type: String): Single<MeasureHelperData> {
        TODO("Not yet implemented")
    }
}
