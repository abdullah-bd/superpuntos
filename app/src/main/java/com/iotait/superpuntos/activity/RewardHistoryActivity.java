package com.iotait.superpuntos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.adapter.ActivitiesAdapter;
import com.iotait.superpuntos.adapter.RewardHistoryAdapter;
import com.iotait.superpuntos.databinding.ActivityRewardHistoryBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.ClaimedReward;

import java.util.ArrayList;
import java.util.List;

import static com.iotait.superpuntos.helper.Constants.ACTIVITY_LIST;
import static com.iotait.superpuntos.helper.Constants.USER;

public class RewardHistoryActivity extends AppCompatActivity {

    ActivityRewardHistoryBinding binding;
    List<ClaimedReward> list = new ArrayList<>();
    RewardHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRewardHistoryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fetchHistory();

        setUpRecyclerview();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchHistory() {
        final DatabaseReference claimedRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"claimedreward");
        Query query = claimedRef.orderByChild("userId").equalTo(USER.getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    ClaimedReward claimedReward = dataSnapshot.getValue(ClaimedReward.class);
                    list.add(claimedReward);
                    Log.e("TAG", "onChildAdded: "+claimedReward.getUserName() );
                    setUpRecyclerview();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerview() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvRewardClaimed.setLayoutManager(layoutManager);
        adapter = new RewardHistoryAdapter(this, list);
        binding.rvRewardClaimed.setAdapter(adapter);
        if (list.size() > 0){
            binding.rvRewardClaimed.setVisibility(View.VISIBLE);
            binding.tvNoClaim.setVisibility(View.INVISIBLE);
        }
    }
}
