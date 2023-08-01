package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.reinforced

import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.HealthCardEntity
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.login.fragments.personaldata.LoginBaseFragment
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.Utils
import kotlinx.android.synthetic.main.fragment_login_reinforced.*
import kotlinx.android.synthetic.main.fragment_login_reinforced.birthday_it
import kotlinx.android.synthetic.main.fragment_login_reinforced.health_card_it
import kotlinx.android.synthetic.main.fragment_login_reinforced.identifier_it
import kotlinx.android.synthetic.main.fragment_login_reinforced.identifier_type_tv
import kotlinx.android.synthetic.main.fragment_login_reinforced.save_user_ch
import javax.inject.Inject

class LoginReinforcedFragment : LoginBaseFragment(), LoginReinforcedContract.View {
    @Inject
    lateinit var presenter: LoginReinforcedContract.Presenter
    override fun bindPresenter() = presenter

    //region INITIALIZATION
    override fun bindLayout() = R.layout.fragment_login_reinforced

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }


    override fun setupView() {
        super.setupView()
        initPhoneInput()
    }


    override fun showErrorPhoneNumber() {
        can_it.error = getString(R.string.msspa_auth_error_phone)
    }

    override fun showValidPhoneNumber() {
        can_it.isErrorEnabled = false
    }

    override fun navigateToSMS(
        authorizeEntity: AuthorizeEntity,
        MsspaAuthenticationUserEntity: MsspaAuthenticationUserEntity,
        saveUser: Boolean
    ) {
        val bundle = bundleOf(
            ApiConstants.Arguments.ARG_AUTHORIZE to authorizeEntity,
            ApiConstants.Arguments.ARG_QUIZ_AUTHORIZE to MsspaAuthenticationUserEntity,
            ApiConstants.Arguments.ARG_SAVE_USER to saveUser
        )

        findNavController().navigate(R.id.first_factor_reinforcement_dest_to_second_factor_dest, bundle)
    }



    private fun initPhoneInput() {
        can_it.editText?.apply {
            filters = arrayOf(Utils.whiteSpaceFilter)
            addTextChangedListener(afterTextChanged = {
                presenter.checkPhoneNumber(
                    text.toString()
                )
            })
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    presenter.validatePhoneNumber(
                        text.toString()
                    )
                    // hideKeyboard()
                }
            }
        }

        phone_help_iv.setOnClickListener{presenter.onPhoneHelpClicked()}
    }

    override fun onSendClicked() {
        disableLoginButton()
        presenter.onSendClicked(
            saveUser = save_user_ch.isChecked,
            nuss = Utils.validateHealthCard(HealthCardEntity.NUSS, health_card_it?.editText?.text.toString()),
            nuhsa = Utils.validateHealthCard(HealthCardEntity.NUHSA, health_card_it?.editText?.text.toString()),
            idType = identifier_type_tv.currentItem!!,
            identification = identifier_it.editText?.text.toString(),
            birthDate = birthday_it.editText?.text.toString(),
            phone = can_it.editText?.text.toString()
        )
    }

    override fun showPhoneHelp() {
        showWarning(R.string.login_phone_help, onAccept = {})
    }
    //endregion
}