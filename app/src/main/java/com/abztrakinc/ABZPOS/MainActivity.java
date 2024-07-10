package com.abztrakinc.ABZPOS;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abztrakinc.ABZPOS.ADMIN.AppService;
import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.CASHIER.cashier_invoice;
import com.abztrakinc.ABZPOS.CASHIER.cashier_payment;
import com.abztrakinc.ABZPOS.CASHIER.cashier_shift;
import com.abztrakinc.ABZPOS.LOGIN.LoginActivity;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice2;

import java.io.File;
import java.util.zip.Inflater;


public class MainActivity extends AppCompatActivity {

  public Button cashierInvoiceFragment,cashierPaymentFragment,cashierShiftFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );


        cashierInvoiceFragment = findViewById(R.id.invoice_fragment);
        cashierPaymentFragment = findViewById(R.id.payment_fragment);
        cashierShiftFragment = findViewById(R.id.shift_fragment);
        cashierInvoiceFragment.setBackgroundColor(getResources().getColor(R.color.Python));

       replaceFragment(new cashier_invoice());

        cashierInvoiceFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //cashierInvoiceFragment.setBackgroundColor(Color.GREEN);

                buttonInvoiceColor();
                replaceFragment(new cashier_invoice());
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );

            }
        });
        cashierPaymentFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                buttonPaymentColor();
              //  replaceFragment(new cashier_payment());
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );

            }
        });
        cashierShiftFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonShiftColor();
                replaceFragment(new cashier_shift());
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );


            }
        });

    //    KeyBoardMap();




//        if(savedInstanceState == null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.framelayout, new cashier_invoice());
//            transaction.commit();
//        }

        hideSystemBars();

    }

    public void buttonInvoiceColor(){
        cashierInvoiceFragment.setBackgroundColor(getResources().getColor(R.color.Python));
        cashierPaymentFragment.setBackgroundColor(getResources().getColor(R.color.gray));
        cashierShiftFragment.setBackgroundColor(getResources().getColor(R.color.gray));
    }
    public void buttonPaymentColor(){


        cashierInvoiceFragment.setBackgroundColor(this.getResources().getColor(R.color.gray));
        cashierPaymentFragment.setBackgroundColor(this.getResources().getColor(R.color.Python));
        cashierShiftFragment.setBackgroundColor(this.getResources().getColor(R.color.gray));

        replaceFragment(new cashier_payment());
    } private void buttonShiftColor(){
        cashierInvoiceFragment.setBackgroundColor(getResources().getColor(R.color.gray));
        cashierPaymentFragment.setBackgroundColor(getResources().getColor(R.color.gray));
        cashierShiftFragment.setBackgroundColor(getResources().getColor(R.color.Python));

    }

    public View getViewById(int id){
        Log.d("BUTTON",String.valueOf(id));
      return findViewById(id);
    }




    @Override
    public void onBackPressed() {

    }





    @Override
    public void onDestroy() {
        Log.e("APPLICATION DESTROY","TRUE");
       // kboard.UnInit();
      //  KeyboardDevice.UnInit();
        super.onDestroy();

        //super.onDestroy();
    }




    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
//        Log.d("onUserLeaveHint","Home button pressed");
//        ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).moveTaskToFront(getTaskId(), 0);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment).commitNow();







    }
    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    KeyboardDevice2 kboard;
    KeyCodeManager kManager;


    private void KeyBoardMap(){
        kboard=new KeyboardDevice2();
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
                                RouteKeyIndex(nKeyIndex);
                                // Log.e("RouteKeyIndex",String.valueOf(nKeyIndex));

                            }
                            String keyName=KeyCodeManager.getDefaultKeyName(nKeyIndex);
                            String strShow = String.format("KeyIndex:%d", nKeyIndex) + " ScanCode:" + keyData[1]+" KeyName:"+keyName;
                            // ShowMsg(strShow);
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

        if (nVKCode==131) {  //131 for calling invoice
            cashierInvoiceFragment.setBackgroundColor(Color.parseColor("#32cd32"));
            cashierPaymentFragment.setBackgroundColor(Color.parseColor("#f44336"));
            cashierShiftFragment.setBackgroundColor(Color.parseColor("#f44336"));
            replaceFragment(new cashier_invoice());
            Log.d("CASHIER","INVOICE");
        }
        if (nVKCode==132) {  //131 for calling invoice
            cashierInvoiceFragment.setBackgroundColor(Color.parseColor("#f44336"));
            cashierPaymentFragment.setBackgroundColor(Color.parseColor("#32cd32"));
            cashierShiftFragment.setBackgroundColor(Color.parseColor("#f44336"));
            replaceFragment(new cashier_payment());
         //   Log.e("new Frag","2");
            Log.d("CASHIER","PAYMENT");
        }
        if (nVKCode==133) {  //131 for calling invoice
            cashierInvoiceFragment.setBackgroundColor(Color.parseColor("#f44336"));
            cashierPaymentFragment.setBackgroundColor(Color.parseColor("#f44336"));
            cashierShiftFragment.setBackgroundColor(Color.parseColor("#32cd32"));
            Log.d("CASHIER","SHIFT");
            replaceFragment(new cashier_shift());
           // Log.e("new Frag","3");
        }

        if (nVKCode==202){
            printer_settings_class prn = new printer_settings_class(this.getApplicationContext());
            prn.PaperFeed();
        }



    }
    private void SimulateKeyboard(int keyCode) {

        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);


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


}