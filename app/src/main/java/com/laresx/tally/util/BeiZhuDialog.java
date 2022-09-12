package com.laresx.tally.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;

import com.laresx.tally.R;

import android.os.Handler;

public class BeiZhuDialog extends Dialog implements View.OnClickListener {
    EditText et;
    Button cancelBtn, ensureBtn;
    OnEnsureListener onEnsureListener;
    //set a method of recalling interface

    public BeiZhuDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_beizhu); //set dialog view design
        et = findViewById(R.id.dialog_beizhu_et);
        cancelBtn = findViewById(R.id.dialog_beizhu_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_beizhu_btn_ensure);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }

    public OnEnsureListener getOnEnsureListener() {
        return onEnsureListener;
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_beizhu_btn_cancel:
                cancel();
                break;
            case R.id.dialog_beizhu_btn_ensure:
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure();
                }
                break;
        }

    }

    //get input data method
    public String getEditText(){
        return et.getText().toString().trim();
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
