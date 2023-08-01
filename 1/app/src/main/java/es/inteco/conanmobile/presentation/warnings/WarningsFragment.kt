package es.inteco.conanmobile.presentation.warnings

import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_warnings.*
import javax.inject.Inject

/**
 * Warnings fragment
 *
 * @constructor Create empty Warnings fragment
 */
class WarningsFragment: BaseFragment(), WarningsContract.View{

    @Inject
    lateinit var presenter: WarningsContract.Presenter
    lateinit var warningsAdapter: WarningsAdapter

    override fun bindPresenter(): WarningsContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout() = R.layout.fragment_warnings

    override fun initScreen() {
        requireActivity().setTitle(R.string.warnings)
        warningsAdapter = WarningsAdapter()
    }

    override fun showWarnings(list: List<WarningEntity>) {
        warnings_rv.adapter = warningsAdapter
        warningsAdapter.setItemsAndNotify(list)
    }

    override fun showWarningsError() {
        showErrorDialog(R.string.error, R.string.warnings_error)
    }

    override fun showEmptyView() {
       showSuccessDialog (title = R.string.no_warnings, onAccept = {presenter.onClickAcceptNoWarnings()})
    }
}