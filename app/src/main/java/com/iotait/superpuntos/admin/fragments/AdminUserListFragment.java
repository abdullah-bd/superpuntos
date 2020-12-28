package com.iotait.superpuntos.admin.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotait.superpuntos.activity.LoginActivity;
import com.iotait.superpuntos.adapter.UserListAdminAdapter;
import com.iotait.superpuntos.databinding.FragmentAdminAccHistoryBinding;
import com.iotait.superpuntos.databinding.FragmentAdminUserListBinding;
import com.iotait.superpuntos.helper.LogoutDialog;

import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.IS_ADMIN;

public class AdminUserListFragment extends Fragment implements LogoutDialog.OnYesButtonClick, LogoutDialog.OnNoButtonClick {

    public AdminUserListFragment() {
    }

    private FragmentAdminUserListBinding binding;
    private LogoutDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminUserListBinding.inflate(inflater, container, false);

        setUpRecyclerview();

        binding.ivLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return binding.getRoot();
    }

    private void setUpRecyclerview() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvUserList.setLayoutManager(layoutManager);
        UserListAdminAdapter userListAdminAdapter = new UserListAdminAdapter(getContext());
        binding.rvUserList.setAdapter(userListAdminAdapter);
    }

    private void showDialog() {
        dialog = new LogoutDialog(Objects.requireNonNull(getContext()));
        dialog.setListener(this, this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onYesButtonClick() {
        dialog.dismiss();
        SharedPreferences sharedPref = getContext().getSharedPreferences("login", 0);
        sharedPref.edit().clear().apply();
        IS_ADMIN = false;
        startActivity(new Intent(getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onNoButtonClick() {
        dialog.dismiss();
    }
}
