package com.sauyang.androidsample.module.graph

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.sauyang.androidsample.R
import com.sauyang.androidsample.ui.SampleBaseActivity
import com.sauyang.webservices.utils.SUtils.getMobileDataJSON
import org.json.JSONException
import org.json.JSONObject
import webservices.debug.LogDbug


class GraphDataActivity : SampleBaseActivity(),View.OnClickListener {

    private var barChart: BarChart? = null
    private var mobileDataList = ArrayList<MobileData>()

    override fun contentViewID(): Int {
        return R.layout.graph_data_activity
    }

    override fun populateData(savedInstanceState: Bundle?) {

        if(savedInstanceState != null)
            super.populateData(savedInstanceState)


    }
    override fun populateLayout(v: View?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        barChart = findViewById(R.id.barChart)
        showToolbar(true)
        setData(12)

        //Show marker
        val mv = XYMarkerView(this)
        mv.chartView = barChart // For bounds control

        barChart?.marker = mv // Set the marker to the chart


        barChart?.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Log.v("####", "nothing selected")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e == null)
                    return

                Log.i(
                    "VAL SELECTED",
                    "Value: " + e.getY().toString() + ", index: " + h?.getX()
                        .toString() + ", DataSet index: " + h?.getDataSetIndex()
                )
            }
        })
    }


    private fun setData(_xAxis: Int) {
        val yVals1: ArrayList<BarEntry> = ArrayList()

        val iconList: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until _xAxis + 1) {
            val value = 10.toFloat()
            yVals1.add(BarEntry(i.toFloat(), value))
        }
        for (i in 0 until _xAxis + 1) {

            val value2 = (-0.5).toFloat()
            iconList.add(BarEntry(i.toFloat(), value2, resources.getDrawable(R.mipmap.ic_launcher)))
        }

        var quater = ArrayList<String>()
        var volumeMobileSize= ArrayList<BarEntry>()
        getMobileDataFromJSON()

        for(i in 0 until mobileDataList.size){
            quater.add(mobileDataList.get(i).quarter)
            volumeMobileSize.add(BarEntry(i.toFloat(),mobileDataList.get(i).volume_of_mobile_data.toFloat()))
        }

        val set1: BarDataSet
        var set2: BarDataSet
        //Test1
        set1 = BarDataSet(volumeMobileSize, "Mobile Data Volume")
        set1.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        set1.valueTextSize = 5f
        //set1.setValueTextColor(Color.GRAY);
        //set1.setIconsOffset(new MPPointF(0, 250));

        barChart?.description?.isEnabled = false
        barChart?.setPinchZoom(false)
        barChart?.setDrawGridBackground(false)
        barChart?.legend?.isEnabled = false
        barChart?.animateY(3000)

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)

        //Draw icon on X-value label.
        val imageList: ArrayList<Bitmap> = ArrayList()
        val iexplorer = BitmapFactory.decodeResource(resources, R.drawable.ie)
        val chrome = BitmapFactory.decodeResource(resources, R.drawable.chrome)
        imageList.add(chrome)
        imageList.add(iexplorer)
        imageList.add(chrome)
        imageList.add(iexplorer)
        imageList.add(chrome)
        imageList.add(iexplorer)
        imageList.add(chrome)
        imageList.add(iexplorer)
        imageList.add(chrome)
        imageList.add(iexplorer)
        imageList.add(chrome)
        imageList.add(iexplorer)
        imageList.add(chrome)
        imageList.add(iexplorer)
        barChart?.setDrawValueAboveBar(true)
//
        val xAxis = barChart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawGridLines(true)
        xAxis?.granularity = 1f
        xAxis?.labelCount = 7
        //X-Axis bottom label, true to show
        xAxis?.setDrawLabels(true)
//
        //This is to show x-axis label on bottom
        barChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(quater)
        barChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        barChart?.setDrawGridBackground(false)
        //Label on left or right should be display or not
//        barChart?.axisLeft?.isEnabled = false
        barChart?.axisRight?.isEnabled = false
        barChart?.description?.isEnabled = false

        //Left Axis properties set
        val left = barChart?.getAxisLeft();
        left?.setDrawLabels(true);
//        left?.setSpaceTop(25f);
        left?.setSpaceBottom(25f);
        left?.setDrawZeroLine(true); // draw a zero line
        left?.setZeroLineColor(Color.GRAY);
        left?.setZeroLineWidth(0.7f);
        left?.axisLineColor = Color.WHITE
        left?.setDrawGridLines(true)
        left?.setDrawAxisLine(true);
        left?.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        left?.axisMinimum = 0f // This is equals to setStartAtZero(true)


        val rightAxis = barChart!!.axisRight
        rightAxis.isEnabled = false

        val l = barChart?.legend
        l?.isEnabled = false

        val data = BarData(dataSets)
        data.setDrawValues(false)
        barChart?.data = data
        barChart?.renderer = CustomBarAdapter(
            barChart,
            barChart?.animator,
            barChart?.viewPortHandler,
            imageList,
            this
        )
        barChart?.setFitBars(true)
        barChart?.setScaleEnabled(true)
        barChart?.setExtraOffsets(0f, 0f, 0f, 20f)


        //Set Bar Border
        for (set in barChart?.data
        !!.dataSets) (set as BarDataSet).barBorderWidth = 1f

        //Highlight bar enable
        if (barChart?.data != null) {
            barChart?.data?.isHighlightEnabled = true
        }

        //Sym with iOS added shadow background.
        barChart?.setDrawBarShadow(true)
        barChart?.invalidate()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.barChart -> {

            }

        }
    }


    inner class MobileData{
        var id : String = ""
        var quarter : String = ""
        var volume_of_mobile_data : String = ""


        constructor(id : String , _quater : String, _volume_of_mobile_data: String){
            this.id = id
            this.quarter = _quater
            this.volume_of_mobile_data = _volume_of_mobile_data
        }
    }

    private fun addMobileData(id :String ,quater: String, volume_of_mobile_data: String ){
        mobileDataList.add(MobileData(id,quater,volume_of_mobile_data))
    }

    open fun getMobileDataFromJSON(): List<MobileData>? {

        try {
            val allDataJSON: JSONObject = getMobileDataJSON()
            if (allDataJSON.has("result")) {

                val jsonResult: JSONObject = allDataJSON.getJSONObject("result")
                val recordJSONArray = jsonResult.getJSONArray("records")

                val recordsSize: Int = recordJSONArray.length()

                for (i in 0 until recordsSize) {
                    var id = ""
                    var quater = ""
                    var volumeOfMobileData = ""

                    val recordJSONObject: JSONObject = recordJSONArray.getJSONObject(i)

                    if(recordJSONObject.has("_id")){
                        id = recordJSONObject.getString("_id")
                    }
                    if(recordJSONObject.has("quarter")){
                        quater = recordJSONObject.getString("quarter")
                    }
                    if(recordJSONObject.has("volume_of_mobile_data")){
                        volumeOfMobileData = recordJSONObject.getString("volume_of_mobile_data")
                    }
                    addMobileData(id,quater,volumeOfMobileData)
                }
                return mobileDataList
            }
        } catch (e: JSONException) {
            LogDbug.printStackTrace(e)
        }
        //}
        return ArrayList(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {

            android.R.id.home ->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_recipe, menu)

        return true
    }
}