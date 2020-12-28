package com.iotait.superpuntos.models;

public class ClaimedReward {
    private String id;
    private String userId;
    private String userName;
    private String claimingDate;
    private String claimedAmount;
    String uri;
    boolean processing=true;
    boolean validnumber=true;
    public ClaimedReward() {
    }

    public ClaimedReward(String id, String userId, String userName, String claimingDate, String claimedAmount,String uri) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.claimingDate = claimingDate;
        this.claimedAmount = claimedAmount;
        this.uri = uri;
    }

    public boolean isProcessing() {
        return processing;
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClaimingDate() {
        return claimingDate;
    }

    public void setClaimingDate(String claimingDate) {
        this.claimingDate = claimingDate;
    }

    public String getClaimedAmount() {
        return claimedAmount;
    }

    public void setClaimedAmount(String claimedAmount) {
        this.claimedAmount = claimedAmount;
    }

    public boolean isValidnumber() {
        return validnumber;
    }

    public void setValidnumber(boolean validnumber) {
        this.validnumber = validnumber;
    }
}
