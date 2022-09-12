package com.laresx.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.laresx.tally.adapter.AccountAdapter;
import com.laresx.tally.db.AccountBean;
import com.laresx.tally.db.DBManager;
import com.laresx.tally.util.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    ListView historyLv;
    TextView timeTv;

    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month;
    int dialogSelPos = -1;
    int dialogSelMonth = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);
        mDatas = new ArrayList<>();
        // set adapter
        adapter = new AccountAdapter(this,mDatas);
        historyLv.setAdapter(adapter);
        initTime();
        timeTv.setText(month + "/" + year);
        loadData(year,month);
        setLVClickListner();
    }

    //set listview longclick listener
    private void setLVClickListner() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AccountBean accountBean = mDatas.get(i);
                deleteItem(accountBean);
                return false;
            }
        });

    }

    private void deleteItem(AccountBean accountBean) {
        int delId = accountBean.getId();
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("Warning").setMessage("Are you sure to delete this record?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBManager.deleteItemFromAccounttbById(delId);
                        mDatas.remove(accountBean);
                        adapter.notifyDataSetChanged();
                    }
                });
        builder.create().show();
    }


    /* get specific month bill*/
    private void loadData(int year,int month) {
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccounttb(year, month);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_rili:
                CalendarDialog dialog = new CalendarDialog(this,dialogSelPos,dialogSelMonth);
                dialog.show();
                dialog.setDialogSize();
                dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                    @Override
                    public void onRefresh(int selPos, int year, int month) {
                        timeTv.setText(month + "/" + year);
                        loadData(year,month);
                        dialogSelPos = selPos;
                        dialogSelMonth = month;
                    }
                });
                break;
        }
    }
}
