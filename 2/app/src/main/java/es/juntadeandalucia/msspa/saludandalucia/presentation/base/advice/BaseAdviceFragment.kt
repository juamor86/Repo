package es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create.AdviceCreateFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail.AdviceDetailFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.adapter.ContactsAdviceAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.dialog.CustomContactAdviceDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.adapter.BaseContactsAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.fragment_new_advice.*

abstract class BaseAdviceFragment : BaseFragment(), BaseAdviceContract.View {

    private val presenter by lazy { bindPresenter() }
    abstract override fun bindPresenter(): BaseAdviceContract.Presenter
    override fun bindLayout() = R.layout.fragment_new_advice
    protected lateinit var contactsAdapter: BaseContactsAdapter
    private var actualFragment: Fragment? = null
    private lateinit var contactAdviceDialog: CustomContactAdviceDialog<ContactAdviceEntity>

    override fun setupAdviceTitle(title: String, description: String) {
        new_advice_title_tv.text = title
        new_advice_subtitle_tv.text = description
    }

    override fun setupCommonListeners() {
        new_advice_share_bt.setOnClickListener {
            actualFragment = it.findFragment()
            //TODO reset contacts
            //checkContactsPermissions()
        }

        by_mail_sw.setOnCheckedChangeListener { _, isChecked ->
            presenter.switchItemChanged(isChecked)
        }

        advice_email_it.editText?.apply {
            addTextChangedListener(afterTextChanged = {
                presenter.checkEmailFormat(text.toString())
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.checkEmailFormat(text.toString())
                    hideKeyboard()
                }
            }
        }
    }

    override fun loadContactsToRecyclerView(contacts: List<ValueReferenceEntity>) {
        if (contacts.isEmpty()) {
            other_persons_recive_tv.text =
                resources.getString(R.string.new_advice_want_other_persons_receive)
            can_share_with_others_tv.visibility = View.VISIBLE
            contacts_rv.visibility = View.GONE
        } else {
            other_persons_recive_tv.text =
                resources.getString(R.string.new_advice_has_shared_this_advice)
            can_share_with_others_tv.visibility = View.GONE
            contacts_rv.visibility = View.VISIBLE
            contactsAdapter.setItemsAndNotify(contacts)
        }
    }

    override fun setupAdapter() {
        this.contactsAdapter = BaseContactsAdapter(
            presenter::onAdviceItemClick,
            presenter::onAdviceItemRemoved,
            presenter::onRemoveButtonClick
        )
    }

    override fun setupContactsRecycler() {
        contacts_rv.adapter = contactsAdapter
    }

    override fun showRemoveContactDialog(contact: ValueReferenceEntity, type: String) {
        val message = getString(R.string.new_advice_want_stop_sharing).replace("%0", type)
            .replace("%1", contact.display)
        showConfirmDialog(title = message, onAccept = {
            presenter.removeContact(contact)
        }, onCancel = {})
    }

    override fun showErrorPhoneNotValid() {
        showDialog(
            icon = R.drawable.ic_error,
            title = R.string.advices_number_not_valid,
            onAccept = {},
            type = CustomDialog.TypeDialog.ERROR
        )
    }

    override fun showErrorContactAlreadyAdded(phone: String) {
        showConfirmDialog(
            title = getString(R.string.error_duplicated_contact),
            onAccept = { }
        )
    }

    override fun showDialogContactSelf() {
        showConfirmDialog(
            title = getString(R.string.dialog_import_contacts_self),
            onAccept = { }
        )
    }
//TODO reset contacts
    /*override fun showImportNumbersContact(numbersContact: List<ContactAdviceEntity>) {
        val adapter = ContactsAdviceAdapter(
            presenter::onNumberContactItemClick
        ).apply {
            setItemsAndNotify(numbersContact)
        }
        contactAdviceDialog = CustomContactAdviceDialog.newInstance(adapter = adapter)
        contactAdviceDialog.show(requireActivity().supportFragmentManager, "")
    }*/

    override fun dismissDialogImportContact() {
        contactAdviceDialog.dismiss()
    }

    override fun showOrHideEmailEditText(enable: Boolean) {
        if (enable) advice_email_it.visibility = View.VISIBLE else advice_email_it.visibility =
            View.GONE
    }

    override fun showValidEmail() {
        advice_email_it.isErrorEnabled = false
    }

    override fun showInvalidEmail() {
        advice_email_it.error = getString(R.string.new_advice_enter_valid_mail)
    }
    //TODO reset contacts
    override fun openContactsSelector() {
       /* val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        resultLauncher.launch(intent)*/
    }

    /*override fun performExtractContact(numbersContact: List<ContactAdviceEntity>) {
        presenter.buildExtractContact(numbersContact)
    }*/

    /*private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { contactUri ->
                    context?.contentResolver?.let { contentResolver ->
                        presenter.extractContactFromIntentCursor(contentResolver, contactUri)
                    }
                }
            }
        }*/

    /*fun checkContactsPermissions(){
        Manifest.permission.READ_CONTACTS.let { contactPermission ->
            when {
                PermissionsUtil.checkIsContactPermissionGranted(requireContext()) ->
                    openContactsSelector()
                shouldShowRequestPermissionRationale(contactPermission) ->
                    permissionsLauncher.launch(contactPermission)
                else ->
                    presenter.onRequestPermissionContactDenied()
            }
        }
    }*/

    /*private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openContactsSelector()
            } else {
                presenter.onRequestPermissionContactDenied()
            }
        }*/

    override fun showDialogPermissionContactsDeniedTwice() {
        showWarningDialog (title = R.string.confirm_permission_contact_denied_twice, onAccept = {
            navigateToConfiguration()
        }, onCancel = {})
    }

    private fun navigateToConfiguration() {
        when (actualFragment) {
            is AdviceDetailFragment -> {
                findNavController().navigate(R.id.action_detail_dest_to_configuration_dest)
            }
            is AdviceCreateFragment -> {
                findNavController().navigate(R.id.action_new_dest_to_configuration_dest)
            }
        }
    }
}