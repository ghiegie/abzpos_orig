package com.abztrakinc.ABZPOS.CASHIER;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.ADMIN.Header_Footer_class;
import com.abztrakinc.ABZPOS.ADMIN.admin_email_send;
import com.abztrakinc.ABZPOS.ADMIN.admin_pos_settings;
import com.abztrakinc.ABZPOS.ADMIN.admin_stock_card;
import com.abztrakinc.ABZPOS.ADMIN.admin_stock_receiving;
import com.abztrakinc.ABZPOS.ADMIN.admin_stock_transfer_out;
import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.LOGIN.LoginActivity;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.MainActivity;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.system_final_date;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class cashier_shift extends Fragment {

    View view;
    ImageButton btn_cashier_z_report,btn_cashier_cashinout,btn_cashier_x_report,btn_cashier_logout,btn_cashier_refund,btn_cashier_return_exchange,btn_cashier_receiving,btn_cashier_transfer_out;
    ImageButton btn_cashier_check_sales;
    TextView tv_totalRefundAmount;
    Spinner sp_reason;
    int activateCode=1;

    RecyclerView rv_refund_list;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    //xread zread cursor
    int finalCursor=0; //1 x 2z

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_cashier_shift, container, false);
        Toast.makeText(getActivity(), "TESTING", Toast.LENGTH_SHORT).show();
        btn_cashier_z_report = view.findViewById(R.id.btn_cashier_z_report);
        btn_cashier_cashinout = view.findViewById(R.id.btn_cashier_cashinout);
        btn_cashier_x_report = view.findViewById(R.id.btn_cashier_x_report);
//        btn_cashier_logout = view.findViewById(R.id.btn_cashier_logout);
        btn_cashier_check_sales=view.findViewById(R.id.btn_cashier_check_sales);
        btn_cashier_refund=view.findViewById(R.id.btn_cashier_refund);
        btn_cashier_return_exchange = view.findViewById(R.id.btn_cashier_return_exchange);
        btn_cashier_receiving = view.findViewById(R.id.btn_cashier_receiving);
        btn_cashier_transfer_out = view.findViewById(R.id.btn_cashier_transfer_out);






     btn_cashier_check_sales.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             startActivity(new Intent(view.getContext(), cashier_shift_check_sales.class));
         }
     });
        btn_cashier_x_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finalXreadingViewing();









//                Intent loginscreen=new Intent(getContext(), LoginActivity.class);
//                loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(loginscreen);
               // cashier_shift_Xread.updateReading(getContext());
            }
        });
        btn_cashier_z_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ReadFloatINZ(getContext());

                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
                cashier_shift_Zread.printReport(getActivity(),getContext(),"0");





            }
        });
        btn_cashier_cashinout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), cashier_cash.class));
            }
        });
//        btn_cashier_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                triggerRebirth(getActivity().getApplicationContext());
//            }
//        });
        btn_cashier_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show new refund
               // showDialogRefund();

                String Datefrom="2023-01-01";
                String DateTo="2023-12-30";
                StringBuffer buffer = new StringBuffer();

                Toast.makeText(getContext(), "Tender Report", Toast.LENGTH_SHORT).show();

                printer_settings_class PrinterSettings = new printer_settings_class(getContext());
                buffer.append("--------------------------------" + "\n");
                buffer.append("Tender Report                   " + "\n");
                buffer.append("--------------------------------" + "\n");


                SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
                Cursor getTenderType = db.rawQuery("select Distinct typeOfPayment from InvoiceReceiptTotal where Date(TransDate) between '"+Datefrom+"' and '"+DateTo+"' and typeOfPayment != ''", null);
                if (getTenderType.getCount()!=0){
                    while(getTenderType.moveToNext()){
                        Cursor getData = db.rawQuery("select Sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='"+getTenderType.getString(0)+"' and Date(TransDate) between '"+Datefrom+"' and '"+DateTo+"'", null);
                        if(getData.getCount()!=0){
                            if (getData.moveToFirst()){
                             buffer.append(getTenderType.getString(0) + "        " + getData.getString(0) + "\r\n");
                            }
                        }
                    }
                }

                PrinterSettings.OnlinePrinter(buffer.toString(),1,0,1);






            }
        });

        btn_cashier_return_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogReturnExchange();

            }
        });

        btn_cashier_receiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), admin_stock_receiving.class));
            }
        });

        btn_cashier_transfer_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), admin_stock_transfer_out.class));
            }
        });



        KeyBoardMap();



       return view;



    }

    private void salesReport(){
        String Datefrom="2023-01-01";
        String DateTo="2023-12-30";
        StringBuffer buffer = new StringBuffer();

        Toast.makeText(getContext(), "Tender Report", Toast.LENGTH_SHORT).show();

        printer_settings_class PrinterSettings = new printer_settings_class(getContext());
        buffer.append("--------------------------------" + "\n");
        buffer.append("Hourly Sales Report                   " + "\n");
        buffer.append("--------------------------------" + "\n");
        buffer.append("6:00  - 9:00 AM -" + "\n");
        buffer.append("                                " + "\n");
        buffer.append("9:01  - 12:00 NN " + "\n");
        buffer.append("                                " + "\n");
        buffer.append("12:01 - 3:00 PM" + "\n");
        buffer.append("" + "\n");
        buffer.append("3:01  - 6:00 PM" + "\n");
        buffer.append("" + "\n");
        buffer.append("6:01  - 9:00 PM" + "\n");
        buffer.append("" + "\n");
        buffer.append("9:01  - 12:00 MN" + "\n");
        buffer.append("" + "\n");
        buffer.append("12:01  - 3:00 AM" + "\n");
        buffer.append("" + "\n");
        buffer.append("3:01  - 6:00 AM" + "\n");



        SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor getTenderType = db.rawQuery("select Distinct typeOfPayment from InvoiceReceiptTotal where Date(TransDate) between '"+Datefrom+"' and '"+DateTo+"' and typeOfPayment != ''", null);
        if (getTenderType.getCount()!=0){
            while(getTenderType.moveToNext()){
                Cursor getData = db.rawQuery("select Sum(OrderPriceTotal) from InvoiceReceiptTotal where typeOfPayment='"+getTenderType.getString(0)+"' and Date(TransDate) between '"+Datefrom+"' and '"+DateTo+"'", null);
                if(getData.getCount()!=0){
                    if (getData.moveToFirst()){
                        buffer.append(getTenderType.getString(0) + "        " + getData.getString(0) + "\r\n");
                    }
                }
            }
        }

        PrinterSettings.OnlinePrinter(buffer.toString(),1,0,1);

    }



    int CursorKeyboard=0;
    private void finalXreading(){
        finalCursor=2;
        CursorKeyboard=1;
        AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_xread_buttons, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        Button btn_cashier_x_report = alertLayout.findViewById(R.id.btn_cashier_x_report);
        Button btn_endShift=alertLayout.findViewById(R.id.btn_endShift);


        btn_cashier_x_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cashier_shift_Xread cashier_shift_Xread = new cashier_shift_Xread();
                cashier_shift_Xread.printReport(getContext(),"0",1);
                CursorKeyboard=0;
                finalCursor=0;

            }
        });
        btn_endShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadFloatINX(getContext());
                CursorKeyboard=0;
                finalCursor=0;
            }
        });


        // alertDialog.show();


        //   ReadFloatINX(getContext());




        cashier_shift_Xread cashier_shift_Xread = new cashier_shift_Xread();
        cashier_shift_Xread.printReport(getContext(),"0",1); // 1 to activate printing

        showDialog(); ////to activate the show z-reading



    }


    private void finalXreadingViewing(){
        CursorKeyboard=1;
        finalCursor=1;


        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext(),R.style.DialogSlide2);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_xread_viewing, null);
        TextView tv_xreadView=alertLayout.findViewById(R.id.tv_xreadView);
        Button btn_close = alertLayout.findViewById(R.id.btn_close);
        Button btn_printXRead = alertLayout.findViewById(R.id.btn_printXread);
        Button btn_printXReadCopy = alertLayout.findViewById(R.id.btn_printXreadCopy);


        //        cashier_shift_Xread cashier_shift_Xread = new cashier_shift_Xread();
//

        cashier_shift_Xread cashier_shift_xread = new cashier_shift_Xread();
        cashier_shift_xread.ReportViewing(getContext(),"0",1);
        cashier_shift_xread.setPrintCallback(new cashier_shift_Xread.PrintCallback() {
            @Override
            public void onPrintResult(String result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_xreadView.setText((result));
                        tv_xreadView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tv_xreadView.setGravity(Gravity.LEFT);  // Adjust as needed
                        tv_xreadView.setPadding(0, 0, 0, 0);    // Adjust as needed
                        tv_xreadView.setTypeface(Typeface.create("monospace", Typeface.NORMAL));


//                        String yourFormattedText = "<b>2/F 670 SGT. BUMATAY ST.</b><br>"
//                                + "PLAINVIEW MANDALUYONG CITY<br>"
//                                + "VAT REG TIN: 008-184-146-00000<br>"
//                                + "MIN : 00000000000000000<br>"
//                                + "SN: 0000000000<br><br>"
//
//                                + "<b>===============================</b><br>"
//                                + "<b>X_READING</b><br>"
//                                + "<b>===============================</b><br>"
//                                + "TRANS#: 00-0000000003<br>"
//                                + "Business Date: 11/09/2023<br>"
//                                + "Report ID: 001<br>"
//                                + "POS: 1 ECR Shift: 1<br>"
//                                + "CASHIER: Supervisor<br><br>"
//
//                                + "<b>Beginning Invoice No.:</b> 0000000001<br>"
//                                + "<b>Ending Invoice No.:</b> 0000000001<br>"
//                                + "<b>SI RESET COUNTER:</b> 00<br>"
//                                + "<b>Beginning Balance</b> 0000000000000.00<br>"
//                                + "<b>Ending Balance</b> 0000000003350.00<br><br>"
//
//                                // Continue with the rest of your formatted text
//                                + "...";
//
//                     //   TextView textView = findViewById(R.id.yourTextViewId);
//                        tv_xreadView.setText(Html.fromHtml(yourFormattedText));




                        Log.d("XREAD TEXTFILE", "\n"+result);
                    }
                });
            }
        });



        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finalCursor=0;
            }
        });

        btn_printXReadCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashier_shift_Xread cashier_shift_xread2 = new cashier_shift_Xread();
                cashier_shift_xread2.printReportViewing(getContext(),"0",1);
            }
        });



        btn_printXRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
              //  cashier_shift_Xread cashier_shift_xread2 = new cashier_shift_Xread();
                //cashier_shift_xread2.printReport(getContext(),"0",1);
                finalXreading();


            }
        });

        //cashier_shift_Xread.(getContext(),"0",1); // 1 to activate printing



        builder.setView(alertLayout);





        alertDialog = builder.create();
        //alertDialog.setCanceledOnTouchOutside(false);
        //alertDialog.setCancelable(false);


        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);























    }

    int cashFloatInIndicator=0;
    private void ReadFloatINX(Context context){

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadFloatIN =db.rawQuery("select * from CashInOut where TransType='FLOAT IN'", null);
        if (cursorReadFloatIN.getCount()!=0){
            cashFloatInIndicator=1;
//            while (cursorReadFloatIN.moveToNext()){
//                cash1000+=Integer.parseInt(cursorReadFloatIN.getString(1));
//                cash500+=Integer.parseInt(cursorReadFloatIN.getString(2));
//                cash200+=Integer.parseInt(cursorReadFloatIN.getString(3));
//                cash100+=Integer.parseInt(cursorReadFloatIN.getString(4));
//                cash50+=Integer.parseInt(cursorReadFloatIN.getString(5));
//                cash20+=Integer.parseInt(cursorReadFloatIN.getString(6));
//                cash10+=Integer.parseInt(cursorReadFloatIN.getString(7));
//                cash5+=Integer.parseInt(cursorReadFloatIN.getString(8));
//                cash1+=Integer.parseInt(cursorReadFloatIN.getString(9));
//                cashCents+=Integer.parseInt(cursorReadFloatIN.getString(10));
//
//            }
//
//            cashInTotal=cash1000*1000 + cash500*500 + cash200*200 + cash100*100 + cash50*50 +cash20*20 +cash10*10 + cash5*5 + cash1*1 + cashCents*.25;
            Toast.makeText(context, "GOOD", Toast.LENGTH_SHORT).show();
            //show alertdialog xread


            showDialogX();






        }
        else{
            cashFloatInIndicator=0;
            cashier_shift_Xread cashier_shift_Xread = new cashier_shift_Xread();
            cashier_shift_Xread.printReport(getContext(),"0",1);
        }


    }



    @Override
    public void onResume() {
        super.onResume();
        //    KeyBoardMap();
    }

    @Override
    public void onPause() {
        super.onPause();
        //if (_broadcastReceiver != null)
        //  getActivity().unregisterReceiver(_broadcastReceiver);
        kboard.UnInit();
        Log.d("onUserLeaveHint", "FRAGMENT LEAVE");


//        super.onUserLeaveHint();
    }






    private void ReadFloatINZ(Context context){

        SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor cursorReadFloatIN =db.rawQuery("select * from CashInOut where TransType='FLOAT IN'", null);
        if (cursorReadFloatIN.getCount()!=0){
            cashFloatInIndicator=1;
//            while (cursorReadFloatIN.moveToNext()){
//                cash1000+=Integer.parseInt(cursorReadFloatIN.getString(1));
//                cash500+=Integer.parseInt(cursorReadFloatIN.getString(2));
//                cash200+=Integer.parseInt(cursorReadFloatIN.getString(3));
//                cash100+=Integer.parseInt(cursorReadFloatIN.getString(4));
//                cash50+=Integer.parseInt(cursorReadFloatIN.getString(5));
//                cash20+=Integer.parseInt(cursorReadFloatIN.getString(6));
//                cash10+=Integer.parseInt(cursorReadFloatIN.getString(7));
//                cash5+=Integer.parseInt(cursorReadFloatIN.getString(8));
//                cash1+=Integer.parseInt(cursorReadFloatIN.getString(9));
//                cashCents+=Integer.parseInt(cursorReadFloatIN.getString(10));
//
//            }
//
//            cashInTotal=cash1000*1000 + cash500*500 + cash200*200 + cash100*100 + cash50*50 +cash20*20 +cash10*10 + cash5*5 + cash1*1 + cashCents*.25;
            Toast.makeText(context, "GOOD", Toast.LENGTH_SHORT).show();
            //show alertdialog xread


            showDialogZ();






        }
        else{
            cashFloatInIndicator=0;
            cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
            cashier_shift_Zread.printReport(getActivity(),getContext(),"0");
        }


    }
    Double totalCashDrawer;

    private void showDialogX(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_cashout, null);
        builder.setView(alertLayout);

        final EditText et_TotalCashDrawerX =(EditText) alertLayout.findViewById(R.id.et_TotalCashDrawerX);

        final Button btn_CashDrawerSubmitX = (Button)alertLayout.findViewById(R.id.btn_CashDrawerSubmitX);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_CashDrawerSubmitX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashier_shift_Xread cashier_shift_Xread = new cashier_shift_Xread();
                cashier_shift_Xread.printReport(getContext(),et_TotalCashDrawerX.getText().toString(),1);
              // cashItem.setTotalAmount(Double.parseDouble(et_TotalCashDrawerX.getText().toString()));
                totalCashDrawer=Double.parseDouble(et_TotalCashDrawerX.getText().toString());


                createReceipt();








                alertDialog.dismiss();
            }
        });


    }
   cashier_cash_item cashItem = new cashier_cash_item();
    Date currentDate = Calendar.getInstance().getTime();

  //  SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
    //Simple dateOnly = new SimpleDateFormat("MM/dd/yyyy");

    //DateFormat timeOnly = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
    String printData;
String FinalDate;
    private void createReceipt() {
        shift_active shift_active = new shift_active();
        shift_active.getAccountInfo(getContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());
        FinalDate=SysDate.getSystemDate();



        shift_active.getAccountInfo(getContext());


        readReferenceNumber();
        try {

            StringBuffer buffer = new StringBuffer();
            // FileOutputStream stream = new FileOutputStream(file);




//            buffer.append(HeaderContent + "\n\n");
//            buffer.append( "------------------------------"+"\n");
            buffer.append("==============================================" + "\n");
            buffer.append("              CASH FLOAT OUT                  " + "\n");
//            buffer.append("                                              " + "\n");
            buffer.append("==============================================" + "\n");


            buffer.append(FinalDate + "\t" + timeOnly.format(currentDate.getTime())+"\t #"+ cashItem.getTransactionID()+"\n\n\n");
            buffer.append("POS: "+ shift_active.getPOSCounter() + "\n");
            buffer.append("CASH:" + String.valueOf(totalCashDrawer*-1)+"\n\n\n");
            buffer.append("CASHIER ID:" +shift_active.getActiveUserID() +"\n");
            buffer.append("CASHIER NAME:" +shift_active.getActiveUserName() +"\n\n\n\n");
            buffer.append("      SIGNATURE:_________________"+"\n");
//            buffer.append(
//                    "1000 x "+cashItem.getC1000Counter() + "PCS = " + cashItem.getC1000() + "\r\n" +
//                            "500  x "+cashItem.getC500Counter() + "PCS = " + cashItem.getC500() + "\r\n" +
//                            "200  x "+cashItem.getC200Counter() + "PCS = " + cashItem.getC200() + "\r\n" +
//                            "100  x "+cashItem.getC100Counter() + "PCS = " + cashItem.getC100() + "\r\n" +
//                            "50   x "+cashItem.getC50Counter() + "PCS = " + cashItem.getC50() + "\r\n" +
//                            "20   x "+cashItem.getC20Counter() + "PCS = " + cashItem.getC20() + "\r\n" +
//                            "10   x "+cashItem.getC10Counter() + "PCS = " + cashItem.getC10() + "\r\n" +
//                            "5    x "+cashItem.getC5Counter() + "PCS = " + cashItem.getC5() + "\r\n" +
//                            "1    x "+cashItem.getC1Counter() + "PCS = " + cashItem.getC1() + "\r\n" +
//                            ".25  x "+cashItem.getCcentsCounter() + "PCS = " + cashItem.getCcents() + "\r\n"
//
//
//            );
            buffer.append( "------------------------------" + "\n\n\n\n\n\n\n\n\n");






            //reOpenBT();
            // mmOutputStream.write(buffer.toString().getBytes()); // for printing receipt BT
            try {
                printData=buffer.toString();

                //JMPrinter(printData); // for jolimark
                printer_settings_class printerSetting=new printer_settings_class(this.getContext());
                printerSetting.OnlinePrinter(printData,1,0,1);
            }
            catch (Exception ex){

            }
            // InsertInvoiceTransaction();
            InsertInvoiceTransaction();


            // closeBT();
        } catch (Exception e) {

        }
    }


    private void InsertInvoiceTransaction(){
        DatabaseHandler databaseHandler = new DatabaseHandler(getContext());
        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(getContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());
        FinalDate=SysDate.getSystemDate();
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                cashItem.getTransactionID(),
                "",
                "",
                "",
                String.valueOf(totalCashDrawer),
                "",
                "",
                "",
                "Float OUT",
               FinalDate,
                timeOnly.format(currentDate.getTime()),
                cashItem.getUser(),
                shift_active.getShiftActive()








        );
    }



//    private void SunmiPrinter(String printData){
//        try{
//            SunmiPrintHelper.getInstance().printText(printData, 24,true,false,null);
//            SunmiPrintHelper.getInstance().cutpaper();
//        }catch (Exception ex){
//
//        }
//    }
    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    private void readReferenceNumber() {

        //primary key 00000001

        // int readPK;
        SQLiteDatabase db2 = getContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        //   db2 = this .openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        Cursor  itemListC = db2.rawQuery("select * from InvoiceReceiptTotal ", null);
        if (itemListC.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%09d", origRefNumber);
            cashItem.setTransactionID(formatted);
        } else {

            itemListC = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%09d", incrementPK);

            // readRefNumber = incrementPKString;
            cashItem.setTransactionID(incrementPKString);


            // }
        }
        itemListC.close();
        db2.close();


    }




    private void showDialogZ(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_cashout, null);
        builder.setView(alertLayout);

        final EditText et_TotalCashDrawerX =(EditText) alertLayout.findViewById(R.id.et_TotalCashDrawerX);

        final Button btn_CashDrawerSubmitX = (Button)alertLayout.findViewById(R.id.btn_CashDrawerSubmitX);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btn_CashDrawerSubmitX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
                cashier_shift_Zread.printReport(getActivity(),getContext(),et_TotalCashDrawerX.getText().toString());





                alertDialog.dismiss();







            }
        });


    }

    AlertDialog alertDialog;

    private void showDialog() {



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_xread_done, null);
        builder.setView(alertLayout);





        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);



        ImageButton btn_endShift = alertLayout.findViewById(R.id.btn_endShift);
        ImageButton btn_zRead = alertLayout.findViewById(R.id.btn_zRead);

        btn_endShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // triggerRebirth(LoginActivity.class);
                alertDialog.dismiss();
                getActivity().finish();
                Toast.makeText(builder.getContext(), "L O G O U T", Toast.LENGTH_SHORT).show();

            }
        });
        btn_zRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
                cashier_shift_Zread.printReport(getActivity(),getContext(),"0");





            if (activateCode==1){
                getActivity().finish();
            }




            }
        });


        alertDialog.show();














    }
    public void showDialogReturnExchange(){



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_invoice_refund, null);
        builder.setView(alertLayout);





        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final Button btn_Enter = alertLayout.findViewById(R.id.btn_Enter);
        final Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        final EditText et_invoiceNumber = alertLayout.findViewById(R.id.et_invoiceNumber);
        // final




        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invoiceNo=(String.format("%09d",Integer.parseInt(et_invoiceNumber.getText().toString())));
                String transactionId="";
                //check





                if (invoiceNo.equals(null) || invoiceNo.equals("")){
                    Toast.makeText(getActivity(), "NO INPUTTED INVOICE", Toast.LENGTH_SHORT).show();
                    Log.d("Invoice #", "invoice : null ");

                }
                else{
                    Toast.makeText(getActivity(), "invoice number :" + invoiceNo , Toast.LENGTH_SHORT).show();
                    Log.d("Invoice #", "invoice :"+invoiceNo);


                    SQLiteDatabase db= getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
                    Cursor transactionID =db.rawQuery("select TransactionID from FinalTransactionReport where ORTrans='"+invoiceNo+"'", null);
                    if (transactionID.getCount()!=0){

                        while (transactionID.moveToNext()){
                            Log.d("TransactionID #", "TransactionID :"+transactionID.getString(0));
                            transactionId=transactionID.getString(0);
                        }
                        alertDialog.dismiss();
                        showDialogReturnExchangeData(invoiceNo,transactionId);




                    }
                    else{
                        Toast.makeText(getActivity(), "NO DATA FOUND" , Toast.LENGTH_LONG).show();
                    }



                    db.close();


                }








            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



//        ImageButton btn_endShift = alertLayout.findViewById(R.id.btn_endShift);
//        ImageButton btn_zRead = alertLayout.findViewById(R.id.btn_zRead);
//
//        btn_endShift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // triggerRebirth(LoginActivity.class);
//                getActivity().finish();
//                Toast.makeText(builder.getContext(), "L O G O U T", Toast.LENGTH_SHORT).show();
//            }
//        });
//        btn_zRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
//                cashier_shift_Zread.printReport(getContext(),"0");
//
//
//
//
//
//                if (activateCode==1){
//                    getActivity().finish();
//                }
//
//
//
//
//            }
//        });


        alertDialog.show();














    }
    private void showDialogReturnExchangeData(String invoice,String transactionID) {



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_invoice_refund_data, null);

        builder.setView(alertLayout);
       // ReasonList.clear();
//        ReasonList.add("product defective/damage");
//        ReasonList.add("Expired");
        loadItemChangeList();






        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final Button btn_Enter = alertLayout.findViewById(R.id.btn_Enter);
        final Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        TextView et_invoiceNumber = alertLayout.findViewById(R.id.et_invoiceNumber);
        TextView et_transID= alertLayout.findViewById(R.id.et_transID);
        rv_refund_list = alertLayout.findViewById(R.id.rv_refund_list);
        sp_reason = alertLayout.findViewById(R.id.sp_reason);
        tv_totalRefundAmount=alertLayout.findViewById(R.id.tv_totalRefundAmount);
        LinearLayout ll_invoice_refund = alertLayout.findViewById(R.id.ll_invoice_refund);
       // ll_invoice_refund.setVisibility(View.GONE);
        TextView tv_amountText = alertLayout.findViewById(R.id.tv_amountText);
        tv_amountText.setText("RETURN/EXCHANGE AMOUNT");
        TextView tv_spinnerText = alertLayout.findViewById(R.id.tv_spinnerText);
        tv_spinnerText.setText("ITEM EXCHANGE");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,ExchangeItemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_reason.setAdapter(adapter);
        sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        et_invoiceNumber.setText(invoice);
        et_transID.setText(transactionID);
        FillDataRV(transactionID,invoice);
        RefreshRV();

        // final




        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invoiceNo=(String.format("%09d",Integer.parseInt(et_invoiceNumber.getText().toString())));
                //check





                if (tv_totalRefundAmount.equals(null) || tv_totalRefundAmount.equals("")){
                    //  Toast.makeText(getActivity(), "NO INPUTTED INVOICE", Toast.LENGTH_SHORT).show();
                    //  Log.d("Invoice #", "invoice : null ");

                }
                else{

                    //  insertRefundTransaction(invoice,tv_totalRefundAmount.getText().toString());
                    //createReceiptRefund(invoice,et_transID.getText().toString());
                  //  alertDialog.dismiss();

                    Double TotalExchangeAmt=0.00;
                    Double AmtToExchange=0.00;
                     TotalExchangeAmt= Double.parseDouble(tv_totalRefundAmount.getText().toString());
                     AmtToExchange=Double.parseDouble(ExchangeItemPrice.get(sp_reason.getSelectedItemPosition()));
                    if (TotalExchangeAmt>AmtToExchange){
                        Toast.makeText(getActivity(), "ITEM PRICE MUST BE EQUAL OR HIGHER", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "ITEM PRICE OK", Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                        showDialogTender();

                    }





                }








            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



        alertDialog.show();




    }

    private void showDialogTender(){



    }


    ArrayList<String> ExchangeItemID=new ArrayList<>();
    ArrayList<String> ExchangeItemName=new ArrayList<>();
    ArrayList<String> ExchangeItemPrice=new ArrayList<>();
    ArrayList<String> ExchangeItemList=new ArrayList<>();



    private void loadItemChangeList(){

        ExchangeItemName.clear();
        ExchangeItemPrice.clear();
        ExchangeItemID.clear();
        ExchangeItemList.clear();

        SQLiteDatabase db2 = getActivity().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        Cursor POSItem = db2.rawQuery("select * from ITEM", null);
        if (POSItem.getCount()!=0){
            while (POSItem.moveToNext()){
                ExchangeItemID.add(POSItem.getString(0));
                ExchangeItemName.add(POSItem.getString(1));
                ExchangeItemPrice.add(POSItem.getString(3));
           //     ExchangeItemList.add((POSItem.getString(0) + "     ").substring(0,5) + (POSItem.getString(1) + "--------------------").substring(0,20) + (POSItem.getString(3) + "----------").substring(0,10));
                ExchangeItemList.add((POSItem.getString(1) + "                              ").substring(0,30) + "\n" +(POSItem.getString(3) + "          ").substring(0,10));

            }

        }
        db2.close();

    }




    private void showDialogRefund() {



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_invoice_refund, null);
        builder.setView(alertLayout);





        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final Button btn_Enter = alertLayout.findViewById(R.id.btn_Enter);
        final Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        final EditText et_invoiceNumber = alertLayout.findViewById(R.id.et_invoiceNumber);
       // final




        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invoiceNo=(String.format("%09d",Integer.parseInt(et_invoiceNumber.getText().toString())));
                String transactionId="";
                //check





                if (invoiceNo.equals(null) || invoiceNo.equals("")){
                    Toast.makeText(getActivity(), "NO INPUTTED INVOICE", Toast.LENGTH_SHORT).show();
                    Log.d("Invoice #", "invoice : null ");

                }
                else{
                    Toast.makeText(getActivity(), "invoice number :" + invoiceNo , Toast.LENGTH_SHORT).show();
                    Log.d("Invoice #", "invoice :"+invoiceNo);


                    SQLiteDatabase db= getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
                    Cursor transactionID =db.rawQuery("select TransactionID from FinalTransactionReport where ORTrans='"+invoiceNo+"'", null);
                    if (transactionID.getCount()!=0){

                        while (transactionID.moveToNext()){
                            Log.d("TransactionID #", "TransactionID :"+transactionID.getString(0));
                            transactionId=transactionID.getString(0);
                        }
                        alertDialog.dismiss();
                        showDialogRefundData(invoiceNo,transactionId);




                    }
                    else{
                        Toast.makeText(getActivity(), "NO DATA FOUND" , Toast.LENGTH_LONG).show();
                    }



                    db.close();


                }








            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



//        ImageButton btn_endShift = alertLayout.findViewById(R.id.btn_endShift);
//        ImageButton btn_zRead = alertLayout.findViewById(R.id.btn_zRead);
//
//        btn_endShift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // triggerRebirth(LoginActivity.class);
//                getActivity().finish();
//                Toast.makeText(builder.getContext(), "L O G O U T", Toast.LENGTH_SHORT).show();
//            }
//        });
//        btn_zRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
//                cashier_shift_Zread.printReport(getContext(),"0");
//
//
//
//
//
//                if (activateCode==1){
//                    getActivity().finish();
//                }
//
//
//
//
//            }
//        });


        alertDialog.show();














    }
    ArrayList<String> ReasonList=new ArrayList<>();




    private void showDialogRefundData(String invoice,String transactionID) {



        AlertDialog.Builder builder  = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_invoice_refund_data, null);
        builder.setView(alertLayout);
        ReasonList.clear();
        ReasonList.add("product defective/damage");
        ReasonList.add("Expired");






        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        final Button btn_Enter = alertLayout.findViewById(R.id.btn_Enter);
        final Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        TextView et_invoiceNumber = alertLayout.findViewById(R.id.et_invoiceNumber);
        TextView et_transID= alertLayout.findViewById(R.id.et_transID);
        rv_refund_list = alertLayout.findViewById(R.id.rv_refund_list);
        sp_reason = alertLayout.findViewById(R.id.sp_reason);
        tv_totalRefundAmount=alertLayout.findViewById(R.id.tv_totalRefundAmount);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,ReasonList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_reason.setAdapter(adapter);
        sp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        et_invoiceNumber.setText(invoice);
        et_transID.setText(transactionID);
        FillDataRV(transactionID,invoice);
        RefreshRV();

        // final




        btn_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String invoiceNo=(String.format("%09d",Integer.parseInt(et_invoiceNumber.getText().toString())));
                //check





                if (tv_totalRefundAmount.equals(null) || tv_totalRefundAmount.equals("")){
                  //  Toast.makeText(getActivity(), "NO INPUTTED INVOICE", Toast.LENGTH_SHORT).show();
                  //  Log.d("Invoice #", "invoice : null ");

                }
                else{

                  //  insertRefundTransaction(invoice,tv_totalRefundAmount.getText().toString());
                    createReceiptRefund(invoice,et_transID.getText().toString());


                }








            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });



//        ImageButton btn_endShift = alertLayout.findViewById(R.id.btn_endShift);
//        ImageButton btn_zRead = alertLayout.findViewById(R.id.btn_zRead);
//
//        btn_endShift.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // triggerRebirth(LoginActivity.class);
//                getActivity().finish();
//                Toast.makeText(builder.getContext(), "L O G O U T", Toast.LENGTH_SHORT).show();
//            }
//        });
//        btn_zRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
//                cashier_shift_Zread.printReport(getContext(),"0");
//
//
//
//
//
//                if (activateCode==1){
//                    getActivity().finish();
//                }
//
//
//
//
//            }
//        });


        alertDialog.show();














    }
    private void insertRefundTransaction(String invoiceNo,String amount){

        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(getActivity());
        shift_active.getAccountInfo(getActivity());

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        //  shift_active shift_active = new shift_active();
        //  shift_active.getShiftingTable(getApplicationContext());
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                cashItem.getTransactionID(),
                "",
                "",
                "",
                //String.valueOf(cashItem.getTotalAmount()),
                amount,
                "",
                "",
                "",
                "refund",
                FinalDate,
                timeOnly.format(currentDate.getTime()),
                shift_active.getActiveUserID(),
                shift_active.getShiftActive()


        );

        boolean isInserted2=databaseHandler.insertRefundTransaction(
//                contentValues.put("TransactionID",TransactionID );
//        contentValues.put("ORNumber",ORNumber );
//        contentValues.put("AmountRefunded",AmountRefunded );
//        contentValues.put("Reason",Reason );
//        contentValues.put("TransUser",TransUser );
//        contentValues.put("TransUserID",TransUserID );
//        contentValues.put("TransDate",TransDate );
//        contentValues.put("TransTime",TransTime );
//        contentValues.put("TransShift",TransShift );
                cashItem.getTransactionID(),
                invoiceNo,
               // String.valueOf(cashItem.getTotalAmount()),
                amount,
                sp_reason.getSelectedItem().toString(),
                shift_active.getActiveUserName(),
                shift_active.getActiveUserID(),
                // dateOnly.format(currentDate.getTime()),
                FinalDate,
                timeOnly.format(currentDate.getTime()),
                shift_active.getShiftActive()






        );

    }

   // Double FinalTotalPrice=0.00;

    //final variable for deduction

    Double FinalTotalPriceDeduct; // totalAmount
    Double FinalTotalDiscountDeduct; // TotalDiscount ~ should be positive
    Double FinalTotalVatableSalesDeduct;
    Double FinalTotalVatAmountDeduct;
    Double FinalTotalVatExemptDeduct;
    Double FinalTotalZeroRatedDeduct;
    Double FinalTotalLessVatDeduct;
    Double FinalTotalServiceCharge;


    private void createReceiptRefund(String SInumber,String Transaction) {
         FinalTotalPriceDeduct=0.00;
         FinalTotalDiscountDeduct=0.00;
         FinalTotalVatableSalesDeduct=0.00;
         FinalTotalVatAmountDeduct=0.00;
         FinalTotalVatExemptDeduct=0.00;
         FinalTotalZeroRatedDeduct=0.00;
         FinalTotalLessVatDeduct=0.00;
         FinalTotalServiceCharge=0.00;


        shift_active shift_active = new shift_active();
        shift_active.getAccountInfo(getContext());
        shift_active.getAccountInfo(getContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getContext());
        FinalDate=SysDate.getSystemDate();
        readReferenceNumber();

        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        HeaderFooterClass.HeaderNote(this.getContext());
        HeaderFooterClass.FooterNote(this.getContext());
        printer_settings_class PrinterSettings=new printer_settings_class(this.getContext());


        StringBuffer buffer = new StringBuffer();
        // FileOutputStream stream = new FileOutputStream(file);
        buffer.append(HeaderFooterClass.getHeaderText() + "\n\n");
//            buffer.append( "------------------------------"+"\n");
        buffer.append("================================" + "\n");
        buffer.append("           R E F U N D          " + "\n");
        buffer.append("================================" + "\n");




        buffer.append("Trans#:"+cashItem.getTransactionID()+"\n");
        buffer.append("CASHIER ID:" +shift_active.getActiveUserID() +"\n");
        buffer.append("CASHIER Name:" +shift_active.getActiveUserName() +"\n");
        buffer.append("SHIFT:" +shift_active.getShiftActive() + "POS :" + shift_active.getPOSCounter()+"\n");
        buffer.append(FinalDate + timeOnly.format(currentDate.getTime()) + "\n");
        buffer.append( "--------------------------------"+"\n");
        buffer.append("INVOICE #: "+ SInumber + "\n");
        buffer.append("TRANSACTION #: "+ Transaction + "\n");
        int totalQty=0;
        Double totalPrice=0.00;
        Double totalOrderPriceTotal=0.00;
        Double totalOrderPriceTotalDiscounted=0.00;
        Double totalVATableSales=0.00;
        Double totalVATAmount=0.00;
        Double totalVATExempt=0.00;
        Double totalZeroRated=0.00;
        Double totalLessVAT=0.00;
        Double totalDiscount=0.00;
        DecimalFormat format = new DecimalFormat("0.00");

        for (int x=0;x<finalItemList.size();x++){
            buffer.append(finalItemListName.get(x) + "\n");


            SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
            Cursor InvoiceReceipt = db.rawQuery("select OrderPriceTotal from InvoiceReceiptItemFinal where TransactionID='"+Transaction+"' and OrderID='"+finalItemList.get(x)+"'", null);
            if (InvoiceReceipt.getCount()!=0){
                while (InvoiceReceipt.moveToNext()){
                    totalOrderPriceTotal = Double.parseDouble(InvoiceReceipt.getString(0));

                    Cursor InvoiceReceiptDiscounted = db.rawQuery("select OrderPriceTotal,VAT from InvoiceReceiptItemFinalWDiscount where TransactionID='"+Transaction+"' and OrderID='"+finalItemList.get(x)+"'", null);
                    if(InvoiceReceiptDiscounted.getCount()!=0){
                        while (InvoiceReceiptDiscounted.moveToNext()){
                            totalOrderPriceTotalDiscounted=Double.parseDouble(InvoiceReceiptDiscounted.getString(0));

                            if (!InvoiceReceiptDiscounted.getString(1).equals("0.00")){
                                totalVATExempt += Double.parseDouble(format.format(totalOrderPriceTotalDiscounted/1.12));
                                totalLessVAT += Double.parseDouble(format.format((totalOrderPriceTotalDiscounted/1.12)-totalOrderPriceTotalDiscounted));
                            }
                            else{
                                totalVATableSales += Double.parseDouble(format.format(totalOrderPriceTotalDiscounted/1.12));
                                totalVATAmount += Double.parseDouble(format.format((totalOrderPriceTotalDiscounted/1.12)-totalOrderPriceTotalDiscounted));
                            }


                        }
                    }
                    else{
                        //no record in discount
                        totalVATableSales += Double.parseDouble(format.format(totalOrderPriceTotal/1.12));
                        totalVATAmount += Double.parseDouble(format.format((totalOrderPriceTotal/1.12)-totalOrderPriceTotal));
                      //  totalOrderPriceTotal =
                    }
                }
            }



            totalQty = Integer.parseInt(finalItemListQty.get(x))*-1;
            totalPrice = Double.parseDouble(finalItemListPrice.get(x));
            Double FinalTotalPrice=totalQty*totalPrice;
            String totalPriceString = String.format("%7.2f",totalPrice);
            String FinalTotalPriceString = String.format("%7.2f",FinalTotalPrice);

            buffer.append((finalItemList.get(x)+"                     ")+
                    "\n"+("      "+totalQty+"X"+( totalPriceString+ "                   ")).substring(0,21)+(FinalTotalPriceString+"           ").substring(0,11) +"\n");

            FinalTotalPriceDeduct+=Double.parseDouble(FinalTotalPriceString);
            //discount field


            Cursor InvoiceReceiptDiscountInfo = db.rawQuery("select OrderPriceTotal from InvoiceReceiptItemFinal where TransactionID='"+Transaction+"' and OrderID='"+finalItemList.get(x)+"'", null);
            if (InvoiceReceiptDiscountInfo.getCount()!=0){
                while (InvoiceReceiptDiscountInfo.moveToNext()){
                    Cursor InvoiceReceiptDiscounted = db.rawQuery("select DiscountType,DiscAmount,VAT from InvoiceReceiptItemFinalWDiscount where TransactionID='"+Transaction+"' and OrderID='"+finalItemList.get(x)+"'", null);
                    if(InvoiceReceiptDiscounted.getCount()!=0){
                        while (InvoiceReceiptDiscounted.moveToNext()){
                         //  buffer.append((InvoiceReceiptDiscounted.getString(0)+"                     ").substring(0,21)+ (InvoiceReceiptDiscounted.getString(1)+"           ").substring(0,11) +"\n");
                          // buffer.append(("LESS-VAT"+"                     ").substring(0,21)+InvoiceReceiptDiscounted.getString(2)+"\n");
                            FinalTotalDiscountDeduct+=Double.parseDouble((InvoiceReceiptDiscounted.getString(1)))*-1;
                            FinalTotalLessVatDeduct+= Double.parseDouble((InvoiceReceiptDiscounted.getString(2)));

                        }
                    }

                }
            }


            db.close();

        }


        buffer.append( "--------------------------------"+"\n");
      //  buffer.append("CASH:" + tv_totalRefundAmount.getText().toString() +"\n\n\n");
        buffer.append(("            TOTAL"+"                               ").substring(0,21)+(String.format("%7.2f",Double.parseDouble(String.valueOf(FinalTotalPriceDeduct)))+"           ").substring(0,11) +  "\n");
        buffer.append(("             CASH"+"                               ").substring(0,21)+(String.format("%7.2f",Double.parseDouble(String.valueOf(FinalTotalPriceDeduct)))+"           ").substring(0,11) + "\n");
        buffer.append(("           CHANGE"+"                               ").substring(0,21)+(String.format("%7.2f",Double.parseDouble("0.00"))+"           ").substring(0,11) + "\n");
        buffer.append( "--------------------------------"+"\n");
        buffer.append(("    VATable Sales"+"                               ").substring(0,21)+(String.format("%7.2f",totalVATableSales*-1)+"           ").substring(0,11)+ "\n" );
        FinalTotalVatableSalesDeduct = Double.parseDouble(String.format("%7.2f",totalVATableSales*-1));
        buffer.append(("       VAT Amount"+"                               ").substring(0,21)+(String.format("%7.2f",totalVATAmount)+"              ").substring(0,11)+ "\n" );
        FinalTotalVatAmountDeduct =  Double.parseDouble(String.format("%7.2f",totalVATAmount));
        buffer.append((" VAT Exempt Sales"+"                               ").substring(0,21)+(String.format("%7.2f",totalVATExempt*-1)+"          ").substring(0,11)+ "\n" );
        FinalTotalVatExemptDeduct =  Double.parseDouble(String.format("%7.2f",totalVATExempt*-1));
        buffer.append((" Zero-Rated Sales"+"                               ").substring(0,21)+(String.format("%7.2f",totalZeroRated*-1)+"         ").substring(0,11) + "\n\n");
        FinalTotalZeroRatedDeduct =  Double.parseDouble(String.format("%7.2f",totalZeroRated*-1));

        buffer.append("REASON :"+ (sp_reason.getSelectedItem().toString()) + "\n");
        //buffer.append((reason+"                                          ").substring(25,46) + "\n\n");


        buffer.append("CASHIER NAME:" +shift_active.getActiveUserName() +"\n\n\n\n");
        buffer.append("SIGNATURE:_________________"+"\n");
//            buffer.append(
//                    "1000 x "+cashItem.getC1000Counter() + "PCS = " + cashItem.getC1000() + "\r\n" +
//                            "500  x "+cashItem.getC500Counter() + "PCS = " + cashItem.getC500() + "\r\n" +
//                            "200  x "+cashItem.getC200Counter() + "PCS = " + cashItem.getC200() + "\r\n" +
//                            "100  x "+cashItem.getC100Counter() + "PCS = " + cashItem.getC100() + "\r\n" +
//                            "50   x "+cashItem.getC50Counter() + "PCS = " + cashItem.getC50() + "\r\n" +
//                            "20   x "+cashItem.getC20Counter() + "PCS = " + cashItem.getC20() + "\r\n" +
//                            "10   x "+cashItem.getC10Counter() + "PCS = " + cashItem.getC10() + "\r\n" +
//                            "5    x "+cashItem.getC5Counter() + "PCS = " + cashItem.getC5() + "\r\n" +
//                            "1    x "+cashItem.getC1Counter() + "PCS = " + cashItem.getC1() + "\r\n" +
//                            ".25  x "+cashItem.getCcentsCounter() + "PCS = " + cashItem.getCcents() + "\r\n"
//
//
//            );
        buffer.append( "================================" + "\n\n");
        buffer.append(HeaderFooterClass.getFooterText()+"\n");






        //reOpenBT();
        // mmOutputStream.write(buffer.toString().getBytes()); // for printing receipt BT
//            try {
        printData=buffer.toString();

        Log.d("RESULT", "Total Amount :"+String.valueOf(FinalTotalPriceDeduct));
        Log.d("RESULT", "Total Discount :"+FinalTotalDiscountDeduct);
        Log.d("RESULT", "Total Vatable Sales :"+FinalTotalVatableSalesDeduct);
        Log.d("RESULT", "Total Vat Amount :"+FinalTotalVatAmountDeduct);
        Log.d("RESULT", "Total Vat Exempt :"+FinalTotalVatExemptDeduct);
        Log.d("RESULT", "Total Zero Rated :"+FinalTotalZeroRatedDeduct);
        Log.d("RESULT", "Total Less Vat :"+FinalTotalLessVatDeduct);
        Log.d("RESULT", "Total SVCharge :"+FinalTotalServiceCharge);




        insertRefundTransaction(SInumber,String.valueOf(FinalTotalPriceDeduct));

        updateFinalTransactionReport(Transaction,SInumber,FinalTotalPriceDeduct,FinalTotalDiscountDeduct,FinalTotalVatableSalesDeduct,FinalTotalVatAmountDeduct,FinalTotalVatExemptDeduct,
                FinalTotalZeroRatedDeduct,FinalTotalLessVatDeduct,FinalTotalServiceCharge);


        //JMPrinter(printData); // for jolimark
        PrinterSettings.OnlinePrinter(printData,1,0,1);

        //save to new table


        //delete item from invoice receipt and discount

        for (int x=0;x<finalItemList.size();x++){
            //deleteItemEntry();
            deleteItemEntry(Transaction,finalItemList.get(x));
        }








    }
    private void updateFinalTransactionReport(
            String TransID,String InvoiceID,Double finalTotalPriceDeduct, Double finalTotalDiscountDeduct,Double finalTotalVatableSalesDeduct,Double finalTotalVatAmountDeduct, Double finalTotalVatExemptDeduct,
            Double finalTotalZeroRatedDeduct,Double finalTotalLessVatDeduct,Double finalTotalServiceCharge){


        SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor FinalTransactionReport = db.rawQuery("select * from FinalTransactionReport where TransactionID='"+TransID+"' and ORTrans='"+InvoiceID+"'", null);
        if (FinalTransactionReport.getCount()!=0){
            while(FinalTransactionReport.moveToNext()) {

                finalTotalPriceDeduct = Double.parseDouble(FinalTransactionReport.getString(2))+finalTotalPriceDeduct;
                finalTotalDiscountDeduct =  Double.parseDouble(FinalTransactionReport.getString(3))+ finalTotalDiscountDeduct;
                finalTotalVatableSalesDeduct =  Double.parseDouble(FinalTransactionReport.getString(4))+ finalTotalVatableSalesDeduct;
                finalTotalVatAmountDeduct =  Double.parseDouble(FinalTransactionReport.getString(5))+ finalTotalVatAmountDeduct;
                finalTotalVatExemptDeduct =  Double.parseDouble(FinalTransactionReport.getString(6))+ finalTotalVatExemptDeduct;
                finalTotalZeroRatedDeduct =  Double.parseDouble(FinalTransactionReport.getString(7))+ finalTotalZeroRatedDeduct;
                finalTotalLessVatDeduct =  Double.parseDouble(FinalTransactionReport.getString(8))+ finalTotalLessVatDeduct;
                finalTotalServiceCharge =  Double.parseDouble(FinalTransactionReport.getString(9))+ finalTotalServiceCharge;



                Log.d("UPDATEd", "Total Amount :" + finalTotalPriceDeduct);
                Log.d("UPDATEd", "Total Discount :" + finalTotalDiscountDeduct);
                Log.d("UPDATEd", "Total Vatable Sales :" + finalTotalVatableSalesDeduct);
                Log.d("UPDATEd", "Total Vat Amount :" + finalTotalVatAmountDeduct);
                Log.d("UPDATEd", "Total Vat Exempt :" + finalTotalVatExemptDeduct);
                Log.d("UPDATEd", "Total Zero Rated :" + finalTotalZeroRatedDeduct);
                Log.d("UPDATEd", "Total Less Vat :" + finalTotalLessVatDeduct);
                Log.d("UPDATEd", "Total SVCharge :" + finalTotalServiceCharge);


                  String strsql = "UPDATE FinalTransactionReport set " +
                          "TotalAmount='"+finalTotalPriceDeduct+"'," +
                          "TotalDiscount='"+finalTotalDiscountDeduct+"'," +
                          "TotalVatableSales='"+finalTotalVatableSalesDeduct+"'," +
                          "TotalVatAmount='"+finalTotalVatAmountDeduct+"'," +
                          "TotalVatExempt='"+finalTotalVatExemptDeduct+"'," +
                          "TotalZeroRatedSales='"+finalTotalZeroRatedDeduct+"'," +
                          "TotalLessVat='"+finalTotalLessVatDeduct+"'," +
                          "TotalServiceCharge='"+finalTotalServiceCharge+"'" +
                          "  where TransactionID='"+TransID+"'and ORTrans='"+InvoiceID+"'";
                  db.execSQL(strsql);


            }
        }





        db.close();
    }

    private void deleteItemEntry(String transID,String orderID ){
        SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);


        String strsql = "delete from InvoiceReceiptItemFinal where TransactionID='"+transID+"'and OrderID='"+orderID+"'";
        db.execSQL(strsql);

        String strsql2 = "delete from InvoiceReceiptItemFinalWDiscount where TransactionID='"+transID+"'and OrderID='"+orderID+"'";
        db.execSQL(strsql2);


        db.close();




    }




    KeyboardDevice kboard;
    KeyCodeManager kManager;
    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;

    }
    private final Handler MyHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint({"StringFormatMatches", "DefaultLocale"})
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KeyboardDevice.MSG_KEY: {
                    String strMsgInfo = "";
                    try {
                        strMsgInfo = msg.getData().getString("msg_info");
                        String[] keyData = strMsgInfo.split(":", 2);
                        if (keyData != null) {

                            int nKeyIndex = StringToInt(keyData[0]);
                            if (nKeyIndex == 0) {

                            } else {
                                String KeyIndex = String.format("%d", nKeyIndex);
                                KeyCodeManager kManager = new KeyCodeManager();




                                // editTextTextPersonName.setText(kManager.getDefaultKeyName(StringToInt(KeyIndex)));
                                SimulateKeyboard(nKeyIndex);
                                // Log.e("RouteKeyIndex",String.valueOf(nKeyIndex));

                            }
                            String keyName=KeyCodeManager.getDefaultKeyName(nKeyIndex);
                            String strShow = String.format("KeyIndex:%d", nKeyIndex) + " ScanCode:" + keyData[1]+" KeyName:"+keyName;
                            // ShowMsg(strShow);
                        } else {
                            //Toast.makeText(cashier_invoice.this, strMsgInfo, Toast.LENGTH_SHORT).show();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    private void RouteKeyIndex(int nKeyIndex) {

        int nVKCode = KeyCodeManager.getVKCode(nKeyIndex);
        String nVKCode2 = KeyCodeManager.getDefaultKeyName(nKeyIndex);
        if (nVKCode == -100) {
        } else {
            Log.e("nVKCode",String.valueOf(nVKCode));
            SimulateKeyboard(nVKCode);
        }
        if (nVKCode==123){

        }


        if (nVKCode==131) {  //131 for calling invoice
            replaceFragment(new cashier_invoice());
            //  Log.e("new FragInv","1");

        }
        if (nVKCode==132) {  //131 for calling invoice
             replaceFragment(new cashier_payment());

        }
        if (nVKCode==133) {  //131 for calling invoice
            //replaceFragment(new cashier_shift());
           // Log.e("new FragInv","3");
        }


        if (nVKCode==202){
            printer_settings_class prn = new printer_settings_class(this.getContext());
            prn.PaperFeed();

        }


    }

    private void replaceFragment(Fragment fragment){


        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment).commitNow();


    }

    private void checkOut(){
        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
        buttoninv.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonpayment.setBackgroundColor(getResources().getColor(R.color.Python));
        buttonshift.setBackgroundColor(getResources().getColor(R.color.gray));

        replaceFragment(new cashier_payment());

    }
    private void shift(){
        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
        buttoninv.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonpayment.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonshift.setBackgroundColor(getResources().getColor(R.color.Python));

        replaceFragment(new cashier_shift());

    }

    private void invoice(){
        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
        buttoninv.setBackgroundColor(getResources().getColor(R.color.Python));
        buttonpayment.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonshift.setBackgroundColor(getResources().getColor(R.color.gray));

        replaceFragment(new cashier_invoice());

    }


    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;


        if (input.equalsIgnoreCase("Invoice")){
           invoice();
            //triggerRebirth();
        }
        if (input.equalsIgnoreCase("Shift")){
            shift();
        }
        if (input.equalsIgnoreCase("Payment")){
            checkOut();
        }
        if (input.equalsIgnoreCase("X/TIME")){
            //finalXreading();
            finalXreadingViewing();
        }
        if (input.equalsIgnoreCase("EXIT")){
            if (finalCursor==1) {
                alertDialog.dismiss();
                finalCursor=0;
            }
        }
        if (CursorKeyboard==1){
        if (input.equalsIgnoreCase("Total")){
            if (finalCursor==1){
                alertDialog.dismiss();
                //  cashier_shift_Xread cashier_shift_xread2 = new cashier_shift_Xread();
                //cashier_shift_xread2.printReport(getContext(),"0",1);
                finalXreading();
                finalCursor=0;
            }
            else if(finalCursor==2){


                cashier_shift_Xread cashier_shift_Xread = new cashier_shift_Xread();
                cashier_shift_Xread.printReport(getContext(),"0",1);
                CursorKeyboard=0;
                finalCursor=0;


            }
            else{
                if (CursorKeyboard==1){
                    Log.e("XREADING","ZREADING");
                    cashier_shift_Zread cashier_shift_Zread = new cashier_shift_Zread();
                    cashier_shift_Zread.printReport(getActivity(),getContext(),"0");
                    CursorKeyboard=0;
                    if (activateCode==1){

                        Intent intent = new Intent(this.getContext(), LoginActivity.class);// New activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        this.getActivity().finish();



                        alertDialog.dismiss();
//                    getActivity().finish();
                        //  triggerRebirth(getContext());
                    }
                }
            }




        }
        if (input.equalsIgnoreCase("Sub Total")){

            if (finalCursor==1){
                cashier_shift_Xread cashier_shift_xread2 = new cashier_shift_Xread();
                cashier_shift_xread2.printReportViewing(getContext(),"0",1);

            }
            else if (finalCursor==2){
                ReadFloatINX(getContext());
                CursorKeyboard=0;
                finalCursor=0;
            }
            else {

                if (CursorKeyboard == 1) {
                    CursorKeyboard = 0;
                    alertDialog.dismiss();

                    Intent intent = new Intent(this.getContext(), LoginActivity.class);// New activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    this.getActivity().finish();

                    getActivity().finish();
                    Toast.makeText(getContext(), "L O G O U T", Toast.LENGTH_SHORT).show();

                }
            }
        }
        }

        if(input.equalsIgnoreCase("PO") || input.equalsIgnoreCase("RA")){
            startActivity(new Intent(view.getContext(), cashier_cash.class));
        }
        if (input.equals("Btn")){
            startActivity(new Intent(view.getContext(), admin_stock_receiving.class));
        }
        if (input.equals("PLU")){
            startActivity(new Intent(view.getContext(), admin_stock_transfer_out.class));
        }






    }
    public static int StringToInt(String strParse) {
        int iValue = 0;
        try {
            if (strParse.length() > 0) {
                iValue = Integer.parseInt(strParse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iValue;
    }






    ArrayList<String> OtherPaymentList=new ArrayList<String>();
    List<cashier_shift_model> ArrayDataList = new ArrayList<>();
    private void FillDataRV(String TransactionID,String InvoiceNumber){

        SQLiteDatabase db = getActivity().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        ArrayDataList.clear();
        finalItemList.clear();
        finalItemListName.clear();
        finalItemListQty.clear();
        finalItemListPrice.clear();
        amountList.clear();

        Cursor itemListC = db.rawQuery("select * from InvoiceReceiptItemFinal where TransactionID='"+TransactionID+"'", null);

        int numberOfItem = itemListC.getCount();
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){



            //  int ItemNo=itemCounter+1;

            if (itemListC.getCount()!=0){
                while (itemListC.moveToNext()){

                    String OrderID =itemListC.getString(1);
                    String OrderName=itemListC.getString(2);
                    String OrderQty=itemListC.getString(3);
                   // String OrderPrice=itemListC.getString(4);
                    String OrderPriceTotal=itemListC.getString(5);
                    String TransactionTime=itemListC.getString(6);
                    String TransactionDate=itemListC.getString(7);
                    String TotalDiscount="0.00";
                    String TotalDiscVat="0.00";

                    Cursor itemListCDiscount = db.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where TransactionID='"+TransactionID+"' and OrderID='"+OrderID+"'", null);
                    if (itemListCDiscount.getCount()!=0){
                        itemListCDiscount.moveToFirst();
                        TotalDiscount=itemListCDiscount.getString(11);
                        TotalDiscVat=itemListCDiscount.getString(13);


                    }
                   String OrderPrice = String.valueOf(Double.parseDouble(itemListC.getString(4)) + Double.parseDouble(TotalDiscount) + Double.parseDouble(TotalDiscVat));






// public cashier_shift_model(String transactionID, String transactionInvoice, String transactionOrderID, String transactionItemName,
//                            String transactionItemQty, String transactionTotalPrice,String transactionTotalDisc,String transactionTotalAmount,
//                            String transactionTime,String transactionDate){


                    cashier_shift_model p0=new cashier_shift_model(TransactionID,OrderID,OrderName,OrderQty,OrderPrice,TotalDiscount,OrderPriceTotal,TransactionTime,TransactionDate);
                    //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

                    ArrayDataList.addAll(Arrays.asList(new cashier_shift_model[]{p0}));

                }
            }



        }
        //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";
        db.close();

    }
    private void RefreshRV(){


        rv_refund_list.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.removeAllViews();
        rv_refund_list.setLayoutManager(layoutManager);
        mAdapter=new cashier_shift.RecyclerviewAdapter(ArrayDataList,getActivity());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_refund_list.setAdapter(mAdapter);
        rv_refund_list.smoothScrollToPosition(rv_refund_list.getBottom());

    }

    ArrayList<String> amountList = new ArrayList<>();
    ArrayList<String> finalItemList = new ArrayList<>();
    ArrayList<String> finalItemListName = new ArrayList<>();
    ArrayList<String> finalItemListQty = new ArrayList<>();
    ArrayList<String> finalItemListPrice = new ArrayList<>();
    public class RecyclerviewAdapter extends RecyclerView.Adapter <cashier_shift.RecyclerviewAdapter.MyViewHolder>{
        List<cashier_shift_model> ArrayDataList;
        Context context;
        ArrayList<String> selectList = new ArrayList<>();
        private cashier_shift.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<cashier_shift_model> ArrayDataList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.ArrayDataList = ArrayDataList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public cashier_shift.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_alertdialog_invoice_refund_data_layout,parent,false);



            cashier_shift.RecyclerviewAdapter.MyViewHolder holder = new cashier_shift.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull cashier_shift.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

//            holder.tv_id.setText(String.valueOf(.get(position).getID()));
//            holder.tv_paymentName.setText(ArrayOtherPaymentList.get(position).getPaymentName());
//            holder.tv_allowReference.setText(ArrayOtherPaymentList.get(position).getAllowReference());
//            holder.tv_allowDetails.setText(ArrayOtherPaymentList.get(position).getAllowDetails());
//            holder.tv_paymentType.setText(String.valueOf(ArrayOtherPaymentList.get(position).getPaymentType()));
//            holder.tv_changeType.setText(String.valueOf(ArrayOtherPaymentList.get(position).getChangeType()));
//
//            holder.opID=((ArrayOtherPaymentList.get(position).getID()));
//            holder.opName=(ArrayOtherPaymentList.get(position).getPaymentName());
//            holder.opReference=(ArrayOtherPaymentList.get(position).getAllowReference());
//            holder.opDetails=(ArrayOtherPaymentList.get(position).getAllowDetails());
//            holder.opType=((ArrayOtherPaymentList.get(position).getPaymentType()));
//            holder.opChangetype=(ArrayOtherPaymentList.get(position).getChangeType());

            holder.cb_itemName.setText(ArrayDataList.get(position).getTransactionItemName());

           // holder.tv_discAmount.setText(ArrayDataList.get(position).getTransactionTotalDisc());
            holder.tv_totalAmount.setText(ArrayDataList.get(position).getTransactionTotalPrice());


            holder.cb_itemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //showPassword(isChecked);


                    if (holder.cb_itemName.isChecked()){
                       // Log.e("TEST CB",ArrayDataList.get(position).getTransactionOrderID());
                        selectList.add(ArrayDataList.get(position).getTransactionOrderID());
                        amountList.add(ArrayDataList.get(position).getTransactionTotalPrice());

                        finalItemList.add(ArrayDataList.get(position).getTransactionOrderID());
                        finalItemListName.add(ArrayDataList.get(position).getTransactionItemName());
                        finalItemListQty.add(ArrayDataList.get(position).getTransactionItemQty());
                        finalItemListPrice.add(ArrayDataList.get(position).getTransactionTotalPrice());

                        Log.e("ARRAY",selectList.toString());
                        Log.e("ARRAY",amountList.toString());
                    }
                    else{
                     //   Log.e("TEST CB",ArrayDataList.get(position).getTransactionOrderID());
                        selectList.remove(ArrayDataList.get(position).getTransactionOrderID());
                        amountList.remove(ArrayDataList.get(position).getTransactionTotalPrice());
                        finalItemList.remove(ArrayDataList.get(position).getTransactionOrderID());
                        finalItemListName.remove(ArrayDataList.get(position).getTransactionItemName());
                        finalItemListQty.remove(ArrayDataList.get(position).getTransactionItemQty());
                        finalItemListPrice.remove(ArrayDataList.get(position).getTransactionTotalPrice());

                        Log.e("ARRAY",selectList.toString());
                        Log.e("ARRAY",amountList.toString());
                    }
                    double totalRef=0.00;
                    for (int x=0;x<amountList.size();x++){
                        totalRef=totalRef+Double.parseDouble(amountList.get(x));
                    }

                    tv_totalRefundAmount.setText(String.valueOf(totalRef));
                }
            });


        }

        @Override
        public int getItemCount() {
            return ArrayDataList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
//            TextView tv_id;
//            TextView tv_paymentName;
//            TextView tv_allowReference;
//            TextView tv_allowDetails;
//            TextView tv_paymentType;
//            TextView tv_changeType;
//            LinearLayout ll_otherPaymentSelect;

            CheckBox cb_itemName;
         //   TextView tv_discAmount;
            TextView tv_totalAmount;


//            private int opID;
//            private String opName;
//            private String opReference;
//            private String opDetails;
//            private int opType;
//            private int opChangetype;





            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
//                tv_id = itemView.findViewById(R.id.tv_id);
//                tv_paymentName = itemView.findViewById(R.id.tv_name);
//                tv_allowReference = itemView.findViewById(R.id.tv_reference);
//                tv_allowDetails=itemView.findViewById(R.id.tv_details);
//                tv_paymentType=itemView.findViewById(R.id.tv_paymentType);
//                tv_changeType=itemView.findViewById(R.id.tv_changeType);
//                ll_otherPaymentSelect=itemView.findViewById(R.id.ll_otherPaymentSelect);
                cb_itemName = itemView.findViewById(R.id.cb_itemName);
            //    tv_discAmount = itemView.findViewById(R.id.tv_discAmount);
                tv_totalAmount = itemView.findViewById(R.id.tv_totalAmount);





//             // String DiscID2="";
//                DiscountAmount="";
//                DiscountComputation="";
//                DiscountExcludeTax="";
//                DiscountType="";
//                DiscLabel="";
//                MinTransactionAmount="";
//                MaxTransactionAmount="";
//                MaxDiscountAmount="";
//                SalesExcludeTax="";



//                ll_otherPaymentSelect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        FinalID=opID;
//                        FinalName=opName;
//                        FinalAllowRef=opReference;
//                        FinalAllowDetails=opDetails;
//                        FinalPaymentType=opType;
//                        FinalChangeType=opChangetype;
//
//                        Log.d("TAG",String.valueOf(FinalID));
//                        Log.d("TAG",String.valueOf(FinalName));
//                        Log.d("TAG",String.valueOf(FinalAllowRef));
//                        Log.d("TAG",String.valueOf(FinalAllowDetails));
//                        Log.d("TAG",String.valueOf(FinalPaymentType));
//                        Log.d("TAG",String.valueOf(FinalChangeType));
//
//
//
//
//
//                    }
//                });



                // parentLayout = itemView.findViewById(R.id.linear_orderlist_layout2);



//                tv_itemText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e("item id",tv_itemID.getText().toString());
//                        deleteID=Integer.parseInt(tv_itemID.getText().toString());
//                        updateItem();
//
//
//                    }
//                });



            }
        }
    }








}