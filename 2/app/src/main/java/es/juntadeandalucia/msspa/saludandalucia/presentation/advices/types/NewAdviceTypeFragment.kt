package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeEntry
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryAdviceEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.advices.types.adapter.NewAdviceTypeAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import kotlinx.android.synthetic.main.fragment_new_advice_type.*
import javax.inject.Inject

class NewAdviceTypeFragment : BaseFragment(), NewAdviceTypeContract.View {
    //region VARIABLES
    lateinit var adapter: NewAdviceTypeAdapter
    //endregion

    //region INITIALIZATION & OVERRIDES
    @Inject
    lateinit var presenter: NewAdviceTypeContract.Presenter

    override fun bindPresenter() = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
                .builder()
                .applicationComponent(App.baseComponent)
                .fragmentModule(FragmentModule())
                .build()
                .inject(this)
    }

    override fun bindLayout() = R.layout.fragment_new_advice_type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            presenter.onCreate(
                getString(Consts.ARG_ADVICE_NUHSA),
                getParcelableArrayList<AdviceEntity>(Consts.ARG_ADVICES_SHARED)!!.toList(),
                getString(Consts.ARG_ADVICE_PHONE)
            )
        }
    }

    override fun setupAdapter(advices: List<AdviceEntity>) {
        this.adapter = NewAdviceTypeAdapter(
            presenter::onNotificationItemClick,
            presenter::onNotificationItemRemoved,
            advices
        )
    }

    override fun setupAdviceTypeRecycler() {
        advice_type_rv.adapter = adapter
    }

    override fun showAdviceTypes(adviceTypes: List<AdviceTypeEntry>) {
        var advicesResources = mutableListOf<AdviceTypeResource>()
        adviceTypes.map { advicesResources.add(it.resource) }
        adapter.setItemsAndNotify(advicesResources)
    }

    override fun navigateToNewAdvice(
        nuhsa: String,
        adviceTypeResource: AdviceTypeResource,
        phoneNumber: String
    ) {
        val bundle = bundleOf(Consts.ARG_ADVICE_TYPE to adviceTypeResource,
                                     Consts.ARG_ADVICE_NUHSA to nuhsa,
                                     Consts.ARG_ADVICE_PHONE to phoneNumber)
        findNavController().navigate(R.id.action_advice_type_to_new_advice, bundle)
    }

    override fun navigateToDetail(nuhsa: String, advice: AdviceEntity, phoneNumber: String) {
        val bundle = bundleOf(Consts.ARG_ADVICES_SHARED to advice,
                                     Consts.ARG_ADVICE_NUHSA to nuhsa.trim(),
                                     Consts.ARG_ADVICE_PHONE to phoneNumber)
        findNavController().navigate(R.id.action_advice_type_to_detail_advice, bundle)
    }
    //endregion
}