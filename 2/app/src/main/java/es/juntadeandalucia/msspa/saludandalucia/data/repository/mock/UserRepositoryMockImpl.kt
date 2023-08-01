package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import android.content.Context
import com.google.gson.GsonBuilder
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.data.entities.BeneficiaryListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.GreenPassCertData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasureHelperData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.UserCovidCertData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.UserRepository
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import io.reactivex.Single
import java.io.InputStream
import java.io.StringReader
import timber.log.Timber

class UserRepositoryMockImpl(private val context: Context) : UserRepository {

    override fun getUserLogged(): Single<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getUserCovidCert(accessToken: String?): Single<UserCovidCertData> {
        TODO("Not yet implemented")
    }

    override fun getUserCovidCertPdf(accessToken: String?): Single<UserCovidCertData> {
        TODO("Not yet implemented")
    }

    override fun getGreenPass(type: String, accessToken: String?): Single<GreenPassCertData> =
        Single.just(
            GreenPassCertData(
                content = listOf(
                    GreenPassCertData.Content(
                        attachment = GreenPassCertData.Content.Attachment(
                            contentType = "application/jwt",
                            data = ""
                        ),
                        format = GreenPassCertData.Content.Format("View")
                    )
                ),
                id = "",
                relatesTo = listOf(
                    GreenPassCertData.RelatesTo(
                        code = "",
                        target = GreenPassCertData.RelatesTo.Target(
                            display = "Recuperación del COVID-19",
                            type = "DocumentReference",
                            reference = "/usuarios/historia/certificado?enfermedad=covid19&type=pruebas&format=Print"
                        )
                    )
                ),
                resourceType = "",
                status = "",
                type = GreenPassCertData.Type(coding = listOf())
            )
        )

    override fun getGreenPassPdf(
        format: String,
        type: String,
        accessToken: String?
    ): Single<GreenPassCertData> {
        TODO("Not yet implemented")
    }

    private fun readFromJson(type: String): GreenPassCertData {
        try {
            val inputStream: InputStream = when (type) {
                Consts.TYPE_VACCINATION -> context.resources.openRawResource(R.raw.greenpass_vaccination)
                Consts.TYPE_TEST -> context.resources.openRawResource(R.raw.greenpass_test)
                Consts.TYPE_RECOVERY -> context.resources.openRawResource(R.raw.greenpass_recovery)
                else -> null!!
            }
            val inputString = inputStream.bufferedReader().use { it.readText() }

            var stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson<GreenPassCertData>(
                stringReader,
                GreenPassCertData::class.java
            )
        } catch (e: Exception) {
            Timber.e("Error getting mock notifications: ${e.message}")
        }

        return null!!
    }

    override fun getUserBeneficiaries(): Single<BeneficiaryListData> {
        return Single.just(BeneficiaryListData())
    }

    override fun getMeasuresData(page: Int): Single<MeasurementsData> =
        Single.just(readFromJson(R.raw.measurements, MeasurementsData::class.java))

    override fun getMeasureHelper(type: String): Single<MeasureHelperData> =
        Single.just(readFromJson(R.raw.help_measure, MeasureHelperData::class.java))

    private fun <T> readFromJson(json: Int, clazz: Class<T>): T {
        try {
            val inputStream: InputStream = context.resources.openRawResource(json)
            val inputString = inputStream.bufferedReader().use { it.readText() }

            val stringReader = StringReader(inputString)

            val gsonBuilder = GsonBuilder().serializeNulls()
            val gson = gsonBuilder.create()

            return gson.fromJson(
                stringReader,
                clazz
            )
        } catch (e: Exception) {
            Timber.e("Error getting mock announcements: ${e.message}")
            return null!!
        }
    }
}
