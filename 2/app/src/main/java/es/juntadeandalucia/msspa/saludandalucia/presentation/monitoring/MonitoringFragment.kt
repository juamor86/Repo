package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.adapter.MonitoringAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_follow_up.content_cl
import kotlinx.android.synthetic.main.fragment_follow_up.empty_gp
import kotlinx.android.synthetic.main.fragment_follow_up.follow_up_programs_rv

class MonitoringFragment : BaseFragment(), MonitoringContract.View {

    @Inject
    lateinit var presenter: MonitoringContract.Presenter

    private lateinit var adapter: MonitoringAdapter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_follow_up


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.onViewCreated()
        with(content_cl) {
            animateViews(
                this,
                animId = R.anim.slide_from_bottom
            )
        }
    }


    override fun setupView(followUpEntity: MonitoringEntity) {
        adapter = MonitoringAdapter(presenter::onProgramClicked)
        follow_up_programs_rv.adapter = adapter
        val monitoringEntry = mutableListOf(
            MonitoringEntity.MonitoringEntry(
                title = requireActivity().getString(R.string.measurement_title),
                type = Consts.TYPE_MEASUREMENT,
                code = "",
                id = ""
            )
        )
        monitoringEntry.addAll(followUpEntity.entry)
        adapter.setItemsAndNotify(monitoringEntry)
    }


    override fun navigateToProgram(item: MonitoringEntity.MonitoringEntry) {
        val bundle = bundleOf(Consts.ARG_ITEM to item)
        findNavController().navigate(R.id.monitoring_program_dest, bundle)
    }

    override fun showNotMonitoringMessage() {
        follow_up_programs_rv.visibility = View.GONE
        empty_gp.visibility = View.VISIBLE
    }

    override fun navigateToMeasurements() {
        findNavController().navigate(R.id.measurements_dest)
    }

}
