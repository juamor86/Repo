package es.inteco.conanmobile.data.entities

import com.google.gson.annotations.SerializedName

/**
 * Malicious apk request data
 *
 * @property data
 * @constructor Create empty Malicious apk request data
 */
class MaliciousApkRequestData (
    @SerializedName("datas")
    val data : List<Data>
)

/**
 * Data
 *
 * @property data
 * @property type
 * @constructor Create empty Data
 */
data class Data (
    var data: String,
    var type: String
)