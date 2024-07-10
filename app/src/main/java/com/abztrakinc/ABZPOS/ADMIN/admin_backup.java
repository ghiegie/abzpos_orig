package com.abztrakinc.ABZPOS.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.Backup_database;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.system_final_date;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class admin_backup extends AppCompatActivity {

    Spinner sp_backupPath;
    final List<String> Storagelist = new ArrayList<String>();
    Button btn_restore,btn_manualBackup;
    Backup_database backup_database = new Backup_database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_backup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btn_restore = findViewById(R.id.btn_Restore);
        sp_backupPath = findViewById(R.id.sp_backupPath);
        btn_manualBackup = findViewById(R.id.btn_manualBackup);
        btn_manualBackup.setVisibility(View.GONE);
        btn_manualBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                system_final_date sys = new system_final_date();
                Backup_database backup_database = new Backup_database();
                backup_database.createFolderWithData(getApplicationContext(),sys.getSystemDate());
                addStorageList();
                Toast.makeText(admin_backup.this, sys.getSystemDate() + " backup saved!!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestoreData();
            }
        });

        addStorageList();
        KeyBoardMap();

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



//    private void addStorageList(){
//       // Storagelist.clear();
//        Backup_database backup_database = new Backup_database();
//
//        String MainPath = "/storage"+backup_database.USB_NAME+"/ANDROID_POS/DATABASE";
//        Log.d("Mainpath",MainPath);
//       // String MainPath = "/storage/4B93-0E6A/ANDROID_POS/DATABASE";
//
//        //String USBName = "";
//        File MainPathFolder =  new File(MainPath); //check if USB is available
//        if (MainPathFolder.exists()){
//            Log.e("STORAGE","STORAGE");
//
//            File file[] = MainPathFolder.listFiles();
//           // Log.d("Files", "Size: "+ file.length);
//            for (int i=0; i < file.length; i++)
//            {
//                //Log.d("Files", "FileName:" + file[i].getName());
//                if (file[i].getName()!="emulated" || file[i].getName()!="self"){
//                    Storagelist.add(file[i].getName());
//                }
//
//            }
//
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, Storagelist);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp_backupPath.setAdapter(adapter);
//
//            for(int x=0;x<Storagelist.size();x++){
//              Log.d("Storagelist",Storagelist.get(x))  ;
//            }
//
//
//
//
//
//
//
//
//        }
//        else{
//            Log.e("STORAGE","not exist");
//        }
//    }
//    private void addStorageList(){
//        // Storagelist.clear();
//        Backup_database backup_database = new Backup_database();
//
//        String MainPath = "/storage"+backup_database.USB_NAME+"/ANDROID_POS/DATABASE";
//        Log.d("Mainpath",MainPath);
//        // String MainPath = "/storage/4B93-0E6A/ANDROID_POS/DATABASE";
//
//        //String USBName = "";
//        File MainPathFolder =  new File(MainPath); //check if USB is available
//        if (MainPathFolder.exists()){
//            Log.e("STORAGE","STORAGE");
//
//            File file[] = MainPathFolder.listFiles();
//            // Log.d("Files", "Size: "+ file.length);
//            for (int i=0; i < file.length; i++)
//            {
//                //Log.d("Files", "FileName:" + file[i].getName());
//                if (file[i].getName()!="emulated" || file[i].getName()!="self"){
//                    Storagelist.add(file[i].getName());
//                }
//
//            }
//
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, Storagelist);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp_backupPath.setAdapter(adapter);
//
//            for(int x=0;x<Storagelist.size();x++){
//                Log.d("Storagelist",Storagelist.get(x))  ;
//            }
//
//
//
//
//
//
//
//
//        }
//        else{
//            Log.e("STORAGE","not exist");
//        }
//    }
//    private void addStorageList(){
//        // Storagelist.clear();
//        Backup_database backup_database = new Backup_database();
//
//        String MainPath = "/storage"+backup_database.USB_NAME+"/ANDROID_POS/DATABASE";
//        Log.d("Mainpath",MainPath);
//        // String MainPath = "/storage/4B93-0E6A/ANDROID_POS/DATABASE";
//
//        //String USBName = "";
//        File MainPathFolder =  new File(MainPath); //check if USB is available
//        if (MainPathFolder.exists()){
//            Log.e("STORAGE","STORAGE");
//
//            File file[] = MainPathFolder.listFiles();
//            // Log.d("Files", "Size: "+ file.length);
//            for (int i=0; i < file.length; i++)
//            {
//                //Log.d("Files", "FileName:" + file[i].getName());
//                if (file[i].getName()!="emulated" || file[i].getName()!="self"){
//                    Storagelist.add(file[i].getName());
//                }
//
//            }
//
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, Storagelist);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            sp_backupPath.setAdapter(adapter);
//
//            for(int x=0;x<Storagelist.size();x++){
//                Log.d("Storagelist",Storagelist.get(x))  ;
//            }
//
//
//
//
//
//
//
//
//        }
//        else{
//            Log.e("STORAGE","not exist");
//        }
//    }


    private void addStorageList() {
        // Storagelist.clear();
        Backup_database backup_database = new Backup_database();

        String MainPath = "/storage" + backup_database.USB_NAME + "/ANDROID_POS/DATABASE";
        Log.d("Mainpath", MainPath);

        File MainPathFolder = new File(MainPath); // check if USB is available
        if (MainPathFolder.exists()) {
            Log.e("STORAGE", "STORAGE");

            File[] files = MainPathFolder.listFiles();
            if (files != null) {
                // Sort files by last modified time
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return Long.compare(f2.lastModified(), f1.lastModified());
                    }
                });

                int count = Math.min(10, files.length); // Limit to 10 latest files
                for (int i = 0; i < count; i++) {
                    if (!files[i].getName().equals("emulated") && !files[i].getName().equals("self")) {
                        Storagelist.add(files[i].getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, Storagelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_backupPath.setAdapter(adapter);

                for (String name : Storagelist) {
                    Log.d("Storagelist", name);
                }
            }
        } else {
            Log.e("STORAGE", "not exist");
        }
    }



    private void RestoreData(){

        //   dbMasterfile = new DatabaseHelperMasterfile(MainActivity.this);
        Backup_database backup_database = new Backup_database();
        backup_database.checkDatabaseExternal(sp_backupPath.getSelectedItem().toString());
        backup_database.deleteDatabase();
        backup_database.deleteDatabase2();
        backup_database.deleteDatabase3();
        backup_database.deleteDatabase4();
        try{
            try{
                backup_database.checkDatabaseExternal(sp_backupPath.getSelectedItem().toString());
                Toast.makeText(this, "DATABASE UPLOADED", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(this, "NO DATABASE FOUND", Toast.LENGTH_LONG).show();
            }
        }catch (Exception finalE){
            Toast.makeText(this, "DATABASE ERROR", Toast.LENGTH_SHORT).show();

        }


    }

}