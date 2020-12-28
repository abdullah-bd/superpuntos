package com.iotait.superpuntos.models;

public class Contact {
    String name;
    String phone;
    String countyCode;
    String operator;

    public Contact() {
    }

    public Contact(String name, String phone, String countyCode, String operator) {
        this.name = name;
        this.phone = phone;
        this.countyCode = countyCode;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
