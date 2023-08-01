package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.viewholder

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.dialog.ImageDetailDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.other.UiUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.HTTPS_WEB_START
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.HTTP_WEB_START
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.MB_FILE_SIZE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.PDF_MIME_TYPE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.PDF_TYPE
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.view_item_question.view.*
import kotlinx.android.synthetic.main.view_item_question_attachment.view.*
import kotlinx.android.synthetic.main.view_item_question_boolean.view.*
import kotlinx.android.synthetic.main.view_item_question_datetime.view.*
import kotlinx.android.synthetic.main.view_item_question_open_choice.view.*
import kotlinx.android.synthetic.main.view_item_question_quantity.view.*
import kotlinx.android.synthetic.main.view_item_question_text.view.*
import kotlinx.android.synthetic.main.view_item_question_text.view.text_answer_it
import kotlinx.android.synthetic.main.view_item_question_url.view.*
import kotlinx.android.synthetic.main.view_question_open_choice_edittext.view.*
import java.io.DataOutputStream
import java.io.File
import java.util.*


sealed class DynamicQuestionsViewHolder(
    parent: ViewGroup,
    resId: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(resId, parent, false)
) {

    var binded: Boolean = false
    lateinit var question: DynQuestionEntity

    companion object {
        fun createInstance(
            parent: ViewGroup,
            viewType: Int,
            onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)?,
            questionModified:()-> Unit?,
            imagePickAction: (DynQuestionEntity) -> Unit,
            scrollToPosition: ((Int) -> Unit)
        ): DynamicQuestionsViewHolder =
            when (viewType) {
                DynQuestionEntity.QuestionType.BOOLEAN.ordinal -> BooleanQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.DECIMAL.ordinal -> DecimalQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.INTEGER.ordinal -> IntegerQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.URL.ordinal -> UrlQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.TEXT.ordinal -> TextQuestionItemViewHolder(
                    parent, onResponseSelectedListener = onResponseSelectedListener, questionModified =  questionModified
                )
                DynQuestionEntity.QuestionType.DATE.ordinal -> DateQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.TIME.ordinal -> TimeQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.DATETIME.ordinal -> DateTimeQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.CHOICE.ordinal -> ChoiceQuestionItemViewHolder(
                    parent, onResponseSelectedListener, questionModified
                )
                DynQuestionEntity.QuestionType.OPEN_CHOICE.ordinal -> OpenChoiceQuestionItemViewHolder(
                    parent, onResponseSelectedListener, scrollToPosition, questionModified
                )
                DynQuestionEntity.QuestionType.QUANTITY.ordinal -> QuantityQuestionItemViewHolder(
                    parent, onResponseSelectedListener = onResponseSelectedListener,questionModified = questionModified
                )
                DynQuestionEntity.QuestionType.GROUP.ordinal -> GroupQuestionItemViewHolder(
                    parent
                )
                DynQuestionEntity.QuestionType.ATTACHMENT.ordinal -> AttachmentQuestionItemViewHolder(
                    parent, imagePickAction = imagePickAction, onResponseSelectedListener = onResponseSelectedListener, scrollToPosition = scrollToPosition
                )
                else -> NotSupportedQuestionItemViewHolder(
                    parent
                )
            }
    }

    open fun bind(
        questionEntity: DynQuestionEntity,
        position: Int
    ) {
        question = questionEntity
        with(itemView) {
            question_tv.text = questionEntity.question
        }
    }

    open fun fillQuestion(questionEntity: DynQuestionEntity) {
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
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_boolean_clicsalud
    ) {
        override fun bind(
            questionEntity: DynQuestionEntity,
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
                                top,
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

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
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
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent,
        viewRes
    ) {

        override fun bind(questionEntity: DynQuestionEntity, position: Int) {
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
                        let { (context as BaseActivity).hideKeyboard(it) }
                        onResponseSelectedListener?.let {
                            it(
                                questionEntity,
                                answer_et.text.toString(),
                                top,
                                false
                            )
                        }
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

    class UrlQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_url
    ) {
        override fun bind(questionEntity: DynQuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {

                this.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        answer_url_et.requestFocus()
                    }
                }

                answer_url_et.addTextChangedListener(object : TextWatcher {
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

                answer_url_et.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        let { (context as BaseActivity).hideKeyboard(it) }
                        onResponseSelectedListener?.let {
                            it(
                                questionEntity,
                                getResponse(),
                                top,
                                false
                            )
                        }
                    } else {
                        let { (context as BaseActivity).showKeyBoard() }
                    }
                }
            }
        }

        override fun resetView() {
            with(itemView) {
                answer_url_et.text?.clear()
            }
        }

        private fun getResponse(): String {
            var response = itemView.answer_url_et.text.toString().lowercase()
            if(Utils.isValidUrl(response)){
                if (!(response.contains(HTTP_WEB_START) || response.contains(HTTPS_WEB_START))) {
                    response = HTTP_WEB_START + response
                }
            }
            itemView.answer_url_et.setText(response)
            return response
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            with(itemView){
                answer_url_et?.isEnabled = false
                answer_url_et?.setText(questionEntity.answer!!.getTextValue())
            }
        }
    }

    class DateQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_date
    ) {
        override fun bind(questionEntity: DynQuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {
                answer_et.setOnClickListener {
                    if(!this.hasFocus()){
                        this.requestFocus()
                    }
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
                val formatedDate = UtilDateFormat.formatToAmericanSimpleDatetoString(date)
                answer_et.setText(UtilDateFormat.formatAmericanToStandardDate(formatedDate))
                questionModified.invoke()
                onResponseSelectedListener?.let { it(question, formatedDate, top, false) }
            }
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            with(itemView){
                answer_et.setText(UtilDateFormat.formatAmericanToStandardDate(questionEntity.answer?.valueDate.toString()))
                answer_et.isEnabled = false
            }
        }
    }

    class TimeQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_time
    ) {
        override fun bind(questionEntity: DynQuestionEntity, position: Int) {
            super.bind(questionEntity, position)
            with(itemView) {
                answer_et.setOnClickListener {
                    if(!this.hasFocus()){
                        this.requestFocus()
                    }
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
                questionModified.invoke()
                val formatedTime = UtilDateFormat.formatToSimpleTimeString(date)
                answer_et.setText(UtilDateFormat.formatTimeString(formatedTime))
                onResponseSelectedListener?.invoke(question, formatedTime, top, false)
            }
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            with(itemView){
                answer_et.setText(UtilDateFormat.formatTimeString(questionEntity.answer?.valueTime))
                answer_et.isEnabled = false
            }
        }

    }

    class DateTimeQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_datetime
    ) {
        private var formatedDate = ""
        private var formatedTime = ""

        override fun bind(questionEntity: DynQuestionEntity, position: Int) {
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
                if(!this.hasFocus()){
                    this.requestFocus()
                }
                questionModified.invoke()
                formatedDate = UtilDateFormat.formatToAmericanSimpleDatetoString(date)
                date_et.setText(UtilDateFormat.formatAmericanToStandardDate(formatedDate))
                saveValue(top)
            }
        }

        private fun onTimeSelected(time: Date) {
            with(itemView) {
                if(!this.hasFocus()){
                    this.requestFocus()
                }
                questionModified.invoke()
                formatedTime = UtilDateFormat.formatToSimpleTimeString(time)
                time_et.setText(UtilDateFormat.formatTimeString(formatedTime))
                saveValue(top)
            }
        }

        private fun saveValue(direction: Int) {
            val finalFormattedDate = if (formatedDate.isEmpty() || formatedTime.isEmpty()) {
                ""
            } else {
                formatedDate + "T" + formatedTime + "Z"
            }
            onResponseSelectedListener?.invoke(question, finalFormattedDate, direction, false)
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            with(itemView) {
                val splittedDateTime = questionEntity.answer?.valueDateTime!!.split("T")
                date_et.setText(UtilDateFormat.formatAmericanToStandardDate(splittedDateTime[0]))
                time_et.setText(UtilDateFormat.formatTimeString(splittedDateTime[1].dropLast(1)))
                date_et.isEnabled = false
                time_et.isEnabled = false
            }
        }

    }

    class IntegerQuestionItemViewHolder(
        parent: ViewGroup,
        onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        questionModified: () -> Unit?

    ) : TextQuestionItemViewHolder(
        parent,
        R.layout.view_item_question_integer_clicsalud,
        onResponseSelectedListener,
        questionModified
    )

    class NotSupportedQuestionItemViewHolder(
        parent: ViewGroup
    ) : DynamicQuestionsViewHolder(
        parent,
        R.layout.view_item_question_not_supported
    )

    class DecimalQuestionItemViewHolder(
        parent: ViewGroup,
        onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        questionModified: () -> Unit?

    ) : TextQuestionItemViewHolder(
        parent,
        R.layout.view_item_question_decimal,
        onResponseSelectedListener,
        questionModified
    )

    class GroupQuestionItemViewHolder(
        parent: ViewGroup
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_group
    )

    class ChoiceQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_open_choice
    ) {

        override fun bind(
            questionEntity: DynQuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            val question = questionEntity as DynQuestionEntity.ChoicesQuestionEntity
            with(itemView) {
                if (question.valueRange != null) {
                    val stringId = if (question.valueRange.low == 1) {
                        R.string.choice_subtitle_number_choice
                    } else {
                        R.string.choice_subtitle_number_choices
                    }
                    subtitle_open_choice_tv.text = resources.getString(
                        stringId,
                        question.valueRange.low.toString(),
                    )
                } else {
                    subtitle_open_choice_tv.visibility = View.GONE
                }
                options_ll.removeAllViews()
                for (option in question.options!!) {
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
                        if (isNewQuestion(question.options)) {
                            this.requestFocus()
                        }
                        if (isChecked && shouldActLikeRadioButton(question.valueRange)) {
                            actLikeRadioGroup(view, options_ll)
                        }
                        questionModified.invoke()
                        onResponseSelectedListener?.invoke(
                            questionEntity,
                            getResponses(options_ll,question.options),
                            top,
                            true
                        )
                    }
                }
            }
        }

        private fun shouldActLikeRadioButton(valueRange: DynQuestionEntity.ValueRangeEntity?): Boolean {
            valueRange?.let {
                return it.low == 1 && it.high == 1
            }
            return false
        }

        private fun isNewQuestion(options: List<DynQuestionEntity.AnswerOptionEntity>): Boolean {
            val optionList = options.filter { it.selected }
            return optionList.isEmpty()
        }

        private fun getResponses(
            options_ll: LinearLayout,
            options: List<DynQuestionEntity.AnswerOptionEntity>
        ): List<String>{
            val responseList = mutableListOf<String>()
            for(child in options_ll.children){
                if ((child as CheckBox).isChecked) {
                    responseList.add(options[child.id].getTextValue())
                }
            }
            return responseList
        }

        private fun actLikeRadioGroup(
            view: CompoundButton,
            options_ll: LinearLayout
        ) {
            for (child in options_ll.children) {
                if (child != view) {
                    (child as CheckBox).isChecked = false
                }
            }
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            val question = questionEntity as DynQuestionEntity.ChoicesQuestionEntity
            with(itemView) {
                for (option in question.options!!) {
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

    class AttachmentQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val imagePickAction: (DynQuestionEntity) -> Unit,
        private val scrollToPosition: (Int) -> Unit
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_attachment
    ) {
        override fun bind(
            questionEntity: DynQuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            with(itemView) {
                add_image_bt.setOnClickListener {
                    this.requestFocus()
                    imagePickAction.invoke(questionEntity)
                }

                delete_iv.setOnClickListener {
                    add_image_bt.visibility = View.VISIBLE
                    file_response_gp.visibility =  View.GONE
                    delete_iv.visibility = View.GONE
                    onResponseSelectedListener?.invoke(
                        question,
                        DynQuestionEntity.ValueAttachment(),
                        top,
                        false
                    )
                }
            }
        }

        fun showAttachment(file: File, size: Int) {
            with(itemView) {
                requestFocus()
                scrollToPosition.invoke(top)
                add_image_bt.visibility = View.GONE
                file_response_gp.visibility = View.VISIBLE
                delete_iv.visibility = View.VISIBLE

                file_description_tv.text =
                     "${file.name} - ${Utils.getConvertBytesInMb(size)} $MB_FILE_SIZE"
                file_description_tv.setOnClickListener {
                    if (file.name.substringAfterLast('.', "") != PDF_TYPE) {
                        val dialog = ImageDetailDialog(itemView.context)
                        dialog.show()
                        dialog.setImage(Utils.rotateBitmapOrientation(file.absolutePath))
                    } else {
                        val browserIntent = Intent(Intent.ACTION_VIEW)
                        browserIntent.setDataAndType(
                            FileProvider.getUriForFile(
                                context,
                                itemView.context.applicationContext.packageName + ".provider",
                                file
                            ), PDF_MIME_TYPE
                        )
                        browserIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                        context.startActivity(browserIntent)
                    }
                }
            }
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            val question = questionEntity as DynQuestionEntity.AttachmentQuestionEntity
            val valueAttachment = question.answer!!.valueAttachment!!
            with(itemView) {
                add_image_bt.visibility = View.GONE
                file_response_gp.visibility = View.VISIBLE
                val dataArray = Utils.decodeBase64(valueAttachment.data)

                val dataOutputStream = DataOutputStream(
                    context.openFileOutput(
                        valueAttachment.title,
                        Context.MODE_PRIVATE
                    )
                )
                dataOutputStream.write(dataArray)

                dataOutputStream.flush()
                dataOutputStream.close()
                val file = context.getDir(valueAttachment.title, Context.MODE_PRIVATE)

                file_description_tv.text = valueAttachment.title

                file_description_tv.setOnClickListener {
                    if (file.name.substringAfterLast('.', "") != PDF_TYPE) {
                        val dialog = ImageDetailDialog(itemView.context)
                        dialog.show()
                        dialog.setImage(Utils.getBitmapFromBytearray(dataArray))
                    } else {
                        val single = Single.fromCallable {
                            Utils.getPdf(context, valueAttachment.data, valueAttachment.title.dropLast(4))
                        }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { result: File? ->
                                if (result != null) {
                                    val path: Uri = FileProvider.getUriForFile(
                                        context,
                                        context.applicationContext.packageName + ".provider",
                                        result
                                    )
                                    val target = Intent(Intent.ACTION_VIEW)
                                    target.setDataAndType(path, PDF_MIME_TYPE)
                                    target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                    try {
                                        context.startActivity(target)
                                    } catch (e: ActivityNotFoundException) {

                                    }
                                }
                            }
                    }
                }
                delete_iv.visibility = View.GONE
            }
        }

    }

    class OpenChoiceQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val scrollToPosition: (Int) -> Unit,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_open_choice
    ) {

        override fun bind(
            questionEntity: DynQuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            val question = questionEntity as DynQuestionEntity.OpenChoicesQuestionEntity
            with(itemView) {
                if (question.valueRange != null) {
                    val stringId = if (question.valueRange.low == 1) {
                        R.string.choice_subtitle_number_choice
                    } else {
                        R.string.choice_subtitle_number_choices
                    }
                    subtitle_open_choice_tv.text = resources.getString(
                        stringId,
                        question.valueRange.low.toString(),
                    )
                } else {
                    subtitle_open_choice_tv.visibility = View.GONE
                }

                options_ll.removeAllViews()
                for (option in question.options.filter { !it.isOpenChiceText }) {
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
                    optionButton.setOnCheckedChangeListener { _, _ ->
                        questionModified.invoke()
                        if(isNewQuestion(question.options)){
                            this.requestFocus()
                        }
                        onResponseSelectedListener?.invoke(
                            questionEntity,
                            getResponses(options_ll,question.options),
                            top,
                            true
                        )
                    }
                }
                loadTextField(questionEntity)
            }
        }

        private fun isNewQuestion(options: List<DynQuestionEntity.AnswerOptionEntity>):Boolean{
            val optionList = options.filter { it.selected }
            return optionList.isEmpty()
        }

        private fun View.loadTextField(
            question: DynQuestionEntity.OpenChoicesQuestionEntity
        ) {
            LayoutInflater.from(context)
                .inflate(
                    R.layout.view_question_open_choice_edittext,
                    options_ll,
                    false
                ).also { it ->
                    options_ll.addView(it)
                    open_choice_rb.setOnCheckedChangeListener { view, _ ->
                        questionModified.invoke()
                        if (isNewQuestion(question.options)) {
                            this.requestFocus()
                        }
                        if (open_choice_text_answer_et.text.toString().isNotEmpty()) {
                            onResponseSelectedListener?.invoke(
                                question,
                                getResponses(options_ll, question.options),
                                top,
                                true
                            )
                        } else {
                            view.isChecked = false
                        }
                    }

                    open_choice_text_answer_et.addTextChangedListener(object : TextWatcher {
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
                            if (open_choice_rb.isChecked) {
                                onResponseSelectedListener?.invoke(
                                    question,
                                    getResponses(options_ll, question.options),
                                    -1,
                                    true
                                )
                            }
                        }
                    })

                    open_choice_text_answer_et.setOnFocusChangeListener { _, hasFocus ->
                        if(!hasFocus){
                            scrollToPosition.invoke(top)
                            let { (context as BaseActivity).hideKeyboard(it) }
                        }
                    }
                    open_choice_text_answer_et.setOnEditorActionListener { _, actionId: Int, _ ->
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            let { (context as BaseActivity).hideKeyboard(it) }
                            true
                        } else {
                            false
                        }
                    }
                }
        }

        private fun getResponses(
            options_ll: LinearLayout,
            options: List<DynQuestionEntity.AnswerOptionEntity>
        ): List<String> {
            val responseList = mutableListOf<String>()
            for (child in options_ll.children) {
                when (child) {
                    is CheckBox -> {
                        if (child.isChecked) {
                            responseList.add(options[child.id].getTextValue())
                        }
                    }
                    else -> {
                        with(child.open_choice_text_answer_et.text.toString()) {
                            if(this.isNotEmpty() && itemView.open_choice_rb.isChecked){
                                responseList.add(this)
                            }
                        }
                    }
                }
            }
            return responseList
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            val question = questionEntity as DynQuestionEntity.OpenChoicesQuestionEntity
            with(itemView) {
                for (option in question.options) {
                    with(options_ll.getChildAt(question.options.indexOf(option))) {
                        isEnabled = false
                        when (this) {
                            is CheckBox -> {
                                if (option.selected) {
                                    isChecked = true
                                }
                            }
                            is ConstraintLayout -> {
                                open_choice_text_answer_et.setText(option.getTextValue())
                                open_choice_rb.isChecked = true
                            }
                        }
                    }
                }
                open_choice_rb.isEnabled = false
                open_choice_text_answer_et.isEnabled = false
            }

        }
    }

    class QuantityQuestionItemViewHolder(
        parent: ViewGroup,
        private val onResponseSelectedListener: ((questionEntity: DynQuestionEntity, response: Any, scrollToPosition: Int, multipleSelection: Boolean) -> Unit)? = null,
        private val questionModified: () -> Unit?
    ) : DynamicQuestionsViewHolder(
        parent, R.layout.view_item_question_quantity
    ) {
        override fun bind(
            questionEntity: DynQuestionEntity,
            position: Int
        ) {
            super.bind(questionEntity, position)
            val question = questionEntity as DynQuestionEntity.QuantityQuestionEntity
            with(itemView) {

                this.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        quantity_sb.requestFocus()
                    }
                }

                val minValue = question.minValue ?: 0
                val maxValue = question.maxValue ?: 100
                val difference = maxValue - minValue
                var slideValue = ""

                min_value_tv.text = minValue.toString()
                max_value_tv.text = maxValue.toString()
                quantity_sb.max = difference

                quantity_sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        with(result_value_tv) {
                            visibility = View.VISIBLE
                            slideValue = (minValue + progress).toString()
                            text = slideValue + " " + question.unit
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        if (!itemView.hasFocus()) {
                            itemView.requestFocus()
                        }
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        seekBar?.progress
                        questionModified.invoke()
                        onResponseSelectedListener?.invoke(
                            questionEntity,
                            DynQuestionEntity.ValueQuantity(
                                value = slideValue,
                                unit = question.unit
                            ),
                            top,
                            false
                        )
                    }
                })
            }
        }

        override fun fillQuestion(questionEntity: DynQuestionEntity) {
            val question = questionEntity as DynQuestionEntity.QuantityQuestionEntity
            with(itemView) {
                val minValue = question.minValue ?: 0
                val maxValue = question.maxValue ?: 100
                min_value_tv.text = minValue.toString()
                max_value_tv.text = maxValue.toString()
                question.answer?.valueQuantity?.let {
                    quantity_sb.progress = it.value.toFloat().toInt() - (question.minValue ?: 0)
                    quantity_sb.isEnabled = false
                    with(result_value_tv) {
                        visibility = View.VISIBLE
                        text = it.value.replace(".0","")+ " " + it.unit
                    }
                }
            }
        }
    }
}
