package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();
    static String DB_NAME = "V6BO.db";
    private Context myContext;
    String outFileName;
    private String DB_PATH;
    private SQLiteDatabase db;





    public DatabaseHelper(Context context) {
        super(context,DB_NAME,null,1);
        this.myContext=context;
        DB_PATH = "/data/data/com.example.myapplication/databases/";
        outFileName = DB_PATH + DB_NAME;
        File file = new File(DB_PATH);
        if (!file.exists()){
            file.mkdir();

        }
    }

    public void  createDataBase() throws IOException{
//        boolean dbExist = checkDataBase();
//        if (dbExist){
//
//        }
//        else{
//            this.getReadableDatabase();
//            try{
//                copyDataBase();
//                Toast.makeText(myContext, "DATABASE SETUP FOR FIRST TIME", Toast.LENGTH_SHORT).show();
//            }
//            catch (IOException e){
//                throw  new Error("ERROR IN COPYING DATABASE");
//            }
//        }

    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB=null;
        try {
            checkDB = SQLiteDatabase.openDatabase(outFileName,null,SQLiteDatabase.OPEN_READWRITE);

        }
        catch (SQLiteException e){
            try{
                copyDataBase();
            }
            catch (IOException e1){
                e1.printStackTrace();
            }
        }
        if (checkDB !=null){
            checkDB.close();
        }
        return  checkDB !=null? true:false;
    }

    public void copyDataBase() throws IOException{
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;

//        try{
            File file = Environment.getExternalStorageDirectory();
            String filepath = file.toString() + "/ANDROID_POS/DATABASE/V6BO.db";
        Toast.makeText(myContext, filepath, Toast.LENGTH_SHORT).show();
            FileInputStream myInput = new FileInputStream(filepath);

            myOutput = new FileOutputStream(DB_PATH+DB_NAME);
            while ((length=myInput.read(buffer))>0){
                myOutput.write(buffer,0,length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();

//        }catch(IOException e){
//            e.printStackTrace();
//        }
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

//    public boolean insertOrderedItem(){
//
//    }
}
