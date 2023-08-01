package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Ip botnet entity
 *
 * @property ip
 * @property error
 * @property evidences
 * @constructor Create empty Ip botnet entity
 */
@Parcelize
class IpBotnetEntity (
    @SerializedName("ip")
    val ip: String,
    @SerializedName("error")
    val error: String,
    @SerializedName("evidences")
    val evidences: List<EvidenceEntity>
    ): Parcelable

/**
 * Evidence entity
 *
 * @property name
 * @property threatCode
 * @property operatingSystems
 * @property descriptionURL
 * @property timestamp
 * @constructor Create empty Evidence entity
 */
@Parcelize
data class EvidenceEntity (
    @SerializedName("name")
    val name: String,
    @SerializedName("threatCode")
    val threatCode: String,
    @SerializedName("operatingSystems")
    val operatingSystems: List<OperatingSystemEntity>,
    @SerializedName("descriptionUrl")
    val descriptionURL: String,
    @SerializedName("timestamp")
    val timestamp: String
): Parcelable

/**
 * Operating system entity
 *
 * @property operatingSystem
 * @property disinfectURL
 * @constructor Create empty Operating system entity
 */
@Parcelize
data class OperatingSystemEntity (
    @SerializedName("operatingSystem")
    val operatingSystem: String,
    @SerializedName("desinfectedURL")
    val disinfectURL: List<String>
): Parcelable