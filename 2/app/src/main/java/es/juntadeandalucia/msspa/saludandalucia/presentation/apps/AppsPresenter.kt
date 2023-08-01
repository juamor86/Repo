package es.juntadeandalucia.msspa.saludandalucia.presentation.apps

import android.content.Context
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetAppsUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import timber.log.Timber
import java.util.*

class AppsPresenter(private val getAppsUseCase: GetAppsUseCase) : BasePresenter<AppsContract.View>(),
    AppsContract.Presenter {

    private lateinit var apps: List<AppEntity>

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.apply {
            setupSearchView()
            initializeAppsRecycler()
        }
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.APP_SCREEN_ACCESS

    override fun onResume(isHuawei : Boolean) {
        loadApps(isHuawei = isHuawei)
    }

    override fun refreshApps(isHuawei : Boolean) {
        loadApps(showLoading = false, isRefreshing = true, isHuawei = isHuawei)
    }

    private fun loadApps(
        showLoading: Boolean = true,
        isRefreshing: Boolean = false,
        isHuawei: Boolean
    ) {
        if (showLoading) {
            view.showLoading()
        }
        getAppsUseCase.param(isHuawei).execute(
            onSuccess = { apps ->
                this.apps = apps
                view.apply {
                    if (showLoading) {
                        hideLoading()
                    }

                    if (isRefreshing) {
                        hideRefreshing()
                    }

                    if (apps.isNotEmpty()) {
                        showApps(apps)
                    } else {
                        showEmptyView()
                    }
                }
            },
            onError = {
                Timber.e(it)
                view.apply {
                    if (showLoading) {
                        hideLoading()
                    }

                    if (isRefreshing) {
                        hideRefreshing()
                    }
                    showErrorView()
                    when (it) {
                        is TooManyRequestException -> showTooManyRequestDialog()
                    }
                }
            }
        )
    }

    override fun onAppClicked(app: AppEntity, itemView: View) {
        view.showAppDetails(app, itemView)
    }

    /**
    Check if an app is installed and open the app or, conversely, show the Market to install that app.
     */
    override fun onButtonPressed(context: Context, app: AppEntity) {
        if (Utils.checkAppInstalled(context.applicationContext!!, app.packageName)) {
            view.openApp(app)
        } else {
            view.downloadApp(app)
        }
    }

    override fun unsubscribe() {
        getAppsUseCase.clear()
    }

    override fun filterList(text: String) {
        when {
            text.isNotBlank() -> {
                val filteredApps = apps.filter {
                    it.name.toLowerCase(Locale.getDefault()).contains(text.toLowerCase(Locale.getDefault()))
                }
                if (filteredApps.isNotEmpty()) {
                    view.showApps(filteredApps)
                } else {
                    view.showNoMatchAppsView()
                }
            }
            else -> view.showApps(apps)
        }
    }
}
