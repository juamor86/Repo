package es.inteco.conanmobile.presentation.legal

import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.base.BaseContract

/**
 * Legal contract
 *
 * @constructor Create empty Legal contract
 */
class LegalContract {
    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View : BaseContract.View {
        /**
         * Show legal text
         *
         * @param text
         */
        fun showLegalText(text:String)
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
         * @param messageEntity
         */
        fun onCreateView(messageEntity: MessageEntity)

    }
}