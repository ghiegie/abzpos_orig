package com.abztrakinc.ABZPOS.ADMIN;

public class admin_audit_trail_item {
    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserShift() {
        return UserShift;
    }

    public void setUserShift(String userShift) {
        UserShift = userShift;
    }

    int No;
    String TransactionNo;
    String Activity;
    String User;
    String Date;
    String UserShift;

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    String invoice;

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    String TotalAmount;

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    String Time;



    public admin_audit_trail_item(int ItemNo,String TransactionNo,String inv,String Activity,String User,String totalAmount,String Date,String Time,String UserShift){

        this.No=ItemNo;
        this.TransactionNo=TransactionNo;
        this.invoice=inv;
        this.Activity=Activity;
        this.User=User;
        this.TotalAmount=totalAmount;
        this.Date=Date;
        this.Time=Time;
        this.UserShift=UserShift;




    }



}
