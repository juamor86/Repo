package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactTypesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.BaseAdviceContract

class AdviceCreateContract {

    interface View : BaseAdviceContract.View {
        fun setupRequestBy(adviceContact: String?)
        fun setupAdvicesSwitches(advice: EntryAdviceEntity)
        fun showOrHideNewShareContacts(visibility: Int)
        fun configureButtons()
        fun setupButtonsListeners()
        fun navigateToAdviceList()
        fun showDialogAdviceSuccessSaved()
        fun enableSaveButton()
        fun disableSaveButton()
        fun showEmailError()
    }

    interface Presenter : BaseAdviceContract.Presenter {
        fun onCreate(
            nuhsa: String,
            adviceTypeResource: AdviceTypeResource,
            phoneNumber: String?
        )
        fun onCreateButtonPressed(
            appSwitch: Boolean,
            emailSwitch: Boolean,
            smsSwitch: Boolean,
            email: String
        )
        fun isContactUsed(entry: EntryAdviceEntity, type: ContactTypesEntity): Boolean
        fun getEmailContact(contacts: List<AdviceContactEntity>?): String
    }
}