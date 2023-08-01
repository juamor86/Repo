package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.nonuhsa

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBaseFragment
import kotlinx.android.synthetic.main.fragment_login_base.*
import kotlinx.android.synthetic.main.fragment_login_no_nuhsa.*
import kotlinx.android.synthetic.main.fragment_login_no_nuhsa.birthday_it
import kotlinx.android.synthetic.main.fragment_login_no_nuhsa.identifier_it
import kotlinx.android.synthetic.main.fragment_login_no_nuhsa.identifier_type_tv
import kotlinx.android.synthetic.main.fragment_login_no_nuhsa.login_bt
import javax.inject.Inject

class LoginNoNuhsaFragment : LoginBaseFragment(), LoginNoNuhsaContract.View {

    // TODO Improve this solution, this class should have no logic about scroll & save/get users from LoginBaseFragment

    @Inject
    lateinit var presenter: LoginNoNuhsaContract.Presenter
    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_login_no_nuhsa

    override fun injectComponent() {
        DaggerFragmentComponent
                .builder()
                .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
                .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
                .fragmentModule(FragmentModule())
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onSendClicked() {
        presenter.onSendClicked(
            saveUser = false,
            nuss = null,
            nuhsa = null,
            idType = identifier_type_tv.currentItem!!,
            identification = identifier_it.editText?.text.toString(),
            birthDate = birthday_it.editText?.text.toString()
        )
    }
}