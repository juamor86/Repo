package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceContactEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactTypesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.BaseAdviceContract

class AdviceDetailContract {

    interface View : BaseAdviceContract.View {
        fun configureButtons(status:String, isOwner:Boolean)
        fun setupAdvicesSwitches(advice: EntryAdviceEntity)
        fun setupRequestBy(adviceContact: String?)
        fun showOrHideNewShareContacts(visibility: Int)
        fun showDeleteConfirmation()
        fun navigateToAdviceList()
        fun fillSharedContactName(sharedContacts: MutableList<ValueReferenceEntity>)
        fun hideChannels()
        fun showChannels()
        fun showEmailError()
        fun showErrorNotExistAdvice()
        fun showDialogAdviceNotSaved()
        fun showConfimationDelegatedSuccess()
        fun showDialogAdviceSuccessSaved()
        fun showConfirmDelegated()
        fun onNavBackPressed()
        fun showActiveButtons()
        fun enableSaveButton()
        fun disableSaveButton()
    }

    interface Presenter : BaseAdviceContract.Presenter {
        fun onCreate(
            nuhsa: String,
            advice: AdviceEntity,
            phoneNumber: String?
        )

        fun onSaveButtonPressed(
            appSwitch: Boolean,
            emailSwitch: Boolean,
            smsSwitch: Boolean,
            email: String
        )

        fun onRemoveButtonPressed()
        fun isContactUsed(entry: EntryAdviceEntity, type: ContactTypesEntity): Boolean
        fun getEmailContact(contacts: List<AdviceContactEntity>?): String
        fun loadSharedContactNameUpdated(sharedContacts: MutableList<ValueReferenceEntity>)
        fun checkChanges(appSwitch: Boolean,
                         emailSwitch: Boolean,
                         smsSwitch: Boolean,
                         email: String)
        fun onDeleteClicked(appSwitch: Boolean,
                            emailSwitch: Boolean,
                            smsSwitch: Boolean,
                            email: String)
        fun acceptedDelegatedClick()
    }
}