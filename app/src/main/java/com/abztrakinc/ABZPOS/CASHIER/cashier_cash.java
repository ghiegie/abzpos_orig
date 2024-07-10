package com.abztrakinc.ABZPOS.CASHIER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.ADMIN.Header_Footer_class;
import com.abztrakinc.ABZPOS.ADMIN.admin_field_authorized;
import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.LOGIN.shift_active;
import com.abztrakinc.ABZPOS.MainActivity;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.pos_user;
import com.abztrakinc.ABZPOS.system_final_date;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public class cashier_cash extends AppCompatActivity {

    TextView tv_QtyPcs1,tv_QtyPcs2,tv_QtyPcs3,tv_QtyPcs4,tv_QtyPcs5,tv_QtyPcs6,tv_QtyPcs7,tv_QtyPcs8,tv_QtyPcs9,tv_QtyPcs10;
    TextView tv_total1,tv_total2,tv_total3,tv_total4,tv_total5,tv_total6,tv_total7,tv_total8,tv_total9,tv_total10;
    Button btn_1000,btn_500,btn_200,btn_100,btn_50,btn_20,btn_10,btn_5,btn_1,btn_cents,btn_save,btn_reset,btn_cashOut;
    Button btn_refund,btn_cancel;
    TextView et_cash;
    View view;
    String DB_NAME = "PosSettings.db";
    String HeaderContent,footer;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
   // Context context;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    String printData;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    private SimpleDateFormat dateFormat2;

    Context context;
    String CashTransType;

    cashier_cash_item cashItem;
    com.abztrakinc.ABZPOS.pos_user pos_user = new pos_user();
    Double totalAmount=0.00,finalTotalAmount=0.00;

    Date currentDate = Calendar.getInstance().getTime();
    //SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
    //Simple dateOnly = new SimpleDateFormat("MM/dd/yyyy");

    //DateFormat timeOnly = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier_cash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this.getApplicationContext();

         loadReceiptData();
        KeyBoardMap();






        tv_QtyPcs1 = this.findViewById(R.id.tv_QtyPcs1);
        tv_QtyPcs2 = this.findViewById(R.id.tv_QtyPcs2);
        tv_QtyPcs3 = this.findViewById(R.id.tv_QtyPcs3);
        tv_QtyPcs4 = this.findViewById(R.id.tv_QtyPcs4);
        tv_QtyPcs5 = this.findViewById(R.id.tv_QtyPcs5);
        tv_QtyPcs6 = this.findViewById(R.id.tv_QtyPcs6);
        tv_QtyPcs7 = this.findViewById(R.id.tv_QtyPcs7);
        tv_QtyPcs8 = this.findViewById(R.id.tv_QtyPcs8);
        tv_QtyPcs9 = this.findViewById(R.id.tv_QtyPcs9);
        tv_QtyPcs10 = this.findViewById(R.id.tv_QtyPcs10);

        tv_total1 = this.findViewById(R.id.tv_total1);
        tv_total2 = this.findViewById(R.id.tv_total2);
        tv_total3 = this.findViewById(R.id.tv_total3);
        tv_total4 = this.findViewById(R.id.tv_total4);
        tv_total5 = this.findViewById(R.id.tv_total5);
        tv_total6 = this.findViewById(R.id.tv_total6);
        tv_total7 = this.findViewById(R.id.tv_total7);
        tv_total8 = this.findViewById(R.id.tv_total8);
        tv_total9 = this.findViewById(R.id.tv_total9);
        tv_total10 = this.findViewById(R.id.tv_total10);

        btn_1000 = this.findViewById(R.id.btn_1000);
        btn_500 = this.findViewById(R.id.btn_500);
        btn_200 = this.findViewById(R.id.btn_200);
        btn_100 = this.findViewById(R.id.btn_100);
        btn_50 = this.findViewById(R.id.btn_50);
        btn_20 = this.findViewById(R.id.btn_20);
        btn_10 = this.findViewById(R.id.btn_10);
        btn_5 = this.findViewById(R.id.btn_5);
        btn_1 = this.findViewById(R.id.btn_1);
        btn_cents = this.findViewById(R.id.btn_cents);
        btn_reset = this.findViewById(R.id.btn_reset);
        btn_save= this.findViewById(R.id.btn_save);
        btn_cashOut=this.findViewById(R.id.btn_cashOut);
        et_cash=this.findViewById(R.id.et_cash);

        btn_refund=this.findViewById(R.id.btn_refund);

        tv_total1.setText(" ");
        tv_total2.setText(" ");
        tv_total3.setText(" ");
        tv_total4.setText(" ");
        tv_total5.setText(" ");
        tv_total6.setText(" ");
        tv_total7.setText(" ");
        tv_total8.setText(" ");
        tv_total9.setText(" ");
        tv_total10.setText(" ");

        tv_QtyPcs1.setText("0");
        tv_QtyPcs2.setText("0");
        tv_QtyPcs3.setText("0");
        tv_QtyPcs4.setText("0");
        tv_QtyPcs5.setText("0");
        tv_QtyPcs6.setText("0");
        tv_QtyPcs7.setText("0");
        tv_QtyPcs8.setText("0");
        tv_QtyPcs9.setText("0");
        tv_QtyPcs10.setText("0");





        cashItem = new cashier_cash_item();
        //loadDatabase();

        btn_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               insert1000();


               // Toast.makeText(cashier_cash.this, totalAmount.toString(), Toast.LENGTH_SHORT).show();






            }
        });
        btn_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

                insert500();




            }
        });
        btn_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

             insert200();




            }
        });
        btn_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

              insert100();


            }
        });
        btn_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

                insert50();



            }
        });
        btn_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

              insert20();




            }
        });
        btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

                insert10();



            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

              insert5();


            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

               insert1();




            }
        });
        btn_cents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cashItem.setC1000(1000);

              insertcent();



            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalAmount = 0.00;
                cashItem.setTotalAmount(0.00);
                et_cash.setText(totalAmount.toString());
                tv_total1.setText(" ");
                tv_total2.setText(" ");
                tv_total3.setText(" ");
                tv_total4.setText(" ");
                tv_total5.setText(" ");
                tv_total6.setText(" ");
                tv_total7.setText(" ");
                tv_total8.setText(" ");
                tv_total9.setText(" ");
                tv_total10.setText(" ");

                tv_QtyPcs1.setText("0");
                tv_QtyPcs2.setText("0");
                tv_QtyPcs3.setText("0");
                tv_QtyPcs4.setText("0");
                tv_QtyPcs5.setText("0");
                tv_QtyPcs6.setText("0");
                tv_QtyPcs7.setText("0");
                tv_QtyPcs8.setText("0");
                tv_QtyPcs9.setText("0");
                tv_QtyPcs10.setText("0");
                cashItem.setC1000Counter(0);
                cashItem.setC500Counter(0);
                cashItem.setC200Counter(0);
                cashItem.setC100Counter(0);
                cashItem.setC50Counter(0);
                cashItem.setC20Counter(0);
                cashItem.setC10Counter(0);
                cashItem.setC5Counter(0);
                cashItem.setCcentsCounter(0);
                cashItem.setC1Counter(0);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                admin_field_authorized auth = new admin_field_authorized();

                if (auth.getCashInAuthorization()==1){


                    auth.Authorization(view.getContext());




                }
                else{


//                    AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
//                    LayoutInflater inflater = getLayoutInflater();
//                    final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_cash_in_confirmation, null);
//                    builder.setView(alertLayout);
//                    AlertDialog alertDialog = builder.create();
//
//                    TextView tv_amountDue=alertLayout.findViewById(R.id.tv_amountDue);
//
//                    Button btn_confirm=alertLayout.findViewById(R.id.btn_confirm);
//
//
//
//
//                    DecimalFormat format = new DecimalFormat("0.00");
//                    tv_amountDue.setText(et_cash.getText().toString());
//
//
//
//
//
//                    btn_confirm.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//
//                            CashTransType="FLOAT IN";
//                            try {
//
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        Looper.prepare();
//                                        pos_user.loadShiftActive(getApplicationContext());
//                                        createReceipt();
//                                        ExportBackUPSettingsDB();
//
//                                        triggerRebirth();
//
//                                    }
//                                }).start();
//
//
//
//                            }
//                            catch (Exception e)
//                            {
//                                Toast.makeText(cashier_cash.this, "error", Toast.LENGTH_SHORT).show();
//                            }
//
//
//
//
//                            alertDialog.dismiss();
//
//
//
//                        }
//
//
//
//
//
//
//
//
//                    });
//
//                    alertDialog.show();
//







                //    CASHIN(context);

                    CASHIN();

                }







            }
        });

        btn_cashOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                admin_field_authorized auth = new admin_field_authorized();

                if (auth.getCashInAuthorization()==1){


                    auth.Authorization(view.getContext());




                }
                else{

                CASHOUT();
                openCashBox();

                }







            }
        });



        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("REFUND AMT",et_cash.getText().toString().trim());


                if( et_cash.getText().toString().trim().equals("") || et_cash.getText().toString().trim()==""){
                    Toast.makeText(view.getContext(), "NO AMOUNT INPUTTED", Toast.LENGTH_SHORT).show();
                }
                else{





                AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_refund, null);
                builder.setView(alertLayout);
                AlertDialog alertDialog = builder.create();
                TextView tv_amountDue=alertLayout.findViewById(R.id.tv_amountDue);
                EditText et_OrNumber=alertLayout.findViewById(R.id.tv_OrNumber);
                EditText et_reason = alertLayout.findViewById(R.id.et_reason);
                Button btn_confirm = alertLayout.findViewById(R.id.btn_confirm);

                tv_amountDue.setText(et_cash.getText().toString());

                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                if (et_OrNumber.getText().toString().matches("")) {
                   // Toast.makeText(context, "PLEASE INPUT OR NUMBER", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_reason.getText().toString().matches("")) {
                   // Toast.makeText(context, "PLEASE INPUT REASON", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!et_reason.getText().toString().matches("") && !et_OrNumber.getText().toString().matches("")){


                    CashTransType="REFUND";
                    try {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Looper.prepare();
                                pos_user.loadShiftActive(getApplicationContext());
                                ORnumber=et_OrNumber.getText().toString();
                                reason=et_reason.getText().toString();

                                createReceiptRefund();
                              //  createReceipt();
                            //    ExportBackUPSettingsDB();

                              //  finishActivity(1);
                                triggerRebirth();
                            }
                        }).start();



                    }
                    catch (Exception e)
                    {
                        Toast.makeText(cashier_cash.this, "error", Toast.LENGTH_SHORT).show();
                    }




                    alertDialog.dismiss();



                }

                    }
                });








                alertDialog.show();





                }




            }
        });


     // view = this.findViewById(R.id.content)






    }

    private void insert1000(){
        int counter = cashItem.getC1000Counter();
        cashItem.setC1000(Double.valueOf(1000));
        counter = counter + 1;
        cashItem.setC1000Counter(counter);

        tv_QtyPcs1.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC1000());
        tv_total1.setText(String.valueOf(total));
        totalAmount += cashItem.getC1000();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());
    }
    private void insert500(){
        int counter = cashItem.getC500Counter();
        cashItem.setC500(Double.valueOf(500));
        counter = counter + 1;
        cashItem.setC500Counter(counter);

        tv_QtyPcs2.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC500());
        tv_total2.setText(String.valueOf(total));
        totalAmount += cashItem.getC500();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());
    }
    private void insert200(){
        int counter = cashItem.getC200Counter();
        cashItem.setC200(Double.valueOf(200));
        counter = counter + 1;
        cashItem.setC200Counter(counter);

        tv_QtyPcs3.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC200());
        tv_total3.setText(String.valueOf(total));
        totalAmount += cashItem.getC200();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());
    }
    private void insert100(){
        int counter = cashItem.getC100Counter();
        cashItem.setC100(Double.valueOf(100));
        counter = counter + 1;
        cashItem.setC100Counter(counter);

        tv_QtyPcs4.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC100());
        tv_total4.setText(String.valueOf(total));
        totalAmount += cashItem.getC100();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());

    }
    private void insert50(){
        int counter = cashItem.getC50Counter();
        cashItem.setC50(Double.valueOf(50));
        counter = counter + 1;
        cashItem.setC50Counter(counter);

        tv_QtyPcs5.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC50());
        tv_total5.setText(String.valueOf(total));
        totalAmount += cashItem.getC50();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());

    }
    private void insert20(){
        int counter = cashItem.getC20Counter();
        cashItem.setC20(Double.valueOf(20));
        counter = counter + 1;
        cashItem.setC20Counter(counter);

        tv_QtyPcs6.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC20());
        tv_total6.setText(String.valueOf(total));
        totalAmount += cashItem.getC20();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());

    }
    private void insert10(){
        int counter = cashItem.getC10Counter();
        cashItem.setC10(Double.valueOf(10));
        counter = counter + 1;
        cashItem.setC10Counter(counter);

        tv_QtyPcs7.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC10());
        tv_total7.setText(String.valueOf(total));
        totalAmount += cashItem.getC10();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());
    }
    private void insert5(){
        int counter = cashItem.getC5Counter();
        cashItem.setC5(Double.valueOf(5));
        counter = counter + 1;
        cashItem.setC5Counter(counter);

        tv_QtyPcs8.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC5());
        tv_total8.setText(String.valueOf(total));
        totalAmount += cashItem.getC5();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());


    }
    private void insert1(){
        int counter = cashItem.getC1Counter();
        cashItem.setC1(Double.valueOf(1));
        counter = counter + 1;
        cashItem.setC1Counter(counter);

        tv_QtyPcs9.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getC1());
        tv_total9.setText(String.valueOf(total));
        totalAmount += cashItem.getC1();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());
    }
    private void insertcent(){
        int counter = cashItem.getCcentsCounter();
        cashItem.setCcents(.25);
        counter = counter + 1;
        cashItem.setCcentsCounter(counter);

        tv_QtyPcs10.setText(String.valueOf(counter));
        Double total = counter * (cashItem.getCcents());
        tv_total10.setText(String.valueOf(total));
        totalAmount += cashItem.getCcents();
        cashItem.setTotalAmount(totalAmount);
        et_cash.setText(totalAmount.toString());

    }
    AlertDialog alertDialog;
    int keyboardCursor=0;
    private void CASHIN(){
        keyboardCursor=1;
        AlertDialog.Builder builder  = new AlertDialog.Builder(cashier_cash.this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_cash_in_confirmation, null);
        builder.setView(alertLayout);
        alertDialog = builder.create();

        TextView tv_amountDue=alertLayout.findViewById(R.id.tv_amountDue);

        Button btn_confirm=alertLayout.findViewById(R.id.btn_confirm);
        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);





        DecimalFormat format = new DecimalFormat("0.00");
        tv_amountDue.setText(et_cash.getText().toString());





        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConfirmChangeFund();
                alertDialog.dismiss();
                keyboardCursor=0;

            }



        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                keyboardCursor=0;
            }
        });



        alertDialog.show();




    }
    private void ConfirmChangeFund(){
        CashTransType="FLOAT IN";
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Looper.prepare();
                    pos_user.loadShiftActive(getApplicationContext());
                    createReceipt();
                    openCashBox();
                    ExportBackUPSettingsDB();
                    alertDialog.dismiss();
                 //   startActivity(new Intent(cashier_cash.this, cashier_invoice.class));
                  //  triggerRebirth();
                //    startActivity(new Intent(cashier_cash.this, MainActivity.class));
                    startActivitynew();
                }
            }).start();



        }
        catch (Exception e)
        {
            Toast.makeText(cashier_cash.this, "error", Toast.LENGTH_SHORT).show();
        }




        alertDialog.dismiss();


    }
    private void startActivitynew(){
        Intent intent = new Intent(cashier_cash.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void ConfirmPaidOut(){
        CashTransType="FLOAT OUT";
        try {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    Looper.prepare();
                    pos_user.loadShiftActive(getApplicationContext());
                    createReceipt2();
                    ExportBackUPSettingsDB();
              //      alertDialog.dismiss();
                  //  startActivity(new Intent(view.getContext(), cashier_cash.class));
                  //  startActivity(new Intent(cashier_cash.this, MainActivity.class));
                 //   startActivity(new Intent(cashier_cash.this, cashier_invoice.class));
                  //  triggerRebirth();
                    startActivitynew();
                    keyboardCursor=0;

                }
            }).start();



        }
        catch (Exception e)
        {
            Toast.makeText(cashier_cash.this, "error", Toast.LENGTH_SHORT).show();
        }




//        alertDialog.dismiss();



    }

    private void CASHOUT(){
        keyboardCursor=2;
        AlertDialog.Builder builder  = new AlertDialog.Builder(cashier_cash.this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_cash_in_confirmation, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();

        TextView tv_amountDue=alertLayout.findViewById(R.id.tv_amountDue);
        TextView tv_cashFund=alertLayout.findViewById(R.id.tv_cashFund);
        TextView tv_amountLabel=alertLayout.findViewById(R.id.tv_amountLabel);

        Button btn_confirm=alertLayout.findViewById(R.id.btn_confirm);




        DecimalFormat format = new DecimalFormat("0.00");
        tv_cashFund.setText("PAID OUT");
        tv_amountLabel.setText("AMOUNT OUT:");
        tv_amountDue.setText(et_cash.getText().toString());





        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

ConfirmPaidOut();
keyboardCursor=0;
                openCashBox();


            }








        });

        alertDialog.show();








    }

    private void openCashBox(){
        Intent intent = new Intent("android.intent.action.CASHBOX");
        intent.putExtra("cashbox_open", true);
        // this.sendBroadcast(intent);
        this.getApplicationContext().sendBroadcast(intent);
    }

    public void triggerRebirth() {
//        PackageManager packageManager = context.getPackageManager();
//        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
//        ComponentName componentName = intent.getComponent();
//        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
//        context.startActivity(mainIntent);
//        Runtime.getRuntime().exit(0);

//        this.finish();
//        this.overridePendingTransition(0,0);
//        startActivity(new Intent(this, cashier_invoice.class));
//        this.overridePendingTransition(0,0);



//        this..finish();
//        this.getActivity().overridePendingTransition(0,0);
//        startActivity(getActivity().getIntent());
//        this.getActivity().overridePendingTransition(0,0);
        startActivity(new Intent(this, MainActivity.class));
    }

    private void loadDatabase(){
        readReferenceNumber();
        SQLiteDatabase db2 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select * from CashInOut where TransactionID='"+readRefNumber+"'", null);
        if (item1.getCount()==0){
            cashItem.setC1000(0.00);
            cashItem.setC500(0.00);
            cashItem.setC200(0.00);
            cashItem.setC100(0.00);
            cashItem.setC50(0.00);
            cashItem.setC20(0.00);
            cashItem.setC10(0.00);
            cashItem.setC5(0.00);
            cashItem.setC1(0.00);
            cashItem.setCcents(0.00);

        }
        else{
            while (item1.moveToNext())
            cashItem.setC1000(Double.valueOf(item1.getString(1)));
            cashItem.setC500(Double.valueOf(item1.getString(2)));
            cashItem.setC200(Double.valueOf(item1.getString(3)));
            cashItem.setC100(Double.valueOf(item1.getString(4)));
            cashItem.setC50(Double.valueOf(item1.getString(5)));
            cashItem.setC20(Double.valueOf(item1.getString(6)));
            cashItem.setC10(Double.valueOf(item1.getString(7)));
            cashItem.setC5(Double.valueOf(item1.getString(8)));
            cashItem.setC1(Double.valueOf(item1.getString(9)));
            cashItem.setCcents(Double.valueOf(item1.getString(10)));
        }

//        if (item)
//        while(item1.moveToNext()){
//
//        }

    }

    String readRefNumber;

    private void readReferenceNumber() {

        //primary key 00000001

        // int readPK;
        SQLiteDatabase db2 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
     //   db2 = this .openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
      Cursor  itemListC = db2.rawQuery("select * from InvoiceReceiptTotal ", null);
        if (itemListC.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%010d", origRefNumber);
            cashItem.setTransactionID(formatted);
        } else {

            itemListC = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%010d", incrementPK);

           // readRefNumber = incrementPKString;
            cashItem.setTransactionID(incrementPKString);


            // }
        }
        itemListC.close();
        db2.close();


    }



    private void createReceipt() {
         shift_active shift_active = new shift_active();
         shift_active.getAccountInfo(getApplicationContext());
         shift_active.getAccountInfo(getApplicationContext());
        readReferenceNumber();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        printer_settings_class PrinterSettings=new printer_settings_class(this.getApplicationContext());

        HeaderFooterClass.HeaderNote(this.getApplicationContext());
        HeaderFooterClass.FooterNote(this.getApplicationContext());


        try {

            StringBuffer buffer = new StringBuffer();
           // FileOutputStream stream = new FileOutputStream(file);
            cashItem.setC1000(cashItem.getC1000Counter() * cashItem.getC1000()  );
            cashItem.setC500(cashItem.getC500Counter() * cashItem.getC500()  );
            cashItem.setC200(cashItem.getC200Counter() * cashItem.getC200()  );
            cashItem.setC100(cashItem.getC100Counter() * cashItem.getC100()  );
            cashItem.setC50(cashItem.getC50Counter() * cashItem.getC50()  );
            cashItem.setC20(cashItem.getC20Counter() * cashItem.getC20()  );
            cashItem.setC10(cashItem.getC10Counter() * cashItem.getC10()  );
            cashItem.setC5(cashItem.getC5Counter() * cashItem.getC5()  );
            cashItem.setC1(cashItem.getC1Counter() * cashItem.getC1()  );
            cashItem.setCcents(cashItem.getCcentsCounter() * cashItem.getCcents()  );



            int modx=Integer.parseInt(cashItem.getTransactionID());
            Log.e("modx",String.valueOf(modx));
            int mody=999999999;
            int resetCount = modx/mody;
            Log.e("resetCt",String.valueOf(modx/mody));
            String formattedCtr = String.format("%02d", resetCount);
            String formattedTrans =  String.valueOf(modx % mody);
          String transactionNumber=formattedTrans;
            transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));


            buffer.append(HeaderFooterClass.getHeaderText()+ "\n");
//            buffer.append(HeaderContent + "\n\n");
//            buffer.append( "------------------------------"+"\n");
            buffer.append("================================" + "\n");
            buffer.append("         CASH FLOAT IN          " + "\n");
            buffer.append("================================" + "\n");
            buffer.append(FinalDate + "\t" + timeOnly.format(currentDate.getTime())+"\t" + "TRANS#: "+"("+formattedCtr+")" +transactionNumber +  "\n");
            buffer.append("POS: "+ shift_active.getPOSCounter() + "\n");
            buffer.append("CASH:" + cashItem.getTotalAmount() +"\n");
            buffer.append("CASHIER ID:" +shift_active.getActiveUserID() +"\n");
            buffer.append("CASHIER NAME:" +shift_active.getActiveUserName() +"\n\n");
            buffer.append("      SIGNATURE:_________________"+"\n");
            buffer.append(
                    "1000 x "+cashItem.getC1000Counter() + "PCS = " + cashItem.getC1000() + "\r\n" +
                    "500  x "+cashItem.getC500Counter() + "PCS = " + cashItem.getC500() + "\r\n" +
                    "200  x "+cashItem.getC200Counter() + "PCS = " + cashItem.getC200() + "\r\n" +
                    "100  x "+cashItem.getC100Counter() + "PCS = " + cashItem.getC100() + "\r\n" +
                    "50   x "+cashItem.getC50Counter() + "PCS = " + cashItem.getC50() + "\r\n" +
                    "20   x "+cashItem.getC20Counter() + "PCS = " + cashItem.getC20() + "\r\n" +
                    "10   x "+cashItem.getC10Counter() + "PCS = " + cashItem.getC10() + "\r\n" +
                    "5    x "+cashItem.getC5Counter() + "PCS = " + cashItem.getC5() + "\r\n" +
                    "1    x "+cashItem.getC1Counter() + "PCS = " + cashItem.getC1() + "\r\n" +
                    ".25  x "+cashItem.getCcentsCounter() + "PCS = " + cashItem.getCcents() + "\r\n"


                   );
            buffer.append( "================================" + "\n\n");
            buffer.append(HeaderFooterClass.getFooterText()+"\n\n");






            //reOpenBT();
           // mmOutputStream.write(buffer.toString().getBytes()); // for printing receipt BT
            try {
                printData=buffer.toString();

                //JMPrinter(printData); // for jolimark
                PrinterSettings.OnlinePrinter(printData,1,0,1);

            }
            catch (Exception ex){

            }
           // InsertInvoiceTransaction();
            floatIn();


           // closeBT();
        } catch (Exception e) {

        }
    }
    private void createReceipt2() {
        shift_active shift_active = new shift_active();
        shift_active.getAccountInfo(getApplicationContext());
        shift_active.getAccountInfo(getApplicationContext());
        readReferenceNumber();
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        printer_settings_class PrinterSettings=new printer_settings_class(this.getApplicationContext());

        HeaderFooterClass.HeaderNote(this.getApplicationContext());
        HeaderFooterClass.FooterNote(this.getApplicationContext());


        try {

            StringBuffer buffer = new StringBuffer();
            // FileOutputStream stream = new FileOutputStream(file);
            cashItem.setC1000(cashItem.getC1000Counter() * cashItem.getC1000()  );
            cashItem.setC500(cashItem.getC500Counter() * cashItem.getC500()  );
            cashItem.setC200(cashItem.getC200Counter() * cashItem.getC200()  );
            cashItem.setC100(cashItem.getC100Counter() * cashItem.getC100()  );
            cashItem.setC50(cashItem.getC50Counter() * cashItem.getC50()  );
            cashItem.setC20(cashItem.getC20Counter() * cashItem.getC20()  );
            cashItem.setC10(cashItem.getC10Counter() * cashItem.getC10()  );
            cashItem.setC5(cashItem.getC5Counter() * cashItem.getC5()  );
            cashItem.setC1(cashItem.getC1Counter() * cashItem.getC1()  );
            cashItem.setCcents(cashItem.getCcentsCounter() * cashItem.getCcents()  );



            int modx=Integer.parseInt(cashItem.getTransactionID());
            Log.e("modx",String.valueOf(modx));
            int mody=999999999;
            int resetCount = modx/mody;
            Log.e("resetCt",String.valueOf(modx/mody));
            String formattedCtr = String.format("%02d", resetCount);
            String formattedTrans =  String.valueOf(modx % mody);
            String transactionNumber=formattedTrans;
            transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));


            buffer.append(HeaderFooterClass.getHeaderText()+ "\n");
//            buffer.append(HeaderContent + "\n\n");
//            buffer.append( "------------------------------"+"\n");
            buffer.append("================================" + "\n");
            buffer.append("         CASH FLOAT OUT         " + "\n");
            buffer.append("================================" + "\n");
            buffer.append(FinalDate + "\t" + timeOnly.format(currentDate.getTime())+"\t" + "TRANS#: "+"("+formattedCtr+")" +transactionNumber +  "\n");
            buffer.append("POS: "+ shift_active.getPOSCounter() + "\n");
            buffer.append("CASH:" + cashItem.getTotalAmount() +"\n");
            buffer.append("CASHIER ID:" +shift_active.getActiveUserID() +"\n");
            buffer.append("CASHIER NAME:" +shift_active.getActiveUserName() +"\n\n");
            buffer.append("      SIGNATURE:_________________"+"\n");
            buffer.append(
                    "1000 x "+cashItem.getC1000Counter() + "PCS = " + cashItem.getC1000() + "\r\n" +
                            "500  x "+cashItem.getC500Counter() + "PCS = " + cashItem.getC500() + "\r\n" +
                            "200  x "+cashItem.getC200Counter() + "PCS = " + cashItem.getC200() + "\r\n" +
                            "100  x "+cashItem.getC100Counter() + "PCS = " + cashItem.getC100() + "\r\n" +
                            "50   x "+cashItem.getC50Counter() + "PCS = " + cashItem.getC50() + "\r\n" +
                            "20   x "+cashItem.getC20Counter() + "PCS = " + cashItem.getC20() + "\r\n" +
                            "10   x "+cashItem.getC10Counter() + "PCS = " + cashItem.getC10() + "\r\n" +
                            "5    x "+cashItem.getC5Counter() + "PCS = " + cashItem.getC5() + "\r\n" +
                            "1    x "+cashItem.getC1Counter() + "PCS = " + cashItem.getC1() + "\r\n" +
                            ".25  x "+cashItem.getCcentsCounter() + "PCS = " + cashItem.getCcents() + "\r\n"


            );
            buffer.append( "================================" + "\n\n");
            buffer.append(HeaderFooterClass.getFooterText()+"\n\n");






            //reOpenBT();
            // mmOutputStream.write(buffer.toString().getBytes()); // for printing receipt BT
            try {
                printData=buffer.toString();

                //JMPrinter(printData); // for jolimark
                PrinterSettings.OnlinePrinter(printData,1,0,1);

            }
            catch (Exception ex){

            }
            // InsertInvoiceTransaction();
            floatOut();


            // closeBT();
        } catch (Exception e) {

        }
    }


    String ORnumber;
    String reason;
    private void createReceiptRefund() {
        shift_active shift_active = new shift_active();
        shift_active.getAccountInfo(getApplicationContext());
        shift_active.getAccountInfo(getApplicationContext());
        system_final_date SysDate = new system_final_date();
        SysDate.insertDate(getApplicationContext());
        FinalDate=SysDate.getSystemDate();
        readReferenceNumber();

        Header_Footer_class HeaderFooterClass = new Header_Footer_class();
        HeaderFooterClass.HeaderNote(this.getApplicationContext());
        HeaderFooterClass.FooterNote(this.getApplicationContext());
        printer_settings_class PrinterSettings=new printer_settings_class(this.getApplicationContext());


            StringBuffer buffer = new StringBuffer();
            // FileOutputStream stream = new FileOutputStream(file);
            cashItem.setC1000(cashItem.getC1000Counter() * cashItem.getC1000()  );
            cashItem.setC500(cashItem.getC500Counter() * cashItem.getC500()  );
            cashItem.setC200(cashItem.getC200Counter() * cashItem.getC200()  );
            cashItem.setC100(cashItem.getC100Counter() * cashItem.getC100()  );
            cashItem.setC50(cashItem.getC50Counter() * cashItem.getC50()  );
            cashItem.setC20(cashItem.getC20Counter() * cashItem.getC20()  );
            cashItem.setC10(cashItem.getC10Counter() * cashItem.getC10()  );
            cashItem.setC5(cashItem.getC5Counter() * cashItem.getC5()  );
            cashItem.setC1(cashItem.getC1Counter() * cashItem.getC1()  );
            cashItem.setCcents(cashItem.getCcentsCounter() * cashItem.getCcents()  );



            buffer.append(HeaderFooterClass.getHeaderText() + "\n\n");
//            buffer.append( "------------------------------"+"\n");
            buffer.append("==============================================" + "\n");
            buffer.append("                  CASH REFUND                 " + "\n");
            buffer.append("=============================================" + "\n");




            buffer.append(FinalDate + "\t" + timeOnly.format(currentDate.getTime())+"\t"+ "\n"+"Trans#:"+cashItem.getTransactionID()+"\n\n\n");
            buffer.append("POS: "+ shift_active.getPOSCounter() + "\n");
            buffer.append("OR NUMBER: "+ ORnumber + "\n");

            buffer.append("CASH:" + cashItem.getTotalAmount() +"\n\n\n");
            buffer.append("REASON :"+ (reason+"                         ").substring(0,25) + "\n");
            buffer.append((reason+"                                          ").substring(25,46) + "\n\n");

            buffer.append("CASHIER ID:" +shift_active.getActiveUserID() +"\n");
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
            buffer.append( "=============================================" + "\n\n");
            buffer.append(HeaderFooterClass.getFooterText()+"\n");






            //reOpenBT();
            // mmOutputStream.write(buffer.toString().getBytes()); // for printing receipt BT
//            try {
                printData=buffer.toString();

                //JMPrinter(printData); // for jolimark
                PrinterSettings.OnlinePrinter(printData,1,0,1);
//            }
//            catch (Exception ex){
//
//            }
            // InsertInvoiceTransaction();
            insertRefundTransaction();


            // closeBT();
//        } catch (Exception e) {
//
//        }
    }





    @SuppressLint("MissingPermission")
    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                //myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth,0);
            }


            @SuppressLint("MissingPermission") Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    Toast.makeText(this, device.getName(), Toast.LENGTH_SHORT).show();


                    //  if (device.getName().trim().equals("MP-38")) {
                    if (device.getName().trim().equals("printer001")) {
                        mmDevice = device;
                        break;
                    }
                    if (device.getName().trim().equals("MP-38")) {
                        mmDevice = device;
                        break;
                    }
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void reOpenBT(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try{
                    closeBT();
                    findBT();
                    openBT();
                }
                catch (Exception e){
//
                }

            }
        }).start();
    }
    @SuppressLint("MissingPermission")
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

//            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    String FinalDate;

    private void floatIn(){
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        boolean isInserted = databaseHandler.insertCashInOut(
                cashItem.getTransactionID(),
                String.valueOf(cashItem.getC1000Counter()),
                String.valueOf(cashItem.getC500Counter()),
                String.valueOf(cashItem.getC200Counter()),
                String.valueOf(cashItem.getC100Counter()),
                String.valueOf(cashItem.getC50Counter()),
                String.valueOf(cashItem.getC20Counter()),
                String.valueOf(cashItem.getC10Counter()),
                String.valueOf(cashItem.getC5Counter()),
                String.valueOf(cashItem.getC1Counter()),
                String.valueOf(cashItem.getCcentsCounter()),

                "FLOAT IN",
                pos_user.getCashierID(),
                pos_user.getCashierName(),
                pos_user.getPOSCounter(),

                FinalDate,
                timeOnly.format(currentDate.getTime())

        );
        InsertInvoiceTransaction();
    }
    private void floatOut(){
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        boolean isInserted = databaseHandler.insertCashInOut(
                cashItem.getTransactionID(),
                String.valueOf(cashItem.getC1000Counter()),
                String.valueOf(cashItem.getC500Counter()),
                String.valueOf(cashItem.getC200Counter()),
                String.valueOf(cashItem.getC100Counter()),
                String.valueOf(cashItem.getC50Counter()),
                String.valueOf(cashItem.getC20Counter()),
                String.valueOf(cashItem.getC10Counter()),
                String.valueOf(cashItem.getC5Counter()),
                String.valueOf(cashItem.getC1Counter()),
                String.valueOf(cashItem.getCcentsCounter()),

                "FLOAT OUT",
                pos_user.getCashierID(),
                pos_user.getCashierName(),
                pos_user.getPOSCounter(),

                FinalDate,
                timeOnly.format(currentDate.getTime())

        );
        InsertInvoiceTransactionOut();
    }


    private void insertRefundTransaction(){

        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(getApplicationContext());
        shift_active.getAccountInfo(getApplicationContext());

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");



        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        //  shift_active shift_active = new shift_active();
        //  shift_active.getShiftingTable(getApplicationContext());
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                cashItem.getTransactionID(),
                "",
                "",
                "",
                String.valueOf(cashItem.getTotalAmount()),
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
                ORnumber,
                String.valueOf(cashItem.getTotalAmount()),
                reason,
                shift_active.getActiveUserName(),
                shift_active.getActiveUserID(),
               // dateOnly.format(currentDate.getTime()),
                FinalDate,
                timeOnly.format(currentDate.getTime()),
                shift_active.getShiftActive()






        );

    }


    private void InsertInvoiceTransaction(){
        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(getApplicationContext());


        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
      //  shift_active shift_active = new shift_active();
      //  shift_active.getShiftingTable(getApplicationContext());
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                cashItem.getTransactionID(),
                "",
                "",
                "",
                String.valueOf(cashItem.getTotalAmount()),
                "",
                "",
                "",
                "Float IN",
                FinalDate,
                //dateOnly.format(currentDate.getTime()),
                timeOnly.format(currentDate.getTime()),
                shift_active.getActiveUserID(),
                shift_active.getShiftActive()








        );
    }
    private void InsertInvoiceTransactionOut(){
        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(getApplicationContext());


        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        //  shift_active shift_active = new shift_active();
        //  shift_active.getShiftingTable(getApplicationContext());
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                cashItem.getTransactionID(),
                "",
                "",
                "",
                String.valueOf(cashItem.getTotalAmount()),
                "",
                "",
                "",
                "Float IN",
                FinalDate,
                //dateOnly.format(currentDate.getTime()),
                timeOnly.format(currentDate.getTime()),
                shift_active.getActiveUserID(),
                shift_active.getShiftActive()








        );
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //  myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadReceiptData(){
        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor item1 = db2.rawQuery("select * from receiptHeader", null);
        if (item1.getCount()==0){
            db2.close();
        }else{
            while (item1.moveToNext()){
                HeaderContent=(item1.getString(1));
//                posSupplier=(item1.getString(1));
//                supAddress=(item1.getString(2));
//                supVatRegTin=(item1.getString(3));
//                supAccreditNo=(item1.getString(4));
//                supDateIssuance=(item1.getString(5));
//                supEffectiveDate=(item1.getString(6));
//                supValidUntil=(item1.getString(7));
//                supPermitNo=(item1.getString(8));
//                supDateIssude=(item1.getString(9));
//                supValidUntil2=(item1.getString(10));
//                compName=(item1.getString(11));
//                compAddress=(item1.getString(12));
//                compVatregTin=(item1.getString(13));
//                compMin=(item1.getString(14));
                //et_footer.setText(item1.getString(15));
            }
        }
        db2.close();
    }

    String DATABASE_BACKUP_PATH_POSOutputDB="/data/data/com.example.myapplication/databases/";
    String DATABASE_NAME="PosOutputDB.db";
    String DATABASE_NAME2="settings.db";
    String DATABASE_NAME3="POSAndroidDB.db";
    String DATABASE_PATH_ANDROID=(Environment.getExternalStorageDirectory()+"/ANDROID_POS/");

    public void ExportBackUPSettingsDB() {
        try {
            //  File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
//           if (data.canWrite()){
            String currentDBPath = DATABASE_BACKUP_PATH_POSOutputDB + DATABASE_NAME;
            String copieDBPath = DATABASE_PATH_ANDROID + DATABASE_NAME;
            File currentDB = new File(currentDBPath);
            File copieDB = new File(copieDBPath);
            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }
//           }
//           else{
//               Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
//           }
        } catch (Exception ex) {

        }
    }
    void sendData() throws IOException {
//        try {

        // the text typed by the user
        String msg = "asdasd";
        msg += "\n";

        mmOutputStream.write(msg.getBytes());

    }







    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
//            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "ERROR 1", Toast.LENGTH_SHORT).show();
        }
    }

    //String HeaderText="";
    //String FooterText;

//    private void Header(){
//
//        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);
//
//
//            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader", null);
//            if (getTextToUpdate.getCount()!=0){
//                while (getTextToUpdate.moveToNext()){
//                   HeaderText = HeaderText + getTextToUpdate.getString(1) + "\n";
//                }
//            }
//
//
//
//
//        PosSettings.close();
//
//
//
//
//
//    }
    private void Footer(){
        SQLiteDatabase PosSettings = openOrCreateDatabase("PosSettings.db", Context.MODE_PRIVATE, null);

//        if (radValue==0){
//            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptHeader where HeaderID='"+deleteID+"'", null);
//            if (getTextToUpdate.getCount()!=0){
//                while (getTextToUpdate.moveToNext()){
//                    et_addNewText.setText(getTextToUpdate.getString(1));
//                    if (getTextToUpdate.getString(2).equals("YES")){
//                        cbx_doubleWidth.setChecked(true);
//                    }
//                    else{
//                        cbx_doubleWidth.setChecked(false);
//                    }
//                }
//            }
//        }
//        else{
//            Cursor getTextToUpdate= PosSettings.rawQuery("select * from receiptFooter", null);
//            if (getTextToUpdate.getCount()!=0){
//                while (getTextToUpdate.moveToNext()){
//                    et_addNewText.setText(getTextToUpdate.getString(1));
//                    if (getTextToUpdate.getString(2).equals("YES")){
//                        cbx_doubleWidth.setChecked(true);
//                    }
//                    else{
//                        cbx_doubleWidth.setChecked(false);
//                    }
//                }
//            }
//        }



        PosSettings.close();
    }
//    private String adminID;
//    private String adminPass;
//    private void Authorization(){
//        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_authorization, null);
//        builder.setView(alertLayout);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//
//
//        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//
//                return i == KeyEvent.KEYCODE_BACK;
//                // return false;
//            }
//        });
//
//
//        alertDialog.show();
//
//    }
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


    @Override
    public void onResume() {
        super.onResume();
            KeyBoardMap();
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
    @Override
    public void onStop() {
        super.onStop();



//        super.onUserLeaveHint();
    }
   // @Override
//    public void onBackpressed(){
//        kboard.UnInit();
//        super.onBackPressed();
//    }



    private void replaceFragment(Fragment fragment){


        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment).commitNow();


    }
    private void checkOut(){
//        View invoiceBtn =  ((MainActivity)).getViewById(R.id.invoice_fragment);
//        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
//        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
//        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
//        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
//        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
//        buttoninv.setBackgroundColor(getResources().getColor(R.color.gray));
//        buttonpayment.setBackgroundColor(getResources().getColor(R.color.Python));
//        buttonshift.setBackgroundColor(getResources().getColor(R.color.gray));

        replaceFragment(new cashier_payment());

    }
    private void shift(){
//        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
//        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
//        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
//        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
//        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
//        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
//        buttoninv.setBackgroundColor(getResources().getColor(R.color.gray));
//        buttonpayment.setBackgroundColor(getResources().getColor(R.color.gray));
//        buttonshift.setBackgroundColor(getResources().getColor(R.color.Python));

        replaceFragment(new cashier_shift());

    }

    private void invoice(){
//        View invoiceBtn =  ((MainActivity)getActivity()).getViewById(R.id.invoice_fragment);
//        View paymentBtn =  ((MainActivity)getActivity()).getViewById(R.id.payment_fragment);
//        View shiftBtn =  ((MainActivity)getActivity()).getViewById(R.id.shift_fragment);
//        Button buttoninv = invoiceBtn.findViewById(R.id.invoice_fragment);
//        Button buttonpayment = paymentBtn.findViewById(R.id.payment_fragment);
//        Button buttonshift = shiftBtn.findViewById(R.id.shift_fragment);
//        buttoninv.setBackgroundColor(getResources().getColor(R.color.Python));
//        buttonpayment.setBackgroundColor(getResources().getColor(R.color.gray));
//        buttonshift.setBackgroundColor(getResources().getColor(R.color.gray));

        replaceFragment(new cashier_invoice());

    }






    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;

        if (input.equalsIgnoreCase("RA")){

            CASHIN();

        }
        if (input.equalsIgnoreCase("PO")){
            CASHOUT();

        }
        if (input.equalsIgnoreCase("Clear")){

            totalAmount = 0.00;
            cashItem.setTotalAmount(0.00);
            et_cash.setText(totalAmount.toString());
            tv_total1.setText(" ");
            tv_total2.setText(" ");
            tv_total3.setText(" ");
            tv_total4.setText(" ");
            tv_total5.setText(" ");
            tv_total6.setText(" ");
            tv_total7.setText(" ");
            tv_total8.setText(" ");
            tv_total9.setText(" ");
            tv_total10.setText(" ");

            tv_QtyPcs1.setText("0");
            tv_QtyPcs2.setText("0");
            tv_QtyPcs3.setText("0");
            tv_QtyPcs4.setText("0");
            tv_QtyPcs5.setText("0");
            tv_QtyPcs6.setText("0");
            tv_QtyPcs7.setText("0");
            tv_QtyPcs8.setText("0");
            tv_QtyPcs9.setText("0");
            tv_QtyPcs10.setText("0");
            cashItem.setC1000Counter(0);
            cashItem.setC500Counter(0);
            cashItem.setC200Counter(0);
            cashItem.setC100Counter(0);
            cashItem.setC50Counter(0);
            cashItem.setC20Counter(0);
            cashItem.setC10Counter(0);
            cashItem.setC5Counter(0);
            cashItem.setCcentsCounter(0);
            cashItem.setC1Counter(0);

        }

        if (input.equalsIgnoreCase("Invoice")){
//            triggerRebirth();
         //   startActivity(new Intent(cashier_cash.this, cashier_invoice.class));
          //  startActivity(new Intent(cashier_cash.this, MainActivity.class));
          //  onBackPressed();
//            Intent intent = new Intent(this,Ma)
            startActivitynew();
        }
        if (input.equalsIgnoreCase("Shift")){
          //  shift();
           // onBackPressed();
          //  startActivity(new Intent(cashier_cash.this, cashier_invoice.class));
          //  startActivity(new Intent(view.getContext(), cashier_invoice.class));
            startActivitynew();
        }
        if (input.equalsIgnoreCase("Payment")){
          //  onBackPressed();
           // startActivity(new Intent(view.getContext(), cashier_invoice.class));
           // startActivity(new Intent(cashier_cash.this, cashier_invoice.class));
            startActivitynew();

          //  checkOut();
        }


        //cash

        if (input.equalsIgnoreCase("7")){
            //  checkOut();
            insert1000();
        }
        if (input.equalsIgnoreCase("8")){
            //  checkOut();
            insert500();
        }
        if (input.equalsIgnoreCase("9")){
            //  checkOut();
            insert200();
        }
        if (input.equalsIgnoreCase("D04")){
            //  checkOut();
            insert100();
        }
        if (input.equalsIgnoreCase("D08")){
            //  checkOut();
            insert50();
        }
        if (input.equalsIgnoreCase("4")){
            //  checkOut();
            insert20();
        }
        if (input.equalsIgnoreCase("5")){
            //  checkOut();
            insert10();
        }
        if (input.equalsIgnoreCase("6")){
            //  checkOut();
            insert5();
        }
        if (input.equalsIgnoreCase("D03")){
            //  checkOut();
            insert1();
        }
        if (input.equalsIgnoreCase("D07")){
            //  checkOut();
            insertcent();
        }
        if (input.equalsIgnoreCase("Total")){
            if (keyboardCursor==1){
                ConfirmChangeFund();
                openCashBox();
                alertDialog.dismiss();
                keyboardCursor=0;

            }
        }
        if (input.equalsIgnoreCase("Sub Total")){
            if (keyboardCursor==1){
                alertDialog.dismiss();
                keyboardCursor=0;
            }
        }
        if (keyboardCursor==2){
            if (input.equalsIgnoreCase("Total")){
                ConfirmPaidOut();
                openCashBox();
                alertDialog.dismiss();
                keyboardCursor=0;
            }
            if (input.equalsIgnoreCase("Sub Total")){
                alertDialog.dismiss();
                keyboardCursor=0;
            }
        }











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

                                 Log.e("RouteKeyIndex",String.valueOf(nKeyIndex));

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





}