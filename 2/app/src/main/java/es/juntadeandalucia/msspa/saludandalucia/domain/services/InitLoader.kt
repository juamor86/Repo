package es.juntadeandalucia.msspa.saludandalucia.domain.services

import es.juntadeandalucia.msspa.saludandalucia.domain.bus.DynamicUIBus
import javax.inject.Inject
import timber.log.Timber

class InitLoader @Inject constructor(val dynamicUIBus: DynamicUIBus) {
    fun load() {
        dynamicUIBus.getMenu(
            onSuccess = {},
            onError = {
                Timber.e(it)
            })
        dynamicUIBus.getHome(
            onSuccess = {},
            onError = {
                Timber.e(it)
            })
        dynamicUIBus.getScreen(
            onSuccess = {},
            onError = {
                Timber.e(it)
            })
    }
}
