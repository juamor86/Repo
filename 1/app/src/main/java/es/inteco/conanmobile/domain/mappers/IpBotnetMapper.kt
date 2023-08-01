package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.IpBotnetData
import es.inteco.conanmobile.domain.entities.IpBotnetEntity

/**
 * Ip botnet mapper
 *
 * @constructor Create empty Ip botnet mapper
 */
class IpBotnetMapper {
    companion object {
        fun convert(model: IpBotnetData) = with(model){
            IpBotnetEntity(
                ip = ip,
                error =  error,
                evidences = EvidenceEntityMapper.convert(evidences)
            )
        }
    }
}