package es.inteco.conanmobile.presentation.dialog

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.inteco.conanmobile.R
import kotlin.math.roundToInt


/**
 * Custom full screen dialog
 *
 * @property onDismiss
 * @constructor Create empty Custom full screen dialog
 */
abstract class CustomFullScreenDialog(open val onDismiss: (() -> Any)? = null) :
    BottomSheetDialogFragment() {

    /**
     * Bind content layout
     *
     * @return
     */
    @LayoutRes
    abstract fun bindContentLayout(): Int

    /**
     * Bind data
     *
     */
    open fun bindData() {
        // Empty default implementation
    }

    /**
     * On show
     *
     */
    open fun onShow() {
        // Empty default implementation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = View.inflate(context, R.layout.view_custom_full_screen_bottom_sheet_dialog, null)
        val stub = view.findViewById(R.id.layout_stub) as ViewStub
        stub.layoutResource = bindContentLayout()
        stub.inflate()
        val linearLayout = view.findViewById<LinearLayout>(R.id.root_content)
        linearLayout.layoutParams = linearLayout.layoutParams.apply {
            height = getScreenHeight()
        }
        view.findViewById<ImageView>(R.id.close_iv).setOnClickListener {
            dialog!!.dismiss()
        }

        dialog!!.apply {
            setOnShowListener { dialog ->
                val d = dialog as BottomSheetDialog
                val bottomSheetInternal =
                    d.findViewById<View>(R.id.design_bottom_sheet)!!
                BottomSheetBehavior.from(bottomSheetInternal).state =
                    BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.from(bottomSheetInternal).skipCollapsed = true
                BottomSheetBehavior.from(bottomSheetInternal).isHideable = false
                onShow()
            }
            onDismiss?.let { onDismiss ->
                setOnDismissListener {
                    onDismiss()
                    onDismiss(this)
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        val statusBarHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24f,
            displayMetrics
        )
        return (displayMetrics.heightPixels - statusBarHeight).roundToInt()
    }
}
