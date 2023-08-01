package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.WarningsData
import es.inteco.conanmobile.domain.entities.WarningEntity

/**
 * Warnings mapper
 *
 * @constructor Create empty Warnings mapper
 */
class WarningsMapper {
    companion object {
        fun convert(model: WarningsData): List<WarningEntity> = model.message.map {
            WarningEntity(
                it.id,
                it.title[0].value,
                it.description[0].value,
                it.creationDate,
                getImportance(it.importance)
            )
        }

        private fun getImportance(importance: String): WarningEntity.Importance =
            when (importance) {
                "LOW" -> WarningEntity.Importance.LOW
                "MEDIUM" -> WarningEntity.Importance.MEDIUM
                "HIGH" -> WarningEntity.Importance.HIGH
                else -> WarningEntity.Importance.LOW
            }
    }
}