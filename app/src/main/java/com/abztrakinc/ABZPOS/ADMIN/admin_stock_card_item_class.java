package com.abztrakinc.ABZPOS.ADMIN;

public class admin_stock_card_item_class {


    public String getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

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

    public String getDataIn() {
        return DataIn;
    }

    public void setDataIn(String dataIn) {
        DataIn = dataIn;
    }

    public String getDataOut() {
        return DataOut;
    }

    public void setDataOut(String dataOut) {
        DataOut = dataOut;
    }

    public String getDataBalance() {
        return DataBalance;
    }

    public void setDataBalance(String dataBalance) {
        DataBalance = dataBalance;
    }

    String TransactionNo,Remarks,Date,Time,DataIn,DataOut,DataBalance;

    public String getDialogContent() {
        return DialogContent;
    }

    public void setDialogContent(String dialogContent) {
        DialogContent = dialogContent;
    }

    String DialogContent;

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }

    String UserAccount;

    public admin_stock_card_item_class(String transactionNo,String remarks,String date,String time,String dataIn,String dataOut,String dataBalance,String userAccount,String dialogContent){
        this.TransactionNo = transactionNo;
        this.Remarks = remarks;
        this.Date = date;
        this.Time = time;
        this.DataIn = dataIn;
        this.DataOut = dataOut;
        this.DataBalance = dataBalance;
        this.UserAccount = userAccount;
        this.DialogContent = dialogContent;


    }

}
