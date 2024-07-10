package com.abztrakinc.ABZPOS.CASHIER;

import java.util.ArrayList;

public class other_payment_model {

    public String getOtherPaymentAutoIDList() {
        return OtherPaymentAutoIDList;
    }

    public void setOtherPaymentAutoIDList(String otherPaymentAutoIDList) {
        OtherPaymentAutoIDList = otherPaymentAutoIDList;
    }

    public String getPaymentNameList() {
        return PaymentNameList;
    }

    public void setPaymentNameList(String paymentNameList) {
        PaymentNameList = paymentNameList;
    }

    public String getAllowReferenceList() {
        return AllowReferenceList;
    }

    public void setAllowReferenceList(String allowReferenceList) {
        AllowReferenceList = allowReferenceList;
    }

    public String getAllowUserDetailsList() {
        return AllowUserDetailsList;
    }

    public void setAllowUserDetailsList(String allowUserDetailsList) {
        AllowUserDetailsList = allowUserDetailsList;
    }

    public int getButtonID() {
        return ButtonID;
    }

    public void setButtonID(int buttonID) {
        ButtonID = buttonID;
    }

    //    OtherPaymentAutoIDList=new ArrayList<>();
//    PaymentNameList=new ArrayList<>();
//    AllowReferenceList=new ArrayList<>();
//    AllowUserDetailsList=new ArrayList<>();
    int ButtonID;
    String OtherPaymentAutoIDList,PaymentNameList,AllowReferenceList,AllowUserDetailsList;
    public other_payment_model(int buttonID,String otherPaymentAutoIDList,String paymentNameList,String allowReferenceList,String allowUserDetailsList){
        this.ButtonID = buttonID;
        this.OtherPaymentAutoIDList = otherPaymentAutoIDList;
        this.PaymentNameList= paymentNameList;
        this.AllowReferenceList = allowReferenceList;
        this.AllowUserDetailsList=allowUserDetailsList;
    }
}
