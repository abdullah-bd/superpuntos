package com.iotait.superpuntos.activity.cashout.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.home.fragments.RewardsFragment;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.models.ClaimedReward;
import com.iotait.superpuntos.models.Contact;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.ADMIN_TOKEN;
import static com.iotait.superpuntos.helper.Constants.HAS_CLAIMED;
import static com.iotait.superpuntos.helper.Constants.USER;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

public class AddNewContactFragment extends Fragment implements PopUpDialog.OnOkButtonClick{

    public AddNewContactFragment() {

    }

    private DatabaseReference rewardRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"claimedreward");
    private DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(USER.getUid());
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_new_contact, container, false);
        final EditText name = v.findViewById(R.id.etName);
        final EditText phone = v.findViewById(R.id.etPhoneNumber);
        final Spinner operator = v.findViewById(R.id.spinnerOperator);
        final CountryCodePicker ccp = v.findViewById(R.id.ccp);


        Button save = v.findViewById(R.id.btnSaveAndSubmit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(getContext());
                pd.setMessage("Enviando");

                if(name.getText().toString().isEmpty()){
                    name.setError("Ingresa un nombre");
                }else if(name.getText().toString().isEmpty()){
                    name.setError("Ingresa un nombre");
                }else if (operator.getSelectedItem().equals("Provider name")){
                    Toast.makeText(getContext(), "Elige Proveedor", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().isEmpty()){
                    phone.setError("Ingresa un numero");
                }else {
                    pd.show();
                    Contact contact = new Contact(name.getText().toString(),phone.getText().toString(),ccp.getSelectedCountryCodeWithPlus(),operator.getSelectedItem().toString());

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference(Constants.TEST);

                    DatabaseReference usersRef = ref.child("contact");

                    usersRef.child(USER.getUid()).setValue(contact);
                    if(!Constants.TEST.equals("testenvironment/")) {
                        Constants.sendNotification(ADMIN_TOKEN, "Reward claimed");
                    }
                    submit();
                }


            }
        });

        return v;
    }

    private void submit() {
        String id = rewardRef.push().getKey();
        ClaimedReward claimedReward = new ClaimedReward(id, USER.getUid(), USER.getName(), getTimeStamp(), "170",USER.getUri());
        rewardRef.child(id).setValue(claimedReward).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateUser();
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
