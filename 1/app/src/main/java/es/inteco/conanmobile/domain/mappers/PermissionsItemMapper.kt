package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.PermissionsItem
import es.inteco.conanmobile.domain.entities.PermissionEntity
import es.inteco.conanmobile.domain.entities.PermissionRiskLevel

/**
 * Permissions item mapper
 *
 * @constructor Create empty Permissions item mapper
 */
class PermissionsItemMapper {
    companion object {
        fun convert(permissions: List<PermissionsItem>) =
            mutableMapOf<String, PermissionEntity>().apply {
                permissions.map { item ->
                    convert(item).let { entity -> put(entity.permissionID, entity) }
                }
            }

        fun convert(model: PermissionsItem) = with(model) {
            PermissionEntity(
                permissionID = permissionID,
                description = model.permissionDescriptions.first().value,
                riskLevel = convert(model.permissionRiskLevels)
            )
        }

        private fun convert(permissionRiskLevels: String): PermissionRiskLevel =
            when (permissionRiskLevels.lowercase()) {
                "bajo" -> PermissionRiskLevel.LOW
                "medio" -> PermissionRiskLevel.MEDIUM
                "alto" -> PermissionRiskLevel.HIGH
                else -> PermissionRiskLevel.OTHERS
            }

    }
}