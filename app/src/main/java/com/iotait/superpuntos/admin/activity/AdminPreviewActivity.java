package com.iotait.superpuntos.admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iotait.superpuntos.databinding.ActivityAdminPreviewBinding;
import com.iotait.superpuntos.helper.AddOptionsDialog;
import com.iotait.superpuntos.helper.Constants;
import com.iotait.superpuntos.helper.LogoutDialog;
import com.iotait.superpuntos.helper.PopUpDialog;
import com.iotait.superpuntos.models.Question;
import com.iotait.superpuntos.models.Survey;

import java.util.ArrayList;
import java.util.Objects;

import static com.iotait.superpuntos.helper.Constants.ADMIN_SURVEY;
import static com.iotait.superpuntos.helper.Constants.QUESTION_LIST;
import static com.iotait.superpuntos.helper.Constants.sendNotification;

public class AdminPreviewActivity extends AppCompatActivity implements AddOptionsDialog.OnAddButtonClick, PopUpDialog.OnOkButtonClick{

    ActivityAdminPreviewBinding binding;
    AddOptionsDialog addOptionsDialog;
    int counter = 0;
    int editor;
    boolean isMultipleType = false;
    PopUpDialog popUpDialog;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPreviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setViews();

        setClickListeners();
    }

    private void setViews() {
        if (counter+1 == QUESTION_LIST.size()){
            binding.btnNext.setText("save");
        } else {
            binding.btnNext.setText("next");
        }
        if (counter < QUESTION_LIST.size() && counter >= 0){
            binding.tvCounter.setText(counter+1+"/"+QUESTION_LIST.size());
            binding.tvQuestion.setText(QUESTION_LIST.get(counter).getQuestion());
            binding.cb1.setVisibility(View.GONE);
            binding.cb2.setVisibility(View.GONE);
            binding.cb3.setVisibility(View.GONE);
            binding.cb4.setVisibility(View.GONE);
            binding.cb5.setVisibility(View.GONE);
            binding.cb6.setVisibility(View.GONE);
            binding.cb7.setVisibility(View.GONE);
            binding.cb8.setVisibility(View.GONE);
            binding.cb9.setVisibility(View.GONE);
            binding.cb10.setVisibility(View.GONE);
            binding.cb11.setVisibility(View.GONE);
            binding.cb12.setVisibility(View.GONE);
            binding.cb13.setVisibility(View.GONE);
            binding.cb14.setVisibility(View.GONE);
            binding.cb15.setVisibility(View.GONE);
            binding.rb1.setVisibility(View.GONE);
            binding.rb2.setVisibility(View.GONE);
            binding.rb3.setVisibility(View.GONE);
            binding.rb4.setVisibility(View.GONE);
            binding.rb5.setVisibility(View.GONE);
            binding.rb6.setVisibility(View.GONE);
            binding.rb7.setVisibility(View.GONE);
            binding.rb8.setVisibility(View.GONE);
            binding.rb9.setVisibility(View.GONE);
            binding.rb10.setVisibility(View.GONE);
            binding.rb11.setVisibility(View.GONE);
            binding.rb12.setVisibility(View.GONE);
            binding.rb13.setVisibility(View.GONE);
            binding.rb14.setVisibility(View.GONE);
            binding.rb15.setVisibility(View.GONE);
            binding.ibOption1.setVisibility(View.INVISIBLE);
            binding.ibOption2.setVisibility(View.INVISIBLE);
            binding.ibOption3.setVisibility(View.INVISIBLE);
            binding.ibOption4.setVisibility(View.INVISIBLE);
            binding.ibOption5.setVisibility(View.INVISIBLE);
            binding.ibOption6.setVisibility(View.INVISIBLE);
            binding.ibOption7.setVisibility(View.INVISIBLE);
            binding.ibOption8.setVisibility(View.INVISIBLE);
            binding.ibOption9.setVisibility(View.INVISIBLE);
            binding.ibOption10.setVisibility(View.INVISIBLE);
            binding.ibOption11.setVisibility(View.INVISIBLE);
            binding.ibOption12.setVisibility(View.INVISIBLE);
            binding.ibOption13.setVisibility(View.INVISIBLE);
            binding.ibOption14.setVisibility(View.INVISIBLE);
            binding.ibOption15.setVisibility(View.INVISIBLE);
            if (QUESTION_LIST.get(counter).isMultipleType()){
                isMultipleType = true;
                if (QUESTION_LIST.get(counter).getOptionOne() != null){
                    binding.cb1.setText(QUESTION_LIST.get(counter).getOptionOne());
                    binding.cb1.setVisibility(View.VISIBLE);
                    binding.ibOption1.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionTwo() != null){
                    binding.cb2.setText(QUESTION_LIST.get(counter).getOptionTwo());
                    binding.cb2.setVisibility(View.VISIBLE);
                    binding.ibOption2.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionThree() != null){
                    binding.cb3.setText(QUESTION_LIST.get(counter).getOptionThree());
                    binding.cb3.setVisibility(View.VISIBLE);
                    binding.ibOption3.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFour() != null){
                    binding.cb4.setText(QUESTION_LIST.get(counter).getOptionFour());
                    binding.cb4.setVisibility(View.VISIBLE);
                    binding.ibOption4.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFive() != null){
                    binding.cb5.setText(QUESTION_LIST.get(counter).getOptionFive());
                    binding.cb5.setVisibility(View.VISIBLE);
                    binding.ibOption5.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionSix() != null){
                    binding.cb6.setText(QUESTION_LIST.get(counter).getOptionSix());
                    binding.cb6.setVisibility(View.VISIBLE);
                    binding.ibOption6.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionSeven() != null){
                    binding.cb7.setText(QUESTION_LIST.get(counter).getOptionSeven());
                    binding.cb7.setVisibility(View.VISIBLE);
                    binding.ibOption7.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionEight() != null){
                    binding.cb8.setText(QUESTION_LIST.get(counter).getOptionEight());
                    binding.cb8.setVisibility(View.VISIBLE);
                    binding.ibOption8.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionNine() != null){
                    binding.cb9.setText(QUESTION_LIST.get(counter).getOptionNine());
                    binding.cb9.setVisibility(View.VISIBLE);
                    binding.ibOption9.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionTen() != null){
                    binding.cb10.setText(QUESTION_LIST.get(counter).getOptionTen());
                    binding.cb10.setVisibility(View.VISIBLE);
                    binding.ibOption10.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionEleven() != null){
                    binding.cb11.setText(QUESTION_LIST.get(counter).getOptionEleven());
                    binding.cb11.setVisibility(View.VISIBLE);
                    binding.ibOption11.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionTwelve() != null){
                    binding.cb12.setText(QUESTION_LIST.get(counter).getOptionTwelve());
                    binding.cb12.setVisibility(View.VISIBLE);
                    binding.ibOption12.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionThirteen() != null){
                    binding.cb13.setText(QUESTION_LIST.get(counter).getOptionThirteen());
                    binding.cb13.setVisibility(View.VISIBLE);
                    binding.ibOption13.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFourteen() != null){
                    binding.cb14.setText(QUESTION_LIST.get(counter).getOptionFourteen());
                    binding.cb14.setVisibility(View.VISIBLE);
                    binding.ibOption14.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFifteen() != null){
                    binding.cb15.setText(QUESTION_LIST.get(counter).getOptionFifteen());
                    binding.cb15.setVisibility(View.VISIBLE);
                    binding.ibOption15.setVisibility(View.VISIBLE);
                }

            } else {
                isMultipleType = false;
                if (QUESTION_LIST.get(counter).getOptionOne() != null){
                    binding.rb1.setText(QUESTION_LIST.get(counter).getOptionOne());
                    binding.rb1.setVisibility(View.VISIBLE);
                    binding.ibOption1.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionTwo() != null){
                    binding.rb2.setText(QUESTION_LIST.get(counter).getOptionTwo());
                    binding.rb2.setVisibility(View.VISIBLE);
                    binding.ibOption2.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionThree() != null){
                    binding.rb3.setText(QUESTION_LIST.get(counter).getOptionThree());
                    binding.rb3.setVisibility(View.VISIBLE);
                    binding.ibOption3.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFour() != null){
                    binding.rb4.setText(QUESTION_LIST.get(counter).getOptionFour());
                    binding.rb4.setVisibility(View.VISIBLE);
                    binding.ibOption4.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFive() != null){
                    binding.rb5.setText(QUESTION_LIST.get(counter).getOptionFive());
                    binding.rb5.setVisibility(View.VISIBLE);
                    binding.ibOption5.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionSix() != null){
                    binding.rb6.setText(QUESTION_LIST.get(counter).getOptionSix());
                    binding.rb6.setVisibility(View.VISIBLE);
                    binding.ibOption6.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionSeven() != null){
                    binding.rb7.setText(QUESTION_LIST.get(counter).getOptionSeven());
                    binding.rb7.setVisibility(View.VISIBLE);
                    binding.ibOption7.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionEight() != null){
                    binding.rb8.setText(QUESTION_LIST.get(counter).getOptionEight());
                    binding.rb8.setVisibility(View.VISIBLE);
                    binding.ibOption8.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionNine() != null){
                    binding.rb9.setText(QUESTION_LIST.get(counter).getOptionNine());
                    binding.rb9.setVisibility(View.VISIBLE);
                    binding.ibOption9.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionTen() != null){
                    binding.rb10.setText(QUESTION_LIST.get(counter).getOptionTen());
                    binding.rb10.setVisibility(View.VISIBLE);
                    binding.ibOption10.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionEleven() != null){
                    binding.rb11.setText(QUESTION_LIST.get(counter).getOptionEleven());
                    binding.rb11.setVisibility(View.VISIBLE);
                    binding.ibOption11.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionTwelve() != null){
                    binding.rb12.setText(QUESTION_LIST.get(counter).getOptionTwelve());
                    binding.rb12.setVisibility(View.VISIBLE);
                    binding.ibOption12.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionThirteen() != null){
                    binding.rb13.setText(QUESTION_LIST.get(counter).getOptionThirteen());
                    binding.rb13.setVisibility(View.VISIBLE);
                    binding.ibOption13.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFourteen() != null){
                    binding.rb14.setText(QUESTION_LIST.get(counter).getOptionFourteen());
                    binding.rb14.setVisibility(View.VISIBLE);
                    binding.ibOption14.setVisibility(View.VISIBLE);
                }
                if (QUESTION_LIST.get(counter).getOptionFifteen() != null){
                    binding.rb15.setText(QUESTION_LIST.get(counter).getOptionFifteen());
                    binding.rb15.setVisibility(View.VISIBLE);
                    binding.ibOption15.setVisibility(View.VISIBLE);
                }

            }
        }

    }

    private void setClickListeners() {
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                if (counter == QUESTION_LIST.size()){
                    saveToFirebase();
                    counter--;
                } else {
                    setViews();
                }
            }
        });

        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                if (counter < 0){
                    onBackPressed();
                    counter++;
                } else {
                    setViews();
                }
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFirebase();
            }
        });

        binding.ibQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 0;
                showEditDialog(binding.tvQuestion.getText().toString());
            }
        });

        binding.ibOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminPreviewActivity.this, String.valueOf(isMultipleType), Toast.LENGTH_SHORT).show();
                editor = 1;
                if (isMultipleType){
                    showEditDialog(binding.cb1.getText().toString());
                } else {
                    showEditDialog(binding.rb1.getText().toString());
                }

            }
        });

        binding.ibOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 2;
                if (isMultipleType){
                    showEditDialog(binding.cb2.getText().toString());
                } else {
                    showEditDialog(binding.rb2.getText().toString());
                }

            }
        });

        binding.ibOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 3;
                if (isMultipleType){
                    showEditDialog(binding.cb3.getText().toString());
                } else {
                    showEditDialog(binding.rb3.getText().toString());
                }

            }
        });

        binding.ibOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 4;
                if (isMultipleType){
                    showEditDialog(binding.cb4.getText().toString());
                } else {
                    showEditDialog(binding.rb4.getText().toString());
                }

            }
        });
        binding.ibOption5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 5;
                if (isMultipleType){
                    showEditDialog(binding.cb5.getText().toString());
                } else {
                    showEditDialog(binding.rb5.getText().toString());
                }

            }
        });
        binding.ibOption6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 6;
                if (isMultipleType){
                    showEditDialog(binding.cb6.getText().toString());
                } else {
                    showEditDialog(binding.rb6.getText().toString());
                }

            }
        });
        binding.ibOption7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 7;
                if (isMultipleType){
                    showEditDialog(binding.cb7.getText().toString());
                } else {
                    showEditDialog(binding.rb7.getText().toString());
                }

            }
        });
        binding.ibOption8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 8;
                if (isMultipleType){
                    showEditDialog(binding.cb8.getText().toString());
                } else {
                    showEditDialog(binding.rb8.getText().toString());
                }

            }
        });
        binding.ibOption9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 9;
                if (isMultipleType){
                    showEditDialog(binding.cb9.getText().toString());
                } else {
                    showEditDialog(binding.rb9.getText().toString());
                }

            }
        });
        binding.ibOption10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 10;
                if (isMultipleType){
                    showEditDialog(binding.cb10.getText().toString());
                } else {
                    showEditDialog(binding.rb10.getText().toString());
                }

            }
        });
        binding.ibOption11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 11;
                if (isMultipleType){
                    showEditDialog(binding.cb11.getText().toString());
                } else {
                    showEditDialog(binding.rb11.getText().toString());
                }

            }
        });
        binding.ibOption12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 12;
                if (isMultipleType){
                    showEditDialog(binding.cb12.getText().toString());
                } else {
                    showEditDialog(binding.rb12.getText().toString());
                }

            }
        });
        binding.ibOption13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 13;
                if (isMultipleType){
                    showEditDialog(binding.cb13.getText().toString());
                } else {
                    showEditDialog(binding.rb13.getText().toString());
                }

            }
        });
        binding.ibOption14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 14;
                if (isMultipleType){
                    showEditDialog(binding.cb14.getText().toString());
                } else {
                    showEditDialog(binding.rb14.getText().toString());
                }

            }
        });
        binding.ibOption15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = 15;
                if (isMultipleType){
                    showEditDialog(binding.cb15.getText().toString());
                } else {
                    showEditDialog(binding.rb15.getText().toString());
                }

            }
        });





    }

    private void saveToFirebase(){
        pd = new ProgressDialog(AdminPreviewActivity.this);
        pd.setMessage("Saving");
        pd.show();
        if(!Constants.TEST.equals("testenvironment/")){
            sendNotification("/topics/client", "New survey added");
        }

        DatabaseReference surveyRef = FirebaseDatabase.getInstance().getReference(Constants.TEST+"surveys");
        String surveyId = surveyRef.push().getKey();
        ADMIN_SURVEY.setSurveyId(surveyId);
        surveyRef.child(surveyId).setValue(ADMIN_SURVEY).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ADMIN_SURVEY = new Survey();
                QUESTION_LIST.clear();
                pd.dismiss();
                showDialog();
            }
        });
    }

    private void showDialog() {
        popUpDialog = new PopUpDialog(this, "Survey Added", "Your survey has een added successfully.");
        popUpDialog.setCancelable(false);
        popUpDialog.setListener(this);
        popUpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUpDialog.show();
    }

    private void showEditDialog(String text) {
        addOptionsDialog = new AddOptionsDialog(this, "Edit", text);
        addOptionsDialog.setListener(this);
        addOptionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addOptionsDialog.show();
    }

    @Override
    public void onAddButtonClick(String option) {
        switch (editor){
            case 0:
                binding.tvQuestion.setText(option);
                QUESTION_LIST.get(counter).setQuestion(option);
                addOptionsDialog.dismiss();
                break;

            case 1:
                if (isMultipleType){
                    binding.cb1.setText(option);
                } else {
                    binding.rb1.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionOne(option);
                addOptionsDialog.dismiss();
                break;

            case 2:
                if (isMultipleType){
                    binding.cb2.setText(option);
                } else {
                    binding.rb2.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionTwo(option);
                addOptionsDialog.dismiss();
                break;

            case 3:
                if (isMultipleType){
                    binding.cb3.setText(option);
                } else {
                    binding.rb3.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionThree(option);
                addOptionsDialog.dismiss();
                break;

            case 4:
                if (isMultipleType){
                    binding.cb4.setText(option);
                } else {
                    binding.rb4.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionFour(option);
                addOptionsDialog.dismiss();
                break;
            case 5:
                if (isMultipleType){
                    binding.cb5.setText(option);
                } else {
                    binding.rb5.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionFive(option);
                addOptionsDialog.dismiss();
                break;
            case 6:
                if (isMultipleType){
                    binding.cb6.setText(option);
                } else {
                    binding.rb6.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionSix(option);
                addOptionsDialog.dismiss();
                break;
            case 7:
                if (isMultipleType){
                    binding.cb7.setText(option);
                } else {
                    binding.rb7.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionSeven(option);
                addOptionsDialog.dismiss();
                break;
            case 8:
                if (isMultipleType){
                    binding.cb8.setText(option);
                } else {
                    binding.rb8.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionEight(option);
                addOptionsDialog.dismiss();
                break;
            case 9:
                if (isMultipleType){
                    binding.cb9.setText(option);
                } else {
                    binding.rb9.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionNine(option);
                addOptionsDialog.dismiss();
                break;
            case 10:
                if (isMultipleType){
                    binding.cb10.setText(option);
                } else {
                    binding.rb10.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionTen(option);
                addOptionsDialog.dismiss();
                break;
            case 11:
                if (isMultipleType){
                    binding.cb11.setText(option);
                } else {
                    binding.rb11.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionEleven(option);
                addOptionsDialog.dismiss();
                break;
            case 12:
                if (isMultipleType){
                    binding.cb12.setText(option);
                } else {
                    binding.rb12.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionTwelve(option);
                addOptionsDialog.dismiss();
                break;
            case 13:
                if (isMultipleType){
                    binding.cb13.setText(option);
                } else {
                    binding.rb13.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionThirteen(option);
                addOptionsDialog.dismiss();
                break;
            case 14:
                if (isMultipleType){
                    binding.cb14.setText(option);
                } else {
                    binding.rb14.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionFourteen(option);
                addOptionsDialog.dismiss();
                break;
            case 15:
                if (isMultipleType){
                    binding.cb15.setText(option);
                } else {
                    binding.rb15.setText(option);
                }
                QUESTION_LIST.get(counter).setOptionFifteen(option);
                addOptionsDialog.dismiss();
                break;
        }
    }


    @Override
    public void onOkButtonClick() {
        startActivity(new Intent(AdminPreviewActivity.this, AdminHomeActivity.class));
        finish();
    }
}
