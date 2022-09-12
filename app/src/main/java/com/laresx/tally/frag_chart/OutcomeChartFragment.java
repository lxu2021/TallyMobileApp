package com.laresx.tally.frag_chart;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.laresx.tally.db.BarChartItemBean;
import com.laresx.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class OutcomeChartFragment extends BaseChartFragment{
    int kind = 1;
    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, 0);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year,month);
        loadData(year,month, kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        //get every month every day amount
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if(list.size() == 0){
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        }else{
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            List<BarEntry> barEntries1 = new ArrayList<>();
            for(int i = 0; i < 31;i++){
                //initiate bar size
                BarEntry entry = new BarEntry(i,0.0f);
                barEntries1.add(entry);
            }

            for(int i = 0; i < list.size();i++){
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay(); // get date
                //get x position according to the day
                int xIndex = day - 1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSummoney());
            }
            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // value color
            barDataSet1.setValueTextSize(8f); // value size
            barDataSet1.setColor(Color.RED); // bar color

            barDataSet1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    // default to get one decimal
                    if (value==0) {
                        return "";
                    }
                    return value + "";
                }
            });
            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // set bar width
            barChart.setData(barData);
        }

    }

    @Override
    protected void setYAxis(int year, int month) {
    //get maximum amount of current month and set as the maximum of the axis
        float maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney); //get ceiling of max amount

        //set y axis
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);//set maximum
        yAxis_right.setAxisMaximum(0f);//set minimum
        yAxis_right.setEnabled(false);//no show of right y axis

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }
}