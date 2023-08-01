package com.example.wmm.ui.homeFragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.wmm.R
import com.example.wmm.databinding.FragmentHomeBinding
import com.example.wmm.model.entities.ExpenseEntity
import com.example.wmm.model.entities.TransactionResume
import com.example.wmm.model.entities.TransactionType
import com.example.wmm.ui.base.BaseAdapter
import com.example.wmm.ui.base.GenericSimpleRecyclerBindingInterface
import com.example.wmm.utils.Consts
import com.example.wmm.utils.Consts.Companion.CHART_ANIMATION_DURATION
import com.example.wmm.utils.Consts.Companion.CHART_BAR_SPACE
import com.example.wmm.utils.Consts.Companion.CHART_GROUP_SPACE
import com.example.wmm.utils.DateUtil
import com.example.wmm.utils.RoundedBarChartRenderer
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private var labels = mutableListOf("")
    private var incomeBar: ArrayList<BarEntry> = ArrayList()
    private var expenseBar: ArrayList<BarEntry> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupChart()
        setupDataListener()
        setupDataRecyclerView()
        setupRadioGroup()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadingTypeChart(binding.expenseTabRg.id)
        loadLastTransactions()
    }

    private fun loadLastTransactions() {
        homeViewModel.getLastTransactions()
    }

    private fun setupDataRecyclerView() {
        homeViewModel.lastTransactionsLiveData.observe(viewLifecycleOwner) {
            it?.let { expenseList ->
                showRecyclerView(expenseList)
            }
        }
    }

    private fun setupRadioGroup() {
        binding.expenseTabRg.setOnCheckedChangeListener { _, checkedId ->
            setLoadingTypeChart(checkedId)
        }
    }

    private fun setLoadingTypeChart(checkedId: Int) {
        with(homeViewModel) {
            when (checkedId) {
                R.id.this_week_rb -> getThisWeekData()
                R.id.this_month_rb -> getThisMonthData()
                else -> getYearData()
            }
        }
    }

    private fun setupDataListener() {
        homeViewModel.resumeLiveData.observe(viewLifecycleOwner) {
            loadChart(it)
            loadTotalResume(it)
        }
    }

    private fun showRecyclerView(expenseList: List<ExpenseEntity>) {
        binding.lastTransactionsView.lastTransactionsRv.adapter = BaseAdapter(
            expenseList,
            R.layout.expense_item_view,
            getRecyclerViewBinding()
        )
    }

    private fun getRecyclerViewBinding(): GenericSimpleRecyclerBindingInterface<ExpenseEntity> {
        return object : GenericSimpleRecyclerBindingInterface<ExpenseEntity> {
            override fun bindData(item: ExpenseEntity, view: View) {
                with(view) {
                    findViewById<TextView>(R.id.expense_name_item_tv).text = item.name
                    findViewById<TextView>(R.id.expense_date_item_tv).text =
                        DateUtil.parseDateToStringShortDate(item.date)
                    findViewById<TextView>(R.id.expense_quantity_item_tv).apply {
                        if (item.type == TransactionType.EXPENSE) {
                            text = "- ${item.amount} €"
                            setTextColor(context.getColor(R.color.sunglo))
                        } else {
                            text = "${item.amount} €"
                            setTextColor(context.getColor(R.color.de_york))
                        }
                    }
                }
            }
        }
    }

    private fun loadTotalResume(list: List<TransactionResume>) {
        var totalIncome = BigDecimal(0)
        var totalExpense = BigDecimal(0)
        list.map {
            totalIncome = totalIncome.add(it.incomeAmount.toBigDecimal())
            totalExpense = totalExpense.add(it.expenseAmount.toBigDecimal())
        }

        with(binding.totalCardResume) {
            totalIncomeAmountResumeTitleTv.text = "$totalIncome €"
            totalExpenseAmountResumeTitleTv.text = "$totalExpense €"
            totalResultResumeAmountTitleTv.text =
                (totalIncome.subtract(totalExpense)).toString() + " €"
        }
    }

    private fun loadChart(centerList: List<TransactionResume>?) {
        centerList?.let {
            if (it.isNotEmpty()) {
                cleanChartData()
                setupChartData(it)
                drawChart()
            }
        }
    }

    private fun cleanChartData() {
        binding.transactionHomeChart.clear()
        labels = mutableListOf("")
        incomeBar.clear()
        expenseBar.clear()
    }

    private fun setupChartData(transactions: List<TransactionResume>) {
        for (index in transactions.indices) {
            with(transactions[index]) {
                incomeBar.add(BarEntry(index.toFloat(), incomeAmount))
                expenseBar.add(BarEntry(index.toFloat(), expenseAmount))

                month?.let { month ->
                    labels.add(requireContext().getString(month.monthName))
                }

                weekOfMonth?.let {
                    labels.add(requireContext().getString(it.weekName))
                }

                dayOfWeek?.let {
                    labels.add(requireContext().getString(it.dayName))
                }
            }
        }
        labels.add("")
    }

    private fun setupChart() {
        binding.transactionHomeChart.apply {
            setDrawBarShadow(false)
            description.isEnabled = false
            setPinchZoom(false)
            setDrawGridBackground(false)
            axisRight.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            description.isEnabled = false
            extraBottomOffset = 10f
            setScaleEnabled(false)
            val roundedBarChartRenderer =
                RoundedBarChartRenderer(this, this.animator, this.viewPortHandler)
            roundedBarChartRenderer.setRadius(requireContext().resources.getDimension(R.dimen.chart_corner_dimen))
            this.renderer = roundedBarChartRenderer
            setupLeftAxis()
            setupXAxis()
        }
    }

    private fun drawChart() {
        binding.transactionHomeChart.apply {
            setChartLabels(labels.toTypedArray())
            val incomeSet = BarDataSet(incomeBar, "incomeBar")
            incomeSet.apply {
                color = requireContext().getColor(R.color.de_york)
                isHighlightEnabled = false
                setDrawValues(false)
            }
            val expenseSet = BarDataSet(expenseBar, "expenseBar")
            expenseSet.apply {
                color = requireContext().getColor(R.color.sunglo)
                isHighlightEnabled = false
                setDrawValues(false)
            }

            val data = BarData(arrayListOf<IBarDataSet>(incomeSet, expenseSet))

            data.barWidth = computeBarWidth(data.dataSetCount)
            xAxis.axisMaximum = labels.size.toFloat() - 1.1f
            setData(data)
            groupBars(1f, CHART_GROUP_SPACE, CHART_BAR_SPACE)
            invalidate()
            animator.animateY(CHART_ANIMATION_DURATION)
        }
    }

    private fun computeBarWidth(dataSize: Int) =
        (1 - CHART_GROUP_SPACE - (CHART_BAR_SPACE * dataSize)) / dataSize

    private fun BarChart.setupLeftAxis() {
        with(axisLeft) {
            setDrawGridLines(false)
            setDrawAxisLine(false)
            textColor = context.getColor(R.color.alabaster)
            axisLineColor = Color.WHITE
            setDrawGridLines(false)
            granularity = Consts.CHART_LEFT_AXIS_GRANULARITY
            setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            labelCount = Consts.CHART_LABEL_COUNT
            axisMinimum = Consts.CHART_LEFT_AXIS_MINIMUM
        }
    }

    private fun BarChart.setupXAxis() {
        with(xAxis) {
            setCenterAxisLabels(true)
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = Consts.CHART_X_AXIS_GRANULARITY
            textColor = context.getColor(R.color.alabaster)
            textSize = Consts.CHART_X_AXIS_TEXT_SIZE
            this.typeface = Typeface.create("semibold", Typeface.BOLD)
            axisLineColor = Color.WHITE
            axisMinimum = Consts.CHART_X_AXIS_MINIMUM
            setDrawAxisLine(false)
        }
    }

    private fun setChartLabels(labels: Array<String>) {
        binding.transactionHomeChart.xAxis.valueFormatter = LabelFormatter(labels)
    }
}

private class LabelFormatter(var labels: Array<String>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return if (value.toInt() < labels.size) {
            labels[value.toInt()]
        } else {
            ""
        }
    }
}