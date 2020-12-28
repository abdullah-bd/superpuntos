package com.iotait.superpuntos.models;

import java.util.List;

public class Survey {
    private String surveyId;
    private String surveyName;
    private String surveyCategory;
    private String surveyPoints;
    private String surveyMaxParticipant = "100";
    private String surveyDeadline;
    private List<Question> questionList;
    private String surveyStartDate;
    private String userGroupId;
    int openFor,repeatationDay,userConditionDay;
    boolean repeatable = false;

    public Survey() {
    }

    public Survey(String surveyName, String surveyCategory, List<Question> questionList) {
        this.surveyName = surveyName;
        this.surveyCategory = surveyCategory;
        this.questionList = questionList;
    }

    public Survey(String surveyId, String surveyName, String surveyCategory, List<Question> questionList) {
        this.surveyId = surveyId;
        this.surveyName = surveyName;
        this.surveyCategory = surveyCategory;
        this.questionList = questionList;
    }

    public String getSurveyStartDate() {
        return surveyStartDate;
    }

    public void setSurveyStartDate(String surveyStartDate) {
        this.surveyStartDate = surveyStartDate;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyCategory() {
        return surveyCategory;
    }

    public void setSurveyCategory(String surveyCategory) {
        this.surveyCategory = surveyCategory;
    }

    public String getSurveyPoints() {
        return surveyPoints;
    }

    public void setSurveyPoints(String surveyPoints) {
        this.surveyPoints = surveyPoints;
    }

    public String getSurveyMaxParticipant() {
        return surveyMaxParticipant;
    }

    public void setSurveyMaxParticipant(String surveyMaxParticipant) {
        this.surveyMaxParticipant = surveyMaxParticipant;
    }

    public String getSurveyDeadline() {
        return surveyDeadline;
    }

    public void setSurveyDeadline(String surveyDeadline) {
        this.surveyDeadline = surveyDeadline;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public String getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }

    public int getOpenFor() {
        return openFor;
    }

    public void setOpenFor(int openFor) {
        this.openFor = openFor;
    }

    public int getRepeatationDay() {
        return repeatationDay;
    }

    public void setRepeatationDay(int repeatationDay) {
        this.repeatationDay = repeatationDay;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public int getUserConditionDay() {
        return userConditionDay;
    }

    public void setUserConditionDay(int userConditionDay) {
        this.userConditionDay = userConditionDay;
    }
}
