package com.abztrakinc.ABZPOS.CASHIER;

import android.app.Activity;
import android.content.Intent;
import android.databinding.tool.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.ADMIN.admin_email_send;
import com.abztrakinc.ABZPOS.ADMIN.admin_send_email_async;
import com.abztrakinc.ABZPOS.backup_usb_path;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class create_journal_entry {

    public String getPrintData() {
        return printData;
    }

    public void setPrintData(String printData) {
        this.printData = printData;
    }

    public String getTransNumber() {
        return TransNumber;
    }

    public void setTransNumber(String transNumber) {
        TransNumber = transNumber;
    }

    String printData;
    String TransNumber;
    Context contexts;

    backup_usb_path usbPath = new backup_usb_path();


    public String USB_NAME=usbPath.getUsb_name(); // change this for usb or sdcad name
    public String USBPath = "/storage" + USB_NAME;
    String mainFolder ="/ANDROID_POS";
    String DATABASE="/EJournal";
    String SendFile="/SEND FILE";
    String ReceiptFile="/RECEIPT FILE";
    String EJournal="/EJournal";
    String GeneratedEJournal="/Generated E-Journal";
    String ExternalBackUP=USBPath +mainFolder + DATABASE; // local path of android external stor






    public void journalEntry(String printData,String transNumber,String FinalDate){

        createConsolidateFolder();

       // String FinalDate = sysDate.getSystemDate();
//        File eJournal=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
//        if (!eJournal.exists()){
//            eJournal.mkdirs();
//        }
//        String dateForJournal = FinalDate.replace("/","");
//        File file = new File(eJournal, dateForJournal+transNumber+".txt");
//        FileOutputStream stream = null;
//        try {
//            stream = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String finalPrintData = printData;
//        try {
//            stream.write(finalPrintData.getBytes());
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }




        //sdcard
//        File eJournalSD=new File(ExternalBackUP);
//        if (!eJournalSD.exists()){
//            eJournalSD.mkdirs();
//        }
//        String dateForJournalSD= FinalDate.replace("/","");
//        File fileSD = new File(eJournalSD, dateForJournalSD+transNumber+".txt");
//        FileOutputStream streamSD = null;
//        try {
//            streamSD = new FileOutputStream(fileSD);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        String finalPrintDataSD = "printData";
////        try {
////            streamSD.write(finalPrintDataSD.getBytes());
////        } catch (IOException exception) {
////            exception.printStackTrace();
////        }


        try {
            File eJournal = new File(ExternalBackUP);
            if (!eJournal.exists()) {
                eJournal.mkdirs();
            }
            String dateForJournal = FinalDate.replace("/", "");
            File file = new File(eJournal, dateForJournal + transNumber + ".txt");
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String finalPrintData = printData;
            try {
                stream.write(finalPrintData.getBytes());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            journalEntryLocal(printData,transNumber,FinalDate);
            LocaljournalEntryConsolidate(printData,transNumber,FinalDate);
            ExternaljournalEntryConsolidate(printData,transNumber,FinalDate);


        }
        catch (Exception ex){

        }







    }
   // Environment.getExternalStorageDirectory()+"/ANDROID_POS/BIR_Report"
    public void createConsolidateFolder(){
        File LocalConsolidate = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Consolidate");
        if (!LocalConsolidate.exists()){
            LocalConsolidate.mkdirs();
        }
        File ExternalConsolidate = new File(USBPath +mainFolder+"/Consolidate");
        if (!ExternalConsolidate.exists()){
            ExternalConsolidate.mkdirs();
        }
    }

    public void LocaljournalEntryConsolidate(String printData, String transNumber, String FinalDate) {
        try {
            File LocalConsolidate = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/Consolidate");
            if (!LocalConsolidate.exists()){
                LocalConsolidate.mkdirs();
            }
            String dateForJournal = FinalDate.replaceAll("[/-]", "");
            File file = new File(LocalConsolidate, dateForJournal + "_Consolidate.txt");
            FileOutputStream stream = null;
            try {
                // Use FileOutputStream with append mode
                stream = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String finalPrintData = printData;
            try {
                // Append data to the file
                stream.write(finalPrintData.getBytes());
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        // Close the stream
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Call the method to update the local journal
         //   journalEntryLocal(printData, transNumber, FinalDate);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void ExternaljournalEntryConsolidate(String printData, String transNumber, String FinalDate) {
        try {
            File ExternalConsolidate = new File(USBPath +mainFolder+"/Consolidate");
            if (!ExternalConsolidate.exists()){
                ExternalConsolidate.mkdirs();
            }
            String dateForJournal = FinalDate.replaceAll("[/-]", "");
            File file = new File(ExternalConsolidate, dateForJournal + "_Consolidate.txt");
            FileOutputStream stream = null;
            try {
                // Use FileOutputStream with append mode
                stream = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String finalPrintData = printData;
            try {
                // Append data to the file
                stream.write(finalPrintData.getBytes());
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        // Close the stream
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Call the method to update the local journal
         //   journalEntryLocal(printData, transNumber, FinalDate);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void journalEntryLocal(String printData,String transNumber,String FinalDate){

        try {
            File eJournal = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/EJournal/");
            if (!eJournal.exists()) {
                eJournal.mkdirs();
            }
            String dateForJournal = FinalDate.replace("/", "");
            File file = new File(eJournal, dateForJournal + transNumber + ".txt");
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String finalPrintData = printData;
            try {
                stream.write(finalPrintData.getBytes());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

         ;


        }
        catch (Exception ex){

        }
    }






    private static final int PICK_FILE_REQUEST_CODE = 100;

//        private void launchFilePicker(Activity activity) {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("*/*"); // All file types
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            activity(intent, PICK_FILE_REQUEST_CODE);
//        }

//    private void launchFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*"); // All file types
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple file selection
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(Intent.createChooser(intent, "Select Files"), PICK_FILE_REQUEST_CODE);
//    }











    // Handle result from file picker
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
////            if (data != null && data.getData() != null) {
////               Uri uri = data.getData();
////                admin_send_email_async sendEmailTask = new admin_send_email_async();
////                sendEmailTask.SendEmailTask(getApplicationContext(),"bobwendelarcon@gmail.com", "ABZPOS GENERATED EMAIL", "This File is generated by Abzpos", uri);
////               sendEmailTask.execute();
////            }
////
////
////
////
////
////            else {
////                // Handle case where Uri is null
////                Toast.makeText(getApplicationContext(), "Uri is null", Toast.LENGTH_SHORT).show();
////            }
////        } else {
////            // Handle case where file selection is cancelled or result code mismatch
////            Toast.makeText(getApplicationContext(), "File selection cancelled", Toast.LENGTH_SHORT).show();
////        }
//
//        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
//            if (data != null) {
//                ArrayList<Uri> uris = new ArrayList<>();
//                if (data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount();
//                    for (int i = 0; i < count; i++) {
//                        uris.add(data.getClipData().getItemAt(i).getUri());
//                    }
//                } else if (data.getData() != null) {
//                    uris.add(data.getData());
//                }
//                admin_send_email_async sendEmailTask = new admin_send_email_async();
//                sendEmailTask.SendEmailTask(getApplicationContext(),"bobwendelarcon@gmail.com", "ABZPOS GENERATED EMAIL", "This File is generated by Abzpos for testing", uris);
//                sendEmailTask.execute();
//
//            } else {
//                // Handle case where data is null
//                Toast.makeText(getApplicationContext(), "Data is null", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            // Handle case where file selection is cancelled or result code mismatch
//            Toast.makeText(getApplicationContext(), "File selection cancelled", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//
//
//
//
//
//
//    }











}
