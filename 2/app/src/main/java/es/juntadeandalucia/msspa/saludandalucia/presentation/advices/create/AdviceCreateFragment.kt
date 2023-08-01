package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.create

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ContactTypesEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.ValueReferenceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.advice.BaseAdviceFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.advice_button_item_view.*
import kotlinx.android.synthetic.main.fragment_new_advice.*
import javax.inject.Inject

class AdviceCreateFragment : BaseAdviceFragment(), AdviceCreateContract.View {

    @Inject
    lateinit var presenter: AdviceCreateContract.Presenter

    override fun bindPresenter(): AdviceCreateContract.Presenter = presenter

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
                getParcelable(Consts.ARG_ADVICE_TYPE)!!,
                getString(Consts.ARG_ADVICE_PHONE)
            )
        }
    }

    override fun setupRequestBy(adviceContact: String?) {
        sended_by_cl.visibility = GONE

        adviceContact?.let {
            sended_by_cl.visibility = VISIBLE
            request_sent_user_tv.text = adviceContact
        }
    }

    override fun setupAdvicesSwitches(advice: EntryAdviceEntity) {
        in_my_app_sw.isChecked = presenter.isContactUsed(advice, ContactTypesEntity.PHONE)
        by_sms_sw.isChecked = presenter.isContactUsed(advice, ContactTypesEntity.SMS)
        by_mail_sw.isChecked = presenter.isContactUsed(advice, ContactTypesEntity.EMAIL)

        if (by_mail_sw.isChecked) {
            advice_email_it.visibility = VISIBLE
            advice_email_et.setText(presenter.getEmailContact(advice.contact))
        } else {
            advice_email_it.visibility = GONE
        }
    }

    override fun showOrHideNewShareContacts(visibility: Int) {
        contacts_receive_cl.visibility = GONE
    }

    override fun configureButtons() {
        val childView = layoutInflater.inflate(R.layout.advice_button_item_view, view_ns, false)

        buttonPanel_cl.addView(childView)
    }

    override fun setupContactsRecycler() {
        contacts_rv.adapter = contactsAdapter
    }

    override fun setupButtonsListeners() {
        accept_btn.setOnClickListener {
            presenter.onCreateButtonPressed(
                in_my_app_sw.isChecked,
                by_mail_sw.isChecked,
                by_sms_sw.isChecked,
                advice_email_et.text.toString()
            )
        }

        in_my_app_sw.isChecked = true
        by_sms_sw.isChecked = false
        by_mail_sw.isChecked = false

    }

    override fun navigateToAdviceList() {
        findNavController().navigate(R.id.action_new_dest_to_advices_dest)
    }

    override fun showDialogAdviceSuccessSaved() {
        showSuccessDialog(R.string.new_advice_dialog_advice_saved_success, onAccept = {
            navigateToAdviceList()
        })
    }

    override fun enableSaveButton() {
        accept_btn.isEnabled = true
    }

    override fun disableSaveButton() {
        accept_btn.isEnabled = false
    }

    override fun showEmailError(){
        showConfirmDialog(title = R.string.advice_email_invalid)
    }
    //endregion
}