package com.abztrakinc.ABZPOS.CASHIER;

public class cashier_shift_model {


    public String getTransactionID() {
        return TransactionID;
    }

    public void setTransactionID(String transactionID) {
        TransactionID = transactionID;
    }

    public String getTransactionInvoice() {
        return TransactionInvoice;
    }

    public void setTransactionInvoice(String transactionInvoice) {
        TransactionInvoice = transactionInvoice;
    }

    public String getTransactionOrderID() {
        return TransactionOrderID;
    }

    public void setTransactionOrderID(String transactionOrderID) {
        TransactionOrderID = transactionOrderID;
    }

    public String getTransactionItemName() {
        return TransactionItemName;
    }

    public void setTransactionItemName(String transactionItemName) {
        TransactionItemName = transactionItemName;
    }

    public String getTransactionItemQty() {
        return TransactionItemQty;
    }

    public void setTransactionItemQty(String transactionItemQty) {
        TransactionItemQty = transactionItemQty;
    }

    public String getTransactionTotalPrice() {
        return TransactionTotalPrice;
    }

    public void setTransactionTotalPrice(String transactionTotalPrice) {
        TransactionTotalPrice = transactionTotalPrice;
    }

    public String getTransactionTotalDisc() {
        return TransactionTotalDisc;
    }

    public void setTransactionTotalDisc(String transactionTotalDisc) {
        TransactionTotalDisc = transactionTotalDisc;
    }

    public String getTransactionTotalAmount() {
        return TransactionTotalAmount;
    }

    public void setTransactionTotalAmount(String transactionTotalAmount) {
        TransactionTotalAmount = transactionTotalAmount;
    }

    public String getTransactionTime() {
        return TransactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        TransactionTime = transactionTime;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    String TransactionID;
    String TransactionInvoice;
    String TransactionOrderID;
    String TransactionItemName;
    String TransactionItemQty;
    String TransactionTotalPrice;
    String TransactionTotalDisc;
    String TransactionTotalAmount;
    String TransactionTime;
    String TransactionDate;



//    public cashier_shift_model(String transactionID, String transactionInvoice, String transactionOrderID, String transactionItemName,
//                               String transactionItemQty, String transactionTotalPrice,String transactionTotalDisc,String transactionTotalAmount,
//                               String transactionTime,String transactionDate){
//
//        this.TransactionID=transactionID;
//        this.TransactionInvoice=transactionInvoice;
//        this.TransactionOrderID=transactionOrderID;
//        this.TransactionItemName=transactionItemName;
//        this.TransactionItemQty=transactionItemQty;
//        this.TransactionTotalPrice=transactionTotalPrice;
//        this.TransactionTotalDisc=transactionTotalDisc;
//        this.TransactionTotalAmount=transactionTotalAmount;
//        this.TransactionTime=transactionTime;
//        this.TransactionDate=transactionDate;
//
//
//    }


    public cashier_shift_model(String transactionID, String transactionOrderID, String transactionItemName,
                               String transactionItemQty, String transactionTotalPrice,String transactionTotalDisc,String transactionTotalAmount,
                               String transactionTime,String transactionDate){

        this.TransactionID=transactionID;
        this.TransactionOrderID=transactionOrderID;
        this.TransactionItemName=transactionItemName;
        this.TransactionItemQty=transactionItemQty;
        this.TransactionTotalPrice=transactionTotalPrice;
        this.TransactionTotalDisc=transactionTotalDisc;
        this.TransactionTotalAmount=transactionTotalAmount;
        this.TransactionTime=transactionTime;
        this.TransactionDate=transactionDate;


    }





}
