package es.inteco.conanmobile.presentation.help

import es.inteco.conanmobile.presentation.base.BasePresenter

/**
 * Help presenter
 *
 * @constructor Create empty Help presenter
 */
class HelpPresenter : BasePresenter<HelpContract.View>(), HelpContract.Presenter {

    var currentPage = 0

    override fun onCreate() {
        view.initView()
    }

    override fun onViewCreated() {
        view.initButtons()
    }

    override fun onStart() {
        view.openPdfFromRaw(currentPage)
    }

    override fun onStop() {
        view.closeRenderer()
    }

    override fun onPreviousPageClicked() {
        view.openPdfFromRaw(--currentPage)
    }

    override fun onNextPageClicked() {
        view.openPdfFromRaw(++currentPage)
    }

    override fun onFileError() {
        view.showNoPDFDialog()
    }
}