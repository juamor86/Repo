package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.basic

import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBaseFragment
import javax.inject.Inject

class LoginBasicFragment : LoginBaseFragment(), LoginBasicContract.View {

    @Inject
    lateinit var presenter: LoginBasicContract.Presenter
    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_login_base

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }
    //endregion
}