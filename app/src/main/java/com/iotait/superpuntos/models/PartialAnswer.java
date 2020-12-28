package com.iotait.superpuntos.models;

import java.util.List;

public class PartialAnswer {
    List<String> ans;
    String sID;

    public PartialAnswer() {
    }



    public PartialAnswer(List<String> ans, String sID) {
        this.ans = ans;
        this.sID = sID;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public List<String> getAns() {
        return ans;
    }

    public void setAns(List<String> ans) {
        this.ans = ans;
    }
}
