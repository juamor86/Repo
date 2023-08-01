package es.inteco.conanmobile.presentation.legal

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import es.inteco.conanmobile.utils.Consts
import kotlinx.android.synthetic.main.fragment_legal.*
import javax.inject.Inject

/**
 * Legal fragment
 *
 * @constructor Create empty Legal fragment
 */
class LegalFragment : BaseFragment(), LegalContract.View {
    @Inject
    lateinit var presenter: LegalContract.Presenter

    override fun bindLayout() = R.layout.fragment_legal

    override fun bindPresenter() = presenter

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
        requireActivity().setTitle(R.string.terms_and_conditions_tittle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreateView(arguments?.get(Consts.ARG_CONFIGURATION) as MessageEntity)
    }

    override fun showLegalText(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            legal_text.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            legal_text.text = Html.fromHtml(text)
        }
        legal_text.isClickable = true
        legal_text.movementMethod = LinkMovementMethod.getInstance()
    }
}