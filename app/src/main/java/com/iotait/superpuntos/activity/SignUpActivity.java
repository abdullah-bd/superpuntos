package com.iotait.superpuntos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.home.HomeActivity;
import com.iotait.superpuntos.databinding.ActivitySignupBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.FirebaseHelper;
import com.iotait.superpuntos.helper.OtpBottomSheetDialog;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.models.User;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.iotait.superpuntos.helper.Constants.PRIVACY;
import static com.iotait.superpuntos.helper.Constants.TERMS;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

public class SignUpActivity extends AppCompatActivity implements OtpBottomSheetDialog.BottomSheetListener ,PopUpDialog.OnOkButtonClick {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference(Constants.TEST);
    DatabaseReference usersRef ;
    ActivitySignupBinding binding;
    String uid;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    OtpBottomSheetDialog dialog;
    PopUpDialog popUpDialog;
    int dia=0;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth mAuth;
    private String verificationId;
    SimpleDateFormat simpleDateFormat;
    MotionEvent motionEvent;


    /**Verifying with OTP*/
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                /*When verification is completed*/
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    /*getting sent code*/
                    String sentCode = phoneAuthCredential.getSmsCode();
                    /*Checking code is null or not*/
                    if (sentCode != null) {
                        //binding.etCode.setText(sentCode);
                        Toast.makeText(SignUpActivity.this, "¡Verificado exitosamente!.", Toast.LENGTH_SHORT).show();




                        DatabaseReference ref = database.getReference(Constants.TEST+"users/"+Constants.USER.getCountryCode()+Constants.USER.getPhone());

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                User post = dataSnapshot.getValue(User.class);

                                if(post==null){
                                    editor.putString("phone", Constants.USER.getCountryCode()+Constants.USER.getPhone());
                                    editor.putBoolean("session",true);
                                    editor.commit();
                                    usersRef.child(Constants.USER.getCountryCode()+Constants.USER.getPhone()).setValue(Constants.USER);

                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                }else {
                                    Toast.makeText(SignUpActivity.this, "¡Usuario ya existe!", Toast.LENGTH_SHORT).show();

                                }

                            }
                            @Override
                            public void onCancelled(@NotNull DatabaseError databaseError) {

                            }
                        });




                        //session management
                        finish();

                    }else {
                        //Log.d("rememberMePA", "onClick: "+pref.getBoolean("rememberMe", false));
                    }

                }

                /*When verification is failed*/
                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    //editor.putBoolean("rememberMe", false);
                    //editor.apply();
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(SignUpActivity.this, "Solicitud invalida.", Toast.LENGTH_SHORT).show();
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        Toast.makeText(SignUpActivity.this, "Muchas solicitudes. Intenta en un rato.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //Log.d("rememberMePAa", "onClick: " + pref.getBoolean("rememberMe", false));
                }

                /**When code is sent*/
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    resendingToken = forceResendingToken;
                    verificationId = s;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Locale locale = new Locale("es", "ES");
        Locale.setDefault(locale);
        sharedPref = getApplicationContext().getSharedPreferences("login", 0);
        editor = sharedPref.edit();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.etOccupation.setTitle("Elige ocupación");
        binding.etOccupation.setPositiveButton("Cerrar");

        binding.etAge.setTitle("Elige departamento");
        binding.etAge.setPositiveButton("Cerrar");

        binding.spinnerGender.setTitle("Elige género");
        binding.spinnerGender.setPositiveButton("Cerrar");

        simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");
        mAuth = FirebaseAuth.getInstance();
        usersRef = ref.child("users");

        setClickListeners();

    }

    private void setClickListeners() {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                0.0f,
                0.0f,
                0
        );
        //binding.ccp.setCountryForPhoneCode(505);
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();

            }
        });

        binding.tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.tvterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SignUpActivity.this, "Terms and conditions!", Toast.LENGTH_SHORT).show();
                dia=1;
                showTerms();

            }
        });
        binding.tvprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SignUpActivity.this, "Terms and conditions!", Toast.LENGTH_SHORT).show();
                dia=2;
                showPrivacy();

            }
        });
        binding.editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });

        binding.ettOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            binding.etOccupation.onTouch(view,motionEvent);
            }
        });
        binding.etOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               binding.ettOccupation.setText(binding.etOccupation.getSelectedItem().toString());
                binding.ettOccupation.setError(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.ettAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etAge.onTouch(view,motionEvent);
            }
        });
        binding.etAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.ettAge.setText(binding.etAge.getSelectedItem().toString());
                binding.ettAge.setError(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.sspinnerGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.spinnerGender.onTouch(view,motionEvent);
            }
        });
        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.sspinnerGender.setText(binding.spinnerGender.getSelectedItem().toString());
                binding.sspinnerGender.setError(null);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void showPopup() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog =  new DatePickerDialog(this, R.style.MySpinnerDatePickerStyle,new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //Toast.makeText(SignUpActivity.this, ""+monthOfYear, Toast.LENGTH_SHORT).show();
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                binding.editdate.setText(simpleDateFormat.format(newDate.getTime()));
                binding.editdate.setError(null);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Cerrar",datePickerDialog);

        datePickerDialog.show();

    }

    private void showTerms() {
        popUpDialog = new PopUpDialog(SignUpActivity.this, "Terminos y Condiciones", TERMS, "Aceptar");
        popUpDialog.setListener(this);
        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    private void showPrivacy() {
        popUpDialog = new PopUpDialog(SignUpActivity.this, "Politica de Privacidad", PRIVACY, "Aceptar");
        popUpDialog.setListener(this);
        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    private void signUp(){
        String phone = binding.etPhoneNumber.getText().toString();
        String name = binding.etName.getText().toString();
        String password = binding.etPassword.getText().toString();
        //String occupation = binding.etOccupation.getText().toString();String occupation
        String occupation = "";

        if( binding.etOccupation.getSelectedItem()!=null){
            occupation = binding.etOccupation.getSelectedItem().toString();
        }
        String city = "";
        if( binding.etAge.getSelectedItem()!=null){
            city = binding.etAge.getSelectedItem().toString();

        }

        String sex = "";
        if (binding.spinnerGender.getSelectedItem()!=null) {
            sex = binding.spinnerGender.getSelectedItem().toString();
        }
        String ccp = binding.ccp.getFullNumberWithPlus();
        //String age = binding.etAge.getText().toString();

        String age = binding.editdate.getText().toString();
        //Toast.makeText(this, age, Toast.LENGTH_SHORT).show();
        uid = ccp+phone;
        Constants.USER = new User(phone,password,name,occupation,age,sex,ccp,uid, 0, getTimeStamp());
        Constants.USER.setCity(city);

            if (phone.isEmpty()){
                binding.etPhoneNumber.setError("Ingresa un numero valido");
            } else if(password.length()<6){
                binding.etPassword.setError("Muy corta");
            } else if (name.isEmpty()){
                binding.etName.setError("Ingresa tu nombre");
            } else if(age.isEmpty()){
                binding.editdate.setError("Ingresa edad");
            } else if(occupation.isEmpty()){
                binding.ettOccupation.setError("Ingresa ocupacion");
            } else if(sex.isEmpty() ){
                binding.sspinnerGender.setError("invalid sex");
            } else if(city.isEmpty()){
                binding.ettAge.setError("invalid city");
            } else if(!binding.checkBox.isChecked() || !binding.checkBox2.isChecked()){
                if (!binding.checkBox.isChecked()){
                    dia=1;
                    Toast.makeText(this, "Please acceptar Terminos y Condiciones.", Toast.LENGTH_SHORT).show();
                }else if (!binding.checkBox2.isChecked()){
                    dia=2;
                    Toast.makeText(this, "Please acceptar Politica de Privacidad", Toast.LENGTH_SHORT).show();
                }
            } else {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        ccp+phone,
                        60,
                        TimeUnit.SECONDS,
                        this,
                        mCallbacks
                );

                dialog = new OtpBottomSheetDialog();
                dialog.show(getSupportFragmentManager(), "OTP");

            }


    }

    /**Waiting for OTP
     *Running the timer*/
    private void runTimer() {
        new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //binding.tvRemainingTime.setText(MessageFormat.format("Remaining Time : {0}", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                //binding.btnResend.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    /**Signing in after successful OTP verification*/
    private void signInWithCredential(PhoneAuthCredential credential) {
        FirebaseHelper.getInstance().getAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            DatabaseReference ref = database.getReference(Constants.TEST+"users/"+Constants.USER.getCountryCode()+Constants.USER.getPhone());

                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                    User post = dataSnapshot.getValue(User.class);

                                    if(post==null){
                                        editor.putString("phone", Constants.USER.getCountryCode()+Constants.USER.getPhone());
                                        editor.putBoolean("session",true);
                                        editor.commit();
                                        usersRef.child(Constants.USER.getCountryCode()+Constants.USER.getPhone()).setValue(Constants.USER);

                                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                    }else {
                                        Toast.makeText(SignUpActivity.this, "¡Usuario ya existe!", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                @Override
                                public void onCancelled(@NotNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }




    @Override
    public void otpInput(String text) {
        verifyCode(text);
    }

    @Override
    public void startActivityToLogin() {

        finish();
    }

    @Override
    public void resendCode() {
        if (Constants.USER.getUid() != null && resendingToken != null) {

            resendVerificationCode(Constants.USER.getCountryCode()+Constants.USER.getPhone(), resendingToken);
        }
        dialog = new OtpBottomSheetDialog();
        dialog.show(getSupportFragmentManager(), "OTP");
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
        runTimer();
    }



    @Override
    public void onOkButtonClick() {
        if(dia==1){
            binding.checkBox.setChecked(true);
        }else if(dia==2){
            binding.checkBox2.setChecked(true);
        }
        popUpDialog.dismiss();

    }
}
