package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ordering_station_main extends AppCompatActivity {

    LinearLayout linearLayout,linearLayout2,linearlayout3,linearLayout5,linearLayout4;
    TextView tableNum,totalQtyOrderedTxt,totalAmountInttxt,refNumbertxt,datetxt,usertxt;
    Button printReceipt,resetReceipt,remarksReceipt;
    RecyclerView recyclerView_order;

    ScrollView scrollView;
    int i=1;

    String DB_NAME = "V6BO.db";
    String DB_NAME2 = "PosOutputDB.db";
    String queryCategoryBtn ="Select * from SUBCATG";
    String CategoryId;
    SQLiteDatabase db;
    SQLiteDatabase db2;
    DatabaseHandler myDb;
    int origRefNumber;
    String readRefNumber;
    String remarksString;
    String readRemark;



    String UniversalItemCode,UniversalSubCatg,UniversalRecptDesc;
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
    ArrayList<String> finalOrderedItem;
    String recptDescName;
    String tableNumber;
    String currentDate;
    String currentTime;


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
        totalQtyOrderedTxt=findViewById(R.id.totalQtyOrderedTxt);
        totalAmountInttxt=findViewById(R.id.totalAmountInttxt);
        resetReceipt = findViewById(R.id.ResetReceipt);
        remarksReceipt = findViewById(R.id.remarksReceipt);
        refNumbertxt= findViewById(R.id.refNumbertxt);
        refNumbertxt.setText(String.valueOf(readRefNumber));

        datetxt = findViewById(R.id.datetxt);
        usertxt= findViewById(R.id.usertxt);
        currentDate = new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        datetxt.setText(String.valueOf(currentDate));
        usertxt.setText("bobwendelarcon");

        categoryListName = new ArrayList<String>();
        categoryListId   = new ArrayList<String>();
        productList = new ArrayList<>();
        QtyList = new ArrayList<>();
        priceList = new ArrayList<>();
        itemName  = new ArrayList<String>();
        itemCode  = new ArrayList<String>();
        recptDesc = new ArrayList<String>();
        itemPrice = new ArrayList<String>();
        finalOrderedItem = new ArrayList<String>();
        Intent intent=getIntent();
        tableNum = findViewById(R.id.tableNum);
        tableNumber=intent.getStringExtra("tableNumber");
        //Toast.makeText(this, tableNumber, Toast.LENGTH_SHORT).show();
        tableNum.setText("TABLE # " + tableNumber);

        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);




        //create output database
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

//        setProductInfo();
//        setAdapter();
            selectAllDb();
            categorybtn();
        int numberOfButton=c.getCount();

        for (i = 0; i <numberOfButton; i++) {

            linearLayout = findViewById(R.id.linearlayoutid1);
            final Button button = new Button(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            button.setLayoutParams(lp);
            button.setId(i);
            button.setTag(categoryListId.get(i));
            button.setText(categoryListName.get(i));
            linearLayout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CategoryId = button.getTag().toString();
//
                 //   Toast.makeText(ordering_station_main.this,CategoryId , Toast.LENGTH_SHORT).show();
                   //showItem();
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


    }

    private void setAdapter() {
        ordering_station_recyclerAdapter adapter = new ordering_station_recyclerAdapter(productList,QtyList,priceList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



    private void categorybtn() {
//        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);



        c = db.rawQuery("select * from SUBCATG", null);
        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        while(c.moveToNext()){
            categoryListId.add(c.getString(0));
            categoryListName.add(c.getString(1));
        }
    }

    private void itembtn(){
        c = db.rawQuery("select * from ITEM where SUBCATG='"+CategoryId +"'", null);


        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        while(c.moveToNext()){
            itemCode.add(c.getString(0));
            itemName.add(c.getString(2));
            recptDesc.add(c.getString(1));
            itemPrice.add(c.getString(71));
            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

        }
      //  Toast.makeText(this, String.valueOf(itemName.get(1)), Toast.LENGTH_SHORT).show();

        showItem();

    }

    private void showItem(){

         int numberOfItem = itemName.size();
        Toast.makeText(this,String.valueOf(numberOfItem), Toast.LENGTH_SHORT).show();
        linearLayout2 = findViewById(R.id.linearlayoutid2);
        linearlayout3= findViewById(R.id.linearlayoutid3);
        linearLayout4= findViewById(R.id.linearlayoutid4);
        linearLayout5 = findViewById(R.id.linearlayoutid5);

        linearLayout2.removeAllViews();
        linearlayout3.removeAllViews();
        linearLayout4.removeAllViews();
        float buttonSize;
         for (int j =0;j<numberOfItem;j++){


            final TextView TextView = new TextView(this);
            final Button btnAdd = new Button(this);
            final Button btnSubtract = new Button(this);

            //subtract
             LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
             btnSubtract.setLayoutParams(lp);
             btnSubtract.setId(j);
             //   button2.setText(itemName.get(j));
             btnSubtract.setText("-");
             linearlayout3.addView(btnSubtract);


             String productNameInLists = recptDesc.get(j);
             String ItemCodes = itemCode.get(j);

             String SubCatgs = CategoryId;
             String RecptDescs =recptDesc.get(j);
             int itmQtys = 1;
             String ItemQtys = String.valueOf(itmQtys);
             String UnitSellPrices = itemPrice.get(j);
             btnSubtract.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     UniversalItemCode = ItemCodes;
                     UniversalItemQty =ItemQtys;
                     UniversalRecptDesc = RecptDescs;
                     UniversalUnitSellPrice = UnitSellPrices;
                     UniversalSubCatg = SubCatgs;

                     subtractItem();

                 }
             });


             //textview
             LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,50);
             TextView.setLayoutParams(lp2);
             TextView.setId(j);
             TextView.setTextSize(15);
             TextView.setText(itemName.get(j));
             linearLayout2.addView(TextView);



             btnAdd.setLayoutParams(lp);
             btnAdd.setId(j);
             //   button2.setText(itemName.get(j));
             btnAdd.setText("+");
             btnAdd.setTag(itemName.get(j));
             String productNameInList = recptDesc.get(j);
             String ItemCode = itemCode.get(j);

             String SubCatg = CategoryId;
             String RecptDesc =recptDesc.get(j);
             int itmQty = 1;
             String ItemQty = String.valueOf(itmQty);
             String UnitSellPrice = itemPrice.get(j);




                     btnAdd.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {


                  //  productList.add(new order_station_product(productNameInList));
                   //  productList.add(new order_station_product(SubCatg));

                     //note : insert to database to check if duplicate entry
                     UniversalItemCode = ItemCode;
                     UniversalItemQty =ItemQty;
                     UniversalRecptDesc = RecptDesc;
                     UniversalUnitSellPrice = UnitSellPrice;
                     UniversalSubCatg = SubCatg;
                     //insertItemOrdered();
                     checkIfItemExist();





                 }
             });
             linearLayout4.addView(btnAdd);


        }


    }
    private void setViewRecyclerViewLastItem(){
        recyclerView_order=findViewById(R.id.recyclerView_order);
        recyclerView_order.scrollToPosition(productList.size() - 1);
    }
    private void insertItemOrdered(){
        myDb = new DatabaseHandler(this);
        boolean isInserted = myDb.insertOrderList(
                UniversalItemCode,
                UniversalSubCatg,
                UniversalRecptDesc,
                UniversalItemQty,
               UniversalUnitSellPrice);
       // Toast.makeText(this, UniversalRecptDesc, Toast.LENGTH_SHORT).show();
        if (isInserted = true){
          //  Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();
            selectAllDb();
            setViewRecyclerViewLastItem();



        }
        else{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectAllDb(){
        productList.clear();
        QtyList.clear();
        priceList.clear();
       // db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl ", null);
        if (itemListC.getCount()==0){
          //  Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            totalQtyOrderedTxt.setText(" ");
            totalAmountInttxt.setText(" ");
        }
        while(itemListC.moveToNext()){
            productList.add(new order_station_product(itemListC.getString(2)));
            QtyList.add(new order_station_product("X"+itemListC.getString(3)));
            priceList.add(new order_station_product("P"+itemListC.getString(4)));

        }
        totalQtyOrdered();
        setAdapter();



    }

    private void checkIfItemExist(){
        String strQty;
        String strPrice;
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl where ItemCode ='"+UniversalItemCode+"'" , null);
        if (itemListC.getCount()!=0){
           // Toast.makeText(this, "DUPLICATE " + UniversalItemCode, Toast.LENGTH_SHORT).show();
          while (itemListC.moveToNext()){
              strQty = itemListC.getString(3);
              strPrice = itemListC.getString(4);

              int finalQty = Integer.parseInt(strQty) + 1;
              int finalPrice = Integer.parseInt(strPrice) + Integer.parseInt(UniversalUnitSellPrice) ;
              Toast.makeText(this, String.valueOf(finalQty), Toast.LENGTH_SHORT).show();
             String strsql ="UPDATE OrderTakeTBl SET ItemQty=" + finalQty + ", UnitSellPrice ="+finalPrice+"  where ItemCode='"+UniversalItemCode+"'";
             db2.execSQL(strsql);
             selectAllDb();
              setViewRecyclerViewLastItem();
          }
        }
        else{
            insertItemOrdered();
        }


    }

    private void subtractItem(){


        String strQty;
        String strPrice;
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl where ItemCode ='"+UniversalItemCode+"'" , null);
        if (itemListC.getCount()!=0){
            // Toast.makeText(this, "DUPLICATE " + UniversalItemCode, Toast.LENGTH_SHORT).show();
            while (itemListC.moveToNext()){
                strQty = itemListC.getString(3);
                strPrice = itemListC.getString(4);

                if (Integer.parseInt(strQty)>1){
                    int finalQty = Integer.parseInt(strQty) - 1;
                    int finalPrice = Integer.parseInt(strPrice) - Integer.parseInt(UniversalUnitSellPrice) ;
                    Toast.makeText(this, String.valueOf(finalQty), Toast.LENGTH_SHORT).show();
                    String strsql ="UPDATE OrderTakeTBl SET ItemQty=" + finalQty + ", UnitSellPrice ="+finalPrice+"  where ItemCode='"+UniversalItemCode+"'";
                    db2.execSQL(strsql);
                    selectAllDb();
                }
               else if (Integer.parseInt(strQty)<=1){
                    db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                   // itemListC = db2.rawQuery("Delete * from OrderTakeTBl where ItemCode ='"+UniversalItemCode+"'" , null);
                    String strsql2 ="Delete from OrderTakeTBl where ItemCode ='"+UniversalItemCode+"'" ;
                    db2.execSQL(strsql2);
                    selectAllDb();
                    //db2.execSQL(itemListC);
                }



            }
        }
        else{
           // insertItemOrdered();
        }

    }

    private void totalQtyOrdered(){
        int totalQtyInt=0;
        int totalAmountInt=0;
        String totalQtyString;
        String totalAmountString;
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl" , null);
        if (itemListC.getCount()!=0){
            while (itemListC.moveToNext()){
                totalQtyString = itemListC.getString(3);
                totalQtyInt = totalQtyInt + Integer.parseInt(totalQtyString);
                totalQtyOrderedTxt.setText(String.valueOf(totalQtyInt));


                totalAmountString = itemListC.getString(4);
                totalAmountInt = totalAmountInt + Integer.parseInt(totalAmountString);
                totalAmountInttxt.setText("P " +String.valueOf(totalAmountInt));



            }
        }
    }

    private  void deleteAllItem(){
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        String strsql3 ="Delete from OrderTakeTBl " ;
        db2.execSQL(strsql3);


//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        String strsql4 ="Delete from RemarkReceipt " ;
        db2.execSQL(strsql4);
        selectAllDb();
    }


    private  void deleteFinalReceipt(){
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        String strsql3 ="Delete from FinalReceipt " ;
        db2.execSQL(strsql3);



        selectAllDb();
    }




    private void remarks(){
        readRemarks();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setTitle("REMARKS");
        input.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        input.setSingleLine(false);
       // input.setLines(5);
        input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        input.setText(readRemark);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

           // if (!readRemark.equals(" ")){

            //}
             remarksString = input.getText().toString();
//                db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
                itemListC = db2.rawQuery("select * from RemarkReceipt where refNumber='"+readRefNumber+"'"  , null);
                if (itemListC.getCount()==0){

                //insert
                  //  Toast.makeText(ordering_station_main.this, "INSERT MODE", Toast.LENGTH_SHORT).show();
                    insertRemarks();



                }
                else{
            //update
                    updateRemarks();
                 //   Toast.makeText(ordering_station_main.this, "UPDATE MODE", Toast.LENGTH_SHORT).show();
                }



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
    private void insertRemarks(){
        myDb = new DatabaseHandler(this);
        boolean isInserted = myDb.insertRemarks(readRefNumber,remarksString
        );
        // Toast.makeText(this, UniversalRecptDesc, Toast.LENGTH_SHORT).show();
        if (isInserted = true){
            //  Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();




        }
        else{
           // Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }



    }
    private void updateRemarks(){
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
       // itemListC = db2.rawQuery("select * from RemarkReceipt where refNumber ='"+readRefNumber+"'" , null);
        String strsql ="UPDATE RemarkReceipt SET remCol='" + remarksString + "'  where refNumber='"+readRefNumber+"'";
        db2.execSQL(strsql);
    }
    private void readRemarks(){
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from RemarkReceipt where refNumber ='"+readRefNumber+"'", null);
        if (itemListC.getCount()==0){
            readRemark="";
            Toast.makeText(this, "NO REMARKS", Toast.LENGTH_SHORT).show();

        }
        else{
            while (itemListC.moveToNext()){
                readRemark = itemListC.getString(1);
            }
          //  Toast.makeText(this, readRemark, Toast.LENGTH_SHORT).show();
        }

    }

    private void readReferenceNumber(){

        //primary key 00000001

       // int readPK;

        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from FinalReceipt ", null);
        if (itemListC.getCount() == 0){

            origRefNumber = 1;
            String formatted = String.format("%07d", origRefNumber);
            readRefNumber = formatted;
        }
        else{

            itemListC = db2.rawQuery("SELECT * FROM FinalReceipt", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
                origRefNumber = 1;
               // readRefNumber=String.valueOf(Integer.parseInt(itemListC.getString(0) + origRefNumber));
                int readPK = Integer.parseInt(itemListC.getString(0));
         //  Toast.makeText(this, readPK, Toast.LENGTH_SHORT).show();
              //  Toast.makeText(this, readPK, Toast.LENGTH_SHORT).show();
               // String formatted = String.format("%07d", readPK);
           // St readPKint = Integer.parseInt(readPK);
            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%07d", incrementPK);
            //Toast.makeText(this, incrementPKString, Toast.LENGTH_SHORT).show();
               // readRefNumber=String.format("%07d",Integer.parseInt(readPKint + origRefNumber));
            readRefNumber =incrementPKString;
          //  Toast.makeText(this, String.format("%07d",incrementPKString), Toast.LENGTH_SHORT).show();



           // }
        }


    }

    private void insertFinalReceipt(){
        myDb = new DatabaseHandler(this);
        boolean isInserted = myDb.insertFinalReceipt(readRefNumber,tableNumber,totalAmountInttxt.getText().toString(),
                totalQtyOrderedTxt.getText().toString(),currentTime,currentDate,usertxt.getText().toString() ,remarksString
                );
        // Toast.makeText(this, UniversalRecptDesc, Toast.LENGTH_SHORT).show();
        if (isInserted = true){
              //Toast.makeText(this, "INSERTED", Toast.LENGTH_SHORT).show();

            checkIfOrderIsEmpty();



        }
        else{

            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfOrderIsEmpty(){
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl" , null);
        if (itemListC.getCount()!=0){


            finish();
            //copy remarks
            createTextfile();
            // create pdf
            //clear item
            deleteAllItem();
            //confirmation if yes print
        }
        else {
            Toast.makeText(this, "ORDER IS EMPTY", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAllItemInConfirmation(){
        readRemarks();
//        db2 = getApplicationContext().openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from OrderTakeTBl" , null);
        StringBuffer buffer= new StringBuffer();
        if (itemListC.getCount()!=0){
            int cnter=1;
            while (itemListC.moveToNext()){

                String counter = (String.valueOf(cnter++ +".     ")).toString().substring(0,4);
                String orderItem = (itemListC.getString(2));
                String orderQty = itemListC.getString(3) ;


                buffer.append(counter+ orderItem +"\n" + "     " +  "x"+orderQty +  "\n\n");


            }
            buffer.append("REMARKS: " + readRemark);
            showMessage("SUMMARY OF ORDER",buffer.toString());
        }
        else {
            Toast.makeText(this, "ORDER IS EMPTY", Toast.LENGTH_SHORT).show();

        }

    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.setPositiveButton("CONFIRM ORDER", new DialogInterface.OnClickListener() {


            @Override

            public void onClick(DialogInterface dialog, int which) {

              //  remarksString = input.getText().toString();
                // Toast.makeText(ordering_station_main.this, m_Text, Toast.LENGTH_SHORT).show();
                insertFinalReceipt();
                finish();
                deleteAllItem();

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
    private void createTextfile(){
        try {
            readRemarks();
            File sendFile = new File(Environment.getExternalStorageDirectory() + "/ANDROID_POS/SEND FILE");

            Cursor cursor = db2.rawQuery("SELECT * FROM OrderTakeTBl", null);
           buffer = new StringBuffer();
            File file = new File(sendFile, readRefNumber +".txt");
            FileOutputStream stream = new FileOutputStream(file);
                     buffer.append("REFERENCE: " + readRefNumber  + "\n");
                     buffer.append("DATE: " + currentDate + " " + currentTime + "\n");
            try {


                while (cursor.moveToNext()) {
                    buffer.append((cursor.getString(2) + "      X" + cursor.getString(3) + "\r\n"));
                }
                buffer.append("==================================="+"\n");
                buffer.append("REMARKS: " + readRemark+"\n");
                stream.write(buffer.toString().getBytes());
            }
            finally {
                stream.close();
            }
        }
        catch (Exception e){


        }
    }













}