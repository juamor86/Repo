package es.juntadeandalucia.msspa.saludandalucia.presentation.wallet

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.UnlockEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetCovidCertificatesUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetSharedDynamicIconsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class WalletPresenter(
    private val getDynamicIcons: GetSharedDynamicIconsUseCase,
    private val getCovidCertificatesUseCase: GetCovidCertificatesUseCase,
    private val setFirstAccessWalletUseCase: SetFirstAccessUseCase,
    private val getFirstAccessWalletUseCase: GetFirstAccessUseCase
) : BasePresenter<WalletContract.View>(), WalletContract.Presenter {

    private lateinit var dynamicScreen: DynamicScreenEntity
    private var canAccess = false
    private var isFirstAccess = false

    override fun getScreenNameTracking(): String = Consts.Analytics.WALLET_SCREEN_ACCESS

    override fun onCreate() {
        isFirstAccess = getFirstAccessWalletUseCase.param(Consts.PREF_FIRST_LOAD_WALLET).execute()
        if (isFirstAccess) {
            view.showOnBoardingDialog(true)
            setFirstAccess()
        } else {
            requestFingerprint()
        }

        dynamicScreen = getDynamicIcons.param(Consts.ARG_DYNAMIC_ICONS).execute()
    }

    private fun requestFingerprint() {
        view.apply {
            if(haveBiometricOrPin()){
                authenticationForDecrypt(
                    onSuccess = { _ ->
                        allowAccess()
                    },
                    onError = {
                        deniedAccess()
                    },
                    UnlockEntity.UNLOCK
                )
            }else{
                deniedAccess()
                showDialogNotPhoneSecured()
            }
        }
    }

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.apply {
            setupView()
            animateView()
        }
        if (canAccess) loadCerts()
    }

    private fun setFirstAccess() {
        setFirstAccessWalletUseCase.param(Consts.PREF_FIRST_LOAD_WALLET).execute(
            onComplete = { Timber.i("First access save successfully") },
            onError = { Timber.e(it) }
        )
    }

    override fun allowAccess() {
        loadCerts()
        canAccess = true
    }

    override fun deniedAccess() {
        view.showDeniedAccessText()
    }

    private fun loadCerts() {
        getCovidCertificatesUseCase.execute(
            onSuccess = { certList ->
                if (certList.isNotEmpty()) {
                    view.hideDeniedAccess()
                    filterTypes(certList)
                } else {
                    view.showNoCertsSave()
                }
            },
            onError = {
                view.showErrorDialog()
                Timber.e(it)
            }
        )
    }

    override fun getDynamicIcons(): DynamicScreenEntity =
        dynamicScreen

    override fun navigateToDetail(cert: WalletEntity) {
        view.navigateToDetail(cert)
    }

    private fun filterTypes(certsList: List<WalletEntity>) {
        for (child in dynamicScreen.children) {
            val filterList = certsList.filter { child.navigation.target == it.type.type }
            if (filterList.isEmpty()) continue
            view.showCerts(filterList.sortedBy { it.surname }, child.title.text)
        }
    }

    override fun onBoardingClosed() {
        requestFingerprint()
    }
}