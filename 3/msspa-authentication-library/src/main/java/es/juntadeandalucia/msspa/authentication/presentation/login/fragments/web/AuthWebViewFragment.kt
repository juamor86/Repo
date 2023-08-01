package es.juntadeandalucia.msspa.authentication.presentation.login.fragments.web

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.security.KeyChain
import android.security.KeyChainException
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationManager
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.authentication.di.module.FragmentModule
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.domain.entities.AuthorizeEntity
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseContract
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import kotlinx.android.synthetic.main.fragment_auth_webview.*
import timber.log.Timber
import java.net.URISyntaxException
import java.security.PrivateKey
import java.security.cert.X509Certificate
import javax.inject.Inject
import kotlin.math.max

class AuthWebViewFragment : BaseFragment(), AuthWebViewContract.View {
    //region VARIABLES
    @Inject
    lateinit var presenter: AuthWebViewContract.Presenter

    override fun bindPresenter(): BaseContract.Presenter = presenter
    //endregion

    //region INITIALIZATION
    override fun bindLayout() = R.layout.fragment_auth_webview

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .authLibraryComponent((requireActivity() as BaseActivity).authLibraryComponent)
            .netModule(NetModule((requireActivity() as BaseActivity).authConfig))
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        presenter.onViewCreated(
            (requireActivity() as BaseActivity).authConfig,
            (requireActivity() as BaseActivity).scope
        )
    }

    override fun configureView() {
        configureWebView()
    }

    private fun configureWebView() {
        auth_wv.run {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                private var running = 0

                override fun onReceivedClientCertRequest(
                    webView: WebView?,
                    request: ClientCertRequest?
                ) {
                    request?.let {
                        chooseCertificate(it)
                    }
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (--running == 0) {
                        hideLoading()
                        animateUpWebview()
                    }
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    running = max(running, 1)
                }

                override fun onReceivedError(
                    view: WebView?,
                    errorCode: Int,
                    description: String?,
                    failingUrl: String?
                ) {
                    view?.loadUrl("about:blank")
                    presenter.onWebViewError(description)
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    view?.loadUrl("about:blank")
                    presenter.onWebViewError(error?.description.toString())
                }

                override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
                    running++
                    if (isAdded) {
                        url?.run {
                            val uri = Uri.parse(url)
                            if ((requireActivity() as BaseActivity).authConfig.appKey == uri.scheme) {
                                try {
                                    with(uri) {
                                        getQueryParameter(MsspaAuthConsts.AUTHENTICATION_LOGIN_METHOD)?.let { mode ->
                                            openLoginNative(mode = mode, sessionId = getQueryParameter(MsspaAuthConsts.AUTHENTICATION_SESSION_ID) ?: "",
                                                sessionData = getQueryParameter(MsspaAuthConsts.AUTHENTICATION_SESSION_DATA) ?: "")
                                        } ?: checkLoginFinished(uri)
                                        getExternalUrl(uri)?.let { link ->
                                            openLink(link)
                                        }
                                        return true
                                    }
                                } catch (ex: URISyntaxException) {
                                    Timber.e(ex)
                                    presenter.onLoginConfigError()
                                    return false
                                } finally {
                                    CookieManager.getInstance().removeAllCookies(null)
                                    WebView.clearClientCertPreferences(null)
                                }
                            }
                        }
                    }
                    return false
                }
            }
        }
    }

    private fun getExternalUrl(uri: Uri): String? {
        val url = uri.toString()
        val contentUri: List<String>
        if (url.contains(MsspaAuthConsts.QueryParams.EXTERNAL_LINK)) {
            contentUri = url.split("=")
            return contentUri[1]
        }
        return null
    }

    private fun openLoginNative(mode: String, sessionId: String, sessionData: String){
        val loginMode: MsspaAuthConsts.LoginMethod? = MsspaAuthConsts.LoginMethod.getMethod(mode)
        loginMode?.let {
            if(sessionId.isEmpty() || sessionData.isEmpty()){
                Timber.i("SessioId or SessionData is empty.It not open personal data login.")
                return
            }
            openPersonalDataLogin(it, AuthorizeEntity(sessionId = sessionId, sessionData = sessionData))
        }
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context?.startActivity(intent)
    }

    private fun checkLoginFinished(uri : Uri){
      with(uri){
          if (getQueryParameter(MsspaAuthConsts.QueryParams.STATUS).equals(MsspaAuthConsts.QueryParams.STATUS_VALUE_OK)) {
              presenter.onLoginSuccess(
                  getQueryParameter(MsspaAuthConsts.QueryParams.TOKEN_TYPE)!!,
                  getQueryParameter(MsspaAuthConsts.QueryParams.ACCESS_TOKEN)!!,
                  getQueryParameter(MsspaAuthConsts.QueryParams.EXPIRES_IN),
                  getQueryParameter(MsspaAuthConsts.QueryParams.SCOPE)!!
              )
          } else if (getQueryParameter(MsspaAuthConsts.QueryParams.STATUS).equals(MsspaAuthConsts.QueryParams.STATUS_VALUE_ERROR)) {
              presenter.onLoginConfigError()
          }
      }
    }

    private fun chooseCertificate(request: ClientCertRequest) {
        KeyChain.choosePrivateKeyAlias(
            requireActivity(),
            { alias ->
                if (alias != null) {
                    requireActivity().let {
                        try {
                            val chain: Array<X509Certificate>? =
                                KeyChain.getCertificateChain(it, alias)
                            val privateKey: PrivateKey? = KeyChain.getPrivateKey(it, alias)
                            request.proceed(privateKey, chain)
                        } catch (ex: KeyChainException) {
                            Timber.e(ex, "Error choosing the certificate")
                            requireActivity().runOnUiThread {
                                presenter.onCertError()
                            }
                        } catch (ex: InterruptedException) {
                            Timber.e(ex, "Error choosing the certificate")
                            requireActivity().runOnUiThread {
                                presenter.onCertError()
                            }
                        }
                    }
                } else {
                    Timber.e("Error choosing the certificate.")
                    requireActivity().runOnUiThread {
                        presenter.onNoCertError()
                    }
                }
            },
            null,
            null,
            null,
            -1,
            null
        )
    }

    private fun animateUpWebview() {
        if(activity?.isFinishing == false) {
            auth_wv?.visibility = View.VISIBLE
            val animationFooter =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.slide_from_bottom)
            auth_wv?.startAnimation(animationFooter)
        }
    }

    private fun openPersonalDataLogin(
        loginMethod: MsspaAuthConsts.LoginMethod,
        authorizeEntity: AuthorizeEntity
    ) {
        val action = when (loginMethod) {
            MsspaAuthConsts.LoginMethod.QR -> {
                presenter.resetLoggingQRAttempts()
                R.id.qr_login_dest
            }
            MsspaAuthConsts.LoginMethod.PERSONAL_DATA -> R.id.action_auth_webview_dest_to_first_factor_basic_dest
            MsspaAuthConsts.LoginMethod.PERSONAL_DATA_BDU -> {
                if ((requireActivity() as BaseActivity).authEntity?.scope == MsspaAuthenticationManager.Scope.LEVEL_1) {
                    R.id.action_auth_webview_dest_to_phone_validation_dest
                } else {
                    R.id.action_auth_webview_dest_to_first_factor_reinforcement_dest
                }
            }
            MsspaAuthConsts.LoginMethod.PERSONAL_DATA_NO_NUHSA -> R.id.action_auth_webview_dest_to_first_factor_no_nuhsa_dest
            MsspaAuthConsts.LoginMethod.DNIE -> R.id.action_auth_webview_dest_to_dnie_dest
        }
        val bundle = bundleOf(
            MsspaAuthConsts.AUTH_ARG to (requireActivity() as BaseActivity).authEntity,
            ApiConstants.Arguments.ARG_AUTHORIZE to authorizeEntity
        )
        findNavController().navigate(action, bundle)
    }

    override fun loadLoginWebView(url: String) {
        auth_wv.loadUrl(url)
        showLoading()
    }

    override fun loadHelpWeb() {
        auth_wv.loadUrl(MsspaAuthConsts.HELP_LOGIN_URL)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            presenter.onAbortInitSession()
        }
    }

    //endregion

    //region EVENTS & METHODS

}