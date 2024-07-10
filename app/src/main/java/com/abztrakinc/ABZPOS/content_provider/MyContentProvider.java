package com.abztrakinc.ABZPOS.content_provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.DatabaseHandlerSettings;
import com.abztrakinc.ABZPOS.settingsDB;

public class MyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.abztrakinc.ABZPOS.provider";
    public static final Uri CONTENT_URI_HEADER = Uri.parse("content://" + AUTHORITY + "/receiptHeader");
    public static final Uri CONTENT_URI_FOOTER = Uri.parse("content://" + AUTHORITY + "/receiptFooter");
    private static final int HEADER_DETAILS = 1;
    private static final int FOOTER_DETAILS = 2;

    // posOutputDB

    public static final Uri posOutputDB_invoiceReceipt = Uri.parse("content://" + AUTHORITY + "/InvoiceReceiptTotal");
    public static final Uri posOutputDB_OR = Uri.parse("content://" + AUTHORITY + "/InvoiceReceiptTotal");
    public static final Uri posOutputDB_FinalTransactionReport = Uri.parse("content://" + AUTHORITY + "/FinalTransactionReport");
    public static final Uri posOutputDB_InvoiceReceiptItemFinal = Uri.parse("content://" + AUTHORITY + "/InvoiceReceiptItemFinal");
    public static final Uri posOutputDB_InvoiceReceiptItemFinalWDiscount = Uri.parse("content://" + AUTHORITY + "/InvoiceReceiptItemFinalWDiscount");
    public static final Uri posOutputDB_OfficialReceipt = Uri.parse("content://" + AUTHORITY + "/OfficialReceipt");
    private static final int invoiceReceipt_Details = 3;
    private static final int OR_Details = 4;
    private static final int FinalTransactionReport_insert = 7;
    private static final int invoiceReceiptItemFinal_insert = 8;
    private static final int invoiceReceiptItemFinalWDiscount_insert = 11;
    private static final int invoiceReceiptTotal_insert = 9;
    private static final int OfficialReceipt_insert = 10;

    //settingsDB


    public static final Uri settings_shift = Uri.parse("content://" + AUTHORITY + "/ShiftingTable");
    public static final Uri settings_system_date = Uri.parse("content://" + AUTHORITY + "/SystemDate");

    private static final int shift_Details = 5;

    private static final int system_date_Details = 6;
    private static final int printer_Details = 12;

  //  private static final int OR_Details = 4;






    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "receiptHeader", HEADER_DETAILS);
        sUriMatcher.addURI(AUTHORITY, "receiptFooter", FOOTER_DETAILS);

        //posoutputdb
        sUriMatcher.addURI(AUTHORITY, "InvoiceReceiptTotal", invoiceReceipt_Details);
       // sUriMatcher.addURI(AUTHORITY, "InvoiceReceiptTotal", invoiceReceiptTotal_insert);
        sUriMatcher.addURI(AUTHORITY, "OfficialReceipt", OR_Details);
        sUriMatcher.addURI(AUTHORITY, "FinalTransactionReport", FinalTransactionReport_insert);
        sUriMatcher.addURI(AUTHORITY, "InvoiceReceiptItemFinal", invoiceReceiptItemFinal_insert);
        sUriMatcher.addURI(AUTHORITY, "InvoiceReceiptItemFinalWDiscount", invoiceReceiptItemFinalWDiscount_insert);

       // sUriMatcher.addURI(AUTHORITY, "OfficialReceipt", OfficialReceipt_insert);

        //settingsdb
        sUriMatcher.addURI(AUTHORITY, "ShiftingTable", shift_Details);
        sUriMatcher.addURI(AUTHORITY, "SystemDate", system_date_Details);
        sUriMatcher.addURI(AUTHORITY, "PrinterSettings", printer_Details);



    }


    private SQLiteDatabase mDatabase,mDatabase2,mDatabase3;
    SQLiteOpenHelper dbHelper2;

    @Override
    public boolean onCreate() {
        SQLiteOpenHelper dbHelper = new DatabaseHandlerSettings(getContext());
         dbHelper2 = new DatabaseHandler(getContext());
        SQLiteOpenHelper dbHelper3 = new settingsDB(getContext());

        mDatabase = dbHelper.getWritableDatabase();
        mDatabase2 = dbHelper2.getWritableDatabase();
        mDatabase3 = dbHelper3.getWritableDatabase();

        return (mDatabase != null && mDatabase2 !=null && mDatabase3 !=null );
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case HEADER_DETAILS:
                cursor = mDatabase.query("receiptHeader", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FOOTER_DETAILS:
                cursor = mDatabase.query("receiptFooter", projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case invoiceReceipt_Details:
                cursor = mDatabase2.query("InvoiceReceiptTotal", projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case OR_Details:
                cursor = mDatabase2.query("OfficialReceipt", projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case shift_Details:
                cursor = mDatabase3.query("ShiftingTable", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case system_date_Details:
                cursor = mDatabase3.query("SystemDate", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case printer_Details:
                cursor = mDatabase3.query("PrinterSettings", projection, selection, selectionArgs, null, null, sortOrder);
                break;






            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case HEADER_DETAILS:
                return "vnd.android.cursor.dir/vnd.abztrakinc.receiptHeader";
            case FOOTER_DETAILS:
                return "vnd.android.cursor.dir/vnd.abztrakinc.receiptFooter";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id;
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case HEADER_DETAILS:
                id = mDatabase.insert("receiptHeader", null, values);
                returnUri = Uri.parse(CONTENT_URI_HEADER + "/" + id);
                break;

            case FOOTER_DETAILS:
                id = mDatabase.insert("receiptFooter", null, values);
                returnUri = Uri.parse(CONTENT_URI_FOOTER + "/" + id);
                break;

            case FinalTransactionReport_insert:
                // Insert into FinalTransactionReport table
                id = mDatabase2.insert("FinalTransactionReport", null, values);
                if (id > 0) {
                    returnUri = Uri.withAppendedPath(posOutputDB_FinalTransactionReport, String.valueOf(id));
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            case invoiceReceiptItemFinal_insert:
                // Insert into FinalTransactionReport table
                id = mDatabase2.insert("InvoiceReceiptItemFinal", null, values);
                if (id > 0) {
                    returnUri = Uri.withAppendedPath(posOutputDB_InvoiceReceiptItemFinal, String.valueOf(id));
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;


            case invoiceReceiptItemFinalWDiscount_insert:
                // Insert into FinalTransactionReport table
                id = mDatabase2.insert("InvoiceReceiptItemFinalWDiscount", null, values);
                if (id > 0) {
                    returnUri = Uri.withAppendedPath(posOutputDB_InvoiceReceiptItemFinalWDiscount, String.valueOf(id));
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;


            case invoiceReceipt_Details:
                // Insert into FinalTransactionReport table
                id = mDatabase2.insert("InvoiceReceiptTotal", null, values);
                if (id > 0) {
                    returnUri = Uri.withAppendedPath(posOutputDB_invoiceReceipt, String.valueOf(id));
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            case OR_Details:
                // Insert into FinalTransactionReport table
                id = mDatabase2.insert("OfficialReceipt", null, values);
                if (id > 0) {
                    returnUri = Uri.withAppendedPath(posOutputDB_OfficialReceipt, String.valueOf(id));
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case HEADER_DETAILS:
                rowsDeleted = mDatabase.delete("receiptHeader", selection, selectionArgs);
                break;
            case FOOTER_DETAILS:
                rowsDeleted = mDatabase.delete("receiptFooter", selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated;
        switch (sUriMatcher.match(uri)) {
            case HEADER_DETAILS:
                rowsUpdated = mDatabase.update("receiptHeader", values, selection, selectionArgs);
                break;

            case FOOTER_DETAILS:
                rowsUpdated = mDatabase.update("receiptFooter", values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsUpdated;
    }
}
