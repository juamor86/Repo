package es.inteco.conanmobile.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.inteco.conanmobile.R
import es.inteco.conanmobile.presentation.base.adapter.BaseListAdapter
import timber.log.Timber

/**
 * Custom bottom sheet dialog
 *
 * @param T
 * @property adapter
 * @constructor Create empty Custom bottom sheet dialog
 */
class CustomBottomSheetDialog<T>(
    private val adapter: BaseListAdapter<T>
) : BottomSheetDialogFragment() {

    companion object {
        fun <T> newInstance(adapter: BaseListAdapter<T>): CustomBottomSheetDialog<T> {
            return CustomBottomSheetDialog(adapter)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.activity_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(adapter) {
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
