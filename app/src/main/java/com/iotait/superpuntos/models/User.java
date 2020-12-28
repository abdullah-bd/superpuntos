package com.iotait.superpuntos.models;

public class User {
    private String version;
    private String phone;
    private String email;
    private String password;
    private String name;
    private String occupation;
    private String age;
    private String sex;
    private String countryCode;
    private String uid;
    private int coins;
    private String creationDate;
    private int totalClaimed = 0;
    private String fcmToken;
    private String lastWithdraw = "-----";
    String uri;
    String city;
    int shareCount;
    String phoneModel;

    public User() {
    }

    public User(String phone, String password, String name, String occupation, String age, String sex, String countryCode, String uid, int coins, String creationDate) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.occupation = occupation;
        this.age = age;
        this.sex = sex;
        this.countryCode = countryCode;
        this.uid = uid;
        this.coins = coins;
        this.creationDate = creationDate;
    }

    public User(String phone, String email, String name, String uid, int coins, String creationDate) {
        this.phone = phone;
        this.email = email;
        this.name = name;
        this.uid = uid;
        this.coins = coins;
        this.creationDate = creationDate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getUid() {
        return uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getTotalClaimed() {
        return totalClaimed;
    }

    public void setTotalClaimed(int totalClaimed) {
        this.totalClaimed = totalClaimed;
    }

    public String getLastWithdraw() {
        return lastWithdraw;
    }

    public void setLastWithdraw(String lastWithdraw) {
        this.lastWithdraw = lastWithdraw;
    }
}
