package es.juntadeandalucia.msspa.saludandalucia.presentation.covid.quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.view_item_question_boolean.view.*
import kotlinx.android.synthetic.main.view_item_question_boolean.view.no_rb
import kotlinx.android.synthetic.main.view_item_question_boolean.view.yes_rb
import kotlinx.android.synthetic.main.view_item_question_boolean_clicsalud.view.*

class QuizQuestionsAdapter(
    private val onResponseSelectedListener: (
        questionEntity:
        QuizQuestionEntity,
        response: String,
        shouldScroll: Boolean,
        bottomPosition: Int
    ) -> Unit
) :
    ListAdapter<QuizQuestionEntity, QuizQuestionsAdapter.QuestionItemViewHolder>(
        DiffCallback()
    ) {

    companion object {
        const val TYPE_BOOLEAN = 1
        const val TYPE_BOOLEAN_EXT = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            TYPE_BOOLEAN -> QuestionItemViewHolder.BooleanQuestionItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_item_question_boolean, parent, false),
                onResponseSelectedListener
            )
            TYPE_BOOLEAN_EXT -> QuestionItemViewHolder.BooleanQuestionItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_item_question_boolean_ext, parent, false),
                onResponseSelectedListener
            )
            else -> null
        }!!

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {
            is QuizQuestionEntity.BooleanQuestionEntity -> TYPE_BOOLEAN
            is QuizQuestionEntity.BooleanExtQuestionEntity -> TYPE_BOOLEAN_EXT
            else -> -1
        }

    override fun onBindViewHolder(holder: QuestionItemViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    sealed class QuestionItemViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        class BooleanQuestionItemViewHolder(
            itemView: View,
            internal val onResponseSelectedListener: (questionEntity: QuizQuestionEntity, response: String, shouldScroll: Boolean, bottomPosition: Int) -> Unit
        ) : QuestionItemViewHolder(
            itemView
        ) {

            override fun bind(
                questionEntity: QuizQuestionEntity,
                position: Int
            ) {
                with(itemView) {
                    question_tv.text = questionEntity.question
                    yes_rb.setOnCheckedChangeListener { _, isChecked ->
                        run {
                            if (isChecked) {
                                onResponseSelectedListener(questionEntity, Consts.ANSWER_YES, !no_rb.isChecked, bottom)
                            }
                        }
                    }
                    no_rb.setOnCheckedChangeListener { _, isChecked ->
                        run {
                            if (isChecked) {
                                onResponseSelectedListener(questionEntity, Consts.ANSWER_NO, !yes_rb.isChecked, bottom)
                            }
                        }
                    }
                }
            }
        }

        abstract fun bind(
            questionEntity: QuizQuestionEntity,
            position: Int
        )
    }
}

class DiffCallback : DiffUtil.ItemCallback<QuizQuestionEntity>() {
    override fun areItemsTheSame(oldItem: QuizQuestionEntity, newItem: QuizQuestionEntity): Boolean {
        return oldItem.questionId == newItem.questionId
    }

    override fun areContentsTheSame(oldItem: QuizQuestionEntity, newItem: QuizQuestionEntity): Boolean {
        return oldItem == newItem
    }
}
