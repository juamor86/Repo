package es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnswerQuantity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EnableWhenData(
    @SerializedName("answerBoolean")
    val answerBoolean: Boolean?,
    @SerializedName("answerInteger")
    val answerInteger: Int?,
    @SerializedName("answerCoding")
    val answerCoding: ValueCodingData?,
    @SerializedName("answerQuantity")
    val answerQuantity: AnswerQuantity?,
    @SerializedName("answerTime")
    val answerTime: String = "",
    @SerializedName("answerDate")
    val answerDate: String = "",
    @SerializedName("answerString")
    val answerString: String?,
    @SerializedName("operator")
    val operator: String = "",
    @SerializedName("question")
    val question: String = ""
): Parcelable
