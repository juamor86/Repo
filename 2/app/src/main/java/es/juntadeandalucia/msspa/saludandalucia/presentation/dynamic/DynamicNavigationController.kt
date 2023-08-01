package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic

import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.DynamicUIBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.NavigationEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNotificationsPhoneNumberUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SaveSharedDynamicIconsUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetWalletIsActiveUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicConsts.Nav.DEST_DYNAMIC_QUIZZES_QUESTIONAIRES
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_DYN_QUEST_AVAILIBILITY
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.ARG_QUIZ_ID
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.QUIZ_ID_PARAM
import timber.log.Timber

class DynamicNavigationController(
    val dynamicUIBus: DynamicUIBus,
    private val saveSharedDynamicIconsUseCase: SaveSharedDynamicIconsUseCase,
    private val setWalletIsActiveUseCase: SetWalletIsActiveUseCase,
    val sessionBus: SessionBus,
    private val getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase
) {
    private val dynamicScreensMap: HashMap<String, DynamicScreenEntity> = HashMap()
    private var checkedFileValue: Boolean = false

    init {
        getDynamicScreen()
    }

    private fun getDynamicScreen(strategy: Strategy = Strategy.NETWORK) {
        dynamicUIBus.getScreen(
            onSuccess = {
                processDynamicScreens(it)
            }, onError = {
                Timber.e(it)
                if (checkedFileValue) {
                    getDynamicScreen(Strategy.MOCK)
                } else {
                    checkedFileValue = true
                    getDynamicScreen(Strategy.FILE)
                }
            }, strategy = strategy
        )
    }

    private fun processDynamicScreens(dynamicSectionEntity: DynamicSectionEntity) {
        var isWalletActivated: Boolean = false
        for (screen in dynamicSectionEntity.screens) {
            if (screen.id == DynamicConsts.Nav.WALLET) {
                saveData(screen)
                isWalletActivated = true
            }
            dynamicScreensMap.put(screen.id, screen)
        }
        setWalletIsActiveUseCase.param(isWalletActivated).execute(
            onComplete = {
                Timber.d("Wallet is dynamically activated")
            },
            onError = {
                Timber.e(it)
            }
        )
    }

    lateinit var navigator: DynamicNavigator

    fun navigateTo(dest: NavigationEntity) {
        if(isNavigationAdvice(dest)){
            getNotificationsPhoneNumberUseCase.execute(
                onSuccess = { phone ->
                    if(phone.isEmpty()){
                        navigator.informNotificationsNotEnabled(dest)
                    }else{
                        performNavigateTo(dest)
                    }
                },
                onError = {
                    Timber.e("Error checking if notifications are enabled")
                }
            )
        }else{
            performNavigateTo(dest)
        }
    }

    private fun performNavigateTo(destination:NavigationEntity){
        if (validScopeToDestination(destination)) {
            when (destination.type) {
                DynamicConsts.Nav.APP -> navigateToAppSection(destination)
                DynamicConsts.Nav.APP_NATIVE -> navigateToAppNativeSection(destination)
                DynamicConsts.Nav.WEBVIEW -> navigateToWebview(destination)
                DynamicConsts.Nav.EXTERNAL -> navigateToExternal(destination)
                DynamicConsts.Nav.WEBVIEW_SESSION -> navigateToWebview(destination)
                else -> navigator.informSectionNotAvailable()
            }
        } else {
            navigator.higherAccessRequired(destination)
        }
    }


    private fun validScopeToDestination(dest: NavigationEntity): Boolean =
        dest.level?.let {
            sessionBus.session.msspaAuthenticationEntity.isGrantedAccessToScope(it)
        } ?: false

    private fun navigateToAppSection(destination: NavigationEntity) {
        val uri = Uri.parse(destination.target)
        val target = uri.getQueryParameter(DynamicConsts.Nav.TARGET)
        val accessLevel = uri.getQueryParameter(DynamicConsts.Nav.ACCESS_LEVEL)
        target?.apply {
            if (target == DynamicConsts.Nav.DYNAMIC_SCREEN) {
                val screenId = uri.getQueryParameter(DynamicConsts.Nav.SCREEN)
                val screen = dynamicScreensMap[screenId]
                screen?.let { navigator.navigateToDynamicScreen(it) }
            } else {
                val dest = DynamicConsts.Nav.getAppSectionDest(this)
                dest?.let { it ->
                    val bundleList = bundleOf()
                    dynamicScreensMap[target]?.let { dynamicScreen ->
                        bundleList.putParcelable(Consts.ARG_DYNAMIC_LAYOUT, dynamicScreen)
                        accessLevel.let { level ->
                            bundleList.putString(
                                Consts.ARG_ACCESS_LEVEL,
                                level
                            )
                        }
                    }

                    if (target == DynamicConsts.Nav.DEST_GENERIC_QUESTIONAIRES.first) {
                        bundleList.putParcelable(
                            ARG_DYN_QUEST_AVAILIBILITY,
                            dynamicScreensMap[ARG_DYN_QUEST_AVAILIBILITY]
                        )
                    }

                    destination.bundle?.let { bundle ->
                        bundleList.putAll(bundle)
                    }

                    navigator.navigateToSection(it, bundleList)
                }
                    ?: apply { navigator.informSectionNotAvailable() }
            }
        }
    }

    private fun navigateToAppNativeSection(destination: NavigationEntity) {
        val dest = DynamicConsts.Nav.getAppSectionDest(destination.target)
        dest?.let {
            val bundleList = bundleOf()
            destination.level?.let { level ->
                bundleList.putString(
                    Consts.ARG_ACCESS_LEVEL,
                    level
                )
            }
            destination.bundle?.let { bundle ->
                bundleList.putAll(bundle)
            }
            navigator.navigateToSection(dest, bundleList)
        } ?: apply { navigator.informSectionNotAvailable() }
    }


    private fun navigateToWebview(dest: NavigationEntity) {
        if (dest.target.contains(DynamicConsts.Nav.CLICSALUD)) {
            navigator.navigateToClicSaludWebview(dest)
        } else {
            navigator.navigateToWebview(dest.target)
        }
    }

    private fun navigateToExternal(dest: NavigationEntity) {
        navigator.navigateToExternal(dest.target)
    }

    private fun saveData(screen: DynamicScreenEntity) {
        saveSharedDynamicIconsUseCase.params(
            data = screen,
            key = Consts.ARG_DYNAMIC_ICONS
        ).execute(
            onComplete = { Timber.i("Data save successfully") },
            onError = { Timber.e(it) }
        )
    }

    private fun isNavigationAdvice(destination: NavigationEntity): Boolean {
        val uri = Uri.parse(destination.target)
        val target = uri.getQueryParameter(DynamicConsts.Nav.TARGET)
        return target == DynamicConsts.Nav.ADVICE
    }
}
