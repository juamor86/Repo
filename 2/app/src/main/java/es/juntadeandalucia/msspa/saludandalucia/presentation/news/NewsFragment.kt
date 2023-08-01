package es.juntadeandalucia.msspa.saludandalucia.presentation.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.news.adapter.NewsAdapter
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseFragment(), NewsContract.View {

    @Inject
    lateinit var presenter: NewsContract.Presenter

    lateinit var adapter: NewsAdapter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_news

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let { ctx ->
            sharedElementReturnTransition =
                TransitionInflater.from(ctx).inflateTransition(android.R.transition.move)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun openNewDetails(newsEntity: NewsEntity, itemView: View) {
        if (newsEntity.operationMode == Consts.OPERATION_MODE_APP) {
            val bundle = bundleOf(Consts.ARGUMENT_NEWS_ENTITY to newsEntity)
            findNavController().navigate(R.id.action_news_dest_to_new_details_dest, bundle)
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsEntity.url))
            startActivity(intent)
        }
    }

    override fun showNews(it: List<NewsEntity>) {
        empty_group.visibility = View.GONE
        news_rv.visibility = View.VISIBLE
        adapter.setItemsAndNotify(it)
    }

    override fun setupNewsRecycler() {
        news_swipe.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener {
                presenter.refreshNews()
            }
        }
        this.adapter = NewsAdapter(presenter::onNewItemClicked)
        news_rv.adapter = adapter
    }

    override fun hideRefreshing() {
        news_swipe.isRefreshing = false
    }

    override fun showEmptyView() {
        empty_group.visibility = View.VISIBLE
        empty_tv.text = getString(R.string.news_empty)
        news_rv.visibility = View.GONE
    }

    override fun showErrorView() {
        empty_group.visibility = View.VISIBLE
        empty_tv.text = getString(R.string.news_error)
        news_rv.visibility = View.GONE
    }
}
