package com.iotait.superpuntos.admin.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iotait.superpuntos.databinding.ActivityAdminAddSurveyBinding;
import com.iotait.superpuntos.helper.AddOptionsDialog;
import com.iotait.superpuntos.models.Question;
import com.iotait.superpuntos.models.Survey;

import java.util.Calendar;

import static com.iotait.superpuntos.helper.Constants.ADMIN_SURVEY;
import static com.iotait.superpuntos.helper.Constants.QUESTION_LIST;

public class AdminAddSurveyActivity extends AppCompatActivity implements AddOptionsDialog.OnAddButtonClick {

    ActivityAdminAddSurveyBinding binding;
    AddOptionsDialog addOptionsDialog;
    Question question = new Question();
    DatePickerDialog datePickerDialog;
    boolean isRefreshable = false;
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddSurveyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setClickListeners();
    }

    private void setClickListeners() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.rb.setChecked(false);
                }
            }
        });

        binding.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.cb.setChecked(false);
                }
            }
        });

        binding.tvAppbarPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewSurvey();
            }
        });

        binding.tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndPreview();
            }
        });

        binding.cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.cb.isChecked() && !binding.rb.isChecked()) {
                    Toast.makeText(AdminAddSurveyActivity.this, "Please select a type first", Toast.LENGTH_SHORT).show();
                } else {
                    showAddOptionDialog();
                }
            }
        });

        binding.btnAddMoreQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWithoutPreview();
                if (isRefreshable) {
                    refreshViews();
                }
            }
        });

        binding.tvTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(AdminAddSurveyActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        String formattedDay = String.format("%02d", dayOfMonth);
                        binding.tvTimeline.setText(formattedDay + " "
                                + months[monthOfYear] + ", " + year);
                        binding.tvTimeline.setError(null);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void saveSurvey() {
        question.setQuestion(binding.etQuestion.getText().toString());
        question.setMultipleType(binding.cb.isChecked());
        QUESTION_LIST.add(question);
        ADMIN_SURVEY.setSurveyName(binding.etSurvey.getText().toString());
        ADMIN_SURVEY.setSurveyCategory(binding.etCategory.getText().toString());
        ADMIN_SURVEY.setSurveyPoints(binding.etPoints.getText().toString());
        ADMIN_SURVEY.setSurveyMaxParticipant(binding.etParticipant.getText().toString());
        ADMIN_SURVEY.setSurveyDeadline(binding.tvTimeline.getText().toString());
    }

    private void previewSurvey() {
        if (QUESTION_LIST.size() > 0) {
            ADMIN_SURVEY.setQuestionList(QUESTION_LIST);
            startActivity(new Intent(AdminAddSurveyActivity.this, AdminPreviewActivity.class));
        } else {
            Toast.makeText(this, "Add at least one question to your survey", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveAndPreview() {
        if (TextUtils.isEmpty(binding.etSurvey.getText())) {
            binding.etSurvey.setError("Please add a survey name");
            binding.etSurvey.requestFocus();
        } else if (TextUtils.isEmpty(binding.etCategory.getText())) {
            binding.etCategory.setError("Please add a category name");
            binding.etCategory.requestFocus();
        } else if (TextUtils.isEmpty(binding.etPoints.getText())) {
            binding.etPoints.setError("Please add survey points");
            binding.etPoints.requestFocus();
        } else if (TextUtils.isEmpty(binding.etParticipant.getText())) {
            binding.etParticipant.setError("Please add survey max participant");
            binding.etParticipant.requestFocus();
        } else if (TextUtils.isEmpty(binding.tvTimeline.getText())) {
            binding.tvTimeline.setError("Please add a deadline");
            binding.tvTimeline.requestFocus();
        } else if (TextUtils.isEmpty(binding.etQuestion.getText())) {
            binding.etQuestion.setError("Please add a question");
            binding.etQuestion.requestFocus();
        } else if (!binding.cb.isChecked() && !binding.rb.isChecked()) {
            Toast.makeText(AdminAddSurveyActivity.this, "Please select a type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(binding.cb1.getText()) && TextUtils.isEmpty(binding.rb1.getText())) {
            Toast.makeText(AdminAddSurveyActivity.this, "Please add at least one option", Toast.LENGTH_SHORT).show();
        } else {
            saveSurvey();
            previewSurvey();
        }
    }

    private void saveWithoutPreview() {
        if (TextUtils.isEmpty(binding.etSurvey.getText())) {
            binding.etSurvey.setError("Please add a survey name");
            binding.etSurvey.requestFocus();
        } else if (TextUtils.isEmpty(binding.etCategory.getText())) {
            binding.etCategory.setError("Please add a category name");
            binding.etCategory.requestFocus();
        } else if (TextUtils.isEmpty(binding.etPoints.getText())) {
            binding.etPoints.setError("Please add survey points");
            binding.etPoints.requestFocus();
        } else if (TextUtils.isEmpty(binding.etParticipant.getText())) {
            binding.etParticipant.setError("Please add survey max participant");
            binding.etParticipant.requestFocus();
        } else if (TextUtils.isEmpty(binding.tvTimeline.getText())) {
            binding.tvTimeline.setError("Please add a deadline");
            binding.tvTimeline.requestFocus();
        } else if (TextUtils.isEmpty(binding.etQuestion.getText())) {
            binding.etQuestion.setError("Please add a question");
            binding.etQuestion.requestFocus();
        } else if (!binding.cb.isChecked() && !binding.rb.isChecked()) {
            Toast.makeText(AdminAddSurveyActivity.this, "Please select a type", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(binding.cb1.getText()) && TextUtils.isEmpty(binding.rb1.getText())) {
            Toast.makeText(AdminAddSurveyActivity.this, "Please add at least one option", Toast.LENGTH_SHORT).show();
        } else {
            isRefreshable = true;
            saveSurvey();
        }
    }

    private void refreshViews() {
        isRefreshable = false;
        question = new Question();
        binding.etQuestion.setText("");
        binding.cb.setChecked(false);
        binding.rb.setChecked(false);
        binding.cb1.setText("");
        binding.rb1.setText("");
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
        binding.cvAdd.setVisibility(View.VISIBLE);
        binding.tvAddMore.setVisibility(View.VISIBLE);
    }

    private void showAddOptionDialog() {
        addOptionsDialog = new AddOptionsDialog(this, "Add Option", "");
        addOptionsDialog.setListener(this);
        addOptionsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addOptionsDialog.show();
    }

    @Override
    public void onAddButtonClick(String option) {
        if (TextUtils.isEmpty(option)) {
            Toast.makeText(this, "Please type your option", Toast.LENGTH_SHORT).show();
        } else {
            if (binding.cb.isChecked()) {
                if (binding.cb1.getVisibility() == View.GONE) {
                    binding.cb1.setText(option);
                    question.setOptionOne(option);
                    binding.cb1.setVisibility(View.VISIBLE);
                } else if (binding.cb2.getVisibility() == View.GONE) {
                    binding.cb2.setText(option);
                    question.setOptionTwo(option);
                    binding.cb2.setVisibility(View.VISIBLE);
                } else if (binding.cb3.getVisibility() == View.GONE) {
                    binding.cb3.setText(option);
                    question.setOptionThree(option);
                    binding.cb3.setVisibility(View.VISIBLE);
                } else if (binding.cb4.getVisibility() == View.GONE) {
                    binding.cb4.setText(option);
                    question.setOptionFour(option);
                    binding.cb4.setVisibility(View.VISIBLE);

                }else if (binding.cb5.getVisibility() == View.GONE) {
                    binding.cb5.setText(option);
                    question.setOptionFive(option);
                    binding.cb5.setVisibility(View.VISIBLE);
                }else if (binding.cb6.getVisibility() == View.GONE) {
                    binding.cb6.setText(option);
                    question.setOptionSix(option);
                    binding.cb6.setVisibility(View.VISIBLE);
                }else if (binding.cb7.getVisibility() == View.GONE) {
                    binding.cb7.setText(option);
                    question.setOptionSeven(option);
                    binding.cb7.setVisibility(View.VISIBLE);
                }else if (binding.cb8.getVisibility() == View.GONE) {
                    binding.cb8.setText(option);
                    question.setOptionEight(option);
                    binding.cb8.setVisibility(View.VISIBLE);
                }else if (binding.cb9.getVisibility() == View.GONE) {
                    binding.cb9.setText(option);
                    question.setOptionNine(option);
                    binding.cb9.setVisibility(View.VISIBLE);
                }else if (binding.cb10.getVisibility() == View.GONE) {
                    binding.cb10.setText(option);
                    question.setOptionTen(option);
                    binding.cb10.setVisibility(View.VISIBLE);
                }else if (binding.cb11.getVisibility() == View.GONE) {
                    binding.cb11.setText(option);
                    question.setOptionEleven(option);
                    binding.cb11.setVisibility(View.VISIBLE);
                }else if (binding.cb12.getVisibility() == View.GONE) {
                    binding.cb12.setText(option);
                    question.setOptionTwelve(option);
                    binding.cb12.setVisibility(View.VISIBLE);
                }else if (binding.cb13.getVisibility() == View.GONE) {
                    binding.cb13.setText(option);
                    question.setOptionThirteen(option);
                    binding.cb13.setVisibility(View.VISIBLE);
                }else if (binding.cb14.getVisibility() == View.GONE) {
                    binding.cb14.setText(option);
                    question.setOptionFourteen(option);
                    binding.cb14.setVisibility(View.VISIBLE);
                }else if (binding.cb15.getVisibility() == View.GONE) {
                    binding.cb15.setText(option);
                    question.setOptionFifteen(option);
                    binding.cb15.setVisibility(View.VISIBLE);
                    binding.cvAdd.setVisibility(View.GONE);
                    binding.tvAddMore.setVisibility(View.GONE);
                }

                addOptionsDialog.dismiss();
            } else {
                if (binding.rb1.getVisibility() == View.GONE) {
                    binding.rb1.setText(option);
                    question.setOptionOne(option);
                    binding.rb1.setVisibility(View.VISIBLE);
                } else if (binding.rb2.getVisibility() == View.GONE) {
                    binding.rb2.setText(option);
                    question.setOptionTwo(option);
                    binding.rb2.setVisibility(View.VISIBLE);
                } else if (binding.rb3.getVisibility() == View.GONE) {
                    binding.rb3.setText(option);
                    question.setOptionThree(option);
                    binding.rb3.setVisibility(View.VISIBLE);
                } else if (binding.rb4.getVisibility() == View.GONE) {
                    binding.rb4.setText(option);
                    question.setOptionFour(option);
                    binding.rb4.setVisibility(View.VISIBLE);

                } else if (binding.rb5.getVisibility() == View.GONE) {
                    binding.rb5.setText(option);
                    question.setOptionFive(option);
                    binding.rb5.setVisibility(View.VISIBLE);
                }else if (binding.rb6.getVisibility() == View.GONE) {
                    binding.rb6.setText(option);
                    question.setOptionSix(option);
                    binding.rb6.setVisibility(View.VISIBLE);
                }else if (binding.rb7.getVisibility() == View.GONE) {
                    binding.rb7.setText(option);
                    question.setOptionSeven(option);
                    binding.rb7.setVisibility(View.VISIBLE);
                }else if (binding.rb8.getVisibility() == View.GONE) {
                    binding.rb8.setText(option);
                    question.setOptionEight(option);
                    binding.rb8.setVisibility(View.VISIBLE);
                }else if (binding.rb9.getVisibility() == View.GONE) {
                    binding.rb9.setText(option);
                    question.setOptionNine(option);
                    binding.rb9.setVisibility(View.VISIBLE);
                }else if (binding.rb10.getVisibility() == View.GONE) {
                    binding.rb10.setText(option);
                    question.setOptionTen(option);
                    binding.rb10.setVisibility(View.VISIBLE);
                }else if (binding.rb11.getVisibility() == View.GONE) {
                    binding.rb11.setText(option);
                    question.setOptionEleven(option);
                    binding.rb11.setVisibility(View.VISIBLE);
                }else if (binding.rb12.getVisibility() == View.GONE) {
                    binding.rb12.setText(option);
                    question.setOptionTwelve(option);
                    binding.rb12.setVisibility(View.VISIBLE);
                }else if (binding.rb13.getVisibility() == View.GONE) {
                    binding.rb13.setText(option);
                    question.setOptionThirteen(option);
                    binding.rb13.setVisibility(View.VISIBLE);
                }else if (binding.rb14.getVisibility() == View.GONE) {
                    binding.rb14.setText(option);
                    question.setOptionFourteen(option);
                    binding.rb14.setVisibility(View.VISIBLE);
                }else if (binding.rb15.getVisibility() == View.GONE) {
                    binding.rb15.setText(option);
                    question.setOptionFifteen(option);
                    binding.rb15.setVisibility(View.VISIBLE);
                    binding.cvAdd.setVisibility(View.GONE);
                    binding.tvAddMore.setVisibility(View.GONE);
                }
                addOptionsDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ADMIN_SURVEY = new Survey();
        QUESTION_LIST.clear();
    }
}
