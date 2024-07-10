package com.abztrakinc.ABZPOS.CASHIER;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.system_final_date;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class cashier_reading_item {

    Context context;
    public String getBegORNo() {
        return BegORNo;
    }

    public void setBegORNo(String begORNo) {
        BegORNo = begORNo;
    }

    public String getEndORNo() {
        return EndORNo;
    }

    public void setEndORNo(String endORNo) {
        EndORNo = endORNo;
    }

    public String getBegBal() {
        return BegBal;
    }

    public void setBegBal(String begBal) {
        BegBal = begBal;
    }

    public String getEndBal() {
        return EndBal;
    }

    public void setEndBal(String endBal) {
        EndBal = endBal;
    }

    public String getCashFloat() {
        return CashFloat;
    }

    public void setCashFloat(String cashFloat) {
        CashFloat = cashFloat;
    }

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
    }

    public String getCashRefund() {
        return CashRefund;
    }

    public void setCashRefund(String cashRefund) {
        CashRefund = cashRefund;
    }

    private String BegORNo,EndORNo,BegBal,EndBal,CashFloat,Cash,CashRefund;
    public int getResetCounter() {
        return resetCounter;
    }

    public void setResetCounter(int resetCounter) {
        this.resetCounter = resetCounter;
    }

    int resetCounter;


    private String CashFloatQty;


    private String CashQty;


    public String getCashFloatOut() {
        return CashFloatOut;
    }

    public void setCashFloatOut(String cashFloatOut) {
        CashFloatOut = cashFloatOut;
    }

    private String CashFloatOut;

    public String getCashFloatOutQty() {
        return CashFloatOutQty;
    }

    public void setCashFloatOutQty(String cashFloatOutQty) {
        CashFloatOutQty = cashFloatOutQty;
    }

    private String CashFloatOutQty;

    public String getZreadingFloatIndicator() {
        return ZreadingFloatIndicator;
    }

    public void setZreadingFloatIndicator(String zreadingFloatIndicator) {
        ZreadingFloatIndicator = zreadingFloatIndicator;
    }

    private String ZreadingFloatIndicator;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getCashFloatQty() {
        return CashFloatQty;
    }

    public void setCashFloatQty(String cashFloatQty) {
        CashFloatQty = cashFloatQty;
    }

    public String getCashQty() {
        return CashQty;
    }

    public void setCashQty(String cashQty) {
        CashQty = cashQty;
    }

    public String getCashPickUpQty() {
        return CashPickUpQty;
    }

    public void setCashPickUpQty(String cashPickUpQty) {
        CashPickUpQty = cashPickUpQty;
    }

    private String CashPickUpQty;

    Double FinalBeginningBalance=0.00;
    Double FinalEndingBalance=0.00;

    public int getZcounter() {
        return Zcounter;
    }

    public void setZcounter(int zcounter) {
        Zcounter = zcounter;
    }

    private int Zcounter;



    public void getReadingData(Context context){

        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(context);
        system_final_date systemDate = new system_final_date();
        systemDate.insertDate(context.getApplicationContext());
        ArrayList<String> ListOfOR =new ArrayList<String>();


        SQLiteDatabase db = context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from ReadingTable where readingShift='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
        if(cursor.getCount()!=0){
            Log.e("getReadingData","IF CONDITION");
            while (cursor.moveToNext()){
                if (cursor.getString(4)==null){

                    //get all the transaction number from date today
                    Cursor cursor2 = db2.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
                    if (cursor2.getCount()!=0){
                        while(cursor2.moveToNext()) {

                            Cursor cursor3 = db2.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'

                            if (cursor3.getCount()!=0){

                                while (cursor3.moveToNext()){
                                    Log.e("OR NO",cursor3.getString(0));

                                    ListOfOR.add(cursor3.getString(0));
                                }


                            }
                            else{
                                // setBegORNo(String.format("%09d",312));
                                Log.e("Transaction NO",cursor2.getString(0));
                                // ListOfOR.add("0");
                            }


                        }
                        if (ListOfOR.size()!=0) {
//                    for(int x=0; x<ListOfOR.size();x++){
                            Log.e("BegOR",ListOfOR.get(0));

//                    }
                            Collections.sort(ListOfOR);
                            setBegORNo(ListOfOR.get(0));
                            // setEndORNo(ListOfOR.get(ListOfOR.size() - 1));
//                    setEndORNo(ListOfOR.get(2));
                        }
                        else{
                            Log.e("ELSE","ELSE");
                            setBegORNo(String.format("%010d",0));
                        }

                    }
                    else{
                        setBegORNo(String.format("%010d",0));
                    }






                }

                if (cursor.getString(4)!=null){

                    Cursor cursor2 = db2.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
                    if (cursor2.getCount()!=0){
                        while(cursor2.moveToNext()) {

                            Cursor cursor3 = db2.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'
                            if (cursor3.getCount()!=0){
                                while (cursor3.moveToNext()){
                                    ListOfOR.add(cursor3.getString(0));
                                }
                                Collections.sort(ListOfOR);
                                setBegORNo(ListOfOR.get(0));

                            }
                            else{
                                setBegORNo(String.format("%010d", 0));
                            }
                        }


                    }
                    else{
                        setBegORNo(String.format("%010d", 0));
                    }



                }


            }
            db2.close();

        }

        else{
            Log.e("getReadingData","ELSE CONDITION");
//            getLastOR(context.getApplicationContext());
//               // Log.e("X READ BEGOR",getEndORNo());
//            setBegORNo(getEndORNo());
//            Log.e("NULL"," else else NOT NULL");
//           // String data2="0";
//          //  double dataInt2 = Double.parseDouble(data2);
//            //setBegBal(String.format("%013.2f", dataInt2));
//            //Log.e("getReadingData","EMPTY");

            Cursor cursor2 = db2.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
            if (cursor2.getCount()!=0){
                while(cursor2.moveToNext()) {

                    Cursor cursor3 = db2.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'

                    if (cursor3.getCount()!=0){

                        while (cursor3.moveToNext()){
                            Log.e("OR NO",cursor3.getString(0));
                            ListOfOR.add(cursor3.getString(0));
                        }


                    }
                    else{
                       // setBegORNo(String.format("%09d",312));
                        Log.e("Transaction NO",cursor2.getString(0));
                       // ListOfOR.add("0");
                    }


                }
                if (ListOfOR.size()!=0) {
//                    for(int x=0; x<ListOfOR.size();x++){
                        Log.e("BegOR",ListOfOR.get(0));

//                    }
                    Collections.sort(ListOfOR);
                    setBegORNo(ListOfOR.get(0));
                   // setEndORNo(ListOfOR.get(ListOfOR.size() - 1));
//                    setEndORNo(ListOfOR.get(2));
                }
                else{
                    Log.e("ELSE","ELSE");
                    setBegORNo(String.format("%010d",123));
                }

            }
            else{
                setBegORNo(String.format("%010d",321));
            }







        }
        db.close();




        //read Beginning balance

        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

        Cursor cursorReadingBeginningBalance;
//        if (Integer.parseInt(shift_active.getShiftActive())>1){
//            cursorReadingBeginningBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate!='"+systemDate.getSystemDate()+"' and shiftActive<'"+shift_active.getShiftActive()+"'", null);
//        }
//        else{
//            cursorReadingBeginningBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate!='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
//
//        }
        cursorReadingBeginningBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate!='"+systemDate.getSystemDate()+"'",null);
       // cursorReadingBeginningBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where ORTrans<'"+getBegORNo()+"'", null);
        if (cursorReadingBeginningBalance.getCount()!=0){

            while(cursorReadingBeginningBalance.moveToNext()){

                Log.e("System Date",systemDate.getSystemDate());
              //  Log.e("Begin Balance",cursorReadingBeginningBalance.getString(0));


                if (cursorReadingBeginningBalance.getString(0)==null){
                    FinalBeginningBalance=0.00;
                    Log.e("Beginning Balance","if");

                }
                else{
                  //  FinalBeginningBalance+=Double.parseDouble(cursorReadingBeginningBalance.getString(0));
                    FinalBeginningBalance+=(cursorReadingBeginningBalance.getDouble(0));
                    Log.e("Beginning Balance","else " + String.valueOf(FinalBeginningBalance));
                }

            }
        }
        else{
            FinalBeginningBalance=0.00;
            Log.e("Beginning Balance", "else else ");
            //setEndORNo(String.format("%010d", 0));
        }


        double FinalAmount =FinalBeginningBalance;
        DecimalFormat format = new DecimalFormat("000000000000.00");
       // format.setRoundingMode(RoundingMode.UP);
        String formatted = format.format(FinalAmount);
        double dataInt2 = (FinalAmount);
        //setBegBal(String.format("%013.2f", dataInt2));
        DecimalFormat df = new DecimalFormat("0000000000000.00");
        Log.e("TAG", "tangina: "+dataInt2 );
        setBegBal(df.format(dataInt2));




        //end of reading Beginning balance

        //read Ending balance

       // Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice'", null);
        Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport", null);
       // Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where typeOfTransaction='invoice'"
        if (cursorReadingEndingBalance.getCount()!=0){

            while(cursorReadingEndingBalance.moveToNext()){
                if (cursorReadingEndingBalance.getString(0)==null){
                    FinalBeginningBalance+=0.00;
                }
                else{
                    FinalEndingBalance+=(cursorReadingEndingBalance.getDouble(0));
                }

            }
        }
        else{
            FinalEndingBalance=0.00;
            //setEndORNo(String.format("%09d", 0));
        }


        double FinalAmountEB =FinalEndingBalance;
        DecimalFormat formatEB = new DecimalFormat("0.00");
        formatEB.setRoundingMode(RoundingMode.UP);
        String formattedEB = format.format(FinalAmountEB);
        double dataInt2EB = Double.parseDouble(formattedEB);
        setEndBal(String.format("%013.2f", dataInt2EB));



        db3.close();
        //end of Reading Ending Balance



    } // for xread
    public void getReadingDataZ(Context context){

        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(context);
        system_final_date systemDate = new system_final_date();
        systemDate.insertDate(context.getApplicationContext());
        ArrayList<String> ListOfOR =new ArrayList<String>();


        SQLiteDatabase db = context.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from ReadingTable where readingShift='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
        if(cursor.getCount()!=0){
            Log.e("getReadingData","IF CONDITION");
            while (cursor.moveToNext()){
                if (cursor.getString(4)==null){

                    //get all the transaction number from date today
                    Cursor cursor2 = db2.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
                    if (cursor2.getCount()!=0){
                        while(cursor2.moveToNext()) {

                            Cursor cursor3 = db2.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'

                            if (cursor3.getCount()!=0){

                                while (cursor3.moveToNext()){
                                    Log.e("OR NO",cursor3.getString(0));
                                    ListOfOR.add(cursor3.getString(0));
                                }


                            }
                            else{
                                // setBegORNo(String.format("%09d",312));
                                Log.e("Transaction NO",cursor2.getString(0));
                                // ListOfOR.add("0");
                            }


                        }
                        if (ListOfOR.size()!=0) {
//                    for(int x=0; x<ListOfOR.size();x++){
                            Log.e("BegOR",ListOfOR.get(0));

//                    }
                            Collections.sort(ListOfOR);
                            setBegORNo(ListOfOR.get(0));
                            // setEndORNo(ListOfOR.get(ListOfOR.size() - 1));
//                    setEndORNo(ListOfOR.get(2));
                        }
                        else{
                            Log.e("ELSE","ELSE");
                            setBegORNo(String.format("%010d",0));
                        }

                    }
                    else{
                        setBegORNo(String.format("%010d",0));
                    }






                }

                if (cursor.getString(4)!=null){

                    Cursor cursor2 = db2.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
                    if (cursor2.getCount()!=0){
                        while(cursor2.moveToNext()) {

                            Cursor cursor3 = db2.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'
                            if (cursor3.getCount()!=0){
                                while (cursor3.moveToNext()){
                                    ListOfOR.add(cursor3.getString(0));
                                }
                                Collections.sort(ListOfOR);
                                setBegORNo(ListOfOR.get(0));

                            }
                            else{
                                setBegORNo(String.format("%010d", 0));
                            }
                        }


                    }
                    else{
                        setBegORNo(String.format("%010d", 0));
                    }



                }


            }
            db2.close();

        }

        else{
            Log.e("getReadingData","ELSE CONDITION");
//            getLastOR(context.getApplicationContext());
//               // Log.e("X READ BEGOR",getEndORNo());
//            setBegORNo(getEndORNo());
//            Log.e("NULL"," else else NOT NULL");
//           // String data2="0";
//          //  double dataInt2 = Double.parseDouble(data2);
//            //setBegBal(String.format("%013.2f", dataInt2));
//            //Log.e("getReadingData","EMPTY");

            Cursor cursor2 = db2.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);//where readingID='"+UserID+"'
            if (cursor2.getCount()!=0){
                while(cursor2.moveToNext()) {

                    Cursor cursor3 = db2.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'

                    if (cursor3.getCount()!=0){

                        while (cursor3.moveToNext()){
                            Log.e("OR NO",cursor3.getString(0));
                            ListOfOR.add(cursor3.getString(0));
                        }


                    }
                    else{
                        // setBegORNo(String.format("%09d",312));
                        Log.e("Transaction NO",cursor2.getString(0));
                        // ListOfOR.add("0");
                    }


                }
                if (ListOfOR.size()!=0) {
//                    for(int x=0; x<ListOfOR.size();x++){
                    Log.e("BegOR",ListOfOR.get(0));

//                    }
                    Collections.sort(ListOfOR);
                    setBegORNo(ListOfOR.get(0));
                    // setEndORNo(ListOfOR.get(ListOfOR.size() - 1));
//                    setEndORNo(ListOfOR.get(2));
                }
                else{
                    Log.e("ELSE","ELSE");
                    setBegORNo(String.format("%010d",123));
                }

            }
            else{
                setBegORNo(String.format("%010d",321));
            }







        }
        db.close();




        //read Beginning balance

        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

        Cursor cursorReadingBeginningBalance;
//        if (Integer.parseInt(shift_active.getShiftActive())>1){
//            cursorReadingBeginningBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate!='"+systemDate.getSystemDate()+"' and shiftActive<'"+shift_active.getShiftActive()+"'", null);
//        }
//        else{
//            cursorReadingBeginningBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice' and TransDate!='"+systemDate.getSystemDate()+"' and shiftActive='"+shift_active.getShiftActive()+"'", null);
//
//        }
            cursorReadingBeginningBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where  TransDate!='"+systemDate.getSystemDate()+"'", null);
       // cursorReadingBeginningBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where ORTrans<'"+getBegORNo()+"'", null);
        if (cursorReadingBeginningBalance.getCount()!=0){

            while(cursorReadingBeginningBalance.moveToNext()){

                Log.e("System Date",systemDate.getSystemDate());
                //  Log.e("Begin Balance",cursorReadingBeginningBalance.getString(0));


                if (cursorReadingBeginningBalance.getString(0)==null){
                    FinalBeginningBalance=0.00;
                    Log.e("Beginning Balance","if");

                }
                else{
                    FinalBeginningBalance+=Double.parseDouble(cursorReadingBeginningBalance.getString(0));
                    Log.e("Beginning Balance","else " + String.valueOf(FinalBeginningBalance));
                }

            }
        }
        else{
            FinalBeginningBalance=0.00;
            Log.e("Beginning Balance", "else else ");
            //setEndORNo(String.format("%09d", 0));
        }


        double FinalAmount =FinalBeginningBalance;
        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.UP);
        String formatted = format.format(FinalAmount);
        double dataInt2 = Double.parseDouble(formatted);
        setBegBal(String.format("%013.2f", dataInt2));




        //end of reading Beginning balance

        //read Ending balance

        // Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfTransaction='invoice'", null);
        Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport", null);
        // Cursor cursorReadingEndingBalance = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where typeOfTransaction='invoice'"
        if (cursorReadingEndingBalance.getCount()!=0){

            while(cursorReadingEndingBalance.moveToNext()){
                if (cursorReadingEndingBalance.getString(0)==null){
                    FinalBeginningBalance+=0.00;
                }
                else{
                    FinalEndingBalance+=Double.parseDouble(cursorReadingEndingBalance.getString(0));
                }

            }
        }
        else{
            FinalEndingBalance=0.00;
            //setEndORNo(String.format("%09d", 0));
        }


        double FinalAmountEB =FinalEndingBalance;
        DecimalFormat formatEB = new DecimalFormat("0.00");
        formatEB.setRoundingMode(RoundingMode.UP);
        String formattedEB = format.format(FinalAmountEB);
        double dataInt2EB = Double.parseDouble(formattedEB);
        setEndBal(String.format("%013.2f", dataInt2EB));



        db3.close();
        //end of Reading Ending Balance



    } // for zread
    public void getLastOR(Context context){
        system_final_date systemDate = new system_final_date();
        systemDate.insertDate(context.getApplicationContext());
        ArrayList<String> ListOfOR =new ArrayList<String>();
        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadingOR = db3.rawQuery("select * from OfficialReceipt", null);


        Cursor cursor2 = db3.rawQuery("select TransactionID from InvoiceReceiptTotal where TransDate='"+systemDate.getSystemDate()+"'", null);//where readingID='"+UserID+"'
        if (cursor2.getCount()!=0){
            while(cursor2.moveToNext()) {

                Cursor cursor3 = db3.rawQuery("select ORNo from OfficialReceipt where TransactionID='"+cursor2.getString(0)+"'", null);//where readingID='"+UserID+"'
                if (cursor3.getCount()!=0){
                    while (cursor3.moveToNext()){
                        ListOfOR.add(cursor3.getString(0));
                    }


                }
                else{
                    //setEndORNo(String.format("%09d", 0));
                }

            }

            if (ListOfOR.size()!=0){
                Collections.sort(ListOfOR);
                setEndORNo(ListOfOR.get(ListOfOR.size() - 1));
            }
            else{
                setEndORNo(String.format("%010d",0));
            }


// test
















        }
        else{
            setEndORNo(String.format("%010d", 0));
        }



//        if (cursorReadingOR.getCount()!=0){
//           cursorReadingOR.moveToLast() ;
//               if (cursorReadingOR.getString(0)==null){
//                   setEndORNo(String.format("%09d", 0));
//
//               }
//               else{
//
//                   setEndORNo(cursorReadingOR.getString(0));
//               }
//
//
//
//        }
//        else{
//            setEndORNo(String.format("%09d", 0));
//        }
        db3.close();

    }




    public void resetCounter(Context context){
        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor curResetCtr = db3.rawQuery("select * from ResetCounter", null);
        if (curResetCtr.getCount()!=0){
            while (curResetCtr.moveToNext()){
                setResetCounter(Integer.parseInt(curResetCtr.getString(1)));
            }
        }
        else{
            setResetCounter(00);
        }
        db3.close();



    }


    public void ZCounter(Context context){
        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor curResetCtr = db3.rawQuery("select count(typeOfTransaction) from InvoiceReceiptTotal where typeOfTransaction='ZREAD'", null);
        if (curResetCtr.getCount()!=0){
            while (curResetCtr.moveToNext()){
                setZcounter(Integer.parseInt(curResetCtr.getString(0))+1);
            }
        }
        else{
            setZcounter(1);
        }
        db3.close();



    }

    //for Z read

    Date currentDate = Calendar.getInstance().getTime();
    SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");

    public void getBeginningORZ(Context context){
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());


       // dateOnly.format(currentDate.getTime()),
        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor BeginningORNoZ = db3.rawQuery("select * from FinalTransactionReport where TransDate='"+ SysDate.getSystemDate()+"'", null);
        if (BeginningORNoZ.getCount()!=0){
            BeginningORNoZ.moveToFirst();
            setBegORNo(BeginningORNoZ.getString(1));
        }
        if (BeginningORNoZ.getCount()==0){
            Cursor BeginningORNoZ2 = db3.rawQuery("select * from FinalTransactionReport ", null);
            if (BeginningORNoZ2.getCount()!=0){
                BeginningORNoZ2.moveToLast();
                setBegORNo(BeginningORNoZ2.getString(1));

            }
            else{
                setBegORNo("0");
            }
        }

    }


    public void getBeginningBalanceZ(Context context){
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());

        // dateOnly.format(currentDate.getTime()),
        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor BeginningORNoZ = db3.rawQuery("select sum(TotalAmount) from FinalTransactionReport where TransDate!='"+ SysDate.getSystemDate()+"'", null);
        if (BeginningORNoZ.getCount()!=0){
            BeginningORNoZ.moveToFirst();

            setBegBal(BeginningORNoZ.getString(0));
            if (getBegBal()==null){
                setBegBal("0");
            }


        }
        if (BeginningORNoZ.getCount()==0){
//            Cursor BeginningORNoZ2 = db3.rawQuery("select * from FinalTransactionReport ", null);
//            if (BeginningORNoZ2.getCount()!=0){
//                BeginningORNoZ2.moveToLast();
//                setBegORNo(BeginningORNoZ.getString(1));
//
//            }
//            else{
//                setBegORNo("0");
//            }
            setBegBal("0");
        }

        db3.close();

    }

    int OpTotal,OpTotalOut;
    public void getTotalCashInOuts(Context context){

        SQLiteDatabase db3 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor selectFloatIn = db3.rawQuery("select OrderPriceTotal  from InvoiceReceiptTotal where TransDate='"+ dateOnly.format(currentDate.getTime())+"'and typeOfTransaction='Float IN'",null );
        Cursor selectFloatInCount = db3.rawQuery("select Count(typeOfTransaction) from InvoiceReceiptTotal where TransDate='"+ dateOnly.format(currentDate.getTime())+"' and typeOfTransaction='Float IN'",null );

        Cursor selectFloatOut = db3.rawQuery("select OrderPriceTotal  from InvoiceReceiptTotal where TransDate='"+ dateOnly.format(currentDate.getTime())+"'and typeOfTransaction='Float OUT'",null );
        Cursor selectFloatOutCount = db3.rawQuery("select Count(typeOfTransaction) from InvoiceReceiptTotal where TransDate='"+ dateOnly.format(currentDate.getTime())+"' and typeOfTransaction='Float OUT'",null );



        if (selectFloatIn.getCount()!=0){
            setZreadingFloatIndicator("1");
            while (selectFloatIn.moveToNext()){
                    OpTotal+=Integer.parseInt(selectFloatIn.getString(0));
                setCashFloat(String.valueOf(OpTotal));
                Log.e("DATE",dateOnly.format(currentDate.getTime()));
                Log.e("FLOAT AMOUNT",String.valueOf(OpTotal));
            }

            while (selectFloatOut.moveToNext()){
                OpTotalOut+=Integer.parseInt(selectFloatOut.getString(0));
                setCashFloatOut(String.valueOf(OpTotalOut));
                Log.e("DATE",dateOnly.format(currentDate.getTime()));
                Log.e("FLOAT OUT AMOUNT",String.valueOf(OpTotalOut));
            }


            selectFloatInCount.moveToFirst();
            selectFloatOutCount.moveToFirst();

            setCashFloatQty(selectFloatInCount.getString(0));
            setCashFloatOutQty(selectFloatOutCount.getString(0));


        }
        else{
            setZreadingFloatIndicator("0");
        }
    }





}
