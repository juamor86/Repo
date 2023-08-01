package es.juntadeandalucia.msspa.authentication.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.utils.Utils
import kotlinx.android.synthetic.main.view_qr_help.*
import timber.log.Timber

class CustomLayoutBottomSheetDialog(
    private val layout: Int,
    private val environment: MsspaAuthenticationConfig.Environment = MsspaAuthenticationConfig.Environment.PRODUCTION
) : BottomSheetDialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MSSPAAuthAppBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireDialog().window?.let { Utils.secureAgainstScreenshots(it, environment) }
        setContentCustomDialogQrTv()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layout, container, false)

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().add(this, tag).commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {
            Timber.e(ignored)
        }
    }

    fun setContentCustomDialogQrTv(){
        custom_bottom_dialog_qr_tv?.let {
            val content =
                if (environment == MsspaAuthenticationConfig.Environment.PRODUCTION) MsspaAuthConsts.URL_WEB_AUTENTIFICATION_QR_PRO else MsspaAuthConsts.URL_WEB_AUTENTIFICATION_QR_PRE
            it.text = getString(R.string.login_qr_help, content)
        }
    }
}
