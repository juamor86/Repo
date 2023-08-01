package es.inteco.conanmobile.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * O s i data
 *
 * @property message
 * @property path
 * @property status
 * @property statusMessage
 * @property timestamp
 * @constructor Create empty O s i data
 */
@Parcelize
data class OSIData(
    val message: Message = Message(),
    val path: String = "",
    val status: Int = 0,
    val statusMessage: String = "",
    val timestamp: String = ""
):Parcelable


/**
 * Message
 *
 * @property creationDate
 * @property osiTips
 * @property version
 * @constructor Create empty Message
 */
@Parcelize
data class Message(
    val creationDate: Long = 0,
    @SerializedName("osi_tips")
    val osiTips: List<OsiTip> = listOf(),
    val version: Int = 0
):Parcelable

/**
 * Osi tip
 *
 * @property description
 * @property id
 * @property title
 * @constructor Create empty Osi tip
 */
@Parcelize
data class OsiTip(
    val description: List<Description> = listOf(),
    val id: String = "",
    val title: List<Title> = listOf()
):Parcelable

/**
 * Description
 *
 * @property key
 * @property value
 * @constructor Create empty Description
 */
@Parcelize
data class Description(
    val key: String = "",
    val value: String = ""
):Parcelable

/**
 * Title
 *
 * @property key
 * @property value
 * @constructor Create empty Title
 */
@Parcelize
data class Title(
    val key: String = "",
    val value: String = ""
):Parcelable