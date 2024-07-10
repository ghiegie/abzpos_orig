package com.abztrakinc.ABZPOS.CASHIER;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class cashier_payment_printer
{
    public String getKitchenPrinter() {
        return KitchenPrinter;
    }

    public void setKitchenPrinter(String kitchenPrinter) {
        KitchenPrinter = kitchenPrinter;
    }

    public String getOrderSummaryPrinter() {
        return OrderSummaryPrinter;
    }

    public void setOrderSummaryPrinter(String orderSummaryPrinter) {
        OrderSummaryPrinter = orderSummaryPrinter;
    }

    String KitchenPrinter;
    String OrderSummaryPrinter;

    public String CheckPrinter(SQLiteDatabase posSettings,int PrintLoc){
        String status="";

        Cursor cursor = posSettings.rawQuery("select PrinterAddress from PrinterSettings where PrintID='"+PrintLoc+"'", null);
        if (cursor.getCount()!=0){
            if (cursor.moveToFirst()){
                if (cursor.getString(1).equals("OFF")){
                    status="OFF";
                }
                else{
                    status="ON";
                }
            }

        }
        else{
            status="OFF";
        }
        return status;

    }


}
