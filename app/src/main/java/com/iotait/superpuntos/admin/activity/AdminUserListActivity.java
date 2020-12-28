package com.iotait.superpuntos.admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.cashout.fragments.AddNewContactFragment;
import com.iotait.superpuntos.activity.cashout.fragments.PreviousContactFragment;
import com.iotait.superpuntos.adapter.ViewPagerAdapter;
import com.iotait.superpuntos.admin.fragments.AdminAccDetailsFragment;
import com.iotait.superpuntos.admin.fragments.AdminAccHistoryFragment;
import com.iotait.superpuntos.databinding.ActivityAdminUserlistBinding;
import com.iotait.superpuntos.models.User;

import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.USER_LIST;

public class AdminUserListActivity extends AppCompatActivity {

    ActivityAdminUserlistBinding binding;
    public int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUserlistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        position = getIntent().getIntExtra("Position", 0);

        populateView();

        setUpViewPager();
    }

    private void populateView() {
        User user = USER_LIST.get(position);
        binding.tvUserName.setText(user.getName());
        if (user.getPhone() != null){
            binding.tvPhone.setText(user.getCountryCode()+user.getPhone());
        } else {
            binding.tvPhone.setText(user.getEmail());
        }

        if (user.getUri()==null) {
            Glide.with(AdminUserListActivity.this).load(R.drawable.user).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
        }else {
            if (user.getUri().equals("")){
                Glide.with(AdminUserListActivity.this).load(R.drawable.user).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
            }else {
                Glide.with(AdminUserListActivity.this).load(user.getUri()).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
            }
        }



        binding.tvTotalClaimed.setText("C$"+ user.getTotalClaimed());
        binding.tvLastWithdraw.setText("Last withdraw\n"+user.getLastWithdraw());

    }

    private void setUpViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new AdminAccDetailsFragment(),"Account details");
        pagerAdapter.AddFragment(new AdminAccHistoryFragment(), "Account history");
        binding.viewpager.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }
}
