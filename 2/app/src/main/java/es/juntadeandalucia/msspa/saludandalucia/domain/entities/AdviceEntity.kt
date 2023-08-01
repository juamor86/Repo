package es.juntadeandalucia.msspa.saludandalucia.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdviceEntity(
    val id: String,
    val resourceType: String,
    val meta: MetaEntity,
    var type: String,
    var total: Long,
    val link: List<LinkEntity>,
    val entry: List<EntryAdviceEntity>?
) : Parcelable{
    lateinit  var dataView: DataView
}

@Parcelize
data class DataView(
    var isOwner: Boolean,
    var sharedBy: String?,
    val isShared: Boolean,
    val entryAdviceEntityReceived: EntryAdviceEntity?,
    var title: String?
): Parcelable

@Parcelize
data class EntryAdviceEntity(
    val id: String,
    val resourceType: String,
    val extension: List<EntryExtensionEntity>,
    val text: String,
    var status: String,
    var contact: List<AdviceContactEntity>? = null,
    val reason: String,
    val criteria: String,
    val channel: ChannelEntity,
    val isOwner: Boolean
) : Parcelable

@Parcelize
data class ChannelEntity (
        val type: String
) : Parcelable

@Parcelize
data class MetaEntity(
        val lastUpdated: String = ""
) : Parcelable

@Parcelize
data class AdviceContactEntity (
        val system: String,
        val value: String,
        val use: String,
        val extension: List<ContactExtensionEntity>
) : Parcelable

@Parcelize
data class EntryExtensionEntity (
        val url: String,
        val valueReference: ValueReferenceEntity
) : Parcelable
@Parcelize

data class ContactExtensionEntity(
    val url: String,
    var valueCode: String
) : Parcelable

@Parcelize
data class ValueReferenceEntity (
    val type: String,
    val id: String,
    var display: String,
    var name: String? = null,
    val extension: List<ValueReferenceExtensionEntity>? = null
) : Parcelable

@Parcelize
data class ValueReferenceExtensionEntity (
        val url: String,
        val valueCode: String
) : Parcelable

@Parcelize
data class LinkEntity (
        val relation: String,
        val url: String
) : Parcelable
