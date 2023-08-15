package com.example.msspa_megusta_library

import android.content.Context
import android.webkit.ValueCallback
import android.webkit.WebView
import com.example.msspa_megusta_library.data.entities.ServiceData
import com.example.msspa_megusta_library.di.component.DaggerAuthLibraryComponent
import com.example.msspa_megusta_library.di.module.LibraryModule
import com.example.msspa_megusta_library.domain.entity.LikeItEntity
import com.example.msspa_megusta_library.utils.EvaluateJavaScript
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

open class MeGustaManager constructor(private val context: Context) {
    private val webview: WebView

    init {
        val libraryComponent =
            DaggerAuthLibraryComponent.builder().libraryModule(LibraryModule(context))
                .build()
        libraryComponent.inject(this)
        webview = WebView(context)
    }

    @Inject
    internal lateinit var eventsManager: EventsManager

    fun getEventsData() {
        eventsManager.loadEvents()
    }

    fun launchJavascript(onsuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        webview.settings.javaScriptEnabled = true

        val mapa = mapOf("app_details" to 3)
        val json = Gson().toJson(mapa)

        try {
            webview.evaluateJavascript("events = $json") {
                webview.evaluateJavascript("events['app_details']>=3") { result ->
                    onsuccess.invoke(result)
                }
            }

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}