package com.uow.snazzikiel.prepareo;
/**********************************************
 * CSIT321 - Prepareo
 * Author/s:		David
 ***********************************************/

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;


/**
    Class:   statisticsTabOne
    ---------------------------------------
     **NOT USED **
    Graphic Statistical Data is not used in this application.
    TO DO: This is placed on the futures list.
*/
public class statisticsTabOne extends Fragment {
    private static final String TAG = "Tab1Fragment";

    private Button btnTEST;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_tab1,container,false);

        //createPieChartRed(view);
        //createPieChartWhite(view);
        /*btnTEST = (Button) view.findViewById(R.id.btnTEST);

        btnTEST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
            }
        });
        */
        return view;
    }

    /*public void createPieChartRed(View v){

        PieChartView pieChartView = v.findViewById(R.id.chart1);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(75, R.color.colorWhite));
        pieData.add(new SliceValue(25, R.color.colorLightBlue));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1("70%").setCenterText1FontSize(18).setCenterText1Color(R.color.colorBlack);

        pieChartView.setPieChartData(pieChartData);
    }

    public void createPieChartWhite(View v){

        int chartValue = 70;

        PieChartView pieChartView = v.findViewById(R.id.chart2);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(chartValue, R.color.colorRed));
        pieData.add(new SliceValue((100 - chartValue), R.color.colorLightBlue));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasCenterCircle(true).setCenterText1(Integer.toString(chartValue)).setCenterText1FontSize(18).setCenterText1Color(R.color.colorBlack);

        pieChartView.setPieChartData(pieChartData);
    }*/
}

