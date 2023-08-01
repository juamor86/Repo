package es.inteco.conanmobile.presentation.legal

import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.base.BasePresenter

/**
 * Legal presenter
 *
 * @constructor Create empty Legal presenter
 */
class LegalPresenter : BasePresenter<LegalContract.View>(), LegalContract.Presenter {

    override fun onCreateView(messageEntity: MessageEntity) {
        view.showLegalText(messageEntity.formattedTermsAndConditions)
    }
}