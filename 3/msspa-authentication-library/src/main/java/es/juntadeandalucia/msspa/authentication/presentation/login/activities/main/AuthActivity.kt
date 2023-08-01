package es.juntadeandalucia.msspa.authentication.presentation.login.activities.main

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_BROADCAST_ACTION
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_BROADCAST_RESULT
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_BROADCAST_RESULT_ERROR
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_BROADCAST_RESULT_SUCCESS
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_BROADCAST_RESULT_WARNING
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_ENTITY_ARG
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_ERROR_ARG
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationBroadcastReceiver.Companion.MSSPA_AUTHENTICATION_WARNING_ARG
import es.juntadeandalucia.msspa.authentication.di.component.DaggerActivityComponent
import es.juntadeandalucia.msspa.authentication.di.module.ActivityModule
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*
import timber.log.Timber
import javax.inject.Inject

class AuthActivity : BaseActivity(), AuthContract.View {

    @Inject
    lateinit var presenter: AuthContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout(): Int = R.layout.activity_auth

    override fun bindAuthEntity(): MsspaAuthenticationEntity? =
            intent.extras?.get(MsspaAuthConsts.AUTH_ARG) as MsspaAuthenticationEntity?

    override fun bindConfig(): MsspaAuthenticationConfig =
            intent.extras?.get(MsspaAuthConsts.AUTH_CONFIG_ARG) as MsspaAuthenticationConfig

    override fun bindScope(): MsspaAuthenticationManager.Scope =
            intent.extras?.getSerializable(MsspaAuthConsts.LEVEL_ARG) as MsspaAuthenticationManager.Scope

    override fun injectComponent() {

        DaggerActivityComponent.builder()
                .authLibraryComponent(authLibraryComponent)
                .activityModule(ActivityModule())
                .build()
                .inject(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        findNavController(R.id.nav_host_fragment).popBackStack(R.id.auth_webview_dest, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init Timber library only for debug
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

//        result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            result.data?.extras?.apply {
//                if (result.resultCode == Activity.RESULT_OK) {
//                    setResultSuccess(get(Consts.RESULT_EXTRA) as MsspaAuthenticationEntity)
//                } else {
//                    setResultError(get(Consts.RESULT_ERROR_EXTRA) as MsspaAuthenticationError)
//                }
//            }
//        }
        val dest = intent.extras?.get(MsspaAuthConsts.FRAGMENT_DEST_ARG) as? Int
        presenter.onCreate(authConfig, scope, dest)
    }

    override fun configureView() {
        title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authConfig.logoToolbar?.let {
            toolbar_header_iv.setImageDrawable(
                    ResourcesCompat.getDrawable(
                            resources,
                            it,
                            null
                    )
            )
        }
        authConfig.colorToolbar?.let {
            supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                            ResourcesCompat.getColor(
                                    resources,
                                    it,
                                    null
                            )
                    )
            )
        }
    }

    //region - View methods
//    override fun showErrorDialog() {
//        showErrorDialog()
//    }

    override fun navigateToDest(dest: Int) {
        findNavController(R.id.nav_host_fragment).navigate(dest)
    }

    override fun showLoading() {
        runOnUiThread{
            loading_pb?.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        runOnUiThread {
            loading_pb?.visibility = View.GONE
        }
    }

    override fun setResultSuccess(entity: MsspaAuthenticationEntity) {
        val intent = Intent().apply {
            action = MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                    MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                    MSSPA_AUTHENTICATION_BROADCAST_RESULT_SUCCESS
            )
            putExtra(MSSPA_AUTHENTICATION_ENTITY_ARG, entity)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        finish()
    }

    override fun setResultError(
            errorType: MsspaAuthenticationError
    ) {
        val intent = Intent().apply {
            action = MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                    MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                    MSSPA_AUTHENTICATION_BROADCAST_RESULT_ERROR
            )
            putExtra(
                    MSSPA_AUTHENTICATION_ERROR_ARG,
                    errorType as Parcelable
            )
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        finish()
    }

    override fun setResultWarning(warningType: MsspaAuthenticationWarning) {
        val intent = Intent().apply {
            action = MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                MSSPA_AUTHENTICATION_BROADCAST_RESULT_WARNING
            )
            putExtra(
                MSSPA_AUTHENTICATION_WARNING_ARG,
                warningType as Parcelable
            )
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        finish()
    }

    override fun showError(error: MsspaAuthenticationError) {
        var errorStrings = MsspaAuthenticationError.GENERIC_ERROR
        for (errorRes in authConfig.errors) {
            if (errorRes.error == error) {
                errorStrings = errorRes.error
                break
            }
        }
        showErrorDialog(
                layout = authConfig.errorLayout,
                title = errorStrings.errorTitle,
                message = errorStrings.errorMessage
        )
    }

    override fun showDialog(title: Int?, onAccept: (() -> Unit)?, onCancel: (() -> Unit)?) {
        showInfoDialog(
            title = title,
            onAccept = onAccept,
            onCancel = onCancel
        )
    }

    override fun showConfirm(
        title: Int?,
        message: Int?,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        showConfirmDialog(
            layout = authConfig.warningLayout,
            title = title,
            message = message,
            onAccept = onAccept,
            onCancel = onCancel
        )
    }

    override fun showError(error: Int) {
        showErrorDialog(
            layout = authConfig.errorLayout,
            title = error
        )
    }

    override fun showWarning(warning: MsspaAuthenticationWarning, onAccept: (() -> Unit)?, onCancel: (() -> Unit)?) {
        var warningType = MsspaAuthenticationWarning.GENERIC_WARNING
        for (warningRes in authConfig.warnings) {
            if (warningRes.warning == warning) {
                warningType = warningRes.warning
                break
            }
        }
        showInfoDialog(
                layout = authConfig.warningLayout,
                title = warningType.warningTitle,
                message = warningType.warningMessage,
                onAccept = onAccept,
                onCancel = onCancel
        )
    }

    //endregion

    override fun navigateToBack() {
        setResult(Activity.RESULT_OK)
        onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val currentFragment = findNavController(R.id.nav_host_fragment).currentDestination?.id
        if(item.getItemId() == android.R.id.home) {
            presenter.checkBackPressed()
            return true
        }else if(currentFragment == R.id.pin_dest) {
            presenter.onNavPressedBack(NavBackPressed.NavBackType.PIN_SCREEN)
            return true
        }else if(currentFragment == R.id.auth_webview_dest) {
            presenter.onNavPressedBack(NavBackPressed.NavBackType.WOA_SCREEN)
            return true
        }
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment)) || super.onOptionsItemSelected(item)
    }

}
