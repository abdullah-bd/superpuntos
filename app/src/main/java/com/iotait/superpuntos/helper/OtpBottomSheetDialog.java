package com.iotait.superpuntos.helper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.LoginActivity;
import com.iotait.superpuntos.activity.SignUpActivity;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class OtpBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.bottom_sheet_otp, container, false);

        TextView button1 = v.findViewById(R.id.tvBackToSignUp);
        TextView button2 = v.findViewById(R.id.tvResend);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startActivityToLogin();
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.resendCode();
                dismiss();
            }
        });
        OtpView otp = v.findViewById(R.id.otp_view);
        otp.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                mListener.otpInput(otp);
            }
        });


        return v;
    }

    public void setPin(String st){

    }
    public interface BottomSheetListener {
        void otpInput(String text);
        void startActivityToLogin();
        void resendCode();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
