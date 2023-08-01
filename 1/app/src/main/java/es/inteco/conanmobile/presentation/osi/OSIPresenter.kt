package es.inteco.conanmobile.presentation.osi

import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.domain.usecases.GetOSITipsUseCase
import es.inteco.conanmobile.presentation.base.BasePresenter
import timber.log.Timber

/**
 * O s i presenter
 *
 * @property getOSITipsUseCase
 * @constructor Create empty O s i presenter
 */
class OSIPresenter(
    private val getOSITipsUseCase: SingleUseCase<Void, List<OSIEntity>>
) : BasePresenter<OSIContract.View>(), OSIContract.Presenter {

    override fun onCreate() {
        view.initScreen()
    }

    override fun onViewCreated() {
        view.showLoading()
        getOSITipsUseCase.execute(onSuccess = {
            view.showOSITips(it)
            view.hideLoading()
        }, onError = {
            Timber.e(it, "error loading warnings")
            view.hideLoading()
            view.showWarningsError()
            view.navigateUp()
        })
    }

    override fun unsubscribe() {
        getOSITipsUseCase.clear()
    }
}