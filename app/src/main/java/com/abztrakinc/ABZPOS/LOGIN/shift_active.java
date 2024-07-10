package com.abztrakinc.ABZPOS.LOGIN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class shift_active {
    public String getActiveUserID() {
        return ActiveUserID;
    }

    public void setActiveUserID(String activeUserID) {
        ActiveUserID = activeUserID;
    }

    public String getShiftActive() {
        return ShiftActive;
    }

    public void setShiftActive(String shiftActive) {
        ShiftActive = shiftActive;
    }

    private String ActiveUserID;
    private String ShiftActive;

    public String getPOSCounter() {
        POSCounter="1 ECR";
        return POSCounter;
    }

    public void setPOSCounter(String POSCounter) {
        this.POSCounter = POSCounter;
    }

    private String POSCounter;

    public String getActiveUserName() {
        return ActiveUserName;
    }

    public void setActiveUserName(String activeUserName) {
        ActiveUserName = activeUserName;
    }

    private String ActiveUserName;

    public void getShiftingTable(Context context){
        SQLiteDatabase db2 = context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor c2 = db2.rawQuery("select * from ShiftingTable", null);
        if (c2.getCount()!=0){
            while(c2.moveToNext()){
                setShiftActive(c2.getString(2));
                setActiveUserID(c2.getString(3));
            }
        }
        db2.close();
    }

    public void getAccountInfo(Context context){
        getShiftingTable(context.getApplicationContext());
        SQLiteDatabase db = context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from Accountsettings where accountNumber='"+ getActiveUserID()+"'", null);
        if (cursor.getCount()!=0){
            while(cursor.moveToNext()){
                setActiveUserName(cursor.getString(2));

            }
        }
    }
}
