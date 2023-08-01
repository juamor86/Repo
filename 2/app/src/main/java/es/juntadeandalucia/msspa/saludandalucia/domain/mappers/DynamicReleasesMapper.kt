package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicReleasesData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicReleasesSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.TitleEntity

class DynamicReleasesMapper {

    companion object {

        fun convert(data: DynamicReleasesData): DynamicReleasesSectionEntity =
            DynamicReleasesSectionEntity(
                meta = convert(data.meta),
                releases = data.parameter.map { convert(it) },
                resourceType = data.resourceType
            )

        private fun convert(meta: DynamicReleasesData.Meta): DynamicReleasesSectionEntity.Meta =
            with(meta) {
                DynamicReleasesSectionEntity.Meta(
                    lastUpdated = lastUpdated
                )
            }

        private fun convert(parameter: DynamicReleasesData.Parameter): DynamicReleasesEntity =
            with(parameter) {
                DynamicReleasesEntity(
                    id = id,
                    background = background?.source,
                    header = header?.source,
                    title = title,
                    titleAlt = titleAlt?.let { it },
                    children = children?.let { convert(it) },
                    displayCheck = displayCheck?.let { convert(it) }
                )
            }

        private fun convert(children: List<DynamicReleasesData.Parameter.Children>): List<DynamicReleasesElementEntity> =
            children.map { convert(it) }

        private fun convert(children: DynamicReleasesData.Parameter.Children): DynamicReleasesElementEntity =
            with(children) {
                DynamicReleasesElementEntity(
                    id = id,
                    title = convert(title)
                )
            }

        private fun convert(displayCheck: DynamicReleasesData.Parameter.DisplayCheck): DynamicReleasesEntity.DisplayCheckEntity =
            with(displayCheck) {
                DynamicReleasesEntity.DisplayCheckEntity(
                    id = id,
                    title = title
                )
            }

        private fun convert(title: DynamicReleasesData.Parameter.Children.Title): TitleEntity =
            with(title) {
                TitleEntity(
                    alt = alt,
                    text = text
                )
            }

    }
}