package es.inteco.conanmobile.presentation.analysis.type

import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Analysis type contract
 *
 * @constructor Create empty Analysis type contract
 */
class AnalysisTypeContract {

    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Refill recicler view
         *
         * @param defaultAnalysisList
         * @param selectedAnalysis
         */
        fun refillRecyclerView(defaultAnalysisList : List<AnalysisEntity>, selectedAnalysis: AnalysisEntity)
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
         */
        fun onCreateView()

        /**
         * Save default analysis
         *
         * @param analysisType
         */
        fun saveDefaultAnalysis(analysisType: AnalysisEntity)
    }
}