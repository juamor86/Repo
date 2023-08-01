package es.juntadeandalucia.msspa.saludandalucia.presentation.advices

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypes
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class AdvicesContract {

    interface View : BaseContract.View {
        fun showConfirmDialog()
        fun setupAdapter()
        fun setupTypeTabButtons()
        fun setupScreenStatus(showEmptyScreen: Boolean)
        fun changeStatusTypeTab(type: AdviceTypes)
        fun refillAdvicesList(advices: List<AdviceEntity>, advicesReceived: List<AdviceEntity>, advicesType: AdviceTypes)
        fun navigateToDetail(nuhsa: String, advice: AdviceEntity, phoneNumber: String)
        fun navigateToType(nuhsa: String, phoneNumber: String, advices: List<AdviceEntity>)
        fun showAdviceDialog()
        //TODO reset contacts
       // fun checkContactsPermissions()
        //fun requestPermission()
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(pendingNavDestination: Boolean)
        fun onResume()
        fun onTabButtonPressed(type: AdviceTypes)
        fun onNotificationItemClick(advice: AdviceEntity)
        fun onNotificationItemRemoved(advice: AdviceEntity)
        fun onNewAdvicePressed()
        fun loadAdvices()
        fun isFirstTimeAvisas(): Boolean
        fun saveFirstOpenToAdvice()
        fun removeAdviceCatalogTypePreferences()
        //fun permissionNotGranted()
    }
}