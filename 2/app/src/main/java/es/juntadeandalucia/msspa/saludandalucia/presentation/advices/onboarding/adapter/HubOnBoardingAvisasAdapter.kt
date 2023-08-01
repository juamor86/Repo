package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.onboarding.adapter

import android.text.method.LinkMovementMethod
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.OnBoardingAvisasViewEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseAdapter
import kotlinx.android.synthetic.main.view_item_on_boarding.view.on_boarding_iv
import kotlinx.android.synthetic.main.view_item_on_boarding.view.on_boarding_text_tv
import kotlinx.android.synthetic.main.view_item_on_boarding.view.on_boarding_title_tv

class HubOnBoardingAvisasAdapter :
    BaseAdapter<OnBoardingAvisasViewEntity>({}) {

    override val itemRowResource: Int = R.layout.view_item_on_boarding

    override fun bind(
        itemView: View,
        item: OnBoardingAvisasViewEntity,
        listener: (OnBoardingAvisasViewEntity) -> Unit
    ) {
        with(itemView) {
            on_boarding_iv.setImageDrawable(resources.getDrawable(item!!.image, null))
            on_boarding_title_tv.text = resources.getString(item.title)
            on_boarding_text_tv.text = resources.getText(item.text)
            on_boarding_text_tv.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}