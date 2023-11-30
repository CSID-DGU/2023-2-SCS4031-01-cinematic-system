package com.example.fiebasephoneauth.Guardian.page;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fiebasephoneauth.R;
import com.example.fiebasephoneauth.databinding.ActivityGuardianActivitiesDetailBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GuardianActivitiesDetail extends AppCompatActivity {

    ActivityGuardianActivitiesDetailBinding binding;
    LineChart lineChart;

    List<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuardianActivitiesDetailBinding.inflate(getLayoutInflater());
        lineChart = binding.chart;

        // Firebase에서 데이터 가져오기
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CareReceiver_list/abcd/ActivityData/activity");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entries.clear(); // 기존 데이터를 지우고 새로운 데이터로 채움

                for (int i = 0; i < 24; i++) {
                    // cnt_list에 해당 시간의 데이터가 있는지 확인
                    if (dataSnapshot.child("cnt_list").hasChild(String.valueOf(i))) {
                        // 해당 시간의 cnt 값을 가져오기
                        String cntAsString = String.valueOf(dataSnapshot.child("cnt_list").child(String.valueOf(i)).getValue());

                        // 디버그 로그 출력
                        Log.d("FirebaseData", "Value for " + i + ": " + cntAsString);

                        // 문자열이 비어있는지 체크하고, 비어있으면 0으로 처리
                        int cnt = cntAsString.isEmpty() ? 0 : Integer.parseInt(cntAsString);

                        // Entry에 데이터 추가
                        entries.add(new Entry(i, cnt));
                    }
                }

                // 그래프 그리기
                drawGraph(entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });

        int startColor = Color.parseColor("#042940");
        int endColor = Color.parseColor("#3FA79D");
        Drawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, new int[]{startColor, endColor}
        );

        LineDataSet lineDataSet = new LineDataSet(entries, "");
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
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setLabelCount(8);
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum(23f);
        xAxis.setTextColor(Color.parseColor("#618FAE"));

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawAxisLine(false);
        yAxis.setDrawGridLines(true);
        yAxis.setGridColor(Color.parseColor("#618FAE"));
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(entries.stream().max((a, b) -> (int) (a.getY() - b.getY())).map(Entry::getY).orElse(80f) + 10);
        yAxis.setDrawLabels(true);
        yAxis.setTextColor(Color.parseColor("#618FAE"));

        lineChart.getAxisRight().setEnabled(false);

        lineChart.invalidate();

        binding.back.setOnClickListener(v -> {
            finish();
        });

        setContentView(binding.getRoot());
    }

    private void drawGraph(List<Entry> entries) {
        Log.d("GraphData", "Number of entries: " + entries.size());
        LineDataSet dataSet = new LineDataSet(entries, "Hourly Activity"); // 그래프 라벨 설정
        LineData lineData = new LineData(dataSet);

        // Note: Use the member variable lineChart instead of finding it again
        lineChart.setData(lineData);

        // x축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(24, true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"}));

        // 그래프 업데이트
        lineChart.invalidate();
    }
}

