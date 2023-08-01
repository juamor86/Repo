package es.juntadeandalucia.msspa.saludandalucia.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.EnableWhenData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ExtensionData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.fhir.ValueCodingData
import kotlinx.android.parcel.Parcelize

data class QuestionnaireData(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("item")
    val questions: List<QuestionData> = listOf(),
    @SerializedName("meta")
    val meta: MetaData = MetaData(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("purpose")
    val purpose: String = "",
    @SerializedName("resourceType")
    val resourceType: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("title")
    val title: String = ""
)

data class QuestionData(
    @SerializedName("answerOption")
    val answerOptions: List<AnswerOptionData> = listOf(),
    @SerializedName("enableBehavior")
    val enableBehavior: EnableBehaviorData = EnableBehaviorData(),
    @SerializedName("enableWhen")
    val enableWhen: List<EnableWhenData> = listOf(),
    @SerializedName("extension")
    val extension: List<ExtensionData> = listOf(),
    @SerializedName("item")
    val item: List<ItemXData> = listOf(),
    @SerializedName("linkId")
    val linkId: String = "",
    @SerializedName("maxLength")
    val maxLength: Int = 0,
    @SerializedName("prefix")
    val prefix: String = "",
    @SerializedName("repeats")
    val repeats: Boolean = false,
    @SerializedName("required")
    val required: Boolean = false,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("type")
    val type: String = ""
)

data class MetaData(
    @SerializedName("lastUpdated")
    val lastUpdated: String = ""
)

data class AnswerOptionData(
    @SerializedName("valueCoding")
    val valueCoding: ValueCodingData = ValueCodingData(),
    @SerializedName("valueDate")
    val valueDate: String = "",
    @SerializedName("valueInteger")
    val valueInteger: Int = 0,
    @SerializedName("valueString")
    val valueString: String = "",
    @SerializedName("valueTime")
    val valueTime: String = ""
)

class EnableBehaviorData()

@Parcelize
class AnswerQuantity (
    @SerializedName("value")
    val value: Float = 0F
): Parcelable

data class ItemXData(
    @SerializedName("linkId")
    val linkId: String = "",
    @SerializedName("required")
    val required: Boolean = false,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("type")
    val type: String = ""
)

data class ValueRangeData(
    @SerializedName("high")
    val high: Int = 0,
    @SerializedName("low")
    val low: Int = 0
)
