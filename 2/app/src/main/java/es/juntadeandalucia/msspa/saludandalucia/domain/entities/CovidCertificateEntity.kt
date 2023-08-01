package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CovidCertificateEntity(
    val type: String
) : Parcelable {
    VACCINATE(Consts.TYPE_VACCINATION),
    RECOVERY(Consts.TYPE_RECOVERY),
    TEST(Consts.TYPE_TEST)
}
