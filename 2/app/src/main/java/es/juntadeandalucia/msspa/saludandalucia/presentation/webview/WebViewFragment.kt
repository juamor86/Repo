package es.juntadeandalucia.msspa.saludandalucia.presentation.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.webview.BaseWebViewFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_webview.webview

class WebViewFragment : BaseWebViewFragment(), WebViewContract.View {

    @Inject
    lateinit var presenter: WebViewContract.Presenter
    override fun bindPresenter(): WebViewContract.Presenter = presenter
    override fun bindLayout() = R.layout.fragment_webview
    override fun bindWebView(): WebView = webview
    override fun getViewToAnimate(): View? = webview

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.get(Consts.URL_PARAM) as String
        presenter.onViewCreated(url)
    }
}
