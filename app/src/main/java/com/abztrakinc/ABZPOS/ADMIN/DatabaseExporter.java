package com.abztrakinc.ABZPOS.ADMIN;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.abztrakinc.ABZPOS.backup_usb_path;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DatabaseExporter {

    private static final String TAG = "DatabaseExporter";
    String DATABASE_BACKUP_PATH="/data/data/com.abztrakinc.ABZPOS/databases";


    public static void exportDatabase(Context context) {

        backup_usb_path usbPath = new backup_usb_path();
         String USB_NAME=usbPath.getUsb_name(); // change this for usb or sdcad name
         String USBPath = "/storage" + USB_NAME + "/installer";
      //  File exportDir = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState() + "/sdcard");
        File exportDir = new File(USBPath);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File dbFile = context.getDatabasePath("POSAndroidDB.db");
        File exportFile = new File(exportDir, "POSAndroidDB.db");

        try {
            exportFile.createNewFile();
            copyFile(dbFile, exportFile);
            Log.i(TAG, "Database exported successfully to: " + exportFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Error exporting database: " + e.getMessage());
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static void importDatabase(Context context) {


        backup_usb_path usbPath = new backup_usb_path();
        String USB_NAME=usbPath.getUsb_name(); // change this for usb or sdcad name
        String USBPath = "/storage" + USB_NAME + "/installer/POSAndroidDB.db";
        //  File exportDir = Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState() + "/sdcard");
        File importFile = new File(USBPath);


     //   File importFile = new File(sourcePath);

        File dbFile = context.getDatabasePath("POSAndroidDB.db");

        if (importFile.exists()) {
            try {
                copyFile(importFile, dbFile);
                Log.i(TAG, "Database imported successfully from: " + importFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e(TAG, "Error importing database: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Database file does not exist at path: " + USBPath);
        }
    }
}
