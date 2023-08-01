package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.onboarding

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.OnBoardingViewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass.onboarding.adapter.HubOnBoardingAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_covid_cert_on_boarding_dialog.*

class HubOnBoardingDialog : CustomFullScreenDialog() {

    private lateinit var adapter: HubOnBoardingAdapter
    private var currentView: Int = 0

    override fun bindContentLayout(): Int = R.layout.view_covid_cert_on_boarding_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val array: ArrayList<OnBoardingViewsEntity> = arrayListOf()
        array.addAll(OnBoardingViewsEntity.values())
        adapter = HubOnBoardingAdapter()
        adapter.setItemsAndNotify(array)
        on_boarding_viewpager.adapter = adapter
        slider_indicator.setViewPager2(on_boarding_viewpager)

        on_boarding_viewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == array.size - 1) {
                    on_boarding_next_bt.setText(R.string.on_boarding_exit)
                } else if (position < array.size - 1) {
                    on_boarding_next_bt.setText(R.string.on_boarding_next)
                }
            }
        })

        on_boarding_next_bt.setOnClickListener {
            with(on_boarding_viewpager) {
                if (currentItem == array.size - 1) {
                    dialog!!.dismiss()
                } else {
                    currentView = currentItem
                    setCurrentItem(++currentView, true)
                }
            }
        }
    }
}
