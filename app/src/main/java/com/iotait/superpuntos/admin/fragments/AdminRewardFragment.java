package com.iotait.superpuntos.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotait.superpuntos.R;
import com.iotait.superpuntos.adapter.RewardAdminAdapter;
import com.iotait.superpuntos.adapter.SurveyAdapter;
import com.iotait.superpuntos.databinding.FragmentAdminHomeBinding;
import com.iotait.superpuntos.databinding.FragmentAdminRewardBinding;

import static com.iotait.superpuntos.helper.Constants.CLAIMED_LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminRewardFragment extends Fragment {

    public AdminRewardFragment() {
        // Required empty public constructor
    }

    private FragmentAdminRewardBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminRewardBinding.inflate(inflater, container, false);

        setUpRecyclerview();

        return binding.getRoot();
    }

    private void setUpRecyclerview() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvRewardClaimed.setLayoutManager(layoutManager);
        RewardAdminAdapter rewardAdminAdapter = new RewardAdminAdapter(getContext());
        binding.rvRewardClaimed.setAdapter(rewardAdminAdapter);
        if (CLAIMED_LIST.size() > 0){
            binding.rvRewardClaimed.setVisibility(View.VISIBLE);
            binding.tvNoClaim.setVisibility(View.INVISIBLE);
        } else {
            binding.rvRewardClaimed.setVisibility(View.INVISIBLE);
            binding.tvNoClaim.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerview();
    }
}
