package com.iotait.superpuntos.activity.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.iotait.superpuntos.BuildConfig;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.SplashActivity;
import com.iotait.superpuntos.activity.home.fragments.HomeFragment;
import com.iotait.superpuntos.activity.home.fragments.PersonalFragment;
import com.iotait.superpuntos.activity.home.fragments.RewardsFragment;
import com.iotait.superpuntos.databinding.ActivityHomeBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.VersionChecker;
import com.iotait.superpuntos.models.Contact;
import com.iotait.superpuntos.models.PartialAnswer;
import com.iotait.superpuntos.models.Survey;
import com.iotait.superpuntos.models.User;
import com.iotait.superpuntos.models.UserActivity;
import com.iotait.superpuntos.models.UserGroup;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.iotait.superpuntos.helper.Constants.ACTIVITY_LIST;
import static com.iotait.superpuntos.helper.Constants.ADMIN_TOKEN;
import static com.iotait.superpuntos.helper.Constants.GROUPSMAP;
import static com.iotait.superpuntos.helper.Constants.PARTIAL_INFO;
import static com.iotait.superpuntos.helper.Constants.USER;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    String token;
    static SimpleDateFormat simpleDateFormat;
    static Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        PARTIAL_INFO = new HashMap<>();
        simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Constants.TEST);
        final DatabaseReference lastAct = ref.child("useractivity/"+ USER.getUid()+"/0");

        lastAct.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {

                    try {
                        UserActivity userActivity = dataSnapshot.getValue(UserActivity.class);
                        date1 = simpleDateFormat.parse(userActivity.getDate());
                        //Toast.makeText(HomeActivity.this, date1.toString(), Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
                targetGroupFilter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //targetGroupFilter();
        getAdminToken();

        FirebaseMessaging.getInstance().subscribeToTopic("client");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("iiiddd", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                        String token = task.getResult().getToken();
                        Constants.USER.setFcmToken(token);
                        USER.setPhoneModel(getDeviceName());
                        USER.setVersion(Constants.USERVERSION);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference(Constants.TEST+"users/");
                        if (USER.getUid() != null)
                            ref.child(Constants.USER.getUid()).setValue(Constants.USER);

                        //Toast.makeText(HomeActivity.this, token, Toast.LENGTH_SHORT).show();
                        Log.d("iiiddd", "onComplete: "+token);
                    }
                });

        loadFragment(new HomeFragment());

        binding.bottomNavigation.setSelectedItemId(R.id.navigation_survey);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_rewards:
                        loadFragment(new RewardsFragment());
                        return true;
                    case R.id.navigation_survey:
                        loadFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_parson:
                        loadFragment(new PersonalFragment());
                        return true;
                }
                return false;
            }
        });
    }

    public static void targetGroupFilter() {
        Constants.SURVEY_LIST_FINAL.clear();
        for (Survey survey:Constants.SURVEY_LIST){
            if(survey.getUserGroupId()==null){
                if(survey.getUserConditionDay()==0){
                    Constants.SURVEY_LIST_FINAL.add(survey);
                }else {
                    if (date1!=null){

                        try {
                            Date date2 =simpleDateFormat.parse(getTimeStamp());
                            if(SplashActivity.printDifference(date1,date2)>=survey.getUserConditionDay()){
                                Constants.SURVEY_LIST_FINAL.add(survey);
                            }

                        }catch (Exception e){
                            Log.e("err", "targetGroupFilter: ",e );
                        }

                    }else {
                        Constants.SURVEY_LIST_FINAL.add(survey);
                    }
                }

            }else {
                if(survey.getUserConditionDay()==0){
                    UserGroup ug = GROUPSMAP.get(survey.getUserGroupId());
                    if(ug!=null){
                        if (ug.getKey().equals("age")){
                            if (USER.getAge()!=null) {
                                if (ug.getCondition().equals("is")) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", new Locale("es", "ES"));
                                        Date date1 = sdf.parse(USER.getAge());

                                        Date date2 = simpleDateFormat.parse(getTimeStamp());
                                        Log.d("dadada", "targetGroupFilter: " + date1.toString() + date2.toString() + "-->" + getDiffYears(date2, date1));
                                        //Toast.makeText(this, ""+getDiffYears(date2,date1), Toast.LENGTH_SHORT).show();
                                        if (Integer.parseInt(ug.getStartRange()) == getDiffYears(date2, date1)) {
                                            Constants.SURVEY_LIST_FINAL.add(survey);
                                        }

                                    } catch (Exception e) {
                                        Log.e("err", "targetGroupFilter: ", e);
                                    }

                                } else if (ug.getCondition().equals("between")) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", new Locale("es", "ES"));
                                        Date date1 = sdf.parse(USER.getAge());

                                        Date date2 = simpleDateFormat.parse(getTimeStamp());
                                        if (Integer.parseInt(ug.getStartRange()) <= getDiffYears(date2, date1) && Integer.parseInt(ug.getEndRange()) >= getDiffYears(date2, date1)) {
                                            Constants.SURVEY_LIST_FINAL.add(survey);
                                        }

                                    } catch (Exception e) {
                                        Log.e("err", "targetGroupFilter: ", e);
                                    }
                                }
                            }
                        }else if (ug.getKey().equals("coins")){
                            if (ug.getCondition().equals("is")){
                                if(USER.getCoins()==Integer.parseInt(ug.getStartRange())){
                                    Constants.SURVEY_LIST_FINAL.add(survey);
                                }
                            }else if(ug.getCondition().equals("between")) {
                                if(USER.getCoins()>=Integer.parseInt(ug.getStartRange()) && USER.getCoins()<=Integer.parseInt(ug.getEndRange())){
                                    Constants.SURVEY_LIST_FINAL.add(survey);
                                }
                            }
                        }else if (ug.getKey().equals("city")){
                            if(USER.getCity().equals(ug.getStartRange())){
                                Constants.SURVEY_LIST_FINAL.add(survey);
                            }
                        }else if (ug.getKey().equals("occupation")){
                            if(USER.getOccupation().equals(ug.getStartRange())){
                                Constants.SURVEY_LIST_FINAL.add(survey);
                            }
                        }else if (ug.getKey().equals("sex")){
                            if(USER.getSex().equals(ug.getStartRange())){
                                Constants.SURVEY_LIST_FINAL.add(survey);
                            }
                        }else if (ug.getKey().equals("creationDate")){
                            if (USER.getCreationDate()!=null){
                                if (ug.getCondition().equals("is")) {
                                    try {
                                        Date date2 = simpleDateFormat.parse(ug.getStartRange());

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", new Locale("es", "ES"));
                                        Date date1 = sdf.parse(USER.getCreationDate());

                                        if(SplashActivity.printDifference(date2,date1)==0){
                                            Constants.SURVEY_LIST_FINAL.add(survey);
                                        }
                                    }catch (Exception e){
                                        Log.e("err", "targetGroupFilter: ", e);
                                    }
                                }else if (ug.getCondition().equals("between")) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", new Locale("es", "ES"));
                                        Date date1 = sdf.parse(USER.getCreationDate());

                                        Date date2 = simpleDateFormat.parse(ug.getStartRange());

                                        Date date3 = simpleDateFormat.parse(ug.getEndRange());

                                        if (SplashActivity.printDifference(date2,date1) >=0 && SplashActivity.printDifference(date3,date1) <=0){
                                            Constants.SURVEY_LIST_FINAL.add(survey);
                                        }
                                    }catch (Exception e){
                                        Log.e("err", "targetGroupFilter: ", e);
                                    }
                                }
                            }
                        }

                    }else {
                        Constants.SURVEY_LIST_FINAL.add(survey);
                    }
                }else {
                    if (date1!=null){

                        try {
                            Date date2 =simpleDateFormat.parse(getTimeStamp());
                            if(SplashActivity.printDifference(date1,date2)>=survey.getUserConditionDay()){
                                Constants.SURVEY_LIST_FINAL.add(survey);
                            }


                        }catch (Exception e){
                            Log.e("err", "targetGroupFilter: ",e );
                        }

                    }else {
                        Constants.SURVEY_LIST_FINAL.add(survey);
                    }
                }




            }

        }



    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
    private void getAdminToken() {
        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"admin").child("fcmToken");
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ADMIN_TOKEN = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
       if (USER.getUid() != null){
           loadPartialAnswers();
           loadUserActivities();
           loadRecentContact();
       }
        super.onResume();
    }

    private void loadPartialAnswers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Constants.TEST+"partialanswers").child(Constants.USER.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    PartialAnswer pr = ds.getValue(PartialAnswer.class);
                    PARTIAL_INFO.put(pr.getsID(),pr.getAns().size());
                }
                Constants.SURVEYADAPTER.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });


    }

    private void loadRecentContact() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Constants.TEST+"contact/"+Constants.USER.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                Contact c = dataSnapshot.getValue(Contact.class);

                if(c!=null){
                    Constants.CONTACT = c;

                }else {
                }

            }
            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_fragment, fragment);
        transaction.commit();
    }

    private void loadUserActivities() {
        ACTIVITY_LIST.clear();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Constants.TEST);
        final DatabaseReference usersRef = ref.child("useractivity");

        usersRef.child(Constants.USER.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    UserActivity userActivity = dataSnapshot.getValue(UserActivity.class);
                    ACTIVITY_LIST.add(userActivity);
                    Log.e("TAG", "loadUserActivities: "+ACTIVITY_LIST.size());
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
    public static int getDiffYears(Date first, Date last) {
        return (int) (first.getYear() - last.getYear());
    }
}
