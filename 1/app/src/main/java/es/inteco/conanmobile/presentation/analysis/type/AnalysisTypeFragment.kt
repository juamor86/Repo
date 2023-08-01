package es.inteco.conanmobile.presentation.analysis.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import es.inteco.conanmobile.R
import es.inteco.conanmobile.di.component.DaggerFragmentComponent
import es.inteco.conanmobile.di.module.FragmentModule
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_type_analysis.*
import javax.inject.Inject

/**
 * Analysis type fragment
 *
 * @constructor Create empty Analysis type fragment
 */
class AnalysisTypeFragment : BaseFragment(), AnalysisTypeContract.View {
    @Inject
    lateinit var presenter: AnalysisTypeContract.Presenter

    lateinit var typeAdapter: AnalysisTypeAdapter
    lateinit var typeList: List<AnalysisEntity>
    var type: AnalysisEntity? = null

    override fun bindPresenter(): AnalysisTypeContract.Presenter = presenter

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun bindLayout() = R.layout.fragment_type_analysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle(R.string.analysis_type)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.onCreateView()
        return inflater.inflate(R.layout.fragment_type_analysis, container, false)
    }

    override fun refillRecyclerView(
        defaultAnalysisList: List<AnalysisEntity>,
        selectedAnalysis: AnalysisEntity
    ) {

        this.typeList = defaultAnalysisList
        this.type = selectedAnalysis

        type ?: run {
            type = defaultAnalysisList[0]
        }

        this.typeAdapter = AnalysisTypeAdapter(context = requireContext(), type!!,
            onClickItemListener = { analysisEntity ->
                type = analysisEntity
                presenter.saveDefaultAnalysis(type!!)
            },
            onRemoveItemListener = { }
        )

        rv_default_analysis.layoutManager = LinearLayoutManager(context)
        rv_default_analysis.adapter = typeAdapter
        typeAdapter.setItemsAndNotify(defaultAnalysisList)
    }
}