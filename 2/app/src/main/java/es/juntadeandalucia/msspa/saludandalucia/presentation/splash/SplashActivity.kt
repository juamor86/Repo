package es.juntadeandalucia.msspa.saludandalucia.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerActivityComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.ActivityModule
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseActivity
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity(), SplashContract.View {

    private val TAG: String = "SplashActivity"

    @Inject
    lateinit var presenter: SplashContract.Presenter

    override fun bindLayout() = R.layout.activity_splash

    override fun bindPresenter() = presenter

    override fun injectComponent() {
        DaggerActivityComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .activityModule(ActivityModule())
            .build()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }

    override fun startTextAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        title_tv.startAnimation(animation)
        subtitle_tv.startAnimation(animation)
    }

    override fun navigateToHomeScreen() {
        val navigateIntent = Intent(this, MainActivity::class.java)
        startActivity(navigateIntent)
    }
}
