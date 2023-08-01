package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.dashboard

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class DynQuestDashboardPresenter(
    private val getFirstAccess: GetFirstAccessUseCase,
    private val setFirstAccess: SetFirstAccessUseCase
) : BasePresenter<DynQuestDashboardContract.View>(), DynQuestDashboardContract.Presenter {

    override fun onCreate() {
        if (getFirstAccess
                .param(Consts.PREF_FIRST_ACCESS_TO_DYN_QUEST_HUB)
                .execute()
        ) {
            view.showOnBoarding()
            saveFirstAccess()
        }
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.DYN_QUEST_LANDING

    private fun saveFirstAccess() {
        setFirstAccess
            .param(Consts.PREF_FIRST_ACCESS_TO_DYN_QUEST_HUB)
            .execute(
                onComplete = {},
                onError = { Timber.e(it) }
            )
    }

    override fun onElementClicked(element: DynamicElementEntity) {
        view.handleNavigation(element.navigation)
    }

    override fun onViewCreated(dynamicScreenEntity: DynamicScreenEntity) {
        view.buildScreen(dynamicScreenEntity)
    }
}