package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.dialog.ImageDetailDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest.DynQuestNewController
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.viewholder.DynamicQuestionsViewHolder

class DynQuestNewAdapter(
    private val questHelper: DynQuestNewController,
    private val imagePickAction: ((DynQuestionEntity) -> Unit),
    private val scrollToPosition: ((Int) -> Unit),
    private val questionModified: ()-> Unit
) :
    ListAdapter<DynQuestionEntity, DynamicQuestionsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DynamicQuestionsViewHolder.createInstance(
            parent,
            viewType,
            questHelper::onResponseSelected,
            questionModified,
            imagePickAction,
            scrollToPosition,
        )

    override fun getItemViewType(position: Int) =
        (getItem(position) as DynQuestionEntity).getIntType()

    override fun onBindViewHolder(holder: DynamicQuestionsViewHolder, position: Int) {
        holder.bind(getItem(position), position)
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
