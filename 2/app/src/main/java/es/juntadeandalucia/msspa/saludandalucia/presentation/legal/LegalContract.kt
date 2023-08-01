package es.juntadeandalucia.msspa.saludandalucia.presentation.legal

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class LegalContract {

    interface View : BaseContract.View {
        fun enableAcceptButton(checked: Boolean)
        fun navigateToHome()
        fun navigateToPermission(navigationEntity: NavigationEntity)
        fun hideToolbar()
        fun hideCheckAndButtom()
        fun closeView()
    }

    interface Presenter : BaseContract.Presenter {
        fun onUnderstoodClick()
        fun onSwitchChanged(checked: Boolean)
        fun onViewCreated(isFirstAccess: Boolean)
    }
}
