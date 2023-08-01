package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.adapter.DynQuizFilledAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.dialog.ImageDetailDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_REFRESH
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import kotlinx.android.synthetic.main.fragment_dyn_quest_detail.*
import javax.inject.Inject

class DynQuestDetailFragment : BaseFragment(), DynQuestDetailContract.View {

    @Inject
    lateinit var presenter: DynQuestDetailContract.Presenter
    lateinit var adapter: DynQuizFilledAdapter
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

    override fun bindLayout(): Int = R.layout.fragment_dyn_quest_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = DynQuizFilledAdapter()
        if(arguments?.get(ARG_REFRESH) == true){
            findNavController().previousBackStackEntry?.savedStateHandle?.set(ARG_REFRESH, true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questFilled =
            arguments?.get(Consts.ARG_QUEST_FILLED) as DynQuestListEntity.QuestFilledEntity
        title = arguments?.get(Consts.ARG_TITLE) as String
        id = arguments?.get(Consts.ARG_ID) as String
        presenter.onViewCreated(questFilled,id)
        with(content_ns) {
            animateViews(
                this,
                animId = R.anim.slide_from_bottom
            )
        }
    }

    override fun setupView(entity: DynQuestListEntity.QuestFilledEntity) {
        title_section_tv.text = title
        detail_title_tv.text = entity.description
        detail_title_tv.visibility = View.VISIBLE
        val date = if (entity.date.length <= Consts.DATE_REX_LENGHT) {
            UtilDateFormat.formatStringDateTime(entity.date)
        } else {
            UtilDateFormat.stringToDate(entity.date, UtilDateFormat.DATE_FORMAT_TZ)
        }

        notification_details_date_tv.text = UtilDateFormat.dateToStringMonthName(date)
        notification_details_hour_tv.text = UtilDateFormat.timeToString(date).plus(" h")
        see_more_btn.setOnClickListener {
            if (dyn_answered_detail_rv.isVisible) {
                dyn_answered_detail_rv.visibility = View.GONE
                see_more_btn.apply {
                    text = resources.getText(R.string.btn_see_more)
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
                }
            } else {
                sendEvent(Consts.Analytics.FOLLOWUP_DETAIL_EXTENDED_ACCESS)
                dyn_answered_detail_rv.visibility = View.VISIBLE
                see_more_btn.apply {
                    text = resources.getText(R.string.btn_see_less)
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
                }
            }
        }

        dyn_answered_detail_rv.adapter = adapter
        adapter.submitList(entity.questions)
        adapter.notifyDataSetChanged()
    }
}
