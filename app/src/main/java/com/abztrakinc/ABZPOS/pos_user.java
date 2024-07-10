package com.abztrakinc.ABZPOS;

import android.content.Context;

import com.abztrakinc.ABZPOS.LOGIN.shift_active;

public class pos_user {
    public String getCashierName() {
        return CashierName;
    }

    public void setCashierName(String cashierName) {
        CashierName = cashierName;
    }

    public String getCashierID() {
        return CashierID;
    }

    public void setCashierID(String cashierID) {
        CashierID = cashierID;
    }

    public String getPOSCounter() {
        return POSCounter;
    }

    public void setPOSCounter(String POSCounter) {
        this.POSCounter = POSCounter;
    }

    public String getCashierPassword() {
        return CashierPassword;
    }

    public void setCashierPassword(String cashierPassword) {
        CashierPassword = cashierPassword;
    }

    private String CashierName,CashierID,POSCounter,CashierPassword;

    public void loadShiftActive(Context context){
        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(context.getApplicationContext());
        shift_active.getAccountInfo(context.getApplicationContext());
        setCashierName(shift_active.getActiveUserName());
        setPOSCounter(shift_active.getPOSCounter());
        setCashierID(shift_active.getActiveUserID());
    }

}
