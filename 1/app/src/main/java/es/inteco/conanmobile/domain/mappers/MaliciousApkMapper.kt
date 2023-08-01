package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.MaliciousApkData
import es.inteco.conanmobile.domain.entities.MaliciousApkEntity

/**
 * Malicious apk mapper
 *
 * @constructor Create empty Malicious apk mapper
 */
class MaliciousApkMapper {
    companion object{
        fun convert(model: MaliciousApkData) = with(model){
            MaliciousApkEntity(
                timestamp = timestamp,
                status = status,
                statusMessage = statusMessage,
                message = MaliciousApkMessageMapper.convert(message),
                path = path
            )
        }
    }
}