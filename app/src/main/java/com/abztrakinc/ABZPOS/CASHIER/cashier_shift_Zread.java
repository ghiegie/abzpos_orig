package com.abztrakinc.ABZPOS.CASHIER;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.abztrakinc.ABZPOS.ADMIN.Header_Footer_class;
import com.abztrakinc.ABZPOS.ADMIN.admin_email_send;
import com.abztrakinc.ABZPOS.ADMIN.admin_pos_settings;
import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.Backup_database;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.DateTime;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.system_final_date;

public class cashier_shift_Zread extends cashier_shift {
    SQLiteDatabase db2;

    String DB_NAME = "PosSettings.db";
    String DB_NAME2 = "PosOutputDB.db";
    Cursor cursorReceipt,cursorReceipt2,cursorReceipt3,cursorReceipt4;
    String invoiceTransactionID;
    String orderID;
    String posSupplier,supAddress,supVatRegTin,supAccreditNo,
            supDateIssuance,supEffectiveDate,supValidUntil,supPermitNo,
            supDateIssude,supValidUntil2,compName,compAddress,compVatregTin,
            compMin,footer;
    String HeaderContent;
    String printData;
    String OR="";
    String Trans="";
    String cashierName="admin 100";
    String posNumber="";
    String DateTimenow="";
    Date currentTime = Calendar.getInstance().getTime();
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    String transactionNumber;
    int cashFloatInIndicator=1;  //if 0 close if 1 open
    Double finalCashAmount;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    //OR_TRANS_ITEM or_trans_item;
    Thread workerThread;
    int printerSignal=0;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    DateTime dateTime = new DateTime();
    OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
    shift_active shift_active = new shift_active();
    cashier_reading_item cashier_reading_item = new cashier_reading_item();
    ArrayList<String>BankNameList=new ArrayList<String>();
    ArrayList<String>DiscountNameListSpecial=new ArrayList<String>();
    ArrayList<String>DiscountNameListNormal=new ArrayList<String>();
    ArrayList<String>TransactionWithDiscount=new ArrayList<String>();
    ArrayList<String>OtherPaymentList=new ArrayList<String>();
    DecimalFormat FinalDoubleFormat = new DecimalFormat("0.00");
    Double TotalSpecialDiscounts=0.00;
    Double TotalNormalDiscount=0.00;
    int activateCode=1;
    int activatePrinterCode=1;

    printer_settings_class PrinterSettings;



    public void printReport(Activity activity, Context context, String CashPickup){
        //Toast.makeText(context, "PRINTING Z READ", Toast.LENGTH_SHORT).show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

//                try{
                cashOutTotal=Double.parseDouble(CashPickup);
                readingEndBalance(context);
                PrinterSettings =new printer_settings_class(context);
                ReadFloatIN(context);
                ReadCash(context);
                ReadCashInMultiple(context);
                createTextfile(context);
                Log.e("emailSend","Test");
                admin_email_send emailSend = new admin_email_send();

                //off this to not send email after zread
                // emailSend.admin_email_send(activity,FinalDate);





                if (activateCode==1){




                    system_final_date sys = new system_final_date();
                    sys.insertDate(context);


                    SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
                    SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);

                    String updateStatus = "UPDATE ReadingIndicator set indicatorStatus=1 where indicatorID=1"; // ready for reading
                    db2.execSQL(updateStatus);

                    String updateShifting = "delete from ShiftingTable"; // change shift
                    db2.execSQL(updateShifting);

                    String updateTerminalStatus = "update TerminalStatus set StatusID=1,Status=0 where StatusID=1"; // change shift
                    db.execSQL(updateTerminalStatus);

                    Cursor sysdateCursor = db2.rawQuery("select * from SystemDate", null);
                    if (sysdateCursor.moveToNext()){
                        String updatePrevDate="update SystemDate set PrevBizDate='"+sysdateCursor.getString(1)+"',DateReadingIndicator='o' where ID='1'";
                        db2.execSQL(updatePrevDate);
                    }


//                    String deleteSystemDate= "delete from SystemDate";

//




                    Backup_database backup_database = new Backup_database();
                    backup_database.createFolderWithData(getContext(),sys.getSystemDate());



                }









              //  triggerRebirth(context);


                //updateReading(context);

                //unLockCashBox();

            }
        }).start();






        //Toast.makeText(context, "X READ", Toast.LENGTH_SHORT).show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                try {
//                    findBT(context);
//                    openBT(context);
//
//                    if (printerSignal==1){
//                        //createTextfile(getContext());
//
////                        completeTransaction();
////                        getTransactionRecord();
//
//
//                    }
//                    else{
//                        Log.e("ERROR: ","NO CONNECTION TO PRINTER");
//                        closeBT(context);
//                    }
//
//                } catch (IOException ex) {
//                    Log.e("ERROR: ","OPENING");
//                }
//            }
//        }).start();






    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }


    public void printData(Context context) throws UnsupportedEncodingException {

//        JmPrinter myprinter = new JmPrinter();//create an instance
//        byte[] SData = null;
//        SData = printData.getBytes("GB2312");//GB2312
//            myprinter.PrintText(SData);

//        boolean ret = false;
//        byte[] SData = null;
//        ret = myprinter.Open();
//
//        if (ret) {
//            Toast.makeText(context, "fourth", Toast.LENGTH_SHORT).show();
//            SData = printData.getBytes("GB2312");//GB2312
//            ret = myprinter.WriteBuf(SData, SData.length); //transmit data to printer
//            Toast.makeText(context, "fifth", Toast.LENGTH_SHORT).show();
//            if (!ret) {
//                Toast.makeText(context, "sixth", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, myprinter.GetLastPrintErr() ,Toast.LENGTH_LONG).show();
//                Log.e("PRINTER:",myprinter.GetLastPrintErr().toString());
//              //  Log.e()
//            }
//            else{
//                Toast.makeText(context, "7th", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, myprinter.GetLastPrintErr() ,Toast.LENGTH_LONG).show();
//            }
//            myprinter.Close();//close the printer
//        }
//        myprinter = null ; //release the object

    }

    double creditCardAmount=0.00;
    int creditCardQty;
    double creditCardAmountPerBank=0.00;

    //for vat exempt
    Double totalVatExempt=0.00;
    Double zeroRatedSales=0.00;
    Double serviceCharge=0.00;

    Double lessVat=0.00;
    Double TotalAmountOfDiscount=0.00;

    //for gross sales  grosssales = discAmount+orderpriceTotal
    Double DiscAmountGrossSales=0.00;
    Double OrderPriceTotalGrossSales=0.00;
    Double grossSales=0.00;
    double totalRefund=0.00;
    int totalRefundCounter;

    ArrayList<String>DiscountNameList=new ArrayList<String>();



//    double creditCardAmount=0.00;
//    int creditCardQty;


    double debitCardAmount=0.00;
    int debitCardQty;

    int otherPaymentQty;
    double otherPaymentAmount;

    double unclaimAmount = 0.00;
    int unclaimQty;

    private String convertdate(String finalDate){
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            String inputDateStr = finalDate;
            Date date = inputFormat.parse(inputDateStr);
            String outputDateStr = outputFormat.format(date);
            return outputDateStr;
        }
        catch (Exception ex){
            Log.e("convertdate","ERROR");
        }
        return finalDate;
    }


    private void createTextfile(Context context){
        Double resetCtrDec;
        int resetCtrInt;
        Double totalBal=0.00;
        Double end=9999999999.99;
        String StartBalance;
        String StartBalanceCtr;
        String EndBalance;
        String EndBalanceCtr;


        dateTime.generateDateTime();
        or_trans_item.readReferenceNumber(context.getApplicationContext());
        transactionNumber= or_trans_item.getTransactionNo();
        shift_active.getAccountInfo(context.getApplicationContext());
        cashier_reading_item.getReadingDataZ(context.getApplicationContext());
        cashier_reading_item.getLastOR(context.getApplicationContext());
       // dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        ReadVatableSales(context.getApplicationContext());
        DecimalFormat form = new DecimalFormat("0.00");
        cashier_reading_item.resetCounter(context.getApplicationContext());
        cashier_reading_item.ZCounter(context.getApplicationContext());
        ReadGrossSales(context.getApplicationContext());
        ReadRefund(context.getApplicationContext());
        getEndStartOR(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        HeaderFooterClass.HeaderNote(context.getApplicationContext());
        HeaderFooterClass.FooterNote(context.getApplicationContext());






        resetCtrDec =((Double.parseDouble(cashier_reading_item.getBegBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);
        totalBal = Double.parseDouble(cashier_reading_item.getBegBal())-(resetCtrInt*end) ;

        StartBalanceCtr=String.format("%02d", resetCtrInt);
        StartBalance=String.format("%016.2f", totalBal);


        resetCtrDec =((Double.parseDouble(cashier_reading_item.getEndBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);
        // resetCtrInt = Integer.parseInt(String.valueOf(resetCtrDec));
        totalBal = Double.parseDouble(cashier_reading_item.getEndBal())-(resetCtrInt*end) ;

        EndBalanceCtr=String.format("%02d", resetCtrInt);
        EndBalance=String.format("%016.2f", totalBal);



        int modx=Integer.parseInt(or_trans_item.getTransactionNo());
        Log.e("modx",String.valueOf(modx));
        int mody=999999999;
        int resetCount = modx/mody;
        Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtr = String.format("%02d", resetCount);
        String formattedTrans =  String.valueOf(modx % mody);
        transactionNumber=formattedTrans;
        transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));



        DatabaseHandler databaseHandler=new DatabaseHandler(context);
        databaseHandler.insertInvoiceReceiptTotal(or_trans_item.getTransactionNo(),"","",""
                ,"","","","","ZREAD",FinalDate,timeOnly.format(currentDate.getTime()),shift_active.getActiveUserID(),shift_active.getShiftActive());

        StringBuffer buffer = new StringBuffer();

        buffer.append(HeaderFooterClass.getHeaderText() + "\n");
//            buffer.append("SHF: " + "\t\t" + "POS: " + "\n");

        buffer.append("================================" + "\n");
        buffer.append("           Z_READING            " + "\n");
      //  buffer.append("         TENDER REPORT          " + "\n");
        buffer.append("================================" + "\n");
        buffer.append( "TRANS#: "+formattedCtr+"-" +transactionNumber+ "\n");
        buffer.append("Business Date: " + convertdate(FinalDate) + "\n");
        buffer.append("Report ID: " + shift_active.getActiveUserID()+"\n");
        buffer.append("POS: " + shift_active.getPOSCounter() + "     " +"Shift: "+ "ALL" +"\n");
        buffer.append("CASHIER: " + shift_active.getActiveUserName() + "\n\n\n");








        buffer.append("Z COUNTER: ("+cashier_reading_item.getZcounter()+")"+"\n");



        int modxFinalBegOR=Integer.parseInt(startORZ);
        // Log.e("modx",String.valueOf(modx));
        int modyFinalBegOR=999999999;
        int resetCountFinalBegOR = modxFinalBegOR/modyFinalBegOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalBegOR = String.format("%02d", resetCountFinalBegOR);
        String formattedFinalBegOR =  String.valueOf(modxFinalBegOR % modyFinalBegOR);
        String FinalBegOR=formattedFinalBegOR;
        FinalBegOR =String.format("%010d",Integer.parseInt(FinalBegOR));


        int modxFinalEndOR=Integer.parseInt(endORZ);
        // Log.e("modx",String.valueOf(modx));
        int modyFinalEndOR=999999999;
        int resetCountFinalEndOR = modxFinalEndOR/modyFinalEndOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalEndOR = String.format("%02d", resetCountFinalEndOR);
        String formattedFinalEndOR =  String.valueOf(modxFinalEndOR % modyFinalEndOR);
        String FinalEndOR=formattedFinalEndOR;
        FinalEndOR =String.format("%010d",Integer.parseInt(FinalEndOR));



//        buffer.append("Beginning Invoice No:"  +startORZ+"\n");
//        buffer.append("Ending Invoice NO:"  +endORZ+"\n");

        buffer.append("Beginning Invoice No.: \n"+
                ("                    " +FinalBegOR+"      ").substring(0,32) +"\n");
        buffer.append("Ending Invoice No.: \n"+
                ("                    " +FinalEndOR+"      ").substring(0,32) +"\n");
        buffer.append("SI RESET COUNTER: \n"+
                ("                              " +formattedCtrFinalEndOR+"      ").substring(0,32) +"\n");

        buffer.append("Beginning Balance \n"+
                ("                "  +StartBalance+"      ").substring(0,32)+"\n");
        buffer.append("Ending Balance \n" +
                ("                " + EndBalance+"      ").substring(0,32)+"\n\n");



//        buffer.append("Beginning Balance ("+StartBalanceCtr+") "  +StartBalance+"\n");
//        buffer.append("Ending Balance (" +EndBalanceCtr+") " + EndBalance+"\n\n\n");
        buffer.append("================================" + "\n");
        buffer.append("         TENDER REPORT          " + "\n");
        buffer.append("================================" + "\n");
        buffer.append("TENDER                 AMOUNT   "+"\n");
        buffer.append("--------------------------------" + "\n");

        if (cashFloatInIndicator==1) {
            buffer.append("CASH FLOAT      "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashFloatQty()))+"      ").substring(0,6) + ("   " + String.format("%7.2f",cashInTotal) + "           ").substring(0, 10) + "\n");
        }
            buffer.append("CASH            "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"      ").substring(0,6)+("   " + String.format("%7.2f",finalCashAmount)+"           ").substring(0,10) + "\n");


        if (cashFloatInIndicator==1) {
            buffer.append("CASH PICKUP     "+(String.format("%6d",1)+"       -").substring(0,6) + ("   " + String.format("%7.2f",cashOutTotal*-1) + "           ").substring(0, 10) + "\n");
        }


        SQLiteDatabase RefundDb= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //  Cursor readRefundCtr =RefundDb.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransactionID='"+TransactionIDList.get(x)+"'", null);


        String startTranss =TransactionIDList.get(0);
        String endTranss=TransactionIDList.get(TransactionIDList.size()-1);
        Log.e("readrefund",String.valueOf(totalRefund));


        if (totalRefund!=0.00) {
            buffer.append("CASH REFUND     " + (String.format("%6d", Integer.parseInt(String.valueOf(totalRefundCounter))) + "        ").substring(0, 6) + ("   " + String.format("%7.2f", Double.parseDouble(String.valueOf(totalRefund))) + "           ").substring(0, 10) + "\n");

        }

        // ReadRefund();






//        buffer.append("--------------------------------" + "\n");
        buffer.append("TOTAL: CASH     "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"        ").substring(0,6) + ("   " + String.format("%7.2f",finalCashAmount)+"           ").substring(0,10)  + "\n");
        buffer.append("================================" + "\n");


        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor readBankDetails =db.rawQuery("select * from BankDetails", null);
        Cursor readOtherPayment =db2.rawQuery("select * from OtherPayment", null);
        Cursor readDiscountName =db.rawQuery("select DISTINCT DiscountType from InvoiceReceiptItemFinalWDiscount", null);




//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor readBankDetails =db.rawQuery("select * from BankDetails", null);
//        Cursor readDiscountName =db.rawQuery("select DISTINCT DiscountType from InvoiceReceiptItemFinalWDiscount", null);



        //   Cursor invoiceReceipItemFinal =db.rawQuery("select sum(DiscAmount),sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate='" + dateTime2 + "'", null);




        if (readBankDetails.getCount()!=0){
            while (readBankDetails.moveToNext()) {
                BankNameList.add(readBankDetails.getString(1));
            }
        }
        if (readDiscountName.getCount()!=0){
            while(readDiscountName.moveToNext()){
                DiscountNameList.add(readDiscountName.getString(0));
                Log.e("Discount Name",readDiscountName.getString(0));


            }




            for (int ctr=0;ctr<TransactionIDList.size();ctr++){
                Cursor InvoiceReceiptItemFinalWDiscount =db.rawQuery("select DiscountType,DiscAmount from InvoiceReceiptItemFinalWDiscount where TransactionID='"+TransactionIDList.get(ctr)+"'", null);
                if (InvoiceReceiptItemFinalWDiscount.getCount()!=0){
                    while (InvoiceReceiptItemFinalWDiscount.moveToNext()){

                        SQLiteDatabase DiscountList= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
                        Cursor DiscountType =DiscountList.rawQuery("select DiscountType from DiscountList where DiscountID='"+InvoiceReceiptItemFinalWDiscount.getString(0)+"'", null);
                        if (DiscountType.getCount()!=0){
                            while (DiscountType.moveToNext()){
                                if (DiscountType.getString(0).trim().equals("SPECIAL DISCOUNT")){
                                    TotalSpecialDiscounts+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
                                }
                                if (DiscountType.getString(0).trim().equals("NORMAL DISCOUNT")){
                                    TotalNormalDiscount+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
                                }
                            }
                        }

                        DiscountList.close();



                    }
                }



            }








        }



        if (readOtherPayment.getCount()!=0){
            while (readOtherPayment.moveToNext()){
                OtherPaymentList.add(readOtherPayment.getString(1));
            }
        }
//        if (BankNameList.size()!=0) {
//            for (int i = 0; i < BankNameList.size(); i++) {
//                Cursor readBankTransactionTemp = db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='" + BankNameList.get(i).toString() + "'and TransDate='" + FinalDate + "'and TransUser='" + shift_active.getActiveUserID() + "'", null);
//                Cursor readBankTransactionTempTotal = db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='" + BankNameList.get(i).toString() + "'and TransDate='" + FinalDate + "'and TransUser='" + shift_active.getActiveUserID() + "'", null);
//                if (readBankTransactionTemp.getCount() !=0) {
//
//                    readBankTransactionTemp.moveToNext();
//
//                    readBankTransactionTempTotal.moveToNext();
//
//                    if (readBankTransactionTemp.getString(0)!=null){ //total per bank
//                        buffer.append((BankNameList.get(i) + "                           ").substring(0, 27) + (readBankTransactionTempTotal.getString(0) + "        ").substring(0, 8) + (String.format("%7.2f", Double.parseDouble(readBankTransactionTemp.getString(0))) + "           ").substring(0, 11) + "\n");
//                        creditCardAmountPerBank=Double.parseDouble(readBankTransactionTemp.getString(0));
//                    }
//                    else{
//                        creditCardAmountPerBank=0.00;
//                    }
//                    creditCardAmount+=creditCardAmountPerBank;
//                    creditCardQty+=Integer.parseInt(readBankTransactionTempTotal.getString(0));
//                }
//            }
//            buffer.append("TOTAL:CARD      "+(String.format("%6d",creditCardQty)+"          ").substring(0,6) + (String.format("%7.2f",Double.parseDouble(String.valueOf(creditCardAmount)))+"           ").substring(0,10)  + "\n");
//        }

        buffer.append("\n");
        for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            Double FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='CreditCard' and TransDate='"+FinalDate+"'", null);
            if (invoiceReceiptTotalCheck.getCount()!=0) {
                while(invoiceReceiptTotalCheck.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheck.getString(0);
                    String TransactionAmount=invoiceReceiptTotalCheck.getString(4);
                    Cursor BankTransactionFinalCheck=db.rawQuery("select * from BankTransactionFinal where CardType='CreditCard' and TransNum='"+TransactionID+"' and BankName='"+BankNameList.get(x)+"'", null);
                    if (BankTransactionFinalCheck.getCount()!=0) {
                        while (BankTransactionFinalCheck.moveToNext()) {
                            Ctr++;
                            FinalBankAmount+=Double.parseDouble(TransactionAmount);

                        }
                    }
//
                }

            }
            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);

                    Cursor BankTransactionFinalCheck=db.rawQuery("select * from InvoiceMultiplePaymentFinal where BankType='CreditCard' and TransactionID='"+TransactionID+"' and TypeOfPayment='"+BankNameList.get(x)+"'", null);
                    if (BankTransactionFinalCheck.getCount()!=0) {
                        while (BankTransactionFinalCheck.moveToNext()) {
                            Ctr++;
                            FinalBankAmount+=Double.parseDouble(BankTransactionFinalCheck.getString(3));

                        }
                    }
//
                }

            }
            if (FinalBankAmount.equals(0.00)){

            }
            else{

                buffer.append((FinalBankName+"               ").substring(0,16) +(String.format("%6d",Ctr)+"        ").substring(0,6) + ("   " + String.format("%7.2f",FinalBankAmount)+"           ").substring(0,10)  + "\n");
                creditCardQty+=Ctr;
                creditCardAmount+=FinalBankAmount;
                //  buffer.append(((FinalBankName+"                      ").substring(0,16) + (String.valueOf(Ctr)+"      ").substring(0,6) + (("   "+String.format("%7.2f",(FinalBankAmount)))+"           ").substring(0,11)) +"\n");

            }




        }
        buffer.append("TOTAL:CREDITCARD"+((String.format("%6d",creditCardQty))+"        ").substring(0,6) + ("   " + String.format("%7.2f",creditCardAmount)+"           ").substring(0,10)  + "\n");
        buffer.append("================================" + "\n");
        for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            Double FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='DebitCard' and TransDate='"+FinalDate+"'", null);
            if (invoiceReceiptTotalCheck.getCount()!=0) {
                while(invoiceReceiptTotalCheck.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheck.getString(0);
                    String TransactionAmount=invoiceReceiptTotalCheck.getString(4);
                    Cursor BankTransactionFinalCheck=db.rawQuery("select * from BankTransactionFinal where CardType='DebitCard' and TransNum='"+TransactionID+"' and BankName='"+BankNameList.get(x)+"'", null);
                    if (BankTransactionFinalCheck.getCount()!=0) {
                        while (BankTransactionFinalCheck.moveToNext()) {
                            Ctr++;
                            FinalBankAmount+=Double.parseDouble(TransactionAmount);

                        }
                    }
//
                }

            }
            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                    //String TransactionAmount=invoiceReceiptTotalCheckM.getString(4);
                    Cursor BankTransactionFinalCheck=db.rawQuery("select * from InvoiceMultiplePaymentFinal where BankType='DebitCard' and TransactionID='"+TransactionID+"' and TypeOfPayment='"+BankNameList.get(x)+"'", null);
                    if (BankTransactionFinalCheck.getCount()!=0) {
                        while (BankTransactionFinalCheck.moveToNext()) {
                            Ctr++;
                            FinalBankAmount+=Double.parseDouble(BankTransactionFinalCheck.getString(3));

                        }
                    }
//
                }

            }
            if (FinalBankAmount.equals(0.00)){

            }
            else{

                buffer.append((FinalBankName+"               ").substring(0,16) +(String.format("%6d",Ctr)+"        ").substring(0,6) + ("   " + String.format("%7.2f",FinalBankAmount)+"           ").substring(0,10)  + "\n");
                debitCardQty+=Ctr;
                debitCardAmount+=FinalBankAmount;
                //  buffer.append(((FinalBankName+"                      ").substring(0,16) + (String.valueOf(Ctr)+"      ").substring(0,6) + (("   "+String.format("%7.2f",(FinalBankAmount)))+"           ").substring(0,11)) +"\n");

            }





        }
        buffer.append("TOTAL:DEBIT CARD"+((String.format("%6d",debitCardQty))+"        ").substring(0,6) + ("   " + String.format("%7.2f",debitCardAmount)+"           ").substring(0,10)  + "\n");


        buffer.append("================================" + "\n");

        for (int x=0;x<OtherPaymentList.size();x++){
            int Ctr=0;
            String FinalOtherPayment=OtherPaymentList.get(x);
            Log.e("OTHER PAYMENT",FinalOtherPayment);
            Double FinalOtherPaymentAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='"+FinalOtherPayment.trim()+"' and TransDate='"+FinalDate+"'", null);
            if (invoiceReceiptTotalCheck.getCount()!=0) {
                Log.e("OTHER PAYMENT","NOT ZERO");
                while(invoiceReceiptTotalCheck.moveToNext()){
                    Log.e("OTHER PAYMENT while","NOT ZERO");
                    String TransactionID=invoiceReceiptTotalCheck.getString(0);

                    Cursor OtherPaymentFinalCheck=db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='"+FinalOtherPayment+"' and TransactionID='"+TransactionID+"'", null);
                    if (OtherPaymentFinalCheck.getCount()!=0) {

                        while (OtherPaymentFinalCheck.moveToNext()) {
                            Log.e("OTHER PAYMENT while2","NOT ZERO");
                            Log.e("OTHER TRANS ID",TransactionID);
                            String TransactionAmount=OtherPaymentFinalCheck.getString(4);

                            Ctr++;
                            FinalOtherPaymentAmount+=Double.parseDouble(TransactionAmount);
                            Log.e("OTHER PAYMENT TOTAL",String.valueOf(FinalOtherPaymentAmount));

                        }
                    }
//
                }

            }

            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                Log.e("OTHER PAYMENT","NOT ZERO");
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    Log.e("OTHER PAYMENT while","NOT ZERO");
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);

                    Cursor OtherPaymentFinalCheck=db.rawQuery("select * from InvoiceMultiplePaymentFinal where typeOfPayment='"+FinalOtherPayment+"' and TransactionID='"+TransactionID+"'", null);
                    if (OtherPaymentFinalCheck.getCount()!=0) {

                        while (OtherPaymentFinalCheck.moveToNext()) {
                            Log.e("OTHER PAYMENT while2","NOT ZERO");
                            Log.e("OTHER TRANS ID",TransactionID);
                            String TransactionAmount=OtherPaymentFinalCheck.getString(3);

                            Ctr++;
                            FinalOtherPaymentAmount+=Double.parseDouble(TransactionAmount);
                            Log.e("OTHER PAYMENT TOTAL",String.valueOf(FinalOtherPaymentAmount));

                        }
                    }
//
                }

            }




            if (FinalOtherPaymentAmount.equals(0.00)){
                Log.e("OTHER PAYMENT","ZERO");
            }
            else{
                Log.e("OTHER PAYMENT","ELSE");
                buffer.append((FinalOtherPayment+"               ").substring(0,16) +(String.format("%6d",Ctr)+"        ").substring(0,6) + ("   " + String.format("%7.2f",FinalOtherPaymentAmount)+"           ").substring(0,10)  + "\n");
                otherPaymentQty+=Ctr;
                otherPaymentAmount+=FinalOtherPaymentAmount;
                //  buffer.append(((FinalBankName+"                      ").substring(0,16) + (String.valueOf(Ctr)+"      ").substring(0,6) + (("   "+String.format("%7.2f",(FinalBankAmount)))+"           ").substring(0,11)) +"\n");

            }


        }

        buffer.append("TOTAL: OTHER    "+((String.format("%6d",otherPaymentQty))+"        ").substring(0,6) + ("   " + String.format("%7.2f",otherPaymentAmount)+"           ").substring(0,10)  + "\n");










        buffer.append("================================" + "\n\n");
        buffer.append("TOTAL COLLECTED "+(String.format("%6d",((creditCardQty+(Integer.parseInt(cashier_reading_item.getCashQty()))+totalRefundCounter+otherPaymentQty+debitCardQty)))+"          ").substring(0,6) + ("   " + String.format("%7.2f",Double.parseDouble(String.valueOf(creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+debitCardAmount)))+"           ").substring(0,10)  + "\n\n");
        Cursor checkUnclaimAmount = db.rawQuery("select sum(TotalSalesOverrun) from FinalTransactionReport where ORTrans between '"+FinalBegOR+"' and '"+FinalEndOR+"'", null);
        Cursor checkUnclaimQty = db.rawQuery("select count(TotalSalesOverrun) from FinalTransactionReport where ORTrans between '"+FinalBegOR+"' and '"+FinalEndOR+"'", null);

        if (checkUnclaimAmount.getCount()!=0){
            if (checkUnclaimAmount.moveToFirst()){
                checkUnclaimQty.moveToFirst();
                unclaimAmount = checkUnclaimAmount.getDouble(0);
                unclaimQty = checkUnclaimQty.getInt(0);
                if (unclaimAmount==0.00){

                }
                else{
                    buffer.append("UNCLAIMED AMOUNT"+(String.format("%6d",unclaimQty)+"          ").substring(0,6) + ("   " + String.format("%7.2f",Double.parseDouble(String.valueOf(unclaimAmount)))+"           ").substring(0,10)  + "\n\n");

                }

            }

        }


        buffer.append("BREAKDOWN OF SALES:    " + "\n");

vat_indicator vatIndicator = new vat_indicator();
        if (vatIndicator.getVatIndicator()==0){
            buffer.append("NVATables Sales       " + ("   "+String.format("%7.2f",totalVatablesSales)+"           ").substring(0,10)  + "\n");
            buffer.append("NVAT Amount           " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");

            buffer.append("NVAT Exempt Sales     " + ("   "+String.format("%7.2f",totalVatExempt)+"           ").substring(0,10)  + "\n");
        }
        else{
            buffer.append("VATables Sales        " + ("   "+String.format("%7.2f",totalVatablesSales)+"           ").substring(0,10)  + "\n");
            buffer.append("VAT Amount            " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");

            buffer.append("VAT Exempt Sales      " + ("   "+String.format("%7.2f",totalVatExempt)+"           ").substring(0,10)  + "\n");
        }




//
//        buffer.append("VATables Sales        " + (String.format("%7.2f",totalVatablesSales)+"           ").substring(0,10)  + "\n");
//        buffer.append("VAT Amount            " + (String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
//
//        buffer.append("VAT Exempt Sales      " + (String.format("%7.2f",totalVatExempt)+"           ").substring(0,10)  + "\n");
        buffer.append("Zero-Rated Sales         " + (String.format("%7.2f",zeroRatedSales)+"           ").substring(0,10)  + "\n");
        buffer.append("--------------------------------" + "\n");
        buffer.append("              P L U             " + "\n");
        buffer.append("--------------------------------" + "\n");


        //showing of PLU in item


        ArrayList<String>OrderNameList=new ArrayList<String>();
        ArrayList<Integer>OrderNameListQty=new ArrayList<>();

        SQLiteDatabase PosOutputDB= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        for(int pluctr=0;pluctr<TransactionIDList.size();pluctr++){
            Cursor readPLUItems =PosOutputDB.rawQuery("select DISTINCT OrderName,OrderQty  from InvoiceReceiptItemFinal where TransactionID='"+TransactionIDList.get(pluctr)+"'", null);
            //Cursor readPLUItemsQty =PosOutputDB.rawQuery("select DISTINCT OrderName  from InvoiceReceiptItemFinal where TransactionID='"+TransactionIDList.get(pluctr)+"'", null);
            if (readPLUItems.getCount()!=0){

                int itemQty=0;
                while (readPLUItems.moveToNext()){

                    if (!OrderNameList.contains(readPLUItems.getString(0))){
                        OrderNameList.add(readPLUItems.getString(0));
//                        itemQty+=Integer.parseInt(readPLUItems.getString(1));

                    }
                    else{
//                        itemQty+=Integer.parseInt(readPLUItems.getString(1));

                    }


                }
                //  OrderNameListQty.add(itemQty);





                //   Log.e("ORDER NAME ARRAY",OrderNameList.toString());
            }








//            if (readPLUItems.getCount()!=0){
//                while (readPLUItems.moveToNext()){
//                     Cursor readPLUFinal =PosOutputDB.rawQuery("select DISTINCT OrderName from InvoiceReceiptItemFinal where OrderName='"+readPLUItems.getString(0)+"'", null);
//                        if (readPLUFinal.getCount()!=0){
//                            while (readPLUFinal.moveToNext()){
//                                Cursor readPLUCounter =PosOutputDB.rawQuery("select * from InvoiceReceiptItemFinal where OrderName='"+readPLUFinal.getString(0)+"'", null);
//                                int pluCtr=0;
//                                if (readPLUCounter.getCount()!=0){
//
//                                    buffer.append((readPLUFinal.getString(0)+"                                   ").substring(0,35)+ (String.valueOf(readPLUCounter.getCount())+"           ").substring(0,11)   + "\n");
//                                }
//                            }
//
//                        }
//
//                   // buffer.append((readPLUItems.getString(0)+"                                   ").substring(0,35)  + "\n");
//
//                }
//
        }


        for(int pluctr2=0;pluctr2<OrderNameList.size();pluctr2++){
            Cursor readPLUItems =PosOutputDB.rawQuery("select sum(OrderQty),sum(OrderPriceTotal)  from InvoiceReceiptItemFinal where OrderName='"+OrderNameList.get(pluctr2)+"'and TransactionID between'"+TransactionIDList.get(0)+"'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
            //Cursor readPLUItemsQty =PosOutputDB.rawQuery("select DISTINCT OrderName  from InvoiceReceiptItemFinal where TransactionID='"+TransactionIDList.get(pluctr)+"'", null);
            if (readPLUItems.getCount()!=0){
                while (readPLUItems.moveToNext()) {
                    // buffer.append("TOTAL: CASH              "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"        ").substring(0,10) + (String.format("%7.2f",finalCashAmount)+"           ").substring(0,11)  + "\n");


                    buffer.append((OrderNameList.get(pluctr2) + "                                ").substring(0, 32));
                    if (OrderNameList.get(pluctr2).length()>31){
                        buffer.append((OrderNameList.get(pluctr2) + "                                ").substring(32, 64));
                    }
                    try {
                        buffer.append((("                " + String.format("%6d", Integer.parseInt(readPLUItems.getString(0)))).substring(0, 22) + ("   " + String.format("%7.2f", Double.parseDouble(readPLUItems.getString(1))) + "           ").substring(0, 10) + "\n"));
                    }
                    catch (Exception ex){
                        Log.e("Exception",ex.getMessage());
                    }
                }
            }








//            if (readPLUItems.getCount()!=0){
//                while (readPLUItems.moveToNext()){
//                     Cursor readPLUFinal =PosOutputDB.rawQuery("select DISTINCT OrderName from InvoiceReceiptItemFinal where OrderName='"+readPLUItems.getString(0)+"'", null);
//                        if (readPLUFinal.getCount()!=0){
//                            while (readPLUFinal.moveToNext()){
//                                Cursor readPLUCounter =PosOutputDB.rawQuery("select * from InvoiceReceiptItemFinal where OrderName='"+readPLUFinal.getString(0)+"'", null);
//                                int pluCtr=0;
//                                if (readPLUCounter.getCount()!=0){
//
//                                    buffer.append((readPLUFinal.getString(0)+"                                   ").substring(0,35)+ (String.valueOf(readPLUCounter.getCount())+"           ").substring(0,11)   + "\n");
//                                }
//                            }
//
//                        }
//
//                   // buffer.append((readPLUItems.getString(0)+"                                   ").substring(0,35)  + "\n");
//
//                }
//
        }



//        Log.e("ORDER NAME ARRAY",OrderNameList.toString());
//        Log.e("ORDER NAME QTY",OrderNameListQty.toString());
//        for(int orderNameListCtr=0;orderNameListCtr<OrderNameList.size();orderNameListCtr++){
//            buffer.append((OrderNameList.get(orderNameListCtr)+"                                   ").substring(0,35)+("           ").substring(0,11)   + "\n");
//        }



        PosOutputDB.close();

        //end of showing PLU in item

        buffer.append("--------------------------------" + "\n\n");
//        buffer.append("         DISCOUNT LIST          "+"\n");
//        buffer.append("--------------------------------" + "\n\n");
//        for(int x=0;x<DiscountNameList.size();x++){
//            buffer.append(DiscountNameList.get(x));
//        }
//        buffer.append("--------------------------------" + "\n\n");



        //end of showing PLU in item

//        buffer.append("--------------------------------" + "\n\n");
//
//
//
//        buffer.append("Service Charges                    " + (String.format("%7.2f",serviceCharge)+"           ").substring(0,11)  + "\n\n");
//        buffer.append("--------------------------------" + "\n");
        buffer.append("GROSS SALES           " + (String.format("%7.2f",grossSales)+"           ").substring(0,10)  + "\n");
        buffer.append("Less VAT              " + (String.format("%7.2f",lessVat)+"           ").substring(0,10)  + "\n");


        buffer.append(("SPECIAL DISCOUNT"+"                                   ").substring(0,22) + (String.format("%7.2f",TotalSpecialDiscounts)+"           ").substring(0,10)  + "\n");
        buffer.append(("NORMAL DISCOUNT"+"                                   ").substring(0,22) + (String.format("%7.2f",TotalNormalDiscount)+"           ").substring(0,10)  + "\n");




        if (vatIndicator.getVatIndicator()==0){

            buffer.append("NVAT Amount           " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }
        else{

            buffer.append("VAT Amount            " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }



       // buffer.append("VAT Amount            " + (String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        buffer.append("--------------------------------" + "\n");

        totalNetSales=grossSales-lessVat-TotalSpecialDiscounts-TotalNormalDiscount-totalTaxAmount;
        // totalNetSales=grossSales-TotalSpecialDiscount-TotalNormalDiscount;


        buffer.append("NET SALES             " + (String.format("%7.2f",totalNetSales)+"           ").substring(0,10)  + "\n\n");
        buffer.append("RESET COUNTER : (" + EndBalanceCtr + ")\n");

        system_final_date systemDate= new system_final_date();
        SQLiteDatabase TransactionNumber= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+FinalDate+"'or TransDate='"+systemDate.getSystemDate2()+"'", null);
        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+FinalDate+"'or TransDate='"+systemDate.getSystemDate2()+"'", null);

        if (StartTrans.getCount()!=0){
            StartTrans.moveToFirst();

            buffer.append("START TRANSACTION     " + (String.format(String.valueOf(Integer.parseInt(StartTrans.getString(0)))) + "           ").substring(0, 10) + "\n");
            EndTrans.moveToLast();

            buffer.append("END TRANSACTION       " + (String.format(String.valueOf(Integer.parseInt(EndTrans.getString(0))) )+ "           ").substring(0, 10) + "\n");
            // buffer.append("END TRANSACTION                    " + (Integer.parseInt(transactionNumber) + "           ").substring(0, 11) + "\n");

        }
        else{
            buffer.append("START TRANSACTION         ERROR " +"\n");
            EndTrans.moveToLast();

            buffer.append("END TRANSACTION           ERROR" + "\n");

        }
        TransactionNumber.close();

//        buffer.append("Accumulated Grand Total:"+ FinalDoubleFormat.format(Double.valueOf(cashier_reading_item.getEndBal()))+ "\n");
//        buffer.append("--------------------------------" + "\n");

        buffer.append(("Accumulated Grand Total: "+"              ").substring(0,25)+"\n"+
                ("                " + EndBalance+"      ").substring(0,32)+"\n\n");

        buffer.append(HeaderFooterClass.getFooterText());








        try{
//                mmOutputStream.write(buffer.toString().getBytes( "US-ASCII")); // for printing receipt
//                mmOutputStream.flush();
//                mmOutputStream.close();
//                closeBT(getContext());

            // printData=buffer.toString();




            if (activatePrinterCode==1){
                printData=buffer.toString();

                //JMPrinter(printData); // for jolimark
                PrinterSettings.OnlinePrinter(printData,1,0,1);






                create_journal_entry createJournal = new create_journal_entry();
                createJournal.setPrintData(printData);
                createJournal.setTransNumber(or_trans_item.getTransactionNo());
              //  createJournal.sendEmail(getActivity(),FinalDate);
                createJournal.journalEntry(createJournal.getPrintData(),createJournal.getTransNumber(),FinalDate);















            }











//
//                JmPrinter myprinter = new JmPrinter();//create an instance
//                byte[] SData = null;
//                SData = printData.getBytes("GB2312");//GB2312
//                myprinter.PrintText(SData);
//              //  Toast.makeText(getContext(), "PRINTING", Toast.LENGTH_SHORT).show();
//                unLockCashBox();


        }
        catch (Exception ex){


            Log.e("Error",ex.getMessage());
        }

//
//
//
//        } catch (IOException ex) {
//            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
//        }




    }
    String endOR;
    String endBal;
    String endTrans;
    Date currentDate = Calendar.getInstance().getTime();
 //   SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
    //Simple dateOnly = new SimpleDateFormat("MM/dd/yyyy");

    //DateFormat timeOnly = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



    String startORZ;
    String endORZ;

    ArrayList<String>ORNumberList=new ArrayList<>();
    private void getEndStartOR(Context context){
        TransactionIDList.clear();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
FinalDate=SysDate.getSystemDate();
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor invoiceReceiptTotal =db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='" + FinalDate + "'", null);
        //   Cursor invoiceReceiptTotal = db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='01-01-1970' and TransUser='001'and shiftActive='5'",null);

        if (invoiceReceiptTotal.getCount()!=0){
            while (invoiceReceiptTotal.moveToNext()){
                Log.e("TRANSACTIONID",invoiceReceiptTotal.getString(0));
                TransactionIDList.add(invoiceReceiptTotal.getString(0));
            }
           // for (int x=0;x<TransactionIDList.size();x++){

                Cursor getStartORZ =db.rawQuery("select * from officialReceipt where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
                Cursor getEndORZ =db.rawQuery("select * from officialReceipt where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);




            if (getStartORZ.getCount()!=0){
                while (getStartORZ.moveToNext()){

                    ORNumberList.add(getStartORZ.getString(0));
                }
                startORZ=ORNumberList.get(0);
                endORZ=ORNumberList.get(ORNumberList.size()-1);
            }
            else{
                startORZ="0";
                endORZ="0";
            }




        }
        db.close();

    }

    private void ReadRefund(Context context){
        // ReadGrossSales(context);
        SQLiteDatabase RefundDb= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor readRefund =RefundDb.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='refund' and TransactionID between'"+TransactionIDList.get(0)+"'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
        //  totalRefundCounter=readRefund.getCount();
        if (readRefund.getCount()!=0){
            readRefund.moveToNext();



            if (readRefund.getString(0)==null){
                totalRefund+=0.00;
                //  totalRefundCounter=readRefund.getCount();
            }
            else{
                totalRefund+=Double.parseDouble(readRefund.getString(0));
                totalRefundCounter=readRefund.getCount();

            }




        }

    }
    private void readingEndBalance(Context context){

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadingBalance =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice'", null);
        if (cursorReadingBalance.getCount()!=0) {
//            try {
            while (cursorReadingBalance.moveToNext()) {
                if(cursorReadingBalance.getString(0)==null){
                    String data2="0";
                    double dataInt2 = Double.parseDouble(data2);

                    //endBal=String.format("%013.2f", dataInt2);
                    endBal =String.valueOf(dataInt2);


                }
                else{
                    double dataInt = Double.parseDouble(cursorReadingBalance.getString(0));
                    //endBal = String.format("%013.2f", dataInt);
                    endBal =String.valueOf(cursorReadingBalance.getFloat(0));
                    Log.e("readingBalance","endBal= " + (cursorReadingBalance.getFloat(0)));

                }
                cashier_reading_item.setEndBal(endBal);
            }
//            }catch (Exception ex){
//                endBal = String.format("%013.2f", 0);
//                cashier_reading_item.setEndBal(endBal);
//            }
        }
        else{
            // endBal = String.format("%013.2f", 0);
            endBal =String.valueOf(0);
            cashier_reading_item.setEndBal(endBal);
        }

        db.close();
    }

    int cash1000,cash500,cash200,cash100,cash50,cash20,cash10,cash5,cash1,cashCents;
    Double cashInTotal;
    Double cashOutTotal;

    private void ReadFloatIN(Context context){

        shift_active.getShiftingTable(context.getApplicationContext());
        shift_active.getAccountInfo(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();


        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadFloatIN =db.rawQuery("select * from CashInOut where TransType='FLOAT IN' and CashierID='"+shift_active.getActiveUserID()+"'and TransDate='"+ FinalDate+"'", null);
        Cursor cursorReadFloatINQty =db.rawQuery("select count(TransactionID) from CashInOut where TransType='FLOAT IN' and CashierID='"+shift_active.getActiveUserID()+"'and TransDate='"+ FinalDate+"'", null);
        if (cursorReadFloatINQty.getCount()!=0){
            while (cursorReadFloatINQty.moveToNext()){
                cashier_reading_item.setCashFloatQty(cursorReadFloatINQty.getString(0));
            }
        }
        else{
            cashier_reading_item.setCashFloatQty("0");
        }

        if (cursorReadFloatIN.getCount()!=0){
            cashFloatInIndicator=1;
            while (cursorReadFloatIN.moveToNext()){
                cash1000+=Integer.parseInt(cursorReadFloatIN.getString(1));
                cash500+=Integer.parseInt(cursorReadFloatIN.getString(2));
                cash200+=Integer.parseInt(cursorReadFloatIN.getString(3));
                cash100+=Integer.parseInt(cursorReadFloatIN.getString(4));
                cash50+=Integer.parseInt(cursorReadFloatIN.getString(5));
                cash20+=Integer.parseInt(cursorReadFloatIN.getString(6));
                cash10+=Integer.parseInt(cursorReadFloatIN.getString(7));
                cash5+=Integer.parseInt(cursorReadFloatIN.getString(8));
                cash1+=Integer.parseInt(cursorReadFloatIN.getString(9));
                cashCents+=Integer.parseInt(cursorReadFloatIN.getString(10));

            }

            cashInTotal=cash1000*1000 + cash500*500 + cash200*200 + cash100*100 + cash50*50 +cash20*20 +cash10*10 + cash5*5 + cash1*1 + cashCents*.25;
            // Toast.makeText(context, "GOOD", Toast.LENGTH_SHORT).show();
            //show alertdialog xread






        }
        else{
            cashFloatInIndicator=0;
        }
        db.close();

    }

    String dateTime2;
//    private void ReadCash(Context context){
//        dateTime2=new SimpleDateFormat("yyyy-dd-mm", Locale.getDefault()).format(new Date());
//        shift_active.getAccountInfo(context.getApplicationContext());
//        dateTime.generateDateTime();
//        system_final_date SysDate = new system_final_date();
//        SysDate.insertDate(context.getApplicationContext());
//    FinalDate=SysDate.getSystemDate();
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor cursorReadTotalCash =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'", null);
//        Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'", null);
//        if (cursorReadTotalCash.getCount()!=0){
//            cursorReadTotalCash.moveToFirst();
//           // Toast.makeText(context, cursorReadTotalCash.getString(0), Toast.LENGTH_SHORT).show();
//            if (cursorReadTotalCash.getString(0)==null){
//                Log.e("FinalCashAmount","null");
//                finalCashAmount=0.00;
//            }
//            else{
//                Log.e("FinalCashAmount",cursorReadTotalCash.getString(0));
//                finalCashAmount = Double.parseDouble(cursorReadTotalCash.getString(0));
//            }
//
////            try{
////
////            }
////            catch (Exception ex){
////                Log.e("FinalCashAmount","ERROR");
////                finalCashAmount=0.00;
////            }
//
//
//        }
//        if (cursorReadTotalCashQty.getCount()!=0){
//            while (cursorReadTotalCashQty.moveToNext()){
//                cashier_reading_item.setCashQty(cursorReadTotalCashQty.getString(0));
//            }
//
//        }
//        else{
//            cashier_reading_item.setCashQty("0");
//        }
//        db.close();
//    }

    private void ReadCash(Context context){
        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        // ReadRefund(context);
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadTotalCash =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'", null);
        Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'", null);
        if (cursorReadTotalCash.getCount()!=0){
            cursorReadTotalCash.moveToFirst();
            // Toast.makeText(context, cursorReadTotalCash.getString(0), Toast.LENGTH_SHORT).show();
            if (cursorReadTotalCash.getString(0)==null){
                Log.e("FinalCashAmount","null");
                finalCashAmount=0.00;
            }
            else{
                Log.e("FinalCashAmount",cursorReadTotalCash.getString(0));
                finalCashAmount = Double.parseDouble(cursorReadTotalCash.getString(0));
            }

//            try{
//
//            }
//            catch (Exception ex){
//                Log.e("FinalCashAmount","ERROR");
//                finalCashAmount=0.00;
//            }


        }
        if (cursorReadTotalCashQty.getCount()!=0){
            while (cursorReadTotalCashQty.moveToNext()){
                cashier_reading_item.setCashQty(cursorReadTotalCashQty.getString(0));
            }

        }
        else{
            cashier_reading_item.setCashQty("0");
        }
        db.close();
    }
    ArrayList<String>TransactionList=new ArrayList<String>();
    private void ReadCashInMultiple(Context context){
        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        // ReadRefund(context);
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorGetTransactionID =db.rawQuery("select TransactionID from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"'", null);
        if (cursorGetTransactionID.getCount()!=0){
            while (cursorGetTransactionID.moveToNext()){
                TransactionList.add(cursorGetTransactionID.getString(0));
            }
            for (int x=0;x<TransactionList.size();x++){
                Cursor cursorReadTotalCashAmount =db.rawQuery("select sum(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CASH' and TransactionID='"+TransactionList.get(x)+"'", null);
                if (cursorReadTotalCashAmount.getCount()!=0){
                    while (cursorReadTotalCashAmount.moveToNext()){
                        finalCashAmount=finalCashAmount+cursorReadTotalCashAmount.getDouble(0);
                    }
                }
                Cursor cursorReadTotalCashQty =db.rawQuery("select count(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CASH' and TransactionID='"+TransactionList.get(x)+"'", null);
                if (cursorReadTotalCashQty.getCount()!=0){
                    while (cursorReadTotalCashQty.moveToNext()){
                        cashier_reading_item.setCashQty(String.valueOf(Integer.parseInt(cashier_reading_item.getCashQty())+Integer.parseInt(cursorReadTotalCashQty.getString(0))));

                    }
                }

            }


        }



        db.close();
    }



    Double totalVatablePrice=0.00;
    Double totalVatablesSales=0.00;
    Double totalTaxAmount=0.00;
    Double totalNetSales=0.00;
    Double totalNormalDiscount=0.00;
    Double totalSpecialDiscount=0.00;
    ArrayList<String>TransactionIDList=new ArrayList<String>();
   // String dateTime2;
    String dateTime2Format2;
    String transaction_Date;

    private void ReadGrossSales(Context context){ //for getting Transaction ID and Total Gross Sales
        shift_active shift_active= new shift_active();
        shift_active.getShiftingTable(context.getApplicationContext());
        Date currentTime = Calendar.getInstance().getTime();
        // dateTime2Format2=new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        //  Log.e("date",dateTime2);
        //Log.e("date2",dateTime2Format2);
        Log.e("transuser",shift_active.getActiveUserID());
        Log.e("active shift",shift_active.getShiftActive());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();

        TransactionIDList.clear();

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor invoiceReceiptTotal =db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='" + FinalDate + "'", null);
        //   Cursor invoiceReceiptTotal = db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='01-01-1970' and TransUser='001'and shiftActive='5'",null);

        if (invoiceReceiptTotal.getCount()!=0){
            while (invoiceReceiptTotal.moveToNext()){
                Log.e("TRANSACTIONID",invoiceReceiptTotal.getString(0));
                TransactionIDList.add(invoiceReceiptTotal.getString(0));
            }
            for (int x=0;x<TransactionIDList.size();x++){

                Cursor invoiceReceiptItemFinal =db.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='" +TransactionIDList.get(x)+ "'", null);
                if (invoiceReceiptItemFinal.getCount()!=0){
                    while (invoiceReceiptItemFinal.moveToNext()){
                        grossSales+=Double.parseDouble(invoiceReceiptItemFinal.getString(5));
                    }
                }


            }



        }
        else{
            TransactionIDList.add("0");
        }




        db.close();


    }
//    private void ReadGrossSales(Context context){ //for getting Transaction ID and Total Gross Sales
//
//
//
//        shift_active shift_active= new shift_active();
//        shift_active.getShiftingTable(context.getApplicationContext());
//        Date currentTime = Calendar.getInstance().getTime();
//        //dateTime2Format2=new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        system_final_date systemDate = new system_final_date();
//        systemDate.insertDate(context.getApplicationContext());
//        FinalDate=systemDate.getSystemDate();
//        Cursor invoiceReceiptTotal =db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='" +FinalDate +"'", null);
//
//
//        if (invoiceReceiptTotal.getCount()!=0){
//            while (invoiceReceiptTotal.moveToNext()){
//                TransactionIDList.add(invoiceReceiptTotal.getString(0));
//            }
//            grossSales=0.00;
//            for (int x=0;x<TransactionIDList.size();x++){
//
//                Cursor invoiceReceiptItemFinal =db.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='" +TransactionIDList.get(x)+ "'", null);
//                if (invoiceReceiptItemFinal.getCount()!=0){
//                    while (invoiceReceiptItemFinal.moveToNext()){
//                        grossSales+=Double.parseDouble(invoiceReceiptItemFinal.getString(5));
//                    }
//                }
//
//
//            }
//
//
//
//        }
//        else{
//            TransactionIDList.add("0");
//        }
//
//
//
//
//        db.close();
//
//
//
//
//
//    }


    private void ReadVatableSales(Context context){

//        dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
//        shift_active.getAccountInfo(context.getApplicationContext());
//        dateTime.generateDateTime();
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor cursorReadOrderPriceTotal =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
//        Cursor cursorReadOrderPriceTotalDiscount =db.rawQuery("select sum(DiscAmount) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
//        // Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'", null);
//        if (cursorReadOrderPriceTotal.getCount()!=0){
//            cursorReadOrderPriceTotal.moveToFirst();
//            Toast.makeText(context, cursorReadOrderPriceTotal.getString(0), Toast.LENGTH_SHORT).show();
//            if (cursorReadOrderPriceTotal.getString(0)==null){
//
//                totalVatablePrice=0.00;
//            }
//            else{
//
//                totalVatablePrice = Double.parseDouble(cursorReadOrderPriceTotal.getString(0));
//
//            }
//
//            Double vatablePercent =1.12;
//            Double taxPercent=.12;
//            Double vatableAmount= totalVatablePrice / vatablePercent;
//
//            totalTaxAmount =  vatableAmount * taxPercent;
//            totalVatablesSales=vatableAmount;
//
//
//        }
//
//        db.close();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();

        // dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        shift_active.getAccountInfo(context.getApplicationContext());
        //dateTime.generateDateTime();
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Log.e("TransDate",FinalDate);
        Log.e("TransUserID",shift_active.getActiveUserID());
        Log.e("TransShift",shift_active.getShiftActive());


        Cursor cursorReadOrderPriceTotal =db.rawQuery("select sum(TotalVatableSales),sum(TotalVatAmount),sum(TotalVatExempt),sum(TotalZeroRatedSales),sum(TotalLessVat) from FinalTransactionReport where TransDate='"+FinalDate+"'", null);
        //  Cursor cursorReadOrderPriceTotalDiscount =db.rawQuery("select sum(DiscAmount) from FinalTransactionReport where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        // Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'", null);
        if (cursorReadOrderPriceTotal.getCount()!=0){
            cursorReadOrderPriceTotal.moveToFirst();
         //   Toast.makeText(context, cursorReadOrderPriceTotal.getString(0), Toast.LENGTH_SHORT).show();
            if (cursorReadOrderPriceTotal.getString(0)==null){

                totalVatablePrice=0.00;

            }
            else{

                totalVatablePrice = Double.parseDouble(cursorReadOrderPriceTotal.getString(0));
                totalTaxAmount= Double.parseDouble(cursorReadOrderPriceTotal.getString(1));
                totalVatExempt=Double.parseDouble(cursorReadOrderPriceTotal.getString(2));
                zeroRatedSales=Double.parseDouble(cursorReadOrderPriceTotal.getString(3));
                lessVat=Double.parseDouble(cursorReadOrderPriceTotal.getString(4));

            }

//            Double vatablePercent =1.12;
//            Double taxPercent=.12;
//            Double vatableAmount= totalVatablePrice / vatablePercent;
//

            totalVatablesSales=totalVatablePrice;


        }

        db.close();



    }


//    private void ReadVatableSales(Context context){
//
////        dateTime2=new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
////        shift_active.getAccountInfo(context.getApplicationContext());
////        dateTime.generateDateTime();
////        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
////        Cursor cursorReadOrderPriceTotal =db.rawQuery("select sum(TotalVatablesSales) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'", null);
////        Cursor cursorReadOrderPriceTotalDiscount =db.rawQuery("select sum(DiscAmount) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'", null);
////        // Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'", null);
////        if (cursorReadOrderPriceTotal.getCount()!=0){
////            cursorReadOrderPriceTotal.moveToFirst();
////            Toast.makeText(context, cursorReadOrderPriceTotal.getString(0), Toast.LENGTH_SHORT).show();
////            if (cursorReadOrderPriceTotal.getString(0)==null){
////
////                totalVatablePrice=0.00;
////            }
////            else{
////
////                totalVatablePrice = Double.parseDouble(cursorReadOrderPriceTotal.getString(0));
////
////            }
////
////            Double vatablePercent =1.12;
////            Double taxPercent=.12;
////            Double vatableAmount= totalVatablePrice / vatablePercent;
////
////            totalTaxAmount =  vatableAmount * taxPercent;
////            totalVatablesSales=vatableAmount;
////
////
////        }
////
////        db.close();
//
//
//        system_final_date SysDate = new system_final_date();
//        SysDate.insertDate(context.getApplicationContext());
//        FinalDate=SysDate.getSystemDate();
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor invoiceReceiptTotal =db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='" + FinalDate + "'", null);
//        //   Cursor invoiceReceiptTotal = db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='01-01-1970' and TransUser='001'and shiftActive='5'",null);
//
//        if (invoiceReceiptTotal.getCount()!=0){
//            while (invoiceReceiptTotal.moveToNext()){
//                Log.e("TRANSACTIONID",invoiceReceiptTotal.getString(0));
//                TransactionIDList.add(invoiceReceiptTotal.getString(0));
//            }
//            // for (int x=0;x<TransactionIDList.size();x++){
//
//            Cursor getVatableSales =db.rawQuery("select sum(TotalVatableSales) from FinalTransactionReport where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
//            Cursor getTaxAmount =db.rawQuery("select sum(TotalVatAmount) from FinalTransactionReport where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
//            Cursor getVatExempt =db.rawQuery("select sum(TotalVatExempt) from FinalTransactionReport where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
//            Cursor getZeroRatedSales =db.rawQuery("select sum(TotalZeroRatedSales) from FinalTransactionReport where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
//
//            //    Cursor getEndORZ =db.rawQuery("select * from officialReceipt where TransactionID between'" +TransactionIDList.get(0)+ "'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'", null);
//
//
//            if (getVatableSales.getCount()!=0){
//                totalVatablesSales=0.00;
//                if (getVatableSales.moveToNext()){
//                    double FinalAmount=0.00;
//                    if (getVatableSales.getString(0)==null){
//                        FinalAmount=0.00;
//                    }
//                    else{
//                         FinalAmount = Double.valueOf(getVatableSales.getString(0));
//                    }
//
//
//                   // negVat=Double.valueOf(itemListC2.getString(1));
//                    DecimalFormat format = new DecimalFormat("0.00");
//                    format.setRoundingMode(RoundingMode.UP);
//                    String formatted = format.format(FinalAmount);
//                  //  totalSubtract = formatted;
//
//
//
//                    totalVatablesSales+=Double.parseDouble(formatted);
//
//                }
//
//            }
//            if (getTaxAmount.getCount()!=0){
//                totalTaxAmount=0.00;
//                if (getTaxAmount.moveToNext()){
//                    double FinalAmount=0.00;
//                    if (getTaxAmount.getString(0)==null){
//                        FinalAmount=0.00;
//                    }
//                    else{
//                        FinalAmount= Double.valueOf(getTaxAmount.getString(0));
//                    }
//
//                    // negVat=Double.valueOf(itemListC2.getString(1));
//                    DecimalFormat format = new DecimalFormat("0.00");
//                    format.setRoundingMode(RoundingMode.UP);
//                    String formatted = format.format(FinalAmount);
//                    totalTaxAmount+=Double.parseDouble(formatted);
//
//                }
//
//            }
//            if (getVatExempt.getCount()!=0){
//                totalVatExempt=0.00;
//                if (getVatExempt.moveToNext()){
//
//
//                    double FinalAmount = 0.00 ;
//                    if (getVatExempt.getString(0)==null){
//                        FinalAmount=0.00;
//                    }
//                    else{
//                        FinalAmount= Double.valueOf(getVatExempt.getString(0));
//                    }
//
//                    // negVat=Double.valueOf(itemListC2.getString(1));
//                    DecimalFormat format = new DecimalFormat("0.00");
//                    format.setRoundingMode(RoundingMode.UP);
//                    String formatted = format.format(FinalAmount);
//                    totalVatExempt+=Double.parseDouble(formatted);
//
//                }
//
//            }
//            if (getZeroRatedSales.getCount()!=0){
//                zeroRatedSales=0.00;
//                if (getZeroRatedSales.moveToNext()){
//                    double FinalAmount=0.00;
//                    if (getZeroRatedSales.getString(0)==null){
//                        FinalAmount=0.00;
//                    }
//                    else{
//                        FinalAmount = Double.valueOf(getZeroRatedSales.getString(0));
//                    }
//
//                    // negVat=Double.valueOf(itemListC2.getString(1));
//                    DecimalFormat format = new DecimalFormat("0.00");
//                    format.setRoundingMode(RoundingMode.UP);
//                    String formatted = format.format(FinalAmount);
//
//                    zeroRatedSales+=Double.parseDouble(formatted);
//                }
//
//            }
//
//
//
//
//        }
//        db.close();
//
//
//
//
//    }



//    public void updateReading(Context context){
//        //update ENDING OR,ENDING BAL,ENDING TRANSACTION
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//        String updateStatus = "UPDATE ReadingIndicator set indicatorStatus=1 where indicatorID=1"; // ready for reading
//        db2.execSQL(updateStatus);
//
////        int shftActv = Integer.valueOf(ActiveShift)+1;
////
////        String updateShifting = "UPDATE ShiftingTable set  shiftActive='"+shftActv+"',shiftActiveUser='' where shiftID=1"; // change shift
////        db2.execSQL(updateShifting);
//
////        String deleteReadTable = "delete from ReadingTable"; // change shift
////        db2.execSQL(deleteReadTable);
//
//        String updateShifting = "delete from ShiftingTable"; // change shift
//        db2.execSQL(updateShifting);
//
//
//
//        getActiveUser(context);
//        shift_active shift_active = new shift_active();
//        shift_active.getShiftingTable(getContext());
//
//        Cursor cursorReadingOR = db.rawQuery("select * from OfficialReceipt", null);
//        Cursor cursorReadingBalance =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'", null);
//        if (cursorReadingBalance.getCount()!=0){
//            while (cursorReadingBalance.moveToNext()){
//                if (cursorReadingBalance.getString(0)==null){
//                    double dataInt = Double.parseDouble("0.00");
//                    // endBal = String.format("%013.2f", 0);
//                    endBal=String.format("%013.2f", dataInt);
//                }
//                else{
//                    endBal=cursorReadingBalance.getString(0);
//                }
////              cursorReadingBalance.moveToFirst();
//////              endBal=cursorReadingBalance.getString(0);
//
//            }
//
//        }
//        if (cursorReadingBalance.getCount()==0){
//            double dataInt = Double.parseDouble("0.00");
//            // endBal = String.format("%013.2f", 0);
//            endBal=String.valueOf(dataInt);
//        }
//        if (cursorReadingOR.getCount()!=0){
//            cursorReadingOR.moveToLast();
//            endOR=cursorReadingOR.getString(0);
//        }
//
//        if (cursorReadingOR.getCount()==0){
//            int origRefNumber = 0;
//            String formatted = String.format("%09d", origRefNumber);
//            endOR=formatted;
//        }
//
////        cursorReadingBalance.moveToFirst();
////
//
//        or_trans_item = new OR_TRANS_ITEM();
//        readReferenceNumber(context); //read Transaction before clicking xread
//        DatabaseHandler databaseHandler=new DatabaseHandler(context);
//
//
//        databaseHandler.insertInvoiceReceiptTotal(or_trans_item.getTransactionNo(),"","",""
//                ,"","","","","ZREAD",dateOnly.format(currentDate.getTime()),timeOnly.format(currentDate.getTime()),ActiveUser,shift_active.getShiftActive());
//        //  readReferenceNumber(context); //read Transaction after clicking xread
//
//
//
//
//        endTrans=or_trans_item.getTransactionNo();
//
//
//
//
////        String strsql = "UPDATE ReadingTable set  readingEndOR='"+endOR+"',readingEndBal='"+endBal+"',readingEndTrans='"+endTrans+"'  where readingID=1";
////        db2.execSQL(strsql);
//
//        String updateStatus = "UPDATE ReadingIndicator set indicatorStatus=1 where indicatorID=1"; // ready for reading
//        db2.execSQL(updateStatus);
//
////        int shftActv = Integer.valueOf(ActiveShift)+1;
////
////        String updateShifting = "UPDATE ShiftingTable set  shiftActive='"+shftActv+"',shiftActiveUser='' where shiftID=1"; // change shift
////        db2.execSQL(updateShifting);
//
////        String deleteReadTable = "delete from ReadingTable"; // change shift
////        db2.execSQL(deleteReadTable);
//
//        String updateShifting = "delete from ShiftingTable"; // change shift
//        db2.execSQL(updateShifting);
//
//
//
//
//
//        //copying to final Read Table
//
//
//
////        Cursor cursorReadingTable = db2.rawQuery("select * from ReadingTable", null);
////        if (cursorReadingTable.getCount()!=0){
////            while(cursorReadingTable.moveToNext()) {
////
////
////                settingsDB settingsDB = new settingsDB(context);
////                settingsDB.insertReadTableFinal(
////                        cursorReadingTable.getString(0),
////                        cursorReadingTable.getString(1),
////                        cursorReadingTable.getString(2),
////                        cursorReadingTable.getString(3),
////                        cursorReadingTable.getString(4),
////                        cursorReadingTable.getString(5),
////                        cursorReadingTable.getString(6),
////                        cursorReadingTable.getString(7),
////                        cursorReadingTable.getString(8),
////                        cursorReadingTable.getString(9),
////                        cursorReadingTable.getString(10),
////                        cursorReadingTable.getString(11)
////
////
////                );
////            }
////
////
////        }
//
//        //clear the readingtable
//
//
//
//
//
//        db.close();
//        db2.close();
//
//
//    }
    String ActiveUser;
    String ActiveShift;
    private void getActiveUser(Context context){
        SQLiteDatabase db = context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor readingShift = db.rawQuery("select * from ShiftingTable", null);
        readingShift.moveToFirst();
        ActiveShift=readingShift.getString(0);
        ActiveUser=readingShift.getString(3);
        db.close();
    }




    private void readReferenceNumber(Context context) {

        //primary key 00000001

        // int readPK;
        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

        //   db2 = this .openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        Cursor  itemListC = db2.rawQuery("select * from InvoiceReceiptTotal ", null);
        if (itemListC.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%010d", origRefNumber);
            or_trans_item.setTransactionNo(formatted);
        } else {

            itemListC = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%010d", incrementPK);

            // readRefNumber = incrementPKString;
            or_trans_item.setTransactionNo(incrementPKString);


            // }
        }
        itemListC.close();
        db2.close();


    }
    String DATABASE_BACKUP_PATH_POSOutputDB="/data/data/com.example.myapplication/databases/";
    String DATABASE_NAME="PosOutputDB.db";
    String DATABASE_NAME2="settings.db";
    String DATABASE_NAME3="POSAndroidDB.db";
    String DATABASE_PATH_ANDROID=(Environment.getExternalStorageDirectory()+"/ANDROID_POS/");

    public void ExportBackUPSettingsDB(){
        try{
            //  File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
//           if (data.canWrite()){
            String currentDBPath= DATABASE_BACKUP_PATH_POSOutputDB+DATABASE_NAME2;
            String copieDBPath=DATABASE_PATH_ANDROID+DATABASE_NAME2;
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }
//           }
//           else{
//               Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
//           }
        }
        catch (Exception ex){

        }
    }








}
