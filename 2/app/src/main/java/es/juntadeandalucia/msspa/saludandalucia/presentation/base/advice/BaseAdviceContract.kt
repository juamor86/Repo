package es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

open class BaseAdviceContract {

    interface View : BaseContract.View {
        fun setupAdviceTitle(title: String, description: String)
        fun loadContactsToRecyclerView(contacts: List<ValueReferenceEntity>)
        fun setupAdapter()
        fun setupContactsRecycler()
        fun showRemoveContactDialog(contact: ValueReferenceEntity, type: String)
        fun showOrHideEmailEditText(enable: Boolean)
        fun showValidEmail()
        fun showInvalidEmail()
        fun setupCommonListeners()
        fun openContactsSelector()
        fun showDialogPermissionContactsDeniedTwice()
        fun showErrorContactAlreadyAdded(phone: String)
        //TODO reset contacts
        //fun performExtractContact(numbersContact: List<ContactAdviceEntity>)
        //fun showImportNumbersContact(numbersContact: List<ContactAdviceEntity>)
        fun dismissDialogImportContact()
        fun showDialogContactSelf()
        fun showErrorPhoneNotValid()
    }

    interface Presenter : BaseContract.Presenter {
        fun saveArguments(
            nuhsa: String,
            adviceType: AdviceTypeResource? = null,
            entryAdvice: EntryAdviceEntity? = null,
            phoneNumber: String?,
            advice: AdviceEntity? = null
        )
        fun switchItemChanged(enabledEmail: Boolean)
        //fun extractContactFromIntentCursor(contentResolver: ContentResolver, uri: Uri)
        fun onAdviceItemClick(contact: ValueReferenceEntity, itemView: android.view.View)
        fun onAdviceItemRemoved(contact: ValueReferenceEntity)
        fun onRemoveButtonClick(context: Context, contact: ValueReferenceEntity)
        fun removeContact(contact: ValueReferenceEntity)
        fun checkEmailFormat(email: String)
        fun onRequestPermissionContactDenied()
        //fun buildExtractContact(numbersContact: List<ContactAdviceEntity>)
        //fun onNumberContactItemClick(numberContact: ContactAdviceEntity)
    }
}