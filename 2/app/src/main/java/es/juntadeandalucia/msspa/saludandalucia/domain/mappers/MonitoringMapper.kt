package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.SendMonitoringAnswersResponseData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.monitoring.MonitoringData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.SendMonitoringAnswersResponseEntity
import java.util.*

class MonitoringMapper {

    companion object {
        fun convert(monitoringData: MonitoringData): MonitoringEntity = with(monitoringData) {
            MonitoringEntity(
                entry = entry.map {
                    convert(it)
                }.filter { it.code.toLowerCase(Locale.ROOT) != "ayuda" }
            )
        }

        private fun convert(entry: MonitoringData.Entry): MonitoringEntity.MonitoringEntry =
            with(entry) {
                MonitoringEntity.MonitoringEntry(
                    id = resource.id,
                    title = resource.name,
                    code = resource.code[0].code,
                    type = resource.extension[0].valueReference.type
                )
            }

        private fun convert(resource: MonitoringData.Entry.Resource): MonitoringEntity.MonitoringEntry.Resource =
            with(resource) {
                MonitoringEntity.MonitoringEntry.Resource(
                    extension = extension.map { convert(it) },
                    identifier = this.identifier[0].value
                )
            }

        private fun convert(extension: MonitoringData.Entry.Resource.Extension): MonitoringEntity.MonitoringEntry.Resource.Extension =
            with(extension) {
                MonitoringEntity.MonitoringEntry.Resource.Extension(
                    url = url,
                    valueReference = convert(valueReference)
                )
            }

        private fun convert(valueRef: MonitoringData.Entry.Resource.Extension.ValueReference): MonitoringEntity.MonitoringEntry.Resource.Extension.ValueReference =
            with(valueRef) {
                MonitoringEntity.MonitoringEntry.Resource.Extension.ValueReference(
                    display = display,
                    id = id,
                    type = type
                )
            }

        fun convert(response: SendMonitoringAnswersResponseData): SendMonitoringAnswersResponseEntity {
            with(response) {
                return SendMonitoringAnswersResponseEntity(
                    authored,
                    extension[1].valueString
                )
            }
        }
    }
}
