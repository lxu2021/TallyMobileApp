package com.laresx.tally.frag_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.laresx.tally.R;
import com.laresx.tally.adapter.ChartItemAdapter;
import com.laresx.tally.db.ChartItemBean;
import com.laresx.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChartFragment extends Fragment {
    ListView chartLv;
    int year, month;
    List<ChartItemBean> mDatas;
    private ChartItemAdapter itemAdapter;
    BarChart barChart;
    TextView chartTv; // if no spending or income show textview

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incom_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);

        //get data passed by activity
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        //set database
        mDatas = new ArrayList<>();

        //set adapter
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(itemAdapter);

        //add header
        addLVHeaderView();
        return view;
    }

    protected void addLVHeaderView(){
        //change layout to inflate
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top,null);//add header view to the listview
        chartLv.addHeaderView(headerView);

        //find header components
        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
        barChart.getDescription().setEnabled(false);
        barChart.setExtraOffsets(20, 20, 20, 20);
        setAxis(year,month);
        setAxisData(year, month);
    }

    //set axis data
    protected abstract void setAxisData(int year, int month);

    //set barchart axis
    protected void setAxis(int year, final int month){
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//set x axis
        xAxis.setDrawGridLines(true);//set axis grid
        //set x axis label count
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f); //set label size

        //set axis format
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {
                    return month+"-1";
                }
                if (val==14) {
                    return month+"-15";
                }
                if (month==2) {
                    if (val == 27) {
                        return month+"-28";
                    }
                }else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
                    if (val == 30) {
                        return month+"-31";
                    }
                }else if(month==4||month==6||month==9||month==11){
                    if (val==29) {
                        return month+"-30";
                    }
                }
                return "";
            }
        });
        xAxis.setYOffset(10);

        //set y axis
        setYAxis(year, month);
    }

    protected abstract void setYAxis(int year,int month);

    public void setDate(int year, int month){
        this.year = year;
        this.month = month;
        barChart.clear();
        barChart.invalidate();
        setAxis(year,month);
        setAxisData(year,month);
    };

    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();

    }
}
