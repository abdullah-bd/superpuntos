package com.iotait.superpuntos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.databinding.ActivityForgetPassBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.User;

import java.util.concurrent.TimeUnit;

import static com.iotait.superpuntos.helper.Constants.USER_LIST;

public class ForgetPassActivity extends AppCompatActivity {

    ActivityForgetPassBinding binding;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPassBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setClickListener();
    }

    private void setClickListener() {

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etPhoneNumber.getText())){
                    binding.etPhoneNumber.setError("Ingresa tu numero");
                    binding.etPhoneNumber.requestFocus();
                } else {
                    binding.pb.setVisibility(View.VISIBLE);
                    binding.btnVerify.setEnabled(false);
                    checkForUser();
                }
            }
        });

        binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.etNewPassword.getText())){
                    binding.etNewPassword.setError("Ingresa una contrasena primero");
                    binding.etNewPassword.requestFocus();
                } else if (TextUtils.isEmpty(binding.etConfirmPassword.getText())){
                    binding.etConfirmPassword.setError("Confirma tu contrasena");
                    binding.etConfirmPassword.requestFocus();
                } else if (!(binding.etNewPassword.getText().toString().equals(binding.etConfirmPassword.getText().toString()))){
                    Log.e("TAG", "onClick: "+binding.etNewPassword.getText().toString());
                    Log.e("TAG", "onClick: "+binding.etConfirmPassword.getText().toString() );
                    binding.etConfirmPassword.setError("Password doesn't match");
                    binding.etConfirmPassword.requestFocus();
                } else {
                    changePassword();
                }
            }
        });
    }

    private void checkForUser() {
        uid = binding.ccp.getFullNumberWithPlus()+binding.etPhoneNumber.getText().toString().trim();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    verify();
                } else {
                    binding.etPhoneNumber.setError("Usuario no existe");
                    binding.etPhoneNumber.requestFocus();
                    binding.pb.setVisibility(View.INVISIBLE);
                    binding.btnVerify.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void changePassword() {
        ProgressDialog pd = new ProgressDialog(ForgetPassActivity.this);
        pd.setMessage("Updating Password");
        pd.show();
        uid = binding.ccp.getFullNumberWithPlus()+binding.etPhoneNumber.getText().toString().trim();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(uid).child("password");
        userRef.setValue(binding.etConfirmPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fetchUserList();
                Toast.makeText(ForgetPassActivity.this, "Contrasena cambiada exitosamente ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void verify() {
        uid = binding.ccp.getFullNumberWithPlus()+binding.etPhoneNumber.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                uid,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                /*When verification is completed*/
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    binding.rlVerificationView.setVisibility(View.GONE);
                    binding.llPassChangeView.setVisibility(View.VISIBLE);
                }

                /*When verification is failed*/
                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(ForgetPassActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    binding.pb.setVisibility(View.INVISIBLE);
                    binding.btnVerify.setEnabled(true);
                }

                /**When code is sent*/
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                }
            };

    private void fetchUserList() {
        USER_LIST.clear();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users");
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                USER_LIST.add(user);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
