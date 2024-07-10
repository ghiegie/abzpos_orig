package com.abztrakinc.ABZPOS.ADMIN;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.abztrakinc.ABZPOS.Backup_database;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class admin_zero_out_function {

    public void zeroOutFunction(Context context){




//
//
//
//
//
//
//
//
//
//
//
//
//

        zero_out_item(context);

        SQLiteDatabase PosOutputDB = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

        String BankTransactionFinal= "delete from BankTransactionFinal";
        PosOutputDB.execSQL(BankTransactionFinal);

        String BankTransactionTemp= "delete from BankTransactionTemp";
        PosOutputDB.execSQL(BankTransactionTemp);

        String CashInOut= "delete from CashInOut";
        PosOutputDB.execSQL(CashInOut);

        String FinalReceipt= "delete from FinalReceipt";
        PosOutputDB.execSQL(FinalReceipt);

        String FinalTransactionReport= "delete from FinalTransactionReport";
        PosOutputDB.execSQL(FinalTransactionReport);

        String InvoiceMultiplePaymentFinal= "delete from InvoiceMultiplePaymentFinal";
        PosOutputDB.execSQL(InvoiceMultiplePaymentFinal);



        String InvoiceMultiplePaymentTemp= "delete from InvoiceMultiplePaymentTemp";
        PosOutputDB.execSQL(InvoiceMultiplePaymentTemp);

        String InvoiceReceiptItem= "delete from InvoiceReceiptItem";
        PosOutputDB.execSQL(InvoiceReceiptItem);

        String InvoiceReceiptItemFinal= "delete from InvoiceReceiptItemFinal";
        PosOutputDB.execSQL(InvoiceReceiptItemFinal);

        String InvoiceReceiptItemFinalWDiscount= "delete from InvoiceReceiptItemFinalWDiscount";
        PosOutputDB.execSQL(InvoiceReceiptItemFinalWDiscount);

        String InvoiceReceiptItemFinalWDiscountTemp= "delete from InvoiceReceiptItemFinalWDiscountTemp";
        PosOutputDB.execSQL(InvoiceReceiptItemFinalWDiscountTemp);

        String InvoiceReceiptTotal= "delete from InvoiceReceiptTotal";
        PosOutputDB.execSQL(InvoiceReceiptTotal);



        String InvoiceReference= "delete from InvoiceReference";
        PosOutputDB.execSQL(InvoiceReference);

        String OfficialReceipt= "delete from OfficialReceipt";
        PosOutputDB.execSQL(OfficialReceipt);

        String RefundTransaction= "delete from RefundTransaction";
        PosOutputDB.execSQL(RefundTransaction);

        String RemarkReceipt= "delete from RemarkReceipt";
        PosOutputDB.execSQL(RemarkReceipt);

        String ResetCounter= "delete from ResetCounter";
        PosOutputDB.execSQL(ResetCounter);

        String TerminalStatus= "delete from TerminalStatus";
        PosOutputDB.execSQL(TerminalStatus);

        String InvoiceReceiptItemSuspend= "delete from InvoiceReceiptItemSuspend";
        PosOutputDB.execSQL(InvoiceReceiptItemSuspend);

        String InvoiceReceiptTotalSuspend= "delete from InvoiceReceiptTotalSuspend";
        PosOutputDB.execSQL(InvoiceReceiptTotalSuspend);

        String InvoiceReceiptItemFinalWDiscountSuspend= "delete from InvoiceReceiptItemFinalWDiscountSuspend";
        PosOutputDB.execSQL(InvoiceReceiptItemFinalWDiscountSuspend);


        String InvoiceReturnExchangeTemp= "delete from ReturnExchangeTemp";
        PosOutputDB.execSQL(InvoiceReturnExchangeTemp);
        String InvoiceReturnExchangeFinal= "delete from ReturnExchangeFinal";
        PosOutputDB.execSQL(InvoiceReturnExchangeFinal);

        String CashBox= "delete from CashBox";
        PosOutputDB.execSQL(CashBox);


        String DiscountInfoTbl= "delete from DiscountInfoTbl";
        PosOutputDB.execSQL(DiscountInfoTbl);
        String ReceivingTbl= "delete from ReceivingTbl";
        PosOutputDB.execSQL(ReceivingTbl);




        SQLiteDatabase settings = context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);

//        String DiscountList= "delete from DiscountList";
//        settings.execSQL(DiscountList);

        String ReadingTable= "delete from ReadingTable";
        settings.execSQL(ReadingTable);

        String ReadingTableFinal= "delete from ReadingTableFinal";
        settings.execSQL(ReadingTableFinal);

        String ShiftingTable= "delete from ShiftingTable";
        settings.execSQL(ShiftingTable);

        String SystemDate= "delete from SystemDate";
        settings.execSQL(SystemDate);




        File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal");
        eJournal.delete();


        File dir = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal");
        if (dir.isDirectory())
        {
            if (dir.isDirectory()) {

                try {
                    FileUtils.deleteDirectory(dir);
                    Log.e("dir2","SUCCESS");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("dir2","ERROR");
                }
            }
        }


//        File dir2 = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/DATABASE");
//        if (dir2.isDirectory())
//        {
//            if (dir2.isDirectory()) {
//
//                try {
//                    FileUtils.deleteDirectory(dir2);
//                    Log.e("dir2","SUCCESS");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("dir2","ERROR");
//                }
//            }
//        }

        File dir3 = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Consolidate");
        if (dir3.isDirectory())
        {
            if (dir3.isDirectory()) {

                try {
                    FileUtils.deleteDirectory(dir3);
                    Log.e("dir2","SUCCESS");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("dir2","ERROR");
                }
            }
        }

        Backup_database backup_database = new Backup_database();


        File dirUSB = new File(backup_database.USBPath+"/ANDROID_POS/DATABASE");
        Log.d("USB PATH",dirUSB.toString());


    }

    public void zero_out_item(Context context){

        SQLiteDatabase db2 = context.openOrCreateDatabase("POSAndroidDB.db", android.content.Context.MODE_PRIVATE, null);
        admin_stock_card_update_inventory update_inventory = new admin_stock_card_update_inventory(db2);
        update_inventory.zero_out_item();


    }




}
