package es.juntadeandalucia.msspa.saludandalucia.presentation.wallet.detail

import android.net.Uri
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.WalletEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetSharedDynamicIconsUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class WalletDetailPresenter(
    private val getDynamicTitle: GetSharedDynamicIconsUseCase,
) : BasePresenter<WalletDetailContract.View>(), WalletDetailContract.Presenter {
    private var idType: String? = null
    private var idValue: String? = null

    override fun setupView(cert: WalletEntity) {
        view.sendEvent(Consts.Analytics.WALLET_DETAIL_SCREEN_ACCESS + cert.type.type)
        val token = if (cert.qr.contains(Consts.TOKEN_PARAMETER)) {
            Uri.parse(cert.qr).getQueryParameter(Consts.TOKEN_PARAMETER)
        } else {
            null
        }
        token?.let { decodeJWT(it) }
        val title = getTitle(cert.type.type)
        view.showCert(cert = cert, title = title, idType = idType, idValue = idValue)
    }

    private fun getTitle(certType: String): String {
        val dynamic = getDynamicTitle.param(Consts.ARG_DYNAMIC_ICONS).execute()
        var title = ""

        for (item in dynamic.children) {
            if (item.navigation.target == certType) {
                title = item.title.alt
                break
            }
        }
        return title
    }

    private fun decodeJWT(token: String) {
        val json = String(Base64.decode(token.split(".")[1], Base64.DEFAULT))
        val jsonObj = Gson().fromJson(json, JsonObject::class.java)
        idType = jsonObj.get(Consts.ID_TYPE_PARAMETER).asString
        idValue = jsonObj.get(Consts.ID_VALUE_PARAMETER).asString
    }

}