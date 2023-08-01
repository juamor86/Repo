package es.inteco.conanmobile.presentation.base

/**
 * Base presenter
 *
 * @param V
 * @constructor Create empty Base presenter
 */
abstract class BasePresenter<V : BaseContract.View> : BaseContract.Presenter {

    protected lateinit var view: V

    override fun setViewContract(baseFragment: BaseContract.View) {
        view = baseFragment as V
    }
}
