package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.Children
import es.juntadeandalucia.msspa.saludandalucia.data.entities.ChildrenData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicHomeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuDataItem
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicScreenData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.Parameter
import es.juntadeandalucia.msspa.saludandalucia.data.entities.Title
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicAreaEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicHomeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicItemEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicMenuEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.IconEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.LayoutEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.MetaEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.SubtitleEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.TitleEntity

class DynamicUIMapper {

    companion object {

        fun convertTo(data: DynamicMenuData): DynamicMenuEntity =
            DynamicMenuEntity(map = data.parameter.map { convertTo(it) })

        private fun convertTo(item: DynamicMenuDataItem): DynamicAreaEntity = with(item) {
            DynamicAreaEntity(
                area_desc,
                area_id,
                children.map { convertTo(it) })
        }

        private fun convertTo(child: ChildrenData): DynamicItemEntity = with(child) {
            DynamicItemEntity(
                children?.map { convertTo(it) }?.toMutableList() ?: mutableListOf(),
                id = id,
                navigation = navigation?.let {
                    NavigationEntity(
                        target = navigation.target,
                        type = navigation.type,
                        level = access_level,
                        title = title?.text ?: ""
                    )
                },
                icon = icon?.source?.let {
                    IconEntity(
                        it
                    )
                } ?: IconEntity(),
                title = title?.let {
                    TitleEntity(
                        text = it.text,
                        alt = it.alt
                    )
                }
            )
        }

        fun convertTo(data: DynamicHomeData): DynamicHomeEntity =
            DynamicHomeEntity(
                meta = MetaEntity(
                    data.meta.lastUpdated
                ),
                layouts = data.parameter.map { convertIt(it) })

        private fun convertIt(parameter: Parameter) = with(parameter) {
            LayoutEntity(
                children = children.map { convertIt(it) },
                type = type,
                title = Title(
                    title.alt,
                    title.color,
                    title.text
                )
            )
        }

        private fun convertIt(children: Children): DynamicElementEntity = with(children) {
            DynamicElementEntity(
                accessLevel = access_level,
                title = TitleEntity(
                    title.alt,
                    title.color,
                    title.text
                ),
                type = type,
                icon = IconEntity(
                    icon.source
                ),
                navigation = NavigationEntity(
                    target = navigation.target, type = navigation.type,
                    level = access_level
                ),
                id = id,
                background = background.source,
                subtitle = SubtitleEntity(subtitle.color, subtitle.text)
            )
        }

        fun convert(data: DynamicScreenData): DynamicSectionEntity =
            DynamicSectionEntity(
                meta = convert(data.meta),
                screens = data.parameter.map { convert(it) },
                resourceType = data.resourceType
            )

        private fun convert(meta: DynamicScreenData.Meta): DynamicSectionEntity.Meta = with(meta) {
            DynamicSectionEntity.Meta(
                lastUpdated = lastUpdated
            )
        }

        private fun convert(parameter: DynamicScreenData.Parameter): DynamicScreenEntity =
            with(parameter) {
                DynamicScreenEntity(
                    background = background.source,
                    children = convert(children),
                    header = header?.source,
                    id = id,
                    title = title?.let { convert(it) }
                )
            }

        //region Children
        private fun convert(children: List<DynamicScreenData.Parameter.Children>): List<DynamicElementEntity> =
            children.map { convert(it) }

        private fun convert(children: DynamicScreenData.Parameter.Children): DynamicElementEntity =
            with(children) {
                DynamicElementEntity(
                    accessLevel = access_level ?: "",
                    icon = icon?.let { convert(it) } ?: IconEntity(""),
                    id = id,
                    navigation = convert(navigation, access_level ?: ""),
                    title = convert(title)
                )
            }

        private fun convert(icon: DynamicScreenData.Parameter.Children.Icon): IconEntity =
            with(icon) {
                IconEntity(
                    source = source
                )
            }

        private fun convert(
            navigation: DynamicScreenData.Parameter.Children.Navigation,
            accessLevel: String
        ): NavigationEntity =
            with(navigation) {
                NavigationEntity(
                    target = target,
                    type = type,
                    level = accessLevel
                )
            }

        private fun convert(title: DynamicScreenData.Parameter.Children.Title): TitleEntity =
            with(title) {
                TitleEntity(
                    alt = alt,
                    text = text
                )
            }
        //endregion

        private fun convert(title: DynamicScreenData.Parameter.Title): TitleEntity =
            with(title) {
                TitleEntity(
                    alt = alt,
                    color = color,
                    text = text
                )
            }
    }
}
