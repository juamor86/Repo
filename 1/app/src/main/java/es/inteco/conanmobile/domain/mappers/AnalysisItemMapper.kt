package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.AnalysisItem
import es.inteco.conanmobile.domain.entities.AnalysisEntity

/**
 * Analysis item mapper
 *
 * @constructor Create empty Analysis item mapper
 */
class AnalysisItemMapper {
    companion object {
        fun convert(analysis: List<AnalysisItem>) = analysis.map { convert(it) }

        fun convert(model: AnalysisItem) = with(model) {
            AnalysisEntity(
                id = id,
                names = AdministrationItemMapper.convert(model.names),
                descriptions = AdministrationItemMapper.convert(model.descriptions),
                applicationModules = if(!model.applicationModules.isNullOrEmpty()) ModuleItemMapper.convert(model.applicationModules) else emptyList(),
                deviceModules = if(!model.deviceModules.isNullOrEmpty()) ModuleItemMapper.convert(model.deviceModules) else emptyList(),
                systemModules = if(!model.systemModules.isNullOrEmpty()) ModuleItemMapper.convert(model.systemModules) else emptyList()
            )
        }
    }
}