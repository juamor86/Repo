package es.juntadeandalucia.msspa.saludandalucia.presentation.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseListAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.other.SwipeToDeleteCallback
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.view_custom_bottom_sheet_dialog.*
import timber.log.Timber

class CustomBottomSheetDialog<T>(
    private val adapter: BaseListAdapter<T>
) : BottomSheetDialogFragment() {

    companion object {
        fun <T> newInstance(adapter: BaseListAdapter<T>): CustomBottomSheetDialog<T> {
            return CustomBottomSheetDialog(adapter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.view_custom_bottom_sheet_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireDialog().window?.let { Utils.secureAgainstScreenshots(it) }
        with(adapter) {
            custom_bottom_dialog_rv.adapter = this
            val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(requireContext(), adapter))
            itemTouchHelper.attachToRecyclerView(custom_bottom_dialog_rv)
            notifyDataSetChanged()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(this.dialog)
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setWhiteNavigationBar(@NonNull dialog: Dialog?) {
        dialog?.window?.let { window ->
            val metrics = DisplayMetrics()
            val dimDrawable = GradientDrawable()
            val navigationBarDrawable = GradientDrawable()

            window.getWindowManager().getDefaultDisplay().getMetrics(metrics)
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Color.WHITE)

            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)
            val windowBackground = LayerDrawable(layers)

            windowBackground.setLayerInsetTop(1, metrics.heightPixels)
            window.setBackgroundDrawable(windowBackground)
        }
    }
}
