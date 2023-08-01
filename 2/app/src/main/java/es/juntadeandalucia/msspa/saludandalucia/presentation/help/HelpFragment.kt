package es.juntadeandalucia.msspa.saludandalucia.presentation.help

import androidx.fragment.app.Fragment
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class HelpFragment : BaseFragment(), HelpContract.View {

    @Inject
    lateinit var presenter: HelpContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_help

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }
}
