package com.abztrakinc.ABZPOS.ADMIN;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.abztrakinc.ABZPOS.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileBrowseActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_FILE = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product_browse);

        // Check and request the necessary permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else {
            // Permission already granted, proceed with setting up the button click listener
            setupButtonClick();
            setupDelete();
        }
    }

    private void setupButtonClick() {
        // Assuming you have a button with the ID "browseButton" in your layout
        findViewById(R.id.btn_browse).setOnClickListener(view -> {
            // Create an intent to open a file picker
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            // Filter to show only text files.
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");


            // Start the file picker activity
            startActivityForResult(intent, REQUEST_PICK_FILE);
        });
    }


    private void setupDelete() {
        // Assuming you have a button with the ID "browseButton" in your layout
        findViewById(R.id.btn_deleteALL).setOnClickListener(view -> {
            // Create an intent to open a file picker

            SQLiteDatabase PosOutputDB = this.openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);

            String deleteCATEGORY= "delete from CATEGORY";
            PosOutputDB.execSQL(deleteCATEGORY);

            String deleteSUBCATEGORY= "delete from SUBCATEGORY";
            PosOutputDB.execSQL(deleteSUBCATEGORY);
            String deleteITEM= "delete from ITEM";
            PosOutputDB.execSQL(deleteITEM);

            Toast.makeText(this, "DELETE ITEM", Toast.LENGTH_SHORT).show();


        });
    }



    //public String USBPath = "/storage" + USB_NAME;
    String mainFolder ="/ANDROID_POS";
    String DATABASE="/GENERATED_POS_ITEM/POSAndroidDB.db";
    String SendFile="/SEND FILE";
    String ReceiptFile="/RECEIPT FILE";
    String EJournal="/EJournal";
    String GeneratedEJournal="/Generated E-Journal";
    // /storage/4B93-0E6A/


    //  String LocalFolderBackUP=(Environment.getExternalStorageDirectory()+"/ANDROID_POS"+DATABASE); // local path of android external storage
    String LocalFolderBackUP = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ANDROID_POS" + DATABASE).getPath();
    String DATABASE_BACKUP_PATH="/data/data/com.abztrakinc.ABZPOS/databases";
//    public void RestoreExternal_POSOutput() throws IOException {
//
//
//        byte[] buffer = new byte[1024];
//        OutputStream myOutput = null;
//        int length;
//
//
//        try{
//
//
//            String SourceFolder =  LocalFolderBackUP;
//            FileInputStream myInput = new FileInputStream(SourceFolder);
//
//            myOutput = new FileOutputStream(DATABASE_BACKUP_PATH+"/POSAndroidDB.db");
//            Log.e("ERRORS",DATABASE_BACKUP_PATH+"/POSAndroidDB.db");
//            while ((length = myInput.read(buffer))>0){
//                myOutput.write(buffer,0,length);
//
//            }
//            myOutput.close();
//            myOutput.flush();
//            myInput.close();
//
//
//        }catch (IOException e){
//            e.printStackTrace();
//            // Toast.makeText(mycontext, e.toString(), Toast.LENGTH_SHORT).show();
//
//        }
//
//
//
//
//    }

    public void RestoreExternal_POSOutput() {
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        FileInputStream myInput = null;

        try {
            String sourceFolder = LocalFolderBackUP;
            myInput = new FileInputStream("/sdcard/ANDROID_POS/GENERATED_POS_ITEM"+ "/POSAndroidDB.db");

            String destinationPath = DATABASE_BACKUP_PATH + "/POSAndroidDB.db";
            myOutput = new FileOutputStream(destinationPath);
            Log.e("ERRORS", destinationPath);

            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Inform the user about the successful restore
            Log.i("RestoreInfo", "Database restored successfully");
            // Toast.makeText(mycontext, "Database restored successfully", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RestoreError", "Failed to restore database: " + e.getMessage());
            // Toast.makeText(mycontext, "Failed to restore database", Toast.LENGTH_SHORT).show();

        } finally {
            try {
                if (myOutput != null) {
                    myOutput.close();
                }
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e("CloseError", "Error closing streams: " + ex.getMessage());
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            // The user has picked a file. You can get the URI of the selected file from the data intent.
            if (data != null) {
                Uri selectedFileUri = data.getData();
                // Now you can use the selectedFileUri to work with the chosen file.
                // For example, display the file path:
                if (selectedFileUri != null) {
                    String filePath = selectedFileUri.getPath();
                    // Handle the file path as needed
                    Toast.makeText(this, filePath.toString(), Toast.LENGTH_SHORT).show();


                        RestoreExternal_POSOutput();

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with setting up the button click listener
                setupButtonClick();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }
}

