package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Key value entity
 *
 * @property key
 * @property value
 * @constructor Create empty Key value entity
 */
@Parcelize
class KeyValueEntity(val key: String, val value: String) : Parcelable {

    override fun toString(): String = value

    override fun equals(other: Any?): Boolean = other is KeyValueEntity && other.key == key

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}