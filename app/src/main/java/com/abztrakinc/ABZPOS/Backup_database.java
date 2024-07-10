package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Backup_database {

    public String DATABASE_NAME1="/POSAndroidDB.db";
    public String DATABASE_NAME2="/PosOutputDB.db";
    public String DATABASE_NAME3="/PosSettings.db";
    public String DATABASE_NAME4="/settings.db";
    backup_usb_path usbPath = new backup_usb_path();



    public String USB_NAME=usbPath.getUsb_name(); // change this for usb or sdcad name
    public String USBPath = "/storage" + USB_NAME;
    String mainFolder ="/ANDROID_POS";
    String DATABASE="/DATABASE";
    String SendFile="/SEND FILE";
    String ReceiptFile="/RECEIPT FILE";
    String EJournal="/EJournal";
    String GeneratedEJournal="/Generated E-Journal";
    // /storage/4B93-0E6A/
    String LocalFolderBackUP=(Environment.getExternalStorageDirectory()+"/ANDROID_POS"+DATABASE); // local path of android external storage
    String ExternalBackUP=USBPath +mainFolder + DATABASE; // local path of android external stor
    String DATABASE_BACKUP_PATH="/data/data/com.abztrakinc.ABZPOS/databases";





    public void createBackupUSBFolder(){

//        USB_NAME = "";
//        File MainPathFolder =  new File(MainPath); //check if USB is available
//        if (MainPathFolder.exists()){
//
//            File file[] = MainPathFolder.listFiles();
//            Log.d("Files", "Size: "+ file.length);
////            for (int i=0; i < file.length; i++)
////            {
////                //Log.d("Files", "FileName:" + file[i].getName());
////
////            }
//            USB_NAME = file[0].getName();
//
//
//
//
//
//
//        }


        File USBPathFile =  new File(USBPath);

        File MainFolderFile =new File(USBPathFile + mainFolder); // /storage/4B93-0E6A/ANDROID_POS
        File DatabaseFolderFile =new File(USBPathFile + mainFolder + DATABASE);
        File SendFileFolder =new File(USBPathFile + mainFolder +SendFile);
        File ReceiptFileFolder =new File(USBPathFile + mainFolder +ReceiptFile);
        File EJourNalFolder =new File(USBPathFile +mainFolder + EJournal);
        File GeneratedEJournalFolder =new File(USBPathFile +mainFolder + GeneratedEJournal);


        if (USBPathFile.exists()){
            Log.e("PATH","EXIST");
            // Toast.makeText(this, "BACK UP NOT EXIST", Toast.LENGTH_SHORT).show();
            if (!MainFolderFile.exists()){
                Log.e("MainFolderFile","NOT EXIST");
                MainFolderFile.mkdirs();
            }
            if (!DatabaseFolderFile.exists()){
                DatabaseFolderFile.mkdirs();
            }
            if (!SendFileFolder.exists()){
                SendFileFolder.mkdirs();
            }
            if (!ReceiptFileFolder.exists()){
                ReceiptFileFolder.mkdirs();
            }
            if (!EJourNalFolder.exists()){
                EJourNalFolder.mkdirs();
            }
            if (!GeneratedEJournalFolder.exists()){
                GeneratedEJournalFolder.mkdirs();
            }


        }
        else {
            Log.e("PATH","NOT EXIST");
        }







//        String folder_main = "Test Folder";
//        File f = new File("/storage/4B93-0E6A/" + folder_main);
//        if (!f.exists()) {
//            f.mkdirs();
//            Toast.makeText(this, "CREATED", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "EXIST", Toast.LENGTH_SHORT).show();
//        }

//        File file[] = f.listFiles();
//        Log.d("Files", "Size: "+ file.length);
//        for (int i=0; i < file.length; i++)
//        {
//            Log.d("Files", "FileName:" + file[i].getName());
//
//        }





    }
    public void createFolderWithData(Context context,String sysDate){ // create when zRead
        //local and External(FD)
        //system_final_date systemDate = new system_final_date();
        //systemDate.insertDate(context);
        checkBackUPLocalLimit();
        String FolderName = "/BACKUP " + sysDate;
        File BackupFolder = new File(LocalFolderBackUP +FolderName);
        File BackupFolderExternal = new File(ExternalBackUP +FolderName);
        if (!BackupFolder.exists()){
            BackupFolder.mkdirs();

        }
        if (!BackupFolderExternal.exists()){
            BackupFolderExternal.mkdirs();

        }
        LocalExportBackUP(FolderName);
        ExternalExportBackUP(FolderName);




        //for local Folder

    }


    ArrayList<String> ChildName = new ArrayList<>();
    public void checkBackUPLocalLimit(){ // limit is 10 days only
       // Backup_database backup_database = new Backup_database();
       // File dirUSB = new File(backup_database.USBPath+"/ANDROID_POS/DATABASE");
        File dirLocal = new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/DATABASE/");
        Log.e("dirLocal",dirLocal.toString());
        if (dirLocal.isDirectory())
        {
            Log.e("dirLocal","true");
//            dirUSB.delete();

            String[] children = dirLocal.list();
            for (int i = 0; i < children.length; i++)
            {

              // File fileChile = new File(dirUSB, children[i]);
                ChildName.add(children[i].toString());
                Log.d("CHILDEN FILE",children[i].toString());

            }

            if (ChildName.size()>=5){

                File childToDelete = new File(dirLocal+"/"+ChildName.get(0));
                Log.d("USB PATH",childToDelete.toString());
//        if (dirUSB.isDirectory())
//        {
//            Log.e("Folder Deleted","deleted USB");
////            dirUSB.delete();
//
//            String[] children = dirUSB.list();
//            for (int i = 0; i < children.length; i++)
//            {
//
//                new File(dirUSB, children[i]).delete();
//                Log.d("CHILDEN FILE",children[i].toString());
//
//            }
//        }




                //File dir = new File(Environment.getExternalStorageDirectory() + CommonConstants.DIR_NAME);
                if (childToDelete.isDirectory()) {

                    try {
                        FileUtils.deleteDirectory(childToDelete);
                        Log.e("childToDelete","SUCCESS");
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("childToDelete","ERROR");
                    }
                }






            }
          //  for (int x=0;x)


        }
    }
    public void LocalExportBackUP(String FolderName){

        //POSoutputDB.db

        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME2;
            String copieDBPath=LocalFolderBackUP+FolderName+DATABASE_NAME2;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR");
        }


//        public String DATABASE_NAME1="/POSAndroidDB.db";
//        public String DATABASE_NAME2="/PosOutputDB.db";
//        public String DATABASE_NAME3="/PosSettings.db";
//        public String DATABASE_NAME4="/settings.db";

        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME1;
            String copieDBPath=LocalFolderBackUP+FolderName+DATABASE_NAME1;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR2");
        }

        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME3;
            String copieDBPath=LocalFolderBackUP+FolderName+DATABASE_NAME3;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR3");
        }



        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME4;
            String copieDBPath=LocalFolderBackUP+FolderName+DATABASE_NAME4;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR4");
        }






    }
    public void ExternalExportBackUP(String FolderName){

        //POSoutputDB.db

        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME2;
            String copieDBPath=ExternalBackUP+FolderName+DATABASE_NAME2;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR");
        }


//        public String DATABASE_NAME1="/POSAndroidDB.db";
//        public String DATABASE_NAME2="/PosOutputDB.db";
//        public String DATABASE_NAME3="/PosSettings.db";
//        public String DATABASE_NAME4="/settings.db";

        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME1;
            String copieDBPath=ExternalBackUP+FolderName+DATABASE_NAME1;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR2");
        }

        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME3;
            String copieDBPath=ExternalBackUP+FolderName+DATABASE_NAME3;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR3");
        }



        try{

            String currentDBPath= DATABASE_BACKUP_PATH + DATABASE_NAME4;
            String copieDBPath=ExternalBackUP+FolderName+DATABASE_NAME4;
            Log.e("PATH",copieDBPath);
            File currentDB=new File(currentDBPath);
            File copieDB=new File(copieDBPath);
            if (currentDB.exists()){
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst=new FileOutputStream(copieDB).getChannel();
                dst.transferFrom(src,0,src.size());
                src.close();
                dst.close();
                //Toast.makeText(this, "BACKUP DATABASE EXPORTED", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception ex){
            Log.e("LocalExportBackUP","ERROR4");
        }






    }


    public void deleteDatabase(){
          File data = Environment.getDataDirectory();
          String currentDBPath = DATABASE_BACKUP_PATH + DATABASE_NAME1;
          File currentDB = new File(currentDBPath);
          boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);
          if (deleted){
              Log.e("DATABASE","DELETED");
          }else{
              Log.e("DATABASE","NOT DELETED");
          }
      }
    public void deleteDatabase2(){
        File data = Environment.getDataDirectory();
        String currentDBPath = DATABASE_BACKUP_PATH+ DATABASE_NAME2;
        File currentDB = new File(currentDBPath);
        boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);
        if (deleted){
            Log.e("DATABASE","DELETED");
        }else{
            Log.e("DATABASE","NOT DELETED");
        }
    }
    public void deleteDatabase3(){
        File data = Environment.getDataDirectory();
        String currentDBPath = DATABASE_BACKUP_PATH+ DATABASE_NAME3;
        File currentDB = new File(currentDBPath);
        boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);
        if (deleted){
            Log.e("DATABASE","DELETED");
        }else{
            Log.e("DATABASE","NOT DELETED");
        }
    }
    public void deleteDatabase4(){
        File data = Environment.getDataDirectory();
        String currentDBPath = DATABASE_BACKUP_PATH+ DATABASE_NAME4;
        File currentDB = new File(currentDBPath);
        boolean deleted = SQLiteDatabase.deleteDatabase(currentDB);
        if (deleted){
            Log.e("DATABASE","DELETED");
        }else{
            Log.e("DATABASE","NOT DELETED");
        }
    }


    public boolean checkDatabaseExternal(String filepath){
        String FilePath=filepath;
        SQLiteDatabase checkDB=null;
        String outFileName = DATABASE_BACKUP_PATH+DATABASE_NAME2;
//        try{
//          //  checkDB = SQLiteDatabase.openDatabase(outFileName,null,SQLiteDatabase.OPEN_READWRITE);
//
//
//        }catch (SQLiteException e){
//            try {
//
//
//                RestoreExternal_POSOutput();
//            }
//            catch (IOException e1){
//                e1.printStackTrace();
//            }
//        }
//
//        if (checkDB !=null){
//            checkDB.close();
//        }

        try {
            RestoreExternal_POSOutput(FilePath);
        }catch (SQLiteException | IOException e){

        }
        if (checkDB!=null){
            checkDB.close();
        }




        return checkDB !=null?true:false;
    }
    public void RestoreExternal_POSOutput(String filePath) throws IOException{
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        String FolderName = "/" + filePath;

        try{

//            File file = Environment.getExternalStorageDirectory();
//            String filepath = file.toString() + "/GoldGloryV2/Masterfile/GGAndroidInput.db";
            String SourceFolder =  ExternalBackUP+FolderName+DATABASE_NAME2;
            FileInputStream myInput = new FileInputStream(SourceFolder);

            myOutput = new FileOutputStream(DATABASE_BACKUP_PATH+DATABASE_NAME2);
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();


        }catch (IOException e){
            e.printStackTrace();
            // Toast.makeText(mycontext, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("ERROR",e.toString());
        }

        try{

//            File file = Environment.getExternalStorageDirectory();
//            String filepath = file.toString() + "/GoldGloryV2/Masterfile/GGAndroidInput.db";
            String SourceFolder =  ExternalBackUP+FolderName+DATABASE_NAME1;
            FileInputStream myInput = new FileInputStream(SourceFolder);

            myOutput = new FileOutputStream(DATABASE_BACKUP_PATH+DATABASE_NAME1);
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();


        }catch (IOException e){
            e.printStackTrace();
            // Toast.makeText(mycontext, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("ERROR",e.toString());
        }

        try{

//            File file = Environment.getExternalStorageDirectory();
//            String filepath = file.toString() + "/GoldGloryV2/Masterfile/GGAndroidInput.db";
            String SourceFolder =  ExternalBackUP+FolderName+DATABASE_NAME3;
            FileInputStream myInput = new FileInputStream(SourceFolder);

            myOutput = new FileOutputStream(DATABASE_BACKUP_PATH+DATABASE_NAME3);
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();


        }catch (IOException e){
            e.printStackTrace();
            // Toast.makeText(mycontext, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("ERROR",e.toString());
        }

        try{

//            File file = Environment.getExternalStorageDirectory();
//            String filepath = file.toString() + "/GoldGloryV2/Masterfile/GGAndroidInput.db";
            String SourceFolder =  ExternalBackUP+FolderName+DATABASE_NAME4;
            FileInputStream myInput = new FileInputStream(SourceFolder);

            myOutput = new FileOutputStream(DATABASE_BACKUP_PATH+DATABASE_NAME4);
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();


        }catch (IOException e){
            e.printStackTrace();
            // Toast.makeText(mycontext, e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("ERROR",e.toString());
        }
    }




}
