package com.iotait.superpuntos.admin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotait.superpuntos.R;
import com.iotait.superpuntos.adapter.SurveyAdapter;
import com.iotait.superpuntos.admin.activity.AdminAddSurveyActivity;
import com.iotait.superpuntos.databinding.FragmentAdminHomeBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.Survey;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.iotait.superpuntos.helper.Constants.SURVEY_LIST;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeFragment extends Fragment {

    private List<Survey> surveys = new ArrayList<>();

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    private FragmentAdminHomeBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);



        populateView();

        setUpRecyclerview();

        setClickListeners();

        return binding.getRoot();
    }

    private void populateView() {
        binding.tvUserName.setText("Hola, Admin");
        binding.tvDate.setText(getTimeStamp());
    }

    private void setClickListeners() {
        binding.btnAddSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminAddSurveyActivity.class));
            }
        });
    }

    private void setUpRecyclerview() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvSurvey.setLayoutManager(layoutManager);
        Constants.ADMIN_SURVEYADAPTER = new SurveyAdapter(getContext());
        binding.rvSurvey.setAdapter(Constants.ADMIN_SURVEYADAPTER);
        if (SURVEY_LIST.size()==0){
            binding.tvNoSurvey.setVisibility(View.VISIBLE);
        }

    }
}
