package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.*
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.OtherApkEntity
import es.inteco.conanmobile.domain.entities.ServiceEntity
import java.util.*

/**
 * Post analysis mapper
 *
 * @constructor Create empty Post analysis mapper
 */
class PostAnalysisMapper {
    companion object {
        fun convert(
            analysisResultEntity: AnalysisResultEntity,
            configurationVersion: String,
            analysisEntity: AnalysisEntity
        ): AnalysisResultRequestData {
            val devices: MutableList<Device> = mutableListOf()
            val apps: MutableList<Application> = mutableListOf()

            val version = Version(
                analysisResultEntity.device.releaseVersion,
                analysisResultEntity.device.codeName,
                analysisResultEntity.device.securityPatch
            )
            val systems = SystemClass(
                analysisResultEntity.device.id.toString(),
                analysisResultEntity.device.brand,
                analysisResultEntity.device.manufacturer,
                analysisResultEntity.device.model,
                version,
                analysisResultEntity.device.language,
                analysisResultEntity.device.product,
                if (analysisResultEntity.device.isBotnet == 1) "true" else "false"
            )

            val date = Date().time
            val result = Result(
                date.toString(),
                analysisEntity.names[0].value,
                analysisEntity.id!!,
                devices,
                systems,
                apps
            )

            analysisResultEntity.deviceItems.forEach {
                devices.add(Device(it.item.code, it.notOk.toString()))
            }
            analysisResultEntity.appsItems.forEach {
                apps.add(
                    Application(it.hash,
                        it.name,
                        "${it.name}_${it.hash}",
                        it.version,
                        it.origin,
                        it.fingerprint,
                        isPrivileged = if (it.isPrivileged == 1) "true" else "false",
                        isAllowedUnknownSources = if (it.isAllowedUnknownSources == 1) "true" else "false",
                        isSystemApplication = if (it.isSystemApplication == 1) "true" else "false",
                        isNotificationAccessEnabled = if (it.isNotificationAccessEnabled == 1) "true" else "false",
                        it.packageId,
                        it.safetynetResult,
                        permissions = it.permissions.filter { it.isGranted() }
                            .map { it.permissionID },
                        permissionsAPK = it.permissions,
                        hashAnalysis = ServiceList(it.hashAnalysis.map { service -> convert(service) }),
                        domainsAnalysis = it.apkDomains.map { other -> convert(other) },
                        ipsAnalysis = it.apkIps.map { other -> convert(other) },
                        urlsAnalysis = it.apkUrls.map { other -> convert(other) })
                )
            }
            return AnalysisResultRequestData(configurationVersion.toLong(), result)
        }

        private fun convert(model: OtherApkEntity): OtherApkData =
            OtherApkData(model.data, model.services.map { convert(it) })

        fun convert(model: ServiceEntity) = Service(model.service, model.result, model.analysisDate)
    }
}