package com.abztrakinc.ABZPOS.CASHIER;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.abztrakinc.ABZPOS.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

public class cashier_shift_check_sales extends AppCompatActivity {
    TextView tvR, tvPython, tvCPP, tvJava;
    BarChart M;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);


//        tvR = findViewById(R.id.tvR);
//        tvPython = findViewById(R.id.tvPython);
//        tvCPP = findViewById(R.id.tvCPP);
//        tvJava = findViewById(R.id.tvJava);
        mBarChart = findViewById(R.id.Barchart);

        loadData();


    }

    private void setData()
    {
//
//        tvR.setText(Integer.toString(40));
//        tvPython.setText(Integer.toString(30));
//        tvCPP.setText(Integer.toString(5));
//        tvJava.setText(Integer.toString(25));
//
//
//        pieChart.addPieSlice(
//                new PieModel(
//                        "R",
//                        40,
//                        Color.parseColor("#FFA726")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Python",
//                       30,
//                        Color.parseColor("#66BB6A")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "C++",
//                        10,
//                        Color.parseColor("#EF5350")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Java",
//                        20,
//                        Color.parseColor("#29B6F6")));
//
//        // To animate the pie chart
//        pieChart.startAnimation();
    }


    @Override
    public void onResume() {
        super.onResume();
        mBarChart.startAnimation();
    }




    private void loadData() {
        mBarChart.startAnimation();
        mBarChart.addBar(new BarModel(2.3f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.f,  0xFF1FF4AC));
        mBarChart.addBar(new BarModel(3.3f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(1.1f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.7f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.f,  0xFF1FF4AC));
        mBarChart.addBar(new BarModel(0.4f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(4.f,  0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.3f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.f,  0xFF1FF4AC));
        mBarChart.addBar(new BarModel(3.3f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(1.1f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.7f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(2.f,  0xFF1FF4AC));
        mBarChart.addBar(new BarModel(0.4f, 0xFF1FF4AC));
        mBarChart.addBar(new BarModel(4.f,  0xFF1FF4AC));

    }

    private BarChart mBarChart;
}