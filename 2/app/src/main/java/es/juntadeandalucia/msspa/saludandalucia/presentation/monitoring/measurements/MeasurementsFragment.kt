package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.di.component.DaggerFragmentComponent
import es.juntadeandalucia.msspa.saludandalucia.di.module.FragmentModule
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.App
import es.juntadeandalucia.msspa.saludandalucia.presentation.base.BaseFragment
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.adapters.DetailAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.adapters.SectionAdapter
import es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.help.MeasurementsHelpDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_measurements.*
import kotlinx.android.synthetic.main.view_item_measurement_detail_title_header.*
import kotlinx.android.synthetic.main.view_measurement_detail.*

class MeasurementsFragment : BaseFragment(), MeasurementsContract.View {

    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var helpDialog: CustomFullScreenDialog
    private lateinit var measures: List<MeasurementSectionEntity>
    private lateinit var filterList: MutableList<String>
    private var hasMultipleValues = false
    private var measureSelected = ""

    @Inject
    lateinit var presenter: MeasurementsContract.Presenter

    override fun bindPresenter() = presenter

    override fun bindLayout(): Int = R.layout.fragment_measurements

    override fun injectComponent() {
        DaggerFragmentComponent
            .builder()
            .applicationComponent(App.baseComponent)
            .fragmentModule(FragmentModule())
            .build()
            .inject(this)
    }

    override fun animateView() {
        animateViews(
            content_ll,
            animId = R.anim.slide_from_bottom,
            postAnimation = { content_ll.visibility = View.VISIBLE })
    }


    override fun initView(sectionEntity: List<MeasurementSectionEntity>) {
        measures = sectionEntity

        initFilters()
        filter_measures_rg.apply { check(getChildAt(0).id) }

        sectionAdapter = SectionAdapter(presenter::onSectionClicked)
        sectionAdapter.setItemsAndNotify(sectionEntity)
        section_rv.adapter = sectionAdapter

    }

    override fun onSectionClicked(measure: MeasurementSectionEntity) {
        for (view in filter_measures_rg.children) {
            val radioButton = filter_measures_rg.findViewById<RadioButton>(view.id)
            if (radioButton.text == measure.title) {
                filter_measures_rg.check(radioButton.id)
                filter_sv.smoothScrollTo(radioButton.x.toInt(), radioButton.y.toInt())
            }
        }

        setupDetail(measure)
    }

    override fun onFilterClicked(measure: String) {
        if (measure == getString(R.string.all)) {
            measurement_detail.visibility = View.GONE
            section_rv.visibility = View.VISIBLE
            measureSelected = getString(R.string.all)
        } else {
            measures.any {
                if (it.title == measure) {
                    setupDetail(it)
                    measureSelected = measure
                }
                it.title == measure
            }
            chart_container_ll.visibility = View.GONE
            items_rv.visibility = View.VISIBLE
            detail_list_btn.isChecked = true
            detail_graphic_btn.isChecked = false
        }
    }

    override fun reDrawAdapter(measures: List<MeasurementSectionEntity>) {
        addNewViewsAtFilter(measures.map { data -> data.title })
        sectionAdapter.setItemsAndNotify(measures)

        if (measureSelected.isNotEmpty().and(measureSelected != getString(R.string.all))) {
            measures.any {
                if (it.title == measureSelected) {
                    setupDetail(it)
                    addChartData(it)
                }
                it.title == measureSelected
            }
        }
    }

    override fun showNoMeasuresText() {

        data_gp.visibility = View.GONE

        no_measurements_gp.visibility = View.VISIBLE
    }

    private fun initFilters() {
        filter_measures_rg.removeAllViews()

        filterList = mutableListOf(getString(R.string.all))
        measures.map { data -> filterList.add(data.title) }

        for (item in filterList) {
            inflateRadioButton(item)
        }
        filter_measures_rg.apply {

            setOnCheckedChangeListener { _, checkedId ->
                presenter.onFilterClicked(filter_measures_rg.findViewById<RadioButton>(checkedId).text.toString())
            }
        }
    }

    private fun addNewViewsAtFilter(listString: List<String>) {
        for (string in listString) {
            if (!filterList.contains(string)) {
                inflateRadioButton(string)
            }
            filterList.add(string)
        }
    }


    private fun inflateRadioButton(text: String) {
        val childView = LayoutInflater.from(context)
            .inflate(
                R.layout.view_item_measurements_filter_rb,
                null
            ) as AppCompatRadioButton

        val params = RadioGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        params.setMargins(16, 0, 16, 0)
        childView.layoutParams = params

        childView.text = text
        filter_measures_rg.addView(childView)
    }

    // region Detail View
    private fun setupDetail(measure: MeasurementSectionEntity) {
        detail_sv.scrollTo(0, 0)
        detailAdapter = DetailAdapter()
        items_rv.adapter = detailAdapter
        setLazyLoading(measure)

        detail_graphic_btn.setOnClickListener { presenter.onGraphicButtonPressed(measure) }

        detail_list_btn.setOnClickListener { presenter.onListButtonPressed(measure.title) }

        info_iv.setOnClickListener {
            presenter.getDetailInfo(measure)
        }

        measure_detail_title.text = measure.title
        measure_detail_description.text = measure.range

        section_rv.visibility = View.GONE
        measurement_detail.visibility = View.VISIBLE
    }

    private fun setLazyLoading(measure: MeasurementSectionEntity) {
        var position = Consts.MEASURE_MIN_LOAD_ITEMS - 1
        val lastPosition = measure.values.size - 1
        val auxList = measure.values.take(Consts.MEASURE_MIN_LOAD_ITEMS).toMutableList()
        detailAdapter.setItemsAndNotify(auxList)

        detail_sv.setOnScrollChangeListener { scroll, _, _, _, _ ->

            if (!scroll.canScrollVertically(Consts.SCROLL_DIRECTION_BOTTOM)
            ) {
                if (position < lastPosition) {
                    var currentPosition = position

                    while (currentPosition != position + Consts.MEASURE_MIN_LOAD_ITEMS) {
                        currentPosition++
                        if (currentPosition <= lastPosition) {
                            auxList.add(measure.values[currentPosition])
                        } else {
                            break
                        }
                    }

                    position += Consts.MEASURE_MIN_LOAD_ITEMS
                    detailAdapter.setItemsAndNotify(auxList)
                }
            }
        }
    }


    override fun onGraphicButtonPressed(measure: MeasurementSectionEntity) {
        detail_list_btn.isChecked = false
        detail_graphic_btn.isChecked = true
        items_rv.visibility = View.GONE
        addChartData(measure)
        chart_container_ll.visibility = View.VISIBLE
    }

    override fun onListButtonPressed() {
        detail_list_btn.isChecked = true
        detail_graphic_btn.isChecked = false
        chart_container_ll.visibility = View.GONE
        items_rv.visibility = View.VISIBLE
    }

    override fun showDetailInfo(helpText: String?) {
        val text = helpText
            ?: resources.getString(R.string.measurement_no_help_text)

        helpDialog = MeasurementsHelpDialog(text)

        helpDialog.show(requireActivity().supportFragmentManager, "")

    }
    // endregion Detail

    // region Chart Configuration

    @SuppressLint("ResourceType")
    private fun setColorsBar(
        index: Int
    ): Int {
        val color: Int = when (index) {
            0 -> Color.parseColor(resources.getString(R.color.anakiwa))
            1 -> Color.parseColor(resources.getString(R.color.malibu))
            else -> Color.parseColor(resources.getString(R.color.science_blue))
        }

        return color

    }

    private fun addChartData(measure: MeasurementSectionEntity) {
        chart_container_ll.removeAllViews()

        val dates = mutableListOf<String>()
        for (item in measure.values) {
            dates.add(item.date)
        }

        val formatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase): String =
                    if (value.toInt() > -1 && value.toInt() < dates.size) {
                        dates[value.toInt()]
                    } else {
                        "0"
                    }
            }


        val entriesList: MutableList<MutableList<Entry>> = mutableListOf()
        val charts = mutableListOf<LineChart>()
        for (header in measure.headers) {
            charts.add(LineChart(requireContext()))
        }

        var position = 0f
        for (item in measure.values) {
            item.values.mapIndexed { index, value ->
                if (entriesList.size == index) {
                    entriesList.add(mutableListOf(Entry(position, value.toFloat())))
                } else {
                    (entriesList[index] as MutableCollection<Entry>).add(
                        Entry(
                            position,
                            value.toFloat()
                        )
                    )
                }
                position = position + 1 - index
            }
        }

        for ((index, entries) in entriesList.withIndex()) {
            val chart = LineChart(requireContext())
            val chartBar = LineDataSet(entries, "")
            hasMultipleValues = measure.headers.size > 1
            with(chartBar) {
                colors = listOf(setColorsBar(index))
                setDrawCircles(false)
                setDrawFilled(true)
                lineWidth = 1.5f
                fillAlpha = Consts.CHART_FILL_ALPHA
                fillColor = setColorsBar(index)
            }
            val data = LineData(chartBar)
            data.setDrawValues(false)
            data.setValueTypeface(ResourcesCompat.getFont(requireContext(), R.font.open_sans)!!)

            chart_container_ll.addView(chart)
            setupChart(formatter, chart)
            chart.apply {
                setData(data)
                legend.apply {
                    isEnabled = true
                    resetCustom()
                    val title: String = if (measure.headers.isNotEmpty()) measure.headers[index] else ""
                    setCustom(setEntryLegend(title, index))
                    notifyDataSetChanged()
                }
                invalidate()

            }

        }
    }

    @SuppressLint("ResourceType")
    private fun setEntryLegend(title: String, index: Int): Array<LegendEntry> {
        val entry = mutableListOf<LegendEntry>()

        if (hasMultipleValues) {
            val color = when (index) {
                0 -> Color.parseColor(resources.getString(R.color.anakiwa))
                1 -> Color.parseColor(resources.getString(R.color.malibu))
                else -> Color.parseColor(resources.getString(R.color.science_blue))
            }

            entry.add(
                LegendEntry(
                    title,
                    Legend.LegendForm.DEFAULT,
                    Consts.LEGEND_SQUARE_SIZE,
                    0f,
                    null,
                    color
                )
            )
        }

        return entry.toTypedArray()
    }


    private fun setupChart(formatter: ValueFormatter, chart: LineChart) {

        with(chart) {
            description.isEnabled = false
            isDragEnabled = false
            setNoDataText(resources.getString(R.string.measurement_no_chart_data))
            setNoDataTextTypeface(ResourcesCompat.getFont(requireContext(), R.font.open_sans)!!)
            layoutParams.height = Consts.CHART_HEIGHT

            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            isScaleYEnabled = false
            setMaxVisibleValueCount(60)

            with(axisRight) {
                setDrawGridLines(false)
                isEnabled = false
            }

            with(xAxis) {
                position = XAxis.XAxisPosition.BOTTOM
                axisLineWidth = 0f
                gridLineWidth = 0f
                granularity = 1f
                labelCount = 7
                setDrawGridLines(false)
                valueFormatter = formatter
            }
        }
    }

    // endregion Chart
}
