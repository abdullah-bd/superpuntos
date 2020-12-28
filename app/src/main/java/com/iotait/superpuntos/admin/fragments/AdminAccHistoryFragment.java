package com.iotait.superpuntos.admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iotait.superpuntos.adapter.ActivitiesAdapter;
import com.iotait.superpuntos.admin.activity.AdminUserListActivity;
import com.iotait.superpuntos.databinding.FragmentAdminAccHistoryBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.User;
import com.iotait.superpuntos.models.UserActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.USER_LIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAccHistoryFragment extends Fragment {

    public AdminAccHistoryFragment() {
        // Required empty public constructor
    }

    private FragmentAdminAccHistoryBinding binding;
    private User user;
    private List<UserActivity> userActivityList = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminAccHistoryBinding.inflate(inflater, container, false);
        user = USER_LIST.get(((AdminUserListActivity) Objects.requireNonNull(getActivity())).position);

        fetchUserActivity();

        return binding.getRoot();
    }

    private void fetchUserActivity() {
        DatabaseReference userActivityRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"useractivity").child(user.getUid());
        userActivityRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    UserActivity activity = dataSnapshot.getValue(UserActivity.class);
                    userActivityList.add(activity);
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
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvUserActivities.setLayoutManager(layoutManager);
        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(getContext(), userActivityList);
        binding.rvUserActivities.setAdapter(activitiesAdapter);
    }
}
