package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.detail.adapter.QuizFilledAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_REFRESH
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import kotlinx.android.synthetic.main.fragment_monitoring_program_detail.*
import javax.inject.Inject

class MonitoringDetailFragment : BaseFragment(), MonitoringDetailContract.View {

    @Inject
    lateinit var presenter: MonitoringDetailContract.Presenter
    lateinit var adapter: QuizFilledAdapter
    lateinit var title: String
    lateinit var id: String


    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout(): Int = R.layout.fragment_monitoring_program_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = QuizFilledAdapter()
        if(arguments?.get(ARG_REFRESH) == true){
            findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_REFRESH, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questFilled =
            arguments?.get(Consts.ARG_QUEST_FILLED) as MonitoringListEntity.QuestFilledEntity
        title = arguments?.get(Consts.ARG_TITLE) as String
        id =arguments?.get(Consts.ARG_ID) as String
        presenter.onViewCreated(questFilled,id)
        with(content_ns) {
            animateViews(
                this,
                animId = R.anim.slide_from_bottom
            )
        }
    }

    override fun setupView(entity: MonitoringListEntity.QuestFilledEntity) {

        title_section_tv.text = title
        detail_title_tv.text = ""
        val date = if (entity.date.length <= Consts.DATE_REX_LENGHT) {
            UtilDateFormat.formatStringDateTime(entity.date)
        } else {
            UtilDateFormat.stringToDate(entity.date, UtilDateFormat.DATE_FORMAT_TZ)
        }

        notification_details_date_tv.text = UtilDateFormat.dateToStringMonthName(date)
        notification_details_hour_tv.text = UtilDateFormat.timeToString(date).plus(" h")
        if (entity.description.isNotEmpty()) {
            detail_description_tv.text = entity.description
        }
        see_more_btn.setOnClickListener {
            if (answered_programs_rv.isVisible) {
                answered_programs_rv.visibility = View.GONE
                see_more_btn.apply {
                    text = resources.getText(R.string.btn_see_more)
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                }
            } else {
                sendEvent(Consts.Analytics.FOLLOWUP_DETAIL_EXTENDED_ACCESS)
                answered_programs_rv.visibility = View.VISIBLE
                see_more_btn.apply {
                    text = resources.getText(R.string.btn_see_less)
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                }
            }
        }

        answered_programs_rv.adapter = adapter
        adapter.submitList(entity.questions)
        adapter.notifyDataSetChanged()
    }
}
