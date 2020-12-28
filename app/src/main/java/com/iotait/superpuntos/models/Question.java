package com.iotait.superpuntos.models;

public class Question {
    private String question;
    private boolean isMultipleType;
    private String optionOne, optionTwo, optionThree, optionFour,optionFive,optionSix,optionSeven,optionEight,optionNine,optionTen,optionEleven,optionTwelve,optionThirteen,optionFourteen,optionFifteen;

    public Question() {
    }

    public Question(String question, boolean isMultipleType, String optionOne, String optionTwo, String optionThree, String optionFour) {
        this.question = question;
        this.isMultipleType = isMultipleType;
        this.optionOne = optionOne;
        this.optionTwo = optionTwo;
        this.optionThree = optionThree;
        this.optionFour = optionFour;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isMultipleType() {
        return isMultipleType;
    }

    public void setMultipleType(boolean multipleType) {
        isMultipleType = multipleType;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public void setOptionOne(String optionOne) {
        this.optionOne = optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(String optionTwo) {
        this.optionTwo = optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public void setOptionThree(String optionThree) {
        this.optionThree = optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public void setOptionFour(String optionFour) {
        this.optionFour = optionFour;
    }

    public String getOptionFive() {
        return optionFive;
    }

    public void setOptionFive(String optionFive) {
        this.optionFive = optionFive;
    }

    public String getOptionSix() {
        return optionSix;
    }

    public void setOptionSix(String optionSix) {
        this.optionSix = optionSix;
    }

    public String getOptionSeven() {
        return optionSeven;
    }

    public void setOptionSeven(String optionSeven) {
        this.optionSeven = optionSeven;
    }

    public String getOptionEight() {
        return optionEight;
    }

    public void setOptionEight(String optionEight) {
        this.optionEight = optionEight;
    }

    public String getOptionNine() {
        return optionNine;
    }

    public void setOptionNine(String optionNine) {
        this.optionNine = optionNine;
    }

    public String getOptionTen() {
        return optionTen;
    }

    public void setOptionTen(String optionTen) {
        this.optionTen = optionTen;
    }

    public String getOptionEleven() {
        return optionEleven;
    }

    public void setOptionEleven(String optionEleven) {
        this.optionEleven = optionEleven;
    }

    public String getOptionTwelve() {
        return optionTwelve;
    }

    public void setOptionTwelve(String optionTwelve) {
        this.optionTwelve = optionTwelve;
    }

    public String getOptionThirteen() {
        return optionThirteen;
    }

    public void setOptionThirteen(String optionThirteen) {
        this.optionThirteen = optionThirteen;
    }

    public String getOptionFourteen() {
        return optionFourteen;
    }

    public void setOptionFourteen(String optionFourteen) {
        this.optionFourteen = optionFourteen;
    }

    public String getOptionFifteen() {
        return optionFifteen;
    }

    public void setOptionFifteen(String optionFifteen) {
        this.optionFifteen = optionFifteen;
    }
}
