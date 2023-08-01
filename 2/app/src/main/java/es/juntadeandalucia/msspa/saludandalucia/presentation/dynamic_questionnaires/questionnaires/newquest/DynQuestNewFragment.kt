package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.Intent.EXTRA_MIME_TYPES
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.EXTRA_OUTPUT
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestListEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic_questionnaires.DynQuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.newquest.adapter.DynQuestNewAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.viewholder.DynamicQuestionsViewHolder
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.IMAGE_VALUE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.JPEG_MIME_TYPE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.PDF_MIME_TYPE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.PNG_MIME_TYPE
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.checkCameraPermissionGranted
import es.juntadeandalucia.msspa.saludandalucia.utils.PermissionsUtil.Companion.checkReadStoragePermissionGranted
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.getPath
import kotlinx.android.synthetic.main.fragment_dyn_quest_new.*
import kotlinx.android.synthetic.main.fragment_dyn_quest_new.content_cl
import kotlinx.android.synthetic.main.fragment_dyn_quest_new.title_section_tv
import kotlinx.android.synthetic.main.view_item_question.view.*
import java.io.File
import java.util.*
import javax.inject.Inject


class DynQuestNewFragment : BaseFragment(), DynQuestNewContract.View {

    @Inject
    lateinit var presenter: DynQuestNewContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter

    override fun bindLayout(): Int = R.layout.fragment_dyn_quest_new

    private var onScrollListener: ViewTreeObserver.OnScrollChangedListener? = null
    private lateinit var questionsAdapter: DynQuestNewAdapter
    private var scrollPosition = 0
    private var formChanged = false
    private var photoFile: File? = null

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
        val item = arguments?.get(Consts.ARG_ITEM) as DynQuestionnaireEntity?
        val title = arguments?.get(Consts.ARG_TITLE) as String
        val quizId = arguments?.get(Consts.ARG_ID) as String
        presenter.onCreate(item, title,quizId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    //region - View methods
    override fun setupView(questionnaire: DynQuestionnaireEntity,title:String) {
        title_section_tv.text = title
        onScrollListener = ViewTreeObserver.OnScrollChangedListener {
            if (content_ns != null) {
                scrollPosition = content_ns.scrollY
            }
        }
        content_ns.viewTreeObserver.addOnScrollChangedListener(onScrollListener)
        send_btn.setOnClickListener {
            questions_rv.clearFocus()
            presenter.onSendButtonClicked()
        }
    }

    override fun enableSendButton() {
        send_btn.isEnabled = true
    }

    override fun disableSendButton() {
        send_btn.isEnabled = false
    }

    override fun removeScrollListener() {
        onScrollListener?.let {
            content_ns.viewTreeObserver.removeOnScrollChangedListener(it)
        }
    }

    override fun showConfirmDialog() {
        showDialog(
            title = R.string.quiz_confirm_dialog_title,
            message = R.string.dyn_quest_send_info_preview,
            positiveText = R.string.accept,
            onAccept = {
                presenter.sendQuestionnaireAnswer()
            },
            cancelText = R.string.cancel,
            onCancel = {}
        )
    }

    override fun showConFirmDialogLoggedout() {
        showConfirmDialog(
            title = R.string.quiz_confirm_dialog_title,
            message = R.string.loggedout_dialog_confirmation_title, onAccept = {
            presenter.sendQuestionnaireAnswer()
        }, onCancel = {})
    }
    
    override fun showServiceNotAvailable(){
        showConfirmDialog(title = R.string.dialog_error_text, onAccept = {
            closeView()
        })
    }

    override fun attachmentQuestionChanged() {
        formChanged = true
    }

    override fun showHelpButton() {
        with(quest_info_iv) {
            visibility = View.VISIBLE
            setOnClickListener {
                presenter.onHelpCLicked()
            }
        }
    }

    override fun hidenHelpButton() {
        quest_info_iv.visibility = View.GONE
    }

    override fun navigateToHelp(questionnaireEntityHelp: DynQuestionnaireEntity) {
        val bundle = bundleOf(
            Consts.ARG_QUEST_HELP to questionnaireEntityHelp
        )
        findNavController().navigate(R.id.dyn_quest_new_dest_to_help_dest, bundle)
    }

    override fun setupQuestionnaire(
        questionnaire: DynQuestionnaireEntity,
        questionsList: MutableList<DynQuestionEntity>,
        questionnaireHelper: DynQuestNewController
    ) {
        questionsAdapter =
            DynQuestNewAdapter(questionnaireHelper,presenter::onAttachmentClick,::scrollTo, ::questionModified)
        questions_rv.adapter = questionsAdapter
        questionsAdapter.submitList(questionsList)
        questionsAdapter.notifyDataSetChanged()
    }

    private fun questionModified() {
        formChanged = true
    }

    override fun onBackPressed() {
        if (formChanged) {
            showBackPressAlertDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun showBackPressAlertDialog() {
        showConfirmDialog(title = R.string.back_pressed_dyn_new_form_dialog_text, onAccept = {
            findNavController().popBackStack()
        }, onCancel = { })
    }

    private fun launchChooser() {
        val attachmentChooser = buildAttachmentChooser(
            photoChooser = buildPhotoChooser(),
            galleryChooser = buildGalleryChooser(),
            documentChooser = buildDocumentChooser()
        )
        resultLauncher.launch(attachmentChooser)
    }

    private fun buildAttachmentChooser(
        photoChooser: Intent,
        galleryChooser: Intent,
        documentChooser: Intent
    ): Intent? {
        val chooser = Intent.createChooser(photoChooser,getString(R.string.attachment_chooser_title))
        chooser.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            arrayOf(galleryChooser, documentChooser)
        )
        return chooser
    }

    private fun buildPhotoChooser(): Intent {
        val photo = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFileName = IMAGE_VALUE + Date().time + Consts.JPG_EXTENSION
        photoFile = Utils.getPhotoFileUri(photoFileName, requireContext())
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(
                    requireContext(),
                    "${BuildConfig.APPLICATION_ID}.provider",
                    photoFile!!
                )
            photo.putExtra(EXTRA_OUTPUT, fileProvider)
        }
        return photo
    }

    private fun buildGalleryChooser(): Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    private fun buildDocumentChooser(): Intent {
        val documents = Intent(Intent.ACTION_GET_CONTENT) // or ACTION_OPEN_DOCUMENT
        documents.type = "*/*"
        documents.putExtra(EXTRA_MIME_TYPES, arrayOf(PDF_MIME_TYPE, PNG_MIME_TYPE, JPEG_MIME_TYPE))
        return documents
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            var photoPath = ""
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data: Intent? = result.data
                    data?.data?.let { uri ->
                        val selectedImageUri: Uri = uri
                        photoPath =
                            getPath(requireContext().applicationContext, selectedImageUri)
                    }
                } else {
                    photoPath = photoFile!!.absolutePath
                }
                if (photoPath == "Not found") {
                    photoPath = try {
                        val file = result.data!!.data?.let {
                            FileUtils.createTemporalFile(
                                requireContext(),
                                it
                            )
                        }
                        file?.absolutePath ?: ""
                    } catch (e: Exception) {
                        ""
                    }
                }
                if (photoPath.isNotEmpty()) {
                    val file = File(photoPath)
                    presenter.attachmentCompleted(file)
                }
            }
        }

    override fun scrollTo(scrollPosition: Int) {
        if(scrollPosition != -1){
            content_ns?.postDelayed({ content_ns?.scrollTo(0, scrollPosition) }, 200)
        }
    }

    override fun focusElement(position: Int) {
        questions_rv?.postDelayed({
            questions_rv?.findViewHolderForAdapterPosition(position)?.itemView?.requestFocus()
        }, 300)
    }

    override fun startQuiz() {
        questions_rv.visibility = View.VISIBLE
    }

    override fun setQuestionOk(position: Int) {
        questions_rv.findViewHolderForAdapterPosition(position)?.itemView?.question_tv?.setTextColor(
            ContextCompat.getColor(requireContext(),R.color.green_dark))
    }


    override fun setQuestionError(position: Int) {
        questions_rv.findViewHolderForAdapterPosition(position)?.itemView?.question_tv?.setTextColor(
            ContextCompat.getColor(requireContext(),R.color.red_orange))
    }

    override fun showQuestion(position: Int) {
        questionsAdapter.notifyItemInserted(position)
    }

    override fun hideQuestion(position: Int) {
        if (position > -1) {
            questionsAdapter.notifyItemRemoved(position)
        }
    }

    override fun informQuestionResponseNotValid(position: Int) {
        var isShown = false
        if (!isShown) {
            isShown = true
            showConfirmDialog(R.string.question_response_not_valid, onAccept = {
                isShown = false
                this.scrollTo(position)
            })
        }
    }

    override fun informQuestionResponseLessMin() {
        showConfirmDialog(R.string.question_response_less_min)
    }

    override fun informQuestionResponseMoreMax() {
        showConfirmDialog(R.string.question_response_more_max)
    }

    override fun informQuestionChoiceResponseMoreMax() {
        showConfirmDialog(R.string.question_choice_response_more_max)
    }

    override fun showFilesAreTooLarge() {
        showConfirmDialog(R.string.quest_file_too_large_error)
    }

    override fun informQuestionUrlFormatNotValid(position: Int) {
        var isShown = false
        if (!isShown) {
            this.scrollTo(position)
            isShown = true
            showConfirmDialog(R.string.url_format_nor_valid_error, onAccept = {
                isShown = false
            })
        }
    }

    override fun closeView() {
        findNavController().navigateUp()
    }

    override fun showSendAnswersSuccess(
        detailProgram: DynQuestListEntity.QuestFilledEntity,
        title: String,
        id: String
    ) {
        showSuccessDialog(
            title = R.string.dyn_quest_send_success,
            onAccept = {
                val bundle =
                    bundleOf(Consts.ARG_QUEST_FILLED to detailProgram, Consts.ARG_TITLE to title, Consts.ARG_ID to id)
                findNavController().navigate(R.id.dyn_quest_new_dest_to_detail_dest, bundle)
            })
    }

    override fun showSendAnswersError() {
        showErrorDialog(R.string.dyn_quest_send_error)
    }

    override fun animateView() {
        with(content_cl) {
            visibility = View.VISIBLE
            animateViews(
                this,
                animId = R.anim.slide_from_bottom
            )
        }
    }

    override fun requestPermissions() {
        if (!checkCameraPermissionGranted(requireContext()) || !checkReadStoragePermissionGranted(requireContext())) {
            requestMultiplePermissions.launch(
                arrayOf(READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            )
        } else {
            launchChooser()
        }
    }

    override fun showAttachmentResponse(file: File, position: Int, size: Int) {
        with(questions_rv.findViewHolderForAdapterPosition(position)) {
            if (this is DynamicQuestionsViewHolder.AttachmentQuestionItemViewHolder) {
                this.showAttachment(file,size)
            }
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true && permissions[READ_EXTERNAL_STORAGE] == true) {
                launchChooser()
            } else if (permissions.size == 1 && (permissions[Manifest.permission.CAMERA] == true || permissions[READ_EXTERNAL_STORAGE] == true)) {
                launchChooser()
            }
        }
}

