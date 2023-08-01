package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeEntry
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NewAdviceTypeContract {

    interface View : BaseContract.View {
        fun setupAdapter( advices: List<AdviceEntity>)
        fun setupAdviceTypeRecycler()
        fun showAdviceTypes(adviceTypes: List<AdviceTypeEntry>)
        fun navigateToNewAdvice(nuhsa: String, adviceTypeResource: AdviceTypeResource, phoneNumber: String)
        fun navigateToDetail(nuhsa: String, advice: AdviceEntity, phoneNumber: String)
    }

    interface Presenter : BaseContract.Presenter {
        fun onCreate(nuhsa: String?, advices: List<AdviceEntity>, phoneNumber: String?)
        fun onNotificationItemClick(adviceTypeResource: AdviceTypeResource, itemView: android.view.View)
        fun onNotificationItemRemoved(it: AdviceTypeResource)
    }
}