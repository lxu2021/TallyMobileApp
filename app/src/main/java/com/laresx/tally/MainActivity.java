package com.laresx.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.laresx.tally.adapter.AccountAdapter;
import com.laresx.tally.db.AccountBean;
import com.laresx.tally.db.DBManager;
import com.laresx.tally.util.BudgetDialog;
import com.laresx.tally.util.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv; //show today's income and spending detail
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;

    //declare data source
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;

    //header related variables
    View headerView;
    TextView topOutTv,topInTv,topBudgetTv,topConTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTime();

        initView();

        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        //add listview header
        addLVHeaderView();
        todayLv = findViewById(R.id.main_lv);
        mDatas = new ArrayList<>();
        //set adapter to add items to the list
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }

    //initiate original view
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);

        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        Log.i("initView","initiate main view successfully");

        setLongClickListener();
    }

    private void setLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ( i==  0) {//click on the header
                    return false;
                }

                //get clicked item
                int pos = i - 1;
                AccountBean clickBean = mDatas.get(pos);
                int click_id = clickBean.getId();

                //show if delete warning
                showDeleteItemDialog(clickBean);

                return false;
            }
        });
    }

    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning").setMessage("Are you sure to delete this record?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       //execute delete action
                        int click_id = clickBean.getId();
                        DBManager.deleteItemFromAccounttbById(click_id);
                        //refresh in real-time and remove item from collection
                        mDatas.remove(clickBean);
                        adapter.notifyDataSetChanged();
                        setTopTvShow(); //change header tv show content
                    }
                });
        builder.create().show();
    }

    private void addLVHeaderView() {
        //change layout to an object
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //find header sections
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topBudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topBudgetTv.setOnClickListener(this);
        topShowIv.setOnClickListener(this);
        headerView.setOnClickListener(this);
    }

    //get current time
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Log.i("initTime","initiate Time successfully");
    }

    //when activity get focused...
    @Override
    protected void onResume() {
        super.onResume();
        loadDBdata();
        
        setTopTvShow();
    }

    //set content that will show on the header
    private void setTopTvShow() {

        //get today's spending and income total and show on the header
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "Today's Spending $" + outcomeOneDay+"  Income $" + incomeOneDay;
        topConTv.setText(infoOneDay);

        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("$"+incomeOneMonth);
        topOutTv.setText("$"+outcomeOneMonth);

        //set showing the remaining of the budget
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topBudgetTv.setText("$ 0");
        }else{
            float syMoney = bmoney-outcomeOneMonth;
            topBudgetTv.setText("$"+syMoney);
        }
        Log.i("setTopTvShow","Set TopTvshow successfully");
    }

    private void loadDBdata() {
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent it = new Intent(this, SearchActivity.class);
                startActivity(it);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // change show or hide detials
                toggleShow();
                break;
        }
        if(v == headerView){
            //if header is clicked
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }

    //show budget dialog method
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //put budget to the shared data and store
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney", money);
                editor.commit();

                //calculate remaining amount
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;//预算剩余 = 预算-支出
                topBudgetTv.setText("$"+syMoney);
            }
        });
    }

    boolean isShow = true;
    //swift textView visibility by clicking on the eye image
    private void toggleShow() {
        if(isShow){//show --> hide
            //change to hide
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);
            topOutTv.setTransformationMethod(passwordMethod);
            topBudgetTv.setTransformationMethod(passwordMethod);
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        }else{//hide --> show
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topOutTv.setTransformationMethod(hideMethod);
            topInTv.setTransformationMethod(hideMethod);
            topBudgetTv.setTransformationMethod(hideMethod);
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}