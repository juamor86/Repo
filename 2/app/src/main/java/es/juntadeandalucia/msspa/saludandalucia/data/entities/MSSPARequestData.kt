package es.juntadeandalucia.msspa.saludandalucia.data.entities

import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPARequestData.MSSPAChannelData.Companion.TYPE_MESSAGE
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPARequestData.MSSPAContactData.Companion.SYSTEM_PHONE
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPARequestData.MSSPAContactData.Companion.USE_MOBILE
import es.juntadeandalucia.msspa.saludandalucia.data.entities.MSSPARequestData.SubscribeNotificationsRequestData.Companion.PHONE_NUMBER
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants

sealed class MSSPARequestData(private val resourceType: String) {

    companion object {
        private const val PARAMETER_RESOURCE_TYPE = "Parameters"
        private const val SUBSCRIPTION_RESOURCE_TYPE = "Subscription"
    }

    class VerificationCodeRequestData(parameterValue: String) : MSSPARequestData(PARAMETER_RESOURCE_TYPE) {

        private val parameter: List<MSSPAParameterData> = listOf(MSSPAParameterData(PHONE_NUMBER, parameterValue))
    }

    class SubscribeNotificationsRequestData(phone: String, firebaseToken: String) :
        MSSPARequestData(SUBSCRIPTION_RESOURCE_TYPE) {
        companion object {
            internal const val PHONE_NUMBER = "phone_number"
            private const val STATUS_REQUESTED = "requested"
            private const val REASON = "SaludAndalucia"
            private const val CRITERIA_APP_ID = "Communication?recipient.type=Device&recipient.identifier=id|"
        }

        private val status = STATUS_REQUESTED
        private val contact = listOf(MSSPAContactData(SYSTEM_PHONE, phone, USE_MOBILE))
        private val reason = REASON
        private val criteria = CRITERIA_APP_ID + ApiConstants.General.SALUD_ANDALUCIA_APP_KEY_IDENTIFICATION
        private val channel = MSSPAChannelData(firebaseToken, TYPE_MESSAGE)
    }

    private data class MSSPAParameterData(private val name: String, private val valueString: String)
    private data class MSSPAContactData(
        private val system: String,
        private val value: String,
        private val use: String
    ) {
        companion object {
            const val SYSTEM_PHONE = "phone"
            const val USE_MOBILE = "mobile"
        }
    }

    private data class MSSPAChannelData(private val id: String, private val type: String) {
        companion object {
            const val TYPE_MESSAGE = "message"
        }
    }
}
