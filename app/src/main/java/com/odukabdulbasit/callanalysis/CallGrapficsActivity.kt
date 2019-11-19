package com.odukabdulbasit.callanalysis


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class CallGrapficsActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_call_grapfics)


        var barChartView = findViewById<BarChart>(R.id.chartConsumptionGraph)


        val a1 : MoreSpokenModel = intent.getParcelableExtra("a1")
        var a2 : MoreSpokenModel = intent.getParcelableExtra("a2")
        var a3 : MoreSpokenModel = intent.getParcelableExtra("a3")
        var a5 : MoreSpokenModel = intent.getParcelableExtra("a5")
        val a4 : MoreSpokenModel= intent.getParcelableExtra("a4")
        var a6 : MoreSpokenModel = intent.getParcelableExtra("a6")

        val a1Name = a1.name
        val a1Duraction =a1.duraction

        val a2Name = a2.name
        val a2Duraction =a2.duraction

        val a3Name = a3.name
        val a3Duraction =a3.duraction

        val a4Name = a4.name
        val a4Duraction =a4.duraction

        val a5Name = a5.name
        val a5Duraction =a5.duraction

        val a6Name = a6.name
        val a6Duraction =a6.duraction




        val a1FloatDurac = ((a1Duraction /60)).toFloat()
        val a2FloatDurac = ((a2Duraction /60)).toFloat()
        val a3FloatDurac = ((a3Duraction /60)).toFloat()
        val a4FloatDurac = ((a4Duraction /60)).toFloat()
        val a5FloatDurac = ((a5Duraction /60)).toFloat()
        val a6FloatDurac = ((a6Duraction /60)).toFloat()


        val barEntries : ArrayList<BarEntry> = ArrayList()
        barEntries.add(BarEntry(0f, a4FloatDurac))
        barEntries.add(BarEntry(1f, a1FloatDurac))
        barEntries.add(BarEntry(2f, a5FloatDurac))
        barEntries.add(BarEntry(3f, a3FloatDurac))
        barEntries.add(BarEntry(4f,a2FloatDurac))
        barEntries.add(BarEntry(5f, a6FloatDurac))

        val bardataSet = BarDataSet(barEntries, "Oranlar")


        val ismlerCall : ArrayList<String> = ArrayList()
        ismlerCall.add(a4Name)
        ismlerCall.add(a1Name)
        ismlerCall.add(a5Name)
        ismlerCall.add(a3Name)
        ismlerCall.add(a2Name)
        ismlerCall.add(a6Name)


        val theData = BarData(bardataSet)

        val xAxis = barChartView.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setValueFormatter(IndexAxisValueFormatter(ismlerCall))

        barChartView.data = theData

    }
}
