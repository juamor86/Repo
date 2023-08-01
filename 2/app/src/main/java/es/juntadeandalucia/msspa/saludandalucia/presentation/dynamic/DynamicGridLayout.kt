package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout

class DynamicGridLayout : ConstraintLayout, DynamicLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var container = 0

    override fun addChildren(v: View) {
        (getChildAt(container) as ViewGroup).addView(v)
        container++
    }
}
