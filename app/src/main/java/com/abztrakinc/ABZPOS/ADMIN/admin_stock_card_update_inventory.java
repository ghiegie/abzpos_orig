package com.abztrakinc.ABZPOS.ADMIN;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.tool.Context;

import androidx.core.content.ContextCompat;

public class admin_stock_card_update_inventory {

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    String ItemID;
    int Qty;

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }

    public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    SQLiteDatabase sqLiteDatabase;


    public admin_stock_card_update_inventory (SQLiteDatabase db){

//        this.ItemID = itemID;
//        this.Qty = qty;
//
        //update_insert(db);
        this.sqLiteDatabase=db;

    }

    public void update_insert(String itemID,int qty){

//        String selectItem = "select ItemQty from ITEM where ItemID='"+itemID+"'";
//        String updateQty = "update ITEM set ItemQty='"+oldqty+qty+"' where ItemID='"+itemID+"'";
//        sqLiteDatabase.execSQL(updateQty);

        String selectItem = "SELECT ItemQty FROM ITEM WHERE ItemID='" + itemID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectItem, null);

        if (cursor.moveToFirst()) {
            // Get the current quantity from the cursor
            int currentQty = Integer.parseInt(cursor.getString(0));

            // Calculate the new quantity
            int newQty = currentQty + qty;

            // Update the quantity in the database
            String updateQty = "UPDATE ITEM SET ItemQty='" + newQty + "' WHERE ItemID='" + itemID + "'";
            sqLiteDatabase.execSQL(updateQty);
        } else {
            // Handle the case where the item ID does not exist in the database
        }
        cursor.close();


    }

    public void update_subtract(String itemID,int qty){

//        String selectItem = "select ItemQty from ITEM where ItemID='"+itemID+"'";
//        String updateQty = "update ITEM set ItemQty='"+oldqty+qty+"' where ItemID='"+itemID+"'";
//        sqLiteDatabase.execSQL(updateQty);

        String selectItem = "SELECT ItemQty FROM ITEM WHERE ItemID='" + itemID + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(selectItem, null);

        if (cursor.moveToFirst()) {
            // Get the current quantity from the cursor
            int currentQty = Integer.parseInt(cursor.getString(0));

            // Calculate the new quantity
            int newQty = currentQty - qty;

            // Update the quantity in the database
            String updateQty = "UPDATE ITEM SET ItemQty='" + newQty + "' WHERE ItemID='" + itemID + "'";
            sqLiteDatabase.execSQL(updateQty);
        } else {
            // Handle the case where the item ID does not exist in the database
        }
        cursor.close();


    }

    public void zero_out_item(){

            String updateQty = "UPDATE ITEM SET ItemQty='" + "0" + "'";
            sqLiteDatabase.execSQL(updateQty);

    }



}
