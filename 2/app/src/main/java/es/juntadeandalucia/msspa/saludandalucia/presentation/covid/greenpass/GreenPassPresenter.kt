package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetUserReceiptsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class GreenPassPresenter(
    private val getFirstAccess: GetFirstAccessUseCase,
    private val setFirstAccess: SetFirstAccessUseCase,
    private val sessionBus: SessionBus,
    private val getUserReceiptsUseCase: GetUserReceiptsUseCase
) : BasePresenter<GreenPassContract.View>(), GreenPassContract.Presenter {

    private var destination: NavigationEntity? = null
    private var beneficiaries = mutableListOf<BeneficiaryEntity>()
    private var needLoadBeneficiaries = true

    override fun onCreate() {
        if (getFirstAccess
                .param(Consts.PREF_FIRST_ACCESS_TO_CERTIFICATE_HUB)
                .execute()
        ) {
            view.showOnBoarding()
            saveFirstAccess()
        }
    }

    private fun saveFirstAccess() {
        setFirstAccess
            .param(Consts.PREF_FIRST_ACCESS_TO_CERTIFICATE_HUB)
            .execute(
                onComplete = {},
                onError = { Timber.e(it) }
            )
    }

    override fun onParseScreenFailed() {
        view.showErrorDialogAndFinish()
    }

    override fun onViewCreated(screen: DynamicScreenEntity?) {
        if (screen != null) {
            view.buildScreen(screen)
        } else {
            onParseScreenFailed()
        }
    }

    override fun getScreenNameTracking(): String = Consts.Analytics.CERTIFICATE_LIST

    override fun onCertButtonClicked(dest: NavigationEntity) {
        destination = dest

        if (sessionBus.session.isUserAuthenticated()) {
            if (!dest.target.contains(Consts.VERIFICATION_CERT)) {
                checkForLoadBeneficiaries()
            } else {
                view.navigateToCert(dest)
            }

        } else {
            dest.navigateAfterLogin = false
            view.navigateToCert(dest)
        }
    }

    private fun checkForLoadBeneficiaries() {
        if (needLoadBeneficiaries) {
            getBeneficiaries()
        } else {
            checkBeneficiaryList()
        }
    }

    private fun getBeneficiaries() {
        getUserReceiptsUseCase.execute(
            onSuccess = { list ->
                needLoadBeneficiaries = false
                if (list.isNotEmpty()) {
                    beneficiaries = list.toMutableList()
                    checkBeneficiaryList()
                } else {
                    view.navigateToCert(destination!!)
                }
            },
            onError = {
                Timber.e(it)
                view.navigateToCert(destination!!)
            }
        )
    }

    private fun checkBeneficiaryList() {
        if (beneficiaries.isNotEmpty()) {
            with(sessionBus.session) {
                val beneficiaryList = beneficiaries.toMutableList()
                beneficiaryList.add(
                    0,
                    BeneficiaryEntity(
                        msspaAuthenticationEntity.msspaAuthenticationUser!!.nuhsa,
                        msspaAuthenticationEntity.msspaAuthenticationUser!!.prettyName,
                        msspaAuthenticationEntity.authorizationToken!!
                    )
                )
                view.showBeneficiaryList(beneficiaryList)
            }
        } else {
            destination?.let { dest ->
                view.navigateToCert(dest)
            }
        }
    }

    override fun onSelectBeneficiary(beneficiary: BeneficiaryEntity) {
        destination?.let { dest ->
            dest.bundle?.putParcelable(Consts.BUNDLE_CERT_BENEFICIARY, beneficiary)
            view.navigateToCert(dest)

        }
    }

    override fun unsubscribe() {
        setFirstAccess.clear()
    }
}
