package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.fragment

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class DynamicContract {

    interface View : BaseContract.View {
        fun buildScreen(screen: DynamicScreenEntity)
        fun handleNavigation(navigation: NavigationEntity)
    }

    interface Presenter : BaseContract.Presenter {
        fun onViewCreated(parameter: DynamicScreenEntity)
        fun onElementClicked(element: DynamicElementEntity)
    }
}
