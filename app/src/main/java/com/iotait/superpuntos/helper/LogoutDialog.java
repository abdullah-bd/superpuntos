package com.iotait.superpuntos.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.iotait.superpuntos.R;

public class LogoutDialog extends Dialog implements View.OnClickListener {

    /*Declaration of local variables*/
    private Button btnYes, btnNo;
    private LogoutDialog.OnYesButtonClick onYesButtonClick;
    private LogoutDialog.OnNoButtonClick onNoButtonClick;


    /*Constructor for the alert dialog*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LogoutDialog(@NonNull Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_logout);
        /*Initializing views*/
        initViews();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LogoutDialog(@NonNull Context context,String title,String subTitle) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_logout);
        /*Initializing views*/
        initViews(title,subTitle);
    }

    /**Initializing the views*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        /*link variables to the view*/
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(String title,String subTitle) {
        /*link variables to the view*/
        TextView title1 = findViewById(R.id.tvHeader);
        title1.setText(title);
        TextView text = findViewById(R.id.etOption);
        text.setText(subTitle);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }

    /**Handling clicks*/
    @Override
    public void onClick(View v) {
        if (v==btnYes){
            onYesButtonClick.onYesButtonClick();
        } else if (v==btnNo){
            onNoButtonClick.onNoButtonClick();
        }
    }

    /**Interface for done button click*/
    public interface OnYesButtonClick {
        void onYesButtonClick();
    }

    public interface OnNoButtonClick {
        void onNoButtonClick();
    }

    /**Setting the done button click listener*/
    public void setListener(LogoutDialog.OnYesButtonClick onYesButtonClick, LogoutDialog.OnNoButtonClick onNoButtonClick){
        this.onYesButtonClick = onYesButtonClick;
        this.onNoButtonClick = onNoButtonClick;
    }
}
