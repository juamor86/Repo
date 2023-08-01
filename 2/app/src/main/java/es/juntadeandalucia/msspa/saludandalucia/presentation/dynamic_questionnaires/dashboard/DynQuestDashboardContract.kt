package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class DynQuestDashboardContract {

    interface View : BaseContract.View {
        fun showOnBoarding()
        fun handleNavigation(navigation: NavigationEntity)
        fun buildScreen(dynamicScreenEntity: DynamicScreenEntity)
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate()
        fun onElementClicked(element: DynamicElementEntity)
        fun onViewCreated(dynamicScreenEntity: DynamicScreenEntity)
    }

}