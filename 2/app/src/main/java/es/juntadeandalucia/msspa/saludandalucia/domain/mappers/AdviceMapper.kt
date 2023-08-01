package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.EntryAdvice
import es.juntadeandalucia.msspa.saludandalucia.data.entities.EntryExtension
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.DataView
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class AdviceMapper {

    companion object {

        private var isReceived: Boolean? = false

        fun convert(adviceData: AdviceData) = AdviceEntity(
            id = adviceData.id,
            resourceType = adviceData.resourceType,
            meta = MetaMapper.convert(adviceData.meta),
            type = adviceData.type,
            total = adviceData.total,
            link = LinkMapper.convert(adviceData.link),
            entry = convertEntry(adviceData.entry)
        )

        private fun convertEntry(entryData: List<EntryAdvice>): List<EntryAdviceEntity> =
            entryData.map { convert(it) }

        fun convert(model: EntryAdvice) =
            with(model) {
                EntryAdviceEntity(
                    id = id,
                    resourceType = resourceType,
                    extension = EntryExtensionMapper.convert(extension),
                    text = text,
                    status = status,
                    contact = AdviceContactMapper.convert(contact),
                    reason = reason,
                    criteria = criteria,
                    channel = ChannelMapper.convert(channel),
                    isOwner = isOwner(model.extension)
                )
            }


        fun groupByEntry(
            adviceEntity: AdviceEntity,
            isDelegated: Boolean = false
        ): List<AdviceEntity> {
            isReceived = isDelegated
            val adviceList: MutableList<AdviceEntity> = mutableListOf()
            val entries = adviceEntity.entry?.filter { it.isOwner == !isDelegated }

            entries?.forEach { fatherEntry ->
                val advice =
                    adviceList.find { it.entry?.find { it.criteria == fatherEntry.criteria } != null }
                if (advice == null) {
                    adviceList.add(
                        AdviceEntity(
                            id = adviceEntity.id,
                            resourceType = adviceEntity.resourceType,
                            meta = adviceEntity.meta,
                            type = adviceEntity.type,
                            total = adviceEntity.total,
                            link = adviceEntity.link,
                            entry = entries.filter { it.criteria == fatherEntry.criteria }
                        )
                    )
                }
            }
            adviceList.forEach {
                it.total = it.entry?.size?.toLong() ?: 0L
                it.dataView = convertDataView(it)
            }
            return adviceList
        }

        private fun convertDataView(adviceEntity: AdviceEntity): DataView = DataView(
            isOwner = isOwnerAdvice(adviceEntity),
            sharedBy = sharedBy(adviceEntity),
            isShared = isSharedWith(adviceEntity),
            entryAdviceEntityReceived = getEntryAdviceEntityReceived(adviceEntity),
            title = getTitle(adviceEntity)
        )

        private fun getTitle(advice: AdviceEntity): String? = advice.entry?.get(0)?.text

        private fun isOwnerAdvice(advice: AdviceEntity): Boolean {
            var received = false
            var owner = false
            advice.entry?.forEach { item ->
                if (item.extension.size == 1) {
                    owner = true
                } else {
                    isReceived?.let {
                        received = it
                    }
                    if (!received) {
                        item.extension.forEach { extensionEntity ->
                            if (extensionEntity.url == Consts.SHARED_SUBSCRIPTION) {
                                owner = true
                            }
                        }
                    } else {
                        owner =
                            item.extension.filter { it.url == Consts.PARENT_SUBSCRIPTION && !item.isOwner }
                                .isEmpty()
                    }
                }
            }
            return owner
        }

        private fun sharedBy(advice: AdviceEntity): String {
            var shared = ""
            advice.entry?.forEach { item ->
                item.extension.forEach { extensionEntity ->
                    if (extensionEntity.url == Consts.PARENT_SUBSCRIPTION) {
                        shared = extensionEntity.valueReference.display
                    }
                }
            }
            return shared
        }

        private fun isSharedWith(advice: AdviceEntity): Boolean {
            var shared = 0
            advice.entry?.forEach { item ->
                item.extension.forEach { extensionEntity ->
                    if (extensionEntity.url == Consts.SHARED_SUBSCRIPTION) {
                        shared++
                    }
                }
            }
            return shared > 0
        }

        private fun getEntryAdviceEntityReceived(advice: AdviceEntity): EntryAdviceEntity? =
            advice.entry?.find { !it.isOwner }

        fun isOwner(extensions: List<EntryExtension>): Boolean {
            return extensions.filter { it.url == Consts.PARENT_SUBSCRIPTION }.isEmpty()
        }
    }
}