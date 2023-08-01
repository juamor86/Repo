package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.saludandalucia.presentation.other.UiUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import kotlinx.android.synthetic.main.view_item_question.view.*
import kotlinx.android.synthetic.main.view_item_question_boolean.view.*
import kotlinx.android.synthetic.main.view_item_question_choice.view.*
import kotlinx.android.synthetic.main.view_item_question_datetime.view.*
import kotlinx.android.synthetic.main.view_item_question_open_choice.view.*
import kotlinx.android.synthetic.main.view_item_question_text.view.*
import java.util.*

sealed class QuestionsViewHolder(
    parent: ViewGroup,
    resId: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(resId, parent, false)
) {

    var binded: Boolean = false
    lateinit var question: QuestionEntity

    companion object {
        fun createInstance(
            parent: ViewGroup,
            viewType: Int,
            onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)?,
            questionModified:()-> Unit?,
        ): QuestionsViewHolder =
            when (viewType) {
                QuestionEntity.QuestionType.BOOLEAN.ordinal -> BooleanQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.DECIMAL.ordinal -> DecimalQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.INTEGER.ordinal -> IntegerQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.TEXT.ordinal -> TextQuestionItemViewHolder(
                    parent, onResponseSelectedListener = onResponseSelectedListener, questionModified = questionModified
                )
                QuestionEntity.QuestionType.DATE.ordinal -> DateQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.TIME.ordinal -> TimeQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.DATETIME.ordinal -> DateTimeQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.CHOICE.ordinal -> ChoiceQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.OPEN_CHOICE.ordinal -> OpenChoiceQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                QuestionEntity.QuestionType.QUANTITY.ordinal -> TextQuestionItemViewHolder(
                    parent, onResponseSelectedListener = onResponseSelectedListener,questionModified = questionModified
                )
                QuestionEntity.QuestionType.GROUP.ordinal -> GroupQuestionItemViewHolder(
                    parent
                )
                else -> NotSupportedQuestionItemViewHolder(
                    parent
                )
            }
    }

    open fun bind(
        questionEntity: QuestionEntity,
        position: Int
    ) {
        question = questionEntity
        with(itemView) {
            question_tv.text = questionEntity.question
        }
    }

    open fun fillQuestion(questionEntity: QuestionEntity) {
        with(itemView) {
            answer_et?.isEnabled = false
            answer_et?.setText(questionEntity.answer!!.getTextValue())
            text_answer_it?.hint = resources.getText(R.string.answer)
        }
    }

    open fun resetView() {
    }

    class BooleanQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_boolean_clicsalud
    ) {
        override fun bind(
            questionEntity: QuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            with(itemView) {
                boolean_rg.setOnCheckedChangeListener { _, checkedId ->
                    if (checkedId != -1) {
                        questionModified.invoke()
                        onResponseSelectedListener?.let {
                            it(
                                questionEntity,
                                checkedId == yes_rb.id,
                                bottom,
                                false
                            )
                        }
                    }
                }
            }
        }

        override fun resetView() {
            with(itemView) {
                boolean_rg.clearCheck()
            }
        }

        override fun fillQuestion(questionEntity: QuestionEntity) {
            if (questionEntity.answer?.valueBoolean!!) {
                itemView.yes_rb.isChecked = true
            } else {
                itemView.no_rb.isChecked = true
            }
            itemView.yes_rb.isEnabled = false
            itemView.no_rb.isEnabled = false
        }
    }

    open class TextQuestionItemViewHolder(
        parent: ViewGroup,
        viewRes: Int = R.layout.view_item_question_text,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent,
        viewRes
    ) {

        override fun bind(questionEntity: QuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {

                this.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        answer_et.requestFocus()
                    }
                }

                answer_et.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        //Nothing to do
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        questionModified.invoke()
                    }

                    override fun afterTextChanged(s: Editable?) {
                        //Nothing to do
                    }
                })

                answer_et.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        onResponseSelectedListener?.let {
                            it(
                                questionEntity,
                                answer_et.text.toString(),
                                bottom,
                                false
                            )
                        }
                        let { (context as BaseActivity).hideKeyboard(it) }
                    } else {
                        let { (context as BaseActivity).showKeyBoard() }
                    }
                }
            }
        }

        override fun resetView() {
            with(itemView) {
                answer_et.text?.clear()
            }
        }
    }

    class DateQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_date
    ) {
        override fun bind(questionEntity: QuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {
                answer_et.setOnClickListener {
                    UiUtils.showDatePicker(
                        UiUtils.PickerType.DATE,
                        context,
                        ::onDateSelected
                    )
                }
            }
        }

        private fun onDateSelected(date: Date) {
            with(itemView) {
                answer_et.setText(UtilDateFormat.dateToString(date))
                questionModified.invoke()
                onResponseSelectedListener?.let { it(question, date, bottom, false) }
            }
        }
    }

    class TimeQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_time
    ) {
        override fun bind(questionEntity: QuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {
                answer_et.setOnClickListener {
                    UiUtils.showDatePicker(
                        UiUtils.PickerType.TIME,
                        context,
                        ::onTimeSelected
                    )
                }
            }
        }

        private fun onTimeSelected(date: Date) {
            with(itemView) {
                answer_et.setText(UtilDateFormat.timeToString(date))
                questionModified.invoke()
                onResponseSelectedListener?.invoke(question, date, bottom, false)
            }
        }
    }

    class DateTimeQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_datetime
    ) {
        override fun bind(questionEntity: QuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {
                date_et.setOnClickListener {
                    UiUtils.showDatePicker(
                        UiUtils.PickerType.DATE,
                        context,
                        ::onDateSelected
                    )
                }
                time_et.setOnClickListener {
                    UiUtils.showDatePicker(
                        UiUtils.PickerType.TIME,
                        context,
                        ::onTimeSelected
                    )
                }
            }
        }

        private fun onDateSelected(date: Date) {
            with(itemView) {
                date_et.setText(UtilDateFormat.dateToString(date))
                questionModified.invoke()
                onResponseSelectedListener?.invoke(question, date, bottom, false)
            }
        }

        private fun onTimeSelected(time: Date) {
            with(itemView) {
                time_et.setText(UtilDateFormat.timeToString(time))
                questionModified.invoke()
                onResponseSelectedListener?.invoke(question, time, bottom, false)
            }
        }

        override fun fillQuestion(questionEntity: QuestionEntity) {
            with(itemView) {
                val splittedDateTime = questionEntity.answer?.valueDateTime!!.split("-")
                date_et.setText(splittedDateTime[0])
                time_et.setText(splittedDateTime[1])
            }
        }
    }

    class IntegerQuestionItemViewHolder(
        parent: ViewGroup,
        onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        questionModified: () -> Unit?
    ) : TextQuestionItemViewHolder(
        parent,
        R.layout.view_item_question_integer_clicsalud,
        onResponseSelectedListener,
        questionModified
    )

    class NotSupportedQuestionItemViewHolder(
        parent: ViewGroup
    ) : QuestionsViewHolder(
        parent,
        R.layout.view_item_question_not_supported
    )

    class DecimalQuestionItemViewHolder(
        parent: ViewGroup,
        onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        questionModified: () -> Unit?
    ) : TextQuestionItemViewHolder(
        parent,
        R.layout.view_item_question_decimal,
        onResponseSelectedListener,
        questionModified
    )

    class GroupQuestionItemViewHolder(
        parent: ViewGroup
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_group
    )

    class ChoiceQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_choice
    ) {

        override fun bind(
            questionEntity: QuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            if (!binded) {
                val question = questionEntity as QuestionEntity.ChoicesQuestionEntity
                with(itemView) {
                    for (option in question.options!!) {
                        val optionButton = LayoutInflater.from(context)
                            .inflate(
                                R.layout.view_question_choice,
                                choice_rg,
                                false
                            ) as RadioButton
                        optionButton.text = option.getTextValue()
                        optionButton.tag = option
                        optionButton.id = question.options.indexOf(option)
                        choice_rg.addView(optionButton)
                    }
                    choice_rg.setOnCheckedChangeListener { _, checkedId ->
                        questionModified.invoke()
                        onResponseSelectedListener?.invoke(
                            questionEntity,
                            findViewById(checkedId),
                            bottom,
                            true
                        )
                    }
                }
                binded = true
            }
        }

        override fun resetView() {
            super.resetView()
            with(itemView) {
                choice_rg.clearCheck()
            }
        }

        override fun fillQuestion(questionEntity: QuestionEntity) {
            val question = questionEntity as QuestionEntity.ChoicesQuestionEntity
            with(itemView) {
                for (option in question.options!!) {
                    val index = question.options.indexOf(option)
                    choice_rg[index].isEnabled = false
                    if (option.selected) {
                        choice_rg.check(index)
                    }
                }
            }
        }
    }

    class OpenChoiceQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: QuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : QuestionsViewHolder(
        parent, R.layout.view_item_question_open_choice
    ) {

        override fun bind(
            questionEntity: QuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            val question = questionEntity as QuestionEntity.OpenChoicesQuestionEntity
            with(itemView) {
                options_ll.removeAllViews()
                for (option in question.options) {
                    val optionButton = LayoutInflater.from(context)
                        .inflate(
                            R.layout.view_question_open_choice,
                            options_ll,
                            false
                        ) as CheckBox
                    optionButton.text = option.getTextValue()
                    optionButton.tag = option
                    optionButton.id = question.options.indexOf(option)
                    options_ll.addView(optionButton)
                    optionButton.setOnCheckedChangeListener { view, isChecked ->
                        questionModified.invoke()
                        question.answer
                        onResponseSelectedListener?.invoke(
                            questionEntity,
                            view.tag.also { valueCoding ->
                                (valueCoding as QuestionEntity.AnswerOptionEntity).selected =
                                    isChecked
                            }!!,
                            bottom,
                            false
                        )
                    }
                }
            }
        }

        override fun fillQuestion(questionEntity: QuestionEntity) {
            val question = questionEntity as QuestionEntity.OpenChoicesQuestionEntity
            with(itemView) {
                for (option in question.options) {
                    with(options_ll.getChildAt(question.options.indexOf(option)) as CheckBox) {
                        isEnabled = false
                        if (option.selected) {
                            isChecked = true
                        }
                    }
                }
            }
        }
    }
}
