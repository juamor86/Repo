package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.list.adapter.DynQuestListAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_QUIZ_ID
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_QUIZ_TITLE
import kotlinx.android.synthetic.main.fragment_dyn_quest_list.*
import javax.inject.Inject

class DynQuestListFragment : BaseFragment(), DynQuestListContract.View {

    @Inject
    lateinit var presenter: DynQuestListContract.Presenter

    private lateinit var adapter: DynQuestListAdapter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun bindLayout(): Int = R.layout.fragment_dyn_quest_list


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
        val quizId = arguments?.getString(ARG_QUIZ_ID)!!
        val title = arguments?.getString(ARG_QUIZ_TITLE)!!
        presenter.onCreate(quizId, title)
        findNavController().currentDestination
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val refresh =
            findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>(Consts.ARG_REFRESH)
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
        adapter = DynQuestListAdapter(presenter::onItemClicked)
    }

    override fun showList(questionnaireList: DynQuestListEntity) {
        hideNoMonitoring()
        if (dyn_quest_list_rv?.adapter == null) {
            dyn_quest_list_rv?.adapter = adapter
        }
        dyn_quest_list_rv?.visibility = View.VISIBLE
        adapter.setItemsAndNotify(questionnaireList.questsFilled)
    }

    private fun hideNoMonitoring() {
        no_records_gr?.visibility = View.GONE
    }

    override fun showNoQuestionnaire() {
        dyn_quest_list_rv?.visibility = View.GONE
        no_records_gr?.visibility = View.VISIBLE
    }

    override fun showNewQuestionnaireButton(showError: Boolean) {
        with(new_questionnaire_btn) {
            visibility = View.VISIBLE
            setOnClickListener {
                if (showError) {
                    showServiceNotAvailable()
                } else {
                    presenter.onNewQuestionnaireClicked()
                }
            }
        }
    }

    override fun showServiceNotAvailable(){
        showConfirmDialog(title = R.string.dialog_error_text)
    }

    override fun showQuestionnaireNotAuthorized() {
        questionnaire_authorized_gp.visibility = View.GONE
        questionnaire_no_authorized_gp.visibility = View.VISIBLE
    }

    override fun comeBackToDinamic() {
        findNavController().popBackStack()
    }

    override fun navigateToDetail(
        detailProgram: DynQuestListEntity.QuestFilledEntity,
        title: String,
        id: String
    ) {
        val bundle = bundleOf(
            Consts.ARG_QUEST_FILLED to detailProgram,
            Consts.ARG_TITLE to title,
            Consts.ARG_ID to id
        )
        findNavController().navigate(R.id.dyn_quest_detail_dest, bundle)
    }

    override fun navigateToNewQuestionnaire(
        item: DynQuestionnaireEntity?,
        title: String,
        id: String
    ) {
        val bundle = bundleOf(Consts.ARG_ITEM to item, Consts.ARG_TITLE to title, Consts.ARG_ID to id)
        findNavController().navigate(R.id.dyn_quest_new_dest, bundle)
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

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }
}

