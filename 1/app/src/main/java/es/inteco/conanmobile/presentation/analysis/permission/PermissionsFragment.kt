package es.inteco.conanmobile.presentation.analysis.permission

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseContract
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_permission_list.*
import javax.inject.Inject

/**
 * Permissions fragment
 *
 * @constructor Create empty Permissions fragment
 */
class PermissionsFragment : BaseFragment(), PermissionsContract.View {

    @Inject
    lateinit var presenter: PermissionsContract.Presenter
    lateinit var detailAdapter: PermissionAdapter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun bindLayout() = R.layout.fragment_permission_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.let { _ ->
            presenter.onCreateView( getApplicationFromArguments())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun refillReciclerView(list: List<String>) {
        this.detailAdapter = PermissionAdapter(onClickItemListener = { }, onRemoveItemListener = { })

        permissions_rv.layoutManager = LinearLayoutManager(context)
        permissions_rv.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        permissions_rv.adapter = detailAdapter
        detailAdapter.setItemsAndNotify(list)
    }

    override fun fillToolbarTittle(title: String?) {
        val toolbar = activity?.toolbar
        title?.let { text ->
            toolbar?.setTitle(text)
        }
    }

    private fun getApplicationFromArguments(): ApplicationEntity {
        return arguments?.get(Consts.ARG_APPLICATION) as ApplicationEntity
    }


}