package es.juntadeandalucia.msspa.saludandalucia.presentation.news.details

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NewDetailsContract {
    interface View : BaseContract.View {
        fun setupView()
        fun hideToolbar()
        fun showToolbar()
        fun showNew(newsEntity: NewsEntity)
        fun shareNew(contentToShare: String)
        fun goBack()
    }

    interface Presenter : BaseContract.Presenter {
        fun onResume()
        fun onStop()
        fun onShareNewClicked()
        fun onBackPressed()
        fun setupView(newsEntity: NewsEntity)
    }
}
