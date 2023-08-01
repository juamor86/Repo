package es.juntadeandalucia.msspa.saludandalucia.data.entities

import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants

data class CommunicationData(
    val id: String,
    val resourceType: String = ApiConstants.NotificationReception.NOTIFICATION_COMMUNICATION_RESOURCETYPE,
    val status: String = ApiConstants.NotificationReception.NOTIFICATION_COMMUNICATON_STATUS
)
