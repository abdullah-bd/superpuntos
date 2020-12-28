package com.iotait.superpuntos.activity.cashout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.cashout.fragments.AddNewContactFragment;
import com.iotait.superpuntos.activity.cashout.fragments.PreviousContactFragment;
import com.iotait.superpuntos.activity.intro.fragments.IntroOneFragment;
import com.iotait.superpuntos.activity.intro.fragments.IntroTwoFragment;
import com.iotait.superpuntos.adapter.ViewPagerAdapter;
import com.iotait.superpuntos.databinding.ActivityCashOutBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.PopUpDialog;

import java.util.Objects;

public class CashOutActivity extends AppCompatActivity implements PopUpDialog.OnOkButtonClick {

    ActivityCashOutBinding binding;
    PopUpDialog popUpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCashOutBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        populateView();

        setUpViewpager();
    }

    private void populateView() {
        binding.tvCoin.setText("C$"+ Constants.USER.getCoins());
    }

    private void setUpViewpager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new AddNewContactFragment(), "Agrega nuevo contacto");
        pagerAdapter.AddFragment(new PreviousContactFragment(), "Contacto anterior");
        binding.viewpager.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private void showDialog(String title, String body) {
        popUpDialog = new PopUpDialog(CashOutActivity.this, title, body);
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    @Override
    public void onOkButtonClick() {
        popUpDialog.dismiss();
    }
}
