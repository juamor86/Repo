package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.measure

import es.juntadeandalucia.msspa.saludandalucia.data.entities.ItemExtension
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasurementsData.Entry.Resource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementItemEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat

class MeasurementMapper {

    companion object {
        private lateinit var currentPage: String
        private lateinit var lastPage: String

        fun getPages(links: List<MeasurementsData.Link>) {
            for (link in links) {
                if (link.relation == Consts.MEASURE_CURRENT_PAGE) {
                    currentPage = link.url
                } else if (link.relation == Consts.MEASURE_LAST_PAGE) {
                    lastPage = link.url
                }
            }
        }

        fun convert(resources: List<Resource>): List<MeasurementEntity> {
            val list: MutableList<MeasurementEntity> = mutableListOf()
            for (resource in resources) {
                formatResource(resource).forEach {
                    list.add(it)
                }
            }
            return list
        }

        private fun formatResource(resource: Resource): List<MeasurementEntity> {
            val list: MutableList<MeasurementEntity> = mutableListOf()
            val items = resource.item

                for (item in items) {
                    val measureEntity =
                        MeasurementEntity(
                            title = "",
                            items = listOf(),
                            currentPage = currentPage,
                            nextPage = lastPage
                        )
                    // Remember: 16/11/2021 This should get the date from resources.authored
                    // but we are getting the date from meta.lastUpdate.
                    // Could be necessary change it in the future
                    val long = UtilDateFormat.stringToDate(resource.meta.lastUpdated, UtilDateFormat.DATE_FORMAT_TZ)
                    val date = UtilDateFormat.getDateFormatterShort(long.time)
                    val hour = UtilDateFormat.timeToString(long)
                    measureEntity.items = formatResourceItem(item, date, hour, long.time)
                    val itemExtension = item.itemExtension
                    if (itemExtension.isNotEmpty()) {
                        measureEntity.title = formatHeader(itemExtension)
                    }
                    list.add(measureEntity)
                }

            return list
        }

        private fun formatResourceItem(
            resourceItem: Resource.Item,
            date: String,
            hour: String,
            dateLong: Long
        ): List<MeasurementItemEntity> {
            val list: MutableList<MeasurementItemEntity> = mutableListOf()

            if (resourceItem.subItem != null) {
                for (subItem in resourceItem.subItem) {
                    val item = MeasurementItemEntity(
                        title = "",
                        valueTime = null,
                        valueDecimal = null,
                        valueInteger = null,
                        valueDate = null,
                        valueLong = null,
                        unit = ""
                    )
                    if (subItem!!.answer.isNotEmpty()) {
                        val answer = subItem.answer

                        with(answer[0]) {
                            if (valueInteger != null) {
                                item.valueInteger = valueInteger
                            }
                            if (valueDecimal != null) {
                                item.valueDecimal = valueDecimal
                            }
                        }
                    }
                    item.valueTime = hour
                    item.valueDate = date
                    item.valueLong = dateLong

                    val itemExtension = subItem.subItemExtension
                    if (itemExtension.isNotEmpty()) {
                        item.title = formatHeader(itemExtension)
                        item.unit = formatUnit(itemExtension)
                    }
                    list.add(item)
                }
            }
            return list
        }

        private fun formatHeader(itemExtension: List<ItemExtension>): String {
            for (extension in itemExtension) {
                if (extension.url == Consts.MEASURE_SUBTYPE) {
                    if (extension.valueCoding != null) {
                        return extension.valueCoding.display
                    }
                }
            }
            return ""
        }

        private fun formatUnit(itemExtension: List<ItemExtension>): String {
            for (extension in itemExtension) {
                if (extension.url == Consts.MEASURE_UNIT) {
                    return extension.valueCode!!
                }
            }
            return ""
        }
    }
}
