package com.iotait.superpuntos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.activity.home.HomeActivity;
import com.iotait.superpuntos.admin.activity.AdminHomeActivity;
import com.iotait.superpuntos.databinding.ActivityLoginBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.Admin;
import com.iotait.superpuntos.models.User;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.iotait.superpuntos.helper.Constants.USER;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseDatabase database;
    DatabaseReference userRef, adminRef;
    ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager = CallbackManager.Factory.create();
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sharedPref = getApplicationContext().getSharedPreferences("login", 0);
        editor = sharedPref.edit();
        if (sharedPref.getBoolean("firsttime", true)) {
            editor.putBoolean("firsttime",false);
            editor.apply();
            /*
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

             */
        }

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Constants.TEST+"users");
        adminRef = database.getReference(Constants.TEST+"admin");
        mAuth = FirebaseAuth.getInstance();

        setUpDialog();

        setClickListeners();


    }

    private void setUpDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Logging in. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    private void setClickListeners() {
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                checkForAdmin(binding.etPhoneNumber.getText().toString(), binding.etPassword.getText().toString(), binding.ccp.getFullNumberWithPlus());
                dialog.dismiss();
            }
        });

        binding.tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        binding.tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPassActivity.class));
            }
        });

        binding.btnFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Tag", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Tag", "facebook:onCancel");
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Tag", "facebook:onError", error);
                        // ...
                    }
                });
            }
        });
    }

    private void login(final String phone, final String pass) {

        dialog.show();
        DatabaseReference ref = database.getReference(Constants.TEST+"users/" + phone);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                User post = dataSnapshot.getValue(User.class);

                if (post != null) {
                    if (post.getPassword().equals(pass)) {

                        Toast.makeText(LoginActivity.this, "¡Inicio de sesion exitosa!", Toast.LENGTH_SHORT).show();
                        editor.putString("phone", phone);
                        editor.putBoolean("session", true);
                        editor.commit();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "¡Contrasena incorrecta!", Toast.LENGTH_SHORT).show();
                    }
                } else if (phone.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingresa numero y contrasena valida.", Toast.LENGTH_SHORT).show();
                } else {

                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "¡Inicio de sesion fallo!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.dismiss();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Tag", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag", "signInWithCredential:success");
                            final FirebaseUser fUser = mAuth.getCurrentUser();
                            userRef.child(fUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Constants.USER = dataSnapshot.getValue(User.class);
                                    } else {
                                        User user = new User(fUser.getPhoneNumber(), fUser.getEmail(), fUser.getDisplayName(), fUser.getUid(), 0, getTimeStamp());
                                        Constants.USER = user;
                                        userRef.child(fUser.getUid()).setValue(user);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            editor.putString("phone", fUser.getUid());
                            editor.putBoolean("session", true);
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Tag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Autenticacion fallo." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void logFbKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void login(String phone, String pass, String cc) {
        boolean b = true;
        for (User user : Constants.USER_LIST) {
            if (phone.isEmpty() || pass.isEmpty()) {
                b=false;
                Toast.makeText(LoginActivity.this, "Ingrese un teléfono o contraseña válidos.", Toast.LENGTH_SHORT).show();
                break;
            } else {

                if (user.getPhone() != null && user.getPassword() != null && user.getCountryCode() != null) {
                    if (user.getPhone().equals(phone) && user.getPassword().equals(pass) && user.getCountryCode().equals(cc)) {
                        b = false;
                        Toast.makeText(LoginActivity.this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();
                        USER = user;
                        editor.putString("phone", user.getUid());
                        editor.putBoolean("session", true);
                        editor.commit();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                        break;
                    } else if (user.getPhone().equals(phone) && user.getCountryCode().equals(cc)) {
                        Toast.makeText(this, "Por favor ingrese la contraseña correcta", Toast.LENGTH_SHORT).show();
                        b = false;
                        break;
                    }
                }
            }
        }
        if (b) {
            Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkForAdmin(final String phone, final String pass, final String cc) {

        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Admin admin = dataSnapshot.getValue(Admin.class);
                String phoneNumber = cc + phone;
                if (phoneNumber.equals(admin.getEmail()) && pass.equals(admin.getPassword())) {
                    editor.putBoolean("admin", true);
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                    finish();
                } else {
                    login(binding.etPhoneNumber.getText().toString(), binding.etPassword.getText().toString(), binding.ccp.getFullNumberWithPlus());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}