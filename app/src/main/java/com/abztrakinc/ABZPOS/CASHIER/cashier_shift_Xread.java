package com.abztrakinc.ABZPOS.CASHIER;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
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
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.abztrakinc.ABZPOS.ADMIN.Header_Footer_class;
import com.abztrakinc.ABZPOS.ADMIN.admin_email_send;
import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.DateTime;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.settingsDB;
import com.abztrakinc.ABZPOS.system_final_date;
import com.zqprintersdk.PrinterConst;

import a1088sdk.PrnDspA1088;

public class cashier_shift_Xread extends cashier_shift {
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
    String FooterContent;
    String printData;
    String OR="";
    String Trans="";
    String cashierName="admin 100";
    String posNumber="";
    //String DateTimenow="";
    String FinalDate;
    Date currentTime = Calendar.getInstance().getTime();
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    String transactionNumber;
    int cashFloatInIndicator=0;  //if 0 close if 1 open
    Double finalCashAmount;
    int activateCode=1;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    //OR_TRANS_ITEM or_trans_item;
    Thread workerThread;
    int printerSignal=0;
    DecimalFormat FinalDoubleFormat = new DecimalFormat("0.00");
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    DateTime dateTime = new DateTime();
    OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
    shift_active shift_active = new shift_active();
    cashier_reading_item cashier_reading_item = new cashier_reading_item();
    ArrayList<String>BankNameList=new ArrayList<String>();
    ArrayList<String>OtherPaymentList=new ArrayList<String>();
    ArrayList<String>DiscountNameList=new ArrayList<String>();
    ArrayList<String>DiscountNameListSpecial=new ArrayList<String>();
    ArrayList<String>DiscountNameListNormal=new ArrayList<String>();
    ArrayList<String>TransactionWithDiscount=new ArrayList<String>();

    ArrayList<String>TransactionIDList=new ArrayList<String>();
    printer_settings_class PrinterSettings;






    public void printReport(Context context,String CashPickup,int activateCode2){
        //Toast.makeText(context, "PRINTING Z READ", Toast.LENGTH_SHORT).show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                activateCode=activateCode2;
//                try{
                cashOutTotal=Double.parseDouble(CashPickup);
                    readingEndBalance(context);

                PrinterSettings = new printer_settings_class(context);

                    ReadFloatIN(context);
                    ReadFloatOut(context);
                    ReadCash(context);
                    ReadCashInMultiple(context);
                    createTextfile(context);
                    updateReading(context);




                    //  ExportBackUPSettingsDB();
//



                    //getTransactionRecord();
                   // unLockCashBox();
//                }catch (Exception ex){
//                   // Log.e("ERROR PRINTING",ex.getMessage());
//                    if (ex.getMessage().isEmpty()){
//                        Toast.makeText(context, "ERROR PRINTING", Toast.LENGTH_SHORT).show();
//                    }else{
//                        Toast.makeText(context, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
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


    public interface PrintCallback {
        void onPrintResult(String result);
    }
    private PrintCallback printCallback;
    public void setPrintCallback(PrintCallback callback) {
        this.printCallback = callback;
    }


    public void printReportViewing(Context context, String CashPickup, int activateCode2) {
        // Your existing code...

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                activateCode = activateCode2;

                cashOutTotal = Double.parseDouble(CashPickup);
                readingEndBalance(context);

                PrinterSettings = new printer_settings_class(context);

                ReadFloatIN(context);
                ReadFloatOut(context);
                ReadCash(context);
                ReadCashInMultiple(context);
                printfileViewing(context);



            }
        }).start();
    }

    public void ReportViewing(Context context, String CashPickup, int activateCode2) {
        // Your existing code...

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                activateCode = activateCode2;

                cashOutTotal = Double.parseDouble(CashPickup);
                readingEndBalance(context);

                PrinterSettings = new printer_settings_class(context);

                ReadFloatIN(context);
                ReadFloatOut(context);
                ReadCash(context);
                ReadCashInMultiple(context);

                String result = createTextfileViewing(context);

                // Call the callback to return the result to the UI thread
                if (printCallback != null) {
                    printCallback.onPrintResult(result);
                }

                // Call the callback to return the result to the UI thread

            }
        }).start();
    }


//    public String printReportViewing(Context context,String CashPickup,int activateCode2){
//        //Toast.makeText(context, "PRINTING Z READ", Toast.LENGTH_SHORT).show();
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                activateCode=activateCode2;
////                try{
//                cashOutTotal=Double.parseDouble(CashPickup);
//                readingEndBalance(context);
//
//                PrinterSettings = new printer_settings_class(context);
//
//                ReadFloatIN(context);
//                ReadFloatOut(context);
//                ReadCash(context);
//                ReadCashInMultiple(context);
//               String str= createTextfileViewing(context);
//                return str;
//
//
//            }
//        }).start();
//
//
//
//
//
//
//    }


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
    double debitCardAmount=0.00;
    int debitCardQty;
    int otherPaymentQty;
    double otherPaymentAmount;
    double unclaimAmount = 0.00;
    int unclaimQty;
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
    Double TotalSpecialDiscount=0.00;
    double totalRefund=0.00;
    Double TotalNormalDiscount=0.00;
    Double FinalBankAmount=0.00;



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
        cashier_reading_item.getReadingData(context.getApplicationContext());
        cashier_reading_item.getLastOR(context.getApplicationContext());
        //dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        ReadVatableSales(context.getApplicationContext());
        DecimalFormat form = new DecimalFormat("0.00");
        cashier_reading_item.resetCounter(context.getApplicationContext());
        ReadGrossSales(context.getApplicationContext());
        ReadRefund(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate = SysDate.getSystemDate();

        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        HeaderFooterClass.HeaderNote(context.getApplicationContext());
        HeaderFooterClass.FooterNote(context.getApplicationContext());







        resetCtrDec =((Double.parseDouble(cashier_reading_item.getBegBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);

        totalBal = Double.parseDouble(cashier_reading_item.getBegBal())-(resetCtrInt*end) ;

        DecimalFormat df = new DecimalFormat("0000000000000.00");
        StartBalanceCtr=String.format("%02d", resetCtrInt);
       // StartBalanceCtr=String.valueOf(resetCtrInt);
      //  StartBalance=String.format("%016.2f", totalBal);
        StartBalance = df.format(totalBal);


        resetCtrDec =((Double.parseDouble(cashier_reading_item.getEndBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);
       // resetCtrInt = Integer.parseInt(String.valueOf(resetCtrDec));
        totalBal = Double.parseDouble(cashier_reading_item.getEndBal())-(resetCtrInt*end) ;

        EndBalanceCtr=String.format("%02d", resetCtrInt);
        //EndBalanceCtr=form.format(resetCtrInt);


        //EndBalance=String.format("%016.2f", totalBal);


        EndBalance=df.format(totalBal);




       // totalBal = (10000000000.00)-(resetCtrInt*end) ;

        //cashier_reading_item.setEndBal(String.valueOf(totalBal));








        int modx=Integer.parseInt(or_trans_item.getTransactionNo());
        Log.e("modx",String.valueOf(modx));
        int mody=999999999;
        int resetCount = modx/mody;
        Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtr = String.format("%02d", resetCount);
        String formattedTrans =  String.valueOf(modx % mody);
        transactionNumber=formattedTrans;
        transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));
        readTotalCashLeft(context);




//        try {
//            findBT(context);
//            openBT(context);
//

//


        //note!
        StringBuffer buffer = new StringBuffer();

        buffer.append(HeaderFooterClass.getHeaderText()+ "\n");
//            buffer.append("SHF: " + "\t\t" + "POS: " + "\n");

        buffer.append("================================" + "\n");
        buffer.append("            X_READING           " + "\n");
        buffer.append("================================" + "\n");
        buffer.append( "TRANS#: "+formattedCtr+"-" +transactionNumber+ "\n");
        buffer.append("Business Date: " +  convertdate(FinalDate)+ "\n");
        buffer.append("Report ID: " + shift_active.getActiveUserID()+"\n");
        buffer.append("POS: " + shift_active.getPOSCounter() + "     " +"Shift: "+ shift_active.getShiftActive() +"\n");
        buffer.append("CASHIER: " + shift_active.getActiveUserName() + "\n\n\n");





        //Beg OR NO
        int modxFinalBegOR=Integer.parseInt(cashier_reading_item.getBegORNo());
        // Log.e("modx",String.valueOf(modx));
        long modyFinalBegOR=999999999999999L;
        long resetCountFinalBegOR = modxFinalBegOR/modyFinalBegOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalBegOR = String.format("%02d", resetCountFinalBegOR);
        String formattedFinalBegOR =  String.valueOf(modxFinalBegOR % modyFinalBegOR);
        String FinalBegOR=formattedFinalBegOR;
        FinalBegOR =String.format("%010d",Integer.parseInt(FinalBegOR));

        //Ending OR NO


        int modxFinalEndOR=Integer.parseInt(cashier_reading_item.getEndORNo());
        // Log.e("modx",String.valueOf(modx));
        long modyFinalEndOR=999999999999999L;
        long resetCountFinalEndOR = modxFinalEndOR/modyFinalEndOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalEndOR = String.format("%02d", resetCountFinalEndOR);
        String formattedFinalEndOR =  String.valueOf(modxFinalEndOR % modyFinalEndOR);
        String FinalEndOR=formattedFinalEndOR;
        FinalEndOR =String.format("%010d",Integer.parseInt(FinalEndOR));





//
//        buffer.append("Beginning Invoice No.: \n"+
//                ("                    " +formattedCtrFinalBegOR+"-" +FinalBegOR+"      ").substring(0,32) +"\n");
//        buffer.append("Ending Invoice No.: \n"+
//                ("                    " +formattedCtrFinalEndOR+"-" +FinalEndOR+"      ").substring(0,32) +"\n");


        //
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

        buffer.append("================================" + "\n");
        buffer.append("          TENDER REPORT         " + "\n");
        buffer.append("================================" + "\n");
        buffer.append("TENDER                   AMOUNT "+"\n");
        buffer.append("--------------------------------" + "\n");

//        if (cashFloatInIndicator==1) {
            buffer.append("CASH FLOAT      "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashFloatQty()))+"      ").substring(0,6) +  ("   " + String.format("%7.2f",cashInTotal) + "           ").substring(0, 10) + "\n");
                Log.d("XREPORT", "createTextfile: " + cashInTotal);
//        }
   //     buffer.append("CASH            "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"      ").substring(0,6)+("   " + String.format("%7.2f",finalCashAmount+cashBoxCash)+"           ").substring(0,10) + "\n");
        buffer.append("CASH            "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"      ").substring(0,6)+("   " + String.format("%7.2f",finalCashAmount)+"           ").substring(0,10) + "\n");



        if (cashFloatInIndicator==2) {
            buffer.append("CASH PICKUP     "+(String.format("%6d",1)+"       -").substring(0,6) + ("  " + String.format("%7.2f",cashOutTotal*-1) + "           ").substring(0, 10) + "\n");
        }


        SQLiteDatabase RefundDb= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //  Cursor readRefundCtr =RefundDb.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransactionID='"+TransactionIDList.get(x)+"'", null);


        String startTranss =TransactionIDList.get(0);
        String endTranss=TransactionIDList.get(TransactionIDList.size()-1);
        Log.e("readrefund",String.valueOf(totalRefund));


        if (totalRefund!=0.00) {
            ;
            buffer.append("CASH REFUND     " + (String.format("%6d", Integer.parseInt(String.valueOf(totalRefundCounter))) + "        ").substring(0, 6) + ("   " + String.format("%7.2f", Double.parseDouble(String.valueOf(totalRefund))) + "           ").substring(0, 10) + "\n");

        }

        // ReadRefund();






           // Double finalTotalCash = finalCashAmount-totalRefund+cashInTotal-cashOutTotal+cashBoxCash;
        Double finalTotalCash = finalCashAmount-totalRefund+cashInTotal-cashOutTotal;
            buffer.append("TOTAL: CASH     "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"        ").substring(0,6) + ("   " + String.format("%7.2f",finalTotalCash)+"           ").substring(0,10)  + "\n");
            buffer.append("================================" + "\n");
            updateTotalCashLeft(context,String.valueOf(finalTotalCash));

            SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
             SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);


            Cursor readBankDetails =db.rawQuery("select * from BankDetails", null);
            Cursor readOtherPayment =db2.rawQuery("select * from OtherPayment", null);
            Cursor readDiscountName =db.rawQuery("select DISTINCT DiscountType from InvoiceReceiptItemFinalWDiscount", null);



         //   Cursor invoiceReceipItemFinal =db.rawQuery("select sum(DiscAmount),sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate='" + dateTime2 + "'", null);




            if (readBankDetails.getCount()!=0){
                while (readBankDetails.moveToNext()) {
                    BankNameList.add(readBankDetails.getString(1));
                }
            }

            if (readOtherPayment.getCount()!=0){
                while (readOtherPayment.moveToNext()){
                    OtherPaymentList.add(readOtherPayment.getString(1));
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
                                        TotalSpecialDiscount+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
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
            Double finalBankTransAmount=0.00;

            buffer.append("\n");
            for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
             FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='CreditCard' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheck.getCount()!=0) {
                while(invoiceReceiptTotalCheck.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheck.getString(0);
                    String TransactionAmount=invoiceReceiptTotalCheck.getString(5);
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

            //checking multiplePayment

                Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
                if (invoiceReceiptTotalCheckM.getCount()!=0) {
                    while(invoiceReceiptTotalCheckM.moveToNext()){
                        String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                        //String TransactionAmount=invoiceReceiptTotalCheckM.getString(5);
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

                //end checking




        }
             buffer.append("TOTAL:CREDITCARD"+((String.format("%6d",creditCardQty))+"        ").substring(0,6) + ("   " + String.format("%7.2f",creditCardAmount)+"           ").substring(0,10)  + "\n");






            buffer.append("================================" + "\n");
//            buffer.append("           DEBIT CARD           " + "\n");
//            buffer.append("--------------------------------" + "\n");

            for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            Double FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='DebitCard' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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

                Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
                if (invoiceReceiptTotalCheckM.getCount()!=0) {
                    while(invoiceReceiptTotalCheckM.moveToNext()){
                        String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                        //String TransactionAmount=invoiceReceiptTotalCheckM.getString(5);
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


//             buffer.append("--------------------------------" + "\n");
//            buffer.append("          OTHER PAYMENT         " + "\n");
//            buffer.append("--------------------------------" + "\n");
             for (int x=0;x<OtherPaymentList.size();x++){
            int Ctr=0;
            String FinalOtherPayment=OtherPaymentList.get(x);
                 Log.e("OTHER PAYMENT",FinalOtherPayment);
            Double FinalOtherPaymentAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='"+FinalOtherPayment.trim()+"' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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
                 Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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







        // creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+creditCardAmount
            Log.e("total credit",String.valueOf(creditCardAmount));
        Log.e("total finalCashAmount",String.valueOf(finalCashAmount));
        Log.e("total totalRefund",String.valueOf(totalRefund));
        Log.e("total otherAmount",String.valueOf(otherPaymentAmount));
        Log.e("total debitCardAmount",String.valueOf(debitCardAmount));



       //   buffer.append("TOTAL COLLECTED "+(String.format("%6d",((creditCardQty+(Integer.parseInt(cashier_reading_item.getCashQty()))+totalRefundCounter+otherPaymentQty+debitCardQty)))+"          ").substring(0,6) + ("   " + String.format("%7.2f",Double.parseDouble(String.valueOf(creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+creditCardAmount)))+"           ").substring(0,10)  + "\n\n");

        buffer.append("BREAKDOWN OF SALES:                       " + "\n");
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

        buffer.append("Zero-Rated Sales      " + ("   "+String.format("%7.2f",zeroRatedSales)+"           ").substring(0,10)  + "\n");
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

            }




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
                        Log.e("ERROR",ex.getMessage());
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



           // buffer.append("Service Charges                    " + (String.format("%7.2f",serviceCharge)+"           ").substring(0,11)  + "\n\n");
//            buffer.append("--------------------------------------" + "\n");
            buffer.append("GROSS SALES           " + ("   "+String.format("%7.2f",grossSales)+"           ").substring(0,10)  + "\n");
            buffer.append("Less VAT              " + ("   "+String.format("%7.2f",lessVat)+"           ").substring(0,10)  + "\n");


            buffer.append(("SPECIAL DISCOUNT"+"                               ").substring(0,22) + ("   "+String.format("%7.2f",TotalSpecialDiscount)+"           ").substring(0,10)  + "\n");
            buffer.append(("NORMAL DISCOUNT"+"                                ").substring(0,22) + ("   "+String.format("%7.2f",TotalNormalDiscount)+"           ").substring(0,10)  + "\n");





        if (vatIndicator.getVatIndicator()==0){

            buffer.append("NVAT Amount          " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }
        else{

            buffer.append("VAT Amount            " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        } buffer.append("--------------------------------" + "\n");

            totalNetSales=grossSales-lessVat-TotalSpecialDiscount-TotalNormalDiscount-totalTaxAmount;
       // totalNetSales=grossSales-TotalSpecialDiscount-TotalNormalDiscount;


            buffer.append("NET SALES             " + ("   "+String.format("%7.2f",totalNetSales)+"           ").substring(0,10)  + "\n\n");
            buffer.append("RESET COUNTER : (" +EndBalanceCtr + ")\n");
        system_final_date systemDate= new system_final_date();
        systemDate.insertDate(context.getApplicationContext());
        SQLiteDatabase TransactionNumber= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
//        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);

        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'", null);
        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        if (StartTrans.getCount()!=0){
             StartTrans.moveToFirst();

                 buffer.append("START TRANSACTION         " + (String.format(String.valueOf(Integer.parseInt(StartTrans.getString(0)))) + "           ").substring(0, 10) + "\n");
                 EndTrans.moveToLast();

                 buffer.append("END TRANSACTION           " + (String.format(String.valueOf(Integer.parseInt(transactionNumber)) )+ "           ").substring(0, 10) + "\n");
           // buffer.append("END TRANSACTION                    " + (Integer.parseInt(transactionNumber) + "           ").substring(0, 11) + "\n");

        }
        TransactionNumber.close();

//        if (StartTrans.getCount()!=0){
//            StartTrans.moveToFirst();
//
//            buffer.append("START TRANSACTION                    " + (String.format(String.valueOf(StartTrans.getString(0))) + "           ").substring(0, 11) + "\n");
//
//            // buffer.append("END TRANSACTION                    " + (String.format(String.valueOf(TransactionIDList.get(TransactionIDList.size()-1))) + "           ").substring(0, 11) + "\n");
//            // buffer.append("END TRANSACTION                    " + (Integer.parseInt(transactionNumber) + "           ").substring(0, 11) + "\n");
//
//        }

//        buffer.append(("Accumulated Grand Total: "+"              ").substring(0,25)+"\n"+
//               "                      " +("   "+FinalDoubleFormat.format(Double.valueOf(cashier_reading_item.getEndBal()))+"          ").substring(0,10)+ "\n\n");



        buffer.append(("Accumulated Grand Total: "+"              ").substring(0,25)+"\n"+
                ("                " + EndBalance+"      ").substring(0,32)+"\n\n");


        buffer.append("--------------------------------" + "\n");
        buffer.append(HeaderFooterClass.getFooterText()+"\n");



        TransactionNumber.close();










            db.close();
            db2.close();




            try{
//                mmOutputStream.write(buffer.toString().getBytes( "US-ASCII")); // for printing receipt
//                mmOutputStream.flush();
//                mmOutputStream.close();
//                closeBT(getContext());

                printData=buffer.toString();

                //JMPrinter(printData); // for jolimark
                PrinterSettings.OnlinePrinter(printData,1,0,1);
                Log.e("PrintdData",printData);





                create_journal_entry createJournal = new create_journal_entry();
                createJournal.setPrintData(printData);
                createJournal.setTransNumber(or_trans_item.getTransactionNo());
                createJournal.journalEntry(createJournal.getPrintData(),createJournal.getTransNumber(),FinalDate);






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

    private String createTextfileViewing(Context context){

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
        cashier_reading_item.getReadingData(context.getApplicationContext());
        cashier_reading_item.getLastOR(context.getApplicationContext());
        //dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        ReadVatableSales(context.getApplicationContext());
        DecimalFormat form = new DecimalFormat("0.00");
        cashier_reading_item.resetCounter(context.getApplicationContext());
        ReadGrossSales(context.getApplicationContext());
        ReadRefund(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate = SysDate.getSystemDate();

        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        HeaderFooterClass.HeaderNote(context.getApplicationContext());
        HeaderFooterClass.FooterNote(context.getApplicationContext());







        resetCtrDec =((Double.parseDouble(cashier_reading_item.getBegBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);

        totalBal = Double.parseDouble(cashier_reading_item.getBegBal())-(resetCtrInt*end) ;

        Log.e("TAG", "createTextfileViewing: "+totalBal );

        DecimalFormat df = new DecimalFormat("0000000000000.00");
        StartBalanceCtr=String.format("%02d", resetCtrInt);
        // StartBalanceCtr=String.valueOf(resetCtrInt);
       // StartBalance=String.format("%016.2f", totalBal);
        StartBalance = df.format(totalBal);


        resetCtrDec =((Double.parseDouble(cashier_reading_item.getEndBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);
        // resetCtrInt = Integer.parseInt(String.valueOf(resetCtrDec));
        totalBal = Double.parseDouble(cashier_reading_item.getEndBal())-(resetCtrInt*end) ;
        Log.e("TAG", "totalbal: "+totalBal );

        EndBalanceCtr=String.format("%02d", resetCtrInt);
        //EndBalanceCtr=form.format(resetCtrInt);
       // EndBalance=String.format("%016.2f", totalBal);

        EndBalance = df.format(totalBal);


        // totalBal = (10000000000.00)-(resetCtrInt*end) ;

        //cashier_reading_item.setEndBal(String.valueOf(totalBal));








        int modx=Integer.parseInt(or_trans_item.getTransactionNo());
        Log.e("modx",String.valueOf(modx));
        int mody=999999999;
        int resetCount = modx/mody;
        Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtr = String.format("%02d", resetCount);
        String formattedTrans =  String.valueOf(modx % mody);
        transactionNumber=formattedTrans;
        transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));
        readTotalCashLeft(context);









        StringBuffer buffer = new StringBuffer();

        buffer.append(HeaderFooterClass.getHeaderText()+ "\n");


        buffer.append("================================" + "\n");
        buffer.append("            X_READING           "+ "\n");
        buffer.append("================================" + "\n");
        buffer.append("TRANS#: "+formattedCtr+"-" +transactionNumber+ "\n");
        buffer.append("Business Date: " +  convertdate(FinalDate)+ "\n");
        buffer.append("Report ID: " + shift_active.getActiveUserID()+"\n");
        buffer.append("POS: " + shift_active.getPOSCounter() + "     " +"Shift: "+ shift_active.getShiftActive() +"\n");
        buffer.append("CASHIER: " + shift_active.getActiveUserName() + "\n\n\n");





        //Beg OR NO
        int modxFinalBegOR=Integer.parseInt(cashier_reading_item.getBegORNo());
        // Log.e("modx",String.valueOf(modx));
        long modyFinalBegOR=999999999999999L;
        long resetCountFinalBegOR = modxFinalBegOR/modyFinalBegOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalBegOR = String.format("%02d", resetCountFinalBegOR);
        String formattedFinalBegOR =  String.valueOf(modxFinalBegOR % modyFinalBegOR);
        String FinalBegOR=formattedFinalBegOR;
        FinalBegOR =String.format("%010d",Integer.parseInt(FinalBegOR));

        //Ending OR NO


        int modxFinalEndOR=Integer.parseInt(cashier_reading_item.getEndORNo());
        // Log.e("modx",String.valueOf(modx));
        long modyFinalEndOR=999999999999999L;
        long resetCountFinalEndOR = modxFinalEndOR/modyFinalEndOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalEndOR = String.format("%02d", resetCountFinalEndOR);
        String formattedFinalEndOR =  String.valueOf(modxFinalEndOR % modyFinalEndOR);
        String FinalEndOR=formattedFinalEndOR;
        FinalEndOR =String.format("%010d",Integer.parseInt(FinalEndOR));





//
//        buffer.append("Beginning Invoice No.: \n"+
//                ("                    " +formattedCtrFinalBegOR+"-" +FinalBegOR+"      ").substring(0,32) +"\n");
//        buffer.append("Ending Invoice No.: \n"+
//                ("                    " +formattedCtrFinalEndOR+"-" +FinalEndOR+"      ").substring(0,32) +"\n");


        //
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

        buffer.append("================================" + "\n");
        buffer.append("          TENDER REPORT         " + "\n");
        buffer.append("================================" + "\n");
        buffer.append("TENDER                   AMOUNT "+"\n");
        buffer.append("--------------------------------" + "\n");

//        if (cashFloatInIndicator==1) {
        buffer.append("CASH FLOAT      "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashFloatQty()))+"      ").substring(0,6) +  ("   " + String.format("%7.2f",cashInTotal) + "           ").substring(0, 10) + "\n");
        Log.d("XREPORT", "createTextfile: " + cashInTotal);
//        }
        buffer.append("CASH            "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"      ").substring(0,6)+("   " + String.format("%7.2f",finalCashAmount+cashBoxCash)+"           ").substring(0,10) + "\n");


        if (cashFloatInIndicator==2) {
            buffer.append("CASH PICKUP     "+(String.format("%6d",1)+"       -").substring(0,6) + ("  " + String.format("%7.2f",cashOutTotal*-1) + "           ").substring(0, 10) + "\n");
        }


        SQLiteDatabase RefundDb= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //  Cursor readRefundCtr =RefundDb.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransactionID='"+TransactionIDList.get(x)+"'", null);


        String startTranss =TransactionIDList.get(0);
        String endTranss=TransactionIDList.get(TransactionIDList.size()-1);
        Log.e("readrefund",String.valueOf(totalRefund));


        if (totalRefund!=0.00) {
            ;
            buffer.append("CASH REFUND     " + (String.format("%6d", Integer.parseInt(String.valueOf(totalRefundCounter))) + "        ").substring(0, 6) + ("   " + String.format("%7.2f", Double.parseDouble(String.valueOf(totalRefund))) + "           ").substring(0, 10) + "\n");

        }

        // ReadRefund();






        Double finalTotalCash = finalCashAmount-totalRefund+cashInTotal-cashOutTotal+cashBoxCash;
        buffer.append("TOTAL: CASH     "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"        ").substring(0,6) + ("   " + String.format("%7.2f",finalTotalCash)+"           ").substring(0,10)  + "\n");
        buffer.append("================================" + "\n");
       // updateTotalCashLeft(context,String.valueOf(finalTotalCash));

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);


        Cursor readBankDetails =db.rawQuery("select * from BankDetails", null);
        Cursor readOtherPayment =db2.rawQuery("select * from OtherPayment", null);
        Cursor readDiscountName =db.rawQuery("select DISTINCT DiscountType from InvoiceReceiptItemFinalWDiscount", null);



        //   Cursor invoiceReceipItemFinal =db.rawQuery("select sum(DiscAmount),sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate='" + dateTime2 + "'", null);




        if (readBankDetails.getCount()!=0){
            while (readBankDetails.moveToNext()) {
                BankNameList.add(readBankDetails.getString(1));
            }
        }

        if (readOtherPayment.getCount()!=0){
            while (readOtherPayment.moveToNext()){
                OtherPaymentList.add(readOtherPayment.getString(1));
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
                                    TotalSpecialDiscount+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
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
        Double finalBankTransAmount=0.00;

        buffer.append("\n");
        for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='CreditCard' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheck.getCount()!=0) {
                while(invoiceReceiptTotalCheck.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheck.getString(0);
                    String TransactionAmount=invoiceReceiptTotalCheck.getString(5);
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

            //checking multiplePayment

            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                    //String TransactionAmount=invoiceReceiptTotalCheckM.getString(5);
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

            //end checking




        }
        buffer.append("TOTAL:CREDITCARD"+((String.format("%6d",creditCardQty))+"        ").substring(0,6) + ("   " + String.format("%7.2f",creditCardAmount)+"           ").substring(0,10)  + "\n");






        buffer.append("================================" + "\n");
//            buffer.append("           DEBIT CARD           " + "\n");
//            buffer.append("--------------------------------" + "\n");

        for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            Double FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='DebitCard' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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

            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                    //String TransactionAmount=invoiceReceiptTotalCheckM.getString(5);
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


//             buffer.append("--------------------------------" + "\n");
//            buffer.append("          OTHER PAYMENT         " + "\n");
//            buffer.append("--------------------------------" + "\n");
        for (int x=0;x<OtherPaymentList.size();x++){
            int Ctr=0;
            String FinalOtherPayment=OtherPaymentList.get(x);
            Log.e("OTHER PAYMENT",FinalOtherPayment);
            Double FinalOtherPaymentAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='"+FinalOtherPayment.trim()+"' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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
            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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







        // creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+creditCardAmount
        Log.e("total credit",String.valueOf(creditCardAmount));
        Log.e("total finalCashAmount",String.valueOf(finalCashAmount));
        Log.e("total totalRefund",String.valueOf(totalRefund));
        Log.e("total otherAmount",String.valueOf(otherPaymentAmount));
        Log.e("total debitCardAmount",String.valueOf(debitCardAmount));



        //   buffer.append("TOTAL COLLECTED "+(String.format("%6d",((creditCardQty+(Integer.parseInt(cashier_reading_item.getCashQty()))+totalRefundCounter+otherPaymentQty+debitCardQty)))+"          ").substring(0,6) + ("   " + String.format("%7.2f",Double.parseDouble(String.valueOf(creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+creditCardAmount)))+"           ").substring(0,10)  + "\n\n");

        buffer.append("BREAKDOWN OF SALES:                       " + "\n");

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



        buffer.append("Zero-Rated Sales      " + ("   "+String.format("%7.2f",zeroRatedSales)+"           ").substring(0,10)  + "\n");
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

            }




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
                        Log.e("ERROR",ex.getMessage());
                    }
                }
            }


        }



        PosOutputDB.close();

        //end of showing PLU in item

        buffer.append("--------------------------------" + "\n\n");



        // buffer.append("Service Charges                    " + (String.format("%7.2f",serviceCharge)+"           ").substring(0,11)  + "\n\n");
//            buffer.append("--------------------------------------" + "\n");
        buffer.append("GROSS SALES          " + ("   "+String.format("%7.2f",grossSales)+"           ").substring(0,10)  + "\n");
        buffer.append("Less VAT             " + ("   "+String.format("%7.2f",lessVat)+"           ").substring(0,10)  + "\n");


        buffer.append(("SPECIAL DISCOUNT"+"                               ").substring(0,22) + ("   "+String.format("%7.2f",TotalSpecialDiscount)+"           ").substring(0,10)  + "\n");
        buffer.append(("NORMAL DISCOUNT"+"                                ").substring(0,22) + ("   "+String.format("%7.2f",TotalNormalDiscount)+"           ").substring(0,10)  + "\n");




        if (vatIndicator.getVatIndicator()==0){

            buffer.append("NVAT Amount          " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }
        else{

            buffer.append("VAT Amount            " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }


        buffer.append("--------------------------------" + "\n");

        totalNetSales=grossSales-lessVat-TotalSpecialDiscount-TotalNormalDiscount-totalTaxAmount;
        // totalNetSales=grossSales-TotalSpecialDiscount-TotalNormalDiscount;


        buffer.append("NET SALES             " + ("   "+String.format("%7.2f",totalNetSales)+"           ").substring(0,10)  + "\n\n");
        buffer.append("RESET COUNTER : (" +EndBalanceCtr + ")\n");
        system_final_date systemDate= new system_final_date();
        systemDate.insertDate(context.getApplicationContext());
        SQLiteDatabase TransactionNumber= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
//        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);

        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'", null);
        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        if (StartTrans.getCount()!=0){
            StartTrans.moveToFirst();

            buffer.append("START TRANSACTION         " + (String.format(String.valueOf(Integer.parseInt(StartTrans.getString(0)))) + "           ").substring(0, 10) + "\n");
            EndTrans.moveToLast();

            buffer.append("END TRANSACTION           " + (String.format(String.valueOf(Integer.parseInt(transactionNumber)) )+ "           ").substring(0, 10) + "\n");
            // buffer.append("END TRANSACTION                    " + (Integer.parseInt(transactionNumber) + "           ").substring(0, 11) + "\n");

        }
        TransactionNumber.close();


        buffer.append(("Accumulated Grand Total: "+"              ").substring(0,25)+"\n"+
                ("                " + EndBalance+"      ").substring(0,32)+"\n\n");
        buffer.append("--------------------------------" + "\n");
        buffer.append(HeaderFooterClass.getFooterText()+"\n");
        TransactionNumber.close();

        db.close();
        db2.close();




        try{


            printData=buffer.toString();
            return printData;




        }
        catch (Exception ex){


            Log.e("Error",ex.getMessage());
            return "";
        }

//
//
//
//        } catch (IOException ex) {
//            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
//        }




    }
    private void printfileViewing(Context context){

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
        cashier_reading_item.getReadingData(context.getApplicationContext());
        cashier_reading_item.getLastOR(context.getApplicationContext());
        //dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        ReadVatableSales(context.getApplicationContext());
        DecimalFormat form = new DecimalFormat("0.00");
        cashier_reading_item.resetCounter(context.getApplicationContext());
        ReadGrossSales(context.getApplicationContext());
        ReadRefund(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate = SysDate.getSystemDate();

        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        HeaderFooterClass.HeaderNote(context.getApplicationContext());
        HeaderFooterClass.FooterNote(context.getApplicationContext());







        resetCtrDec =((Double.parseDouble(cashier_reading_item.getBegBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);

        totalBal = Double.parseDouble(cashier_reading_item.getBegBal())-(resetCtrInt*end) ;

        DecimalFormat df = new DecimalFormat("0000000000000.00");
        StartBalanceCtr=String.format("%02d", resetCtrInt);
        // StartBalanceCtr=String.valueOf(resetCtrInt);
       // StartBalance=String.format("%016.2f", totalBal);
        StartBalance = df.format(totalBal);


        resetCtrDec =((Double.parseDouble(cashier_reading_item.getEndBal())/end));
        resetCtrInt = (int)Math.round(resetCtrDec);
        // resetCtrInt = Integer.parseInt(String.valueOf(resetCtrDec));
        totalBal = Double.parseDouble(cashier_reading_item.getEndBal())-(resetCtrInt*end) ;

        EndBalanceCtr=String.format("%02d", resetCtrInt);
        //EndBalanceCtr=form.format(resetCtrInt);
       // EndBalance=String.format("%016.2f", totalBal);
        EndBalance = df.format(totalBal);


        // totalBal = (10000000000.00)-(resetCtrInt*end) ;

        //cashier_reading_item.setEndBal(String.valueOf(totalBal));








        int modx=Integer.parseInt(or_trans_item.getTransactionNo());
        Log.e("modx",String.valueOf(modx));
        int mody=999999999;
        int resetCount = modx/mody;
        Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtr = String.format("%02d", resetCount);
        String formattedTrans =  String.valueOf(modx % mody);
        transactionNumber=formattedTrans;
        transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));
        readTotalCashLeft(context);




//        try {
//            findBT(context);
//            openBT(context);
//

//


        //note!
        StringBuffer buffer = new StringBuffer();

        buffer.append(HeaderFooterClass.getHeaderText()+ "\n");
//            buffer.append("SHF: " + "\t\t" + "POS: " + "\n");

        buffer.append("================================" + "\n");
        buffer.append("     X_READING  C O P Y             " + "\n");
        buffer.append("================================" + "\n");
        buffer.append( "TRANS#: "+formattedCtr+"-" +transactionNumber+ "\n");
        buffer.append("Business Date: " +  convertdate(FinalDate)+ "\n");
        buffer.append("Report ID: " + shift_active.getActiveUserID()+"\n");
        buffer.append("POS: " + shift_active.getPOSCounter() + "     " +"Shift: "+ shift_active.getShiftActive() +"\n");
        buffer.append("CASHIER: " + shift_active.getActiveUserName() + "\n\n\n");





        //Beg OR NO
        int modxFinalBegOR=Integer.parseInt(cashier_reading_item.getBegORNo());
        // Log.e("modx",String.valueOf(modx));
        long modyFinalBegOR=999999999999999L;
        long resetCountFinalBegOR = modxFinalBegOR/modyFinalBegOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalBegOR = String.format("%02d", resetCountFinalBegOR);
        String formattedFinalBegOR =  String.valueOf(modxFinalBegOR % modyFinalBegOR);
        String FinalBegOR=formattedFinalBegOR;
        FinalBegOR =String.format("%010d",Integer.parseInt(FinalBegOR));

        //Ending OR NO


        int modxFinalEndOR=Integer.parseInt(cashier_reading_item.getEndORNo());
        // Log.e("modx",String.valueOf(modx));
        long modyFinalEndOR=999999999999999L;
        long resetCountFinalEndOR = modxFinalEndOR/modyFinalEndOR;
        //Log.e("resetCt",String.valueOf(modx/mody));
        String formattedCtrFinalEndOR = String.format("%02d", resetCountFinalEndOR);
        String formattedFinalEndOR =  String.valueOf(modxFinalEndOR % modyFinalEndOR);
        String FinalEndOR=formattedFinalEndOR;
        FinalEndOR =String.format("%010d",Integer.parseInt(FinalEndOR));





//
//        buffer.append("Beginning Invoice No.: \n"+
//                ("                    " +formattedCtrFinalBegOR+"-" +FinalBegOR+"      ").substring(0,32) +"\n");
//        buffer.append("Ending Invoice No.: \n"+
//                ("                    " +formattedCtrFinalEndOR+"-" +FinalEndOR+"      ").substring(0,32) +"\n");


        //
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

        buffer.append("================================" + "\n");
        buffer.append("          TENDER REPORT         " + "\n");
        buffer.append("================================" + "\n");
        buffer.append("TENDER                   AMOUNT "+"\n");
        buffer.append("--------------------------------" + "\n");

//        if (cashFloatInIndicator==1) {
        buffer.append("CASH FLOAT      "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashFloatQty()))+"      ").substring(0,6) +  ("   " + String.format("%7.2f",cashInTotal) + "           ").substring(0, 10) + "\n");
        Log.d("XREPORT", "createTextfile: " + cashInTotal);
//        }
        buffer.append("CASH            "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"      ").substring(0,6)+("   " + String.format("%7.2f",finalCashAmount+cashBoxCash)+"           ").substring(0,10) + "\n");


        if (cashFloatInIndicator==2) {
            buffer.append("CASH PICKUP     "+(String.format("%6d",1)+"       -").substring(0,6) + ("  " + String.format("%7.2f",cashOutTotal*-1) + "           ").substring(0, 10) + "\n");
        }


        SQLiteDatabase RefundDb= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //  Cursor readRefundCtr =RefundDb.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where TransactionID='"+TransactionIDList.get(x)+"'", null);


        String startTranss =TransactionIDList.get(0);
        String endTranss=TransactionIDList.get(TransactionIDList.size()-1);
        Log.e("readrefund",String.valueOf(totalRefund));


        if (totalRefund!=0.00) {
            ;
            buffer.append("CASH REFUND     " + (String.format("%6d", Integer.parseInt(String.valueOf(totalRefundCounter))) + "        ").substring(0, 6) + ("   " + String.format("%7.2f", Double.parseDouble(String.valueOf(totalRefund))) + "           ").substring(0, 10) + "\n");

        }

        // ReadRefund();






        Double finalTotalCash = finalCashAmount-totalRefund+cashInTotal-cashOutTotal+cashBoxCash;
        buffer.append("TOTAL: CASH     "+(String.format("%6d",Integer.parseInt(cashier_reading_item.getCashQty()))+"        ").substring(0,6) + ("   " + String.format("%7.2f",finalTotalCash)+"           ").substring(0,10)  + "\n");
        buffer.append("================================" + "\n");
       // updateTotalCashLeft(context,String.valueOf(finalTotalCash));

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);


        Cursor readBankDetails =db.rawQuery("select * from BankDetails", null);
        Cursor readOtherPayment =db2.rawQuery("select * from OtherPayment", null);
        Cursor readDiscountName =db.rawQuery("select DISTINCT DiscountType from InvoiceReceiptItemFinalWDiscount", null);



        //   Cursor invoiceReceipItemFinal =db.rawQuery("select sum(DiscAmount),sum(OrderPriceTotal) from InvoiceReceiptTotal where TransDate='" + dateTime2 + "'", null);




        if (readBankDetails.getCount()!=0){
            while (readBankDetails.moveToNext()) {
                BankNameList.add(readBankDetails.getString(1));
            }
        }

        if (readOtherPayment.getCount()!=0){
            while (readOtherPayment.moveToNext()){
                OtherPaymentList.add(readOtherPayment.getString(1));
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
                                    TotalSpecialDiscount+=InvoiceReceiptItemFinalWDiscount.getDouble(1)*-1;
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
        Double finalBankTransAmount=0.00;

        buffer.append("\n");
        for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='CreditCard' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheck.getCount()!=0) {
                while(invoiceReceiptTotalCheck.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheck.getString(0);
                    String TransactionAmount=invoiceReceiptTotalCheck.getString(5);
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

            //checking multiplePayment

            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                    //String TransactionAmount=invoiceReceiptTotalCheckM.getString(5);
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

            //end checking




        }
        buffer.append("TOTAL:CREDITCARD"+((String.format("%6d",creditCardQty))+"        ").substring(0,6) + ("   " + String.format("%7.2f",creditCardAmount)+"           ").substring(0,10)  + "\n");






        buffer.append("================================" + "\n");
//            buffer.append("           DEBIT CARD           " + "\n");
//            buffer.append("--------------------------------" + "\n");

        for (int x=0;x<BankNameList.size();x++){
            int Ctr=0;
            String FinalBankName=BankNameList.get(x);
            Double FinalBankAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='DebitCard' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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

            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
            if (invoiceReceiptTotalCheckM.getCount()!=0) {
                while(invoiceReceiptTotalCheckM.moveToNext()){
                    String TransactionID=invoiceReceiptTotalCheckM.getString(0);
                    //String TransactionAmount=invoiceReceiptTotalCheckM.getString(5);
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


//             buffer.append("--------------------------------" + "\n");
//            buffer.append("          OTHER PAYMENT         " + "\n");
//            buffer.append("--------------------------------" + "\n");
        for (int x=0;x<OtherPaymentList.size();x++){
            int Ctr=0;
            String FinalOtherPayment=OtherPaymentList.get(x);
            Log.e("OTHER PAYMENT",FinalOtherPayment);
            Double FinalOtherPaymentAmount=0.00;
            Cursor invoiceReceiptTotalCheck = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='"+FinalOtherPayment.trim()+"' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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
            Cursor invoiceReceiptTotalCheckM = db.rawQuery("select * from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
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







        // creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+creditCardAmount
        Log.e("total credit",String.valueOf(creditCardAmount));
        Log.e("total finalCashAmount",String.valueOf(finalCashAmount));
        Log.e("total totalRefund",String.valueOf(totalRefund));
        Log.e("total otherAmount",String.valueOf(otherPaymentAmount));
        Log.e("total debitCardAmount",String.valueOf(debitCardAmount));



        //   buffer.append("TOTAL COLLECTED "+(String.format("%6d",((creditCardQty+(Integer.parseInt(cashier_reading_item.getCashQty()))+totalRefundCounter+otherPaymentQty+debitCardQty)))+"          ").substring(0,6) + ("   " + String.format("%7.2f",Double.parseDouble(String.valueOf(creditCardAmount+finalCashAmount-totalRefund+otherPaymentAmount+creditCardAmount)))+"           ").substring(0,10)  + "\n\n");

        buffer.append("BREAKDOWN OF SALES:                       " + "\n");

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


        buffer.append("Zero-Rated Sales      " + ("   "+String.format("%7.2f",zeroRatedSales)+"           ").substring(0,10)  + "\n");
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

            }




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
                        Log.e("ERROR",ex.getMessage());
                    }
                }
            }


        }



        PosOutputDB.close();

        //end of showing PLU in item

        buffer.append("--------------------------------" + "\n\n");



        // buffer.append("Service Charges                    " + (String.format("%7.2f",serviceCharge)+"           ").substring(0,11)  + "\n\n");
//            buffer.append("--------------------------------------" + "\n");
        buffer.append("GROSS SALES           " + ("   "+String.format("%7.2f",grossSales)+"           ").substring(0,10)  + "\n");
        buffer.append("Less VAT              " + ("   "+String.format("%7.2f",lessVat)+"           ").substring(0,10)  + "\n");


        buffer.append(("SPECIAL DISCOUNT"+"                               ").substring(0,22) + ("   "+String.format("%7.2f",TotalSpecialDiscount)+"           ").substring(0,10)  + "\n");
        buffer.append(("NORMAL DISCOUNT"+"                                ").substring(0,22) + ("   "+String.format("%7.2f",TotalNormalDiscount)+"           ").substring(0,10)  + "\n");





        if (vatIndicator.getVatIndicator()==0){

            buffer.append("NVAT Amount          " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }
        else{

            buffer.append("VAT Amount            " + ("   "+String.format("%7.2f",totalTaxAmount)+"           ").substring(0,10)  + "\n");
        }  buffer.append("--------------------------------" + "\n");

        totalNetSales=grossSales-lessVat-TotalSpecialDiscount-TotalNormalDiscount-totalTaxAmount;
        // totalNetSales=grossSales-TotalSpecialDiscount-TotalNormalDiscount;


        buffer.append("NET SALES             " + ("   "+String.format("%7.2f",totalNetSales)+"           ").substring(0,10)  + "\n\n");
        buffer.append("RESET COUNTER : (" +EndBalanceCtr + ")\n");
        system_final_date systemDate= new system_final_date();
        systemDate.insertDate(context.getApplicationContext());
        SQLiteDatabase TransactionNumber= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
//        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);

        Cursor StartTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'", null);
        Cursor EndTrans =TransactionNumber.rawQuery("select * from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        if (StartTrans.getCount()!=0){
            StartTrans.moveToFirst();

            buffer.append("START TRANSACTION         " + (String.format(String.valueOf(Integer.parseInt(StartTrans.getString(0)))) + "           ").substring(0, 10) + "\n");
            EndTrans.moveToLast();

            buffer.append("END TRANSACTION           " + (String.format(String.valueOf(Integer.parseInt(transactionNumber)) )+ "           ").substring(0, 10) + "\n");
            // buffer.append("END TRANSACTION                    " + (Integer.parseInt(transactionNumber) + "           ").substring(0, 11) + "\n");

        }
        TransactionNumber.close();


        buffer.append(("Accumulated Grand Total: "+"              ").substring(0,25)+"\n"+
                ("                " + EndBalance+"      ").substring(0,32)+"\n\n");
        buffer.append("--------------------------------" + "\n");
        buffer.append(HeaderFooterClass.getFooterText()+"\n");
        TransactionNumber.close();

        db.close();
        db2.close();




        try{


            printData=buffer.toString();
            //return printData;
            PrinterSettings.OnlinePrinter(printData,1,0,1);




        }
        catch (Exception ex){


            Log.e("Error",ex.getMessage());
            //return "";
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
    //SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
    //Simple dateOnly = new SimpleDateFormat("MM/dd/yyyy");

    //DateFormat timeOnly = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



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
    Double cashInTotal=0.00;
    Double cashOutTotal=0.00;

    //note!!
    private void ReadFloatIN(Context context){

        shift_active.getShiftingTable(context.getApplicationContext());
        shift_active.getAccountInfo(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        ReadCashBefore(context);
        ReadCashInMultipleBefore(context);
//        ReadFloatOutBefore(context);

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadFloatIN =db.rawQuery("select * from CashInOut where TransType='FLOAT IN'", null);
        Cursor cursorReadFloatINQty =db.rawQuery("select count(TransactionID) from CashInOut where TransType='FLOAT IN' and CashierID='"+shift_active.getActiveUserID()+"'and TransDate='"+ FinalDate+"'", null);
      //  Cursor cursorReadTotalLeftCash =db.rawQuery("select count(TransactionID) from CashInOut where TransType='FLOAT IN' and CashierID='"+shift_active.getActiveUserID()+"'and TransDate='"+ FinalDate+"'", null);

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




    private void ReadFloatOut(Context context){

        shift_active.getShiftingTable(context.getApplicationContext());
        shift_active.getAccountInfo(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadFloatIN =db.rawQuery("select * from CashInOut where TransType='FLOAT OUT'", null);
        Cursor cursorReadFloatINQty =db.rawQuery("select count(TransactionID) from CashInOut where TransType='FLOAT OUT' and CashierID='"+shift_active.getActiveUserID()+"'and TransDate='"+ FinalDate+"'", null);
        if (cursorReadFloatINQty.getCount()!=0){
            while (cursorReadFloatINQty.moveToNext()){
                cashier_reading_item.setCashFloatOutQty(cursorReadFloatINQty.getString(0));
            }
        }
        else{
            cashier_reading_item.setCashFloatOutQty("0");
        }

        if (cursorReadFloatIN.getCount()!=0){


            cashFloatInIndicator=2;

            while (cursorReadFloatIN.moveToNext()){

                cash1000=Integer.parseInt(cursorReadFloatIN.getString(1));
                cash500=Integer.parseInt(cursorReadFloatIN.getString(2));
                cash200=Integer.parseInt(cursorReadFloatIN.getString(3));
                cash100=Integer.parseInt(cursorReadFloatIN.getString(4));
                cash50=Integer.parseInt(cursorReadFloatIN.getString(5));
                cash20=Integer.parseInt(cursorReadFloatIN.getString(6));
                cash10=Integer.parseInt(cursorReadFloatIN.getString(7));
                cash5=Integer.parseInt(cursorReadFloatIN.getString(8));
                cash1=Integer.parseInt(cursorReadFloatIN.getString(9));
                cashCents=Integer.parseInt(cursorReadFloatIN.getString(10));

            }

            cashOutTotal=cash1000*1000 + cash500*500 + cash200*200 + cash100*100 + cash50*50 +cash20*20 +cash10*10 + cash5*5 + cash1*1 + cashCents*.25;

            // Toast.makeText(context, "GOOD", Toast.LENGTH_SHORT).show();
            //show alertdialog xread






        }
        else{
            cashFloatInIndicator=0;
        }
        db.close();

    }
//    Double cashoutTotalBefore=0.00;
//    private void ReadFloatOutBefore(Context context){
//
//        shift_active.getShiftingTable(context.getApplicationContext());
//        shift_active.getAccountInfo(context.getApplicationContext());
//        system_final_date SysDate = new system_final_date();
//        SysDate.insertDate(context.getApplicationContext());
//        FinalDate=SysDate.getSystemDate();
//
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor cursorReadFloatIN =db.rawQuery("select * from CashInOut where TransType='FLOAT OUT'", null);
//        Cursor cursorReadFloatINQty =db.rawQuery("select count(TransactionID) from CashInOut where TransType='FLOAT OUT' and TransDate='"+ FinalDate+"'", null);
//        if (cursorReadFloatINQty.getCount()!=0){
//            while (cursorReadFloatINQty.moveToNext()){
//                cashier_reading_item.setCashFloatOutQty(cursorReadFloatINQty.getString(0));
//            }
//        }
//        else{
//            cashier_reading_item.setCashFloatOutQty("0");
//        }
//
//        if (cursorReadFloatIN.getCount()!=0){
//
//
//
//
//            while (cursorReadFloatIN.moveToNext()){
//                cash1000=Integer.parseInt(cursorReadFloatIN.getString(1));
//                cash500=Integer.parseInt(cursorReadFloatIN.getString(2));
//                cash200=Integer.parseInt(cursorReadFloatIN.getString(3));
//                cash100=Integer.parseInt(cursorReadFloatIN.getString(4));
//                cash50=Integer.parseInt(cursorReadFloatIN.getString(5));
//                cash20=Integer.parseInt(cursorReadFloatIN.getString(6));
//                cash10=Integer.parseInt(cursorReadFloatIN.getString(7));
//                cash5=Integer.parseInt(cursorReadFloatIN.getString(8));
//                cash1=Integer.parseInt(cursorReadFloatIN.getString(9));
//                cashCents=Integer.parseInt(cursorReadFloatIN.getString(10));
//
//            }
//
//            cashoutTotalBefore=cash1000*1000 + cash500*500 + cash200*200 + cash100*100 + cash50*50 +cash20*20 +cash10*10 + cash5*5 + cash1*1 + cashCents*.25;
//
//            // Toast.makeText(context, "GOOD", Toast.LENGTH_SHORT).show();
//            //show alertdialog xread
//
//
//
//
//
//
//        }
//        else{
//
//        }
//        db.close();
//
//    }
    int totalRefundCounter;
    private void ReadRefund(Context context){
       // ReadGrossSales(context);
        SQLiteDatabase RefundDb= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
     //   Log.e("FINAL REFUND TRANS ",TransactionIDList.get(0) + "  " + TransactionIDList.get(TransactionIDList.size()-1));
        shift_active shift_active = new shift_active();
        shift_active.getAccountInfo(context.getApplicationContext());
        shift_active.getShiftingTable(context.getApplicationContext());
        Cursor readRefund =RefundDb.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='refund' and TransactionID between'"+TransactionIDList.get(0)+"'and '"+TransactionIDList.get(TransactionIDList.size()-1)+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
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

    private void ReadTransactionIDInDB(Context context){

    }



    private String date;
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

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
       Cursor invoiceReceiptTotal =db.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='" + FinalDate + "'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
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


    //    Double finalFloatBefore=0.00;
//    private void ReadFinalFloatBefore(Context context){
//        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
//        shift_active.getAccountInfo(context.getApplicationContext());
//        dateTime.generateDateTime();
//        system_final_date SysDate = new system_final_date();
//        SysDate.insertDate(context.getApplicationContext());
//        FinalDate=SysDate.getSystemDate();
//        ReadCashBefore(context);
//        ReadFloatOutBefore(context);
//        ReadCashInMultipleBefore(context);
//        // ReadRefund(context);
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor cursorReadTotalCash =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Float IN' and TransDate='"+FinalDate+"'and shiftActive<'"+shift_active.getShiftActive()+"'", null);
//             if (cursorReadTotalCash.getCount()!=0){
//            cursorReadTotalCash.moveToFirst();
//            // Toast.makeText(context, cursorReadTotalCash.getString(0), Toast.LENGTH_SHORT).show();
//            if (cursorReadTotalCash.getString(0)==null){
//                Log.e("FinalCashAmount","null");
//                finalFloatBefore=0.00 +  + finalCashAmountBefore - cashoutTotalBefore;
//            }
//            else{
//                Log.e("FinalCashAmount",cursorReadTotalCash.getString(0));
//                finalFloatBefore = Double.parseDouble(cursorReadTotalCash.getString(0))+ + finalCashAmountBefore - cashoutTotalBefore;
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
//
//        db.close();
//    }




    Double cashBoxCash=0.00;
    private void readTotalCashLeft(Context context){
//  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        ReadCashBefore(context);

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadTotalCash =db.rawQuery("select * from CashBox where id=1", null);
             if (cursorReadTotalCash.getCount()!=0){
            cursorReadTotalCash.moveToFirst();
            // Toast.makeText(context, cursorReadTotalCash.getString(0), Toast.LENGTH_SHORT).show();
            if (cursorReadTotalCash.getString(0)==null){
                Log.e("ReadCashLeft","null");
                cashBoxCash = 0.00;

             //   finalFloatBefore=0.00 +  + finalCashAmountBefore - cashoutTotalBefore;
            }
            else{
                Log.e("ReadCashLeft",cursorReadTotalCash.getString(1));
                cashBoxCash = Double.parseDouble(cursorReadTotalCash.getString(1));
                //finalFloatBefore = Double.parseDouble(cursorReadTotalCash.getString(0))+ + finalCashAmountBefore - cashoutTotalBefore;
            }

        }

        db.close();
    }
    private void updateTotalCashLeft(Context context,String cashLeft){
//  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();


        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor checksql = db.rawQuery("select * from CashBox where id=1",null);
        if (checksql.getCount()!=0){

           // if (checksql.moveToNext()){

          //  Double cashbefore=Double.parseDouble(checksql.getString(1));
            Double cashNow=Double.parseDouble(cashLeft);
            //Double finalCash = cashbefore + cashNow;



            String strsql = "UPDATE CashBox set  total_cash='"+String.valueOf(cashNow)+"' where id=1";
            db.execSQL(strsql);
            db.close();
           // }
        }
        else{
            DatabaseHandler dbhandler = new DatabaseHandler(context);
            dbhandler.insertCashBox(1,cashLeft);
        }

    }



    String transaction_Date;

    private void ReadCash(Context context){
      //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        // ReadRefund(context);
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadTotalCash =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
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

//    Double finalFloatBefore=0.00;
//    private void ReadFinalFloatBefore(Context context){
//        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
//        shift_active.getAccountInfo(context.getApplicationContext());
//        dateTime.generateDateTime();
//        system_final_date SysDate = new system_final_date();
//        SysDate.insertDate(context.getApplicationContext());
//        FinalDate=SysDate.getSystemDate();
//        ReadCashBefore(context);
//        ReadFloatOutBefore(context);
//        ReadCashInMultipleBefore(context);
//        // ReadRefund(context);
//        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//        Cursor cursorReadTotalCash =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Float IN' and TransDate='"+FinalDate+"'and shiftActive<'"+shift_active.getShiftActive()+"'", null);
//             if (cursorReadTotalCash.getCount()!=0){
//            cursorReadTotalCash.moveToFirst();
//            // Toast.makeText(context, cursorReadTotalCash.getString(0), Toast.LENGTH_SHORT).show();
//            if (cursorReadTotalCash.getString(0)==null){
//                Log.e("FinalCashAmount","null");
//                finalFloatBefore=0.00 +  + finalCashAmountBefore - cashoutTotalBefore;
//            }
//            else{
//                Log.e("FinalCashAmount",cursorReadTotalCash.getString(0));
//                finalFloatBefore = Double.parseDouble(cursorReadTotalCash.getString(0))+ + finalCashAmountBefore - cashoutTotalBefore;
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
//
//        db.close();
//    }

    ArrayList<String>TransactionList=new ArrayList<String>();
    double MultipleBankCreditAmount=0.00;
    int MultipleCreditQty;
    private void ReadCashInMultiple(Context context){
        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        // ReadRefund(context);
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorGetTransactionID =db.rawQuery("select TransactionID from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        if (cursorGetTransactionID.getCount()!=0){
            while (cursorGetTransactionID.moveToNext()){
                TransactionList.add(cursorGetTransactionID.getString(0));
            }
            for (int x=0;x<TransactionList.size();x++){

                //for cash

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

                //end cash
                //Multiple credit card

//                Cursor cursorReadTotalTotalCardAmount =db.rawQuery("select sum(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CreditCard' and TransactionID='"+TransactionList.get(x)+"'", null);
//                if (cursorReadTotalTotalCardAmount.getCount()!=0){
//                    while (cursorReadTotalTotalCardAmount.moveToNext()){
//                        MultipleBankCreditAmount=MultipleBankCreditAmount+cursorReadTotalTotalCardAmount.getDouble(0);
//                    }
//                }
//
//                Cursor cursorReadTotalCardQty =db.rawQuery("select count(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CASH' and TransactionID='"+TransactionList.get(x)+"'", null);
//                if (cursorReadTotalCardQty.getCount()!=0){
//                    while (cursorReadTotalCardQty.moveToNext()){
//                        cashier_reading_item.setCashQty(String.valueOf(Integer.parseInt(cashier_reading_item.getCashQty())+Integer.parseInt(cursorReadTotalCashQty.getString(0))));
//
//                    }
//                }
               // end multiple



            }


        }



        db.close();
    }



//for getting cash total before the shift
    Double finalCashAmountBefore=0.00;
    private void ReadCashBefore(Context context){
        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        // ReadRefund(context);
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadTotalCash =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'and shiftActive<'"+(shift_active.getShiftActive())+"'", null);
        Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='Cash' and TransDate='"+FinalDate+"'and shiftActive<'"+(shift_active.getShiftActive())+"'", null);
        if (cursorReadTotalCash.getCount()!=0){
            cursorReadTotalCash.moveToFirst();
            // Toast.makeText(context, cursorReadTotalCash.getString(0), Toast.LENGTH_SHORT).show();
            if (cursorReadTotalCash.getString(0)==null){
                Log.e("FinalCashAmount","null");
                finalCashAmountBefore=0.00;
            }
            else{
                Log.e("FinalCashAmount",cursorReadTotalCash.getString(0));
                finalCashAmountBefore = Double.parseDouble(cursorReadTotalCash.getString(0));
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
             //   cashier_reading_item.setCashQty(cursorReadTotalCashQty.getString(0));
            }

        }
        else{
            //cashier_reading_item.setCashQty("0");
        }
        db.close();
    }
    private void ReadCashInMultipleBefore(Context context){
        //  dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        TransactionList.clear();
        shift_active.getAccountInfo(context.getApplicationContext());
        dateTime.generateDateTime();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        // ReadRefund(context);
        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorGetTransactionID =db.rawQuery("select TransactionID from InvoiceReceiptTotal where typeOfPayment='Multiple' and TransDate='"+FinalDate+"'and  shiftActive<'"+(shift_active.getShiftActive())+"'", null);
        if (cursorGetTransactionID.getCount()!=0){
            while (cursorGetTransactionID.moveToNext()){
                TransactionList.add(cursorGetTransactionID.getString(0));
            }
            for (int x=0;x<TransactionList.size();x++){

                //for cash

                Cursor cursorReadTotalCashAmount =db.rawQuery("select sum(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CASH' and TransactionID='"+TransactionList.get(x)+"'", null);
                if (cursorReadTotalCashAmount.getCount()!=0){
                    while (cursorReadTotalCashAmount.moveToNext()){
                        finalCashAmountBefore=finalCashAmountBefore+cursorReadTotalCashAmount.getDouble(0);
                    }
                }
                Cursor cursorReadTotalCashQty =db.rawQuery("select count(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CASH' and TransactionID='"+TransactionList.get(x)+"'", null);
                if (cursorReadTotalCashQty.getCount()!=0){
                    while (cursorReadTotalCashQty.moveToNext()){
                      //  cashier_reading_item.setCashQty(String.valueOf(Integer.parseInt(cashier_reading_item.getCashQty())+Integer.parseInt(cursorReadTotalCashQty.getString(0))));

                    }
                }

                //end cash
                //Multiple credit card

//                Cursor cursorReadTotalTotalCardAmount =db.rawQuery("select sum(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CreditCard' and TransactionID='"+TransactionList.get(x)+"'", null);
//                if (cursorReadTotalTotalCardAmount.getCount()!=0){
//                    while (cursorReadTotalTotalCardAmount.moveToNext()){
//                        MultipleBankCreditAmount=MultipleBankCreditAmount+cursorReadTotalTotalCardAmount.getDouble(0);
//                    }
//                }
//
//                Cursor cursorReadTotalCardQty =db.rawQuery("select count(amount) from InvoiceMultiplePaymentFinal where typeOfPayment='CASH' and TransactionID='"+TransactionList.get(x)+"'", null);
//                if (cursorReadTotalCardQty.getCount()!=0){
//                    while (cursorReadTotalCardQty.moveToNext()){
//                        cashier_reading_item.setCashQty(String.valueOf(Integer.parseInt(cashier_reading_item.getCashQty())+Integer.parseInt(cursorReadTotalCashQty.getString(0))));
//
//                    }
//                }
                // end multiple



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


        Cursor cursorReadOrderPriceTotal =db.rawQuery("select sum(TotalVatableSales),sum(TotalVatAmount),sum(TotalVatExempt),sum(TotalZeroRatedSales),sum(TotalLessVat) from FinalTransactionReport where TransDate='"+FinalDate+"'and TransUserID='"+shift_active.getActiveUserID()+"'and TransShift='"+shift_active.getShiftActive()+"'", null);
      //  Cursor cursorReadOrderPriceTotalDiscount =db.rawQuery("select sum(DiscAmount) from FinalTransactionReport where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'and shiftActive='"+shift_active.getShiftActive()+"'", null);
        // Cursor cursorReadTotalCashQty =db.rawQuery("select count(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+dateTime2+"'and TransUser='"+shift_active.getActiveUserID()+"'", null);
        if (cursorReadOrderPriceTotal.getCount()!=0){
            cursorReadOrderPriceTotal.moveToFirst();
           // Toast.makeText(context, cursorReadOrderPriceTotal.getString(0), Toast.LENGTH_SHORT).show();
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



    public void updateReading(Context context){
       // dateTime2=new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        //update ENDING OR,ENDING BAL,ENDING TRANSACTION
     SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2= context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        getActiveUser(context);
        shift_active.getAccountInfo(context.getApplicationContext());
        shift_active.getShiftingTable(context.getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(context.getApplicationContext());
        FinalDate=SysDate.getSystemDate();


        Cursor cursorReadingOR = db.rawQuery("select * from OfficialReceipt", null);
        Cursor cursorReadingBalance =db.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate='"+FinalDate+"'", null);
        if (cursorReadingBalance.getCount()!=0){
          while (cursorReadingBalance.moveToNext()){
              if (cursorReadingBalance.getString(0)==null){
                  double dataInt = Double.parseDouble("0.00");
                  // endBal = String.format("%013.2f", 0);
                  endBal=String.format("%013.2f", dataInt);
              }
              else{
                  endBal=cursorReadingBalance.getString(0);
              }
//              cursorReadingBalance.moveToFirst();
////              endBal=cursorReadingBalance.getString(0);

          }

        }
        if (cursorReadingBalance.getCount()==0){
            double dataInt = Double.parseDouble("0.00");
            // endBal = String.format("%013.2f", 0);
            endBal=String.valueOf(dataInt);
        }
       if (cursorReadingOR.getCount()!=0){
           cursorReadingOR.moveToLast();
           endOR=cursorReadingOR.getString(0);
       }

       if (cursorReadingOR.getCount()==0){
           int origRefNumber = 0;
           String formatted = String.format("%010d", origRefNumber);
           endOR=formatted;
       }

//        cursorReadingBalance.moveToFirst();
//

        or_trans_item = new OR_TRANS_ITEM();
        readReferenceNumber(context); //read Transaction before clicking xread
        DatabaseHandler databaseHandler=new DatabaseHandler(context);


        databaseHandler.insertInvoiceReceiptTotal(or_trans_item.getTransactionNo(),"","",""
                ,"","","","","XREAD",FinalDate,timeOnly.format(currentDate.getTime()),ActiveUser,shift_active.getShiftActive());
      //  readReferenceNumber(context); //read Transaction after clicking xread




        endTrans=or_trans_item.getTransactionNo();




        String strsql = "UPDATE ReadingTable set  readingEndOR='"+endOR+"',readingEndBal='"+endBal+"',readingEndTrans='"+endTrans+"'  where readingID=1";
        db2.execSQL(strsql);


//        // to be qoute

           // activateCode=1;
        if (activateCode==1){
            String updateStatus = "UPDATE ReadingIndicator set indicatorStatus=1 where indicatorID=1"; // ready for reading
            db2.execSQL(updateStatus);

            int shftActv = Integer.valueOf(shift_active.getShiftActive())+1;

            String updateShifting = "UPDATE ShiftingTable set  shiftActive='"+shftActv+"',shiftActiveUser='' where shiftID=1"; // change shift
            db2.execSQL(updateShifting);


            String updateCashIn= "delete from CashInOut";
            db.execSQL(updateCashIn);

            String updateTerminalStatus = "update TerminalStatus set StatusID=1,Status=0 where StatusID=1"; // change shift
            db.execSQL(updateTerminalStatus);


            Cursor cursorReadingTable = db2.rawQuery("select * from ReadingTable", null);
            if (cursorReadingTable.getCount()!=0){
                while(cursorReadingTable.moveToNext()) {


                    settingsDB settingsDB = new settingsDB(context);
                    settingsDB.insertReadTableFinal(
                            cursorReadingTable.getString(0),
                            cursorReadingTable.getString(1),
                            cursorReadingTable.getString(2),
                            cursorReadingTable.getString(3),
                            cursorReadingTable.getString(4),
                            cursorReadingTable.getString(5),
                            cursorReadingTable.getString(6),
                            cursorReadingTable.getString(7),
                            cursorReadingTable.getString(8),
                            cursorReadingTable.getString(9),
                            cursorReadingTable.getString(10),
                            cursorReadingTable.getString(11)


                    );
                }


            }



            //clear the readingtable
            String deleteReadTable = "delete from ReadingTable"; // change shift
            db2.execSQL(deleteReadTable);



        }












        //copying to final Read Table







        db.close();
        db2.close();


    }
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
