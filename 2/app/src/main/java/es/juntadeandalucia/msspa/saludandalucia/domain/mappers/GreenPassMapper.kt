package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import ehn.techiop.hcert.kotlin.crypto.CertificateAdapter
import ehn.techiop.hcert.kotlin.trust.TrustedCertificateV2
import es.juntadeandalucia.msspa.saludandalucia.data.entities.GreenPassCertData
import es.juntadeandalucia.msspa.saludandalucia.domain.cache.TrustListCache
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassCertEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class GreenPassMapper {

    companion object {

        fun convert(model: GreenPassCertData, type: String): GreenPassCertEntity = with(model) {
            if (content.isNotEmpty()) {
                val data = content[0].attachment.data
                val jwt = String(Base64.decode(data, Base64.DEFAULT))
                val jsonObj = Gson().fromJson(jwt, JsonObject::class.java)

                val jsonUserName = jsonObj.getAsJsonObject(Consts.CERT_NAME_AREA)
                val userName =
                    (jsonUserName.get(Consts.USER_NAME).asString ?: "")
                val userSurname = (jsonUserName.get(Consts.USER_SURNAME).asString ?: "")

                val userBirthdate = jsonObj.get(Consts.USER_BIRTHDATE).asString ?: ""

                var qr = ""

                var vaccinationDate = ""
                var vaccineName = ""
                var totalVaccines = ""
                var vaccineNumber = ""

                var positiveRecovery = ""
                var recoveryDate = ""

                var negativeTestDate = ""
                var testType = ""

                when (type) {
                    Consts.TYPE_VACCINATION -> {
                        val jsonCertContent =
                            jsonObj.getAsJsonArray(Consts.TYPE_VACCINATION_SHORT)[0] as JsonObject
                        vaccinationDate =
                            jsonCertContent.get(Consts.DATE_VACCINATION_PARAM)?.asString ?: ""
                        vaccineName = jsonCertContent.get(Consts.VACCINE_NAME)?.asString ?: ""
                        totalVaccines = jsonCertContent.get(Consts.TOTAL_VACCINES)?.asString ?: ""
                        vaccineNumber = jsonCertContent.get(Consts.VACCINE_NUMBER)?.asString ?: ""
                        qr = jsonCertContent.get(Consts.QR)?.asString ?: ""
                    }
                    Consts.TYPE_RECOVERY -> {
                        val jsonCertContent =
                            jsonObj.getAsJsonArray(Consts.TYPE_RECOVERY_SHORT)[0] as JsonObject
                        positiveRecovery =
                            jsonCertContent.get(Consts.RECOVERY_STAUS)?.asString ?: ""
                        recoveryDate = jsonCertContent.get(Consts.RECOVERY_DATE)?.asString ?: ""
                        qr = jsonCertContent.get(Consts.QR)?.asString ?: ""
                    }
                    Consts.TYPE_TEST -> {
                        val jsonCertContent =
                            jsonObj.getAsJsonArray(Consts.TYPE_TEST_SHORT)[0] as JsonObject
                        negativeTestDate =
                            jsonCertContent.get(Consts.NEGATIVE_TEST_DATE)?.asString ?: ""
                        testType = jsonCertContent.get(Consts.TEST_TYPE)?.asString ?: ""
                        qr = jsonCertContent.get(Consts.QR)?.asString ?: ""
                    }
                    else -> {
                    }
                }

                val userId: String = jsonObj.get(Consts.USER_ID_PARAM)?.asString ?: ""
                return GreenPassCertEntity(
                    display = this.type.coding[0].display,
                    qr = qr,
                    userName = userName,
                    userSurname = userSurname,
                    userBirthdate = userBirthdate,
                    userId = userId,
                    certificateDate = vaccinationDate,
                    positiveRecovery = positiveRecovery,
                    recoveryDate = recoveryDate,
                    negativeTestDate = negativeTestDate,
                    testType = testType,
                    vaccinationDate = vaccinationDate,
                    vaccineName = vaccineName,
                    totalVaccines = totalVaccines,
                    vaccineNumber = vaccineNumber,
                    jwt = data
                )
            }
            GreenPassCertEntity()
        }

        fun convert(jsonArray: List<JsonObject>): TrustListCache {
            val resultList = mutableListOf<TrustedCertificateV2>()
            jsonArray.forEach {
                val certString = it.get("certificado").asString
                resultList.add(CertificateAdapter(certString).toTrustedCertificate())
            }
            return TrustListCache(resultList.toTypedArray(), System.currentTimeMillis())
        }
    }
}
