package com.iotait.superpuntos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iotait.superpuntos.R;
import com.iotait.superpuntos.activity.home.HomeActivity;
import com.iotait.superpuntos.databinding.ActivityLoginBinding;
import com.iotait.superpuntos.databinding.ActivitySurveyBinding;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.models.PartialAnswer;
import com.iotait.superpuntos.models.Question;
import com.iotait.superpuntos.models.User;
import com.iotait.superpuntos.models.UserActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    ActivitySurveyBinding binding;
    List<Question> questionList;
    List<String> answers;
    int number = 0;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySurveyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        phone = Constants.USER.getUid();
        questionList= Constants.SURVEY.getQuestionList();
        answers = new ArrayList<>();

        addParticipant();

        checkPreviousAnswer();

        loadQuestion(number);

        binding.earnedpoint.setText("¡Te has ganado C$"+Constants.SURVEY.getSurveyPoints()+" puntos!");

        binding.btnNext.setOnClickListener(v -> {

            if (questionList.size()-1>number){
                if (questionList.get(number).isMultipleType()){
                    String selected = "";
                    if(binding.cbOne.isChecked() || binding.cbTwo.isChecked() || binding.cbThree.isChecked() ||binding.cbFour.isChecked() || binding.cb5.isChecked() || binding.cb6.isChecked() || binding.cb7.isChecked() ||binding.cb8.isChecked() || binding.cb9.isChecked() || binding.cb10.isChecked() || binding.cb11.isChecked() ||binding.cb12.isChecked() || binding.cb13.isChecked() || binding.cb14.isChecked() || binding.cb15.isChecked()){
                        if (binding.cbOne.isChecked()){
                            selected+= binding.cbOne.getText()+"-";
                            binding.cbOne.setChecked(false);
                        }
                        if (binding.cbTwo.isChecked()){
                            selected+= binding.cbTwo.getText()+"-";
                            binding.cbTwo.setChecked(false);
                        }
                        if (binding.cbThree.isChecked()){
                            selected+= binding.cbThree.getText()+"-";
                            binding.cbThree.setChecked(false);
                        }
                        if (binding.cbFour.isChecked()){
                            selected+= binding.cbFour.getText()+"-";
                            binding.cbFour.setChecked(false);
                        }
                        if (binding.cb5.isChecked()){
                            selected+= binding.cb5.getText()+"-";
                            binding.cb5.setChecked(false);
                        }
                        if (binding.cb6.isChecked()){
                            selected+= binding.cb6.getText()+"-";
                            binding.cb6.setChecked(false);
                        }
                        if (binding.cb7.isChecked()){
                            selected+= binding.cb7.getText()+"-";
                            binding.cb7.setChecked(false);
                        }
                        if (binding.cb8.isChecked()){
                            selected+= binding.cb8.getText()+"-";
                            binding.cb8.setChecked(false);
                        }
                        if (binding.cb9.isChecked()){
                            selected+= binding.cb9.getText()+"-";
                            binding.cb9.setChecked(false);
                        }
                        if (binding.cb10.isChecked()){
                            selected+= binding.cb10.getText()+"-";
                            binding.cb10.setChecked(false);
                        }
                        if (binding.cb11.isChecked()){
                            selected+= binding.cb11.getText()+"-";
                            binding.cb11.setChecked(false);
                        }
                        if (binding.cb12.isChecked()){
                            selected+= binding.cbFour.getText()+"-";
                            binding.cbFour.setChecked(false);
                        }
                        if (binding.cb13.isChecked()){
                            selected+= binding.cb13.getText()+"-";
                            binding.cb13.setChecked(false);
                        }
                        if (binding.cb14.isChecked()){
                            selected+= binding.cb14.getText()+"-";
                            binding.cb14.setChecked(false);
                        }
                        if (binding.cb15.isChecked()){
                            selected+= binding.cb15.getText()+"-";
                            binding.cb15.setChecked(false);
                        }


                        answers.add(selected);
                        saveAnswer();
                    }else {
                        Toast.makeText(SurveyActivity.this, "¡Por favor elige una opcion!", Toast.LENGTH_SHORT).show();
                        number--;
                    }


                }else {

                    int rb = binding.radioGroup1.getCheckedRadioButtonId();
                    Log.d("Abdullah", "onClick: "+rb);
                    if (rb<0){
                        Toast.makeText(SurveyActivity.this, "¡Por favor elige una opcion!", Toast.LENGTH_SHORT).show();
                        number--;
                    }else {
                        RadioButton radioButton = findViewById(rb);
                        answers.add(radioButton.getText().toString());
                        saveAnswer();
                        binding.radioGroup1.clearCheck();
                    }
                }

                number++;
                loadQuestion(number);

            }else {
                boolean pass = true;

                if (questionList.get(number).isMultipleType()){
                    String selected = "";
                    if(binding.cbOne.isChecked() || binding.cbTwo.isChecked() || binding.cbThree.isChecked() ||binding.cbFour.isChecked() || binding.cb5.isChecked() || binding.cb6.isChecked() || binding.cb7.isChecked() ||binding.cb8.isChecked() || binding.cb9.isChecked() || binding.cb10.isChecked() || binding.cb11.isChecked() ||binding.cb12.isChecked() || binding.cb13.isChecked() || binding.cb14.isChecked() || binding.cb15.isChecked()){
                        if (binding.cbOne.isChecked()){
                            selected+= binding.cbOne.getText()+"-";
                            binding.cbOne.setChecked(false);
                        }
                        if (binding.cbTwo.isChecked()){
                            selected+= binding.cbTwo.getText()+"-";
                            binding.cbTwo.setChecked(false);
                        }
                        if (binding.cbThree.isChecked()){
                            selected+= binding.cbThree.getText()+"-";
                            binding.cbThree.setChecked(false);
                        }
                        if (binding.cbFour.isChecked()){
                            selected+= binding.cbFour.getText()+"-";
                            binding.cbFour.setChecked(false);
                        }
                        if (binding.cb5.isChecked()){
                            selected+= binding.cb5.getText()+"-";
                            binding.cb5.setChecked(false);
                        }
                        if (binding.cb6.isChecked()){
                            selected+= binding.cb6.getText()+"-";
                            binding.cb6.setChecked(false);
                        }
                        if (binding.cb7.isChecked()){
                            selected+= binding.cb7.getText()+"-";
                            binding.cb7.setChecked(false);
                        }
                        if (binding.cb8.isChecked()){
                            selected+= binding.cb8.getText()+"-";
                            binding.cb8.setChecked(false);
                        }
                        if (binding.cb9.isChecked()){
                            selected+= binding.cb9.getText()+"-";
                            binding.cb9.setChecked(false);
                        }
                        if (binding.cb10.isChecked()){
                            selected+= binding.cb10.getText()+"-";
                            binding.cb10.setChecked(false);
                        }
                        if (binding.cb11.isChecked()){
                            selected+= binding.cb11.getText()+"-";
                            binding.cb11.setChecked(false);
                        }
                        if (binding.cb12.isChecked()){
                            selected+= binding.cbFour.getText()+"-";
                            binding.cbFour.setChecked(false);
                        }
                        if (binding.cb13.isChecked()){
                            selected+= binding.cb13.getText()+"-";
                            binding.cb13.setChecked(false);
                        }
                        if (binding.cb14.isChecked()){
                            selected+= binding.cb14.getText()+"-";
                            binding.cb14.setChecked(false);
                        }
                        if (binding.cb15.isChecked()){
                            selected+= binding.cb15.getText()+"-";
                            binding.cb15.setChecked(false);
                        }

                        answers.add(selected);
                        saveAnswer();

                    }else {
                        Toast.makeText(SurveyActivity.this, "¡Por favor elige una opcion!", Toast.LENGTH_SHORT).show();
                        pass = false;
                    }


                }else {

                    int rb = binding.radioGroup1.getCheckedRadioButtonId();

                    if (rb<0){
                        Toast.makeText(SurveyActivity.this, "¡Por favor elige una opcion!", Toast.LENGTH_SHORT).show();
                        pass = false;
                    }else {
                        RadioButton radioButton = findViewById(rb);
                        answers.add(radioButton.getText().toString());
                        saveAnswer();
                        binding.radioGroup1.clearCheck();
                    }

                }

                if (pass){
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference(Constants.TEST);

                    DatabaseReference usersRef = ref.child("surveyanswer/"+Constants.SURVEY.getSurveyId());

                    usersRef.child(phone).setValue(answers);

                    DateFormat df = new SimpleDateFormat("dd MMM, yyyy");
                    String date = df.format(Calendar.getInstance().getTime());
                    final UserActivity userActivity = new UserActivity(Constants.SURVEY.getSurveyName(),date,Constants.SURVEY.getSurveyPoints());
                    DatabaseReference ref2 = database.getReference(Constants.TEST);

                    final DatabaseReference usersRef2 = ref2.child("useractivity/");
                    final List<UserActivity> listAct = new ArrayList<>();


                    listAct.add(userActivity);
                    usersRef2.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UserActivity post = ds.getValue(UserActivity.class);
                                listAct.add(post);
                            }

                            usersRef2.child(phone).setValue(listAct);
                        }
                        @Override
                        public void onCancelled(@NotNull DatabaseError databaseError) {
                        }
                    });
                    binding.rlQuestion.setVisibility(View.GONE);
                    binding.llComplete.setVisibility(View.VISIBLE);
                }

            }
        });

        binding.btnComplete.setOnClickListener(v -> {
            binding.btnComplete.setEnabled(false);
            Constants.USER.setCoins(Constants.USER.getCoins()+Integer.parseInt(Constants.SURVEY.getSurveyPoints()));
            updateUserCoin();
        });
        binding.ivBack.setOnClickListener(view1 -> finish());
    }

    private void addParticipant() {
        DatabaseReference participantReference = FirebaseDatabase.getInstance().getReference(Constants.TEST+"participant").child(Constants.SURVEY.getSurveyId()).child(Constants.USER.getUid());
        participantReference.setValue("Participated");
    }

    private void updateUserCoin() {
        DatabaseReference userCoinRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"users").child(Constants.USER.getUid()).child("coins");
        userCoinRef.setValue(Constants.USER.getCoins()).addOnSuccessListener(aVoid -> finish());
    }

    private void saveAnswer() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refu = database.getReference(Constants.TEST);

        DatabaseReference usersRefu = refu.child("partialanswers/"+phone);
        PartialAnswer pr = new PartialAnswer(answers,Constants.SURVEY.getSurveyId());
        usersRefu.child(Constants.SURVEY.getSurveyId()).setValue(pr);

    }

    private void checkPreviousAnswer() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refuu = database.getReference(Constants.TEST);

        DatabaseReference usersRefuu = refuu.child("partialanswers/"+phone);
        usersRefuu.child(Constants.SURVEY.getSurveyId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ab", "onDataChange: "+dataSnapshot);
                PartialAnswer pr = dataSnapshot.getValue(PartialAnswer.class);
                if (pr!=null){
                    answers = pr.getAns();
                    //Toast.makeText(SurveyActivity.this, answers.get(0), Toast.LENGTH_SHORT).show();

                    number = answers.size();
                    if (number<questionList.size()){
                        loadQuestion(number);
                    }else {
                        binding.tvCompleted.setText("Ya has participado en esta encuesta.");
                        binding.rlQuestion.setVisibility(View.GONE);
                        binding.llComplete.setVisibility(View.VISIBLE);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void loadQuestion(int num){
        Question question = questionList.get(num);
        if (question.isMultipleType()){
            binding.llmulti.setVisibility(View.VISIBLE);
            binding.radioGroup.setVisibility(View.GONE);
            binding.tvQuestion.setText(question.getQuestion());

            if (question.getOptionOne()==null){
                binding.cbOne.setVisibility(View.GONE);
            }else {
                binding.cbOne.setVisibility(View.VISIBLE);
                binding.cbOne.setText(question.getOptionOne());
            }
            if(question.getOptionTwo()==null){
                binding.cbTwo.setVisibility(View.GONE);
            }else {
                binding.cbTwo.setVisibility(View.VISIBLE);
                binding.cbTwo.setText(question.getOptionTwo());
            }
            if(question.getOptionThree()==null){
                binding.cbThree.setVisibility(View.GONE);
            }else {
                binding.cbThree.setVisibility(View.VISIBLE);
                binding.cbThree.setText(question.getOptionThree());
            }
            if(question.getOptionFour()==null){
                binding.cbFour.setVisibility(View.GONE);
            }else {
                binding.cbFour.setVisibility(View.VISIBLE);
                binding.cbFour.setText(question.getOptionFour());
            }
            if(question.getOptionFive()==null){
                binding.cb5.setVisibility(View.GONE);
            }else {
                binding.cb5.setVisibility(View.VISIBLE);
                binding.cb5.setText(question.getOptionFive());
            }
            if(question.getOptionSix()==null){
                binding.cb6.setVisibility(View.GONE);
            }else {
                binding.cb6.setVisibility(View.VISIBLE);
                binding.cb6.setText(question.getOptionSix());
            }
            if(question.getOptionSeven()==null){
                binding.cb7.setVisibility(View.GONE);
            }else {
                binding.cb7.setVisibility(View.VISIBLE);
                binding.cb7.setText(question.getOptionSeven());
            }
            if(question.getOptionEight()==null){
                binding.cb8.setVisibility(View.GONE);
            }else {
                binding.cb8.setVisibility(View.VISIBLE);
                binding.cb8.setText(question.getOptionEight());
            }
            if(question.getOptionNine()==null){
                binding.cb9.setVisibility(View.GONE);
            }else {
                binding.cb9.setVisibility(View.VISIBLE);
                binding.cb9.setText(question.getOptionNine());
            }
            if(question.getOptionTen()==null){
                binding.cb10.setVisibility(View.GONE);
            }else {
                binding.cb10.setVisibility(View.VISIBLE);
                binding.cb10.setText(question.getOptionTen());
            }
            if(question.getOptionEleven()==null){
                binding.cb11.setVisibility(View.GONE);
            }else {
                binding.cb11.setVisibility(View.VISIBLE);
                binding.cb11.setText(question.getOptionEleven());
            }
            if(question.getOptionTwelve()==null){
                binding.cb12.setVisibility(View.GONE);
            }else {
                binding.cb12.setVisibility(View.VISIBLE);
                binding.cb12.setText(question.getOptionTwelve());
            }
            if(question.getOptionThirteen()==null){
                binding.cb13.setVisibility(View.GONE);
            }else {
                binding.cb13.setVisibility(View.VISIBLE);
                binding.cb13.setText(question.getOptionThirteen());
            }
            if(question.getOptionFourteen()==null){
                binding.cb14.setVisibility(View.GONE);
            }else {
                binding.cb14.setVisibility(View.VISIBLE);
                binding.cb14.setText(question.getOptionFourteen());
            }
            if(question.getOptionFifteen()==null){
                binding.cb15.setVisibility(View.GONE);
            }else {
                binding.cb15.setVisibility(View.VISIBLE);
                binding.cb15.setText(question.getOptionFifteen());
            }
        }else {
            binding.llmulti.setVisibility(View.GONE);
            binding.radioGroup.setVisibility(View.VISIBLE);
            binding.tvQuestion.setText(question.getQuestion());
            if (question.getOptionOne()==null){
                binding.radioOne.setVisibility(View.GONE);
            } else{
                binding.radioOne.setVisibility(View.VISIBLE);
                binding.radioOne.setText(question.getOptionOne());
            }
            if (question.getOptionTwo()==null){
                binding.radioTwo.setVisibility(View.GONE);
            } else{
                binding.radioTwo.setVisibility(View.VISIBLE);
                binding.radioTwo.setText(question.getOptionTwo());
            }
            if (question.getOptionThree()==null){
                binding.radioThree.setVisibility(View.GONE);
            } else{
                binding.radioThree.setVisibility(View.VISIBLE);
                binding.radioThree.setText(question.getOptionThree());
            }
            if (question.getOptionFour()==null){
                binding.radioFour.setVisibility(View.GONE);
            } else{
                binding.radioFour.setVisibility(View.VISIBLE);
                binding.radioFour.setText(question.getOptionFour());
            }
            if (question.getOptionFive()==null){
                binding.radio5.setVisibility(View.GONE);
            } else{
                binding.radio5.setVisibility(View.VISIBLE);
                binding.radio5.setText(question.getOptionFive());
            }
            if (question.getOptionSix()==null){
                binding.radio6.setVisibility(View.GONE);
            } else{
                binding.radio6.setVisibility(View.VISIBLE);
                binding.radio6.setText(question.getOptionSix());
            }
            if (question.getOptionSeven()==null){
                binding.radio7.setVisibility(View.GONE);
            } else{
                binding.radio7.setVisibility(View.VISIBLE);
                binding.radio7.setText(question.getOptionSeven());
            }
            if (question.getOptionEight()==null){
                binding.radio8.setVisibility(View.GONE);
            } else{
                binding.radio8.setVisibility(View.VISIBLE);
                binding.radio8.setText(question.getOptionEight());
            }
            if (question.getOptionNine()==null){
                binding.radio9.setVisibility(View.GONE);
            } else{
                binding.radio9.setVisibility(View.VISIBLE);
                binding.radio9.setText(question.getOptionNine());
            }
            if (question.getOptionTen()==null){
                binding.radio10.setVisibility(View.GONE);
            } else{
                binding.radio10.setVisibility(View.VISIBLE);
                binding.radio10.setText(question.getOptionTen());
            }
            if (question.getOptionEleven()==null){
                binding.radio11.setVisibility(View.GONE);
            } else{
                binding.radio11.setVisibility(View.VISIBLE);
                binding.radio11.setText(question.getOptionEleven());
            }
            if (question.getOptionTwelve()==null){
                binding.radio12.setVisibility(View.GONE);
            } else{
                binding.radio12.setVisibility(View.VISIBLE);
                binding.radio12.setText(question.getOptionTwelve());
            }
            if (question.getOptionThirteen()==null){
                binding.radio13.setVisibility(View.GONE);
            } else{
                binding.radio13.setVisibility(View.VISIBLE);
                binding.radio13.setText(question.getOptionThirteen());
            }
            if (question.getOptionFourteen()==null){
                binding.radio14.setVisibility(View.GONE);
            } else{
                binding.radio14.setVisibility(View.VISIBLE);
                binding.radio14.setText(question.getOptionFourteen());
            }
            if (question.getOptionFifteen()==null){
                binding.radio15.setVisibility(View.GONE);
            } else{
                binding.radio15.setVisibility(View.VISIBLE);
                binding.radio15.setText(question.getOptionFifteen());
            }

        }

        if(number>0){
            double a= (double) number/questionList.size()*100;
            int b= (int)a;
            binding.tvPercentage.setText(b+"%");
            binding.progressBar.setProgress(b);
        }

    }
}
