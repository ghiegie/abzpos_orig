package com.abztrakinc.ABZPOS.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.CASHIER.cashier_cash;
import com.abztrakinc.ABZPOS.CASHIER.cashier_shift_Zread;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;

public class adminMain extends AppCompatActivity {
Button btnProduct,btnReceipt,btnJournal,btnStaff,btnPosSettings,btnRestoreDb,btnreportWriter,btn_Exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btnProduct = findViewById(R.id.btnProduct);
        btnReceipt = findViewById(R.id.btnReceipt);
        btnJournal = findViewById(R.id.btn_Journal);
        btnStaff = findViewById(R.id.btnStaff);
        btnPosSettings = findViewById(R.id.btnPosSettings);
        btnRestoreDb = findViewById(R.id.btnRestoreDb);
        btnreportWriter=findViewById(R.id.btnreportWriter);
        btn_Exit = findViewById(R.id.btn_Exit);

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminMain.this, admin_manage_product.class));

            }
        });
        btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminMain.this, admin_manage_receipt.class));
            }
        });
        btnJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminMain.this, admin_manage_journal.class));
            }
        });

        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminMain.this, admin_manage_staff.class));
            }
        });
        btnPosSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminMain.this, admin_pos_settings.class));
            }
        });
        btnRestoreDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(adminMain.this, "EXPORT", Toast.LENGTH_SHORT).show();
//                ExportBackUP();
//                ExportBackUPSettingsDB();
//                ExportBackUPPOSAndroidDB();

                startActivity(new Intent(adminMain.this, admin_backup.class));



//                SQLiteDatabase db= context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
//                String strsql = "UPDATE ReadingTable set  readingEndOR='"+endOR+"',readingEndBal='"+endBal+"',readingEndTrans='"+endTrans+"'  where readingID=1";
//                db2.execSQL(strsql);




            }
        });

        btnreportWriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminMain.this, admin_manage_report.class));
            }
        });

        btn_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                return;
            }
        });

        KeyBoardMap();






    }

    KeyboardDevice kboard;
    KeyCodeManager kManager;

    @Override
    protected void onResume() {
        super.onResume();
        KeyBoardMap();
    }

    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;

    }
    private void SimulateKeyboard(int keyCode) {

        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;


        if (input.equalsIgnoreCase("Exit")){

            //triggerRebirth();
            onBackPressed();
        }

        if (input.equalsIgnoreCase("D01")){
            startActivity(new Intent(adminMain.this, admin_manage_staff.class));
        }
         if (input.equalsIgnoreCase("D02")){
            startActivity(new Intent(adminMain.this, admin_manage_product.class));
        }
         if (input.equalsIgnoreCase("D03")){
            startActivity(new Intent(adminMain.this, admin_manage_journal.class));
        }
         if (input.equalsIgnoreCase("D04")){
            startActivity(new Intent(adminMain.this, admin_manage_receipt.class));
        }
         if (input.equalsIgnoreCase("D05")){
            startActivity(new Intent(adminMain.this, admin_manage_report.class));
        }
         if (input.equalsIgnoreCase("D06")){
            startActivity(new Intent(adminMain.this, admin_backup.class));
        }
         if (input.equalsIgnoreCase("D07")){
            startActivity(new Intent(adminMain.this, admin_pos_settings.class));
        }
         if (input.equalsIgnoreCase("D08")){
            onBackPressed();
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





}