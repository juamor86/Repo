package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.SessionBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice.GetAdviceTypesUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils.Companion.getAdviceFromTypeCriteria
import retrofit2.HttpException
import timber.log.Timber
import java.net.HttpURLConnection

class NewAdviceTypePresenter(
    private val getAdviceTypesUseCase: GetAdviceTypesUseCase,
    private val sessionBus: SessionBus
) : BasePresenter<NewAdviceTypeContract.View>(), NewAdviceTypeContract.Presenter {

    //region VARIABLES
    private var nuhsa: String? = ""
    private lateinit var advicesShared: List<AdviceEntity>
    private var phoneNumber: String? = ""
    //endregion

    override fun getScreenNameTracking(): String = Consts.Analytics.AVISAS_TYPE_HOME_ACCESS

    //region INITIALIZATION & OVERRIDES
    override fun onCreate(nuhsa: String?, advices: List<AdviceEntity>, phoneNumber: String?) {
        this.nuhsa = nuhsa
        this.advicesShared = advices
        this.phoneNumber = phoneNumber
        view.setupAdapter(advicesShared)
    }

    override fun onViewCreated() {
        super<BasePresenter>.onViewCreated()
        view.setupAdviceTypeRecycler()
        loadAdvices()
    }

    override fun onNotificationItemClick(adviceTypeResource: AdviceTypeResource, itemView: View) {
        nuhsa?.let { nuhsa ->
            val advice = (getAdviceFromTypeCriteria(adviceTypeResource.id, advicesShared))
            if (advice != null) {
                view.navigateToDetail(nuhsa, advice, phoneNumber ?: "")
            } else {
                view.navigateToNewAdvice(nuhsa, adviceTypeResource, phoneNumber ?: "")
            }
        }
    }

    override fun onNotificationItemRemoved(it: AdviceTypeResource) {}

    override fun unsubscribe() {
        getAdviceTypesUseCase.clear()
    }
    //endregion

    //region METHODS
    private fun loadAdvices() {
        view.showLoadingBlocking()
        getAdviceTypesUseCase.execute(onSuccess = { adviceTypes ->
            view.apply {
                hideLoading()
                showAdviceTypes(adviceTypes.entry)
            }
        }, onError = { error ->
            view.hideLoading()
            Timber.e(error)
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
        })
    }
    //endregion

}