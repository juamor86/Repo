package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.content.Context
import ehn.techiop.hcert.kotlin.chain.DefaultChain
import ehn.techiop.hcert.kotlin.chain.VerificationResult
import ehn.techiop.hcert.kotlin.chain.impl.TrustListCertificateRepository
import ehn.techiop.hcert.kotlin.trust.TrustedCertificateV2
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UECovidCertEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.DAY_MILLISECONDS
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.VALIDATION_MAX_DAYS_CERTIFICATE
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat.Companion.daysBetweenDates
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat.Companion.getStringDateCertificateToLong
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat.Companion.instantToDateShortFormat
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.datetime.LocalDate

class ValidateGreenPassUseCase(
    val context: Context,
    val getTrustListUseCase: GetCovidCertTrustListUseCase
) :
    SingleUseCase<UECovidCertEntity>() {

    private lateinit var qrString: String

    override fun buildUseCase() = getTrustListUseCase.buildUseCase().flatMap {
        Single.fromCallable {
            validateCertificate(it.trustList)
        }
    }

    private fun validateCertificate(trustList: Array<TrustedCertificateV2>): UECovidCertEntity {
        val repository = TrustListCertificateRepository(trustList)
        val chain = DefaultChain.buildVerificationChain(repository)

        val result = chain.decode(qrString)

        val data = result.chainDecodeResult.eudgc
        data?.let {
            val name = it.subject.givenName
            val lastName = it.subject.familyName
            val fullName =
                "${it.subject.familyNameTransliterated}<<${it.subject.givenNameTransliterated}"
            var birthDate = it.dateOfBirthString
            birthDate = formatBirthDate(birthDate)

            val isValid = it.vaccinations?.let { vac ->
                validateDoseAndDate(
                    numberDose = vac[0]!!.doseNumber,
                    totalDose = vac[0]!!.doseTotalNumber,
                    dateVaccine = vac[0]!!.date,
                    birthDate = birthDate
                )
            } ?: true

            val isCertificateNotExpired = validateCertificateDates(result.verificationResult)
            return UECovidCertEntity(
                userName = name,
                userLastName = lastName,
                birthdate = birthDate,
                fullName = fullName,
                isOk = isValid && isCertificateNotExpired
            )
        }
        return UECovidCertEntity(isOk = false)
    }

    private fun formatBirthDate(birthDate: String): String {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var result = birthDate
        try {
            val date = dateFormatter.parse(birthDate)
            dateFormatter.applyPattern("dd/MM/yyyy")
            result = dateFormatter.format(date)
        } catch (e: Exception) {
        }
        return result
    }

    private fun checkIsAdult(birthDate: String): Boolean {
        val adultAgeMillis = DAY_MILLISECONDS.toLong() * 365 * 18
        val birthDateMillis = UtilDateFormat.getStringTraditionalShortDateToLong(birthDate)
        return adultAgeMillis + birthDateMillis <= System.currentTimeMillis()
    }

    private fun validateDoseAndDate(
        numberDose: Int,
        totalDose: Int,
        dateVaccine: LocalDate,
        birthDate: String
    ): Boolean {
        if ((numberDose == totalDose) && (totalDose == 1 || totalDose == 2)) {
            return if (checkIsAdult(birthDate)) {
                val dayCount = daysBetweenDates(
                    firstDate = Date().time,
                    lastDate = getStringDateCertificateToLong(dateVaccine.toString())
                )
                dayCount in 0..VALIDATION_MAX_DAYS_CERTIFICATE
            } else
                true
        }
        return true
    }

    private fun validateCertificateDates(result: VerificationResult): Boolean {
        val expirationTime: Date = instantToDateShortFormat(result.expirationTime)
        val validFrom: Date = instantToDateShortFormat(result.certificateValidFrom)
        val validUntil: Date = instantToDateShortFormat(result.certificateValidUntil)
        val issuedAt: Date = instantToDateShortFormat(result.issuedAt)

        val isCertificateNotExpired =
            expirationTime in validFrom..validUntil

        val isCertificateEmittedInTime =
            issuedAt in validFrom..validUntil

        return isCertificateNotExpired && isCertificateEmittedInTime
    }

    fun params(jwt: String) =
        this.apply {
            this@ValidateGreenPassUseCase.qrString = jwt
        }
}
