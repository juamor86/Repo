package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.content.Context
import android.net.Uri
import android.util.Base64
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import io.jsonwebtoken.Jwts
import io.reactivex.Single
import org.json.JSONObject
import timber.log.Timber
import java.io.InputStream
import java.math.BigInteger
import java.security.KeyFactory
import java.security.SignatureException
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*

class ValidateContraindicationUseCase(
    val context: Context,
    val getTrustListUseCase: GetCovidCertTrustListUseCase
) : SingleUseCase<UECovidCertEntity>() {

    private lateinit var jwt: String

    fun params(qrString: String) =
        this.apply {
            val uri = Uri.parse(qrString)
            jwt = try {
                if (uri.getQueryParameter(Consts.TOKEN_PARAMETER) == null) "" else uri.getQueryParameter(
                    Consts.TOKEN_PARAMETER
                ).toString()
            }catch (e:Exception){
                ""
            }
        }
    fun paramToken(token: String) =
        this.apply {
            jwt = token
        }


    override fun buildUseCase() =
        Single.fromCallable {
            validateCertificate()
        }

    private fun validateCertificate(): UECovidCertEntity {
        return try {
            val inputStream: InputStream = context.resources.openRawResource(R.raw.public_key)
            val inputString = inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(inputString)
            val n = json.getString("n").replace('+', '-').replace('/', '_').replace("=", "")
            val e = json.getString("e")
            val eInt = BigInteger(1, Base64.decode(e, Base64.DEFAULT))
            val nInt = BigInteger(1, Base64.decode(n, Base64.URL_SAFE))
            val spec = RSAPublicKeySpec(nInt, eInt)
            val publicKey = KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey

            return try {
                val body = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwt).body
                val identifier: String? = body[Consts.JWT_IDENTIFIER] as String?
                identifier?.let {
                    val isValid = verificationValidityDate(body[Consts.JWT_VACCINE_DATE] as String)
                    UECovidCertEntity(
                        dni = it,
                        isOk = isValid
                    )
                } ?: UECovidCertEntity()
            } catch (e: SignatureException) {
                // don't trust the JWT!
                Timber.e("Signature not valid")
                UECovidCertEntity(isOk = false)
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            UECovidCertEntity(isOk = false)
        }
    }

    private fun verificationValidityDate(vaccinationDate: String): Boolean {
        val expeditionCert =  UtilDateFormat.getStringDateToLong(vaccinationDate)
        return expeditionCert <= Calendar.getInstance().time.time
    }
}