package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import androidx.core.net.toUri
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AppData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity

class AppMapper {

    companion object {

        fun convert(apps: List<AppData>) = apps.map { convert(it) }

        fun convert(model: AppData) = with(model) {
            AppEntity(
                appId = appId,
                name = model.name,
                description = model.description,
                alt = model.alt,
                category = model.category,
                icon = model.icon,
                link = model.link,
                packageName = model.version.ifEmpty { model.link.toUri().getQueryParameter("id") ?: model.version },
                images = model.images ?: emptyList()
            )
        }
    }
}
