package es.inteco.conanmobile.presentation.help

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.synthetic.main.fragment_help.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

/**
 * Help fragment
 *
 * @constructor Create empty Help fragment
 */
class HelpFragment : BaseFragment(), HelpContract.View {

    @Inject
    lateinit var presenter: HelpContract.Presenter
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    override fun bindPresenter(): HelpContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout() = R.layout.fragment_help

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun initView() {
        requireActivity().setTitle(R.string.help)
        val fileCopy = File(requireContext().cacheDir, Consts.FILENAME)
        try {
            if (!fileCopy.exists()) {
                val input = requireContext().resources.openRawResource(R.raw.help)
                val output = FileOutputStream(fileCopy)
                val buffer = ByteArray(1024)
                var size: Int
                while (input.read(buffer).also { size = it } != -1) {
                    output.write(buffer, 0, size)
                }
                input.close()
                output.close()
            }
            parcelFileDescriptor =
                ParcelFileDescriptor.open(fileCopy, ParcelFileDescriptor.MODE_READ_ONLY)
            if (parcelFileDescriptor != null) {
                pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
            }else{
                presenter.onFileError()
            }
        } catch (e: Exception) {
            Timber.e("Error reading pdf file - ${e.message}")
            presenter.onFileError()
        }
    }

    override fun initButtons() {
        button_pre_doc.setOnClickListener {
            presenter.onPreviousPageClicked()
        }
        button_next_doc.setOnClickListener {
            presenter.onNextPageClicked()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun showNoPDFDialog() {
        showWarningDialog(
            title = R.string.no_pdf_found_title,
            message = R.string.no_pdf_found_message,
            onAccept = { navigateUp() })
    }

    override fun openPdfFromRaw(index: Int) {
        val pageCount = pdfRenderer!!.pageCount
        if (pageCount <= index) {
            return
        }
        currentPage?.close()
        currentPage = pdfRenderer!!.openPage(index)
        val bitmap = Bitmap.createBitmap(
            currentPage!!.width, currentPage!!.height,
            Bitmap.Config.ARGB_8888
        )
        currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        pdf_image.setImageBitmap(bitmap)

        button_pre_doc!!.visibility = if (index <= 0) View.GONE else View.VISIBLE
        button_next_doc!!.visibility = if (index + 1 >= pageCount) View.GONE else View.VISIBLE

        pageIndexTv.text = "${index + 1} / $pageCount"
    }

    override fun closeRenderer() {
        if (null != currentPage) {
            currentPage!!.close()
        }
        if (pdfRenderer != null) {
            pdfRenderer!!.close()
        }
        if (parcelFileDescriptor != null) {
            parcelFileDescriptor!!.close()
        }
    }
}