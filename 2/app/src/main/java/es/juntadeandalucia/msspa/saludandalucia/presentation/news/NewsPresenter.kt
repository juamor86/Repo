package es.juntadeandalucia.msspa.saludandalucia.presentation.news

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.data.utils.exceptions.TooManyRequestException
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.usecases.GetNewsUseCase
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import timber.log.Timber

class NewsPresenter(private val getNewsUseCase: GetNewsUseCase) :
    BasePresenter<NewsContract.View>(),
    NewsContract.Presenter {

    override fun onViewCreated() {
        //TODO: Check the saveArguments flow, it may be related to null errors and fragment life cycle
        super<BasePresenter>.onViewCreated()
        view.setupNewsRecycler()
    }

    override fun getScreenNameTracking(): String? = Consts.Analytics.NEWS_SCREEN_ACCESS

    override fun onResume() {
        loadNews()
    }

    override fun refreshNews() {
        loadNews(showLoading = false, isRefreshing = true)
    }

    private fun loadNews(showLoading: Boolean = true, isRefreshing: Boolean = false) {
        if (showLoading) {
            view.showLoading()
        }
        getNewsUseCase.execute(
            onSuccess = { news ->
                view.apply {
                    if (showLoading) {
                        hideLoading()
                    }

                    if (isRefreshing) {
                        hideRefreshing()
                    }

                    if (news.isEmpty()) {
                        showEmptyView()
                    } else {
                        showNews(news)
                    }
                }
            },
            onError = {
                Timber.e(it)
                view.apply {
                    if (showLoading) {
                        hideLoading()
                    }

                    if (isRefreshing) {
                        hideRefreshing()
                    }
                    showErrorView()
                    when (it) {
                        is TooManyRequestException -> showTooManyRequestDialog()
                    }
                }
            }
        )
    }

    override fun onNewItemClicked(it: NewsEntity, itemView: View) {
        view.apply {
            sendEvent(Consts.Analytics.NEWS_DETAIL_SCREEN_ACCESS)
            openNewDetails(it, itemView)
        }
    }

    override fun unsubscribe() {
        getNewsUseCase.clear()
    }
}
