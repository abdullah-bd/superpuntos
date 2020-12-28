package com.iotait.superpuntos.activity.home.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotait.superpuntos.R;
import com.iotait.superpuntos.databinding.FragmentInviteBinding;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

public class InviteFragment extends Fragment {

    public InviteFragment() {
    }

    private FragmentInviteBinding binding;
    private final String appPackageName = getApplicationContext().getPackageName();
    private String appLink="https://play.google.com/store/apps/details?id=" +appPackageName;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInviteBinding.inflate(inflater, container, false);

        setClickListener();
        return binding.getRoot();
    }

    private void setClickListener() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        binding.ivGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etEmailOrPhone.getText())){
                    binding.etEmailOrPhone.setError("Ingresa un numero o correo");
                    binding.etEmailOrPhone.requestFocus();
                } else{
                    if (isEmailValid(binding.etEmailOrPhone.getText().toString().trim())){
                        openEmail((binding.etEmailOrPhone.getText().toString().trim()));
                    } else if (isValidMobile(binding.etEmailOrPhone.getText().toString().trim())){
                        openMessage((binding.etEmailOrPhone.getText().toString().trim()));
                    } else {
                        binding.etEmailOrPhone.setError("Ingresa un numero o correo valido");
                        binding.etEmailOrPhone.requestFocus();
                    }
                }
            }
        });
    }

    private void openMessage(String number) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "smsto",number, null));
        smsIntent.putExtra("sms_body", "Hola, ¡Descarga esta increible app!\n"+appLink);
        startActivity(Intent.createChooser(smsIntent, "Send sms..."));
    }

    private void openEmail(String mail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",mail, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Survey App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hola, ¡Descarga esta increible app!\n"+appLink);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private boolean isEmailValid(String email) {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }
}
