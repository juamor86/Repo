package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.measure

import es.juntadeandalucia.msspa.saludandalucia.data.entities.MeasureHelperData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasureHelperEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class MeasureHelperMapper {

    companion object {

        val list: MutableList<MeasureHelperEntity> = mutableListOf()

        fun convert(model: MeasureHelperData): List<MeasureHelperEntity> {

            for (item in model.item) {
                var groupName = ""
                var range = ""
                var description = ""
                for (value in item.extension) {
                    when (value.url) {
                        Consts.MEASURE_TYPE -> {
                            if (value.valueCoding.code == Consts.MEASURE_HELPER_RANGE) {
                                range = item.text
                            } else if (value.valueCoding.code == Consts.MEASURE_HELPER_INFO) {
                                description = item.text
                            }
                        }
                        Consts.MEASURE_SUBTYPE -> {
                            groupName = value.valueCoding.display
                        }
                    }
                }

                when {
                    list.isEmpty() -> {
                        list.add(
                            MeasureHelperEntity(
                                groupName = groupName,
                                range = range,
                                helpText = description
                            )
                        )
                    }
                    filterHelperByTitle(groupName, list) -> {
                        list.filter { it.groupName == groupName }[0].helpText = description
                    }
                    else -> {
                        list.add(
                            MeasureHelperEntity(
                                groupName = groupName,
                                range = range,
                                helpText = description
                            )
                        )
                    }
                }
            }

            return list
        }

        private fun filterHelperByTitle(
            title: String,
            list: List<MeasureHelperEntity>
        ): Boolean {
            for (item in list) {
                if (item.groupName == title) {
                    return true
                }
            }
            return false
        }
    }
}
