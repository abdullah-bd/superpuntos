package com.iotait.superpuntos.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.iotait.superpuntos.R;

public class PopUpDialog extends Dialog implements View.OnClickListener {

    /*Declaration of local variables*/
    private Button okButton;
    private PopUpDialog.OnOkButtonClick onOkButtonClick;

    /*Constructor for the alert dialog*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PopUpDialog(@NonNull Context context, String title, String text) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_popup);
        /*Initializing views*/
        initViews(title, text);
    }

    public PopUpDialog(@NonNull Context context, String title, String text,String button) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_popup);
        /*Initializing views*/
        initViews(title, text, button);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(String title, String text) {
        /*link variables to the view*/
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvBody = findViewById(R.id.tvBody);
        tvBody.setText(text);
        okButton = findViewById(R.id.btnOk);
        okButton.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(String title, String text,String button) {
        /*link variables to the view*/
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvBody = findViewById(R.id.tvBody);
        tvBody.setText(text);
        tvBody.setTextSize(15);
        okButton = findViewById(R.id.btnOk);
        okButton.setText(button);
        okButton.setOnClickListener(this);
    }

    /**Handling clicks*/
    @Override
    public void onClick(View v) {
        if (v==okButton){
            onOkButtonClick.onOkButtonClick();
        }
    }

    /**Interface for done button click*/
    public interface OnOkButtonClick {
        void onOkButtonClick();
    }

    /**Setting the done button click listener*/
    public void setListener(PopUpDialog.OnOkButtonClick onOkButtonClick){
        this.onOkButtonClick = onOkButtonClick;
    }
}
