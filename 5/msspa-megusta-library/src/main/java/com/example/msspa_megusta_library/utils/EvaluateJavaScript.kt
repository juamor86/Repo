package com.example.msspa_megusta_library.utils

import android.content.Context
import android.webkit.WebView
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EvaluateJavaScript(context: Context) : WebView(context) {

    // you can even further customize the exact thread pool used here
    // by providing a particular dispatcher
    private val jsProcessingScope = CoroutineScope(CoroutineName("js-processing"))

    private val jsQueue = Channel<String>(BUFFERED)

    // this starts the loop right away but you can also put this in a method
    // to start it at a more appropriate moment
    init {
        jsProcessingScope.launch {
            for (script in jsQueue) {
                evaluateJs(script)
            }
        }
    }

    // you could also make this function non-suspend if necessary by calling
    // sendBlocking (or trySend depending on coroutines version)
    suspend fun queueEvaluateJavascript(script: String) {
        jsQueue.send(script)
    }

    private suspend fun evaluateJs(script: String) = suspendCoroutine<String> { cont ->
        evaluateJavascript(script) { result ->
            cont.resume(result)
        }
    }

    fun someCloseOrDisposeCallback() {
        jsProcessingScope.cancel()
    }
}