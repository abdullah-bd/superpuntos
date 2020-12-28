package com.iotait.superpuntos.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.home.HomeActivity;
import com.iotait.superpuntos.activity.intro.IntroActivity;
import com.iotait.superpuntos.admin.activity.AdminHomeActivity;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.helper.VersionCk;
import com.iotait.superpuntos.models.Survey;
import com.iotait.superpuntos.models.User;
import com.iotait.superpuntos.models.UserGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.GROUPSMAP;
import static com.iotait.superpuntos.helper.Constants.SURVEY_LIST;
import static com.iotait.superpuntos.helper.Constants.USER_LIST;
import static com.iotait.superpuntos.helper.Constants.getTimeStamp;

public class SplashActivity extends AppCompatActivity implements PopUpDialog.OnOkButtonClick{
    private FirebaseAnalytics mFirebaseAnalytics;

    private Handler handler = new Handler();
    private int progressStatus =0;
    private ProgressBar progressBar;
    SimpleDateFormat simpleDateFormat;
    PopUpDialog popUpDialog;
    final double[] a = {0.0};
    boolean version = false;
    String versionName;
    private static String latestVersion;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBarSplash);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("login", 0);
        Constants.PHONE = sharedPref.getString("phone","!!!");
        final boolean firstTime = sharedPref.getBoolean("firsttime",true);


        simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy");

        versionName ="1.0";
        try {
            versionName= getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            Log.d("Splash", "checkForUpdate: "+e.toString());
        }

        final DatabaseReference version = FirebaseDatabase.getInstance().getReference(Constants.TEST+"latestversion");

        version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    latestVersion = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fetchUser();
        fetchUserList();
        fetchSurveyList();
        fetchGroupList();


        new Thread(() -> {

            progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#009245")));

            while(progressStatus<100){
                progressStatus +=1;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try
                {
                    Thread.sleep(25);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            if(checkForUpdate(versionName,latestVersion)){
                SplashActivity.this.version =true;

                runOnUiThread(() -> showDialog("¡Nueva version disponible!"));

            }else if (sharedPref.getBoolean("session",false)){
                if (Constants.USER.getUid()==null){

                    runOnUiThread(this::showDialog);

                }else {

                    Intent intent =new Intent(SplashActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }



            }else {
                if(firstTime){
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                }else {
                    if (sharedPref.getBoolean("admin", false)){
                        startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }

                }
                finish();
            }


        }).start();


    }

    private boolean checkForUpdate(String versionName,String latestVersion) {
    Constants.USERVERSION = versionName;
        try {
            Log.e("GSK ", "checkForUpdate: "+versionName+" "+latestVersion);
            return VersionCk.isVersionHigher(versionName,latestVersion);

        }catch (Exception e){
            Log.d("Splash", "checkForUpdate: "+e.toString());
        }

        return false;
    }

    private void fetchGroupList() {
        GROUPSMAP.clear();
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"userGroups");
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot sp:dataSnapshot.getChildren()){
                    UserGroup ug = sp.getValue(UserGroup.class);
                    GROUPSMAP.put(ug.getId(),ug);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchUser() {
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(Constants.PHONE);
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Constants.USER = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchSurveyList() {
        SURVEY_LIST.clear();
        DatabaseReference surveyRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"surveys");
        surveyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Survey survey = ds.getValue(Survey.class);
                    if(survey.isRepeatable()){
                        try {

                            Date date1 = simpleDateFormat.parse(survey.getSurveyStartDate());
                            Date date2 = simpleDateFormat.parse(getTimeStamp());
                            //Toast.makeText(SplashActivity.this, "Print dif-- "+printDifference(date1, date2), Toast.LENGTH_SHORT).show();
                            long daydiff = printDifference(date1, date2);
                            if(daydiff>=survey.getRepeatationDay()){
                                long dayOf = daydiff%survey.getRepeatationDay();
                                if (dayOf >= 0 && dayOf <= survey.getOpenFor()){
                                    SURVEY_LIST.add(survey);
                                }
                            }

                        }catch (Exception e){
                            Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }




                    }else {

                        try {
                            Date date1 = simpleDateFormat.parse(survey.getSurveyDeadline());
                            Date date2 = simpleDateFormat.parse(getTimeStamp());
                            if (printDifference(date2, date1) >= 0) {
                                if (survey.getSurveyStartDate() == null) {
                                    SURVEY_LIST.add(survey);
                                } else {
                                    date1 = simpleDateFormat.parse(survey.getSurveyStartDate());
                                    if (printDifference(date2, date1) <= 0) {
                                        SURVEY_LIST.add(survey);
                                    }
                                }

                            }
                        }catch (Exception e){
                            Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                        }

                    }

                }
                if(Constants.USER.getName()!=null)
                    HomeActivity.targetGroupFilter();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long yearsInMilli = daysInMilli * 365;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedYear = different / yearsInMilli;
        different = different % daysInMilli;

        System.out.println("year : " + elapsedYear);

        long elapsedSeconds = different / secondsInMilli;

        return elapsedDays;

    }

    private void fetchUserList() {
        USER_LIST.clear();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users");
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                USER_LIST.add(user);
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
    private void showDialog() {
        popUpDialog = new PopUpDialog(this, "¡No hay internet!", "Por favor chequea to conexion o intenta en un rato.");
        popUpDialog.setCancelable(false);
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }
    private void showDialog(String s) {
        popUpDialog = new PopUpDialog(this, s, "Por favor actualiza tu app.");
        popUpDialog.setCancelable(false);
        popUpDialog.setListener(this);
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    @Override
    public void onOkButtonClick() {
        if (version){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.APPNAME)));
        }else {

            finish();
        }
    }
}
