package es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.detail

import android.os.Bundle
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.fragment_wallet_detail.*
import javax.inject.Inject

class WalletDetailFragment : BaseFragment(), WalletDetailContract.View {

    @Inject
    lateinit var presenter: WalletDetailContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_wallet_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cert = arguments?.get(Consts.ARG_WALLET_CERT) as WalletEntity
        presenter.setupView(cert)
    }

    override fun showCert(cert: WalletEntity, title: String, idValue: String?, idType: String?) {
        covid_cert_title_tv.text = title
        with(cert) {
            name_value_tv.text = name
            surname_value_tv.text = surname
            qr_cert_iv.setImageBitmap(Utils.generateQR(requireContext(), qr))
        }

        if (idValue.isNullOrEmpty() || idType.isNullOrEmpty()) {
            hideExtraParameters()
        } else {
            idtype_value_tv.text = idType
            idValue_value_tv.text = idValue
        }
    }

    private fun hideExtraParameters() {
        extra_cert_fields_gr.visibility = View.GONE
    }
}