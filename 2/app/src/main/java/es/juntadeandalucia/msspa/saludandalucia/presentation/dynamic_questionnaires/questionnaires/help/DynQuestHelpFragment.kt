package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.fragment_dyn_quest_help.*
import kotlinx.android.synthetic.main.fragment_dyn_quest_help.content_ll
import kotlinx.android.synthetic.main.fragment_dyn_quest_help.text_header_tv
import kotlinx.android.synthetic.main.fragment_dyn_quest_help.view_cl
import kotlinx.android.synthetic.main.view_item_help.view.*

class DynQuestHelpFragment : DialogFragment() {

   fun bindLayout(): Int = R.layout.fragment_dyn_quest_help

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(bindLayout(),container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dynQuestionnaireEntity = arguments?.get(Consts.ARG_QUEST_HELP) as DynQuestionnaireEntity
        buildScreen(dynQuestionnaireEntity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )
    }

    private fun buildScreen(dynHelp: DynQuestionnaireEntity) {
        back_arrow_iv.setOnClickListener {
            this.dismiss()
        }
        text_header_tv.text = dynHelp.name
        dynHelp.questions.forEach { dynQuestionEntity ->
            val childView = layoutInflater.inflate(R.layout.view_item_help, view_cl, false)
            val dynQuestionHelp = dynQuestionEntity as DynQuestionEntity.NotSupportedQuestionEntity
            val text = dynQuestionEntity.question
            dynQuestionHelp.valueCodingExtension?.forEach { item ->
                if(item.url == "subtipo"){
                    if(item.code == "Titulo"){
                        childView.title_help_tv.text = text
                        childView.title_help_tv.visibility = View.VISIBLE
                        childView.subtitle_help_tv.visibility = View.GONE
                    }
                    if(item.code == "Descripcion"){
                        childView.subtitle_help_tv.text = text
                        childView.subtitle_help_tv.visibility = View.VISIBLE
                        childView.title_help_tv.visibility = View.GONE
                    }
                    content_ll.addView(childView)
                }
            }
        }

    }

}
