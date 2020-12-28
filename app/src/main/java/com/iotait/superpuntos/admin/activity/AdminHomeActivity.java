package com.iotait.superpuntos.admin.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.home.fragments.HomeFragment;
import com.iotait.superpuntos.activity.home.fragments.PersonalFragment;
import com.iotait.superpuntos.activity.home.fragments.RewardsFragment;
import com.iotait.superpuntos.admin.fragments.AdminHomeFragment;
import com.iotait.superpuntos.admin.fragments.AdminRewardFragment;
import com.iotait.superpuntos.admin.fragments.AdminUserListFragment;
import com.iotait.superpuntos.databinding.ActivityAdminHomeBinding;
import com.iotait.superpuntos.databinding.ActivityHomeBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.ClaimedReward;

import static com.iotait.superpuntos.helper.Constants.ADMIN_TOKEN;
import static com.iotait.superpuntos.helper.Constants.CLAIMED_LIST;
import static com.iotait.superpuntos.helper.Constants.IS_ADMIN;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        IS_ADMIN = true;

        getToken();

        fetchClaimedReward();

        loadFragment(new AdminHomeFragment());

        binding.bottomNavigation.setSelectedItemId(R.id.navigation_survey);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_rewards:
                        loadFragment(new AdminRewardFragment());
                        return true;
                    case R.id.navigation_survey:
                        loadFragment(new AdminHomeFragment());
                        return true;
                    case R.id.navigation_users:
                        loadFragment(new AdminUserListFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult().getToken();
                        ADMIN_TOKEN = token;
                        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"admin").child("fcmToken");
                        adminRef.setValue(token);

                    }
                });

    }

    private void fetchClaimedReward() {
        CLAIMED_LIST.clear();
        DatabaseReference claimedRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"claimedreward");
        claimedRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    ClaimedReward claimedReward = dataSnapshot.getValue(ClaimedReward.class);
                    if (claimedReward.isProcessing()){
                        CLAIMED_LIST.add(claimedReward);
                    }

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


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment, fragment);
        transaction.commit();
    }
}
