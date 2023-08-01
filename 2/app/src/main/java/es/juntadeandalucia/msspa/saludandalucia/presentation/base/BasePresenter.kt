package es.juntadeandalucia.msspa.saludandalucia.presentation.base

import es.juntadeandalucia.msspa.saludandalucia.utils.Tracker
import retrofit2.HttpException
import java.net.HttpURLConnection

abstract class BasePresenter<V : BaseContract.View> : BaseContract.Presenter {

    protected lateinit var view: V

    override fun setViewContract(baseFragment: BaseContract.View) {
        view = baseFragment as V
    }

    override fun onViewCreated() {
        super.onViewCreated()
        getScreenNameTracking()?.apply { view.sendEvent(this) }
    }

    open fun getScreenNameTracking(): String? = null

    fun handleUnauthorizedException(exception: Throwable, action: (() -> Unit) = { view.showErrorDialog() }) {
       when (exception){
           is HttpException -> {
               if (exception.code() != HttpURLConnection.HTTP_UNAUTHORIZED) {
                   action.invoke()
               }
           }
       }
    }
}
