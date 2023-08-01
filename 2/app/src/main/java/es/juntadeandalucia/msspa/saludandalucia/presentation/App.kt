package es.juntadeandalucia.msspa.saludandalucia.presentation

import android.app.Application
import es.juntadeandalucia.msspa.saludandalucia.BuildConfig
import es.juntadeandalucia.msspa.saludandalucia.di.component.ApplicationComponent
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerApplicationComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.ApplicationModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.*
import es.juntadeandalucia.msspa.saludandalucia.domain.services.InitLoader
import es.juntadeandalucia.msspa.saludandalucia.presentation.preferences.notifications.SmsReceiver
import timber.log.Timber
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var loader: InitLoader

    @Inject
    lateinit var smsBroadcastReceiver : SmsReceiver

    companion object {
        lateinit var baseComponent: ApplicationComponent
        var quizSession: QuizSession? = null
        var session: Session =
            Session()
        var navBackPressed: NavBackPressed = NavBackPressed()
        var loginAdvicePressed: LoginAdvicePressed = LoginAdvicePressed()
        var launcherScreen: LauncherScreen = LauncherScreen()
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

        loader.load()

        /*registerReceiver(
                smsBroadcastReceiver,
        IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        )*/
    }
}
