package com.abztrakinc.ABZPOS.ORDERSTATION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.abztrakinc.ABZPOS.DatabaseHandler;
import com.abztrakinc.ABZPOS.FtpConnect;
import com.abztrakinc.ABZPOS.R;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ordering_station_main extends AppCompatActivity {

    LinearLayout linearLayout, linearLayout2, linearlayout3, linearLayout5, linearLayout4;
    TextView tableNum, totalQtyOrderedTxt, totalAmountInttxt, refNumbertxt, datetxt, usertxt,connectionStatus;
    Button printReceipt, resetReceipt, remarksReceipt, addQty, subtractItem;
    //ScrollView scrollView;
    RecyclerView recyclerView_order;

    ScrollView scrollView;
    int i = 1;

    String DB_NAME = "POSAndroidDB.db";
    String DB_NAME2 = "PosOutputDB.db";
    String queryCategoryBtn = "Select * from SUBCATG";
    String CategoryId;
    SQLiteDatabase db;
    SQLiteDatabase db2;
    DatabaseHandler myDb;
    int origRefNumber;
    String readRefNumber;
    String remarksString;
    String readRemark;
    int activeButton;
    int activeCategId = 0;
    int turnOffColorCateg = 0;
    int turnOffColor = 0;
    int imageOff = 0;
    String offImage = "off";
    String connectionStatusValue;


    String UniversalItemCode, UniversalSubCatg, UniversalRecptDesc;
    String UniversalItemQty;
    String UniversalUnitSellPrice;


    Cursor c;
    Cursor itemListC;
    ArrayList<String> categoryListName;
    ArrayList<String> categoryListId;
    ArrayList<String> itemName;
    ArrayList<String> itemCode;
    ArrayList<String> recptDesc;
    ArrayList<String> itemPrice;
    ArrayList<String> itemImage;
    ArrayList<String> finalOrderedItem;
    String recptDescName;
    String tableNumber;
    String currentDate;
    String currentTime;
    String ipAddress;
    String user;
    String password;
    FTPClient client2;

    //for generating pdf
    Bitmap bmp;
    Bitmap scaledbmp;


    private ArrayList<order_station_product> productList;
    private ArrayList<order_station_product> QtyList;
    private ArrayList<order_station_product> priceList;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering_station_main);

        readReferenceNumber();


        recyclerView = findViewById(R.id.recyclerView_order);
        printReceipt = findViewById(R.id.printReceipt);
        totalQtyOrderedTxt = findViewById(R.id.totalQtyOrderedTxt);
        totalAmountInttxt = findViewById(R.id.totalAmountInttxt);
        resetReceipt = findViewById(R.id.ResetReceipt);
        remarksReceipt = findViewById(R.id.remarksReceipt);
        refNumbertxt = findViewById(R.id.refNumbertxt);
        refNumbertxt.setText(String.valueOf(readRefNumber));
       // addQty = findViewById(R.id.addQty);
       // subtractItem = findViewById(R.id.subtractItem);
        connectionStatus=findViewById(R.id.connectionStatus);

        datetxt = findViewById(R.id.datetxt);
        usertxt = findViewById(R.id.usertxt);
        currentDate = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        datetxt.setText(String.valueOf(currentDate));
        usertxt.setText("bobwendelarcon");

        categoryListName = new ArrayList<String>();
        categoryListId = new ArrayList<String>();
        productList = new ArrayList<>();
        QtyList = new ArrayList<>();
        priceList = new ArrayList<>();
        itemName = new ArrayList<String>();
        itemCode = new ArrayList<String>();
        recptDesc = new ArrayList<String>();
        itemPrice = new ArrayList<String>();
        itemImage = new ArrayList<String>();
        finalOrderedItem = new ArrayList<String>();
        Intent intent = getIntent();
        tableNum = findViewById(R.id.tableNum);
        tableNumber = intent.getStringExtra("tableNumber");
        //Toast.makeText(this, tableNumber, Toast.LENGTH_SHORT).show();
        tableNum.setText("TABLE # " + tableNumber);
        String message;

        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        ipAddress = "10.0.0.44";
        user = "user";
        password = "12345";






           deleteAllItem();
        //create output database
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

//        setProductInfo();
//        setAdapter();
        selectAllDb();
        categorybtn();
        checkConnectionStatus();


        int numberOfButton = c.getCount();

        for (i = 0; i < numberOfButton; i++) {

            linearLayout = findViewById(R.id.linearlayoutid1);
            final Button button = new Button(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            button.setLayoutParams(lp);
            button.setId(i);
            int btnHeight = lp.height;

            button.setTag(categoryListId.get(i));
            button.setText(categoryListName.get(i));
            linearLayout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CategoryId = button.getTag().toString();
                    activeCategId = button.getId();
                    offImage = "off";
                    categorybtn();
                    itembtn();

                }
            });

        }

        printReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //insertFinalReceipt();
                showAllItemInConfirmation();

            }
        });
        resetReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAllItem();

                //  deleteFinalReceipt();

            }
        });
        remarksReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //
                //
                //createJPG();
                remarks();

            }
        });
        addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UniversalItemCode != null) {
                    // checkIfItemExist();
                    checkIfItemExist();
                } else {
                    Toast.makeText(ordering_station_main.this, "NO ITEM TO DELETE", Toast.LENGTH_SHORT).show();
                }

            }
        });


        subtractItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UniversalItemCode != null) {

                    subtractItem();
                } else {
                    Toast.makeText(ordering_station_main.this, "PLEASE SELECT ITEM", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void setAdapter() {
        ordering_station_recyclerAdapter adapter = new ordering_station_recyclerAdapter(productList, QtyList, priceList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    private void categorybtn() {
//        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);


        c = db.rawQuery("select * from SUBCATEGORY", null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        while (c.moveToNext()) {
            categoryListId.add(c.getString(0));
            categoryListName.add(c.getString(1));
        }

        c.close();

    }

    private void itembtn() {
        try {


            c = db.rawQuery("select * from ITEM where SubCatgID='" + CategoryId + "'", null);


            if (c.getCount() == 0) {
                Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            }
            itemCode.clear();
            itemName.clear();
            recptDesc.clear();
            itemPrice.clear();
            itemImage.clear();
            while (c.moveToNext()) {
                itemCode.add(c.getString(0));
                itemName.add(c.getString(2));
                recptDesc.add(c.getString(1));
                itemPrice.add(c.getString(4));
                itemImage.add(c.getString(5));
                // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

            }
            //  Toast.makeText(this, String.valueOf(itemName.get(1)), Toast.LENGTH_SHORT).show();
            scrollView = findViewById(R.id.scrollview);
            scrollView.scrollTo(0, 0);
            turnOffColor = 1;
            activeButton = 0;
            UniversalItemCode = "";
            showItem();
            c.close();
        }
        catch (Exception e){
            Toast.makeText(this, "ERROR IN ITEM BTN", Toast.LENGTH_SHORT).show();
        }

    }



    @SuppressLint("ResourceAsColor")
    private void showItem() {

try {


    int numberOfItem = itemName.size();
    linearlayout3 = findViewById(R.id.linearlayoutid3);
    linearLayout4 = findViewById(R.id.linearlayoutid4);
    linearLayout5 = findViewById(R.id.linearlayoutid5);
    linearlayout3.removeAllViews();
    linearLayout5.removeAllViews();
    float buttonSize;


    int totalBtn = numberOfItem;
    int excessBtn = totalBtn % 3;
    int newTotalBtn = totalBtn - excessBtn;
    int row = newTotalBtn / 3 + 1;
    int col = 5;
    int width = linearLayout5.getWidth() / 3;


    int btnCounter = 0;
    for (int i2 = 1; i2 <= row; i2++) {
        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);


        row2.setLayoutParams(new ViewGroup.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));


        for (int j2 = 0; j2 < 3; j2++) {

            linearlayout3.removeView(row2);
            LinearLayout rootView = new LinearLayout(this);
            rootView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rootView.setOrientation(LinearLayout.VERTICAL);


            btnCounter++;

            if (btnCounter <= totalBtn) {

                final ImageButton button = new ImageButton(this);
                final ImageView imageView = new ImageView(this);


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
                imageView.setLayoutParams(layoutParams);
                imageView.setAlpha(0.0f);
                imageView.setBackgroundResource(R.drawable.c);


                final TextView textView = new TextView(this);
                if (imageOff == 1) {

                } else {
                    c = db.rawQuery("select * from ITEM where itemID='" + itemCode.get(btnCounter - 1) + "'", null);
                    if (c.getCount() == 0) {

                    }

                    while (c.moveToNext()) {
                        try {

                            Bitmap bmImg = BitmapFactory.decodeFile(c.getString(5));
                            Bitmap resized = Bitmap.createScaledBitmap(bmImg, 400, 400, true);
                            button.setImageBitmap(resized);
                        } catch (Exception e) {
                            button.setBackgroundResource(R.mipmap.ic_launcher_round);
                        }
                    }
                    c.close();
                }


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, 150);


                button.setId(btnCounter - 1);
                button.setTag(itemName.get(btnCounter - 1));
                button.setLayoutParams(lp);

                button.setId(btnCounter - 1);


                String productNameInList = recptDesc.get(btnCounter - 1);
                String ItemCode = itemCode.get(btnCounter - 1);


                if (offImage == "off") {

                    imageView.setAlpha(0.0f);

                } else {
                    if (button.getId() == activeButton) {

                        imageView.setAlpha(1.0f);
                    } else {
                        imageView.setAlpha(0.0f);
                    }
                }

                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                frameLayout.addView(button);
                frameLayout.addView(imageView);


                rootView.addView(frameLayout);


                String SubCatg = CategoryId;
                String RecptDesc = recptDesc.get(btnCounter - 1);
                int itmQty = 1;
                String ItemQty = String.valueOf(itmQty);
                String UnitSellPrice = itemPrice.get(btnCounter - 1);

                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);

                textView.setText(itemName.get(btnCounter - 1));
                textView.setLayoutParams(lp2);


                // textView.setBackgroundColor(Color.BLUE);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        offImage = "on";
                        UniversalItemCode = ItemCode;
                        UniversalItemQty = ItemQty;
                        UniversalRecptDesc = RecptDesc;
                        UniversalUnitSellPrice = UnitSellPrice;
                        UniversalSubCatg = SubCatg;
                        activeButton = button.getId();
                        showItem();


                    }
                });


                rootView.addView(textView);


            }

            row2.addView(rootView);
            linearlayout3.addView(row2);
        }

    }
    linearLayout5.addView(linearlayout3);
}
catch (Exception ex) {
    Toast.makeText(this, "ERROR IN SHOWITEM", Toast.LENGTH_SHORT).show();

        }


    }

    private void setViewRecyclerViewLastItem() {
        recyclerView_order = findViewById(R.id.recyclerView_order);
        recyclerView_order.scrollToPosition(productList.size() - 1);
    }

    private void insertItemOrdered() {


        if (UniversalItemCode != "") {


            myDb = new DatabaseHandler(this);
            boolean isInserted = myDb.insertOrderList(
                    UniversalItemCode,
                    UniversalSubCatg,
                    UniversalRecptDesc,
                    UniversalItemQty,
                    UniversalUnitSellPrice);

            if (isInserted = true) {

                selectAllDb();
                setViewRecyclerViewLastItem();


            } else {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "PLEASE SELECT ITEM", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectAllDb() {
        productList.clear();
        QtyList.clear();
        priceList.clear();
         db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl ", null);
        if (itemListC.getCount() == 0) {
            //  Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            totalQtyOrderedTxt.setText(" ");
            totalAmountInttxt.setText(" ");
        }
        while (itemListC.moveToNext()) {
            productList.add(new order_station_product(itemListC.getString(2)));
            QtyList.add(new order_station_product("X" + itemListC.getString(3)));
            priceList.add(new order_station_product("P" + itemListC.getString(4)));

        }
        totalQtyOrdered();
        setAdapter();
        itemListC.close();


    }

    private void checkIfItemExist() {
        String strQty;
        String strPrice;
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl where ItemCode ='" + UniversalItemCode + "'", null);
        if (itemListC.getCount() != 0) {
            // Toast.makeText(this, "DUPLICATE " + UniversalItemCode, Toast.LENGTH_SHORT).show();
            while (itemListC.moveToNext()) {
                strQty = itemListC.getString(3);
                strPrice = itemListC.getString(4);

                int finalQty = Integer.parseInt(strQty) + 1;
                int finalPrice = Integer.parseInt(strPrice) + Integer.parseInt(UniversalUnitSellPrice);

                String strsql = "UPDATE OrderTakeTBl SET ItemQty=" + finalQty + ", UnitSellPrice =" + finalPrice + "  where ItemCode='" + UniversalItemCode + "'";
                db2.execSQL(strsql);
                selectAllDb();
                setViewRecyclerViewLastItem();
            }
        } else {
            insertItemOrdered();
        }
        itemListC.close();

    }

    private void subtractItem() {


        String strQty;
        String strPrice;
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl where ItemCode ='" + UniversalItemCode + "'", null);
        if (itemListC.getCount() != 0) {
            // Toast.makeText(this, "DUPLICATE " + UniversalItemCode, Toast.LENGTH_SHORT).show();
            while (itemListC.moveToNext()) {
                strQty = itemListC.getString(3);
                strPrice = itemListC.getString(4);

                if (Integer.parseInt(strQty) > 1) {
                    int finalQty = Integer.parseInt(strQty) - 1;
                    int finalPrice = Integer.parseInt(strPrice) - Integer.parseInt(UniversalUnitSellPrice);

                    String strsql = "UPDATE OrderTakeTBl SET ItemQty=" + finalQty + ", UnitSellPrice =" + finalPrice + "  where ItemCode='" + UniversalItemCode + "'";
                    db2.execSQL(strsql);
                    selectAllDb();
                } else if (Integer.parseInt(strQty) <= 1) {
                    db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                    // itemListC = db2.rawQuery("Delete * from OrderTakeTBl where ItemCode ='"+UniversalItemCode+"'" , null);
                    String strsql2 = "Delete from OrderTakeTBl where ItemCode ='" + UniversalItemCode + "'";
                    db2.execSQL(strsql2);
                    selectAllDb();
                    //db2.execSQL(itemListC);
                }


            }
        } else {
            // insertItemOrdered();
        }
        itemListC.close();

    }

    private void totalQtyOrdered() {
        int totalQtyInt = 0;
        int totalAmountInt = 0;
        String totalQtyString;
        String totalAmountString;
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl", null);
        if (itemListC.getCount() != 0) {
            while (itemListC.moveToNext()) {
                totalQtyString = itemListC.getString(3);
                totalQtyInt = totalQtyInt + Integer.parseInt(totalQtyString);
                totalQtyOrderedTxt.setText(String.valueOf(totalQtyInt));


                totalAmountString = itemListC.getString(4);
                totalAmountInt = totalAmountInt + Integer.parseInt(totalAmountString);
                totalAmountInttxt.setText("P " + String.valueOf(totalAmountInt));


            }
        }
        itemListC.close();
    }

    private void deleteAllItem() {
        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        String strsql3 = "Delete from OrderTakeTBl ";
        db2.execSQL(strsql3);


//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        String strsql4 = "Delete from RemarkReceipt ";
        db2.execSQL(strsql4);
        selectAllDb();
    }


    private void deleteFinalReceipt() {
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        String strsql3 = "Delete from FinalReceipt ";
        db2.execSQL(strsql3);


        selectAllDb();
    }


    private void remarks() {
        readRemarks();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setTitle("REMARKS");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        input.setSingleLine(false);
        // input.setLines(5);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        input.setText(readRemark);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                remarksString = input.getText().toString();
//                db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                itemListC = db2.rawQuery("select * from RemarkReceipt where refNumber='" + readRefNumber + "'", null);
                if (itemListC.getCount() == 0) {

                    insertRemarks();


                } else {

                    updateRemarks();

                }


                itemListC.close();
            }


        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }

        });
        builder.show();

    }

    private void insertRemarks() {
        myDb = new DatabaseHandler(this);
        boolean isInserted = myDb.insertRemarks(readRefNumber, remarksString
        );
        // Toast.makeText(this, UniversalRecptDesc, Toast.LENGTH_SHORT).show();
        if (isInserted = true) {
            //  Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();


        } else {
            // Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateRemarks() {
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        // itemListC = db2.rawQuery("select * from RemarkReceipt where refNumber ='"+readRefNumber+"'" , null);
        String strsql = "UPDATE RemarkReceipt SET remCol='" + remarksString + "'  where refNumber='" + readRefNumber + "'";
        db2.execSQL(strsql);
    }

    private void readRemarks() {
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from RemarkReceipt where refNumber ='" + readRefNumber + "'", null);
        if (itemListC.getCount() == 0) {
            readRemark = "";


        } else {
            while (itemListC.moveToNext()) {
                readRemark = itemListC.getString(1);
            }
            //  Toast.makeText(this, readRemark, Toast.LENGTH_SHORT).show();
        }
        itemListC.close();

    }

    private void readReferenceNumber() {

        //primary key 00000001

        // int readPK;

        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from FinalReceipt ", null);
        if (itemListC.getCount() == 0) {

            origRefNumber = 1;
            String formatted = String.format("%09d", origRefNumber);
            readRefNumber = formatted;
        } else {

            itemListC = db2.rawQuery("SELECT * FROM FinalReceipt", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%09d", incrementPK);

            readRefNumber = incrementPKString;


            // }
        }
        itemListC.close();


    }

    private void insertFinalReceipt() {
        myDb = new DatabaseHandler(this);
        boolean isInserted = myDb.insertFinalReceipt(readRefNumber, tableNumber, totalAmountInttxt.getText().toString(),
                totalQtyOrderedTxt.getText().toString(), currentTime, currentDate, usertxt.getText().toString(), remarksString
        );
        // Toast.makeText(this, UniversalRecptDesc, Toast.LENGTH_SHORT).show();
        if (isInserted = true) {
            //Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();

            checkIfOrderIsEmpty();


        } else {

            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfOrderIsEmpty() {
        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl", null);
        if (itemListC.getCount() != 0) {



            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
//
                    createTextfile();
                    finish();



                }

            }).start();


          FTP();



        } else {
            Toast.makeText(this, "ORDER IS EMPTY", Toast.LENGTH_SHORT).show();
        }
        itemListC.close();
       // deleteAllItem();
    }

    private void showAllItemInConfirmation() {
        readRemarks();
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl", null);
        StringBuffer buffer = new StringBuffer();
        if (itemListC.getCount() != 0) {
            int cnter = 1;
            while (itemListC.moveToNext()) {

                String counter = (String.valueOf(cnter++ + ".     ")).toString().substring(0, 4);
                String orderItem = (itemListC.getString(2));
                String orderQty = itemListC.getString(3);


                buffer.append(counter + orderItem + "\n" + "     " + "x" + orderQty + "\n\n");


            }
            buffer.append("REMARKS: " + readRemark);
            showMessage("SUMMARY OF ORDER", buffer.toString());
        } else {
            Toast.makeText(this, "ORDER IS EMPTY", Toast.LENGTH_SHORT).show();

        }
      //  itemListC.close();

    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("CONFIRM ORDER", new DialogInterface.OnClickListener() {


            @Override

            public void onClick(DialogInterface dialog, int which) {

                FtpConnect checkConnection = new FtpConnect();
             // if (client2.isConnected()){

                  readRemarks();
                  insertFinalReceipt();
               //  deleteAllItem();

//                  try {
//                      client2.logout();
//
//                  } catch (IOException exception) {
//                      exception.printStackTrace();
//                      connectionStatus.setText("NOT  2CONNECTED TO NETWORK");
//                  }
//
//              }
//                if (!client2.isConnected()) {
//                    connectionStatus.setText("NOT 3CONNECTED TO NETWORK");
//
//                  //  return;
             // }



            }


        });

        builder.setNeutralButton("Cancel Order", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }

        });
        builder.show();

    }

    StringBuffer buffer;

    private void createTextfile() {
        try {
            readRemarks();
            File sendFile = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/SEND FILE");
            db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
            Cursor cursor = db2.rawQuery("SELECT * FROM OrderTakeTBl", null);
            buffer = new StringBuffer();
            File file = new File(sendFile, readRefNumber + ".txt");
            FileOutputStream stream = new FileOutputStream(file);
            buffer.append("REFERENCE: " + readRefNumber + "\n");
            buffer.append("USER: " + usertxt.getText() + "\n");
            buffer.append("TABLE: " + tableNumber + "\n");
            buffer.append("DATE: " + currentDate + "\nTIME: " + currentTime + "\n\n");

            try {


                while (cursor.moveToNext()) {
                    buffer.append(((cursor.getString(2) + "                                                ").substring(0,30) + "x"+cursor.getString(3) + "\r\n"));
                }
                buffer.append("===================================" + "\n");
                buffer.append("REMARKS: " + readRemark + "\n");
                stream.write(buffer.toString().getBytes());
            } finally {
                stream.close();
            }
        } catch (Exception e) {


        }
    }

    private void FTP() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FtpConnect ftpConnect = new FtpConnect();
        String filename = readRefNumber + ".txt";
        File receiptFile = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/SEND FILE/" + filename);
        //  File    receiptFile=new File(Environment.getExternalStorageDirectory()+"/ANDROID_POS/");
        String Filepath = receiptFile.toString();
        String OutputName = readRefNumber + ".txt";

        try {

           ftpConnect.jetzt(filename, Filepath, OutputName, ipAddress, user, password);

        } catch (IOException exception) {

        }
}
    //private FTPClient ftp = null;


    private void checkConnectionStatus(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                 client2 = new FTPClient();

                try {


                    client2.connect(ipAddress);//10.0.0.36
                    client2.login(user, password);
                    if (client2.isConnected()) {
                        connectionStatus.setText("CONNECTED");
                    }
                   else if (!client2.isConnected()) {
                        connectionStatus.setText("not connected");
                    }
                    else{
                        connectionStatus.setText("not connected");
                    }



                }
                catch (Exception e){

                }
            }

        }).start();
        //connectionStatus.setText(connectionStatusValue);

    }


}







