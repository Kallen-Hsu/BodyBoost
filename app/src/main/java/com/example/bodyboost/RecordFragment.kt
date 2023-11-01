package com.example.bodyboost

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*

class ReportFragment : Fragment() {

    private lateinit var weightChart: LineChart
    private lateinit var caloriesChart: LineChart
    private lateinit var nutrientChart: PieChart
    private lateinit var waterChart: BarChart

    // date and weight data list
    private val weightRecordDate = mutableListOf<String>()
    private val weightList = mutableListOf<Float>()
    // weight chart use data
    private val weightChartMaxAndMinList = mutableListOf<Float>()

    // calories data list
    private val caloriesList = mutableListOf<Float>()
    private val caloriesRecordDate = mutableListOf<String>()
    private val caloriesChartMaxAndMinList = mutableListOf<Float>()

    // nutrient data list
    private val nutrientList = mutableListOf<Float>()
    private val nutrientLabel = mutableListOf<String>()
    private val nutrientRecordDate = mutableListOf<String>()

    //water data list
    private val waterList = mutableListOf<Float>()
    private val waterRecordDate = mutableListOf<String>()
    private val waterMaxAndMinList = mutableListOf<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_report, container, false)

        // findViewById
        weightChart = rootView.findViewById(R.id.lineChart_weight)
        caloriesChart = rootView.findViewById(R.id.lineChart_calorie)
        nutrientChart = rootView.findViewById(R.id.pieChart_nutrient)
        waterChart = rootView.findViewById(R.id.barChart_water)

        // api write here
        // get dateList and weightList data: WeightHistory

        weightChartInit()
        caloriesChartInit()
        nutrientChartInit()
        waterChartInit()

        // Inflate the layout for this fragment
        return rootView
    }

    private fun weightChartInit() {
        // init weight chart
        weightChart.description.isEnabled = false
        weightChart.axisRight.isEnabled = false
        weightChart.setNoDataText("目前尚無資料")
        weightChart.setNoDataTextColor(Color.BLACK)
        weightChart.setDrawGridBackground(false)
        weightChart.setDrawBorders(false)
        weightChart.invalidate()
        // set weightChartMaxAndMinList
        for (i in 0..600 step 4) {
            weightChartMaxAndMinList.add(i.toFloat())
        }
        // updateData
        if (weightRecordDate.isNotEmpty()) {
            setWeightChart(findWeightChartMaxValue(weightList.max()), findWeightChartMinValue(weightList.min()))
        }
    }

    private fun findWeightChartMaxValue(weightListMaxValue: Float): Float {
        var max = 0f
        for (value in weightChartMaxAndMinList) {
            if (value > weightListMaxValue) {
                max = value.toFloat()
                break
            }
        }
        return max
    }

    private fun findWeightChartMinValue(weightListMinValue: Float): Float {
        var min = 0f
        for (value in weightChartMaxAndMinList.reversed()) {
            if (value < weightListMinValue) {
                min = value.toFloat()
                break
            }
        }
        return min
    }

    private fun setWeightChart(yMax: Float, yMin: Float) {
        // set X
        weightChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(weightRecordDate)
            labelCount = weightRecordDate.size
            granularity = 1f
            setDrawGridLines(false)
        }
        // set Y
        weightChart.axisLeft.apply {
            axisMaximum = yMax // weight max value
            axisMinimum = yMin // weight min value
            labelCount = 5
        }
        // add Y data
        val yList = mutableListOf<Entry>()
        var index = 0f
        for (weight in weightList) {
            yList.add(Entry(index, weight))
            index += 1f
        }
        // set Y data
        val lineDataSet = LineDataSet(yList, "體重")
        lineDataSet.color = 0xFFE39D8F.toInt()
        lineDataSet.setDrawValues(false)
        lineDataSet.lineWidth = 1.5f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(0xFFE39D8F.toInt())
        val lineData = LineData(lineDataSet)

        // update weightChart data
        weightChart.data = lineData
        weightChart.setVisibleXRangeMaximum(4f)
        weightChart.invalidate()
    }

    private fun caloriesChartInit() {
        // init calories chart
        caloriesChart.description.isEnabled = false
        caloriesChart.axisRight.isEnabled = false
        caloriesChart.setNoDataText("目前尚無資料")
        caloriesChart.setNoDataTextColor(Color.BLACK)
        caloriesChart.setDrawGridBackground(false)
        caloriesChart.setDrawBorders(false)
        caloriesChart.invalidate()
        // set weightChartMaxAndMinList
        for (i in 0..10000 step 4) {
            caloriesChartMaxAndMinList.add(i.toFloat())
        }
        // updateData
        if (caloriesRecordDate.isNotEmpty()) {
            setCaloriesChart(findCaloriesChartMaxValue(caloriesList.max()), findCaloriesChartMinValue(caloriesList.min()))
        }
    }

    private fun findCaloriesChartMaxValue(caloriesListMaxValue: Float): Float {
        var max = 0f
        for (value in caloriesChartMaxAndMinList) {
            if (value > caloriesListMaxValue) {
                max = value.toFloat()
                break
            }
        }
        return max
    }

    private fun findCaloriesChartMinValue(caloriesListMinValue: Float): Float {
        var min = 0f
        for (value in caloriesChartMaxAndMinList.reversed()) {
            if (value < caloriesListMinValue) {
                min = value.toFloat()
                break
            }
        }
        return min
    }

    private fun setCaloriesChart(yMax: Float, yMin: Float) {
        // set X
        caloriesChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(caloriesRecordDate)
            labelCount = caloriesRecordDate.size
            granularity = 1f
            setDrawGridLines(false)
        }
        // set Y
        caloriesChart.axisLeft.apply {
            axisMaximum = yMax // max value
            axisMinimum = yMin // min value
            labelCount = 5
        }
        // add Y data
        val yList = mutableListOf<Entry>()
        var index = 0f
        for (calories in caloriesList) {
            yList.add(Entry(index, calories))
            index += 1f
        }
        // set Y data
        val lineDataSet = LineDataSet(yList, "熱量")
        lineDataSet.color = 0xFFE39D8F.toInt()
        lineDataSet.setDrawValues(false)
        lineDataSet.lineWidth = 1.5f
        lineDataSet.circleRadius = 4f
        lineDataSet.setCircleColor(0xFFE39D8F.toInt())

        val lineData = LineData(lineDataSet)

        // update caloriesChart data
        caloriesChart.data = lineData
        caloriesChart.setVisibleXRangeMaximum(4f)
        caloriesChart.invalidate()

    }

    private fun nutrientChartInit() {
        nutrientChart.apply {
            description.isEnabled = false
            setNoDataText("目前尚無資料")
            setNoDataTextColor(Color.BLACK)
            setDrawEntryLabels(false)
            invalidate()
        }
        // label list
        nutrientLabel.add("碳水化合物")
        nutrientLabel.add("蛋白質")
        nutrientLabel.add("脂肪")
        nutrientLabel.add("其他")
        // updateData
        if (nutrientList.isNotEmpty()) {
            setNutrientChart()
        }
    }

    private fun setNutrientChart() {
        // set nutrient
        // set color
        val pieColors = ArrayList<Int>()
        pieColors.add(0xFFFF9E9E.toInt())
        pieColors.add(0xFFFFBE9E.toInt())
        pieColors.add(0xFFFFDB9E.toInt())
        pieColors.add(0xFF9EBBFF.toInt())
        // add data
        val nList = mutableListOf<PieEntry>()
        var index = 0
        for (nutrient in nutrientList) {
            var label = nutrientLabel[index]
            nList.add(PieEntry(nutrient, label))
            index += 1
        }
        // PieDataSet
        val pieDataSet = PieDataSet(nList, "")
        pieDataSet.colors = pieColors
        pieDataSet.valueTextSize = 11f
        // set PieData
        val pieData = PieData(pieDataSet).apply {
            setDrawValues(true)
            setValueFormatter(PercentFormatter(nutrientChart))
            setValueTextColor(Color.BLACK)
        }
        // set attributes
        nutrientChart.apply {
            setUsePercentValues(true)
            setEntryLabelColor(Color.BLACK)
            setHoleColor(Color.TRANSPARENT)
            isDrawHoleEnabled = true
            holeRadius = 50f
            setDrawCenterText(false)
        }
// update data
        nutrientChart.data = pieData
        nutrientChart.invalidate()
    }

    private fun waterChartInit() {
        waterChart.apply {
            description.isEnabled = false
            axisRight.isEnabled = false
            setNoDataText("目前尚無資料")
            setNoDataTextColor(Color.BLACK)
            setDrawGridBackground(false)
            setDrawBorders(false)
            invalidate()
        }
        // set weightChartMaxAndMinList
        for (i in 0..4000 step 5) {
            waterMaxAndMinList.add(i.toFloat())
        }
        // updateData
        if (waterList.isNotEmpty()) {
            setWaterChart(findWaterMaxValue(waterList.max()), findWaterMinValue(waterList.min()))
        }
    }

    private fun setWaterChart(yMax: Float, yMin: Float) {
        // set water
        // set X
        waterChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(waterRecordDate)
            labelCount = waterRecordDate.size
            granularity = 1f
            setDrawGridLines(false)
        }
        // set Y
        waterChart.axisLeft.apply {
            axisMaximum = yMax
            axisMinimum = yMin
            labelCount = 5
        }
        // add Y data
        val yList = mutableListOf<BarEntry>()
        var index = 0f
        for (water in waterList) {
            yList.add(BarEntry(index, water))
            index += 1f
        }
        // set Y data
        val barDataSet = BarDataSet(yList, "飲水量")
        barDataSet.setDrawValues(false)
        barDataSet.color = Color.parseColor("#87B3FF")

        val barData = BarData(barDataSet)
        // update data
        waterChart.data = barData
        waterChart.setVisibleXRangeMaximum(7f)
        waterChart.invalidate()
    }

    private fun findWaterMaxValue(waterListMaxValue: Float): Float {
        var max = 0f
        for (value in waterMaxAndMinList) {
            if (value > waterListMaxValue) {
                max = value.toFloat()
                break
            }
        }
        return max
    }

    private fun findWaterMinValue(waterListMinValue: Float): Float {
        var min = 0f
        for (value in waterMaxAndMinList) {
            if (value < waterListMinValue) {
                min = value.toFloat()
                break
            }
        }
        return min
    }
}