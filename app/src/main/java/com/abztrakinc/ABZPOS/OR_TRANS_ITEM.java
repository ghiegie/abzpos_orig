package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OR_TRANS_ITEM {

    private String officialReceiptNo;
    private String TransactionNo;


    public String getOfficialReceiptNo() {
        return officialReceiptNo;
    }

    public void setOfficialReceiptNo(String officialReceiptNo) {
        this.officialReceiptNo = officialReceiptNo;
    }

    public String getTransactionNo() {
        return TransactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        TransactionNo = transactionNo;
    }



    public void readReferenceNumber(Context context) {

        //primary key 00000001

        // int readPK;
        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //   db2 = this .openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        Cursor itemListC = db2.rawQuery("select * from InvoiceReceiptTotal ", null);
        if (itemListC.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%010d", origRefNumber);
          //  cashItem.setTransactionID(formatted);
            setTransactionNo(formatted);
        } else {

            itemListC = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%010d", incrementPK);

            // readRefNumber = incrementPKString;
           // cashItem.setTransactionID(incrementPKString);
            setTransactionNo(incrementPKString);


            // }
        }
        itemListC.close();
        db2.close();


    }

    public void readOfficialReceiptNumber(Context context) {

        //primary key 00000001

        // int readPK;
        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //   db2 = this .openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        Cursor itemListC = db2.rawQuery("select * from OfficialReceipt ", null);
        if (itemListC.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%010d", origRefNumber);
            //  cashItem.setTransactionID(formatted);
            setOfficialReceiptNo(formatted);
        } else {

            itemListC = db2.rawQuery("SELECT * FROM OfficialReceipt", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%010d", incrementPK);

            // readRefNumber = incrementPKString;
            // cashItem.setTransactionID(incrementPKString);
            setOfficialReceiptNo(incrementPKString);


            // }
        }
        itemListC.close();
        db2.close();


    }



}
