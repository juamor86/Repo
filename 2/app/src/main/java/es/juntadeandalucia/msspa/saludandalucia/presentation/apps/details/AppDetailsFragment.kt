package es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.transition.TransitionInflater
import com.squareup.picasso.Picasso
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.apps.details.image.adapter.ImagesAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_app_details.*

class AppDetailsFragment : BaseFragment(), AppDetailsContract.View {

    @Inject
    lateinit var presenter: AppDetailsContract.Presenter

    private lateinit var imagesAdapter: ImagesAdapter

    override fun bindPresenter() = presenter

    override fun bindLayout(): Int = R.layout.fragment_app_details

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { ctx->
            sharedElementEnterTransition =
                TransitionInflater.from(ctx).inflateTransition(R.transition.move)
        }
        postponeEnterTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appEntity = arguments?.get("app_entity") as AppEntity

        Handler(Looper.getMainLooper()).postDelayed({
            app_detail_layout_cl.transitionName = appEntity.packageName
            app_detail_icon_iv.transitionName = appEntity.icon
            app_detail_name_tv.transitionName = appEntity.name
            app_detail_category_tv.transitionName = appEntity.category
            app_detail_description_tv.transitionName = appEntity.description
            app_detail_download_btn.transitionName = "download_button"
        }, 100)

        presenter.onCreateView(appEntity)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun setUpView() {
        // set up button event
        app_detail_download_btn.setOnClickListener {
            presenter.onButtonPressed()
        }
        // set up list of the images
        imagesAdapter = ImagesAdapter(presenter::onImageDetailsClick)
    }

    override fun showAppDetails(appEntity: AppEntity) {
        Picasso.get()
            .load(appEntity.icon)
            .placeholder(R.drawable.img_news_placeholder)
            .into(app_detail_icon_iv)
        app_detail_name_tv.text = appEntity.name
        app_detail_category_tv.text = appEntity.category
        app_detail_description_tv.text = appEntity.description

        if (appEntity.images.isNotEmpty()) {
            loadDetailsImages(appEntity)
        }
        checkAppInstalled(appEntity.packageName)

        startPostponedEnterTransition()
    }

    override fun checkAppInstalled(packageName: String) {
        if (this.isAppInstalled(packageName)) {
            val openString = context?.getString(R.string.app_details_btn_download_open)
            app_detail_download_btn.text = openString
        } else {
            val downloadString =
                context?.getString(R.string.app_details_btn_download_download)
            app_detail_download_btn.text = downloadString
        }
    }

    override fun downloadApp(linkApp: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(linkApp)
            )
        )
    }

    override fun openApp(packageName: String) {
        val pm = context?.packageManager
        val intent = pm?.getLaunchIntentForPackage(packageName)
        startActivity(intent)
    }

    override fun openImageDetail(URLImage: String, item: View) {
        // TODO APP IMAGE DETAIL CLICK EVENT
    }

    override fun isAppInstalled(packageName: String): Boolean {
        return Utils.checkAppInstalled(context!!, packageName)
    }

    private fun loadDetailsImages(appEntity: AppEntity) {
        imagesAdapter.setItemsAndNotify(appEntity.images!!)
    }
}
