package es.juntadeandalucia.msspa.saludandalucia.presentation.wallet

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.onboarding.WalletOnBoardingDialog
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.BiometricUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.fragment_wallet.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.item_dyn_t.view.title_tv
import kotlinx.android.synthetic.main.view_dynamic_item.view.icon_iv
import kotlinx.android.synthetic.main.view_dynamic_item.view.section_tv
import javax.crypto.Cipher

class WalletFragment : BaseFragment(), WalletContract.View {

    @Inject
    lateinit var presenter: WalletContract.Presenter
    lateinit var icons: DynamicScreenEntity

    @set:Inject
    var cryptoManager: CrytographyManager? = null

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_wallet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wallet_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.show_on_boarding) {
            showOnBoardingDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showOnBoardingDialog(isFirstTime: Boolean) {
        val onBoarding = WalletOnBoardingDialog(onDismiss = {
            if (isFirstTime) presenter.onBoardingClosed()
        })

        activity?.supportFragmentManager?.apply {
            onBoarding.show(this, Consts.ON_BOARDING_DIALOG_TAG)
        }
    }

    override fun setupView() {
        icons = presenter.getDynamicIcons()
        text_header_tv.text = icons.title?.text ?: resources.getString(R.string.wallet_title)
    }

    override fun authenticationForDecrypt(
        onSuccess: (Cipher) -> Unit,
        onError: (String) -> Unit,
        unlockEntity: UnlockEntity?
    ) {
        cryptoManager?.promptForDecryption(
            onSuccess,
            onError,
            requireActivity(),
            Consts.IDENTIFIER,
            unlockEntity
        )
    }

    override fun animateView() {
        view_cl.visibility = View.VISIBLE
        animateViews(view_cl, animId = R.anim.slide_from_bottom)
    }

    override fun showDeniedAccessText() {
        empty_gp?.visibility = View.GONE
        denied_gp?.visibility = View.VISIBLE
    }

    override fun showCerts(certs: List<WalletEntity>, title: String) {
        val titleSection = layoutInflater.inflate(R.layout.view_dynamic_text, content_ll, false)
        titleSection.apply {
            title_tv.text = title
        }
        content_ll.addView(titleSection)
        for (cert in certs) {
            val childView = layoutInflater.inflate(R.layout.view_dynamic_item, content_ll, false)

            childView.apply {
                section_tv.text = resources.getString(
                    R.string.wallet_user_name,
                    cert.surname,
                    cert.name
                )
                setOnClickListener {
                    presenter.navigateToDetail(cert)
                }

                Picasso.get().load(Utils.getCurrentCertLogo(cert.type, icons)).into(icon_iv)

                content_ll.addView(childView)
            }
        }
    }

    override fun showNoCertsSave() {
        denied_gp.visibility = View.GONE
        empty_gp.visibility = View.VISIBLE
    }

    override fun hideDeniedAccess() {
        denied_gp.visibility = View.GONE
    }

    override fun navigateToDetail(cert: WalletEntity) {
        findNavController().navigate(R.id.action_wallet_dest_to_detail_dest, bundleOf(Consts.ARG_WALLET_CERT to cert))
    }

    override fun showDialogNotPhoneSecured() {
        showWarningDialog(title = R.string.dialog_not_phone_secured, onAccept = {})
    }

    override fun haveBiometricOrPin(): Boolean =
        BiometricUtils.isBiometricOrSecured(requireContext())
}