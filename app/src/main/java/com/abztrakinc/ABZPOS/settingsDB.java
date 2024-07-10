package com.abztrakinc.ABZPOS;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class settingsDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 29;
    private static final String DATABASE_NAME = "settings.db";

    private static final String TABLE_NAME1 = "orderStationSettings";
    private static final String id = "id";
    private static final String hostname = "hostname";
    private static final String username = "username";
    private static final String password = "password";
    private static final String image = "image";

    private static final String TABLE_NAME2 = "Accountsettings";



    //    private static final String accountID = "accountID";
    private static final String accountUsername = "accountUsername";
    private static final String accountNumber = "accountNumber"; // accountid
    private static final String accountPassword = "accountPassword";
    private static final String accountType = "accountType";




    private static final String TABLE_NAME3 = "ShiftingCount";
    private static final String shiftingNumber = "shiftingNumber";

    private static final String TABLE_NAME4 = "ShiftingTable";
    private static final String shiftID = "shiftID";
    private static final String shiftTotal = "shiftTotal";
    private static final String shiftActive = "shiftActive";
    private static final String shiftActiveUser = "shiftActiveUser";

    private static final String TABLE_NAME5 = "ReadingTable";
    private static final String readingID = "readingID";
    private static final String readingPOS = "readingPOS";
    private static final String readingCashierName = "readingCashierName";
    private static final String readingShift = "readingShift";
    private static final String readingBegOR = "readingBegOR";
    private static final String readingEndOR = "readingEndOR";
    private static final String readingBegBal = "readingBegBal";
    private static final String readingEndBal = "readingEndBal";
    private static final String readingStartTrans = "readingStartTrans";
    private static final String readingEndTrans = "readingEndTrans";
    private static final String readingCashFloat = "readingCashFloat";
    private static final String readingCash = "readingCash";





    private static final String TABLE_NAME6 = "ReadingIndicator";
    private static final String indicatorID = "indicatorID";
    private static final String indicatorStatus = "indicatorStatus";

    private static final String TABLE_NAME7 = "ReadingTableFinal";


    private static final String TABLE_NAME8 = "PrinterSettings";



    private static final String TABLE_NAME9="BankDetails";
    private static final String itemName="ItemName";
    private static final String bankName="BankName";
    private static final String cardType="CardType";
    private static final String idNo="IDNo";


    private static final String TABLE_NAME10="CardType";
    private static final String TABLE_NAME11="BankName";














    SQLiteDatabase database;
    public settingsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME1 + "(" + id + " TEXT PRIMARY KEY," + hostname + " TEXT," + username + " TEXT," + password + " TEXT," + image + " TEXT)");
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME2 + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT," + accountNumber +" TEXT," + accountUsername + " TEXT," + accountPassword + " TEXT,"  + accountType + " TEXT)");
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME3 + "(" + "shiftCountID" + " TEXT PRIMARY KEY," + shiftingNumber + " TEXT)");
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4 + "(" + shiftID + " TEXT PRIMARY KEY," + shiftTotal + " TEXT," + shiftActive + " TEXT," + shiftActiveUser + " TEXT)");
//
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME5 + "(" + readingID + " TEXT PRIMARY KEY," + readingPOS + " TEXT," +
//                readingCashierName + " TEXT," + readingShift + " TEXT" +"," + readingBegOR + " TEXT," + readingEndOR + " TEXT," +
//                readingBegBal + " TEXT," + readingEndBal + " TEXT,"+
//                readingStartTrans + " TEXT," + readingEndTrans + " TEXT,"
//                        + readingCashFloat + " TEXT," + readingCash + " TEXT)");
//
//
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME7 + "("
//                + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + readingID + " TEXT,"
//                + readingPOS + " TEXT," +
//                readingCashierName + " TEXT," + readingShift + " TEXT" +"," + readingBegOR + " TEXT," + readingEndOR + " TEXT," +
//                readingBegBal + " TEXT," + readingEndBal + " TEXT,"+
//                readingStartTrans + " TEXT," + readingEndTrans + " TEXT,"
//                + readingCashFloat + " TEXT," + readingCash + " TEXT)");
//
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME6 + "(" + indicatorID + " TEXT PRIMARY KEY," + indicatorStatus + " TEXT)");
//
//
//
        sqLiteDatabase.execSQL("CREATE TABLE " + "DiscountList"+ "("
                + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "DiscountID" + " TEXT,"
                + "DiscountName" + " TEXT,"
                + "DiscountAmount" + " TEXT,"
                + "DiscountComputation" + " TEXT,"
                + "DiscountExcludeTax" + " TEXT,"       //if 1=yes else=no
                + "DiscountType" + " TEXT,"
                + "DiscCategory" + " TEXT,"
                + "DiscLabel" + " TEXT,"
                + "MinTransactionAmount" + " TEXT,"
                + "MaxTransactionAmount" + " TEXT,"
                + "MaxDiscountAmount" + " TEXT,"
                + "SalesExcludeTax" + " TEXT,"
                + "ReceiptOption" + " TEXT,"
                + "ProRated" + " TEXT,"
                + "OpenDiscount" + " TEXT)"); // openDisocunt 1 on 0 off

//
//        sqLiteDatabase.execSQL("CREATE TABLE " + "SystemDate"+ "("
//                + "ID" + " INTEGER PRIMARY KEY,"
//                + "BizDate" + " TEXT,"       //if 1=yes else=no
//                + "PrevBizDate" + " TEXT,"+
//                "DateReadingIndicator" + " TEXT)");
//
//
//        sqLiteDatabase.execSQL("CREATE TABLE " + "OtherPayment" + "(" + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + "PaymentName" +" TEXT,"
//                +"AllowReference "+" TEXT,"+
//                "AllowUserDetails "+" TEXT,"+
//                "PaymentType "+" INTEGER,"+
//   "ChangeType "+" INTEGER)");
//
//
//
//
//
//
//
//                sqLiteDatabase.execSQL("CREATE TABLE " + "PrinterSettings" + "(" + "PrintID" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + "PrinterAddress" +" TEXT)");
//
//
//                        Log.e("TAG", "onCreate: new db created" );
//
//
//
//
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME9 + "(" + idNo + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + itemName +" TEXT,"
//                + bankName +" TEXT,"
//                + cardType +" TEXT)");
//
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME10 + "(" + idNo + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//
//
//                + cardType +" TEXT)");
//
//        // Bank Name
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME11 + "(" + idNo + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//
//                + bankName +" TEXT,"
//                + cardType +" TEXT)");
//




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME6);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME7);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME8);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "DiscountList");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "SystemDate");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "OtherPayment");
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME9);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME10);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME11);



    //        insertPrinterSettings(sqLiteDatabase,1);
    //        insertPrinterSettings(sqLiteDatabase,2);
    //        insertPrinterSettings(sqLiteDatabase,3);
    //        insertPrinterSettings(sqLiteDatabase,4);

        onCreate(sqLiteDatabase);


      //  insertPrinterSettings();

    }

    public boolean insertSettings(String hostname, String username,String password,String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", "1");
        contentValues.put("hostname", hostname);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("image", image);


        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    //public boolean insertPrinterSettings(SQLiteDatabase sqLiteDatabase,int PrintID) {
    public boolean insertPrinterSettings(int PrintID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PrintID",PrintID);
       // long result = sqLiteDatabase.insert(TABLE_NAME8, null, contentValues);
        long result = db.insert(TABLE_NAME8, null, contentValues);

        if (result == 1){
            Log.d("Printer Configuration", "insertPrinterSettings: " + result );
            return false;
        }

        else {
            Log.e("Printer Configuration", "insertPrinterSettings: " + result );
            return true;
        }
    }



    public boolean insertAccount(String accountNumber,String accountUsername,String accountPassword,String accountType) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("accountNumber", accountNumber);
        contentValues.put("accountUsername", accountUsername);
        contentValues.put("accountPassword", accountPassword);
        contentValues.put("accountType", accountType);


        long result = db.insert(TABLE_NAME2, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    //    private static final String shiftID = "shiftID";
//    private static final String shiftTotal = "shiftTotal";
//    private static final String shiftActive = "shiftActive";
//    private static final String shiftActiveUser = "shiftActiveUser";

    public boolean insertShiftTable(String shiftTotal, String shiftActive, String shiftActiveUser) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("shiftID", "1");
        contentValues.put("shiftTotal", shiftTotal);
        contentValues.put("shiftActive", shiftActive);
        contentValues.put("shiftActiveUser", shiftActiveUser);


        long result = db.insert(TABLE_NAME4, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public  boolean insertReadTable() {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("readingID", "1");



        long result = db.insert(TABLE_NAME5, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }



    public boolean insertReadTableFinal(String  readingID,String readingPOS,String readingCashierName,String readingShift,String readingBegOR
    ,String readingEndOR,String readingBegBal,String readingEndBal,String readingStartTrans,String readingEndTrans,String readingCashFloat,String readingCash) {



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("readingID", readingID);
        contentValues.put("readingPOS",readingPOS );
        contentValues.put("readingCashierName", readingCashierName);
        contentValues.put("readingShift",readingShift );
        contentValues.put("readingBegOR",readingBegOR );
        contentValues.put("readingEndOR",readingEndOR );
        contentValues.put("readingBegBal",readingBegBal );
        contentValues.put("readingEndBal",readingEndBal );
        contentValues.put("readingStartTrans",readingStartTrans );
        contentValues.put("readingEndTrans",readingEndTrans );
        contentValues.put("readingCashFloat",readingCashFloat );
        contentValues.put("readingCash",readingCash );



        long result = db.insert(TABLE_NAME7, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }



    public boolean insertReadingStatus() {

//        private static final String indicatorID = "indicatorID";
//        private static final String indicatorStatus = "indicatorStatus";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("indicatorID", "1");
        contentValues.put("indicatorStatus", "1");



        long result = db.insert(TABLE_NAME6, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertAccountTest() {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", "1");
        contentValues.put("accountNumber", "001");
        contentValues.put("accountUsername", "Supervisor");
        contentValues.put("accountPassword", "123");
        contentValues.put("accountType", "2");


        long result = db.insert(TABLE_NAME2, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

//    public boolean insertDiscount(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put("ID", "1");
//        contentValues.put("DiscountID", "scd");
//        contentValues.put("DiscountName", "Senior Discount");
//        contentValues.put("Discount Amount", "123");
//        contentValues.put("accountType", "2");
//
//
//        long result = db.insert("DiscountList", null, contentValues);
//
//        if (result == 1)
//            return false;
//        else
//            return true;
//
//    }

    public boolean insertDiscount(String DiscountID,String DiscountName,String DiscountAmount,String DiscountComputation,String DiscExcludeTax,String DiscountType,String DiscCategory,
                                  String DiscLabel,String MinTransactionAmount,String MaxTransactionAmount,String MaxDiscountAmount,String SalesExcludeTax,String ReceiptOption,String ProRated,String OpenDiscount){

//        DiscLabel
//                MinTransactionAmount
//        MaxTransactionAmount
//                MaxDiscountAmount
//        SalesExcludeTax

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("DiscountID", DiscountID);
        contentValues.put("DiscountName", DiscountName);
        contentValues.put("DiscountAmount", DiscountAmount);
        contentValues.put("DiscountComputation", DiscountComputation);
        contentValues.put("DiscountExcludeTax", DiscExcludeTax);
        contentValues.put("DiscountType", DiscountType);
        contentValues.put("DiscCategory", DiscCategory);
        contentValues.put("DiscLabel", DiscLabel);
        contentValues.put("MinTransactionAmount", MinTransactionAmount);
        contentValues.put("MaxTransactionAmount", MaxTransactionAmount);
        contentValues.put("MaxDiscountAmount", MaxDiscountAmount);
        contentValues.put("SalesExcludeTax", SalesExcludeTax);
        contentValues.put("ReceiptOption", ReceiptOption);
        contentValues.put("ProRated", ProRated);
        contentValues.put("OpenDiscount", OpenDiscount);


        long result = db.insert("DiscountList", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;

    }


    public boolean insertDate(String SysDateID,String SysDate,String SysDate2,String readingIndicator){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("ID",SysDateID);
        contentValues.put("BizDate",SysDate);
        contentValues.put("PrevBizDate",SysDate2);
        contentValues.put("DateReadingIndicator",readingIndicator);



        long result = db.insert("SystemDate", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;

    }

    public boolean insertBankPayment(String itemNam,String bankNam,String cardTyp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


//        contentValues.put("ID",SysDateID);
        contentValues.put(itemName,itemNam);
        contentValues.put(bankName,bankNam);
        contentValues.put(cardType,cardTyp);



        long result = db.insert(TABLE_NAME9, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;

    }


    public boolean insertBankName(String bankNam,String cardTyp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


//        contentValues.put("ID",SysDateID);

        contentValues.put(bankName,bankNam);
        contentValues.put(cardType,cardTyp);




        long result = db.insert(TABLE_NAME11, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;

    }

    public boolean insertCardType(String cardTyp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


//        contentValues.put("ID",SysDateID);

        contentValues.put(cardType,cardTyp);



        long result = db.insert(TABLE_NAME10, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;

    }





    public boolean insertOtherPayment(String PaymentName,String AllowReference,String AllowUserDetails,long type,long changetype) {
        // PaymentName,AllowReference,AllowUserDetails
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("PaymentName", PaymentName);
            contentValues.put("AllowReference", AllowReference);
            contentValues.put("AllowUserDetails", AllowUserDetails);
            contentValues.put("PaymentType",type);
            contentValues.put("ChangeType",changetype);


            //  contentValues.put("TransUser",TransUser );


            long result = db.insert("OtherPayment", null, contentValues);

            if (result == 1) {
                //Toast.makeText(context, "INSERT", Toast.LENGTH_SHORT).show();
                Log.e("INSERT", "INSERTED");
                return false;
            } else {
                return true;
            }
        }catch (Exception ex){
            Log.e("ERROR","DUPLICATE");
            return false;
        }


    }






//    sqLiteDatabase.execSQL("CREATE TABLE " + "DiscountList"+ "("
//            + "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//            + "DiscountID" + " TEXT,"
//            + "DiscountName" + " TEXT,"
//            + "DiscountAmount" + " TEXT,"
//            + "DiscountComputation" + " TEXT,"
//            + "DiscountType" + " TEXT)");



}
