package com.iotait.superpuntos.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    /**Declaration*/
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitles = new ArrayList<>();

    /**Constructor for the adapter*/
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**Getting fragment*/
    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    /**Getting number of fragment*/
    @Override
    public int getCount() {
        return fragmentListTitles.size();
    }

    /**Getting page title for the fragments*/
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    /**Adding fragment to the viewpager*/
    public void AddFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentListTitles.add(title);
    }
}