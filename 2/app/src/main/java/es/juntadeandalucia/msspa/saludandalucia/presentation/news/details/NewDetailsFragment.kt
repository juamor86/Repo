package es.juntadeandalucia.msspa.saludandalucia.presentation.news.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.transition.TransitionInflater
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.IMAGE_HEADER_TRANSITION
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_news_details.*

/**
 * A simple [Fragment] subclass.
 */
class NewDetailsFragment : BaseFragment(), NewDetailsContract.View {

    @Inject
    lateinit var presenter: NewDetailsContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout() = R.layout.fragment_news_details

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
            sharedElementEnterTransition =
                TransitionInflater.from(ctx).inflateTransition(R.transition.move)
        }
        postponeEnterTransition()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newEntity = arguments?.get(Consts.ARGUMENT_NEWS_ENTITY) as NewsEntity

        new_detail_header_iv.transitionName = IMAGE_HEADER_TRANSITION + newEntity.url
        new_detail_content_ly.transitionName = IMAGE_HEADER_TRANSITION + newEntity.title

        presenter.setupView(newEntity)
    }

    override fun onResume() {
        presenter.onResume()
        super.onResume()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    //region View Methods

    override fun setupView() {
        back_arrow_iv.setOnClickListener {
            presenter.onBackPressed()
        }
        new_detail_share_btn.setOnClickListener {
            presenter.onShareNewClicked()
        }
    }

    override fun hideToolbar() {
        val toolbar = activity?.toolbar
        toolbar?.visibility = View.GONE
    }

    override fun showToolbar() {
        val toolbar = activity?.toolbar
        toolbar?.visibility = View.VISIBLE
    }

    override fun showNew(newsEntity: NewsEntity) {
        with(newsEntity) {
            val iconResourceId = requireContext().resources.getIdentifier(
                url,
                "drawable",
                context?.packageName
            )

            if (iconResourceId > 0) {
                new_detail_header_iv.setImageResource(iconResourceId)
            }

            new_detail_title_tv.text = title
            new_detail_description_tv.text =
                description?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
        }
        startPostponedEnterTransition()
    }

    override fun shareNew(contentToShare: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, contentToShare)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun goBack() {
        view?.let { Navigation.findNavController(it).navigateUp() }
    }

    //endregion
}
