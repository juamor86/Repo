package es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview

import android.Manifest
import android.app.DownloadManager
import android.app.DownloadManager.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.huawei.hms.framework.common.ContextCompat
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Analytics.DOWNLOAD_FILE_FAILURE
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Analytics.DOWNLOAD_FILE_SUCCESS
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils.Companion.extractUriDownload
import es.juntadeandalucia.msspa.saludandalucia.utils.FileUtils.Companion.openDownloadedLocalUri
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.net.ssl.TrustManagerFactory

abstract class BaseWebViewFragment : BaseFragment(), BaseWebviewContract.View {

    private val presenter by lazy { bindPresenter() }
    abstract override fun bindPresenter(): BaseWebviewContract.Presenter
    private val baseWebView by lazy { bindWebView() }
    protected var firstTimeEnter = true
    abstract fun bindWebView(): WebView
    abstract fun getViewToAnimate(): View?
    protected var token = ""
    private var urlDownloader: String = ""
    private var contentDispositionCurrent: String = ""
    private var mimetypeCurrent: String = ""
    var trustManagerFactory: TrustManagerFactory? = null
    private var anyDisposable: Disposable? = null
    private var urlCurrent: String? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (baseWebView.canGoBack()) {
                baseWebView.goBack()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        bindWebView()
        configureWebView()
    }

    private fun configureWebView() {
        baseWebView.run {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    hideLoading()
                    animateWebView()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    if (error?.errorCode == -1 && error.description == "net::ERR_CACHE_MISS") {
                        urlCurrent?.let {
                            showLoading()
                            loadUrl(it, mapOf(Consts.AUTHORIZATION to token))
                        }
                    } else {
                        view?.loadUrl("about:blank")
                        presenter.onWebViewError()
                        Timber.e("Webview Error -> " + error.toString())
                    }
                }
            }

            setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                urlDownloader = url
                contentDispositionCurrent = contentDisposition
                mimetypeCurrent = mimetype
                checkPermissions()
            }
        }
    }

    private fun checkPermissions() {
        requestPermission()
    }

    private fun requestPermission() {
        permissionsLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                downloadManager()
            }
        }

    private fun downloadManager() {
        val request = DownloadManager.Request(Uri.parse(urlDownloader))
        request.apply {
            setTitle(
                URLUtil.guessFileName(
                    urlDownloader,
                    contentDispositionCurrent,
                    mimetypeCurrent
                )
            )
            setDescription(getString(R.string.downloading))
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            addRequestHeader(Consts.AUTHORIZATION, token)
            setDestinationInExternalFilesDir(
                requireContext(),
                Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(urlDownloader, contentDispositionCurrent, mimetypeCurrent)
            )
        }
        val downloadManager =
            ContextCompat.getSystemService(requireContext(), DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(requireContext(), getString(R.string.downloading), Toast.LENGTH_SHORT).show()
        requireActivity().registerReceiver(
            attachmentDownloadCompleteReceive,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    private var attachmentDownloadCompleteReceive: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, thisIntent: Intent?) {
                thisIntent?.let { intent ->
                    intent.action?.let { action ->
                        when (action) {
                            ACTION_DOWNLOAD_COMPLETE -> {
                                Timber.d("Download completed")
                                if (isAdded) {
                                    fileDownloaded(intent)
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun fileDownloaded(intent: Intent) {
        val downloadId = intent.getLongExtra(EXTRA_DOWNLOAD_ID, 0)
        anyDisposable = Single.fromCallable {
            extractUriDownload(
                requireContext(),
                downloadId
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resultUri: String? ->
                resultUri?.let { rUri ->
                    (requireActivity() as MainActivity).sendEvent(
                        DOWNLOAD_FILE_SUCCESS
                    ) {
                        openDownloadedLocalUri(
                            requireContext(),
                            rUri
                        )
                    }
                } ?: (requireActivity() as MainActivity).sendEvent(DOWNLOAD_FILE_FAILURE)
            }
    }

    override fun loadUrl(url: String) {
        urlCurrent = url
        sendEvent(Consts.Analytics.INTERNAL_WEBVIEW_SCREEN_ACCESS + Utils.getSelectedOptionUrl(url))
        showLoading()
        baseWebView.loadUrl(url)
    }

    override fun animateWebView() {
        if (firstTimeEnter) {
            getViewToAnimate()?.let {
                it.visibility = View.VISIBLE
                animateViews(it, animId = R.anim.slide_from_bottom)
                firstTimeEnter = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearCookies()
        anyDisposable?.dispose()
    }

    @SuppressWarnings("deprecation")
    fun clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else if (context != null) {
            val cookieSyncManager = CookieSyncManager.createInstance(requireContext())
            cookieSyncManager.startSync()
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncManager.stopSync()
            cookieSyncManager.sync()
        }
    }

    override fun loadToken(tokenCurrent: String) {
        token = tokenCurrent
    }
}
