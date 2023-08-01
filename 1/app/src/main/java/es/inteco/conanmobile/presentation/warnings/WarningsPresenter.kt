package es.inteco.conanmobile.presentation.warnings

import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.domain.usecases.GetWarningsUseCase
import es.inteco.conanmobile.presentation.base.BasePresenter
import timber.log.Timber

class WarningsPresenter(
    private val getWarningsUseCase: SingleUseCase<Void, List<WarningEntity>>
) : BasePresenter<WarningsContract.View>(), WarningsContract.Presenter {

    override fun onCreate() {
        view.initScreen()
    }

    override fun onViewCreated() {
        view.showLoading()
        getWarningsUseCase.execute(onSuccess = {
            if(it.isNotEmpty()){
                view.showWarnings(it)
            }else{
                view.showEmptyView()
            }
            view.hideLoading()
        }, onError = {
            Timber.e(it, "error loading warnings")
            view.hideLoading()
            view.showWarningsError()
            view.navigateUp()
        })
    }

    override fun onClickAcceptNoWarnings() {
        view.navigateUp()
    }

    override fun unsubscribe() {
        getWarningsUseCase.clear()
    }
}