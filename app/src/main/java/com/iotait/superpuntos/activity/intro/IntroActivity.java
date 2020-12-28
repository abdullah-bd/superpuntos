package com.iotait.superpuntos.activity.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.iotait.superpuntos.activity.LoginActivity;
import com.iotait.superpuntos.activity.intro.fragments.IntroOneFragment;
import com.iotait.superpuntos.activity.intro.fragments.IntroTwoFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("firsttime",false);
        editor.commit();
        //IntroOneFragment fragment = new IntroOneFragment();
        IntroTwoFragment fragment1 = new IntroTwoFragment();
        //addSlide(fragment);
        addSlide(fragment1);

        // Hide Skip/Done button.
        showSkipButton(false);
        showDoneButton(false);
        setColorDoneText(Color.parseColor("#8CC63F"));


        setIndicatorColor(Color.parseColor("#FFFFFFFF"), Color.parseColor("#FFFFFFFF"));

        setBarColor (Color.TRANSPARENT);
        setSeparatorColor(Color.TRANSPARENT);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
