package com.laresx.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.laresx.tally.adapter.ChartVPAdapter;
import com.laresx.tally.db.DBManager;
import com.laresx.tally.frag_chart.IncomeChartFragment;
import com.laresx.tally.frag_chart.OutcomeChartFragment;
import com.laresx.tally.util.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    Button inBtn, outBtn;
    TextView dateTv, inTv, outTv;
    ViewPager chartVp;
    int year, month;

    int selectPos = -1, selectMonth = -1;

    List<Fragment> chartFragList;
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        //add fragment object
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();

        //add data to the fragment
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);

        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);

        //use adapter
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);

        //put fragment to the activity

    }

    //initiate time method
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    //initiate time method
    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);

        String monthStr = month + "";
        if(month < 10){
            monthStr = "0" + month;
        }
        dateTv.setText(monthStr + "/" + year + " Bill");
        inTv.setText("Income: " + incountItemOneMonth + "   Total Amount: $" + inMoneyOneMonth);
        outTv.setText("Spending: " + outcountItemOneMonth + "   Total Amount: $" + outMoneyOneMonth);

    }

    //initiate sections
    private void initView() {
        inBtn =findViewById(R.id.chart_btn_in);
        outBtn =findViewById(R.id.chart_btn_out);
        dateTv =findViewById(R.id.chart_tv_date);
        inTv =findViewById(R.id.chart_tv_in);
        outTv =findViewById(R.id.chart_tv_out);
        chartVp =findViewById(R.id.chart_vp);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_iv_rili:
                showCalendarDialog();
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
        }
    }

    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPos, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year,month);
                incomeChartFragment.setDate(year,month);
                outcomeChartFragment.setDate(year,month);
            }
        });
    }

    //set the button style
    private void setButtonStyle(int kind){
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        }else if (kind == 1){
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }
}