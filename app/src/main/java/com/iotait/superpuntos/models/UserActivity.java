package com.iotait.superpuntos.models;

public class UserActivity {

    private String surveyName;
    private String date;
    private String earning;

    public UserActivity() {
    }

    public UserActivity(String surveyName, String date, String earning) {
        this.surveyName = surveyName;
        this.date = date;
        this.earning = earning;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEarning() {
        return earning;
    }

    public void setEarning(String earning) {
        this.earning = earning;
    }
}
