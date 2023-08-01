package es.juntadeandalucia.msspa.saludandalucia.presentation.main.dialog

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicAreaEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicUIHelper
import kotlinx.android.synthetic.main.item_dyn_fab.view.title_tv
import kotlinx.android.synthetic.main.view_fab_dialog.view.*

class FabDialog(
    context: Context,
    area: DynamicAreaEntity,
    clickListener: (NavigationEntity) -> Unit
) : BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme) {

    init {
        val view = View.inflate(context, R.layout.view_fab_dialog, null)
        view.title_tv.text = area.area_desc
        DynamicUIHelper.getFabViews(
            context,
            view.container_gl,
            area.children[0].children,
            clickListener
        )

        setOnShowListener { dialog ->
            val bottomSheetInternal = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheetInternal?.let {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        setContentView(view)
    }
}
