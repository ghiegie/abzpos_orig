package com.abztrakinc.ABZPOS.CASHIER;

public class bank_transaction_variables {
    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }

    public String getORNum() {
        return ORNum;
    }

    public void setORNum(String ORNum) {
        this.ORNum = ORNum;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCardNumber() {
        return CardNumber;
    }

    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return CardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        CardExpiry = cardExpiry;
    }

    public String getCardOwner() {
        return CardOwner;
    }

    public void setCardOwner(String cardOwner) {
        CardOwner = cardOwner;
    }

    public String getCardApproval() {
        return CardApproval;
    }

    public void setCardApproval(String cardApproval) {
        CardApproval = cardApproval;
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public String getTransTime() {
        return TransTime;
    }

    public void setTransTime(String transTime) {
        TransTime = transTime;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    private String transNum,ORNum,BankName,CardNumber,CardExpiry,CardOwner,CardApproval,TransDate,TransTime,User;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    private String cardType;
}
