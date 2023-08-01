package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import android.content.Context
import android.webkit.WebView
import com.google.gson.Gson
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import io.reactivex.Single

class ExecuteJavascriptUseCase(context: Context)  {

    private var webview: WebView
    private var eventsMap: Map<String, Int> = mapOf()
    private lateinit var function: String

    init {
        webview = WebView(context)
        webview.settings.javaScriptEnabled = true
    }

    fun buildUseCase(onSuccess : (Boolean) -> Unit, onError: (Throwable)-> Unit){
        try{
            webview.evaluateJavascript("events = ${convertObjectJSON()}") {
                webview.evaluateJavascript(function) {
                    onSuccess.invoke(it.toBoolean())
                }
            }
        } catch (e:Exception){
            onError.invoke(e)
        }
    }

    fun params(eventsMap: Map<String, Int>, function: String) =
        this.apply {
            this@ExecuteJavascriptUseCase.eventsMap = eventsMap
            this@ExecuteJavascriptUseCase.function = function
        }

    private fun convertObjectJSON(): String = Gson().toJson(eventsMap)

}