package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_dynamic.*
import kotlinx.android.synthetic.main.view_dynamic_item.view.*

class DynamicFragment : BaseFragment(), DynamicContract.View {

    @Inject
    lateinit var presenter: DynamicContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_dynamic

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parameter = arguments?.get(Consts.PARAMETER_PARAM) as DynamicScreenEntity
        presenter.onViewCreated(parameter)
    }

    override fun buildScreen(screen: DynamicScreenEntity) {

        screen.background?.let { view_cl.setBackgroundColor(Color.parseColor(it)) }

        if (screen.header != null) {
            text_header_tv.visibility = View.GONE
            Picasso.get().load(screen.header).into(logo_header_iv)
            logo_header_iv.visibility = View.VISIBLE
        } else {
            logo_header_iv.visibility = View.GONE
            text_header_tv.text = screen.title!!.text
            text_header_tv.setTextColor(Color.parseColor(screen.title.color))
            text_header_tv.visibility = View.VISIBLE
        }

        for (child in screen.children) {
            val childView = layoutInflater.inflate(R.layout.view_dynamic_item, view_cl, false)

            childView.apply {
                section_tv.text = child.title.text

                if (child.icon.source.isNotEmpty()) {
                    icon_iv.visibility = View.VISIBLE
                    Picasso.get().load(child.icon.source).into(icon_iv)
                } else {
                    icon_iv.visibility = View.GONE
                }

                setOnClickListener {
                    presenter.onElementClicked(child)
                }
            }
            content_ll.addView(childView)
        }
    }

    override fun handleNavigation(navigation: NavigationEntity) {
        getNavigator().handleNavigation(navigation)
    }
}
