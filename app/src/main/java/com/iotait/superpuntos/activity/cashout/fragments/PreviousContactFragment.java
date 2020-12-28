package com.iotait.superpuntos.activity.cashout.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iotait.superpuntos.activity.home.fragments.RewardsFragment;
import com.iotait.superpuntos.admin.activity.AdminPreviewActivity;
import com.iotait.superpuntos.databinding.FragmentPreviousContactBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.models.ClaimedReward;

import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.ADMIN_TOKEN;
import static com.iotait.superpuntos.helper.Constants.CONTACT;
import static com.iotait.superpuntos.helper.Constants.HAS_CLAIMED;
import static com.iotait.superpuntos.helper.Constants.USER;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousContactFragment extends Fragment implements PopUpDialog.OnOkButtonClick{

    public PreviousContactFragment() {
        // Required empty public constructor
    }

    private FragmentPreviousContactBinding binding;
    private DatabaseReference rewardRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"claimedreward");
    private DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(USER.getUid());
    ProgressDialog pd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPreviousContactBinding.inflate(inflater, container, false);

        populateView();

        setClickListener();

        return binding.getRoot();
    }

    private void populateView() {
        //binding.ccp.setTextSize();
        binding.etName.setKeyListener(null);
        binding.etPhoneNumber.setKeyListener(null);
        if(CONTACT!=null){
            binding.etName.setText(CONTACT.getName());
            binding.etPhoneNumber.setText(CONTACT.getPhone());
            binding.ccp.setCountryForPhoneCode(Integer.parseInt(CONTACT.getCountyCode()));

            for (int i = 0; i < binding.spinnerOperatorr.getCount(); i++) {
                if (binding.spinnerOperatorr.getItemAtPosition(i).equals(CONTACT.getOperator())) {
                    binding.spinnerOperatorr.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setClickListener() {
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(getContext());
                pd.setMessage("Enviando");
                pd.show();
                if(!Constants.TEST.equals("testenvironment/")) {
                    Constants.sendNotification(ADMIN_TOKEN, "Reward claimed");
                }
                String id = rewardRef.push().getKey();
                ClaimedReward claimedReward = new ClaimedReward(id, USER.getUid(), USER.getName(), getTimeStamp(), "170",USER.getUri());
                rewardRef.child(id).setValue(claimedReward).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateUser();
                    }
                });
            }
        });
    }

    private void updateUser() {
        USER.setLastWithdraw(getTimeStamp());
        USER.setTotalClaimed(USER.getTotalClaimed()+USER.getCoins());
        USER.setCoins(USER.getCoins()-170);
        currentUserRef.setValue(USER).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(USER.getCoins()<170)
                    HAS_CLAIMED = true;
                pd.dismiss();
                showDialog("¡Exito!", "Tu solicitud ha sido enviada");
            }
        });
    }

    private void showDialog(String title, String body) {
        PopUpDialog popUpDialog = new PopUpDialog(getContext(), title, body);
        popUpDialog.setCancelable(false);
        popUpDialog.setListener(this);
        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    @Override
    public void onOkButtonClick() {

        RewardsFragment.setcoinstext();
        Objects.requireNonNull(getActivity()).finish();
    }

}
