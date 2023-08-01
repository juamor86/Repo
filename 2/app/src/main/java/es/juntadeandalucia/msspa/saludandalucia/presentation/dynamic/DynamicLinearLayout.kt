package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes

class DynamicLinearLayout : LinearLayout, DynamicLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun addChildren(v: View) {
        addView(v)
    }
}
