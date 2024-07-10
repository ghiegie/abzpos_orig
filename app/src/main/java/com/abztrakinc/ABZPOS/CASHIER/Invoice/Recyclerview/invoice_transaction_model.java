package com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview;

public class invoice_transaction_model {

    public String getTransaction() {
        return Transaction;
    }

    public void setTransaction(String transaction) {
        Transaction = transaction;
    }

    public int getButtonID() {
        return ButtonID;
    }

    public void setButtonID(int buttonID) {
        ButtonID = buttonID;
    }

    int ButtonID;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    String Date;
    String Time;
    String Transaction;
    public invoice_transaction_model(int buttonID, String transaction,String date,String time){
        this.ButtonID = buttonID;
        this.Transaction = transaction;
        this.Date = date;
        this.Time = time;
    }
}
