package com.iotait.superpuntos.admin.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.databinding.ActivityAdminReleaseBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.ClaimedReward;
import com.iotait.superpuntos.models.Contact;

import static com.iotait.superpuntos.helper.Constants.ADMIN_TOKEN;
import static com.iotait.superpuntos.helper.Constants.CLAIMED_LIST;
import static com.iotait.superpuntos.helper.Constants.CONTACT;
import static com.iotait.superpuntos.helper.Constants.sendNotification;

public class AdminReleaseActivity extends AppCompatActivity {

    ActivityAdminReleaseBinding binding;
    int position = 0;
    ClaimedReward claimedReward = new ClaimedReward();
    DatabaseReference currentClaimedRef;
    String userToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminReleaseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        position = getIntent().getIntExtra("Position", 0);
        claimedReward = CLAIMED_LIST.get(position);
        getUserToken(claimedReward.getUserId());
        currentClaimedRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"claimedreward").child(claimedReward.getId());

        setClickListener();

        fetchUserContact();
    }

    private void getUserToken(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(userId).child("fcmToken");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    userToken = dataSnapshot.getValue(String.class);
                    Log.e("TAG", "onDataChange: "+userToken );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setClickListener() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Constants.TEST.equals("testenvironment/")) {
                    sendNotification(userToken, "Fund released");
                }
                CLAIMED_LIST.get(position).setProcessing(false);
                currentClaimedRef.setValue(CLAIMED_LIST.get(position));
                CLAIMED_LIST.remove(position);
                finish();
            }
        });
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCancel.setVisibility(View.GONE);
                CLAIMED_LIST.get(position).setProcessing(false);
                CLAIMED_LIST.get(position).setValidnumber(false);
                currentClaimedRef.setValue(CLAIMED_LIST.get(position));
                String intval = CLAIMED_LIST.get(position).getClaimedAmount();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users/"+claimedReward.getUserId()+"/coins");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null){
                            int coin = Integer.parseInt(dataSnapshot.getValue().toString()) + Integer.parseInt(intval);
                            ref.setValue(coin);
                            CLAIMED_LIST.remove(position);

                            finish();
                        }else {

                            Toast.makeText(AdminReleaseActivity.this, "Cancellation Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if(!Constants.TEST.equals("testenvironment/")) {
                    sendNotification(userToken, "Fund Rejected");
                }

            }
        });
    }

    private void fetchUserContact() {
        DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"contact").child(claimedReward.getUserId());
        contactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Contact contact = dataSnapshot.getValue(Contact.class);
                    populateView(contact);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateView(Contact contact) {

        binding.etName.setKeyListener(null);
        binding.etPhoneNumber.setKeyListener(null);
        binding.etProvider.setKeyListener(null);
        binding.etAmount.setKeyListener(null);

        if (claimedReward.getUri()==null) {
            Glide.with(AdminReleaseActivity.this).load(R.drawable.user).
                    diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
        }else {
            if (claimedReward.getUri().equals("")){
                Glide.with(AdminReleaseActivity.this).load(R.drawable.user).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
            }else {
                Glide.with(AdminReleaseActivity.this).load(claimedReward.getUri()).
                        diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivUser);
            }
        }

        binding.tvUserName.setText(claimedReward.getUserName());
        binding.tvTotalClaimed.setText("C$"+claimedReward.getClaimedAmount());
        binding.tvDate.setText(claimedReward.getClaimingDate());
        binding.etName.setText(contact.getName());
        binding.etPhoneNumber.setText(contact.getPhone());
        binding.ccp.setCountryForPhoneCode(Integer.parseInt(contact.getCountyCode()));
        binding.tvPhone.setText(claimedReward.getUserId());
        binding.etAmount.setText("C$"+claimedReward.getClaimedAmount());
        binding.etProvider.setText(contact.getOperator());

    }
}
