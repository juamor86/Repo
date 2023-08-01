package es.inteco.conanmobile.presentation.osi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_osi.*
import kotlinx.android.synthetic.main.fragment_warnings.warnings_rv
import javax.inject.Inject


/**
 * O s i fragment
 *
 * @constructor Create empty O s i fragment
 */
class OSIFragment : BaseFragment(), OSIContract.View {

    @Inject
    lateinit var presenter: OSIContract.Presenter
    lateinit var OSIAdapter: OSIAdapter

    override fun bindPresenter(): OSIContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent.builder().applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule()).build().inject(this)
    }

    override fun bindLayout() = R.layout.fragment_osi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        link_osi_tv.setOnClickListener { goToOsiWebsite() }
    }

    override fun initScreen() {
        requireActivity().setTitle(R.string.osi_tips)
        OSIAdapter = OSIAdapter()
    }

    private fun goToOsiWebsite() {
        val url = "http://www.osi.es"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    override fun showOSITips(list: List<OSIEntity>) {
        warnings_rv.adapter = OSIAdapter
        OSIAdapter.setItemsAndNotify(list)
    }

    override fun showWarningsError() {
        showErrorDialog(R.string.error, R.string.warnings_error)
    }
}