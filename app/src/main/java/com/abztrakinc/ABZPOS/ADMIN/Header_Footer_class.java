package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import a1088sdk.PrnDspA1088;

public class Header_Footer_class {
    public int getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(int headerID) {
        HeaderID = headerID;
    }

    public String getHeaderText() {
        return HeaderText;
    }

    public void setHeaderText(String headerText) {
        HeaderText = headerText;
    }

    public String getHeaderDouble() {
        return HeaderDouble;
    }

    public void setHeaderDouble(String headerDouble) {
        HeaderDouble = headerDouble;
    }

    public int getFooterID() {
        return FooterID;
    }

    public void setFooterID(int footerID) {
        FooterID = footerID;
    }

    public String getFooterText() {
        return FooterText;
    }

    public void setFooterText(String footerText) {
        FooterText = footerText;
    }

    public String getFooterDouble() {
        return FooterDouble;
    }

    public void setFooterDouble(String footerDouble) {
        FooterDouble = footerDouble;
    }

    int HeaderID;
    String HeaderText;
    String HeaderDouble;

    public int getHeaderTotal() {
        return headerTotal;
    }

    public void setHeaderTotal(int headerTotal) {
        this.headerTotal = headerTotal;
    }

    int headerTotal=0;
    int FooterID;
    String FooterText;
    String FooterDouble;


    public void HeaderNote(Context context){
        SQLiteDatabase PosSettings = context.openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
        HeaderText="";

        Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader", null);
        if (getTextToUpdate.getCount()!=0){
            while (getTextToUpdate.moveToNext()){
                HeaderText = HeaderText + getTextToUpdate.getString(1) + "\n";
            }
        }




        PosSettings.close();
    }
    public void FooterNote(Context context){
        SQLiteDatabase PosSettings = context.openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
        FooterText="";

        Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptFooter", null);
        if (getTextToUpdate.getCount()!=0){
            while (getTextToUpdate.moveToNext()){
                FooterText = FooterText + getTextToUpdate.getString(1) + "\n";
            }
        }
        PosSettings.close();
    }



    public void HeaderNoteTotal(Context context){
        SQLiteDatabase PosSettings = context.openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
        HeaderText="";

        Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader", null);
        if (getTextToUpdate.getCount()!=0){
            while (getTextToUpdate.moveToNext()){
                HeaderText = HeaderText + getTextToUpdate.getString(1) + "\n";
            }
        }

        headerTotal=HeaderText.length();



        PosSettings.close();
    }





}
