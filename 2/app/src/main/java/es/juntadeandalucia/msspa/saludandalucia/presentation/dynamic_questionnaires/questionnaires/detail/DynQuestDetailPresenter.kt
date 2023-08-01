package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class DynQuestDetailPresenter() : BasePresenter<DynQuestDetailContract.View>(),
    DynQuestDetailContract.Presenter {

    override fun onViewCreated(questFilled: DynQuestListEntity.QuestFilledEntity, id:String) {
        view.apply {
            setupView(questFilled)
        }
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.DYN_QUEST_QUIZ_EXTENDED_DETAIL

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
    }
}
