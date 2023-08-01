package es.juntadeandalucia.msspa.saludandalucia.presentation.advices

import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.LoginAdvicePressedBus
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.SetFirstAccessUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.*
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.*
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class AdvicesPresenter(
    private val getAdvicesUseCase: GetAdvicesUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val getAdvicesReceivedUseCase: GetAdvicesReceivedUseCase,
    private val sessionBus: SessionBus,
    private val getFirstAccess: GetFirstAccessUseCase,
    private val saveFirstAccess: SetFirstAccessUseCase,
    private val getAdviceCatalogTypeNetworkUseCase: GetAdviceCatalogTypeNetUseCase,
    private val getAdviceCatalogTypePreferencesUseCase: GetAdviceCatalogTypePrefUseCase,
    private val saveAdviceTypeCatalogPreferencesUseCase: SaveAdviceTypeCatalogUseCase,
    private val isAdviceCatalogTypeUserUseCase: IsAdviceCatalogTypeUserUseCase,
    private val removeAdviceCatalogTypeUseCase: RemoveAdviceCatalogTypeUseCase,
    private val getNotificationsPhoneNumberUseCase: GetNotificationsPhoneNumberUseCase,
    private val loginAdvicePressedBus: LoginAdvicePressedBus
) : BasePresenter<AdvicesContract.View>(), AdvicesContract.Presenter {

    //region VARIABLES
    var advicesType: AdviceTypes = AdviceTypes.CREATED
    private lateinit var advices: List<AdviceEntity>
    private var advicesReceived: List<AdviceEntity> = listOf()
    private lateinit var userlogged: MsspaAuthenticationUserEntity
    private var phoneNumber: String = ""
    private lateinit var adviceCatalogList: List<AdviceCatalogTypeEntity>
    private var pendingNavigationDestination = false

    override fun getScreenNameTracking(): String = Consts.Analytics.AVISAS_HOME_ACCESS

    //endregion

    //region INITIALIZATION & OVERRIDES
    override fun onCreate(pendingNavDestination: Boolean) {
        pendingNavigationDestination = pendingNavDestination
        userlogged = getUserSessionUseCase.execute()?.msspaAuthenticationEntity?.msspaAuthenticationUser!!
    }

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
        getNotificationsPhoneNumberUseCase.execute(
            onSuccess = { phone ->
                phoneNumber = phone
                initialiseAdvice()
                if (!pendingNavigationDestination) {
                    checkSettingAdvice()
                }
            },
            onError = {
                Timber.e("Error checking if notifications are enabled")
            }
        )

        with(loginAdvicePressedBus) {
            execute(onNext = {
                checkSettingAdvice()
            }, onError = {
                Timber.e(it)
            })
        }
    }

    private fun initialiseAdvice(){
        view.apply {
            changeStatusTypeTab(advicesType)
            setupTypeTabButtons()
            setupAdapter()
        }
    }

    private fun checkSettingAdvice() {
        if (isFirstTimeAvisas()) {
            view.showAdviceDialog()
            //TODO check permissisions again
        } /*else {
            view.checkContactsPermissions()
        }*/
    }

    override fun onResume() {
        loadAdvices()
    }

    override fun onTabButtonPressed(type: AdviceTypes) {
        this.advicesType = type
        filterAdvices()
    }

    override fun onNotificationItemClick(advice: AdviceEntity) {
        view.navigateToDetail(
            nuhsa = userlogged.nuhsa,
            advice = advice,
            phoneNumber = phoneNumber
        )
    }

    override fun onNotificationItemRemoved(advice: AdviceEntity) {}

    override fun onNewAdvicePressed() {
        view.navigateToType(
            nuhsa = userlogged.nuhsa,
            phoneNumber = phoneNumber,
            advices = advices
        )
    }

    override fun loadAdvices() {
        view.showLoadingBlocking()
        if(!isAdviceCatalogTypeUserUseCase.execute()){
            getAdviceCatalogTypeNetworkUseCase.execute(onSuccess = { adviceCatalogTypeEntity ->
                adviceCatalogList = adviceCatalogTypeEntity
                saveAdviceTypeCatalogPreferencesUseCase.params(adviceCatalogList)
                    .execute(onComplete = {
                        loadAdviceAndReceived()
                    }, onError = { Timber.e(it) })

            }, onError = { error ->
                handleError(error)
            })
        }else{
            getAdviceCatalogTypePreferencesUseCase.execute(onSuccess = { catalogList->
                adviceCatalogList = catalogList.toMutableList()
                loadAdviceAndReceived()
            }, onError = { error ->
                handleError(error)
            })
        }
    }

    private fun loadAdviceAndReceived(){
        getAdvicesUseCase.params(nuhsa = userlogged.nuhsa, adviceCatalogList).execute(
            onSuccess = { advices ->
                this.advices = advices
                //loadAdviceReceived()
                //TODO remove this after release
                val screenStatus = this@AdvicesPresenter.advices.isNotEmpty() /*|| !this@AdvicesPresenter.advicesReceived.isNullOrEmpty()*/
                view.setupScreenStatus(!screenStatus)
                filterAdvices()
            }, onError = { error ->
                handleError(error)
            })
    }

    private fun loadAdviceReceived(){
        getAdvicesReceivedUseCase.params(phone = "${Consts.PREFIX_CONTACT}${phoneNumber}", adviceCatalogList).execute(
            onSuccess = { advicesReceived ->
                this.advicesReceived = advicesReceived
                val screenStatus = this@AdvicesPresenter.advices.isNotEmpty() /*|| !this@AdvicesPresenter.advicesReceived.isNullOrEmpty()*/
                view.setupScreenStatus(!screenStatus)
                filterAdvices()
            }, onError = { error ->
                handleError(error)
            })
    }

    private fun filterAdvices() {
        view.apply {
            changeStatusTypeTab(advicesType)
            refillAdvicesList(
                advices = advices,
                advicesReceived = advicesReceived,
                advicesType = advicesType
            )
            hideLoading()
        }
    }

    private fun handleError(error: Throwable){
        Timber.e(error)
        view.apply {
            hideLoading()
            setupScreenStatus(true)
        }
        showOnError(error)
    }

    private fun showOnError(error: Throwable) {
        when (error) {
            is HttpException -> {
                when (error.code()) {
                    HttpURLConnection.HTTP_FORBIDDEN , HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        sessionBus.onUnauthorizedEvent()
                    }
                    else -> sessionBus.showErrorAndHome()
                }
            }
            else -> {
                sessionBus.showErrorAndHome()
            }
        }
    }

    override fun isFirstTimeAvisas(): Boolean {
        return getFirstAccess.param(Consts.PREF_FIRST_ACCESS_ADVICES).execute()
    }

    override fun saveFirstOpenToAdvice() {
        //TODO check permissions again
        saveFirstAccess.param(Consts.PREF_FIRST_ACCESS_ADVICES).execute(
            onComplete = { /*view.checkContactsPermissions()*/},
            onError = { Timber.e(it) }
        )
    }

    override fun removeAdviceCatalogTypePreferences() {
        removeAdviceCatalogTypeUseCase.execute(onComplete = {}, onError = { Timber.e(it) })
    }
    //TODO reset contacts
    /*override fun permissionNotGranted() {
        if (getFirstAccess.param(Consts.PREF_FIRST_TIME_CONTACT_PERMISSION_REQUEST).execute()) {
            saveFirstAccess.param(Consts.PREF_FIRST_TIME_CONTACT_PERMISSION_REQUEST)
                .execute(onComplete = {
                    view.requestPermission()
                }, onError = {
                    Timber.e(it)
                })
        } else {
            view.requestPermission()
        }
    }*/

    override fun unsubscribe() {
        super<BasePresenter>.unsubscribe()
        getAdvicesUseCase.clear()
        getAdvicesReceivedUseCase.clear()
        getAdviceCatalogTypeNetworkUseCase.clear()
        saveAdviceTypeCatalogPreferencesUseCase.clear()
        getAdviceCatalogTypePreferencesUseCase.clear()
        saveFirstAccess.clear()
    }
}