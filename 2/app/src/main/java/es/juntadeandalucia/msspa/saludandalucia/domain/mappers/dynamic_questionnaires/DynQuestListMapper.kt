package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.dynamic_questionnaires

import es.juntadeandalucia.msspa.saludandalucia.data.entities.dynamic_questionnaires.DynQuestListData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity

class DynQuestListMapper {
    companion object {
        fun convert(monitoringProgram: DynQuestListData): DynQuestListEntity =
            with(monitoringProgram) {
                DynQuestListEntity(
                    questsFilled = if(entry.isEmpty()) mutableListOf() else entry[0].resource.map { DynamicQuestionMapper.convert(it) }.toMutableList()
                )
            }
    }
}