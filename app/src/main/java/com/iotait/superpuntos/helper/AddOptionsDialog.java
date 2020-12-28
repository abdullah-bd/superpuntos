package com.iotait.superpuntos.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.iotait.superpuntos.R;

public class AddOptionsDialog extends Dialog implements View.OnClickListener {

    /*Declaration of local variables*/
    private String title;
    private String text;
    private Button buttonDone;
    private EditText etOption;
    private TextView tvTitle;
    private OnAddButtonClick onAddButtonClick;

    /*Constructor for the alert dialog*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AddOptionsDialog(@NonNull Context context, String title, String text) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_add_option);
        this.title = title;
        this.text = text;
        /*Initializing views*/
        initViews();
    }

    /**Initializing the views*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        /*link variables to the view*/
        tvTitle = findViewById(R.id.tvHeader);
        tvTitle.setText(title);
        etOption = findViewById(R.id.etOption);
        etOption.setText(text);
        buttonDone = findViewById(R.id.btnDone);
        buttonDone.setOnClickListener(this);
    }

    /**Handling clicks*/
    @Override
    public void onClick(View v) {
        if (v==buttonDone){
            onAddButtonClick.onAddButtonClick(etOption.getText().toString());
        }
    }

    /**Interface for done button click*/
    public interface OnAddButtonClick {
        void onAddButtonClick(String option);
    }

    /**Setting the done button click listener*/
    public void setListener(OnAddButtonClick onAddButtonClick){
        this.onAddButtonClick = onAddButtonClick;
    }
}
