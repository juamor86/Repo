package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.login

import android.content.Context
import android.graphics.Color
import android.nfc.NfcManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import de.tsenger.androsmex.data.CANSpecDO
import es.gob.fnmt.dniedroid.gui.PasswordUI
import es.gob.fnmt.dniedroid.gui.fragment.NFCCommunicationFragment
import es.gob.fnmt.dniedroid.nfc.NFCCommReaderFragment
import es.gob.fnmt.dniedroid.nfc.NFCCommReaderFragment.NFCCommReaderFragmentListener.NFC_callback_notify
import es.gob.jmulticard.card.baseCard.mrtd.MrtdCard
import es.gob.jmulticard.jse.provider.DnieProvider
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.presentation.dialog.CustomLayoutBottomSheetDialog
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.dni.DniUtil
import es.juntadeandalucia.msspa.authentication.utils.Utils
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_login_dnie.*
import kotlinx.android.synthetic.main.fragment_phone_validation.continue_bt
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.inject.Inject


class LoginDnieFragment : BaseFragment(), LoginDnieContract.View,
    NFCCommReaderFragment.NFCCommReaderFragmentListener {
    private var finishReading: Boolean = false

    @Inject
    lateinit var presenter: LoginDnieContract.Presenter
    override fun bindPresenter(): BaseContract.Presenter = presenter
    override fun bindLayout() = R.layout.fragment_login_dnie

    private lateinit var readerFragment: NFCCommunicationFragment

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun checkPhoneIsNFCCompatible(): Boolean {
        val manager = requireContext().getSystemService(Context.NFC_SERVICE) as NfcManager
        val adapter = manager.defaultAdapter
        return adapter != null && adapter.isEnabled
    }

    override fun showAlertNotNFCCompatible() {
        showWarning(R.string.not_nfc_supported, onAccept = { findNavController().popBackStack() })
    }

    override fun setupView() {
        hideLoading()
        dnie_help_iv.setOnClickListener { showDnieHelp() }
        add_can_help_iv.setOnClickListener { showCanHelp() }
        continue_bt.setOnClickListener { presenter.onContinueClicked() }
        can_it.editText?.filters = arrayOf(Utils.whiteSpaceFilter)
    }

    private fun showDnieHelp() {
        val environment = (requireActivity() as BaseActivity).authConfig.environment
        val dialog = CustomLayoutBottomSheetDialog(R.layout.view_dnie_help, environment)
        activity?.supportFragmentManager?.apply {
            dialog.show(this, "DNI_HELP_DIALOG")
        }

    }

    private fun showCanHelp() {
        val environment = (requireActivity() as BaseActivity).authConfig.environment
        val dialog = CustomLayoutBottomSheetDialog(R.layout.view_add_can_help, environment)
        activity?.supportFragmentManager?.apply {
            dialog.show(this, "ADD_CAN_HELP_DIALOG")
        }
    }


    override fun navigateToReadDni() {
        hideKeyboard()
        PasswordUI.setPasswordDialog(DNIEPinDialog(requireContext(), false))
        readerFragment = NFCCommunicationFragment()

        val bundle =
            bundleOf(NFCCommReaderFragment.CAN_ARGUMENT_KEY_STRING to can_et.text.toString())
        readerFragment.arguments = bundle
        readerFragment.setTextColor(Color.BLACK)
        readerFragment.setReaderListener(this)

        val transaction: FragmentTransaction =
            nav_host_fragment.parentFragmentManager.beginTransaction()
        transaction.add(R.id.nav_host_fragment, readerFragment)
        transaction.hide(this)
        transaction.addToBackStack("reader")
        transaction.commit()
    }

    override fun getCanToStore(keystore: KeyStore?, can: String?, p2: MrtdCard?): CANSpecDO {
        val certificate = keystore?.getCertificate(DnieProvider.AUTH_CERT_ALIAS) as X509Certificate
        presenter.onDniReaded(keystore, requireActivity())
        finishReading = true
        return CANSpecDO(can, DniUtil.getCN(certificate), DniUtil.getNIF(certificate))
    }

    override fun doNotify(
        notification: NFC_callback_notify?,
        msg: String?
    ) {
        val message = msg
        if(finishReading)
            return

        when (notification) {
            NFC_callback_notify.NFC_TASK_INIT -> {
                readerFragment.updateInfo(
                    notification,
                    getString(R.string.dnie_comm),
                    getString(R.string.dnie_safe_channel)
                )
            }
            NFC_callback_notify.NFC_TASK_UPDATE -> readerFragment.updateInfo(
                notification,
                getString(R.string.dnie_comm),
                getString(R.string.dnie_getting_info)
            )
            NFC_callback_notify.NFC_TASK_DONE -> {
                readerFragment.updateInfo(
                    notification,
                    getString(R.string.dnie_title),
                    getString(R.string.dnie_start_connect)
                )
            }
            NFC_callback_notify.FINAL_TASK_DONE -> {
            }
            NFC_callback_notify.ERROR -> if (msg.equals(
                    NFCCommReaderFragment.ERROR_PIN_LOCKED,
                    ignoreCase = true
                )
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.lib_dni_password_error_pin_locked),
                    Toast.LENGTH_LONG
                ).show()
                val transaction: FragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                transaction.remove(readerFragment)
                transaction.commit()
                return
            } else {
                readerFragment.updateInfo(notification, "Error en comunicación", message)
                if (msg != null) {
                    if (msg.contains("CAN incorrecto")) {
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            }
            else -> readerFragment.updateInfo(
                notification, getString(R.string.dnie_comm),
                message
            )
        }
    }

    override fun authenticateInProgress() {
        readerFragment.updateInfo(NFC_callback_notify.FINAL_TASK_DONE, getString(R.string.dnie_connecting))
    }


}