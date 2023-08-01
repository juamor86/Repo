package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdviceTypeEntity(
        val id: String,
        val resourceType: String,
        val meta: AdviceTypeMeta,
        val type: String,
        val total: Long,
        val link: List<AdviceTypeLink>,
        val entry: List<AdviceTypeEntry>
) : Parcelable

@Parcelize
data class AdviceTypeEntry (
        val resource: AdviceTypeResource
) : Parcelable

@Parcelize
data class AdviceTypeResource (
        val id: String,
        val resourceType: String,
        val text: String,
        val status: String,
        val reason: String,
        val criteria: String,
        val channel: AdviceTypeChannel
) : Parcelable

@Parcelize
data class AdviceTypeChannel (
        val type: String
) : Parcelable

@Parcelize
data class AdviceTypeLink (
        val relation: String,
        val url: String
) : Parcelable

@Parcelize
data class AdviceTypeMeta (
        val lastUpdated: String
) : Parcelable
