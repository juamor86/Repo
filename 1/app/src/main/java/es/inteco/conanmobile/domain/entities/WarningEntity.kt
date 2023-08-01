package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Warning entity
 *
 * @property id
 * @property title
 * @property description
 * @property date
 * @property importance
 * @constructor Create empty Warning entity
 */
@Parcelize
class WarningEntity(
    val id: String,
    val title: String,
    val description: String,
    val date: Long,
    val importance: Importance
): Parcelable{
    /**
     * Importance
     *
     * @constructor Create empty Importance
     */
    enum class Importance {
        /**
         * High
         *
         * @constructor Create empty High
         */
        HIGH,

        /**
         * Medium
         *
         * @constructor Create empty Medium
         */
        MEDIUM,

        /**
         * Low
         *
         * @constructor Create empty Low
         */
        LOW}
}