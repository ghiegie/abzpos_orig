package com.abztrakinc.ABZPOS;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class POSAndroidDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;

    //self create db
    private static final String DATABASE_NAME = "POSAndroidDB.db";
    private static final String TABLE_NAME1 = "ITEM";
    private static final String TABLE_NAME1B = "ITEM_RESERVE";
    private static final String TABLE_NAME2 = "CATEGORY";
    private static final String TABLE_NAME3 = "SUBCATEGORY";


    //item database
    private static final String ItemID = "ItemID";
    private static final String COLUMN_ItemName = "ItemName";
    private static final String COLUMN_ItemDesc = "ItemDesc";
    private static final String COLUMN_ItemPrice = "ItemPrice";
    private static final String COLUMN_ItemQTY = "ItemQTY";
    private static final String COLUMN_ItemImage = "ItemImage";
   // private static final String COLUMN_SubCatg = "SubCatg";

    //category
    private static final String CatgID = "CatgID";
    private static final String CatgName = "CatgName";
    private static final String CatgDesc = "CatgDesc";

    //subcategory
    private static final String SubCatgID = "SubCatgID";
    private static final String SubCatgName = "SubCatgName";
    private static final String SubCatgDesc = "SubCatgDesc";

    //product shortCutKeys
    private static final String ShortCutKeysMap = "ShortCutKeysMap";
    private static final String VatIndicator = "VatIndicator";
    private static final String PriceOverride= "PriceOverride"; //yes or no
    private static final String PageLayer = "pageLayer";









    //append other database to be included


    SQLiteDatabase database;


    public POSAndroidDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME1 + "(" + ItemID + " TEXT PRIMARY KEY," + COLUMN_ItemName + " TEXT," + COLUMN_ItemDesc + " TEXT," + COLUMN_ItemPrice + " TEXT," + COLUMN_ItemQTY + " TEXT," + COLUMN_ItemImage + " BLOB," + CatgID + " TEXT," + SubCatgID + " TEXT,"+ "ItemCode" + " TEXT," + "Barcode" + " TEXT,"+  "ItemSafeStock" + " TEXT,"+  "ItemPrice2" + " TEXT," + "ItemPrice3" + " TEXT,"+ "ItemPrice4" + " TEXT," + "ItemPrice5" + " TEXT," + VatIndicator + " TEXT,"+PriceOverride + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME1B + "(" + ItemID + " TEXT PRIMARY KEY," + COLUMN_ItemName + " TEXT," + COLUMN_ItemDesc + " TEXT," + COLUMN_ItemPrice + " TEXT," + COLUMN_ItemQTY + " TEXT," + COLUMN_ItemImage + " BLOB," + CatgID + " TEXT," + SubCatgID + " TEXT,"+ "ItemCode" + " TEXT," + "Barcode" + " TEXT,"+  "ItemSafeStock" + " TEXT,"+  "ItemPrice2" + " TEXT," + "ItemPrice3" + " TEXT,"+ "ItemPrice4" + " TEXT," + "ItemPrice5" + " TEXT," + VatIndicator + " TEXT,"+PriceOverride + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME2 + "(" + CatgID + " TEXT PRIMARY KEY," + CatgName + " TEXT," + CatgDesc + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME3 + "(" + SubCatgID + " TEXT PRIMARY KEY," + SubCatgName + " TEXT," + SubCatgDesc + " TEXT," + CatgID + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + ShortCutKeysMap + "(" + "shortCutKeys" + " TEXT ," + "shortCutID" + " TEXT,"+ PageLayer + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1B);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ShortCutKeysMap);
        onCreate(sqLiteDatabase);


    }

    public boolean insertShortcutKeys(String shortCutKeys, String shortCutID,int pagelayer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shortCutKeys", shortCutKeys);
        contentValues.put("shortCutID", shortCutID);
        contentValues.put(PageLayer,pagelayer);

        long result = db.insert(ShortCutKeysMap, null, contentValues);
        if (result == 1)
            return false;
        else
            return true;
    }



    public boolean insertProduct(
            String ItemID, String COLUMN_ItemName,String COLUMN_ItemDesc,
            String COLUMN_ItemPrice,String COLUMN_ItemQTY,String COLUMN_ItemImage,
            String CatgID,String SubCatgID,String priceOverride,String priceIndicator) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", ItemID);
        contentValues.put("ItemName", COLUMN_ItemName);
        contentValues.put("ItemDesc", COLUMN_ItemDesc);
        contentValues.put("ItemPrice", COLUMN_ItemPrice);
        contentValues.put("ItemQTY", COLUMN_ItemQTY);
        contentValues.put("ItemImage", COLUMN_ItemImage);
        contentValues.put("CatgID", CatgID);
        contentValues.put("SubCatgID", SubCatgID);
        contentValues.put("PriceOverride", priceOverride);
        contentValues.put("VatIndicator", priceIndicator);
        contentValues.put("ItemCode", ItemID);
        contentValues.put("Barcode", ItemID);

        long result = db.insert(TABLE_NAME1, null, contentValues);
        if (result == 1)
            return false;
        else
            return true;
    }

    public boolean insertProductReserve(
            String ItemID, String COLUMN_ItemName,String COLUMN_ItemDesc,
            String COLUMN_ItemPrice,String COLUMN_ItemQTY,
            String CatgID,String SubCatgID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", ItemID);
        contentValues.put("ItemName", COLUMN_ItemName);
        contentValues.put("ItemDesc", COLUMN_ItemDesc);
        contentValues.put("ItemPrice", COLUMN_ItemPrice);
        contentValues.put("ItemQTY", COLUMN_ItemQTY);
        //contentValues.put("ItemImage", COLUMN_ItemImage);
        contentValues.put("CatgID", CatgID);
        contentValues.put("SubCatgID", SubCatgID);
        contentValues.put("VatIndicator", "VATable");
        long result = db.insert(TABLE_NAME1B, null, contentValues);
        if (result == 1)
            return false;
        else
            return true;
    }




    public boolean insertCategory(String CatgID, String CatgName,String CatgDesc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CatgID", CatgID);
        contentValues.put("CatgName", CatgName);
        contentValues.put("CatgDesc", CatgDesc);


        long result = db.insert(TABLE_NAME2, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    public boolean insertSubCategory(String SubCatgID, String SubCatgName,String SubCatgDesc,String CatgID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SubCatgID", SubCatgID);
        contentValues.put("SubCatgName", SubCatgName);
        contentValues.put("SubCatgDesc", SubCatgDesc);
        contentValues.put("CatgID", CatgID);


        long result = db.insert(TABLE_NAME3, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    //for testing
    public boolean insertCategoryTest() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CatgID", "001");
        contentValues.put("CatgName", "FOOD");
        contentValues.put("CatgDesc", "foodd");


        long result = db.insert(TABLE_NAME2, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    public boolean insertSubCategoryTest() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SubCatgID", "FOOD [001]-[01]");
        contentValues.put("SubCatgName", "rm");
        contentValues.put("SubCatgDesc", "rm");
        contentValues.put("CatgID", "FOOD [001]");


        long result = db.insert(TABLE_NAME3, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    public boolean insertProductTest() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-01");
        contentValues.put("ItemName", "tapsilog");
        contentValues.put("ItemDesc", "tapa");
        contentValues.put("ItemPrice", "100");
        contentValues.put("ItemQTY", "100");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [001]");
        contentValues.put("SubCatgID", "FOOD [001]-[01]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    public boolean insertProductTest2() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
                contentValues.put("ItemID", "rm-02");
        contentValues.put("ItemName", "tapsilog2");
        contentValues.put("ItemDesc", "tapa2");
        contentValues.put("ItemPrice", "9999999999.99");
        contentValues.put("ItemQTY", "100");
        contentValues.put("CatgID", "FOOD [002]");
        contentValues.put("SubCatgID", "FOOD [001]-[02]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertProductTest3() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-03");
        contentValues.put("ItemName", "tapsilog3");
        contentValues.put("ItemDesc", "tapa3");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [003]");
        contentValues.put("SubCatgID", "FOOD [001]-[03]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest4() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-04");
        contentValues.put("ItemName", "tapsilog4");
        contentValues.put("ItemDesc", "tapa4");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [004]");
        contentValues.put("SubCatgID", "FOOD [001]-[04]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest5() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-05");
        contentValues.put("ItemName", "tapsilog5");
        contentValues.put("ItemDesc", "tapa5");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [005]");
        contentValues.put("SubCatgID", "FOOD [001]-[05]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest6() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-06");
        contentValues.put("ItemName", "tapsilog6");
        contentValues.put("ItemDesc", "tapa6");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [006]");
        contentValues.put("SubCatgID", "FOOD [001]-[06]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest7() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-07");
        contentValues.put("ItemName", "tapsilog7");
        contentValues.put("ItemDesc", "tapa7");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [007]");
        contentValues.put("SubCatgID", "FOOD [001]-[07]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest8() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-08");
        contentValues.put("ItemName", "tapsilog8");
        contentValues.put("ItemDesc", "tapa8");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [008]");
        contentValues.put("SubCatgID", "FOOD [001]-[08]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest9() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-09");
        contentValues.put("ItemName", "tapsilog9");
        contentValues.put("ItemDesc", "tapa9");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [009]");
        contentValues.put("SubCatgID", "FOOD [001]-[09]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertProductTest10() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemID", "rm-10");
        contentValues.put("ItemName", "tapsilog10");
        contentValues.put("ItemDesc", "tapa4");
        contentValues.put("ItemPrice", "100.25");
        contentValues.put("ItemQTY", "100.25");
//        contentValues.put("ItemImage", );
        contentValues.put("CatgID", "FOOD [010]");
        contentValues.put("SubCatgID", "FOOD [001]-[10]");
        contentValues.put("VatIndicator","VATable");

        long result = db.insert(TABLE_NAME1, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }











}


