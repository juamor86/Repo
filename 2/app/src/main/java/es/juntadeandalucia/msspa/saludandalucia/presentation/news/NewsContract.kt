package es.juntadeandalucia.msspa.saludandalucia.presentation.news

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseContract

class NewsContract {

    interface View : BaseContract.View {
        fun setupNewsRecycler()
        fun showNews(it: List<NewsEntity>)
        fun openNewDetails(it: NewsEntity, itemView: android.view.View)
        fun hideRefreshing()
    }

    interface Presenter : BaseContract.Presenter {
        fun onResume()
        fun refreshNews()
        fun onNewItemClicked(it: NewsEntity, itemView: android.view.View)
    }
}
