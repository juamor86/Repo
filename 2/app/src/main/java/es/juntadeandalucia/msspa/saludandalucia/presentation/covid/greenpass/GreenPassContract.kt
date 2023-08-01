package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.greenpass

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class GreenPassContract {

    interface View : BaseContract.View {
        fun showOnBoarding()
        fun buildScreen(screen: DynamicScreenEntity)
        fun navigateToCert(dest: NavigationEntity)
        fun showBeneficiaryList(
            beneficiaryList: MutableList<BeneficiaryEntity>
        )
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate()
        fun onParseScreenFailed()
        fun onViewCreated(screen: DynamicScreenEntity?)
        fun onCertButtonClicked(dest: NavigationEntity)
        fun onSelectBeneficiary(beneficiary: BeneficiaryEntity)
    }
}
