package es.inteco.conanmobile.presentation.analysis.detail

import android.content.Context
import es.inteco.conanmobile.domain.entities.NetworkEntity
import es.inteco.conanmobile.presentation.base.BasePresenter
import es.inteco.conanmobile.utils.ApplicationPackageUtils
import es.inteco.conanmobile.utils.Consts
import es.inteco.conanmobile.utils.Utils

/**
 * Detail presenter
 *
 * @constructor Create empty Detail presenter
 */
class DetailPresenter :
    BasePresenter<DetailContract.View>(), DetailContract.Presenter {

    override fun onCreateView(title: String?, detailData: MutableList<NetworkEntity>) {
        view.fillToolbarTittle(title)
        view.fillSubTitle(title)
        view.initButtons()
        buildNetworkStringList(detailData)
    }

    private fun buildNetworkStringList(networkEntityList: MutableList<NetworkEntity>){
        val list: MutableList<String> = mutableListOf()
        networkEntityList.forEach { networkEntity ->
            list.add(networkEntity.name)
        }
        if(list.isNotEmpty()) {
            list.sort()
        }
        view.refillReciclerView(list)
    }

    override fun goToDeviceConfiguration() {
        view.navitateToWifiSettings()
    }

    override fun lunchWhatsapp(ct: Context) {
        ct.applicationContext?.let { applicationContext ->
            ApplicationPackageUtils.isAppAvailable(applicationContext, Consts.WHATSAPP_PACKAGE_NAME).let { isAppAvailable ->
                if (isAppAvailable) {
                    Utils.goToWhatsapp(ct)
                } else {
                    view.showWarningIntentWhatsapp()
                }
            }
        }
    }

    override fun onNavigateToOSITipsClicked() {
        view.navigateToOSITips()
    }
}