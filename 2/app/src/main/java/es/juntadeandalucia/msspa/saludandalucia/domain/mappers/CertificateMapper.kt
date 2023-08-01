package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import es.juntadeandalucia.msspa.saludandalucia.data.entities.WalletData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.GreenPassCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UserCovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletType
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.security.PinPatternCryptographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils

class CertificateMapper {

    companion object {

        fun convert(
            covidCertificate: GreenPassCertEntity,
            certType: String,
            userId: String
        ): WalletData =
            with(covidCertificate) {
                val idCipher = PinPatternCryptographyManager.encryptAndSavePassword(
                    userId + Utils.getCovidCertificateCode(certType)
                )
                val idType = Utils.getCovidCertificateCode(certType)
                val idJwt = PinPatternCryptographyManager.encryptAndSavePassword(jwt!!)

                WalletData(
                    id = idCipher,
                    type = idType,
                    jwt = idJwt
                )
            }

        fun convert(contraindicationCert: UserCovidCertEntity, userId: String): WalletData =
            with(contraindicationCert) {
                val idCipher = PinPatternCryptographyManager.encryptAndSavePassword(
                    userId + Utils.getCovidCertificateCode("")
                )
                val idType = Utils.getCovidCertificateCode("")
                val idJwt = PinPatternCryptographyManager.encryptAndSavePassword(jwt!!)

                WalletData(
                    id = idCipher,
                    type = idType,
                    jwt = idJwt
                )
            }

        fun convert(walletList: List<WalletData>): List<WalletEntity> =
            walletList.map { convert(it) }

        fun convert(model: WalletData): WalletEntity {
            val idDecrypt = PinPatternCryptographyManager.getDecryptedPassword(model.id)
            val type = mapType(model.type)
            val jwtDecrypt = PinPatternCryptographyManager.getDecryptedPassword(model.jwt)
            val data = decodeJWT(jwtDecrypt, type)

            return WalletEntity(
                id = idDecrypt,
                type = type,
                name = data[0],
                surname = data[1],
                qr = data[2],
                jwt = model.jwt
            )
        }


        fun mapType(type: Int): WalletType = when (type) {
            1 -> WalletType.VACCINE
            2 -> WalletType.TEST
            3 -> WalletType.RECOVERY
            else -> WalletType.CONTRAINDICATION
        }

        private fun decodeJWT(jwt: String, type: WalletType): List<String> {
            val name: String
            val surname: String
            val qr: String
            when (type) {
                WalletType.CONTRAINDICATION -> {
                    val json = String(Base64.decode(jwt.split(".")[1], Base64.DEFAULT))
                    val jsonObj = Gson().fromJson(json, JsonObject::class.java)
                    name = jsonObj.get(Consts.CERT_NAME)?.asString ?: ""
                    surname =
                        (jsonObj.get(Consts.CERT_FIRST_SURNAME)?.asString ?: "").plus(" ").plus(
                            jsonObj.get(Consts.CERT_SECOND_SURNAME)?.asString ?: ""
                        )
                    qr = Consts.VACCINE_CERT_QR_TEMPLATE + jwt
                }
                else -> {
                    val decode = String(Base64.decode(jwt, Base64.DEFAULT))
                    val jsonObject = Gson().fromJson(decode, JsonObject::class.java)
                    val jsonUserName = jsonObject.getAsJsonObject(Consts.CERT_NAME_AREA)
                    name =
                        (jsonUserName.get(Consts.USER_NAME).asString ?: "")
                    surname = (jsonUserName.get(Consts.USER_SURNAME).asString ?: "")
                    val jsonCertContent = when (type) {
                        WalletType.VACCINE ->
                            jsonObject.getAsJsonArray(Consts.TYPE_VACCINATION_SHORT)[0] as JsonObject

                        WalletType.TEST ->
                            jsonObject.getAsJsonArray(Consts.TYPE_TEST_SHORT)[0] as JsonObject

                        else ->
                            jsonObject.getAsJsonArray(Consts.TYPE_RECOVERY_SHORT)[0] as JsonObject

                    }
                    qr = jsonCertContent.get(Consts.QR)?.asString ?: ""
                }
            }

            return listOf(name, surname, qr)
        }

    }
}