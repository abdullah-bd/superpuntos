package com.iotait.superpuntos.activity.home.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.LoginActivity;
import com.iotait.superpuntos.activity.ProfileActivity;
import com.iotait.superpuntos.activity.home.HomeActivity;
import com.iotait.superpuntos.adapter.ActivitiesAdapter;
import com.iotait.superpuntos.databinding.FragmentPersonalBinding;
import com.iotait.superpuntos.helper.AddOptionsDialog;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.LogoutDialog;
import com.iotait.superpuntos.models.UserActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.ACTIVITY_LIST;
import static com.iotait.superpuntos.helper.Constants.USER_LIST;

public class PersonalFragment extends Fragment implements LogoutDialog.OnYesButtonClick, LogoutDialog.OnNoButtonClick {

    public PersonalFragment() {
    }

    private FragmentPersonalBinding binding;
    private ActivitiesAdapter activitiesAdapter;
    private LogoutDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPersonalBinding.inflate(inflater, container, false);

        populateView();

        setupRecyclerview();

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });

        binding.ivLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        binding.ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "superpuntosapp@gmail.com" });
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                intent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(intent, "Elige Email App"));
            }
        });

        return binding.getRoot();
    }

    private void showDialog() {
        dialog = new LogoutDialog(Objects.requireNonNull(getContext()));
        dialog.setListener(this, this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void populateView() {
        if (Constants.USER.getUri()==null) {
            Glide.with(getContext()).load(R.drawable.user).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);

        }else {

            if (Constants.USER.getUri().equals("") ){
                Glide.with(getContext()).load(R.drawable.user).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
            }else {
                Glide.with(getContext()).load(Constants.USER.getUri()).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
            }

        }
        binding.tvUserName.setText(Constants.USER.getName());
        if (Constants.USER.getPhone() != null){
            binding.tvPhone.setText(Constants.USER.getCountryCode()+Constants.USER.getPhone());
        } else {
            binding.tvPhone.setText(Constants.USER.getEmail());
        }
        binding.coinTv.setText("C$"+Constants.USER.getCoins());

    }

    private void setupRecyclerview() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvUserActivities.setLayoutManager(layoutManager);
        activitiesAdapter = new ActivitiesAdapter(getContext(),ACTIVITY_LIST);
        binding.rvUserActivities.setAdapter(activitiesAdapter);
        if (ACTIVITY_LIST.size() > 0){
            binding.rvUserActivities.setVisibility(View.VISIBLE);
            binding.tvNoActivities.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onYesButtonClick() {

        dialog.dismiss();
        SharedPreferences sharedPref = getContext().getSharedPreferences("login", 0);
        sharedPref.edit().clear().apply();
        startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    @Override
    public void onNoButtonClick() {
        dialog.dismiss();
    }
}
