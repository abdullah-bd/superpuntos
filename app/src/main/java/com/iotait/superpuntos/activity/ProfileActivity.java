package com.iotait.superpuntos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.databinding.ActivityProfileBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.LogoutDialog;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.models.User;

import java.util.Objects;
import java.util.UUID;

import static android.view.View.VISIBLE;
import static com.iotait.superpuntos.helper.Constants.PRIVACY;
import static com.iotait.superpuntos.helper.Constants.TERMS;

public class ProfileActivity extends AppCompatActivity implements LogoutDialog.OnYesButtonClick, LogoutDialog.OnNoButtonClick,PopUpDialog.OnOkButtonClick{

    StorageReference ref;
    UploadTask uploadTask;
    ActivityProfileBinding binding;
    private static final int PICK_IMAGE_CONTENT = 1001;
    Uri imageUriAfterPick;
    LogoutDialog dialog;
    PopUpDialog popUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if(Constants.USER!=null){
            binding.etName.setText(Constants.USER.getName());
            if(Constants.USER.getEmail()!=null){
                binding.etMail.setText(Constants.USER.getEmail());
            } else {
                binding.ccp.setCountryForPhoneCode(Integer.parseInt(Constants.USER.getCountryCode()));
                binding.etPhoneNumber.setText(Constants.USER.getPhone());
            }
        }


        if (Constants.USER.getUri()==null) {
            Glide.with(ProfileActivity.this).load(R.drawable.user).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilepic);

        }else {

            if (Constants.USER.getUri().equals("") ){
                Glide.with(ProfileActivity.this).load(R.drawable.user).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilepic);
            }else {
                Glide.with(ProfileActivity.this).load(Constants.USER.getUri()).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profilepic);
            }

        }

        binding.btnSave.setOnClickListener(v -> {
            if(binding.etName.getText().toString().isEmpty()){
                binding.etName.setError("Ingresa tu nombre");
            }else if (binding.etPhoneNumber.getText().toString().isEmpty()){
                binding.etPhoneNumber.setError("Ingresa tu numero");
            } else {
                Constants.USER.setName(binding.etName.getText().toString());
                Constants.USER.setCountryCode(binding.ccp.getSelectedCountryCodeWithPlus());
                Constants.USER.setPhone(binding.etPhoneNumber.getText().toString());
                Constants.USER.setEmail(binding.etMail.getText().toString());
                //Constants.PROFILEPHONE=binding.ccp.getSelectedCountryCodeWithPlus()+binding.etPhoneNumber.getText();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference(Constants.TEST+"users/");
                ref.child(Constants.USER.getUid()).setValue(Constants.USER);
                //Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                showDialog("¡Exito!","Perfil actualizado exitosamente.");
            }
        });

        binding.btnDelete.setOnClickListener(view1 -> createDialog());

        binding.profilepic.setOnClickListener(view13 -> choosePhoto());

        binding.tvtermsandc.setOnClickListener(view12 -> showTerms());

        binding.tvpriv.setOnClickListener(view14 -> showPrivacy());

        binding.checkBox2.setOnClickListener(view15 -> {
            binding.checkBox2.setChecked(true);
            showTerms(" ");
        });

        binding.checkBox22.setOnClickListener(view16 -> {
            binding.checkBox22.setChecked(true);
            showTerms(" ");
        });

    }
    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Elige foto de perfil"), PICK_IMAGE_CONTENT);
    }

    private void uploadPic() {

        final ProgressDialog mDialog = new ProgressDialog(ProfileActivity.this);
        mDialog.setMessage("Uploading Item");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        ref = FirebaseStorage.getInstance().getReference().child(Constants.TEST+"Userphoto/"+ Constants.USER.getUid());

        uploadTask = ref.putFile(imageUriAfterPick);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mDialog.dismiss();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Constants.USER.setUri(uri.toString());
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference(Constants.TEST+"users/");
                        ref.child(Constants.USER.getUid()).setValue(Constants.USER);
                        Toast.makeText(ProfileActivity.this, "¡Imagen subida exitosamente!", Toast.LENGTH_SHORT).show();


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                mDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CONTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUriAfterPick = data.getData();

            binding.profilepic.setImageURI(imageUriAfterPick);
            uploadPic();
        }
    }

    private void createDialog() {

        dialog = new LogoutDialog(ProfileActivity.this,"¿Borrar?","¿Seguro que quieres borrar tu cuenta?");
        dialog.setListener(this, this);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onYesButtonClick() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Constants.TEST+"users/");
        ref.child(Constants.USER.getUid()).removeValue();
        Constants.USER=null;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", 0);
        sharedPref.edit().clear().apply();
        startActivity(new Intent(ProfileActivity.this,SplashActivity.class));
        finish();
    }

    @Override
    public void onNoButtonClick() {
        dialog.dismiss();
    }

    private void showDialog(String title, String body) {
        popUpDialog = new PopUpDialog(ProfileActivity.this, title, body);
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    @Override
    public void onOkButtonClick() {
        popUpDialog.dismiss();
    }

    private void showTerms() {

        popUpDialog = new PopUpDialog(ProfileActivity.this, "Terminos y Condiciones", Constants.TERMS, "Aceptar");
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    private void showTerms(String st) {

        popUpDialog = new PopUpDialog(ProfileActivity.this, "¡Error!", "Es necesario que todos los usuarios accepted nuestros terminos y condiciones.");
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    private void showPrivacy() {
        popUpDialog = new PopUpDialog(ProfileActivity.this, "Politica de Privacidad", PRIVACY, "Aceptar");
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }
}
