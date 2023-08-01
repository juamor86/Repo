package es.juntadeandalucia.msspa.saludandalucia.presentation.news.details

import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class NewDetailsPresenter : BasePresenter<NewDetailsContract.View>(), NewDetailsContract.Presenter {

    private lateinit var newsEntity: NewsEntity

    override fun setupView(newsEntity: NewsEntity) {
        this.newsEntity = newsEntity
        view.apply {
            hideToolbar()
            setupView()
            showNew(newsEntity)
        }
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.NEWS_DETAIL_SCREEN_ACCESS

    override fun onResume() {
        view.hideToolbar()
    }

    override fun onStop() {
        view.showToolbar()
    }

    override fun onBackPressed() {
        view.goBack()
    }

    override fun onShareNewClicked() {
        val content =
            "${this.newsEntity.title}\n${this.newsEntity.subtitle}\n${this.newsEntity.description}"
        view.shareNew(contentToShare = content)
    }

    override fun unsubscribe() {
        // Does nothing
    }
}
