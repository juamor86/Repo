package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.onboarding

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.OnBoardingAvisasViewEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.onboarding.adapter.HubOnBoardingAvisasAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_covid_cert_on_boarding_dialog.on_boarding_next_bt
import kotlinx.android.synthetic.main.view_covid_cert_on_boarding_dialog.on_boarding_viewpager
import kotlinx.android.synthetic.main.view_covid_cert_on_boarding_dialog.slider_indicator

class AvisasHubOnboardingDialog(private val onClose: () -> Unit) :
    CustomFullScreenDialog(onDismiss = { onClose.invoke() }) {

    private lateinit var adapter: HubOnBoardingAvisasAdapter
    private var currentView: Int = 0

    override fun bindContentLayout(): Int = R.layout.view_covid_cert_on_boarding_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val array: ArrayList<OnBoardingAvisasViewEntity> = arrayListOf()
        array.addAll(OnBoardingAvisasViewEntity.values())
        adapter = HubOnBoardingAvisasAdapter()
        adapter.setItemsAndNotify(array)
        on_boarding_viewpager.adapter = adapter
        if (adapter.itemCount > 1) {
            slider_indicator.setViewPager2(on_boarding_viewpager)
        }else{
            slider_indicator.visibility = View.GONE
        }
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