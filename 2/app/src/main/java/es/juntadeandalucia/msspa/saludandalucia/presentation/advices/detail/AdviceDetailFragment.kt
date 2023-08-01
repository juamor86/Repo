package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.detail

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdvicesStatus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactTypesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.BaseAdviceFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.advice_button_item_view.accept_btn
import kotlinx.android.synthetic.main.advice_buttons_item_view.*
import kotlinx.android.synthetic.main.fragment_new_advice.*
import javax.inject.Inject

class AdviceDetailFragment : BaseAdviceFragment(), AdviceDetailContract.View {

    @Inject
    lateinit var presenter: AdviceDetailContract.Presenter
    private var adviceSucessSavedDialogShown: Boolean = false
    private var deleteConfirmationDialogShown: Boolean = false
    private var confirmationDelegatedDialogShown: Boolean = false

    override fun bindPresenter(): AdviceDetailContract.Presenter = presenter

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            presenter.checkChanges(in_my_app_sw.isChecked, by_mail_sw.isChecked, by_sms_sw.isChecked, advice_email_et.text.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        presenter.onViewCreated()
    }

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            presenter.onCreate(
                getString(Consts.ARG_ADVICE_NUHSA)!!,
                getParcelable<AdviceEntity>(Consts.ARG_ADVICES_SHARED)!!,
                getString(Consts.ARG_ADVICE_PHONE)
            )
        }
    }

    override fun configureButtons(status:String, isOwner:Boolean) {
        val childView = layoutInflater.inflate(R.layout.advice_buttons_item_view, view_ns, false )
        buttonPanel_cl.addView(childView)
        if(isOwner){
            showOwnerButtons()
        }else{
            when(status){
                AdvicesStatus.ACTIVE.name.lowercase()->{
                    showActiveButtons()
                }
                AdvicesStatus.REQUESTED.name.lowercase()->{
                    showRequestedButtons()
                }
                Consts.ADVICE_TYPE_OFF->{
                    showOffButtons()
                }
                else->{
                    showOwnerButtons()
                }
            }
        }

        accept_btn.setOnClickListener {
            presenter.onSaveButtonPressed(in_my_app_sw.isChecked, by_mail_sw.isChecked, by_sms_sw.isChecked, advice_email_et.text.toString())
        }

        refuse_btn.setOnClickListener {
            presenter.onRemoveButtonPressed()
        }
    }

    private fun showOffButtons() {
        accept_btn.apply {
            setText(R.string.accept)
            visibility = View.VISIBLE
        }

        refuse_btn.visibility = View.GONE
    }

    private fun showRequestedButtons() {
        accept_btn.apply {
            setText(R.string.accept)
            visibility = View.VISIBLE
        }

        refuse_btn.apply {
            setText(R.string.refuse)
            visibility = View.VISIBLE
        }
    }

    override fun showActiveButtons() {
        accept_btn.apply {
            setText(R.string.new_advice_save)
            visibility = View.VISIBLE
        }

        refuse_btn.apply {
            setText(R.string.refuse)
            visibility = View.VISIBLE
        }
    }

    override fun enableSaveButton() {
        accept_btn.isEnabled = true
    }

    override fun disableSaveButton() {
        accept_btn.isEnabled = false
    }

    private fun showOwnerButtons() {
        accept_btn.apply {
            setText(R.string.new_advice_save)
            visibility = View.VISIBLE
        }

        refuse_btn.apply {
            setText(R.string.avisas_delete_text_button)
            visibility = View.VISIBLE
        }
    }

    override fun setupAdvicesSwitches(advice: EntryAdviceEntity) {
        in_my_app_sw.isChecked = presenter.isContactUsed(advice, ContactTypesEntity.PHONE)
        by_sms_sw.isChecked = presenter.isContactUsed(advice, ContactTypesEntity.SMS)
        by_mail_sw.isChecked = presenter.isContactUsed(advice, ContactTypesEntity.EMAIL)

        if (by_mail_sw.isChecked) {
            advice_email_it.visibility = View.VISIBLE
            advice_email_et.setText(presenter.getEmailContact(advice.contact))
        } else {
            advice_email_it.visibility = View.GONE
        }
    }

    override fun setupRequestBy(adviceContact: String?) {
        sended_by_cl.visibility = View.GONE

        adviceContact?.let {
            sended_by_cl.visibility = View.VISIBLE
            Utils.getContactByPhoneNumber(requireContext(), adviceContact).let { name->
                request_sent_user_tv.text = name
            }
        }
    }

    override fun showOrHideNewShareContacts(visibility: Int) {
        contacts_receive_cl.visibility = View.GONE
    }

    override fun showDeleteConfirmation() {
        if (!deleteConfirmationDialogShown) {
            deleteConfirmationDialogShown = true
            showConfirmDialog(title = R.string.new_advice_are_you_sure_delete, onAccept = {
                deleteConfirmationDialogShown = false
                presenter.onDeleteClicked(
                    in_my_app_sw.isChecked,
                    by_mail_sw.isChecked,
                    by_sms_sw.isChecked,
                    advice_email_et.text.toString()
                )
            }, onCancel = {
                deleteConfirmationDialogShown = false
            })
        }
    }

    override fun navigateToAdviceList() {
        findNavController().navigate(R.id.action_detail_dest_to_advices_dest)
    }

    override fun fillSharedContactName(sharedContacts: MutableList<ValueReferenceEntity>) {
        sharedContacts.forEach { valueReferenceEntity ->
            Utils.getContactByPhoneNumber(requireContext(), valueReferenceEntity.display).let { name ->
                valueReferenceEntity.name = name
            }
        }
        presenter.loadSharedContactNameUpdated(sharedContacts)
    }

    override fun hideChannels() {
        how_to_receive_cl.visibility=View.GONE
    }

    override fun showChannels() {
        how_to_receive_cl.visibility=View.VISIBLE
    }

    override fun showEmailError(){
        showConfirmDialog(title = R.string.advice_email_invalid)
    }

    override fun showErrorNotExistAdvice() {
        showWarningDialog(
            title = R.string.new_advice_dialog_not_exist_adivce,
            onAccept = { navigateToAdviceList() }
        )
    }

    override fun showDialogAdviceNotSaved() {
        showConfirmDialog(title = R.string.new_advice_dialog_not_advice_saved, onAccept = {
            navigateToAdviceList()
        }, onCancel = {
        })
    }

    override fun showDialogAdviceSuccessSaved() {
        if (!adviceSucessSavedDialogShown) {
            adviceSucessSavedDialogShown = true
            showSuccessDialog(R.string.new_advice_dialog_advice_saved_success, onAccept = {
                adviceSucessSavedDialogShown = false
                navigateToAdviceList()
            })
        }
    }

    override fun showConfimationDelegatedSuccess() {
        showSuccessDialog(
            R.string.new_advice_dialog_advice_requested_saved_success,
            onAccept = {
                navigateToAdviceList()
            })
    }

    override fun showConfirmDelegated(){
        if (!confirmationDelegatedDialogShown) {
            confirmationDelegatedDialogShown = true
            showConfirmDialog(
                title = R.string.confirm_accept_advice_delegated,
                onAccept = {
                    confirmationDelegatedDialogShown = false
                    presenter.acceptedDelegatedClick()
                },
                onCancel = { confirmationDelegatedDialogShown = false })
        }
    }

    override fun onNavBackPressed() {
        presenter.checkChanges(
            in_my_app_sw.isChecked,
            by_mail_sw.isChecked,
            by_sms_sw.isChecked,
            advice_email_et.text.toString()
        )
    }
}
