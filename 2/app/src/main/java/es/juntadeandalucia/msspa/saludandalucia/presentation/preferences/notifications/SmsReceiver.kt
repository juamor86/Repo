package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SmsBus
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.SMS_RECEIVER_CONTENT
import javax.inject.Inject

class SmsReceiver @Inject constructor(val smsBus: SmsBus) /*: BroadcastReceiver()*/ {

    /*override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null || intent.action == null) {
            return
        }
        if (intent.action != (Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            return
        }

        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val msgBody = validateMsg(smsMessages)
        if (msgBody.isNotEmpty()) {
            smsBus.smsReceived(msgBody)
        }
    }

    private fun validateMsg(smsMessages: Array<SmsMessage>?): String {
        var msgBody = ""
        smsMessages?.forEach { smsMessage ->
            if (smsMessage.displayMessageBody.contains(SMS_RECEIVER_CONTENT)) {
                msgBody = smsMessage.displayMessageBody
            }
        }
        return msgBody
    }*/
}