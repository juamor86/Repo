package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.newquestionnaire.QuestionnaireController
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.viewholder.QuestionsViewHolder

class QuestionnaireAdapter(
    private val questHelper: QuestionnaireController,
    private val questionModified: ()-> Unit
) :
    ListAdapter<QuestionEntity, QuestionsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuestionsViewHolder.createInstance(
            parent,
            viewType,
            questHelper::onResponseSelected,
            questionModified,
        )

    override fun getItemViewType(position: Int) =
        (getItem(position) as QuestionEntity).getIntType()

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun onViewDetachedFromWindow(holder: QuestionsViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.resetView()
    }

    class DiffCallback : DiffUtil.ItemCallback<QuestionEntity>() {
        override fun areItemsTheSame(oldItem: QuestionEntity, newItem: QuestionEntity): Boolean {
            return oldItem.questionId == newItem.questionId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: QuestionEntity, newItem: QuestionEntity): Boolean {
            return oldItem == newItem
        }
    }
}
