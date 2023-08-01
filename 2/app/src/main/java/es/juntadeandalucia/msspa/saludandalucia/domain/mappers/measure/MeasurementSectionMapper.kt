package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.measure

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementValueEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class MeasurementSectionMapper {

    companion object {

        private lateinit var currentPage: String
        private lateinit var lastPage: String

        fun convert(measures: List<MeasurementEntity>): MutableList<MeasurementSectionEntity> {

            filterUrl(measures[0].currentPage, measures[0].nextPage)

            val list: MutableList<MeasurementSectionEntity> = mutableListOf()
            for (measure in measures) {
                val headersAux = mutableListOf<String>()
                val valuesAux = mutableListOf<String>()
                val unitsAux = mutableListOf<String>()
                var date = ""
                var hour = ""
                var long = 0L

                for (value in measure.items) {
                    with(value) {
                        valueInteger?.run { valuesAux.add(toString()) }

                        valueDecimal?.run { valuesAux.add(toString()) }

                        valueTime?.run {
                            date = valueDate.toString()
                            hour = toString()
                            long = valueLong!!
                        }

                        if (unit != "") {
                            unitsAux.add(unit)
                        }

                        if ((valueInteger != 0 || valueDecimal != 0.0) && !headersAux.contains(title) && title.isNotEmpty()) {
                            headersAux.add(title)
                        }
                    }
                }

                var measurementValueEntity =
                    MeasurementValueEntity(listOf(), listOf(), "", "", 0L)
                if (valuesAux.count() > 0) {
                    measurementValueEntity = MeasurementValueEntity(
                        values = valuesAux,
                        units = unitsAux,
                        date = date.uppercase().replace('.', Character.MIN_VALUE),
                        hour = hour,
                        dateLong = long
                    )
                }

                if (measure.title != "" && measurementValueEntity.values.isNotEmpty()) {
                    formatMeasurement(measure.title, measurementValueEntity, headersAux, list)
                }
            }

            return list
        }

        private fun formatMeasurement(
            title: String,
            value: MeasurementValueEntity,
            header: MutableList<String>,
            list: MutableList<MeasurementSectionEntity>
        ) {
            val measurement = filterMeasurementByTitle(title, list)

            if (measurement != null) {
                measurement.values.add(value)
                if (measurement.values.count() > 1) {

                    addHeadersIfNoExist(measurement, header)
                }
            } else {
                val eHeaders =
                    if (value.values.count() > 1) {
                        header
                    } else {
                        mutableListOf<String>()
                    }
                val measurementSectionEntity =
                    MeasurementSectionEntity(
                        title,
                        mutableListOf(value),
                        eHeaders,
                        null,
                        null,
                        currentPage = currentPage.toInt(),
                        lastPage = lastPage.toInt()
                    )

                list.add(measurementSectionEntity)
            }
        }

        private fun addHeadersIfNoExist(
            measure: MeasurementSectionEntity,
            headers: MutableList<String>
        ) {
            for (header in headers) {
                if (!measure.headers.contains(header)) {
                    measure.headers.add(header)
                }
            }
        }

        private fun filterMeasurementByTitle(
            title: String,
            list: List<MeasurementSectionEntity>
        ): MeasurementSectionEntity? {

            for (item in list) {
                if (item.title == title) {
                    return item
                }
            }
            return null
        }

        private fun filterUrl(currentUrl: String, nextUrl: String) {

            currentPage = currentUrl.split(
                Consts.MEASURE_DELIMITER_PAGE,
                Consts.MEASURE_DELIMITER_AMPERSAND
            )[1]

            lastPage =
                nextUrl.split(Consts.MEASURE_DELIMITER_PAGE, Consts.MEASURE_DELIMITER_AMPERSAND)[1]
        }
    }
}
