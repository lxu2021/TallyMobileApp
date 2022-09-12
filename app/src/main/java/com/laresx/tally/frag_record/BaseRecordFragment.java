package com.laresx.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.laresx.tally.R;
import com.laresx.tally.db.AccountBean;
import com.laresx.tally.db.TypeBean;
import com.laresx.tally.util.BeiZhuDialog;
import com.laresx.tally.util.KeyBoardUtils;
import com.laresx.tally.util.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;   //store the data that should be kept in tally as an object

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        accountBean.setTypename("Others");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //load data to gv
        loadDataToGV();
        setGVListener();
        return view;
    }

    //get current time and show at time_tv
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();  //提示绘制发生变化了
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);
                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);
            }
        });
    }

    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);

        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //show keyboard
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();

        //set interface and onclick listener "enter"
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //get input amount
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);
                //get data and store in the database
                saveAccountToDB();
                // return to upperlevel
                getActivity().finish();
            }
        });

    }

    //require extended class to reuse this method
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }

    private void showTimeDialog(){
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();

        //set onclicklistener of the confirm button
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    //show description dialog
    public void showBZDialog(){
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }
}