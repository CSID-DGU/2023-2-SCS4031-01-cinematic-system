package com.example.fiebasephoneauth.Guardian.page;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import com.example.fiebasephoneauth.databinding.ActivityGuardianActivitiesDetailBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GuardianActivitiesDetail extends AppCompatActivity {

    ActivityGuardianActivitiesDetailBinding binding;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianActivitiesDetailBinding.inflate(getLayoutInflater());
        lineChart = binding.chart;

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0,10));
        entries.add(new Entry(1,25));
        entries.add(new Entry(2,15));
        entries.add(new Entry(3,2));
        entries.add(new Entry(4,20));
        entries.add(new Entry(5,10));
        entries.add(new Entry(6,25));
        entries.add(new Entry(7,74));
        entries.add(new Entry(8,25));
        entries.add(new Entry(9,30));
        entries.add(new Entry(10,40));
        entries.add(new Entry(11,35));
        entries.add(new Entry(12,45));
        entries.add(new Entry(13,50));
        entries.add(new Entry(14,20));
        entries.add(new Entry(15,50));
        entries.add(new Entry(16,35));
        entries.add(new Entry(17,30));
        entries.add(new Entry(18,25));
        entries.add(new Entry(19,10));
        entries.add(new Entry(20,15));
        entries.add(new Entry(21,10));
        entries.add(new Entry(22,5));
        entries.add(new Entry(23,40));

        int startColor = Color.parseColor("#042940");
        int endColor = Color.parseColor("#3FA79D");
        Drawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, new int[]{startColor, endColor}
        );

        LineDataSet lineDataSet = new LineDataSet(entries,"");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setColor(Color.parseColor("#042940"));
        lineDataSet.setLineWidth(4f);
        lineDataSet.setCircleColor(Color.parseColor("#042940"));
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        //lineDataSet.setFillColor(startColor);
        lineDataSet.setFillAlpha(400);
        lineDataSet.setFillDrawable(drawable);
        //lineDataSet.setValueTextSize(20f);
        //lineDataSet.setValueTextColor(Color.parseColor("#042940"));
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);

        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineChart.setBackgroundColor(Color.parseColor("#FAFAFA"));
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setDrawMarkers(false);
        lineChart.setNoDataText("아직 데이터가 없습니다");
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setDrawMarkers(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getLegend().setEnabled(false);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawLabels(true);
        xAxis.enableGridDashedLine(10f,10f,0f);
        xAxis.setLabelCount(8);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(23f);
        xAxis.setTextColor(Color.parseColor("#618FAE"));

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(true);
        yAxis.setGridColor(Color.parseColor("#618FAE"));
        yAxis.enableGridDashedLine(10f,10f,0f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(entries.stream().max((a,b)->(int)(a.getY()-b.getY())).get().getY()+10);
        yAxis.setDrawLabels(true);
        yAxis.setTextColor(Color.parseColor("#618FAE"));

        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();

        binding.back.setOnClickListener(v->{
            finish();
        });

        setContentView(binding.getRoot());

    }


}