package com.laresx.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.laresx.tally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    Button ensureBtn;
    EditText moneyEt;

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnEnsureListener{
        public void onEnsure(float money);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);
        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        moneyEt = findViewById(R.id.dialog_budget_et);
        ensureBtn = findViewById(R.id.dialog_budget_btn_ensure);

        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_budget_iv_error:
                cancel();
                break;
            case R.id.dialog_budget_btn_ensure:
                String data = moneyEt.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    Toast.makeText(getContext(),"Please input a budget",Toast.LENGTH_SHORT).show();
                    return;
                }
                float money = Float.parseFloat(data);
                if(money <= 0){
                    Toast.makeText(getContext(),"Budget must be more than 0",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(onEnsureListener != null){
                    onEnsureListener.onEnsure(money);
                }

                cancel();
                break;
        }
    }
    //set dialog size to match screen size
    public void setDialogSize(){
        //get window object and parameters
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();

        //set to the screen size
        wlp.width = (int) (d.getWidth());
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
        handler.sendEmptyMessageDelayed(1, 100);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg){
            //auto show keyboard
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, inputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
















