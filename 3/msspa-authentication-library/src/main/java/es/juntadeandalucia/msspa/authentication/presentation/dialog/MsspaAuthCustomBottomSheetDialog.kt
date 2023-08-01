package es.juntadeandalucia.msspa.authentication.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseListAdapter
import es.juntadeandalucia.msspa.authentication.utils.Utils
import es.juntadeandalucia.msspa.authentication.utils.other.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.msspa_auth_view_custom_bottom_sheet_dialog.*
import timber.log.Timber

class MsspaAuthCustomBottomSheetDialog<T>(
    private val adapter: BaseListAdapter<T>
) : BottomSheetDialogFragment() {

    companion object {
        fun <T> newInstance(adapter: BaseListAdapter<T>): MsspaAuthCustomBottomSheetDialog<T> {
            return MsspaAuthCustomBottomSheetDialog(adapter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MSSPAAuthAppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.msspa_auth_view_custom_bottom_sheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireDialog().window?.let {
            Utils.secureAgainstScreenshots(it, (requireActivity() as BaseActivity).authConfig.environment)
        }
        with(adapter) {
            custom_bottom_dialog_rv.adapter = this
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(requireContext(), adapter))
            itemTouchHelper.attachToRecyclerView(custom_bottom_dialog_rv)
            notifyDataSetChanged()
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: IllegalStateException) {
            Timber.e(ignored)
        }
    }
}
