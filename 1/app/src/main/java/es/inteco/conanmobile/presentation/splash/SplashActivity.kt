package es.inteco.conanmobile.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import es.inteco.conanmobile.BuildConfig
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerActivityComponent
import es.inteco.conanmobile.di.module.ActivityModule
import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseActivity
import es.inteco.conanmobile.presentation.legal.LegalDialog
import es.inteco.conanmobile.presentation.main.MainActivity
import es.inteco.conanmobile.utils.Consts
import es.inteco.conanmobile.utils.Utils
import es.inteco.conanmobile.utils.Utils.Companion.getSHA
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * Splash activity
 *
 * @constructor Create empty Splash activity
 */
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(), SplashContract.View {

    companion object {
        const val LEGAL_DIALOG = "legal"
    }

    @Inject
    lateinit var presenter: SplashContract.Presenter

    override fun bindLayout() = R.layout.activity_splash

    override fun bindPresenter() = presenter

    override fun injectComponent() {
        DaggerActivityComponent.builder().applicationComponent(App.baseComponent)
            .activityModule(ActivityModule()).build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when (resources.configuration.locale.language) {
            "eu" -> setAppLocale("es")
            "gl" -> setAppLocale("es")
            "ca" -> setAppLocale("es")
        }
        super.onCreate(savedInstanceState)
        presenter.onCreate(idDevice = getSHA(Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)))
        val rootView = window.decorView.rootView
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            if (this.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                initButtons()
            }
        }
    }

    override fun checkInternetConnectivity() {
        if (Utils.checkInternet(this)) {
            presenter.onAvailableInternetConnectivity()
        } else {
            presenter.onUnavailableInternetConnectivity()
        }
    }

    private fun setAppLocale(localeCode: String) {
        val locale = Locale(localeCode.lowercase(), localeCode.lowercase())
        Locale.setDefault(locale)
        val config: Configuration = baseContext.resources.configuration
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }

    override fun navigateToHomeScreen(message: MessageEntity) {
        val navigateIntent = Intent(this, MainActivity::class.java)
        val bundle = bundleOf(Consts.ARG_CONFIGURATION to message)
        navigateIntent.putExtras(bundle)
        startActivity(navigateIntent)
        finish()
    }

    override fun showNoConfigurationScreen() {
        splash_iv.visibility = View.GONE
        no_internet_cv.visibility = View.GONE
        terms_cv.visibility = View.GONE
        no_configuration_cv.visibility = View.VISIBLE
    }

    override fun showTermsAndConditionsScreen() {
        splash_iv.visibility = View.GONE
        no_internet_cv.visibility = View.GONE
        terms_cv.visibility = View.VISIBLE
        documents_iv.setBackgroundResource(R.drawable.ic_documents)
        no_configuration_cv.visibility = View.GONE
    }

    override fun showSplashScreen() {
        splash_iv.visibility = View.VISIBLE
        no_internet_cv.visibility = View.GONE
        terms_cv.visibility = View.GONE
        no_configuration_cv.visibility = View.GONE
        version_tv.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    override fun showNoInternetScreen() {
        splash_iv.visibility = View.GONE
        no_internet_cv.visibility = View.VISIBLE
        terms_cv.visibility = View.GONE
        internet_iv.setBackgroundResource(R.drawable.ic_connection_fail)
        no_configuration_cv.visibility = View.GONE
    }

    override fun navigateToLegalScreen(message: MessageEntity) {
        runOnUiThread {
            val dialog = LegalDialog(message)
            supportFragmentManager.apply {
                dialog.show(this, LEGAL_DIALOG)
            }
        }
    }

    override fun initButtons() {
        accept_tv?.setOnClickListener {
            presenter.onAcceptButtonClicked()
        }
        text_tv?.setOnClickListener {
            presenter.onLegalClicked()
        }
        reject_tv?.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(-1)
        }
        understood_tv?.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(-1)
        }
        no_configuraion_accept_tv?.setOnClickListener {
            moveTaskToBack(true)
            exitProcess(-1)
        }
    }

    override fun showNoInternetConnectionDialog() {
        showWarningDialog(title = R.string.no_internet_tittle,
            message = R.string.no_internet_content,
            positiveText = R.string.accept,
            onAccept = {})
    }
}