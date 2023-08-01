package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.permissions

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class PermissionsContract {

    interface View : BaseContract.View {
        fun buildScreen(dynamicScreenEntity: DynamicScreenEntity?)
        fun enableAcceptButton(checked: Boolean)
        fun navigateToHome()
        fun hideToolbar()
        fun setupViews()
        fun hideCheckAndButtom()
        fun displayToolbarOrNo(activated: Boolean)
        fun doBackOrNo(activated: Boolean)
        fun closeView()
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(dynamicScreenEntity: DynamicScreenEntity?)
        fun onUnderstoodClick()
        fun onSwitchChanged(checked: Boolean)
        fun isActivatedPermission()
        fun onBackPressedEvent()
    }
}
