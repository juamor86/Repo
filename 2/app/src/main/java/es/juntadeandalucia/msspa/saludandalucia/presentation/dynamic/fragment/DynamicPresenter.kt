package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.fragment

import android.net.Uri
import androidx.core.os.bundleOf
import es.juntadeandalucia.msspa.saludandalucia.domain.bus.DynamicUIBus
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicElementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BasePresenter
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicConsts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class DynamicPresenter(
    private val dynamicUIBus: DynamicUIBus
) : BasePresenter<DynamicContract.View>(), DynamicContract.Presenter {
    private var screen :DynamicScreenEntity? = null

    override fun onViewCreated(screen: DynamicScreenEntity) {
        view.buildScreen(screen)
        this.screen = screen
    }

    override fun onElementClicked(element: DynamicElementEntity) {
        val uri = Uri.parse(element.navigation.target)
        val target = uri.getQueryParameter(DynamicConsts.Nav.TARGET)
        if (element.navigation.type == DynamicConsts.Nav.WEBVIEW_SESSION) {
            element.navigation.title = element.title.text
            view.handleNavigation(element.navigation)
        } else if (target == DynamicConsts.Nav.DEST_DYNAMIC_QUIZZES_QUESTIONAIRES.first) {
            val quizId = uri.getQueryParameter(Consts.QUIZ_ID_PARAM)
            val title = element.title.text
            val bundle = bundleOf(Consts.ARG_QUIZ_ID to quizId, Consts.ARG_QUIZ_TITLE to title)
            if (element.navigation.bundle == null) {
                element.navigation.bundle = bundle
            } else {
                element.navigation.bundle!!.putAll(bundle)
            }
            view.handleNavigation(element.navigation)
        } else {
            view.handleNavigation(element.navigation)
        }
    }
}
