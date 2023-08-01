package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.list.adapter.MonitoringListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_REFRESH
import kotlinx.android.synthetic.main.fragment_monitoring_program.*
import javax.inject.Inject

class MonitoringListFragment : BaseFragment(), MonitoringListContract.View {

    @Inject
    lateinit var presenter: MonitoringListContract.Presenter

    private lateinit var adapter: MonitoringListAdapter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun bindLayout(): Int = R.layout.fragment_monitoring_program

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val item = arguments?.get(Consts.ARG_ITEM) as MonitoringEntity.MonitoringEntry
        presenter.onCreate(item)
        findNavController().currentDestination
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_program_btn.setOnClickListener { presenter.onNewProgramClicked() }
        val refresh =
            findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>(ARG_REFRESH)
        if (refresh == true) {
            presenter.onRefresh()
        }
    }

    override fun setTitle(title: String) {
        title_section_tv.text = title
    }

    override fun setSubtitle(subtitle: String) {
        subtitle_tv.text = subtitle
    }

    override fun setupList() {
        adapter = MonitoringListAdapter(presenter::onItemClicked)
    }


    override fun showList(monitoringList: MonitoringListEntity) {
        hideNoMonitoring()
        if (card_rv?.adapter == null) {
            card_rv?.adapter = adapter
        }
        card_rv?.visibility = View.VISIBLE
        adapter.setItemsAndNotify(monitoringList.questsFilled)
    }

    private fun hideNoMonitoring() {
        no_records_gr?.visibility = View.GONE
    }

    override fun showNoMonitoring() {
        card_rv?.visibility = View.GONE
        no_records_gr?.visibility = View.VISIBLE
    }

    override fun showNewMonitoringButton() {
        new_program_btn.visibility = View.VISIBLE
    }

    override fun navigateToDetail(
        detailProgram: MonitoringListEntity.QuestFilledEntity,
        title: String,
        id: String
    ) {
        val bundle = bundleOf(Consts.ARG_QUEST_FILLED to detailProgram, Consts.ARG_TITLE to title,Consts.ARG_ID to id)
        findNavController().navigate(R.id.monitoring_program_detail_dest, bundle)
    }

    override fun navigateToNewProgram(item: QuestionnaireEntity) {
        val bundle = bundleOf(Consts.ARG_ITEM to item)
        findNavController().navigate(R.id.monitoring_new_program_dest, bundle)
    }

    override fun animateView() {
        with(content_cl) {
            visibility = View.VISIBLE
            animateViews(
                this,
                animId = R.anim.slide_in_right
            )
        }
    }
}
