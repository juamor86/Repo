package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.dialog.ImageDetailDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.viewholder.DynamicQuestionsViewHolder

class DynQuizFilledAdapter :
    ListAdapter<DynQuestionEntity, DynamicQuestionsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DynamicQuestionsViewHolder.createInstance(parent, viewType, null, imagePickAction = {}, scrollToPosition = {}, questionModified = {})

    override fun getItemViewType(position: Int) =
        (getItem(position) as DynQuestionEntity).getIntType()

    override fun onBindViewHolder(holder: DynamicQuestionsViewHolder, position: Int) {
        with(getItem(position)) {
            holder.bind(this, position)
            if (answer != null) {
                holder.fillQuestion(this)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: DynamicQuestionsViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.resetView()
    }

    class DiffCallback : DiffUtil.ItemCallback<DynQuestionEntity>() {
        override fun areItemsTheSame(oldItem: DynQuestionEntity, newItem: DynQuestionEntity): Boolean {
            return oldItem.questionId == newItem.questionId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DynQuestionEntity, newItem: DynQuestionEntity): Boolean {
            return oldItem == newItem
        }
    }
}
