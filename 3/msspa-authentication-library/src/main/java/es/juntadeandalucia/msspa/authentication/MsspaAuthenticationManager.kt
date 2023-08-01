package es.juntadeandalucia.msspa.authentication

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import es.juntadeandalucia.msspa.authentication.di.component.DaggerNetComponent
import es.juntadeandalucia.msspa.authentication.di.component.NetComponent
import es.juntadeandalucia.msspa.authentication.di.module.NetModule
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.presentation.login.activities.main.AuthActivity
import javax.inject.Inject

open class MsspaAuthenticationManager constructor(
    private val context: Context,  private val msspaAuthenticationConfig: MsspaAuthenticationConfig
) {
    init {
        val netComponent: NetComponent =
            DaggerNetComponent.builder().netModule(NetModule(msspaAuthenticationConfig)).build()
        netComponent.inject(this)

        val authenticationBroadcastReceiver =
            MsspaAuthenticationBroadcastReceiver(context as MsspaAuthenticationBroadcastReceiver.MsspaAuthenticationCallback)

        val intentFilter = IntentFilter(MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_ACTION)
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(authenticationBroadcastReceiver, intentFilter)
    }

    @Inject
    lateinit var msspaTokenManager: MsspaTokenManager

    enum class Scope(val level: String) {
        EMPTY(""),
        LEVEL_0("ciudadano"),
        LEVEL_1("conf/N"),
        LEVEL_2("conf/R"),
        LEVEL_3("conf/V");

        companion object {
            fun getScope(level: String?): Scope {
                return level?.let {
                    for (levelAccess in values().reversedArray()) {
                        if (level.contains(levelAccess.level)) {
                            return levelAccess
                        }
                    }
                    return EMPTY
                } ?: EMPTY
            }
        }
    }

    fun launchVerificationQRLogin(mSSPAAuthEntity: MsspaAuthenticationEntity) {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(MsspaAuthConsts.AUTH_ARG, mSSPAAuthEntity)
        intent.putExtra(MsspaAuthConsts.AUTH_CONFIG_ARG, msspaAuthenticationConfig)
        intent.putExtra(MsspaAuthConsts.LEVEL_ARG, Scope.EMPTY)
        intent.putExtra(MsspaAuthConsts.FRAGMENT_DEST_ARG, R.id.verification_qr_login_dest)
        context.startActivity(intent)
    }

    fun launch(
        mSSPAAuthEntity: MsspaAuthenticationEntity? = null,
        requiredScope: String = ""
    ) {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(MsspaAuthConsts.AUTH_CONFIG_ARG, msspaAuthenticationConfig)
        intent.putExtra(MsspaAuthConsts.LEVEL_ARG, Scope.getScope(requiredScope))
        mSSPAAuthEntity?.run {
            intent.putExtra(MsspaAuthConsts.AUTH_ARG, this)
        }
        context.startActivity(intent)
    }

    fun launchOtherAuthMethods() {
        val intent = Intent(context, AuthActivity::class.java)
        intent.putExtra(MsspaAuthConsts.AUTH_CONFIG_ARG, msspaAuthenticationConfig)
        intent.putExtra(MsspaAuthConsts.LEVEL_ARG, Scope.EMPTY)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        context.startActivity(intent)
    }
}
