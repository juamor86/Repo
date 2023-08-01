package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebViewFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebviewContract
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_clicsalud_webview.*

class ClicSaludWebViewFragment : BaseWebViewFragment(), ClicSaludWebViewContract.View {

    @Inject
    lateinit var presenter: ClicSaludWebViewContract.Presenter
    override fun bindPresenter(): BaseWebviewContract.Presenter = presenter
    override fun bindLayout(): Int = R.layout.fragment_clicsalud_webview
    override fun bindWebView(): WebView = clicsalud_webview
    override fun getViewToAnimate(): View? = content_ll

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
        presenter.onCreate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val element = arguments?.get(Consts.URL_PARAM) as NavigationEntity
        presenter.onViewCreated(element)
    }

    override fun setupView(title: String) {
        text_header_tv.text = title
    }

    override fun loadUrl(url: String, header: Pair<String, String>?) {
        super.loadUrl(url)
        showLoading()
        if (header != null) {
            val extraHeaders: MutableMap<String, String> = HashMap()
            extraHeaders[header.first] = header.second
            clicsalud_webview?.loadUrl(url, extraHeaders)
        } else {
            clicsalud_webview?.loadUrl(url)
        }
    }
}
