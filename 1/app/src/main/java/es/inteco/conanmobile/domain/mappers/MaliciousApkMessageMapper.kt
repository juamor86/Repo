package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.ApkData
import es.inteco.conanmobile.data.entities.ApkService
import es.inteco.conanmobile.data.entities.MaliciousApkMessage
import es.inteco.conanmobile.domain.entities.MaliciousApkMessageEntity

/**
 * Malicious apk message mapper
 *
 * @constructor Create empty Malicious apk message mapper
 */
class MaliciousApkMessageMapper {
    companion object {
        fun convert(model: MaliciousApkMessage) = MaliciousApkMessageEntity(
            datas = DataMapper.convert(model.data)
        )
    }

    class DataMapper {
        companion object {
            fun convert(data: List<ApkData>) = data.map {convert(it)}

            fun convert(model: ApkData) = es.inteco.conanmobile.domain.entities.SAnalysis(
                data = model.data,
                type = model.type,
                services = ApkServicesMapper.convert(model.services)
            )
        }
    }

    class ApkServicesMapper{
        companion object {
            fun convert(services: List<ApkService>) = services.map {convert(it)}

            fun convert(model: ApkService) = es.inteco.conanmobile.domain.entities.ServiceEntity(
                service = model.service,
                result = model.result,
                analysisDate = model.analysisDate
            )
        }
    }
}