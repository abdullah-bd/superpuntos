package com.iotait.superpuntos.admin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.admin.activity.AdminUserListActivity;
import com.iotait.superpuntos.databinding.FragmentAdminAccDetailsBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.Contact;
import com.iotait.superpuntos.models.User;

import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.USER_LIST;

public class AdminAccDetailsFragment extends Fragment {

    public AdminAccDetailsFragment() {
    }

    private FragmentAdminAccDetailsBinding binding;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminAccDetailsBinding.inflate(inflater, container, false);
        user = USER_LIST.get(((AdminUserListActivity) Objects.requireNonNull(getActivity())).position);

        populateView();

        fetchUserContact();

        return binding.getRoot();
    }

    private void populateView() {
        binding.etName.setKeyListener(null);
        binding.etPhoneNumber.setKeyListener(null);
        binding.etProvider.setKeyListener(null);
        binding.etName.setText(user.getName());
        binding.etPhoneNumber.setText(user.getPhone());
        binding.ccp.setCountryForPhoneCode(Integer.parseInt(user.getCountryCode()));
    }

    private void fetchUserContact() {
        DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"contact").child(user.getUid());
        contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    assert contact != null;
                    binding.etName.setText(contact.getName());
                    binding.etPhoneNumber.setText(contact.getPhone());
                    binding.etProvider.setText(contact.getOperator());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
