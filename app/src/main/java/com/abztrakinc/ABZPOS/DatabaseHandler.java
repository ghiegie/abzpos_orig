package com.abztrakinc.ABZPOS;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.widget.Toast;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;

    private static final String DATABASE_NAME = "PosOutputDB.db";
    private static final String TABLE_NAME = "OrderTakeTBl";
    private static final String COLUMN_ItemCode = "ItemCode";
    private static final String COLUMN_SubCatg = "SubCatg";
    private static final String COLUMN_RecptDesc = "RecptDesc";
    private static final String COLUMN_ItemQty = "ItemQty";
    private static final String COLUMN_UnitSellPrice = "UnitSellPrice";

    private static final String TABLE_NAME2 = "RemarkReceipt";
    private static final String TABLE_NAME3 = "FinalReceipt";
    private static final String refNumCol = "refNumber";
    private static final String tableNumCol = "tableNumCol";
    private static final String totAmtCol = "totAmtCol";
    private static final String totQtyCol = "totQtyCol";
    private static final String timeCol = "timeCol";
    private static final String dateCol = "dateCol";
    private static final String userCol = "userCol";
    private static final String remCol = "remCol";
    Context context;


    //invoice per item

    private static final String TABLE_NAME4 = "InvoiceReceiptItem";
    private static final String TABLE_NAME4Suspend = "InvoiceReceiptItemSuspend";



    private static final String TABLE_NAME4Final = "InvoiceReceiptItemFinal";
    private static final String TABLE_NAME4FinalWDiscount = "InvoiceReceiptItemFinalWDiscount";
    private static final String TABLE_NAME4FinalWDiscountSuspend = "InvoiceReceiptItemFinalWDiscountSuspend";
    private static final String TABLE_NAME4FinalWDiscountTemp = "InvoiceReceiptItemFinalWDiscountTemp";
    private static final String ID="ID";
    private static final String transaction_ID="TransactionID";
    private static final String order_ID="OrderID"; // id of product
    private static final String order_Name="OrderName";
    private static final String order_Qty="OrderQty";
    private static final String order_Price="OrderPrice";
    private static final String transaction_Time="TransactionTime";
    private static final String transaction_Date="TransactionDate";
    private static final String discount_type="DiscountType";
    private static final String item_remarks="ItemRemarks";


    //for discount
    private static final String order_discount_qty="DiscQty";
    private static final String order_discount_amount="DiscAmount";
    private static final String order_discount_percent="DiscPercent";
    private static final String disc_buyer_name="DiscBuyerName";
    private static final String disc_buyer_id="DiscIdNumber ";
    private static final String disc_other="DiscOther";



    //invoice total order

    private static final String TABLE_NAME5 = "InvoiceReceiptTotal";
    private static final String TABLE_NAME5Suspend = "InvoiceReceiptTotalSuspend";

    //private static final String transaction_ID="TransactionID";
    private static final String order_QtyTotal="OrderQtyTotal";
    private static final String order_PriceTotal="OrderPriceTotal";
    private static final String payment_rendered_amount="PaymentRenderedAmount";
    private static final String payment_change_amount="ChangeAmount";
    private static final String payment_type="typeOfPayment";

    private static final String TransDate="TransDate";
    private static final String TransTime="TransTime";
    private static final String TransUser="TransUser";
    private static final String CashierName="CashierName";
    private static final String CashierID="CashierID";
    private static final String POSCounterName="POSCounterName";



    private static final String TABLE_NAME6 = "CashInOut";
    private static final String total_1000="cash1000";
    private static final String total_500="cash500";
    private static final String total_200="cash200";
    private static final String total_100="cash100";
    private static final String total_50="cash50";
    private static final String total_20="cash20";
    private static final String total_10="cash10";
    private static final String total_5="cash5";
    private static final String total_1="cash1";
    private static final String total_cents="cashCents";

    private static final String TABLE_NAME7 = "OfficialReceipt";
    private static final String ORNo="ORNo";


    //for cash left inside box

    private static  final String TABLE_NAME8="CashBox";
    private static final String  total_cash_id = "id";
    private static final String  total_cash = "total_cash";




    //for settings;








    SQLiteDatabase database;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ItemCode + " TEXT PRIMARY KEY," + COLUMN_SubCatg + " TEXT," + COLUMN_RecptDesc + " TEXT," + COLUMN_ItemQty + " NUMERIC," + COLUMN_UnitSellPrice + " NUMERIC)");


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME2 + "(" + refNumCol + " TEXT  PRIMARY KEY ,"+ remCol + " TEXT  )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME3 + "(" + refNumCol + " TEXT  PRIMARY KEY ," + tableNumCol + " TEXT," + totAmtCol + " NUMERIC," + totQtyCol + " NUMERIC,"
                + timeCol + " TEXT," + dateCol + " TEXT," + userCol + " TEXT," + remCol + " TEXT  )");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4 + "(" + transaction_ID + " TEXT," + order_ID + " TEXT," + order_Name + " TEXT," + order_Qty + " NUMERIC,"
                + order_Price + " NUMERIC," +  order_PriceTotal + " NUMERIC," +transaction_Time + " TEXT," + transaction_Date + " TEXT," + discount_type + " TEXT," + item_remarks + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4Suspend + "(" + transaction_ID + " TEXT," + order_ID + " TEXT," + order_Name + " TEXT," + order_Qty + " NUMERIC,"
                + order_Price + " NUMERIC," +  order_PriceTotal + " NUMERIC," +transaction_Time + " TEXT," + transaction_Date + " TEXT," + discount_type + " TEXT," + item_remarks + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4Final + "(" + transaction_ID + " TEXT," + order_ID + " TEXT," + order_Name + " TEXT," + order_Qty + " NUMERIC,"
                + order_Price + " NUMERIC," +  order_PriceTotal + " NUMERIC," +transaction_Time + " TEXT," + transaction_Date + " TEXT," + discount_type + " TEXT," + item_remarks + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4FinalWDiscount + "(" + transaction_ID + " TEXT," + order_ID + " TEXT," + order_Name + " TEXT," + order_Qty + " NUMERIC,"
                + order_Price + " NUMERIC," +  order_PriceTotal + " NUMERIC," +transaction_Time + " TEXT," + transaction_Date + " TEXT," + discount_type + " TEXT," + item_remarks + " TEXT," +  order_discount_qty + " NUMERIC," + order_discount_amount + " NUMERIC," +order_discount_percent + " TEXT," + "VAT" + " TEXT," + disc_buyer_name + " TEXT," + disc_buyer_id + " TEXT," + disc_other + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4FinalWDiscountSuspend + "(" + transaction_ID + " TEXT," + order_ID + " TEXT," + order_Name + " TEXT," + order_Qty + " NUMERIC,"
                + order_Price + " NUMERIC," +  order_PriceTotal + " NUMERIC," +transaction_Time + " TEXT," + transaction_Date + " TEXT," + discount_type + " TEXT," + item_remarks + " TEXT," +  order_discount_qty + " NUMERIC," + order_discount_amount + " NUMERIC," +order_discount_percent + " TEXT," + "VAT" + " TEXT," + disc_buyer_name + " TEXT," + disc_buyer_id + " TEXT," + disc_other + " TEXT)");



        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME4FinalWDiscountTemp + "(" + transaction_ID + " TEXT," + order_ID + " TEXT," + order_Name + " TEXT," + order_Qty + " NUMERIC,"
                + order_Price + " NUMERIC," +  order_PriceTotal + " NUMERIC," +transaction_Time + " TEXT," + transaction_Date + " TEXT," + discount_type + " TEXT," + item_remarks + " TEXT," +  order_discount_qty + " NUMERIC," + order_discount_amount + " NUMERIC,"+ order_discount_percent + " TEXT," + "VAT" + " TEXT," +disc_buyer_name + " TEXT," + disc_buyer_id + " TEXT," + disc_other + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME5 + "(" + transaction_ID + " TEXT  PRIMARY KEY ," + order_QtyTotal + " NUMERIC," +   order_discount_qty + " NUMERIC," + order_discount_amount +  " NUMERIC," +  order_PriceTotal + " NUMERIC," + payment_rendered_amount + " NUMERIC," + payment_change_amount + " NUMERIC," + payment_type + " TEXT," + "typeOfTransaction"+ " TEXT," + TransDate + " TEXT," + TransTime + " TEXT," + TransUser + " TEXT," + "shiftActive" + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME5Suspend + "(" + transaction_ID + " TEXT  PRIMARY KEY ," + order_QtyTotal + " NUMERIC," +   order_discount_qty + " NUMERIC," + order_discount_amount +  " NUMERIC," +  order_PriceTotal + " NUMERIC," + payment_rendered_amount + " NUMERIC," + payment_change_amount + " NUMERIC," + payment_type + " TEXT," + "typeOfTransaction"+ " TEXT," + TransDate + " TEXT," + TransTime + " TEXT," + TransUser + " TEXT," + "shiftActive" + " TEXT)");


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME6 + "(" + transaction_ID + " TEXT,"+ total_1000 + " TEXT,"+total_500 + " TEXT,"+total_200 + " TEXT,"+total_100 + " TEXT,"+total_50 + " TEXT,"+total_20 + " TEXT,"+total_10 + " TEXT,"+total_5 + " TEXT,"+total_1 + " TEXT,"+total_cents + " TEXT,"+"TransType" + " TEXT,"+CashierID + " TEXT,"+CashierName + " TEXT,"+POSCounterName + " TEXT,"+TransDate + " TEXT,"+TransTime + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME7 + "(" + ORNo + " TEXT,"+ transaction_ID + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + "TerminalStatus" + "(" + "StatusID" + " TEXT,"+ "Status" + " TEXT)");


        sqLiteDatabase.execSQL("CREATE TABLE " + "BankDetails" + "(" + "BankID" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "BankName" +" TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + "BankTransactionTemp" + "(" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                "TransNum"+"  TEXT,"+
                "ORTrans" +"  TEXT,"+
                "BankName"+"  TEXT,"+

                "CardOwner"+"   TEXT,"+
                "CardNum"+"   TEXT,"+
                "CardExpiry"+"   TEXT,"+
                "CardApproval"+"   TEXT,"+
                "TransDate"+"   TEXT,"+
                "TransTime"+"   TEXT,"+
                "User"+"   TEXT,"+
                "CardType"+"  TEXT)");


        sqLiteDatabase.execSQL("CREATE TABLE " + "BankTransactionFinal" + "(" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                "TransNum"+"  TEXT,"+
                "ORTrans" +"  TEXT,"+
                "BankName"+"  TEXT,"+
                "CardOwner"+"   TEXT,"+
                "CardNum"+"   TEXT,"+
                "CardExpiry"+"   TEXT,"+
                "CardApproval"+"   TEXT,"+
                "TransDate"+"   TEXT,"+
                "TransTime"+"   TEXT,"+
                "User"+"   TEXT,"+
                "CardType"+"  TEXT)");


        sqLiteDatabase.execSQL("CREATE TABLE " + "FinalTransactionReport" + "(" +
                "TransactionID" + " TEXT," +
                "ORTrans" +"  TEXT,"+
                "TotalAmount" +"  NUMERIC,"+
                "TotalDiscount"+"  NUMERIC,"+
                "TotalVatableSales"+"   NUMERIC,"+
                "TotalVatAmount"+"   NUMERIC,"+
                "TotalVatExempt"+"   NUMERIC,"+
                "TotalZeroRatedSales"+"   NUMERIC,"+
                "TotalLessVat"+"   NUMERIC,"+
                "TotalServiceCharge"+"   NUMERIC,"+
                "TransUser"+"   TEXT,"+
                "TransUserID"+"   TEXT,"+
                "TransDate"+"   TEXT,"+
                "TransTime"+"   TEXT,"+
                "TransShift"+"   TEXT,"+
                "TotalSalesOverrun"+"   NUMERIC)");


        sqLiteDatabase.execSQL("CREATE TABLE " + "RefundTransaction" + "(" +
                "TransactionID" + " TEXT," +
                "ORNumber" +"  TEXT,"+
                "AmountRefunded" +"  NUMERIC,"+
                "Reason"+"  TEXT,"+
                "TransUser"+"   TEXT,"+
                "TransUserID"+"   TEXT,"+
                "TransDate"+"   TEXT,"+
                "TransTime"+"   TEXT,"+
                "TransShift"+"   TEXT)");



        sqLiteDatabase.execSQL("CREATE TABLE " + "ResetCounter" + "(" +
                "resetCtrID" + " TEXT PRIMARY KEY," +
                "resetCtrValue"+"   TEXT)");


        //for multiple payment


        sqLiteDatabase.execSQL("CREATE TABLE " + "InvoiceMultiplePaymentTemp" + "(" + "ID" + " INTEGER  PRIMARY KEY ," + transaction_ID + " TEXT," +payment_type + " TEXT," + "amount" + " numeric," + "BankType" + " Text)");
        sqLiteDatabase.execSQL("CREATE TABLE " + "InvoiceMultiplePaymentFinal" + "(" + "ID" + " INTEGER  PRIMARY KEY ," + transaction_ID + " TEXT," + payment_type + " TEXT," + "amount" + " numeric," + "BankType" + " Text)");
        sqLiteDatabase.execSQL("CREATE TABLE " + "InvoiceReference" + "(" + "ID" + " INTEGER  PRIMARY KEY ," + transaction_ID + " NUMERIC," + "ReferenceNumber" + " TEXT)");







        sqLiteDatabase.execSQL("CREATE TABLE " + "ReturnExchangeTemp" + "(" +
              //  "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TransNo"+"  TEXT,"+
                "InvNo" +"  TEXT)");  // for ReturnExchangeTemp

        sqLiteDatabase.execSQL("CREATE TABLE " + "ReturnExchangeFinal" + "(" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "RefTransNo"+"  TEXT,"+
                "RefInvNo"+"  TEXT,"+
                "NewTransNo"+"  TEXT,"+
                "NewInvNo" +"  TEXT)");  // for ReturnExchangeFinal


        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME8 + "(" + total_cash_id + " INT PRIMARY KEY," + total_cash + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + "DiscountInfoTbl" + "(" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TransactionID"+"  TEXT,"+
                "DiscBuyerName"+"  TEXT,"+
                "DiscIdNumber"+"  TEXT,"+
                "DiscOther" +"  TEXT)");  // for ReturnExchangeFinal
//


        sqLiteDatabase.execSQL("CREATE TABLE " + "ReceivingTbl" + "(" +
                "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TransactionNo"+"  TEXT,"+
                "OrderID"+"  TEXT,"+
                "OrderName"+"  TEXT,"+
                "OrderQty"+"  TEXT,"+
                "TransactionTime"+"  TEXT,"+
                "TransactionDate"+"  TEXT,"+
                "Remarks"+"  TEXT,"+
                "TransUser"+"  TEXT,"+
                "Reason" +"  TEXT)");  // for ReturnExchangeFinal
//



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4Suspend);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4Final);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4FinalWDiscount);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4FinalWDiscountSuspend);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4FinalWDiscountTemp);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5Suspend);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME6);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME7);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "TerminalStatus");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "BankDetails");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "BankTransactionTemp");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "BankTransactionFinal");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "FinalTransactionReport");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "RefundTransaction");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "ResetCounter");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "InvoiceMultiplePaymentTemp");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "InvoiceMultiplePaymentFinal");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "InvoiceReference");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "ReturnExchangeTemp");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "ReturnExchangeFinal");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME8);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "DiscountInfoTbl");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "ReceivingTbl");

        onCreate(sqLiteDatabase);


    }


    public boolean insertBank(String BankName) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            //contentValues.put("BankID", 2);
            contentValues.put("BankName", BankName);


            //  contentValues.put("TransUser",TransUser );


            long result = db.insert("BankDetails", null, contentValues);

            if (result == 1) {
                Toast.makeText(context, "Bank Added", Toast.LENGTH_SHORT).show();
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


    public boolean insertOrderList(String COLUMN_ItemCode, String COLUMN_SubCatg, String COLUMN_RecptDesc, String  COLUMN_ItemQty, String COLUMN_UnitSellPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemCode", COLUMN_ItemCode);
        contentValues.put("SubCatg", COLUMN_SubCatg);
        contentValues.put("RecptDesc", COLUMN_RecptDesc);
        contentValues.put("ItemQty", COLUMN_ItemQty);
        contentValues.put("UnitSellPrice", COLUMN_UnitSellPrice);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }



    public boolean insertFinalReceipt( String refNumber, String tableNumCol,String totAmtCol,String totQtyCol,String timeCol,String dateCol,String userCol,String remCol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("refNumber",refNumber );
        contentValues.put("tableNumCol",tableNumCol );
        contentValues.put("totAmtCol",totAmtCol );
        contentValues.put("totQtyCol",totQtyCol );
        contentValues.put("timeCol",timeCol );
        contentValues.put("dateCol",dateCol );
        contentValues.put("userCol",userCol );
        contentValues.put("remCol",remCol );



        long result = db.insert(TABLE_NAME3, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public boolean insertRemarks( String refNumber,String remCol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("refNumber",refNumber );
        contentValues.put("remCol",remCol );



        long result = db.insert(TABLE_NAME2, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }




    public boolean insertInvoiceReceipt(  String transaction_ID,String order_ID,String order_Name,String order_Qty,String order_Price,String order_PriceTotal,String transaction_Time,String transaction_Date,String discount_type,String item_remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderID",order_ID );
        contentValues.put("OrderName",order_Name );
        contentValues.put("OrderQty",order_Qty );
        contentValues.put("OrderPrice",order_Price );
        contentValues.put("OrderPriceTotal",order_PriceTotal );
        contentValues.put("TransactionTime",transaction_Time );
        contentValues.put("TransactionDate",transaction_Date );
        contentValues.put("DiscountType",discount_type );
        contentValues.put("ItemRemarks",item_remarks );



        long result = db.insert(TABLE_NAME4, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }



    public boolean insertInvoiceReceiptSuspend(  String transaction_ID,String order_ID,String order_Name,String order_Qty,String order_Price,String order_PriceTotal,String transaction_Time,String transaction_Date,String discount_type,String item_remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderID",order_ID );
        contentValues.put("OrderName",order_Name );
        contentValues.put("OrderQty",order_Qty );
        contentValues.put("OrderPrice",order_Price );
        contentValues.put("OrderPriceTotal",order_PriceTotal );
        contentValues.put("TransactionTime",transaction_Time );
        contentValues.put("TransactionDate",transaction_Date );
        contentValues.put("DiscountType",discount_type );
        contentValues.put("ItemRemarks",item_remarks );



        long result = db.insert(TABLE_NAME4Suspend, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertInvoiceMultipleTemp(  String transaction_ID,String payment_type,String payment_amount,String BankType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("typeOfPayment",payment_type );
        contentValues.put("amount",payment_amount );
        contentValues.put("BankType",BankType );








        long result = db.insert("InvoiceMultiplePaymentTemp", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertInvoiceMultipleFinal(  String transaction_ID,String payment_type,String payment_amount,String BankType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("typeOfPayment",payment_type );
        contentValues.put("amount",payment_amount );
        contentValues.put("BankType",BankType );








        long result = db.insert("InvoiceMultiplePaymentFinal", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }




    public boolean insertInvoiceReceiptFinal(  String transaction_ID,String order_ID,String order_Name,String order_Qty,String order_Price,String order_PriceTotal,String transaction_Time,String transaction_Date,String discount_type,String item_remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderID",order_ID );
        contentValues.put("OrderName",order_Name );
        contentValues.put("OrderQty",order_Qty );
        contentValues.put("OrderPrice",order_Price );
        contentValues.put("OrderPriceTotal",order_PriceTotal );
        contentValues.put("TransactionTime",transaction_Time );
        contentValues.put("TransactionDate",transaction_Date );
        contentValues.put("DiscountType",discount_type );
        contentValues.put("ItemRemarks",item_remarks );



        long result = db.insert(TABLE_NAME4Final, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }

    public void deleteInvoiceReceipt(){
        SQLiteDatabase db = this.getWritableDatabase();
        String strsql = "DELETE FROM InvoiceReceiptItem";
        db.execSQL(strsql);
    }

    public boolean insertRefundTransaction(String TransactionID,String ORNumber,String AmountRefunded,String Reason
    ,String TransUser,String TransUserID,String TransDate,String TransTime,String TransShift){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",TransactionID );
        contentValues.put("ORNumber",ORNumber );
        contentValues.put("AmountRefunded",AmountRefunded );
        contentValues.put("Reason",Reason );
        contentValues.put("TransUser",TransUser );
        contentValues.put("TransUserID",TransUserID );
        contentValues.put("TransDate",TransDate );
        contentValues.put("TransTime",TransTime );
        contentValues.put("TransShift",TransShift );

        long result = db.insert("RefundTransaction", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;



    }






    public boolean insertInvoiceReceiptDiscountTemp(  String transaction_ID,String order_ID,String order_Name,String order_Qty,String order_Price,String order_PriceTotal,String transaction_Time,String transaction_Date,String discount_type,String item_remarks,String order_discount_qty,String order_discount_amount,String order_discount_percent,String VAT,String disc_buyer_name,String disc_buyer_id,String disc_other) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderID",order_ID );
        contentValues.put("OrderName",order_Name );
        contentValues.put("OrderQty",order_Qty );
        contentValues.put("OrderPrice",order_Price );
        contentValues.put("OrderPriceTotal",order_PriceTotal );
        contentValues.put("TransactionTime",transaction_Time );
        contentValues.put("TransactionDate",transaction_Date );
        contentValues.put("DiscountType",discount_type );
        contentValues.put("ItemRemarks",item_remarks );
        contentValues.put("DiscQty",order_discount_qty );
        contentValues.put("DiscAmount",order_discount_amount );
        contentValues.put("DiscPercent",order_discount_percent );
        contentValues.put("VAT",VAT );
        contentValues.put("DiscBuyerName",disc_buyer_name );
        contentValues.put("DiscIdNumber",disc_buyer_id );
        contentValues.put("DiscOther",disc_other );





        long result = db.insert(TABLE_NAME4FinalWDiscountTemp, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }



    public boolean insertDiscountInfo(  String transaction_ID,String disc_buyer_name,String disc_buyer_id,String disc_other) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("DiscBuyerName",disc_buyer_name );
        contentValues.put("DiscIdNumber",disc_buyer_id );
        contentValues.put("DiscOther",disc_other );





        long result = db.insert("DiscountInfoTbl", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertReceivingTbl(  String transaction_ID,String OrderId,String OrderName,String OrderQty,String TransactionTime,String TransactionDate,String Remarks,String TransUser,String reasonTxt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionNo",transaction_ID );
        contentValues.put("OrderID",OrderId );
        contentValues.put("OrderName",OrderName );
        contentValues.put("OrderQty",OrderQty );
        contentValues.put("TransactionTime",TransactionTime);
        contentValues.put("TransactionDate",TransactionDate);
        contentValues.put("Remarks",Remarks );
        contentValues.put("TransUser",TransUser );
        contentValues.put("Reason",reasonTxt);





        long result = db.insert("ReceivingTbl", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertCashBox(  int id,String totalcash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(total_cash_id,id );
        contentValues.put("total_cash",totalcash );





        long result = db.insert(TABLE_NAME8, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertInvoiceReceiptDiscountSuspend(  String transaction_ID,String order_ID,String order_Name,String order_Qty,String order_Price,String order_PriceTotal,String transaction_Time,String transaction_Date,String discount_type,String item_remarks,String order_discount_qty,String order_discount_amount,String order_discount_percent,String VAT,String disc_buyer_name,String disc_buyer_id,String disc_other) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderID",order_ID );
        contentValues.put("OrderName",order_Name );
        contentValues.put("OrderQty",order_Qty );
        contentValues.put("OrderPrice",order_Price );
        contentValues.put("OrderPriceTotal",order_PriceTotal );
        contentValues.put("TransactionTime",transaction_Time );
        contentValues.put("TransactionDate",transaction_Date );
        contentValues.put("DiscountType",discount_type );
        contentValues.put("ItemRemarks",item_remarks );
        contentValues.put("DiscQty",order_discount_qty );
        contentValues.put("DiscAmount",order_discount_amount );
        if (order_discount_percent != null && !order_discount_percent.trim().isEmpty()) {
            double discountPercent = Double.parseDouble(order_discount_percent);
            contentValues.put("DiscPercent", discountPercent);
        } else {
            // Handle the case where the discount percent is empty or null
            contentValues.put("DiscPercent", 0); // or any default value you prefer
        }
        contentValues.put("VAT",VAT );
        contentValues.put("DiscBuyerName",disc_buyer_name );
        contentValues.put("DiscIdNumber",disc_buyer_id );
        contentValues.put("DiscOther",disc_other );





        long result = db.insert(TABLE_NAME4FinalWDiscountSuspend, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertInvoiceReceiptDiscountTempFinal(  String transaction_ID,String order_ID,String order_Name,String order_Qty,String order_Price,String order_PriceTotal,String transaction_Time,String transaction_Date,String discount_type,String item_remarks,String order_discount_qty,String order_discount_amount,String order_discount_percent,String VAT,String disc_buyer_name,String disc_buyer_id,String disc_other) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderID",order_ID );
        contentValues.put("OrderName",order_Name );
        contentValues.put("OrderQty",order_Qty );
        contentValues.put("OrderPrice",order_Price );
        contentValues.put("OrderPriceTotal",order_PriceTotal );
        contentValues.put("TransactionTime",transaction_Time );
        contentValues.put("TransactionDate",transaction_Date );
        contentValues.put("DiscountType",discount_type );
        contentValues.put("ItemRemarks",item_remarks );
        contentValues.put("DiscQty",order_discount_qty );
        contentValues.put("DiscAmount",order_discount_amount );
        if (order_discount_percent != null && !order_discount_percent.trim().isEmpty()) {
            double discountPercent = Double.parseDouble(order_discount_percent);
            contentValues.put("DiscPercent", discountPercent);
        } else {
            // Handle the case where the discount percent is empty or null
            contentValues.put("DiscPercent", 0); // or any default value you prefer
        }
        contentValues.put("VAT",VAT );
        contentValues.put("DiscBuyerName",disc_buyer_name );
        contentValues.put("DiscIdNumber",disc_buyer_id );
        contentValues.put("DiscOther",disc_other );





        long result = db.insert(TABLE_NAME4FinalWDiscount, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }
    public void deleteInvoiceReceiptDiscount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String strsql = "DELETE FROM InvoiceReceiptItemFinalWDiscountTemp";
        db.execSQL(strsql);
    }

//        private static final String payment_rendered_amount="PaymentRenderedAmount";
//        private static final String payment_change_amount="ChangeAmount";
//        private static final String payment_type="typeOfPayment";


    public boolean insertInvoiceReceiptTotal( String transaction_ID,String order_QtyTotal,String order_discount_qty,String order_discount_amount,String order_PriceTotal,String payment_rendered_amount, String payment_change_amount, String payment_type,String transaction_type,String TransDate,String TransTime,String TransUser,String shiftActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderQtyTotal",order_QtyTotal );


        contentValues.put("DiscQty",order_discount_qty );
        contentValues.put("DiscAmount",order_discount_amount );
//        contentValues.put("DiscPercent",order_discount_percent );



        contentValues.put("OrderPriceTotal",order_PriceTotal );

        contentValues.put("PaymentRenderedAmount",payment_rendered_amount );
        contentValues.put("ChangeAmount",payment_change_amount );
        contentValues.put("typeOfPayment",payment_type );

        contentValues.put("typeOfTransaction",transaction_type );


        contentValues.put("TransDate",TransDate );
        contentValues.put("TransTime",TransTime );
        contentValues.put("TransUser",TransUser );
        contentValues.put("shiftActive",shiftActive );



        long result = db.insert("InvoiceReceiptTotal", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertInvoiceReceiptTotalSuspend( String transaction_ID,String order_QtyTotal,String order_discount_qty,String order_discount_amount,String order_PriceTotal,String payment_rendered_amount, String payment_change_amount, String payment_type,String transaction_type,String TransDate,String TransTime,String TransUser,String shiftActive) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );
        contentValues.put("OrderQtyTotal",order_QtyTotal );


        contentValues.put("DiscQty",order_discount_qty );
        contentValues.put("DiscAmount",order_discount_amount );
//        contentValues.put("DiscPercent",order_discount_percent );



        contentValues.put("OrderPriceTotal",order_PriceTotal );

        contentValues.put("PaymentRenderedAmount",payment_rendered_amount );
        contentValues.put("ChangeAmount",payment_change_amount );
        contentValues.put("typeOfPayment",payment_type );

        contentValues.put("typeOfTransaction",transaction_type );


        contentValues.put("TransDate",TransDate );
        contentValues.put("TransTime",TransTime );
        contentValues.put("TransUser",TransUser );
        contentValues.put("shiftActive",shiftActive );



        long result = db.insert("InvoiceReceiptTotalSuspend", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }


    public boolean insertCashInOut( String transaction_ID,String total_1000,String total_500,String total_200,String total_100,String total_50,
                                    String total_20,String total_10,String total_5,String total_1,String total_cents,String transactionType,String CashierID,String CashierName,String POSCounterName,String TransDate,String TransTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID",transaction_ID );

        contentValues.put("cash1000",total_1000 );
        contentValues.put("cash500",total_500 );
        contentValues.put("cash200",total_200 );
        contentValues.put("cash100",total_100 );
        contentValues.put("cash50",total_50 );
        contentValues.put("cash20",total_20 );
        contentValues.put("cash10",total_10 );
        contentValues.put("cash5",total_5 );
        contentValues.put("cash1",total_1 );
        contentValues.put("cashCents",total_cents );
        contentValues.put("TransType",transactionType );

        contentValues.put("CashierID",CashierID );
        contentValues.put("CashierName",CashierName );
        contentValues.put("POSCounterName",POSCounterName );

        contentValues.put("TransDate",TransDate );
        contentValues.put("TransTime",TransTime );
      //  contentValues.put("TransUser",TransUser );



        long result = db.insert("CashInOut", null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }




    public boolean insertOfficialReceipt(String ORNo,String transaction_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ORNo",ORNo );
        contentValues.put("TransactionID",transaction_ID );

        //  contentValues.put("TransUser",TransUser );



        long result = db.insert(TABLE_NAME7, null, contentValues);

        if (result == 1)
            return false;
        else
            return true;
    }







    public boolean insertResetCtr() {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("resetCtrID", "1");
            contentValues.put("resetCtrValue", "0");


            long result = db.insert("ResetCounter", null, contentValues);

            if (result == 1) {

                Log.e("INSERT", "INSERTED");
                return false;
            } else {
                return true;
            }
        }catch (Exception ex){
//            Log.e("ERROR","DUPLICATE");
            return false;
        }


    }

    public boolean insertFinalTransactionReportDB(
            String TransactionID,String ORTrans,String TotalAmount,String TotalDiscount,String TotalVatableSales,String TotalVatAmount,
            String TotalVatExempt,String TotalZeroRatedSales,String TotalLessVat,String TotalServiceCharge,
            String TransUser,String TransUserID,String TransDate,String TransTime,String TransShift,String SalesOverrun) {



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionID", TransactionID);
        contentValues.put("ORTrans", ORTrans);
        contentValues.put("TotalAmount", TotalAmount);
        contentValues.put("TotalDiscount", TotalDiscount);
        contentValues.put("TotalVatableSales", TotalVatableSales);
        contentValues.put("TotalVatAmount", TotalVatAmount);
        contentValues.put("TotalVatExempt", TotalVatExempt);
        contentValues.put("TotalZeroRatedSales", TotalZeroRatedSales);
        contentValues.put("TotalLessVat", TotalLessVat);
        contentValues.put("TotalServiceCharge", TotalServiceCharge);
        contentValues.put("TransUser", TransUser);
        contentValues.put("TransUserID", TransUserID);
        contentValues.put("TransDate", TransDate);
        contentValues.put("TransTime", TransTime);
        contentValues.put("TransShift", TransShift);
        contentValues.put("TotalSalesOverrun",SalesOverrun);



        long result = db.insert("FinalTransactionReport", null, contentValues);

        if (result == 1) {
            //  Toast.makeText(context.getApplicationContext(), "INSERT", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }







    public boolean InsertBankTransactionTemp(String TransNum,String ORTrans,String BankName,String CardOwner,String CardNum,String CardExpiry,String CardApproval,String TransDate,String TransTime,String User,String CardType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransNum",TransNum );
        contentValues.put("ORTrans",ORTrans );
        contentValues.put("BankName",BankName );

        contentValues.put("CardOwner",CardOwner );
        contentValues.put("CardNum",CardNum);
        contentValues.put("CardExpiry",CardExpiry );
        contentValues.put("CardApproval",CardApproval );
        contentValues.put("TransDate",TransDate );
        contentValues.put("TransTime",TransTime );
        contentValues.put("User",User );
        contentValues.put("CardType",CardType );







        long result = db.insert("BankTransactionTemp", null, contentValues);

        if (result == 1) {
          //  Toast.makeText(context.getApplicationContext(), "INSERT", Toast.LENGTH_SHORT).show();
            Log.e("Bank transaction Temp","INSERTED");
            return false;
        }
        else{
            return true;
        }

    }



    public boolean InsertBankTransactionFinal(String TransNum,String ORTrans,String BankName,String CardOwner,String CardNum,String CardExpiry,String CardApproval,String TransDate,String TransTime,String User,String CardType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransNum",TransNum );
        contentValues.put("ORTrans",ORTrans );
        contentValues.put("BankName",BankName );

        contentValues.put("CardOwner",CardOwner );
        contentValues.put("CardNum",CardNum);
        contentValues.put("CardExpiry",CardExpiry );
        contentValues.put("CardApproval",CardApproval );
        contentValues.put("TransDate",TransDate );
        contentValues.put("TransTime",TransTime );
        contentValues.put("User",User );
        contentValues.put("CardType",CardType );

        long result = db.insert("BankTransactionFinal", null, contentValues);

        if (result == 1) {
            //  Toast.makeText(context.getApplicationContext(), "INSERT", Toast.LENGTH_SHORT).show();
            Log.e("Bank transaction Temp","INSERTED");
            return false;
        }
        else{
            return true;
        }

    }

    public boolean insertReturnExchangeTemp(String TransNo,String InvNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransNo",TransNo );
        contentValues.put("InvNo",InvNo );
        long result = db.insert("ReturnExchangeTemp", null, contentValues);

        if (result == 1) {

            //  Toast.makeText(context.getApplicationContext(), "INSERT", Toast.LENGTH_SHORT).show();
         //   Log.e("Bank transaction Temp","INSERTED");
            Log.d("REXCHANGEFinal","TRANS " + TransNo+ "  INV " + InvNo);
            return false;
        }
        else{
            return true;
        }
    }

//      "ID" + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//              "RefTransNo"+"  TEXT,"+
//              "RefInvNo"+"  TEXT,"+
//              "NewTransNo"+"  TEXT,"+
//              "NewInvNo" +"  TEXT)");  // for ReturnExchangeFinal


    public boolean insertReturnExchangeFinal(String RefTransNo,String RefInvNo,String NewTransNo,String NewInvNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("RefTransNo",RefTransNo );
        contentValues.put("RefInvNo",RefInvNo );
        contentValues.put("NewTransNo",NewTransNo );
        contentValues.put("NewInvNo",NewInvNo );
        long result = db.insert("ReturnExchangeFinal", null, contentValues);

        if (result == 1) {
            //  Toast.makeText(context.getApplicationContext(), "INSERT", Toast.LENGTH_SHORT).show();
            //   Log.e("Bank transaction Temp","INSERTED");
            return false;
        }
        else{
            return true;
        }
    }







    public void deleteBank(Context context){

        SQLiteDatabase db2 = context.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        String updateStatus = "delete from BankDetails";
        db2.execSQL(updateStatus);
    }







}


