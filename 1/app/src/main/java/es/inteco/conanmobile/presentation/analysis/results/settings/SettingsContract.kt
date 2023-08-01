package es.inteco.conanmobile.presentation.analysis.results.settings

import android.content.Context
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.analysis.results.AnalysisResultView
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Settings contract
 *
 * @constructor Create empty Settings contract
 */
class SettingsContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Refill recycler view
         *
         * @param incidencesNamesList
         * @param attentionNamesList
         */
        fun refillRecyclerView(
            incidencesNamesList: List<ModuleResultEntity>,
            attentionNamesList: List<ModuleResultEntity>
        )

        /**
         * Hide ok screen
         *
         */
        fun hideOkScreen()

        /**
         * Show description
         *
         * @param moduleResultEntity
         */
        fun showDescription(moduleResultEntity: ModuleResultEntity)

        /**
         * Show warning intent whatsapp
         *
         */
        fun showWarningIntentWhatsapp()

        /**
         * Set title applications
         *
         */
        fun setTitleApplications()

        /**
         * Set toolbar title settings
         *
         */
        fun setToolbarTitleSettings()

        /**
         * Set title system
         *
         */
        fun setTitleSystem()

        /**
         * Navigate to analysis detail
         *
         * @param name
         * @param list
         */
        fun navigateToAnalysisDetail(name: String, list: MutableList<NetworkEntity>)
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter : BaseContract.Presenter {
        /**
         * On create view
         *
         * @param previous
         * @param result
         * @param type
         */
        fun onCreateView(previous: AnalysisResultEntity?, result: AnalysisResultEntity, type: ModuleEntity.AnalysisType)

        /**
         * Go to unknown resources device configuration
         *
         * @param ct
         */
        fun goToUnknownResourcesDeviceConfiguration(ct: Context)

        /**
         * On module clicked
         *
         * @param module
         */
        fun onModuleClicked(module: ModuleResultEntity)

        /**
         * On action module clicked
         *
         * @param module
         * @param view
         */
        fun onActionModuleClicked(
            module: ModuleResultEntity, view: AnalysisResultView
        )
    }
}