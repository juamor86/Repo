package es.juntadeandalucia.msspa.authentication.utils

import android.graphics.Rect
import android.view.View
import androidx.core.widget.NestedScrollView

fun Any.toByteArray(): ByteArray? {
    return this as? ByteArray
}

fun NestedScrollView.isViewVisible(toCheckView: View): Boolean {
    val scrollBounds = Rect()
    getDrawingRect(scrollBounds)

    val top = toCheckView.y
    val bottom = top + toCheckView.height

    return (scrollBounds.top < top && scrollBounds.bottom >= bottom)
}
