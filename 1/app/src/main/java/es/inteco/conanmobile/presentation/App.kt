package es.inteco.conanmobile.presentation

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import es.inteco.conanmobile.BuildConfig
import es.inteco.conanmobile.di.component.ApplicationComponent
import es.inteco.conanmobile.di.component.DaggerApplicationComponent
import es.inteco.conanmobile.di.module.ApplicationModule
import es.inteco.conanmobile.domain.base.bus.LifecycleBus
import timber.log.Timber
import javax.inject.Inject

class App : Application(), LifecycleObserver {

    @Inject
    lateinit var lifecycleBus: LifecycleBus

    companion object {
        lateinit var baseComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        // Init Timber library only for debug
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        baseComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
            .apply { inject(this@App) }

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        lifecycleBus.publish(LifecycleBus.LifecycleState.BACKGROUND)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        // App in foreground
        lifecycleBus.publish(LifecycleBus.LifecycleState.FOREGROUND)
    }
}