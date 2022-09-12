package com.laresx.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.laresx.tally.adapter.AccountAdapter;
import com.laresx.tally.db.AccountBean;
import com.laresx.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountBean> mDatas;
    AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        searchLv.setAdapter(adapter);
        //when there is no content found
        searchLv.setEmptyView(emptyTv);
    }

    private void initView() {
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:
                String msg = searchEt.getText().toString().trim();
                //check if input is empty, if so show warning
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"Input cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //start to search
                List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(msg);
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;

        }
    }
}