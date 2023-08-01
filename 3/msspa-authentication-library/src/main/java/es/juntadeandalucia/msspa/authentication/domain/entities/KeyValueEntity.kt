package es.juntadeandalucia.msspa.authentication.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class KeyValueEntity(val key: String, val value: String) : Parcelable {

    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean =
            if (other is KeyValueEntity) other.key == key else false
}
