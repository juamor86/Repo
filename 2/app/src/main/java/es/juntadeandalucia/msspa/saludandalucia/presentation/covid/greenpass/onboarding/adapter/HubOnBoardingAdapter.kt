package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.onboarding.adapter

import android.text.method.LinkMovementMethod
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.OnBoardingViewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.adapter.BaseAdapter
import kotlinx.android.synthetic.main.view_item_on_boarding.view.*

class HubOnBoardingAdapter() :
    BaseAdapter<OnBoardingViewsEntity>({}) {

    override val itemRowResource: Int = R.layout.view_item_on_boarding

    override fun bind(
        itemView: View,
        item: OnBoardingViewsEntity,
        listener: (OnBoardingViewsEntity) -> Unit
    ) {
        with(itemView) {
            on_boarding_iv.setImageDrawable(resources.getDrawable(item!!.image, null))
            on_boarding_title_tv.text = resources.getString(item.title)
            // Use getText to format HTML tag if needed
            on_boarding_text_tv.text = resources.getText(item.text)
            on_boarding_text_tv.setMovementMethod(LinkMovementMethod.getInstance())
        }
    }
}
