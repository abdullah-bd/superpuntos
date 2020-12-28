package com.iotait.superpuntos.activity.home.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.adapter.SurveyAdapter;
import com.iotait.superpuntos.databinding.FragmentHomeBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.Survey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.iotait.superpuntos.helper.Constants.SURVEY_LIST;
import static com.iotait.superpuntos.helper.Constants.SURVEY_LIST_FINAL;
import static com.iotait.superpuntos.helper.Constants.USER;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

public class HomeFragment extends Fragment {
    private FirebaseAnalytics mFirebaseAnalytics;
    public HomeFragment() {
        // Required empty public constructor
    }

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);



        populateView();

        setUpRecyclerview();
        setClickListeners();
        Constants.SURVEYADAPTER.notifyDataSetChanged();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        return binding.getRoot();
    }

    private void populateView() {
        if(USER.getName()!=null){
            String[] args = USER.getName().split(" ");
            binding.userNameTv.setText("Hola, "+args[0]);
            binding.tvDate.setText(getTimeStamp());
            binding.coinTv.setText("C$"+USER.getCoins());
        }
    }

    private void setClickListeners() {
        binding.btnInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* InviteFragment inviteFragment= new InviteFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, inviteFragment)
                        .addToBackStack(null)
                        .commit();*/

               /*

                Bundle params = new Bundle();
                params.putInt("ShareClick",v.getId());

                mFirebaseAnalytics.logEvent("ShareApp",params);

                */



                logEventToFirebase(v);
                USER.setShareCount(USER.getShareCount()+1);
                //Toast.makeText(getContext(), USER.getShareCount()+"", Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference(Constants.TEST+"users/");
                ref.child(USER.getUid()).setValue(USER);
                final Intent intent = new Intent(Intent.ACTION_SEND);
                final String appPackageName = getApplicationContext().getPackageName();
                String appLink="https://play.google.com/store/apps/details?id=com.iotait.superpuntos&ah=SO1CosqlPJ6Rw9UHcnYLoR08yfI&fbclid=IwAR17Qn0Iy5QT8CRfrJ6GGtPRmXMEVQImTEVMr6DrY8oFKu6zOAxBRWWApHw" +appPackageName;

                intent.setType("text/plain");
                String shareBody = "¡Hola! Descarga esta increible app..." + "\n"+""+appLink;
                String shareSub = "APP NAME/TITLE";
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Using"));
            }
        });
    }

    private void logEventToFirebase(View v){


        Bundle params = new Bundle();
        params.putInt("ShareClick", v.getId());
        mFirebaseAnalytics.logEvent("ShareApp", params);


//        Bundle params = new Bundle();
//        params.putInt(FirebaseAnalytics.Param.CONTENT_TYPE,0);
//        params.putInt(FirebaseAnalytics.Param.ITEM_ID,v.getId());
//        params.putInt(FirebaseAnalytics.Param.METHOD, 0);
//
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE,params);
    }

    private void setUpRecyclerview() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvSurvey.setLayoutManager(layoutManager);
        Constants.SURVEYADAPTER = new SurveyAdapter(getContext());
        Constants.SURVEYADAPTER.setHasStableIds(false);
        binding.rvSurvey.setAdapter(Constants.SURVEYADAPTER);

        if (SURVEY_LIST_FINAL.size()==0){
            binding.tvNoSurvey.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateView();
        setUpRecyclerview();
    }
}
