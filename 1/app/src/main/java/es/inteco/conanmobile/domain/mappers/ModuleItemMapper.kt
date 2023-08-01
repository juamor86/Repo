package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.AdministrationItem
import es.inteco.conanmobile.data.entities.ModuleItem
import es.inteco.conanmobile.domain.entities.ModuleEntity
import java.util.*

/**
 * Module item mapper
 *
 * @constructor Create empty Module item mapper
 */
class ModuleItemMapper {
    companion object {
        fun convert(modules: List<ModuleItem>) = modules.map { convert(it) }

        fun convert(model: ModuleItem) = ModuleEntity(
            names = AdministrationItemMapper.convert(model.names),
            descriptions = convert(model.descriptions),
            valoration = model.valoration,
            code = model.code,
            showResult = model.showResult,
            comparable = model.comparable,
            assessment = model.assessment,
            type = ModuleEntity.getType(model.type)
        )

        private fun convert(description: List<AdministrationItem>): String {
            var result = ""
            val language = Locale.getDefault().language.lowercase()
            description.forEach {
                if(it.key.lowercase() == language){
                    result = it.value
                }
            }
            return result
        }
    }
}