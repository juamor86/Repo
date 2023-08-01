package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringListData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity

class MonitoringListMapper {

    companion object {
        fun convert(monitoringProgram: MonitoringListData): MonitoringListEntity =
            with(monitoringProgram) {
                MonitoringListEntity(
                    questsFilled = entry[0].resource.map { MonitoringQuestionMapper.convert(it) }.toMutableList()
                )
            }
    }
}
