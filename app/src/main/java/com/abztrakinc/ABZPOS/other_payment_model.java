package com.abztrakinc.ABZPOS;

public class other_payment_model {


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPaymentName() {
        return PaymentName;
    }

    public void setPaymentName(String paymentName) {
        PaymentName = paymentName;
    }

    public String getAllowReference() {
        return AllowReference;
    }

    public void setAllowReference(String allowReference) {
        AllowReference = allowReference;
    }

    public String getAllowDetails() {
        return AllowDetails;
    }

    public void setAllowDetails(String allowDetails) {
        AllowDetails = allowDetails;
    }

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }

    public int getChangeType() {
        return ChangeType;
    }

    public void setChangeType(int changeType) {
        ChangeType = changeType;
    }

    int ID;
    String PaymentName;
    String AllowReference;
    String AllowDetails;
    int PaymentType;
    int ChangeType;

    public other_payment_model(int iD, String paymentName, String allowReference, String allowDetails, int paymentType,
                               int changeType){

        this.ID=iD;
        this.PaymentName=paymentName;
        this.AllowReference=allowReference;
        this.AllowDetails=allowDetails;
        this.PaymentType=paymentType;
        this.ChangeType=changeType;


    }
}
