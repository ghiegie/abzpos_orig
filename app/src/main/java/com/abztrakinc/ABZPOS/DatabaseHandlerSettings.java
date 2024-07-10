package com.abztrakinc.ABZPOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandlerSettings extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PosSettings.db";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase database;
    private static final String TABLE_NAME = "receiptHeader";

        private static final String col_header0 = "HeaderID";
        private static final String col_header1 = "HeaderContent";
        private static final String col_header2 = "HeaderDouble";



    private static final String TABLE_NAME2 = "receiptFooter";
    private static final String col_footer0 = "FooterID";
    private static final String col_Footer1 = "FooterContent";
    private static final String col_Footer2 = "FooterDouble";





    public DatabaseHandlerSettings(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" + col_header0 + " TEXT PRIMARY KEY," + col_header1 + " TEXT," + col_header2 + " TEXT," + col_header3 + " TEXT," + col_header4 + " TEXT," + col_header5 + " TEXT," + col_header6 + " TEXT," + col_header7
//                + " TEXT," + col_header8 + " TEXT," + col_header9 + " TEXT," + col_header10 + " TEXT," + col_header11 + " TEXT," + col_header12 + " TEXT," + col_header13 + " TEXT," + col_header14 + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" + col_header0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_header1 + " TEXT,"+ col_header2 + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME2 + "(" + col_footer0 + " INTEGER PRIMARY KEY AUTOINCREMENT," + col_Footer1 + " TEXT,"+ col_Footer2 + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
    }


//    public boolean insertHeaders(String headerID, String supName, String supAddress,String supVatRegTin,String supAccreditNo,String supDateIssuance,
//                                   String supEffectivityDate,String supValidUntil,String supPermitNo, String supDateIssued,
//                                   String supIssuedValidUntil,String clientName,String clientAddress,String clientVatRegTin,String clientMin) {
public boolean insertHeaders(String HeaderContent,String HeaderDouble) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

            contentValues.put("HeaderContent",HeaderContent );
            contentValues.put("HeaderDouble",HeaderDouble );
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    public boolean insertFooters(String FooterContent,String FooterDouble) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put("FooterID",FooterID);
        contentValues.put("FooterContent",FooterContent);
        contentValues.put("FooterDouble",FooterDouble);





        long result = db.insert(TABLE_NAME2, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
}
