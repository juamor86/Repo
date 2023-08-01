package es.juntadeandalucia.msspa.saludandalucia.utils

import android.content.Context
import android.content.Intent
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey
import timber.log.Timber

class HmsUpdate(private val onHmsUpdate: () -> Unit) : CheckUpdateCallBack {

    fun checkHMSUpdate(context: Context) {
        val client = JosApps.getAppUpdateClient(context)
        client.checkAppUpdate(context, this)
    }

    override fun onUpdateInfo(intent: Intent?) {
        val info = intent!!.getSerializableExtra(UpdateKey.INFO)
        if (info is ApkUpgradeInfo) {
            onHmsUpdate.invoke()
        }
    }

    override fun onMarketInstallInfo(intent: Intent?) {
        Timber.d("HMS onMarketInstallInfo not implemented.")
    }

    override fun onMarketStoreError(p0: Int) {
        Timber.d("HMS onMarketStoreError not implemented.")
    }

    override fun onUpdateStoreError(p0: Int) {
        Timber.d("HMS onUpdateStoreError not implemented.")
    }
}