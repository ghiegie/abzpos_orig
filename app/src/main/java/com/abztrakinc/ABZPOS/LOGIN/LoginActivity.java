package com.abztrakinc.ABZPOS.LOGIN;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.ADMIN.AppService;
import com.abztrakinc.ABZPOS.ADMIN.printer_settings_class;
import com.abztrakinc.ABZPOS.Backup_database;
import com.abztrakinc.ABZPOS.CASHIER.create_journal_entry;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.DatabaseHandlerSettings;
import com.abztrakinc.ABZPOS.DatabaseHelper;
import com.abztrakinc.ABZPOS.MainActivity;
import com.abztrakinc.ABZPOS.MyKeyboard;
import com.abztrakinc.ABZPOS.MyKeyboardLogin;
import com.abztrakinc.ABZPOS.OR_TRANS_ITEM;
import com.abztrakinc.ABZPOS.POSAndroidDB;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.ADMIN.adminMain;
import com.abztrakinc.ABZPOS.ORDERSTATION.ordering_station;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.TCSKeyboard.SoftKeyboard;
import com.abztrakinc.ABZPOS.settingsDB;
import com.abztrakinc.ABZPOS.system_final_date;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    Button btnLogin;
    Button btn_exitPos;
    CheckBox chkAdmin,chkOrdering;
    public EditText editTextTextPassword,editTextTextPersonName;
    private DatabaseHelper db;
    DatabaseHelper myDb;
    POSAndroidDB posAndroidDB;
    DatabaseHandlerSettings databaseHandlerSettings;
    String transactionNumber;
    String printData;
    KeyboardDevice kboard;
    KeyCodeManager kManager;
    public int nMsgCnt = 0;
    private ScrollView myScroll;
    private TextView tvMsg;
    private EditText edKeyIndex;
    private EditText edKeyInput;
    settingsDB settingsDB;
    TextView pck_packageName,tv_systemDate;
    int CursorKeyboard=0; // cursor2 == Last busines date dialog





    int stat=1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CursorKeyboard=0;


        startService(new Intent(getBaseContext(), AppService.class));


        // Check if the permission is not granted
        if (ContextCompat.checkSelfPermission(this, getString(R.string.permission_access_content_provider)) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{getString(R.string.permission_access_content_provider)}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted
            Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

        }


        if (checkPermission()){
            Toast.makeText(this, "WE HAVE PERMISSION", Toast.LENGTH_SHORT).show();
        }else{
           requestPermission();
        }

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult( ActivityResult result ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager())
                        Toast.makeText(LoginActivity.this,"We Have Permission",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "You Denied the permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "You Denied the permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btnLogin =(Button) findViewById(R.id.btnLogin);
        chkAdmin =(CheckBox)findViewById(R.id.chkAdmin);
        chkOrdering =(CheckBox)findViewById(R.id.chkOrdering);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        tv_systemDate= findViewById(R.id.tv_systemDate);
        systemDateCompare=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

//        tv_systemDate.setText(systemDateCompare);
//        pck_packageName = findViewById(R.id.pck_packageName);
        closeKeyboard();


        editTextTextPersonName.setFocusable(true);
        editTextTextPersonName.requestFocus(1);
        editTextTextPersonName.setFocusableInTouchMode(true);
        //editTextTextPersonName.setInputType(InputType.TYPE_NULL);

        //for testing auto create account
        settingsDB = new settingsDB(this);
        POSAndroidDB posAndroidDB = new POSAndroidDB(this);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandlerSettings = new DatabaseHandlerSettings(this);
        try {
            settingsDB.insertAccountTest();
//            posAndroidDB.insertCategoryTest();
//            posAndroidDB.insertSubCategoryTest();
//
//            posAndroidDB.insertProductTest();
//          //  posAndroidDB.insertProductTest2();
//            posAndroidDB.insertProductTest3();
//            posAndroidDB.insertProductTest4();
//            posAndroidDB.insertProductTest5();
//            posAndroidDB.insertProductTest6();
//            posAndroidDB.insertProductTest7();
//            posAndroidDB.insertProductTest8();
//            posAndroidDB.insertProductTest9();
//            posAndroidDB.insertProductTest10();
                //databaseHandler.Bank();
        databaseHandler.insertResetCtr();

        }
        catch (Exception ex){

        }

        //terminal status =1 open =0 off
        hideSystemBars();
        createDirectory();



        Backup_database backup_database = new Backup_database();
        backup_database.createBackupUSBFolder();


        //database






        //create outputDb

        posAndroidDB = new POSAndroidDB(this);

        //settingsDB settingsDB = new settingsDB(this);



        int Activate=1;
        //database to be copied
        if (Activate==1) {
            db = new DatabaseHelper(this);
            try {
                db.createDataBase();
                Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();

            } catch (IOException exception) {
                Toast.makeText(this, "NO V6BO FOUND", Toast.LENGTH_SHORT).show();
            }
        }




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    //showSystemLock();



//                    SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//                    Cursor c = db.rawQuery("select * from Accountsettings where accountNumber='" + editTextTextPersonName.getText().toString() + "'and accountPassword='" + editTextTextPassword.getText().toString() + "'", null);
//                    if (c.getCount() == 0) {
//
//                        if (editTextTextPassword.getText().toString().trim().equals("1") && editTextTextPersonName.getText().toString().trim().equals("1")) {
//                            //|| c.getString(4).equals("1")
//                            //check shift
//
////                            SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
////                            Cursor c2 = db.rawQuery("select * from ShiftingTable", null);
////                            if (c2.getCount() == 0) {
////                                boolean isInserted = settingsDB.insertShiftTable("", "", "");
////                                if (isInserted = true) {
////                                    Toast.makeText(LoginActivity.this, "ACTIVE USER : " + "ADMIN", Toast.LENGTH_SHORT).show();
////                                } else {
////
////                                }
////                            }
//
//
////                            if (chkAdmin.isChecked()) {
//                               // stat=0;
//                                startActivity(new Intent(LoginActivity.this, adminMain.class));
//                              //  //checkTerminalStatus();
////                            }
////                            if (chkOrdering.isChecked()) {
////                                stat=0;
////                                startActivity(new Intent(LoginActivity.this, ordering_station.class));
////
////                            }
////                            if (!chkAdmin.isChecked() && !chkOrdering.isChecked()) {
////                                stat = 1;
////                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
////                                if (stat == 1) {
////                                 //   checkTerminalStatus();
////                                }
////
////                            }
//
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Login Credentials Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    else {
//
//                        SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//                        Cursor c2 = db.rawQuery("select * from ShiftingTable", null);
//                        if (c2.getCount() == 0) {
//                            settingsDB.insertShiftTable("", "1", editTextTextPersonName.getText().toString());
//
//                            Cursor c3 = db.rawQuery("select * from ShiftingTable", null);
//                            c3.moveToNext();
//                            int shftActv;
//                            if (c3.getString(2).trim().equals("")) {
//                                shftActv = 1;
//
//                                String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
//                                db2.execSQL(strsql);
//                                settingsDB.insertReadTable();
//
//                            } else {
//                                shftActv = Integer.valueOf(c3.getString(2));
//                                String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
//                                db2.execSQL(strsql);
//
//                                settingsDB.insertReadTable();
//
//
//                            }
//
//
//                            // SQLiteDatabase db2 = getActivity().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//
//                            Cursor item2 = db2.rawQuery("select indicatorStatus from ReadingIndicator where indicatorID=1", null);
//                            if (item2.getCount()==0){
//                                readingIndicator=1;
//
//
//
//
//
//                            }
//                            else{
//                                item2.moveToFirst();
//                                readingIndicator = item2.getInt(0);
//
//                            }
//
//                            if (readingIndicator==1){
//                                shift_active shift_active=new shift_active();
//                                // shift_active.getShiftingTable(getApplicationContext());
//
//                                String strsql2 = "UPDATE ReadingTable set " +
////                                "readingID='"+c2.getString(2)+"'," +
//                                        "readingPOS='"+shift_active.getPOSCounter()+"'," +
//                                        "readingCashierName='" + editTextTextPersonName.getText().toString() + "'," +
//                                        "readingShift='" + c3.getString(2) + "'"+
//                                        // reading starts when creating new transaction
//                                        //"readingEndOR='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingBegBal='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingEndBal='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingCashFloat='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingCash='"+editTextTextPersonName.getText().toString()+"',"+
//
//                                        "where readingID=1";
//                                db2.execSQL(strsql2);
//
//
//                                Toast.makeText(LoginActivity.this, "UPDATED", Toast.LENGTH_SHORT).show();
//                            }
//
//
//
//
//
//                        }
//                        else {
//                            c2.moveToNext();
//                            int shftActv;
//                            if (c2.getString(2).trim().equals("")) {
//                                shftActv = 1;
//
//                                String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
//                                db2.execSQL(strsql);
//                                settingsDB.insertReadTable();
//
//                            } else {
//                                shftActv = Integer.valueOf(c2.getString(2));
//                                String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
//                                db2.execSQL(strsql);
//
//                                settingsDB.insertReadTable();
//
//
//                            }
//
//
//                           // SQLiteDatabase db2 = getActivity().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//
//                            Cursor item2 = db2.rawQuery("select indicatorStatus from ReadingIndicator where indicatorID=1", null);
//                            if (item2.getCount()==0){
//                                readingIndicator=1;
//
//
//
//
//
//                            }
//                            else{
//                                item2.moveToFirst();
//                                readingIndicator = item2.getInt(0);
//
//                            }
//
//                            if (readingIndicator==1){
//                                shift_active shift_active=new shift_active();
//                                // shift_active.getShiftingTable(getApplicationContext());
//
//                                String strsql2 = "UPDATE ReadingTable set " +
////                                "readingID='"+c2.getString(2)+"'," +
//                                        "readingPOS='"+shift_active.getPOSCounter()+"'," +
//                                        "readingCashierName='" + editTextTextPersonName.getText().toString() + "'," +
//                                        "readingShift='" + c2.getString(2) + "'"+
//                                        // reading starts when creating new transaction
//                                        //"readingEndOR='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingBegBal='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingEndBal='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingCashFloat='"+editTextTextPersonName.getText().toString()+"',"+
//                                        // "readingCash='"+editTextTextPersonName.getText().toString()+"',"+
//
//                                        "where readingID=1";
//                                db2.execSQL(strsql2);
//
//
//                                Toast.makeText(LoginActivity.this, "UPDATED", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        }
//
//
//                        if (chkAdmin.isChecked()) {
//                            stat=0;
//
//                            startActivity(new Intent(LoginActivity.this, adminMain.class));
//
//                           // //checkTerminalStatus();
//                        }
//                        if (chkOrdering.isChecked()) {
//                            stat=0;
//                            startActivity(new Intent(LoginActivity.this, ordering_station.class));
//                        }
//                        if (!chkAdmin.isChecked() && !chkOrdering.isChecked()) {
//
//                            SQLiteDatabase db3 = view.getContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
//                            // system_final_date systemDate = new system_final_date();
//                            Cursor sysdateCursor = db3.rawQuery("select * from SystemDate", null);
//                            if (sysdateCursor.getCount()==0){
//
//                               showDialogDate();
//
//                            }
//                            else{
//
//                                stat = 1;
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//
//                                if (stat == 1) {
//                                  //  checkTerminalStatus();
//                                }
//
//                            }
//
//
//
//
//
//
//                        }
//
//
//
//
//                    }
//
//
//
//                    db.close();

//                } catch (Exception ex){
//                    Toast.makeText(LoginActivity.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                }

//                if (chkAdmin.isChecked()){
//                    login();
//                }
//                else{
//                    showDialogDate();
//                }

               // checkAccountType();
                changeEditText();

               // KeyboardDevice.UnInit();

            }
        });
        btn_exitPos=findViewById(R.id.btn_exitPOS);
        btn_exitPos.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                finishAndRemoveTask();





            }
        });
      //  showDialogDate();


//       try{
//          // showSystemLock();
//       }
//        catch (Exception ex){
//
//        }

        //ShowKeyboard();
        //KeyBoardMap();
        versionName();


        loadShiftActive();
        loadBizDate();


//off this to not show systemlock
       // showSystemLock();




        //Hide Keyboard






    }






    private static final int PERMISSION_REQUEST_CODE = 100;




        // Handle permission request result
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == PERMISSION_REQUEST_CODE) {
                // Check if the permission is granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                    // You can proceed with accessing the content provider
                } else {
                    // Permission is denied
                    // You may inform the user or take appropriate action
                }
            }
        }


    private void loadShiftActive(){

        //TextView tv_businessDate = this.findViewById(R.id.tv_businessDate);
        TextView tv_cashierActive = this.findViewById(R.id.tv_cashierActive);
        TextView tv_shiftActive = this.findViewById(R.id.tv_shiftActive);

        SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor checkShift = db2.rawQuery("select * from ShiftingTable", null);
        if (checkShift.getCount()!=0){
            if (checkShift.moveToFirst()){

                tv_cashierActive.setText(checkShift.getString(3));
                tv_shiftActive.setText(checkShift.getString(2));

            }
        }

        db2.close();

    }
    private void loadBizDate(){

        TextView tv_businessDate = this.findViewById(R.id.tv_businessDate);


        SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor checkShift = db2.rawQuery("select * from SystemDate", null);
        if (checkShift.getCount()!=0){
            if (checkShift.moveToFirst()){

                tv_businessDate.setText(checkShift.getString(1));


            }
        }

        db2.close();

    }


//    private void ShowKeyboard() {
//
//        MyKeyboardLogin keyboard = findViewById(R.id.keyboardLogin);
//
//
//        // prevent system keyboard from appearing when EditText is tapped
//
//        editTextTextPersonName.requestFocus();
//
//        if (editTextTextPersonName.hasFocus()) {
//            Log.e("usernumber", "has foucs");
//
//            editTextTextPersonName.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            editTextTextPersonName.setTextIsSelectable(true);
//
//            int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
//
//            ((EditText) findViewById(R.id.editTextTextPersonName)).setInputType(type);
//            //   pass the InputConnection from the EditText to the keyboard
//
//            InputConnection ic = editTextTextPersonName.onCreateInputConnection(new EditorInfo());
//            Log.e("loginOutput", String.valueOf(ic));
//            keyboard.setInputConnection(ic);
//
//
//
//        }
//        else if (editTextTextPassword.hasFocus()) {
//            editTextTextPassword.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            editTextTextPassword.setTextIsSelectable(true);
//
//            int type = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
//
//            ((EditText) findViewById(R.id.editTextTextPassword)).setInputType(type);
//            //   pass the InputConnection from the EditText to the keyboard
//
//            InputConnection ic = editTextTextPassword.onCreateInputConnection(new EditorInfo());
//            Log.e("loginOutput", String.valueOf(ic));
//            keyboard.setInputConnection(ic);
//
//            Log.e("userpassword", "has foucs");
//        }
//
//
//
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        ShowKeyboard();
        KeyBoardMap();
        editTextTextPersonName.setText("");
        editTextTextPassword.setText("");
        editTextTextPersonName.requestFocus();
        userType=0;
        dialogCursor=0;
        CursorKeyboard=0;
        lastBusinessDate=0;
        showFinalDialog=0;
        openNewDefaultDay=0;
        loadShiftActive();
        loadBizDate();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        // KeyBoardMap();
    }

    @Override
    public  void onBackPressed(){

        if (alertDialogCursor1o != null && alertDialogCursor1o.isShowing()) {
            // If the dialog is showing, do nothing (preventing back press)
            return;
        }
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            // If the dialog is showing, do nothing (preventing back press)
            return;
        }

    }

    private void ShowKeyboard(){

        Button mButton1 = (Button) findViewById(R.id.button_1);
        Button mButton2 = (Button) findViewById(R.id.button_2);
        Button mButton3 = (Button) findViewById(R.id.button_3);
        Button mButton4 = (Button) findViewById(R.id.button_4);
        Button  mButton5 = (Button) findViewById(R.id.button_5);
        Button mButton6 = (Button) findViewById(R.id.button_6);
        Button mButton7 = (Button) findViewById(R.id.button_7);
        Button mButton8 = (Button) findViewById(R.id.button_8);
        Button  mButton9 = (Button) findViewById(R.id.button_9);
        Button mButton0 = (Button) findViewById(R.id.button_0);

        Button mButtonClear=(Button) findViewById(R.id.button_Clear);
        ImageButton mNext =  findViewById(R.id.button_next);
       // editTextTextPersonName.requestFocus();
       // editTextTextPersonName.setBackground(ContextCompat.getDrawable(this,R.drawable.custom_edittext_login_green));




            mButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("1");
                    Log.e("TEST","INPUTKEY 1");
                }
            });
            mButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("2");
                    Log.e("TEST","INPUTKEY 2");
                }
            });
            mButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("3");
                    Log.e("TEST","INPUTKEY 3");
                }
            });
            mButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("4");
                    Log.e("TEST","INPUTKEY 4");
                }
            });
            mButton5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("5");
                    Log.e("TEST","INPUTKEY 5");
                }
            });
            mButton6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("6");
                    Log.e("TEST","INPUTKEY 6");
                }
            });
            mButton7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("7");
                    Log.e("TEST","INPUTKEY 7");
                }
            });
            mButton8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("8");
                    Log.e("TEST","INPUTKEY 8");
                }
            });
            mButton9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("9");
                    Log.e("TEST","INPUTKEY 9");
                }
            });
            mButton0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputEdittext("0");
                    Log.e("TEST","INPUTKEY 0");
                }
            });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // inputEdittext("");
                changeEditText();
                Log.e("TEST","INPUTKEY NEXT");
            }
        });
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clearEditText();

            }
        });






    }

    private void inputEdittext(String s){
//        int position = editTextTextPersonName.length();
//        Editable etext = editTextTextPersonName.getText();
//        Selection.setSelection(etext,position);
        if (editTextTextPersonName.hasFocus()) {
            String content = editTextTextPersonName.getText().toString();
            int index = editTextTextPersonName.getSelectionStart() >= 0 ? editTextTextPersonName.getSelectionStart() : 0;
            StringBuilder sBuilder = new StringBuilder(content);
            sBuilder.insert(index, s);
            editTextTextPersonName.setText(sBuilder.toString());
            editTextTextPersonName.setSelection(index + s.length());
        }
        else{
            String content = editTextTextPassword.getText().toString();
            int index = editTextTextPassword.getSelectionStart() >= 0 ? editTextTextPassword.getSelectionStart() : 0;
            StringBuilder sBuilder = new StringBuilder(content);
            sBuilder.insert(index, s);
            editTextTextPassword.setText(sBuilder.toString());
            editTextTextPassword.setSelection(index + s.length());
        }

    }
    private void changeEditText(){
        if (editTextTextPersonName.hasFocus()){
            editTextTextPassword.requestFocus();
            editTextTextPersonName.setTextColor(Color.GRAY);
            editTextTextPassword.setTextColor(Color.BLACK);
            Log.e("TAG", "changeEditText: 0" );
        }


        else if (editTextTextPassword.hasFocus()){

           // editTextTextPersonName.requestFocus();
          //  btnLogin.requestFocus();
           // checkAccountType();
            checkAccountType();
            editTextTextPersonName.setTextColor(Color.BLACK);
            editTextTextPassword.setTextColor(Color.GRAY);
            editTextTextPersonName.requestFocus();
            Log.e("TAG", "changeEditText: 1" );
        }
//        else{
//            checkAccountType();
//        }

    }
    private void clearEditText(){
        if (editTextTextPersonName.hasFocus()){
            editTextTextPersonName.setText("");
            Log.e("TAG", "changeEditText: 0 clear" );
        }
        else{
            editTextTextPassword.setText("");
            Log.e("TAG", "changeEditText: 1 clear" );
        }
    }




    private void versionName() {
        PackageManager pm = getApplicationContext().getPackageManager();
        String pkgName = this.getPackageName();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String ver = pkgInfo.versionName;
       // pck_packageName.setText("ABZPOSS"+" " +ver);
    }

    //check if admin
    int userType;
    AlertDialog alertDialog;
    int showFinalDialog=0;
    private void SimulateKeyboard(int keyCode) {


        kManager = new KeyCodeManager();
        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
        String input = kManager.getDefaultKeyName(keyCode);
        Log.d("TAG", "input: "+input);
        int digitType=1; //1 number //2 letter
        final int PRESS_INTERVAL =  700;

        //   Log.d("TAG", "SimulateKeyboard Keypress: "+MultipleKeypress());





        // if(input.equals("A") || input)

        String[] allowedInput = {"0","1","2","3","4","5","6","7","8","9","A","B",
                "C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S",
                "T","U","V","W","X","Y","Z","00"};

        for (String element : allowedInput){
            if (element ==  input){
                if (digitType==1) {
                    if (editTextTextPersonName.hasFocus()) {

//            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
//            KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
//
//            editTextTextPersonName.onKeyDown(keyCode, keyEventDown);
//            editTextTextPersonName.onKeyUp(keyCode, keyEventUp);

                        if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==0) {
                            int start = Math.max(editTextTextPersonName.getSelectionStart(), 0);
                            int end = Math.max(editTextTextPersonName.getSelectionEnd(), 0);
                            editTextTextPersonName.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (editTextTextPersonName.length() == 0) {
                                return;
                            }
                        }


                        else if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==2) {
                            int start = Math.max(editTextTextPersonName.getSelectionStart(), 0);
                            int end = Math.max(editTextTextPersonName.getSelectionEnd(), 0);
                            editTextTextPersonName.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (editTextTextPersonName.length() == 0) {
                                return;
                            }
                        }

                        else  if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==22 && lastBusinessDate ==2) {
                            int start = Math.max(editTextTextPersonName.getSelectionStart(), 0);
                            int end = Math.max(editTextTextPersonName.getSelectionEnd(), 0);
                            editTextTextPersonName.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (editTextTextPersonName.length() == 0) {
                                return;
                            }
                        }
                    }
                    else  if (editTextTextPassword.hasFocus()) {

//                        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
//                        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
//                        editTextTextPassword.onKeyDown(keyCode, keyEventDown);
//                        editTextTextPassword.onKeyUp(keyCode, keyEventUp);
//                        if (CursorKeyboard==0) {
//                            if (keyCode == 66) {
//                                if (editTextTextPersonName.length() == 0) {
//                                    editTextTextPersonName.requestFocus(0);
//                                    if (editTextTextPassword.length() != 0) {
//                                        return;
//                                        // login();
//                                    } else {
//                                        checkAccountType();
//                                    }
//                                } else if ((editTextTextPersonName.length() != 0) && (editTextTextPassword.length() != 0)) {
//                                    checkAccountType();
//                                }
//                            }
//                        }
//
//                        if (dialogCursor==0) {
                        if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==0) {
                            int start = Math.max(editTextTextPassword.getSelectionStart(), 0);
                            int end = Math.max(editTextTextPassword.getSelectionEnd(), 0);
                            editTextTextPassword.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (editTextTextPassword.length() == 0) {
                                return;
                            }
                        }
                        else if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==2) {
                            int start = Math.max(editTextTextPassword.getSelectionStart(), 0);
                            int end = Math.max(editTextTextPassword.getSelectionEnd(), 0);
                            editTextTextPassword.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (editTextTextPassword.length() == 0) {
                                return;
                            }
                        }
                       else if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==22 && lastBusinessDate ==2) {
                            int start = Math.max(editTextTextPassword.getSelectionStart(), 0);
                            int end = Math.max(editTextTextPassword.getSelectionEnd(), 0);
                            editTextTextPassword.getText().replace(Math.min(start, end), Math.max(start, end),
                                    input, 0, input.length());


                            if (editTextTextPassword.length() == 0) {
                                return;
                            }
                        }


                    }
                }
                else{

//                        MultipleKeypress(kManager.getDefaultKeyName(keyCode));
//                        Log.e("Final Letter",FinalLetterToWrite);
//                        editTextTextPersonName.setText(FinalLetterToWrite);
                    //   Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
                    //editTextTextPersonName.set



                }
            }

        }

        if (input.equalsIgnoreCase("Clear")){
            if (editTextTextPersonName.hasFocus()){
                editTextTextPersonName.setText("");
            }
            if (editTextTextPassword.hasFocus()){
                editTextTextPassword.setText("");
            }
        }

        if (input.equalsIgnoreCase("EC")){
            if (editTextTextPersonName.hasFocus()){
                int length = editTextTextPersonName.getText().length();
                if (length > 0) {
                    editTextTextPersonName.getText().delete(length - 1, length);
                }
            }
            if (editTextTextPassword.hasFocus()){
                int length = editTextTextPassword.getText().length();
                if (length > 0) {
                    editTextTextPassword.getText().delete(length - 1, length);
                }
            }
        }















        if (input.equalsIgnoreCase("Total")){
            View view = this.getCurrentFocus();

//
//            if (dialogCursor==1){
//                Log.e("TAG", "SimulateKeyboard: open new day counter" );
//
//               // _Cursor1oConfirm(view);
//
//                //
//
//            }
//            else if (dialogCursor==2){
////                _Cursor1oConfirm(view);
////                loadShiftActive();
////                loadBizDate();
//            }
//            else if(dialogCursor==0){
//                changeEditText();
//            }


//            if (CursorKeyboard==1){
//
//                if (userType==2) {
//                    if (input.equalsIgnoreCase("Sub total")) {
//                        Log.e("SELECT BUTTON", "ADMIN");
//                        chkAdmin.setChecked(true);
//                        login();
//                        alertDialog.dismiss();
//                        CursorKeyboard=0;
//                        userType=0;
//
//                    }
//                    if (input.equalsIgnoreCase("Total")) {
//                        Log.e("SELECT BUTTON", "CASHIER");
//                        chkAdmin.setChecked(false);
//                        showDialogDate();
//                        alertDialog.dismiss();
//
//                        loadShiftActive();
//                        loadBizDate();
//                        CursorKeyboard=3;
//                        userType=0;
//
//
//                        //  alertDialog.dismiss();
//                    }
//                }
//
//
//            }
//            else if(CursorKeyboard==3){
//                //Last business date dialog
//                _Cursor1oConfirm(view);
//                loadShiftActive();
//                loadBizDate();
//
//            }
//            else{
//                changeEditText();
//            }
//            if (CursorKeyboard==2){
//
//                btn_confirm.setOnKeyListener(new View.OnKeyListener()
//                {
//                    public boolean onKey(View v, int keyCode, KeyEvent event)
//                    {
//                        if (event.getAction() == KeyEvent.ACTION_DOWN)
//                        {
//                            switch (keyCode)
//                            {
//                                case KeyEvent.KEYCODE_DPAD_CENTER:
//                                case KeyEvent.KEYCODE_ENTER:
//                                    //addCourseFromTextBox();
//                                    Log.e("ENTER","ENTER");
//                                    return true;
//                                default:
//                                    break;
//                            }
//                        }
//                        return false;
//                    }
//                });
//
//            }

            if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==0 ){
                changeEditText();
            }
           else if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==22 && lastBusinessDate ==0 ){
                changeEditText();
            }

            else if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==2 ){
                changeEditText();
            }
            else if (showFinalDialog==0 && CursorKeyboard== 0 && openNewDefaultDay ==22 && lastBusinessDate ==2 ){
                changeEditText();
            }
            else if (CursorKeyboard==1){

                Toast.makeText(this, "cashier option  " , Toast.LENGTH_SHORT).show();

                Log.d("TAG", "openNewDefaultDay: "+openNewDefaultDay);
                Log.d("TAG", "CursorKeyboard: "+CursorKeyboard);
                chkAdmin.setChecked(false);
                showDialogDate();
                alertDialog.dismiss();
                CursorKeyboard=0;


            }
            else if (CursorKeyboard==11){
                Toast.makeText(this, "Show dialog date", Toast.LENGTH_SHORT).show();
            }
            else if (openNewDefaultDay==22 && CursorKeyboard==0 && lastBusinessDate==1){
            //   Toast.makeText(this, "OPENING NEW DAY 0001(1)", Toast.LENGTH_SHORT).show();
//                _Cursor1o(view);
                _Cursor1x(this.getCurrentFocus());
            }
            else if (openNewDefaultDay==0 && CursorKeyboard ==0 && lastBusinessDate ==1){
            //    Toast.makeText(this, "OPENING NEW DAY 0001(2)", Toast.LENGTH_SHORT).show();
                _Cursor1x(this.getCurrentFocus());
            }
            else if (openNewDefaultDay==22 && CursorKeyboard ==0 && lastBusinessDate ==2){

                _Cursor1xConfirm(view);
                login();
                dialogCursor1.dismiss();
                alertDialog2.dismiss();

            }

            else if (openNewDefaultDay==0 && CursorKeyboard ==0 && lastBusinessDate ==2){

//                _Cursor1xConfirm(this.getCurrentFocus());
//                dialogCursor1.dismiss();
//                alertDialog2.dismiss();
                _Cursor1xConfirm(view);
                login();
                dialogCursor1.dismiss();
                alertDialog2.dismiss();

            }

            else if (openNewDefaultDay==22 && CursorKeyboard ==0 && lastBusinessDate ==3){

                _Cursor1o(this.getCurrentFocus());
                dialogCursor1.dismiss();
                alertDialog2.dismiss();
            }

            else if (openNewDefaultDay==0 && CursorKeyboard ==0 && lastBusinessDate ==3){

                _Cursor1o(this.getCurrentFocus());
                dialogCursor1.dismiss();
                alertDialog2.dismiss();
            }
            else if (openNewDefaultDay==22 && CursorKeyboard ==0 && lastBusinessDate ==4){



                if(newBizDate.equals(prevBizDate)){
                    Toast.makeText(LoginActivity.this, ""+newBizDate + " is already used", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }
                else{

                    dialogCursor1.dismiss();
                    // alertDialog2.dismiss();
                    alertDialogCursor1o.dismiss();
                    _Cursor1oConfirm(this.getCurrentFocus());
                }





            }
            else if (openNewDefaultDay==0 && CursorKeyboard ==0 && lastBusinessDate ==4){


//                dialogCursor1.dismiss();
//               // alertDialog2.dismiss();
//                alertDialogCursor1o.dismiss();
//                _Cursor1oConfirm(this.getCurrentFocus());

                if(newBizDate.equals(prevBizDate)){
                    Toast.makeText(LoginActivity.this, ""+newBizDate + " is already used", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }
                else{

                    dialogCursor1.dismiss();
                    // alertDialog2.dismiss();
                    alertDialogCursor1o.dismiss();
                    _Cursor1oConfirm(this.getCurrentFocus());
                }



            }


//            else if(showFinalDialog==1){
//                if (userType==2) {
//                    if (input.equalsIgnoreCase("Total")) {
//                        Log.e("SELECT BUTTON", "CASHIER");
////                        chkAdmin.setChecked(false);
////                        showDialogDate();
////                        alertDialog.dismiss();
////
////                        loadShiftActive();
////                        loadBizDate();
////                        showFinalDialog=2;
//
//
//                        //  alertDialog.dismiss();
//                    }
//                }
//            }
//            else if(showFinalDialog==2){
////                _Cursor1oConfirm(view);
////                loadShiftActive();
////                loadBizDate();
////                showFinalDialog=3;
//            }
//            else if(showFinalDialog==3){
////                _Cursor1x(view);
////                showFinalDialog=4;
//
//            }
//            else if(showFinalDialog==4) {
////                _Cursor1xConfirm(view);
//            }
//            else{
//
//            }






        }
        if (input.equalsIgnoreCase("Sub Total")){
//            if (dialogCursor==1){
//                Log.e("TAG", "SimulateKeyboard: Cursor1  subtotal" );
//                dialogCursor1.dismiss();
//                dialogCursor=0;
//            }
//            else if (dialogCursor==2){
//                Log.e("TAG", "SimulateKeyboard: Cursor2  subtotal" );
//                alertDialogCursor1o.dismiss();
//                dialogCursor=1;
//            }
//            else if(dialogCursor==0){
//                Log.e("TAG", "SimulateKeyboard: Cursor0  subtotal" );
//                //changeEditText();
//                alertDialog.dismiss();
//            }


            if (showFinalDialog==1 && CursorKeyboard== 1 && openNewDefaultDay ==0 && lastBusinessDate ==0 ){
                Log.e("TAG", "admin option" );

                 chkAdmin.setChecked(true);
                 login();
                 alertDialog.dismiss();
                CursorKeyboard=0;
            }

             else if(showFinalDialog==1 && CursorKeyboard== 0 && openNewDefaultDay ==22 && lastBusinessDate ==1 ){
                alertDialog.dismiss();
                dialogCursor1.dismiss();
            //    alertDialogCursor1o.dismiss();
                showFinalDialog=0;
                CursorKeyboard=0;
                openNewDefaultDay=0;
                lastBusinessDate=0;
             }

            else if(showFinalDialog==1 && CursorKeyboard== 0 && openNewDefaultDay ==22 && lastBusinessDate ==2 ){
                alertDialog.dismiss();
                dialogCursor1.dismiss();
                //    alertDialogCursor1o.dismiss();
                showFinalDialog=0;
                CursorKeyboard=0;
                openNewDefaultDay=0;
                lastBusinessDate=0;
            }

            else if(showFinalDialog==1 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==1 ){
//                alertDialog.dismiss();
               dialogCursor1.dismiss();
               showFinalDialog=0;
               CursorKeyboard=0;
               openNewDefaultDay=0;
               lastBusinessDate=0;
            }

            else if(showFinalDialog==1 && CursorKeyboard== 0 && openNewDefaultDay ==0 && lastBusinessDate ==2 ){
//                alertDialog.dismiss();
                dialogCursor1.dismiss();
                alertDialog2.dismiss();
              //  alertDialogCursor1o.dismiss();
                showFinalDialog=0;
                CursorKeyboard=0;
                openNewDefaultDay=0;
                lastBusinessDate=0;
            }


            Log.d("TAG", "showFinalDialog: "+showFinalDialog);
            Log.d("TAG", "CursorKeyboard: "+CursorKeyboard);
            Log.d("TAG", "openNewDefaultDay: "+openNewDefaultDay);

            Log.d("TAG", "lastBusinessDate: "+lastBusinessDate);





        }







    }

    private void checkAccountType(){

        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select accountType from Accountsettings where accountNumber='" + editTextTextPersonName.getText().toString() + "'and accountPassword='" + editTextTextPassword.getText().toString() + "'", null);
        if(c.getCount()!=0){
            if (c.moveToNext()){
                userType=Integer.parseInt(c.getString(0));
               // Log.d("usertype",userType);


                showFinalDialog=1;
                if (userType==2){
                    //show dialog
                    CursorKeyboard=1;
                    AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_admin_option, null);



                    getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    );
                    builder.setView(alertLayout);
                   alertDialog = builder.create();
                   alertDialog.setCanceledOnTouchOutside(false);

                    Button btn_admin = alertLayout.findViewById(R.id.btn_admin);
                    Button btn_cashier = alertLayout.findViewById(R.id.btn_cashier);
                    ImageButton imgb_exit= alertLayout.findViewById(R.id.imgb_exit);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //imgb_exit.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    btn_admin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            chkAdmin.setChecked(true);
                            login();
                            alertDialog.dismiss();
                        }
                    });
                    btn_cashier.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            chkAdmin.setChecked(false);
                            showDialogDate();
                            alertDialog.dismiss();
                        }
                    });

                    imgb_exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            alertDialog.dismiss();
//                            CursorKeyboard=0;
                        }
                    });





                    alertDialog.show();





                }
                else{
                    CursorKeyboard=11;

                    showDialogDate();
                }
            }
        }


        db.close();
    }




    public void login(){
        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from Accountsettings where accountNumber='" + editTextTextPersonName.getText().toString() + "'and accountPassword='" + editTextTextPassword.getText().toString() + "'", null);
        if (c.getCount() == 0) {

//            if (editTextTextPassword.getText().toString().trim().equals("1") && editTextTextPersonName.getText().toString().trim().equals("1")) {
//                //|| c.getString(4).equals("1")
//                //check shift
//
////                            SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
////                            Cursor c2 = db.rawQuery("select * from ShiftingTable", null);
////                            if (c2.getCount() == 0) {
////                                boolean isInserted = settingsDB.insertShiftTable("", "", "");
////                                if (isInserted = true) {
////                                    Toast.makeText(LoginActivity.this, "ACTIVE USER : " + "ADMIN", Toast.LENGTH_SHORT).show();
////                                } else {
////
////                                }
////                            }
//
//
////                            if (chkAdmin.isChecked()) {
//                // stat=0;
//                startActivity(new Intent(LoginActivity.this, adminMain.class));
//                //  //checkTerminalStatus();
////                            }
////                            if (chkOrdering.isChecked()) {
////                                stat=0;
////                                startActivity(new Intent(LoginActivity.this, ordering_station.class));
////
////                            }
////                            if (!chkAdmin.isChecked() && !chkOrdering.isChecked()) {
////                                stat = 1;
////                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
////                                if (stat == 1) {
////                                 //   checkTerminalStatus();
////                                }
////
////                            }
//
//            } else {
                Toast.makeText(LoginActivity.this, "Login Credentials Error", Toast.LENGTH_SHORT).show();
//            }
        }
        else {

            SQLiteDatabase db2 = getApplicationContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
            Cursor c2 = db.rawQuery("select * from ShiftingTable", null);



            if (chkAdmin.isChecked()) {

                if (userType==2) {
                    stat = 0;

                    startActivity(new Intent(LoginActivity.this, adminMain.class));
                }
                if (stat==1){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

                // //checkTerminalStatus();
            }
            if (chkOrdering.isChecked()) {
                stat=0;
                startActivity(new Intent(LoginActivity.this, ordering_station.class));
            }
            if (!chkAdmin.isChecked() && !chkOrdering.isChecked()) {



                // region for updating shiftable
                if (c2.getCount() == 0) {
                    settingsDB.insertShiftTable("", "1", editTextTextPersonName.getText().toString());

                    Cursor c3 = db.rawQuery("select * from ShiftingTable", null);
                    c3.moveToNext();
                    int shftActv;
                    if (c3.getString(2).trim().equals("")) {
                        shftActv = 1;

                        String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
                        db2.execSQL(strsql);
                        settingsDB.insertReadTable();

                    } else {
                        shftActv = Integer.valueOf(c3.getString(2));
                        String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
                        db2.execSQL(strsql);

                        settingsDB.insertReadTable();


                    }


                    // SQLiteDatabase db2 = getActivity().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);

                    Cursor item2 = db2.rawQuery("select indicatorStatus from ReadingIndicator where indicatorID=1", null);
                    if (item2.getCount()==0){
                        readingIndicator=1;





                    }
                    else{
                        item2.moveToFirst();
                        readingIndicator = item2.getInt(0);

                    }

                    if (readingIndicator==1){
                        shift_active shift_active=new shift_active();
                        // shift_active.getShiftingTable(getApplicationContext());

                        String strsql2 = "UPDATE ReadingTable set " +
//                                "readingID='"+c2.getString(2)+"'," +
                                "readingPOS='"+shift_active.getPOSCounter()+"'," +
                                "readingCashierName='" + editTextTextPersonName.getText().toString() + "'," +
                                "readingShift='" + c3.getString(2) + "'"+
                                // reading starts when creating new transaction
                                //"readingEndOR='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingBegBal='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingEndBal='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingCashFloat='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingCash='"+editTextTextPersonName.getText().toString()+"',"+

                                "where readingID=1";
                        db2.execSQL(strsql2);


                        //Toast.makeText(LoginActivity.this, "UPDATED", Toast.LENGTH_SHORT).show();
                    }





                }
                else {
                    c2.moveToNext();
                    int shftActv;
                    if (c2.getString(2).trim().equals("")) {
                        shftActv = 1;

                        String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
                        db2.execSQL(strsql);
                        settingsDB.insertReadTable();

                    } else {

                        //

                        if (c2.getString(3).trim().equals("")){
                            //  stat=0;
                            Toast.makeText(this, "NO ACTIVE USER", Toast.LENGTH_LONG).show();
                            stat=1;
                            //while(c3.moveToNext())
                            shftActv = Integer.valueOf(c2.getString(2));
                            String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
                            db2.execSQL(strsql);

                            settingsDB.insertReadTable();

                        }
                        else{
                            Cursor c3 = db.rawQuery("select * from ShiftingTable where shiftActiveUser='"+editTextTextPersonName.getText().toString()+"'", null);


                            Toast.makeText(this, "USER : "+c2.getString(3).toString() + " Still Active", Toast.LENGTH_LONG).show();
                            if (c2.getString(3).equals(editTextTextPersonName.getText().toString())){
                                stat=1;
                                //Toast.makeText(this, "USER : "+c2.getString(3).toString() + "stat 1", Toast.LENGTH_LONG).show();
                            }
                            else{
                                stat=0;
                                // Toast.makeText(this, "USER : "+c2.getString(3).toString() + "Stat 2", Toast.LENGTH_LONG).show();
                            }

                            editTextTextPassword.setText("");
                            editTextTextPersonName.setText("");

                            changeEditText();
                            loadShiftActive();
                            loadBizDate();
                            showFinalDialog=0;
                            editTextTextPersonName.requestFocus();
                            // changeEditText();
                        }





//                    if(c3.getCount()!=0){
//
//                        // there is another account logged in
//                        Toast.makeText(this, "USER : "+c2.getString(3).toString() + "Still Active(2)", Toast.LENGTH_LONG).show();
//                        stat=0;
//                    }
//                    else{
//                        Toast.makeText(this, "USER : "+c2.getString(3).toString() + "no active", Toast.LENGTH_LONG).show();
//                        stat=1;
//                        //while(c3.moveToNext())
//                        shftActv = Integer.valueOf(c2.getString(2));
//                        String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
//                        db2.execSQL(strsql);
//
//                        settingsDB.insertReadTable();
//                    }

//                    stat=0;
//                    if (c3.getCount()!=0){
//                        Toast.makeText(this, "USER : "+c2.getString(3).toString() + "Still Active(2)", Toast.LENGTH_LONG).show();
//                        if (c2.getString(3).equals(editTextTextPersonName.getText().toString())){
//                            stat=1;
//                        }
//                        else{
//                            stat=0;
//                        }
//                    }
//                    else if(c3.getCount()==0){
//                        stat=0;
//                        Toast.makeText(this, "NO ACTIVE USER", Toast.LENGTH_LONG).show();
////                        stat=1;
////                        //while(c3.moveToNext())
////                        shftActv = Integer.valueOf(c2.getString(2));
////                        String strsql = "UPDATE ShiftingTable set  shiftActive='" + shftActv + "',shiftActiveUser='" + editTextTextPersonName.getText().toString() + "'where shiftID=1";
////                        db2.execSQL(strsql);
////
////                        settingsDB.insertReadTable();
//                    }




                    }


                    // SQLiteDatabase db2 = getActivity().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);

                    Cursor item2 = db2.rawQuery("select indicatorStatus from ReadingIndicator where indicatorID=1", null);
                    if (item2.getCount()==0){
                        readingIndicator=1;





                    }
                    else{
                        item2.moveToFirst();
                        readingIndicator = item2.getInt(0);

                    }

                    if (readingIndicator==1){
                        shift_active shift_active=new shift_active();
                        // shift_active.getShiftingTable(getApplicationContext());

                        String strsql2 = "UPDATE ReadingTable set " +
//                                "readingID='"+c2.getString(2)+"'," +
                                "readingPOS='"+shift_active.getPOSCounter()+"'," +
                                "readingCashierName='" + editTextTextPersonName.getText().toString() + "'," +
                                "readingShift='" + c2.getString(2) + "'"+
                                // reading starts when creating new transaction
                                //"readingEndOR='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingBegBal='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingEndBal='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingCashFloat='"+editTextTextPersonName.getText().toString()+"',"+
                                // "readingCash='"+editTextTextPersonName.getText().toString()+"',"+

                                "where readingID=1";
                        db2.execSQL(strsql2);


                        //  Toast.makeText(LoginActivity.this, "UPDATED", Toast.LENGTH_SHORT).show();
                    }


                }

                //endregion







                SQLiteDatabase db3 = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
                // system_final_date systemDate = new system_final_date();
                Cursor sysdateCursor = db3.rawQuery("select * from SystemDate", null);
                if (sysdateCursor.getCount()==0){

                    showDialogDate();

                }
                else{
                    //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                    //stat = 1;


                    if (stat == 1) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        //  checkTerminalStatus();
                    }

                }






            }




        }



        db.close();
    }




    private void closeKeyboard(){

        SoftKeyboard keyboardSettings = new SoftKeyboard();
        int Hide=keyboardSettings.getShowKboard();
        if (Hide!=1){
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }


    }

    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;
        Log.d("RESUME KEYBOARD","RESUME");

    }
    @Override
    protected void onDestroy() {
        KeyboardDevice.UnInit();
        kboard.UnInit();
        super.onDestroy();
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
                              //  RouteKeyIndex(nKeyIndex);
                                SimulateKeyboard(nKeyIndex);
                               // Log.e("RouteKeyIndex",String.valueOf(nKeyIndex));

                            }
                            String keyName=KeyCodeManager.getDefaultKeyName(nKeyIndex);
                            String strShow = String.format("KeyIndex:%d", nKeyIndex) + " ScanCode:" + keyData[1]+" KeyName:"+keyName;
                           // ShowMsg(strShow);
                        } else {
                            Toast.makeText(LoginActivity.this, strMsgInfo, Toast.LENGTH_SHORT).show();

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

//        int nVKCode = KeyCodeManager.getVKCode(nKeyIndex);
//        if (nVKCode == -100) {
//        } else {
//            Log.e("nVKCode",String.valueOf(nVKCode));
//            SimulateKeyboard(nVKCode);
//        }
//        if (nVKCode==123){
//            if (editTextTextPersonName.hasFocus()){
//                editTextTextPersonName.setText("");
//            }
//            if (editTextTextPassword.hasFocus()) {
//                editTextTextPassword.setText("");
//            }
//        }
//        if (nVKCode==202){
//            printer_settings_class prn = new printer_settings_class(this.getApplicationContext());
//            prn.PaperFeed();
//        }

    }



    private static final long DOUBLE_PRESS_INTERVAL = 4000; // in millis
    private static final long TRIPLE_PRESS_INTERVAL = 1500; // in millis
    private long lastPressTime;
    String letter;
    String ButtonNum1;
    String ButtonNum2;
    int ctr;
    int x =0;
    String FinalLetterToWrite;
    private String MultipleKeypress(String letters){
        long pressTime = System.currentTimeMillis();
        Handler handler = new Handler();
        ButtonNum1=letters;

       // Log.d("TAG", "PressTime: "+Integer.parseInt(String.valueOf(pressTime)));

        ArrayList<String> LetterArray;
        LetterArray = new ArrayList<String>();
        LetterArray.add("P");
        LetterArray.add("Q");
        LetterArray.add("R");
        LetterArray.add("S");

//        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {


            if (ButtonNum1 == ButtonNum2) {
                ctr++;
                Log.d("TAG", "MultipleKeypress: Repeat");
                Log.e("TAG", "Array Letters:" + ctr);
               // lastPressTime=pressTime;


            } else {

                ctr = 0;
                Log.e("TAG", "MultipleKeypress: New");
                Log.e("TAG", "Array Letters:" + ctr);




               // lastPressTime = pressTime;
            }
        ButtonNum2=ButtonNum1;

//            if (pressTime ==DOUBLE_PRESS_INTERVAL){
//                ctr=0;
//                ButtonNum2="";
//            }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int finalctr = ctr;
                ctr =0;
                ButtonNum2="";
                FinalLetterToWrite = LetterArray.get(finalctr);
                x=1;


            }
        }, 2000);

        if (x==1){
            return FinalLetterToWrite;
        }





//        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
//            Toast.makeText(getApplicationContext(), "Double Click Event", Toast.LENGTH_SHORT).show();
//            Log.d("TAG", "Time press: "+(pressTime - lastPressTime));
//           // mHasDoubleClicked = true;
//        }
////        if ((pressTime - lastPressTime > TRIPLE_PRESS_INTERVAL) && (pressTime-lastPressTime<=TRIPLE_PRESS_INTERVAL)) {
////            Toast.makeText(getApplicationContext(), "Triple Click Event", Toast.LENGTH_SHORT).show();
////            // mHasDoubleClicked = true;
////        }
//        else{
//            Toast.makeText(getApplicationContext(), "Single Click Event", Toast.LENGTH_SHORT).show();
//            Log.d("TAG", "Time press: "+(pressTime - lastPressTime));
//        }
//        lastPressTime = pressTime;


        letter = letters;
      //  return FinalLetterToWrite;
        return null;

    }





    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                Log.d("TAG", "onKeyUp: "+"enter");

                return true;

            default:
                return super.onKeyUp(keyCode, event);
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

    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else{
            int readCheck= ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
            int writeCheck= ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
            return readCheck==PackageManager.PERMISSION_GRANTED && writeCheck==PackageManager.PERMISSION_GRANTED;
        }
    }

    private String[] permissions = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission")
                    .setMessage("Please give the Storage permission")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick( DialogInterface dialog, int which ) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setData(Uri.parse(String.format("package:%s", new Object[]{getApplicationContext().getPackageName()})));
                                activityResultLauncher.launch(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                activityResultLauncher.launch(intent);
                            }
                        }
                    })
                    .setCancelable(false)
                    .show();

        } else {

            ActivityCompat.requestPermissions(this, permissions, 30);
        }
    }



    int readingIndicator=0;
    String FinalDate;


    private void checkTerminalStatus(){
        shift_active shift_active=new shift_active();
        shift_active.getShiftingTable(getApplicationContext());
        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        printer_settings_class PrinterSettings=new printer_settings_class(this.getApplicationContext());


        Cursor c2 = db.rawQuery("select Status from TerminalStatus", null);
        if (c2.getCount()==0){

        String strsql = "insert into TerminalStatus(StatusID,Status)values('1','0')";
            Toast.makeText(this, "FIRST TIME CREATED", Toast.LENGTH_SHORT).show();
        db.execSQL(strsql);

        }
        Cursor c = db.rawQuery("select Status from TerminalStatus  where StatusID=1", null);

        if (c.getCount()!=0){
            while (c.moveToNext()){
                if (c.getString(0).matches("1")) {
                    Toast.makeText(this, "TERMINAL STATUS:OPENED", Toast.LENGTH_SHORT).show();
                    Log.e("TERMINAL STATUS","OPENED");
                }
                else {
                    Toast.makeText(this, "TERMINAL STATUS:CLOSED", Toast.LENGTH_SHORT).show();
                    Log.e("TERMINAL STATUS","CLOSED");
                    OR_TRANS_ITEM or_trans_item = new OR_TRANS_ITEM();
                    or_trans_item.readReferenceNumber(getApplicationContext());


                   // transactionNumber= or_trans_item.getTransactionNo();

                        int modx=Integer.parseInt(or_trans_item.getTransactionNo());
                        Log.e("modx",String.valueOf(modx));
                        int mody=999999999;
                        int resetCount = modx/mody;
                        Log.e("resetCt",String.valueOf(modx/mody));
                    String formattedCtr = String.format("%02d", resetCount);
                    String formattedTrans =  String.valueOf(modx % mody);
                    transactionNumber=formattedTrans;
                    transactionNumber =String.format("%010d",Integer.parseInt(transactionNumber ));




                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
                    system_final_date Sysdate = new system_final_date();
                    Sysdate.insertDate(getApplicationContext());

                    StringBuffer buffer = new StringBuffer();
                    buffer.append("TERMINAL OPENED" + "\n");
                    buffer.append( convertdate(dateOnly.format(currentDate.getTime()))+  "\t\t\t" + timeOnly.format(currentDate.getTime()) + "\n");
                    buffer.append("==========OPEN COUNTER==========" + "\n");
                //    buffer.append("SHF:"+shift_active.getShiftActive() +"\t\t\t" +"POS:"+shift_active.getPOSCounter() +  "\n");
                    buffer.append("POS:"+shift_active.getPOSCounter() +  "\n");
                    buffer.append(convertdate(dateOnly.format(currentDate.getTime()))+ "\t" + timeOnly.format(currentDate.getTime()) + "\t"  + "TRANS#: "+""+formattedCtr+"-" +transactionNumber +  "\n");
                    buffer.append("TERMINAL OPENED" + "\n");
                    buffer.append("================================" + "\n\n");

                    try {
                        printData = buffer.toString();


//                        JmPrinter myprinter = new JmPrinter();//create an instance
//                        byte[] SData = null;
//                        SData = printData.getBytes("GB2312");//GB2312
//                        myprinter.PrintText(SData);
//
//                        String strsql = "update TerminalStatus set Status=1 where StatusID=1";
////                        Toast.makeText(this, "FIRST TIME CREATED", Toast.LENGTH_SHORT).show();
//

                     //   SunmiPrinter(printData);



                        PrinterSettings.OnlinePrinter(printData,1,0,1);
                        FinalDate=dateOnly.format(currentDate.getTime());


                        create_journal_entry createJournal = new create_journal_entry();
                        createJournal.setPrintData(printData);
                        createJournal.setTransNumber(or_trans_item.getTransactionNo());
                        createJournal.journalEntry(createJournal.getPrintData(),createJournal.getTransNumber(),FinalDate);




                        String strsql = "update TerminalStatus set Status=1 where StatusID=1";
                        db.execSQL(strsql);







                    }
                    catch (Exception ex){

                    }
                    InsertInvoiceTransaction();






            db.close();


                }
            }
        }

    }
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

    private void InsertInvoiceTransaction(){

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
        shift_active shift_active = new shift_active();
        shift_active.getShiftingTable(getApplicationContext());

        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        boolean isInserted = databaseHandler.insertInvoiceReceiptTotal(
                transactionNumber,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "Terminal Opened",
                dateOnly.format(currentDate.getTime()),
                timeOnly.format(currentDate.getTime()),
                editTextTextPersonName.getText().toString(),
                shift_active.getShiftActive()










        );
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
    String newBizDate;
    Date date1;
    Button btn_confirm;
    int dialogCursor=0; //1 open new day 2// confirm new day
    AlertDialog dialogCursor1;
    SQLiteDatabase db3;
    int lastBusinessDate=0;
    String prevBizDate;
    private void showDialogDate(){

       // CursorKeyboard=2;
        String Message;
       db3 = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        // system_final_date systemDate = new system_final_date();
        Cursor sysdateCursor = db3.rawQuery("select * from SystemDate", null);
        if (sysdateCursor.getCount()!=0){
            while(sysdateCursor.moveToNext()){
                if (sysdateCursor.getString(3).equals("x")){ //biz date
                    Log.e("systedate","X");
                    String prevBizDate = sysdateCursor.getString(2);
                    AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_date, null);
                    builder.setView(alertLayout);
                    dialogCursor1 = builder.create();
                    dialogCursor1.setCanceledOnTouchOutside(false);
                    Log.e("showDialogDate","IF1");
                  btn_confirm = alertLayout.findViewById(R.id.btn_confirm);
                    Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
                    TextView tv_date = alertLayout.findViewById(R.id.tv_date);
                    //String systemDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
                    tv_date.setText("Last Business Date : " + prevBizDate );

                    lastBusinessDate=1;

                 //   dialogCursor=1;


                    btn_confirm.requestFocus();
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                    system_final_date sysDate= new system_final_date();
//                    sysDate.insertDate(view.getContext());
                            // alertDialog.dismiss();

                           // Log.d("TAG", "onClick: confirm 1");
//                            _Cursor1xConfirm(view);
                            _Cursor1x(view);

                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogCursor1.dismiss();
                            dialogCursor=0;
                        }
                    });


                    dialogCursor1.show();


                }

                else if (sysdateCursor.getString(3).equals("o")){ //biz date
                    Log.e("systedate","O");
                     prevBizDate= sysdateCursor.getString(2);
                    AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_date, null);
                    builder.setView(alertLayout);
                    dialogCursor1 = builder.create();
                    dialogCursor1.setCanceledOnTouchOutside(false);
                    Log.e("showDialogDate","IF");
                    Button btn_confirm = alertLayout.findViewById(R.id.btn_confirm);
                    Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
                    TextView tv_date = alertLayout.findViewById(R.id.tv_date);
                    //String systemDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
                    tv_date.setText("Last Business Date : " + prevBizDate );
                    dialogCursor=1;
                    lastBusinessDate=3;
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                    system_final_date sysDate= new system_final_date();
//                    sysDate.insertDate(view.getContext());
                            // alertDialog.dismiss();

                           _Cursor1o(view);


                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            dialogCursor=0;
                        }
                    });


                    dialogCursor1.show();


                }
                else{
                   // Log.e("showDialogDate","else");
                    login();
                    dialogCursor=0;
                }
            }






        }
        else{
            openNewDefaultDay=22;
            Log.e("showDialog","else2");
            system_final_date sysDate= new system_final_date();
            sysDate.insertDate(this.getApplicationContext());
            showDialogDate();
           // showSystemLock();
        }
        CursorKeyboard=0;







    }
    int openNewDefaultDay=0;


    String BizDate;

    AlertDialog alertDialog2;
    private void _Cursor1x(View view){
        AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_date2, null);
        builder.setView(alertLayout);
        alertDialog2 = builder.create();
        alertDialog2.setCanceledOnTouchOutside(false);
        Log.e("showDialogDate","IF2");
        Button btn_confirm = alertLayout.findViewById(R.id.btn_confirm);
        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        TextView tv_date = alertLayout.findViewById(R.id.tv_date);
       BizDate =new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //String systemDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText("New Business Date : "+BizDate );
        dialogCursor=2;
        lastBusinessDate=2;
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    system_final_date sysDate= new system_final_date();
//                    sysDate.insertDate(view.getContext());
                // alertDialog.dismiss();

                //   showSystemLock();

//
//                if(BizDate.equals(prevBizDate)){
//                    Toast.makeText(LoginActivity.this, ""+BizDate + " is already used", Toast.LENGTH_SHORT).show();
//                    alertDialog.dismiss();
//
//                }
//                else{
//                    _Cursor1xConfirm(view);
//                    login();
//                    dialogCursor1.dismiss();
//                    alertDialog2.dismiss();
//                }



                _Cursor1xConfirm(view);
                login();
                dialogCursor1.dismiss();
                alertDialog2.dismiss();







            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();

    }

    private void _Cursor1xConfirm(View view){
        SQLiteDatabase db2= view.getContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        String updateBizDate= "Update SystemDate set BizDate='"+BizDate+"',DateReadingIndicator='-' where ID=1" ;
        db2.execSQL(updateBizDate);
        db2.close();
        alertDialog.dismiss();
//        alertDialog2.dismiss();
//        dialogCursor1.dismiss();
        checkTerminalStatus();
        login();
    }

    AlertDialog alertDialogCursor1o;
    private void _Cursor1o(View view){
        AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_date2, null);
        builder.setView(alertLayout);
        alertDialogCursor1o = builder.create();
        alertDialogCursor1o.setCanceledOnTouchOutside(false);
        Log.e("showDialogDate","IF");
        Button btn_confirm = alertLayout.findViewById(R.id.btn_confirm);
        Button btn_cancel = alertLayout.findViewById(R.id.btn_cancel);
        TextView tv_date = alertLayout.findViewById(R.id.tv_date);
        dialogCursor=2;
        lastBusinessDate=3;


        //add 1 day to date



        Cursor sysdateCursorNew = db3.rawQuery("select * from SystemDate", null);
        while (sysdateCursorNew.moveToNext()){
            newBizDate=sysdateCursorNew.getString(2);

        }

        String dt = newBizDate;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(dt));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
       // c.add(Calendar.DATE, 1);  // number of days to add
        dt = sdf.format(c.getTime());  // dt is now the new date
        newBizDate=dt;
        // Date dt = new Date();



        lastBusinessDate=4;

        String BizDate=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //String systemDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText("New Businesss Date : "+newBizDate );
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    system_final_date sysDate= new system_final_date();
//                    sysDate.insertDate(view.getContext());
                // alertDialog.dismiss();



                if(newBizDate.equals(prevBizDate)){
                    Toast.makeText(LoginActivity.this, ""+BizDate + " is already used", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }
                else{

                _Cursor1oConfirm(view);

                alertDialogCursor1o.dismiss();
                dialogCursor=0;
                }







            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogCursor1o.dismiss();
                dialogCursor=0;
            }
        });


        alertDialogCursor1o.show();
    }
    private void _Cursor1oConfirm(View view){
        SQLiteDatabase db2= view.getContext().openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        String updateBizDate= "Update SystemDate set BizDate='"+newBizDate+"',DateReadingIndicator='-' where ID=1" ;
        db2.execSQL(updateBizDate);
        db2.close();
        dialogCursor1.dismiss();

        //showSystemLock();
        checkTerminalStatus();
        dialogCursor=0;
        login();

    }


    String systemDateCompare;
    private void showSystemLock(){

        system_final_date sysDate= new system_final_date();
        sysDate.insertDate(getApplicationContext());
        systemDateCompare=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());




//        if (systemDateCompare<sysDate.getSystemDate())


            try{

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");



                String str1 = systemDateCompare;
                Date posSystemDate = formatter.parse(str1);
                Log.d("TAG", "date1: "+posSystemDate.toString());
                String str2 = sysDate.getSystemDate();
                Date posBizDate = formatter2.parse(str2);



                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);



                Log.d("TAG", "date2: "+posBizDate.toString());
                //date 1 is equal to system date
                //date 2 is equal to  posDate


                //if (posBizDate.compareTo(posSystemDate)<0)
                if (hour >= 4 && hour < 9)

                {
                    Log.e("SYSTEM LOCK","date1 is Greater than my date2");

                    AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_systemlock, null);
                    builder.setView(alertLayout);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    TextView unlockTime = alertLayout.findViewById(R.id.tv_unlockTime);


                    Calendar currentDateCalendar = Calendar.getInstance();
                    currentDateCalendar.set(Calendar.HOUR_OF_DAY, 9);
                    currentDateCalendar.set(Calendar.MINUTE, 0);
                    currentDateCalendar.set(Calendar.SECOND, 0);
                    Date nineAMDate = currentDateCalendar.getTime();

// Calculate the time left until 9 AM
                    long unlockTimeInMillis = nineAMDate.getTime() - posSystemDate.getTime();

                   // long unlockTimeInMillis = posBizDate.getTime() - posSystemDate.getTime();
                if (unlockTimeInMillis > 0) {
                    // Convert the milliseconds to hours, minutes, and seconds
                    int seconds = (int) (unlockTimeInMillis / 1000) % 60;
                    int minutes = (int) ((unlockTimeInMillis / (1000 * 60)) % 60);
                    int hours = (int) ((unlockTimeInMillis / (1000 * 60 * 60)) % 24);

                    // Display the remaining time
                    String unlockTimeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                    unlockTime.setText("Unlock in: " + unlockTimeText);

                    // Start a countdown timer to update the remaining time every second
                    new CountDownTimer(unlockTimeInMillis, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000) % 60;
                            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);

                            // Update the remaining time
                            String unlockTimeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                            unlockTime.setText("Unlock in: " + unlockTimeText);
                        }

                        @Override
                        public void onFinish() {
                            // Handle when the countdown timer finishes (optional)
                            alertDialog.dismiss();
                        }
                    }.start();
                }

                else{
                    Log.e("TAG", "showSystemLock: testtt" );
                }


                    alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {

                            return i == KeyEvent.KEYCODE_BACK;
                           // return false;
                        }
                    });


                    alertDialog.show();

                  //  System.out.println();
                }
                else{
                    Log.e("SYSTEM LOCK OFF","Hour :" + hour);
                }

            }catch (ParseException e1){
                e1.printStackTrace();

                Log.e("SYSTEM LOCK",e1.getMessage());
            }





        //String systemDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());

    }





//
//    private void showSystemLock() {
//        system_final_date sysDate = new system_final_date();
//        sysDate.insertDate(getApplicationContext());
//        systemDateCompare = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//        TextView unlockTime = findViewById(R.id.tv_unlockTime);
//
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//            Log.e("TAG", "showSystemLock: test" );
//
//            String str1 = systemDateCompare;
//            Date posSystemDate = formatter.parse(str1);
//            String str2 = sysDate.getSystemDate();
//            Date posBizDate = formatter2.parse(str2);
//
//            Calendar calendar = Calendar.getInstance();
//            int hour = calendar.get(Calendar.HOUR_OF_DAY);
//
//            // Check if the current hour is between 4 AM and 9 AM
//            if (hour >= 4 && hour < 9) {
//                long unlockTimeInMillis = posBizDate.getTime() - posSystemDate.getTime();
//                if (unlockTimeInMillis > 0) {
//                    // Convert the milliseconds to hours, minutes, and seconds
//                    int seconds = (int) (unlockTimeInMillis / 1000) % 60;
//                    int minutes = (int) ((unlockTimeInMillis / (1000 * 60)) % 60);
//                    int hours = (int) ((unlockTimeInMillis / (1000 * 60 * 60)) % 24);
//
//                    // Display the remaining time
//                    String unlockTimeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
//                    unlockTime.setText("Unlock in: " + unlockTimeText);
//
////                    // Start a countdown timer to update the remaining time every second
////                    new CountDownTimer(unlockTimeInMillis, 1000) {
////                        @Override
////                        public void onTick(long millisUntilFinished) {
////                            int seconds = (int) (millisUntilFinished / 1000) % 60;
////                            int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
////                            int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
////
////                            // Update the remaining time
////                            String unlockTimeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
////                            unlockTime.setText("Unlock in: " + unlockTimeText);
////                        }
////
////                        @Override
////                        public void onFinish() {
////                            // Handle when the countdown timer finishes (optional)
////                        }
////                    }.start();
//                } else {
//                    Log.e("TAG", "showSystemLock: PAST" );
//                    // Handle the case when the unlock time is in the past (optional)
//                }
//            } else {
//                Log.e("SYSTEM LOCK OFF", "Hour: " + hour);
//                // Handle the case when the current time is not within the lock hour range
//            }
//
//        } catch (ParseException e1) {
//            e1.printStackTrace();
//            Log.e("SYSTEM LOCK", e1.getMessage());
//        }
//    }





    private void createDirectory(){
        //String folder_main = "NewFolderss";
        boolean success = true;
        File file =  new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS");
        File DATABASE=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/DATABASE");
        File sendFile=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/SEND FILE");
        File receiptFile=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/RECEIPT FILE");
        File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal");
        File generatedEJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Generated E-Journal");
        if (!file.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();
            file.mkdir();
        }


        if (!sendFile.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, sendFile.toString(), Toast.LENGTH_SHORT).show();
            sendFile.mkdir();
        }

        if (!receiptFile.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, receiptFile.toString(), Toast.LENGTH_SHORT).show();
            receiptFile.mkdir();
        }


        if (!eJournal.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, eJournal.toString(), Toast.LENGTH_SHORT).show();
            eJournal.mkdir();
        }

        if (!generatedEJournal.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, generatedEJournal.toString(), Toast.LENGTH_SHORT).show();
            generatedEJournal.mkdir();
        }



        if (!DATABASE.exists()){
            Toast.makeText(this, "NOT CREATED", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, file.toString(), Toast.LENGTH_SHORT).show();
            DATABASE.mkdir();
        }
        if (success){

        }
        else
        {
            Toast.makeText(this, "Failed error", Toast.LENGTH_SHORT).show();
        }





    }


    public void copyDatabase(){
        db = new DatabaseHelper(this);
        try{
            try{
                db.copyDataBase();
                Toast.makeText(this, "DATABASE UPLOADED", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(this, "NO DATABASE FOUND", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "DATABASE UPLOADED ERROR", Toast.LENGTH_SHORT).show();
        }
    }


    private void Authorization(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_authorization, null);
        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {

                return i == KeyEvent.KEYCODE_BACK;
                // return false;
            }
        });


        alertDialog.show();

    }
}