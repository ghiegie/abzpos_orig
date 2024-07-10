package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.POSAndroidDB;
import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyCodeManager;
import com.abztrakinc.ABZPOS.TCSKeyboard.KeyboardDevice;
import com.abztrakinc.ABZPOS.keyboardSettings;
import com.bumptech.glide.disklrucache.DiskLruCache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class admin_manage_product extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE =23 ;
    String DB_NAME = "POSAndroidDB.db";
    //String DB_NAME = "V6BO.db";
    SQLiteDatabase db,db2;
    Cursor c;
    LinearLayout linearLayout5,linearlayoutid4;
    ArrayList<String> itemName; // name
    ArrayList<String> itemCode; // id
    ArrayList<String> recptDesc;// desc
    ArrayList<String> itemPrice;//price
    ArrayList<String> itemQuantity;//qty


    ArrayList<String> fastMoving;//price
    ArrayList<String> fastMovingButton;//qty
    ArrayList<String> itemCode2;//price
    ArrayList<String> itemBarcode;//qty
    ArrayList<String> VATIndicatorList;//qty




    ArrayList<String> CategoryNameListID;//id of category
    ArrayList<String> CategoryNameListName;//name of category
    ArrayList<String> CategoryNameList;


    ArrayList<String> ProductListID;//id of cproduct
    ArrayList<String> ProductListSubCategoryID;//id of cproduct
    ArrayList<String> ProductListSubCategoryName;//id of cproduct
    ArrayList<String> ProductListCategoryID;//id of cproduct


    //for showing category/subcategory item
    ArrayList<String> CategoryArrayListID;//id of category
    ArrayList<String> CategoryArrayListName;//id of category
    ArrayList<String> SubCategoryArrayListID;//name of category
    ArrayList<String> SubCategoryArrayListName;//name of category



    RecyclerView rv_productList;
    RecyclerView rv_categoryContent;
    ImageButton btn_add;

    String FinalItemCode;

    String strItemName;
    String strDescription;
    String strItemID;
    String strPrice;
    String strQty;
    String strCode;
    String strBarcode;
    String strShortKeys;
    String strItemSafeStock;
    String strItemPrice2;
    String strItemPrice3;
    String strItemPrice4;
    String strItemPrice5;
    String VATIndicator;
    String PriceOverride;
    int VATIndicatorID;



   // ImageView image;
   ImageView imageView1;
   ImageButton ib_return;
   TextView tv_itemD01,tv_itemD02,tv_itemD03,tv_itemD04,tv_itemD05,tv_itemD06,tv_itemD07,tv_itemD08,tv_itemD09,tv_itemD10,tv_itemD11,tv_itemD12,tv_itemD13,tv_itemD14;



    String categoryIDNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_product2);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        POSAndroidDB posAndroidDB = new POSAndroidDB(this);


        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        db2 = getApplicationContext().openOrCreateDatabase("POSAndroidDB.db", Context.MODE_PRIVATE, null);
        itemName  = new ArrayList<String>();
        itemCode  = new ArrayList<String>();
        recptDesc = new ArrayList<String>();
        itemPrice = new ArrayList<String>();
        itemQuantity = new ArrayList<String>();
        fastMoving = new ArrayList<String>();
        fastMovingButton = new ArrayList<String>();
        itemCode2 = new ArrayList<String>();
        itemBarcode = new ArrayList<String>();
        VATIndicatorList=new ArrayList<String>();





        CategoryNameListName = new ArrayList<String>();
        CategoryNameListID = new ArrayList<String>();
        CategoryNameList = new ArrayList<String>();


        ProductListID = new ArrayList<String>();
        ProductListSubCategoryID = new ArrayList<String>();
        ProductListSubCategoryName = new ArrayList<String>();
        ProductListCategoryID = new ArrayList<String>();

        CategoryArrayListID = new ArrayList<String>();
        CategoryArrayListName = new ArrayList<String>();
        SubCategoryArrayListID = new ArrayList<String>();
        SubCategoryArrayListName = new ArrayList<String>();

       // showDepartmentSettings();


        btn_add =findViewById(R.id.img_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_add_product_item, null);
                builder.setView(alertLayout);
                AlertDialog alertDialog = builder.create();

                Button btn_Category = alertLayout.findViewById(R.id.btn_Category);
                btn_Category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                loadCategory();
                    }
                });

                Button btn_SubCategory = alertLayout.findViewById(R.id.btn_SubCategory);
                btn_SubCategory.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                loadSubCategory();
                    }
                });

                Button btn_Item = alertLayout.findViewById(R.id.btn_Item);
                btn_Item.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                      loadProduct();
                    }
                });




                alertDialog.show();



            }
        });

        ib_return=findViewById(R.id.ib_return);
        ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


//        showItem();
//        refreshRecycleview();

      //  loadSubCategory();

      //  createList();

        showItemCategory();
        refreshRecycleviewCategory();
        KeyBoardMap();


     //  showDepartmentSettings();



    }





    KeyboardDevice kboard;
    KeyCodeManager kManager;
    private void KeyBoardMap(){
        kboard=new KeyboardDevice();
        kManager=new KeyCodeManager();
        kboard.Init();
        kboard.BeepOnOff(false);
        kManager.InitKeyMap();
        kboard.mHandler=this.MyHandler;

    }
    @Override
    protected void onResume() {
        super.onResume();
       // showItemCategory();
        refreshRecycleviewCategory();
        KeyBoardMap();
    }

    int x=-1;

    private void SimulateKeyboard(int keyCode) {

//        kManager = new KeyCodeManager();
//        Log.d("TAG", "SimulateKeyboard: "+kManager.getDefaultKeyName(keyCode));
//        String input = kManager.getDefaultKeyName(keyCode);
//        Log.d("TAG", "input: "+input);
//        int digitType=1; //1 number //2 letter
//        final int PRESS_INTERVAL =  700;
//
//
//        if (input.equalsIgnoreCase("Exit")){
//
//            //triggerRebirth();
//            onBackPressed();
//        }


        kManager = new KeyCodeManager();
        String input = kManager.getDefaultKeyName(keyCode);
        //  Toast.makeText(context1, input, Toast.LENGTH_SHORT).show();

        switch(input){
            case "EC":
             //   ECFunction();
                break;


            case "Total":

                totalFunction();


                break;

            case "Exit":
                //  pushCloseKeyboard();
                //  closeKeyboard();

                if(alertDialogDeptSetting!=null && alertDialogDeptSetting.isShowing()){
                    alertDialogDeptSetting.dismiss();
                }
                else{
                    onBackPressed();
                }



                Log.d("TAG", "SimulateKeyboard: Exit");
                break;

            case "Page UP":
                functionPageUPCategory();
              //  navigateRight();
                break;

            case "Page DN":
               // navigateLeft();
                 functionPageDNCategory();
                break;

            case "D01":
             //   D01Function();
                break;

            case "D05":
                //insertNewItem();
               // D05Function();
                break;

            case "D09":
               // D09Function();
                //deleteItem();
                break;

            case "D13":
               // D13Function();
                // printTest();
                break;


            case "RA":
               // handleRadioButtonCheckedChange(0);
                break;

            case "PO":
               // handleRadioButtonCheckedChange(1);
                break;

            case "Sub Total":
                break;


        }










    }

    private void totalFunction(){


//        if (alertDialog != null && alertDialog.isShowing()) {
//            // Do nothing or add custom logic as needed
//            // functionTab();
//
//            saveButton(saveBtn);
//            alertDialog.dismiss();
//
//
//        } else {
            // If no AlertDialog is showing, proceed with the default behavior
            RecyclerviewAdapter2 myAdapter = (RecyclerviewAdapter2) mAdapterCategory;
            myAdapter.openUpdateDialog(x);
          //  Log.d("SELECTED ID",headerFooterID.get(x).toString());
//        }


    }

    private void functionPageUPCategory(){
        if (x > 0) {
            x = x - 1; // Decrease the selected position
            RecyclerviewAdapter2 myAdapter = (RecyclerviewAdapter2) mAdapterCategory;
            myAdapter.setSelectedPosition(x);
            Log.d("TAG", "PAGE UP" + x);
            rv_categoryContent.smoothScrollToPosition(x);
           // Log.d("SELECTED ID",headerFooterID.get(x).toString());
        }
    }
    private void functionPageDNCategory(){
        RecyclerviewAdapter2 myAdapter = (RecyclerviewAdapter2) mAdapterCategory;


        if (x < myAdapter.getItemCount() - 1) {
            x = x + 1;
            myAdapter.setSelectedPosition(x);
            Log.d("TAG", "PAGE DOWN" + x);
            rv_categoryContent.smoothScrollToPosition(x);


        }
    }


    private void functionPageUPProduct(){
        if (x > 0) {
            x = x - 1; // Decrease the selected position
            RecyclerviewAdapter myAdapter = (RecyclerviewAdapter) mAdapterCategory;
            myAdapter.setSelectedPosition(x);
            Log.d("TAG", "PAGE UP" + x);
            rv_categoryContent.smoothScrollToPosition(x);
            // Log.d("SELECTED ID",headerFooterID.get(x).toString());
        }
    }
    private void functionPageDNProduct(){
        RecyclerviewAdapter myAdapter = (RecyclerviewAdapter) mAdapterCategory;


        if (x < myAdapter.getItemCount() - 1) {
            x = x + 1;
            myAdapter.setSelectedPosition(x);
            Log.d("TAG", "PAGE DOWN" + x);
            rv_categoryContent.smoothScrollToPosition(x);


        }
    }



    public static int StringToInt(String strParse) {
        int iValue = 0;
        try {
            if (strParse.length() > 0) {
                iValue = Integer.parseInt(strParse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iValue;
    }
    private final Handler MyHandler = new Handler(Looper.getMainLooper()) {
        @SuppressLint({"StringFormatMatches", "DefaultLocale"})
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case KeyboardDevice.MSG_KEY: {
                    String strMsgInfo = "";
                    try {
                        strMsgInfo = msg.getData().getString("msg_info");
                        String[] keyData = strMsgInfo.split(":", 2);
                        if (keyData != null) {

                            int nKeyIndex = StringToInt(keyData[0]);
                            if (nKeyIndex == 0) {

                            } else {
                                String KeyIndex = String.format("%d", nKeyIndex);
                                KeyCodeManager kManager = new KeyCodeManager();




                                // editTextTextPersonName.setText(kManager.getDefaultKeyName(StringToInt(KeyIndex)));
                                SimulateKeyboard(nKeyIndex);
                                // Log.e("RouteKeyIndex",String.valueOf(nKeyIndex));

                            }
                            String keyName=KeyCodeManager.getDefaultKeyName(nKeyIndex);
                            String strShow = String.format("KeyIndex:%d", nKeyIndex) + " ScanCode:" + keyData[1]+" KeyName:"+keyName;
                            // ShowMsg(strShow);
                        } else {
                            //Toast.makeText(cashier_invoice.this, strMsgInfo, Toast.LENGTH_SHORT).show();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        keyboardSettings keyboardSettings = new keyboardSettings();
        switch (item.getItemId()){

            case R.id.item1:

//                String strsql = "Delete from ShortCutKeysMap";
//                db2.execSQL(strsql);
//                Toast.makeText(this, "Shortcut keys Reset", Toast.LENGTH_LONG).show();
//                //showItem();
//                refreshRecycleview();
                showDepartmentSettings();




                return true;


            case R.id.item2:

              //  resetDeptKey();
                String strsql = "Delete from ShortCutKeysMap";
                db2.execSQL(strsql);
                Toast.makeText(this, "Department keys Reset", Toast.LENGTH_LONG).show();

                return true;


            case R.id.sub3sub1Item1:
              //add category
                loadCategory();
                return true;


            case R.id.sub3sub1Item2:
                //delete category
             //   keyboardSettings.getWindow();
               loadCategoryDelete();

//                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);




                return true;

            case R.id.sub3item2:
                loadSubCategory();
                return true;

            case R.id.sub3sub3Item1:
                loadProduct();
                return true;


            case R.id.item4:

                startActivity(new Intent(this, FileBrowseActivity.class));





            case R.id.importDB:

                Toast.makeText(this, "IMPORT ITEM DATABASE", Toast.LENGTH_SHORT).show();

               // String importFilePath = "/storage/emulated/0/installer/POSAndroidDB.db"; // Example import file path
                DatabaseExporter.importDatabase(this.getApplicationContext());


            case R.id.exportDB:

                Toast.makeText(this, "EXPORT ITEM DATABASE", Toast.LENGTH_SHORT).show();
                DatabaseExporter.exportDatabase(this.getApplicationContext());


            default:

        }
        return super.onOptionsItemSelected(item);
    }




    String Message;
    Bitmap bmp;
    private void createList(){

        int totalBtn = itemName.size();
        linearLayout5 = findViewById(R.id.linearlayoutid5);
        linearlayoutid4= findViewById(R.id.linearlayoutid4);
        linearLayout5.removeAllViews();

      for (int i=0;i<totalBtn;i++){

            CardView cardView = new CardView(this);
          //  row2.setOrientation(LinearLayout.VERTICAL);
           // cardView.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            cardView.setLayoutParams(layoutparams);
            //cardView.setRadius(0);
            cardView.setPadding(25, 25, 25, 25);
            //cardView.setCardBackgroundColor(Color.MAGENTA);
            cardView.setMaxCardElevation(30);
            cardView.setMaxCardElevation(6);

            for (int j=0;j<2;j++){
                LinearLayout rootViewI = new LinearLayout(this);

                rootViewI.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rootViewI.setOrientation(LinearLayout.VERTICAL);

                LinearLayout rootViewS = new LinearLayout(this);
                rootViewS.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rootViewS.setOrientation(LinearLayout.VERTICAL);


                LinearLayout rootViewPic = new LinearLayout(this);
                rootViewPic.setOrientation(LinearLayout.VERTICAL);
                rootViewPic.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));






                for (int k=0;k<1;k++) {


                 //   cardView.removeView(rootView);


                    LinearLayout rootView2 = new LinearLayout(this);

                    rootView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    rootView2.setOrientation(LinearLayout.HORIZONTAL);


                    final ImageButton imageView = new ImageButton(this);

                    imageView.setId(i);

                    c = db.rawQuery("select * from ITEM where itemID='"+itemCode.get(i)+"'", null);
                    if (c.getCount()==0){
                        Message="NO IMAGE";
                    }

                    while (c.moveToNext()){
                      try {
                          int width = imageView.getMeasuredWidth();
                          int height = imageView.getMeasuredHeight();
                          Bitmap bmImg = BitmapFactory.decodeFile(c.getString(5));
                          Bitmap resized = Bitmap.createScaledBitmap(bmImg,150 , 150, false);

                           LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                          layoutParams.setMargins(0, 10, 10, 10);
                          imageView.setBackgroundColor(R.drawable.pos_logo);
                           imageView.setLayoutParams(layoutParams);

                          imageView.setImageBitmap(resized);
                      }
                      catch (Exception e){
                          imageView.setBackgroundResource(R.mipmap.ic_launcher_round);
                      }
                    }










                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          //
                            FinalItemCode=itemCode.get(imageView.getId());

                            //pickImage();

                        }
                    });

                    //rootViewPic.addView(imageView);
                    rootView2.addView(imageView);





                        final TextView textViewlabel = new TextView(this);
                        textViewlabel.setText("ITEM NAME :  ");
                        textViewlabel.setEnabled(false);
                         rootViewI.addView(textViewlabel);
                       // rootView.addView(rootView2);


                        final TextView textView2Label = new TextView(this);
                        textView2Label.setText("DESCRIPTION: ");
                        textView2Label.setEnabled(false);
                         rootViewI.addView(textView2Label);


                        final TextView textView3Label = new TextView(this);
                        textView3Label.setText("ITEM ID : ");
                        textView3Label.setEnabled(false);
                    rootViewI.addView(textView3Label);

                        final TextView textView4Label = new TextView(this);
                        textView4Label.setText("ITEM PRICE :  ");
                        textView4Label.setEnabled(false);
                    rootViewI.addView(textView4Label);


                        final TextView textView5Label = new TextView(this);
                        textView5Label.setText("ITEM QTY  :");
                        textView5Label.setEnabled(false);
                        rootViewI.addView(textView5Label);

                    final TextView textView6Label = new TextView(this);
                    textView6Label.setText("Image PATH  :");
                    textView6Label.setEnabled(false);
                    rootViewI.addView(textView6Label);


                        rootView2.addView(rootViewI);





                  //  rootView2.removeView(rootViewI);
                        final TextView textView = new TextView(this);
                        textView.setText(itemName.get(i));
                        textView.setEnabled(false);
                        rootViewS.addView(textView);
                   // rootViewS.addView(rootViewS);


                        final TextView textView2 = new TextView(this);
                        textView2.setText(recptDesc.get(i));
                        textView2.setEnabled(false);
                    rootViewS.addView(textView2);




                        final TextView textView3 = new TextView(this);
                        textView3.setText(itemCode.get(i));
                        textView3.setEnabled(false);
                    rootViewS.addView(textView3);




                        final TextView textView4 = new TextView(this);
                        textView4.setText("P "+itemPrice.get(i));
                        textView4.setEnabled(false);
                    rootViewS.addView(textView4);



                        final TextView textView5 = new TextView(this);
                        textView5.setText(itemQuantity.get(i));
                        textView5.setEnabled(false);
                    rootViewS.addView(textView5);


                    c = db.rawQuery("select * from ITEM where itemID='"+itemCode.get(i)+"'", null);
                    if (c.getCount()==0){
                        Message="NO IMAGE";
                    }
                    while (c.moveToNext()){
                        Message=c.getString(5);
                    }

                    final TextView textView6 = new TextView(this);
                    textView6.setText(Message);
                    textView6.setEnabled(false);
                    rootViewS.addView(textView6);


                    rootView2.addView(rootViewS);












                    cardView.addView(rootView2);

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FinalItemCode=itemCode.get(imageView.getId());
                           // pickImage();
                        }
                    });



                }


               // cardView.setOnClickListener();








            }
           // rootView.addView(cardView);
          //  linearlayoutid4.addView(rootView);
            linearLayout5.addView(cardView);

        }




    }
    String shortcutKeys;
    String fastMovingInd;
    private void showItem(String subcatgid){
        itemCode.clear();
        itemName.clear();
        recptDesc.clear();
        itemPrice.clear();
        itemQuantity.clear();
        fastMoving.clear();
        fastMovingButton.clear();
        itemCode.clear();
        itemBarcode.clear();
        ProductList.clear();
        VATIndicatorList.clear();

        c = db.rawQuery("select * from ITEM where SubCatgID='"+subcatgid+"'", null);



        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }


        while(c.moveToNext()){
            itemCode.add(c.getString(0));
            itemName.add(c.getString(1));
            recptDesc.add(c.getString(2));
            itemPrice.add(c.getString(3));
            itemQuantity.add(c.getString(4));
            itemCode.add(c.getString(8));
            itemBarcode.add(c.getString(9));
            VATIndicatorList.add(c.getString(15));

            Cursor c2=db.rawQuery("select * from ShortCutKeysMap where shortCutID='"+c.getString(0)+"'", null);
            if (c2.getCount()!=0) {
                while (c2.moveToNext()){
                    shortcutKeys = c2.getString(0);
                    fastMovingInd="YES";
                }
            }
            else{
                shortcutKeys="NONE";
                fastMovingInd="NO";
            }



            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();

            admin_manage_product_item p0=new admin_manage_product_item(
                    c.getString(1),
                    c.getString(2),
                    c.getString(0),
                    c.getString(3),
                    c.getString(4),
                    "NO",
                    shortcutKeys,
                    c.getString(8),
                    c.getString(9),
                    c.getString(15),
                    c.getString(16));



            ProductList.addAll(Arrays.asList(new admin_manage_product_item[]{p0}));
        }
    }

    ArrayList<String> list;
    ArrayList<String> list2;
   // ArrayList<String> list3;



    private void getCategory(){
       // POSAndroidDB posAndroidDB = new POSAndroidDB(this);

        list = new ArrayList<>();
        list2= new ArrayList<>();
        list.clear();
       Cursor cursor = db.rawQuery("select * from SUBCATEGORY", null);
       if (cursor.getCount()!=0){
           while (cursor.moveToNext()){
               list.add(cursor.getString(1));
               list2.add(cursor.getString(0));
           }
       }

       cursor.close();

    }
    private void getItem(){
        // POSAndroidDB posAndroidDB = new POSAndroidDB(this);

       list = new ArrayList<>();
       list2= new ArrayList<>();

       list.clear();
       list2.clear();


        Cursor cursor = db.rawQuery("select * from ITEM", null);
        if (cursor.getCount()!=0){
            while (cursor.moveToNext()){
                list.add(cursor.getString(1));
                list2.add(cursor.getString(0));
            }
        }

        cursor.close();

    }

//    @Override
//    public void onBackPressed() {
//        // Check if the AlertDialog is showing and cursor is equal to 1
//        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
//        if ( cursor == 1) {
//            // Do nothing, to prevent the AlertDialog from closing
//            return;
//        }
//
//        // For other cases, allow the default onBackPressed behavior
//        super.onBackPressed();
//    }




    Spinner sp_layer;
    int cursor=0;//cursor =1 showDepartment control
    String selectedValue;
    int layer=0;
    AlertDialog alertDialogDeptSetting;
    View alertLayoutDept;
    private void showDepartmentSettings(){


        cursor=1;
        AlertDialog.Builder builder  = new AlertDialog.Builder(this,android.R.style.Theme_Light);
        LayoutInflater inflater = getLayoutInflater();
         alertLayoutDept = inflater.inflate(R.layout.custom_alertdialog_admin_manage_product_department, null);
        builder.setView(alertLayoutDept);
        alertDialogDeptSetting = builder.create();
        Button button_frameD01;

        ArrayList<String> layers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            layers.add(String.valueOf(i));
        }

        sp_layer = alertLayoutDept.findViewById(R.id.sp_layer);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, layers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_layer.setAdapter(adapter);
        sp_layer.setSelection(layer-1);

        sp_layer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected value from the spinner
               selectedValue  = (String) parent.getItemAtPosition(position);
               layer = Integer.parseInt(selectedValue);


              // showDepartmentSettings();
                loadAllButton(alertLayoutDept,layer);
                // Do something with the selected value
                Log.d("layer", String.valueOf(layer)); // Example: Log the selected value
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        alertDialogDeptSetting.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // Check if the back button is pressed and cursor is equal to 1
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP && cursor == 1) {
                    // Do nothing, to prevent the AlertDialog from closing
                    return true; // Consume the back button press
                }
                return false; // Allow the default behavior for other key events
            }
        });













        button_frameD01=alertLayoutDept.findViewById(R.id.button_frameD01);
        button_frameD01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(admin_manage_product.this, "D01", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D01",layer);
              //  showAlertDialog(alertLayout.getContext(),"D01");
            }
        });
        Button button_frameD02 = alertLayoutDept.findViewById(R.id.button_frameD02);
        button_frameD02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D02");
                Toast.makeText(admin_manage_product.this, "D02", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D02",layer);
            }
        });

        Button button_frameD03 = alertLayoutDept.findViewById(R.id.button_frameD03);
        button_frameD03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D03");
                Toast.makeText(admin_manage_product.this, "D03", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D03",layer);
            }
        });


        Button button_frameD04 = alertLayoutDept.findViewById(R.id.button_frameD04);
        button_frameD04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D04");
                Toast.makeText(admin_manage_product.this, "D04", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D04",layer);
            }
        });

        Button button_frameD05 = alertLayoutDept.findViewById(R.id.button_frameD05);
        button_frameD05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showAlertDialog(alertLayout.getContext(),"D05");
                Toast.makeText(admin_manage_product.this, "D05", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D05",layer);
            }
        });

        Button button_frameD06 = alertLayoutDept.findViewById(R.id.button_frameD06);
        button_frameD06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showAlertDialog(alertLayout.getContext(),"D06");
                Toast.makeText(admin_manage_product.this, "D06", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D06",layer);
            }
        });

        Button button_frameD07 = alertLayoutDept.findViewById(R.id.button_frameD07);
        button_frameD07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D07");
                Toast.makeText(admin_manage_product.this, "D07", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D07",layer);
            }
        });


        Button button_frameD08 = alertLayoutDept.findViewById(R.id.button_frameD08);
        button_frameD08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showAlertDialog(alertLayout.getContext(),"D08");
                Toast.makeText(admin_manage_product.this, "D08", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D08",layer);
            }
        });

        Button button_frameD09 = alertLayoutDept.findViewById(R.id.button_frameD09);
        button_frameD09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  showAlertDialog(alertLayout.getContext(),"D09");
                Toast.makeText(admin_manage_product.this, "D09", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D09",layer);
            }
        });


        Button button_frameD10 = alertLayoutDept.findViewById(R.id.button_frameD10);
        button_frameD10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D10");
                Toast.makeText(admin_manage_product.this, "D10", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D10",layer);
            }
        });

        Button button_frameD11= alertLayoutDept.findViewById(R.id.button_frameD11);
        button_frameD11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D11");
                Toast.makeText(admin_manage_product.this, "D11", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D11",layer);
            }
        });

        Button button_frameD12 = alertLayoutDept.findViewById(R.id.button_frameD12);
        button_frameD12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D12");
                Toast.makeText(admin_manage_product.this, "D12", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D12",layer);
            }
        });

        Button button_frameD13 = alertLayoutDept.findViewById(R.id.button_frameD13);
        button_frameD13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // showAlertDialog(alertLayout.getContext(),"D13");
                Toast.makeText(admin_manage_product.this, "D13", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D13",layer);
            }
        });

        Button button_frameD14 = alertLayoutDept.findViewById(R.id.button_frameD14);
        button_frameD14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showAlertDialog(alertLayout.getContext(),"D14");
                Toast.makeText(admin_manage_product.this, "D14", Toast.LENGTH_SHORT).show();
                showAlertDialogSelection(alertLayoutDept.getContext(),"D14",layer);
            }
        });












        ImageButton imgb_exit = alertLayoutDept.findViewById(R.id.imgb_exit);
        imgb_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogDeptSetting.dismiss();
               // onBackPressed();
                cursor=0;
            }
        });








        alertDialogDeptSetting.show();



        getCategory();
        getItem();


        RecyclerView recyclerView = alertLayoutDept.findViewById(R.id.rv_categoryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(alertLayoutDept.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        MenuRecAdapter menuRecAdapter = new MenuRecAdapter(list);
        recyclerView.setAdapter(menuRecAdapter);






    }
    String subCatgID="";
    private void showAlertDialogDept(Context context,String dept,int pagelayer,AlertDialog alertDialogLast){
        getCategory();
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_admin_manage_category_selection, null);

        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();

    //    alertDialog.setCanceledOnTouchOutside(false);
//
        Button btn_insertDept = alertLayout.findViewById(R.id.btn_insertDept);
        Button btn_Cancel = alertLayout.findViewById(R.id.btn_Cancel);
        TextView tv_selectionDeptLabel = alertLayout.findViewById(R.id.tv_selectionDeptLabel);
        tv_selectionDeptLabel.setText(dept);

        Spinner TransactionSpinner = alertLayout.findViewById(R.id.TransactionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TransactionSpinner.setAdapter(adapter);
        TransactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              //  TransReprint = TransactionSpinner.getSelectedItem().toString();
                subCatgID = list2.get(TransactionSpinner.getSelectedItemPosition());



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_insertDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert to

                Log.d("TAG", "onClick: "+dept + "   " + subCatgID);
                    updateDept(dept,alertLayout,pagelayer);
                    alertDialog.dismiss();
                    alertDialogLast.dismiss();





            }
        });




        alertDialog.show();

    }
    private void updateDept(String dept,View view,int pagelayer){
        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+dept+"' and pageLayer='"+pagelayer+"'", null);
        if (cursor.getCount()!=0){
            Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            values.put("shortCutID", subCatgID); // Assuming subCatgID is the column you want to update
            int rowsAffected = db.update("ShortCutKeysMap", values, "shortCutKeys = ?", new String[]{dept});
            if (rowsAffected > 0) {
                Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
                loadAllButton(alertLayoutDept,layer);
            } else {
                Toast.makeText(this, "Failed to update " + dept, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            POSAndroidDB posAndroidDB = new POSAndroidDB(view.getContext());
            posAndroidDB.insertShortcutKeys(dept,subCatgID,pagelayer);
           // alertDialog.dismiss();
            loadAllButton(alertLayoutDept,layer);
        }
        cursor.close();
    }
    private void showAlertDialogItem(Context context,String dept,int pagelayer,AlertDialog alertDialogLast){
        getItem();
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_admin_manage_category_selection, null);

        builder.setView(alertLayout);
        AlertDialog alertDialog = builder.create();

        //    alertDialog.setCanceledOnTouchOutside(false);
//
        Button btn_insertDept = alertLayout.findViewById(R.id.btn_insertDept);
        Button btn_Cancel = alertLayout.findViewById(R.id.btn_Cancel);
        TextView tv_selectionDeptLabel = alertLayout.findViewById(R.id.tv_selectionDeptLabel);
        tv_selectionDeptLabel.setText(dept);

        Spinner TransactionSpinner = alertLayout.findViewById(R.id.TransactionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TransactionSpinner.setAdapter(adapter);
        TransactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  TransReprint = TransactionSpinner.getSelectedItem().toString();
               // subCatgID = list3.get(TransactionSpinner.getSelectedItemPosition());
                subCatgID = list2.get(TransactionSpinner.getSelectedItemPosition());



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn_insertDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert to

                //Log.d("TAG", "onClick: "+dept + "   " + subCatgID);


                //POSAndroidDB posAndroidDB = new POSAndroidDB(view.getContext());
               // posAndroidDB.insertShortcutKeys(dept,subCatgID);
             //   alertDialog.dismiss();

                Log.d("TAG", "onClick: "+dept + "   " + subCatgID);
                updateItem(dept,alertLayout,pagelayer);
                alertDialog.dismiss();
                alertDialogLast.dismiss();

            }
        });




        alertDialog.show();

    }

    private void updateItem(String dept,View view,int pagelayer){
        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+dept+"'and pageLayer='"+pagelayer+"'", null);
        if (cursor.getCount()!=0){
            Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            values.put("shortCutID", subCatgID); // Assuming subCatgID is the column you want to update
            int rowsAffected = db.update("ShortCutKeysMap", values, "shortCutKeys = ?", new String[]{dept});
            if (rowsAffected > 0) {
                Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
                loadAllButton(alertLayoutDept,layer);
            } else {
                Toast.makeText(this, "Failed to update " + dept, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            POSAndroidDB posAndroidDB = new POSAndroidDB(view.getContext());
            posAndroidDB.insertShortcutKeys(dept,subCatgID,pagelayer);
            // alertDialog.dismiss();
            loadAllButton(alertLayoutDept,layer);
        }
        cursor.close();
    }


    private void allProduct (String dept,View view,int pagelayer){
        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+dept+"' and pageLayer='"+pagelayer+"'", null);
        if (cursor.getCount()!=0){
            Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
            ContentValues values = new ContentValues();
            values.put("shortCutID", "ALL"); // Assuming subCatgID is the column you want to update
            int rowsAffected = db.update("ShortCutKeysMap", values, "shortCutKeys = ?", new String[]{dept});
            if (rowsAffected > 0) {
               // Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
                loadAllButton(alertLayoutDept,layer);
            } else {
                Toast.makeText(this, "Failed to update " + dept, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            POSAndroidDB posAndroidDB = new POSAndroidDB(view.getContext());
            posAndroidDB.insertShortcutKeys(dept,"ALL",pagelayer);
            // alertDialog.dismiss();
            loadAllButton(alertLayoutDept,layer);
        }
        cursor.close();
    }


//    private void deleteButton(String dept,int pagelayer){
//        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+dept+"' and pageLayer='"+pagelayer+"'", null);
//        if (cursor.getCount()!=0){
//           // Toast.makeText(this, "UPDATE " + dept, Toast.LENGTH_SHORT).show();
//            ContentValues values = new ContentValues();
//            values.put("shortCutID", subCatgID); // Assuming subCatgID is the column you want to update
//            int rowsAffected = db.delete("ShortCutKeysMap", "shortCutKeys = ?", new String[]{dept});
//            if (rowsAffected > 0) {
//                Toast.makeText(this, "Button Reset ", Toast.LENGTH_SHORT).show();
//                showDepartmentSettings();
//            } else {
//                Toast.makeText(this, "Failed to delete " , Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        cursor.close();
//    }

    private void deleteButton(String dept, int pagelayer) {
        Cursor cursor = db.rawQuery("SELECT * FROM ShortCutKeysMap WHERE shortCutKeys=? AND pageLayer=?", new String[]{dept, String.valueOf(pagelayer)});
        if (cursor.getCount() != 0) {
            ContentValues values = new ContentValues();
            values.put("shortCutID", subCatgID); // Assuming subCatgID is the column you want to update
            int rowsAffected = db.delete("ShortCutKeysMap", "shortCutKeys = ? AND pageLayer = ?", new String[]{dept, String.valueOf(pagelayer)});
            if (rowsAffected > 0) {
                Toast.makeText(this, "Button Reset ", Toast.LENGTH_SHORT).show();
                loadAllButton(alertLayoutDept,layer);
                alertLayoutDept.invalidate();
            } else {
                Toast.makeText(this, "Failed to delete ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No such button found for page layer " + pagelayer, Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }


    private void showAlertDialogSelection(Context context,String dept,int layer){
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_admin_manage_selection, null);

        builder.setView(alertLayout);
        Button btn_deptSelect = alertLayout.findViewById(R.id.btn_deptSelect);
        Button btn_itemSelect = alertLayout.findViewById(R.id.btn_itemSelect);
        Button btn_itemDelete = alertLayout.findViewById(R.id.btn_itemDelete);
        Button btn_AllProduct = alertLayout.findViewById(R.id.btn_AllProduct);
        AlertDialog alertDialog = builder.create();

        btn_deptSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAlertDialogDept(alertLayout.getContext(),dept,layer,alertDialog);
            }
        });
        btn_itemSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show item

                showAlertDialogItem(alertLayout.getContext(),dept,layer,alertDialog);
            }
        });
        btn_itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alertdialogLast.dismiss();
                deleteButton(dept,layer);
            }
        });

        btn_AllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  deleteButton(dept);
             //   alertdialogLast.dismiss();
                allProduct(dept,view,layer);
            }
        });




        alertDialog.show();

    }

//    private void loadAllButton(View view,int pageLayer){
//        tv_itemD01 = view.findViewById(R.id.tv_itemD01);
//        tv_itemD02 = view.findViewById(R.id.tv_itemD02);
//        tv_itemD03 = view.findViewById(R.id.tv_itemD03);
//        tv_itemD04 = view.findViewById(R.id.tv_itemD04);
//        tv_itemD05 = view.findViewById(R.id.tv_itemD05);
//        tv_itemD06 = view.findViewById(R.id.tv_itemD06);
//        tv_itemD07 = view.findViewById(R.id.tv_itemD07);
//        tv_itemD08 = view.findViewById(R.id.tv_itemD08);
//        tv_itemD09 = view.findViewById(R.id.tv_itemD09);
//        tv_itemD10 = view.findViewById(R.id.tv_itemD10);
//        tv_itemD11 = view.findViewById(R.id.tv_itemD11);
//        tv_itemD12 = view.findViewById(R.id.tv_itemD12);
//        tv_itemD13 = view.findViewById(R.id.tv_itemD13);
//        tv_itemD14 = view.findViewById(R.id.tv_itemD14);
//        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where pageLayer='"+pageLayer+"'", null);
//        if (cursor.getCount()!=0){
//            while (cursor.moveToNext()){
//               // Toast.makeText(this, "BUTTON : " + cursor.getString(0), Toast.LENGTH_SHORT).show();
//                Log.d("TAG", "loadAllButton: " + cursor.getString(0));
//                //list.add(cursor.getString(1));
//                //list2.add(cursor.getString(0));
//
//                switch (cursor.getString(0)) {
//                    case "D01":
//                        //System.out.println("Monday");
//
//                        tv_itemD01.setText(  itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D02":
//                        //System.out.println("Monday");
//                        tv_itemD02.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D03":
//                        //System.out.println("Monday");
//                        tv_itemD03.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D04":
//                        //System.out.println("Monday");
//                        tv_itemD04.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D05":
//                        //System.out.println("Monday");
//                        tv_itemD05.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D06":
//                        //System.out.println("Monday");
//                        tv_itemD06.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D07":
//                        //System.out.println("Monday");
//                        tv_itemD07.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D08":
//                        //System.out.println("Monday");
//                        tv_itemD08.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D09":
//                        //System.out.println("Monday");
//                        tv_itemD09.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D10":
//                        //System.out.println("Monday");
//                        tv_itemD10.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D11":
//                        //System.out.println("Monday");
//                        tv_itemD11.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D12":
//                        //System.out.println("Monday");
//                        tv_itemD12.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//
//                    case "D13":
//                        //System.out.println("Monday");
//                        tv_itemD13.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D14":
//                        //System.out.println("Monday");
//                        tv_itemD14.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//
//                    default:
//                        System.out.println("NO DATA");
//                        break;
//                }
//
//            }
//        }
//        else {
//            while (cursor.moveToNext()){
//                // Toast.makeText(this, "BUTTON : " + cursor.getString(0), Toast.LENGTH_SHORT).show();
//                Log.d("TAG", "loadAllButton: " + cursor.getString(0));
//                //list.add(cursor.getString(1));
//                //list2.add(cursor.getString(0));
//
//                switch (cursor.getString(0)) {
//                    case "D01":
//                        //System.out.println("Monday");
//
//                        tv_itemD01.setText(  itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D02":
//                        //System.out.println("Monday");
//                        tv_itemD02.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D03":
//                        //System.out.println("Monday");
//                        tv_itemD03.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D04":
//                        //System.out.println("Monday");
//                        tv_itemD04.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D05":
//                        //System.out.println("Monday");
//                        tv_itemD05.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D06":
//                        //System.out.println("Monday");
//                        tv_itemD06.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D07":
//                        //System.out.println("Monday");
//                        tv_itemD07.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D08":
//                        //System.out.println("Monday");
//                        tv_itemD08.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D09":
//                        //System.out.println("Monday");
//                        tv_itemD09.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D10":
//                        //System.out.println("Monday");
//                        tv_itemD10.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D11":
//                        //System.out.println("Monday");
//                        tv_itemD11.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D12":
//                        //System.out.println("Monday");
//                        tv_itemD12.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//
//                    case "D13":
//                        //System.out.println("Monday");
//                        tv_itemD13.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//                    case "D14":
//                        //System.out.println("Monday");
//                        tv_itemD14.setText(itemName(cursor.getString(0),pageLayer));
//                        break;
//
//                    default:
//                        System.out.println("NO DATA");
//                        break;
//                }
//
//            }
//        }
//    }
private void loadAllButton(View view, int pageLayer) {
    HashMap<String, TextView> buttonMap = new HashMap<>();
    buttonMap.put("D01", view.findViewById(R.id.tv_itemD01));
    buttonMap.put("D02", view.findViewById(R.id.tv_itemD02));
    buttonMap.put("D03", view.findViewById(R.id.tv_itemD03));
    buttonMap.put("D04", view.findViewById(R.id.tv_itemD04));
    buttonMap.put("D05", view.findViewById(R.id.tv_itemD05));
    buttonMap.put("D06", view.findViewById(R.id.tv_itemD06));
    buttonMap.put("D07", view.findViewById(R.id.tv_itemD07));
    buttonMap.put("D08", view.findViewById(R.id.tv_itemD08));
    buttonMap.put("D09", view.findViewById(R.id.tv_itemD09));
    buttonMap.put("D10", view.findViewById(R.id.tv_itemD10));
    buttonMap.put("D11", view.findViewById(R.id.tv_itemD11));
    buttonMap.put("D12", view.findViewById(R.id.tv_itemD12));
    buttonMap.put("D13", view.findViewById(R.id.tv_itemD13));
    buttonMap.put("D14", view.findViewById(R.id.tv_itemD14));

    // Clear the text of all TextViews
    for (TextView textView : buttonMap.values()) {
        textView.setText("");
    }


    Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where pageLayer='" + pageLayer + "'", null);
    if (cursor.getCount() != 0) {
        while (cursor.moveToNext()) {
            String buttonId = cursor.getString(0);
            TextView textView = buttonMap.get(buttonId);
            if (textView != null) {
                textView.setText(itemName(buttonId, pageLayer));
            }
        }
    } else {
        for (Map.Entry<String, TextView> entry : buttonMap.entrySet()) {
            TextView textView = entry.getValue();
            if (textView != null) {
                textView.setText("");
            }
        }
    }
}

    String finalName;
    private String itemName(String Button,int pageLayer){

        Cursor cursor = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+Button+"' and pageLayer ='"+pageLayer+"'", null);
        if (cursor.getCount()!=0) {
            if (cursor.moveToFirst()) {

                String shortCutId = cursor.getString(1);
                Cursor cursor1 = db.rawQuery("select * from SUBCATEGORY where SubCatgID='" + shortCutId + "'", null);
                if (cursor1.getCount() != 0) {
                    if (cursor1.moveToFirst()) {
                         finalName = ("DEPT : " + cursor1.getString(1));
                    }
                } else {
                    Cursor cursor2 = db.rawQuery("select * from ITEM where ItemID='" + shortCutId + "'", null);
                    if (cursor2.getCount() != 0) {
                        if (cursor2.moveToFirst()) {
                             finalName = ("ITEM : " + cursor2.getString(1));
                        }
                    }
                    else{
                        finalName ="ALL PRODUCT";
                    }
                    cursor2.close();
                }
                cursor1.close();
            } else {
               // return "";
                finalName="";
            }

        }
        else{
            finalName="";
           // return "";
        }
        //return "";
        return finalName;


    }


    public class MenuRecAdapter extends RecyclerView.Adapter<RecViewHolder>{

        private ArrayList<String> mList = new ArrayList<>();
        Activity context;

        public MenuRecAdapter(ArrayList<String> mList){
            this.mList = mList;
        }

        public int getItemCount(){
            return mList.size();
        }

        public RecViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){

            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_admin_manage_product2_recyclerview, viewGroup, false);
            RecViewHolder pvh = new RecViewHolder(v);
            return pvh;
        }

        public void onBindViewHolder(RecViewHolder holder, int i){
            holder.menuTeXT.setText(mList.get(i));
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }

    public class RecViewHolder extends RecyclerView.ViewHolder {

        public TextView menuTeXT;

        public RecViewHolder(View itemView){
            super(itemView);

            menuTeXT = (TextView)itemView.findViewById(R.id.menuTXT);
        }
    }





    private void setViewRecyclerViewLastItem(){
        ScrollView scrollView;
        scrollView=findViewById(R.id.scrollview);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }

    String FinalProductSubCatgID,FinalProductCatgID,FinalProductID;

    private void AddProduct(){

        POSAndroidDB posAndroidDB = new POSAndroidDB(this);
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_addproduct, null);
        builder.setView(alertLayout);
        final Spinner SubCategorySpinner =(Spinner) alertLayout.findViewById(R.id.SubCategorySpinner);
        final EditText productName =(EditText)alertLayout.findViewById(R.id.etProductName);
        final EditText productId =(EditText) alertLayout.findViewById(R.id.etProductId);
        final EditText productDes =(EditText) alertLayout.findViewById(R.id.etProductDes);
        final EditText productQuantity =(EditText) alertLayout.findViewById(R.id.etProductQuantity);
        final EditText productPrice =(EditText) alertLayout.findViewById(R.id.etProductPrice);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnSaveAddProduct);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancelAddProduct);
       final RadioGroup radioGroup =(RadioGroup) alertLayout.findViewById(R.id.radio_group_id2);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ProductListSubCategoryName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubCategorySpinner.setAdapter(arrayAdapter);
        AlertDialog alertDialog = builder.create();
        SubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // productName.setText(String.valueOf(SubCategorySpinner.getSelectedItemId()));
                String prodSubCatgID = String.valueOf(SubCategorySpinner.getSelectedItemId());

                FinalProductSubCatgID= ProductListSubCategoryID.get(Integer.valueOf(prodSubCatgID));
                FinalProductCatgID= ProductListCategoryID.get(Integer.valueOf(prodSubCatgID));
                c = db.rawQuery("select * from ITEM where CatgID='"+FinalProductCatgID+"'and SubCatgID='"+FinalProductSubCatgID+"'", null);
                if (c.getCount()==0){
                    Toast.makeText(admin_manage_product.this, "NO ITEM", Toast.LENGTH_SHORT).show();
                    int numberIncrement=1;
                    String formatted = String.format("%02d", numberIncrement);
                    productId.setText(SubCategorySpinner.getSelectedItem().toString()+"-"+formatted);
                    FinalProductID=formatted;
                }
                else{
                    Toast.makeText(admin_manage_product.this, "ITEM PRESENT", Toast.LENGTH_SHORT).show();
                    // c.moveToLast();
                    int readPK = c.getCount();

                    int incrementPK = readPK + 1;
                    String incrementPKString = String.format("%02d", incrementPK);
                    productId.setText(SubCategorySpinner.getSelectedItem().toString()+"-"+incrementPKString);
                    FinalProductID=incrementPKString;
                }


            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        String priceOverride;


        if (selectedRadioButtonId != -1) {
            // Find the selected RadioButton
            RadioButton selectedRadioButton = alertLayout.findViewById(selectedRadioButtonId);

            // Get the text of the selected RadioButton
            String selectedText = selectedRadioButton.getText().toString();

            // Use the selected text as needed
            // For example, show it in a Toast
           // Toast.makeText(getActivity(), "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
            priceOverride="yes";
        } else {
            priceOverride="no";
            // No RadioButton is selected
            // Handle this case if needed
        }



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finalProductName =    productName.getText().toString();
                String finalProductDes = productDes.getText().toString();
                String finalProductQuantity =productQuantity.getText().toString();
                String finalProductPrice = productPrice.getText().toString();
                if (finalProductName.equals("")){
                    Toast.makeText(admin_manage_product.this, "PLEASE INPUT PRODUCT NAME", Toast.LENGTH_SHORT).show();
                    productName.requestFocus();
                }
                else if(finalProductDes.equals("")){
                    Toast.makeText(admin_manage_product.this, "PLEASE INPUT PRODUCT DESCRIPTION", Toast.LENGTH_SHORT).show();
                    productDes.requestFocus();
                }
                else if(finalProductQuantity.equals("")){
                    Toast.makeText(admin_manage_product.this, "PLEASE INPUT PRODUCT QTY", Toast.LENGTH_SHORT).show();
                    productQuantity.requestFocus();
                }
                else if(finalProductPrice.equals("")){
                    Toast.makeText(admin_manage_product.this, "PLEASE INPUT PRODUCT PRICE", Toast.LENGTH_SHORT).show();
                    productPrice.requestFocus();

                }
                else{
                    Log.e("PRODUCT ID", productId.getText().toString());
                    Log.e("productName ID", productName.getText().toString());
                    Log.e("productDes ID", productDes.getText().toString());
                    Log.e("productPrice ID", productPrice.getText().toString());
                    Log.e("productQuantity ID", productQuantity.getText().toString());

                    Log.e("FinalProductCatgID ID", FinalProductCatgID);
                    Log.e("FinalProductSubCatgID", FinalProductSubCatgID);


                    boolean isInserted = posAndroidDB.insertProduct(
                            productId.getText().toString(),
                            productName.getText().toString(),
                            productDes.getText().toString(),
                            productPrice.getText().toString(),
                            productQuantity.getText().toString(),
                            " ",
                            FinalProductCatgID,
                            FinalProductSubCatgID,
                            priceOverride,
                            "VATable"




                    );
                    if (isInserted = true)
                    {


                       // showItem();
//                        createList();
                        refreshRecycleview();
                        setViewRecyclerViewLastItem();


                        c = db.rawQuery("select * from ITEM where CatgID='"+FinalProductCatgID+"'and SubCatgID='"+FinalProductSubCatgID+"'", null);




                        int readPK =(c.getCount());

                        int incrementPK = readPK + 1;
                        String incrementPKString = String.format("%02d", incrementPK);


                        productId.setText(SubCategorySpinner.getSelectedItem().toString()+"-"+incrementPKString);
                        productName.setText("");
                        productDes.setText("");
                        productPrice.setText("");
                        productQuantity.setText("");
                        productName.requestFocus();
                        Toast.makeText(admin_manage_product.this, "PRODUCT INSERTED", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(admin_manage_product.this, "PRODUCT NOT INSERTED", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        alertDialog.show();

    }
    private void loadProduct(){
        ProductListSubCategoryID.clear();
        ProductListSubCategoryName.clear();
        ProductListCategoryID.clear();
        //final List<String> list = new ArrayList<String>();
        c = db.rawQuery("select * from SUBCATEGORY", null);


        if (c.getCount() == 0) {
            Toast.makeText(this, "NO SUBCATEGORY FOUND", Toast.LENGTH_SHORT).show();
            loadSubCategory();
//            int numberIncrement=1;
//            String formatted = String.format("%03d", numberIncrement);
//            categoryIDNumber=formatted;

        }
        else {

            while (c.moveToNext()){
                ProductListSubCategoryID.add(c.getString(0));
                ProductListSubCategoryName.add(c.getString(1));
                ProductListCategoryID.add(c.getString(3));

//                ProductListID = new ArrayList<String>();
//                ProductListSubCategoryID = new ArrayList<String>();
//                ProductListSubCategoryName = new ArrayList<String>();
//                ProductListCategoryID = new ArrayList<String>();
            }
           // loadCategoryNameAndID();
            AddProduct();

        }


    }
    private void AddCategory(){

        POSAndroidDB db = new POSAndroidDB(this);
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("ADD CATEGORY");
       // AlertDialog alert = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alerdialog_addcategory, null);
        builder.setView(alertLayout);
        final EditText categoryID =(EditText)alertLayout.findViewById(R.id.categoryID);
        final EditText categoryName =(EditText) alertLayout.findViewById(R.id.categoryName);
        final EditText categoryDesc =(EditText) alertLayout.findViewById(R.id.categoryDesc);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnSaveAddProduct);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancelAddProduct);
        final Button btnSubCatg = (Button) alertLayout.findViewById(R.id.btnSubCatg);
        categoryID.setText(categoryIDNumber);
        categoryID.setTextColor(Color.BLACK);
        categoryID.setEnabled(false);
        categoryName.requestFocus();

        AlertDialog alertDialog = builder.create();




        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String catgName = categoryName.getText().toString();
                String catgDesc = categoryDesc.getText().toString();

                if (catgName.equals("")){
                    Toast.makeText(admin_manage_product.this, "EMPTY CATEGORY NAME", Toast.LENGTH_SHORT).show();
                    categoryName.requestFocus();
                }
                else if(catgDesc.equals("")){
                    Toast.makeText(admin_manage_product.this, "EMPTY CATEGORY DESCRIPTION", Toast.LENGTH_SHORT).show();
                    categoryDesc.requestFocus();
                }
                else{
                    boolean isInserted = db.insertCategory(
                            categoryID.getText().toString(),
                            categoryName.getText().toString(),
                            categoryDesc.getText().toString()
                    );

                    if (isInserted = true)
                    {
                        Toast.makeText(admin_manage_product.this, "CATEGORY INSERTED", Toast.LENGTH_SHORT).show();
                       // loadCategory();



                        int readPK = Integer.parseInt( categoryID.getText().toString());

                        int incrementPK = readPK + 1;
                        String incrementPKString = String.format("%03d", incrementPK);



                        categoryID.setText(incrementPKString);
                        categoryName.setText("");
                        categoryDesc.setText("");
                        categoryName.requestFocus();
                    }
                    else
                    {
                        Toast.makeText(admin_manage_product.this, "CATEGORY NOT INSERTED", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

       btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
          alertDialog.cancel();
           }
       });

       btnSubCatg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertDialog.cancel();
               loadSubCategory();
           }
       });






       alertDialog.show();


    }
    private void loadCategory(){
        c = db.rawQuery("select * from CATEGORY", null);


        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            int numberIncrement=1;
            String formatted = String.format("%03d", numberIncrement);
            categoryIDNumber=formatted;

        }
        else {

                try {
                    //c = db.rawQuery("SELECT * FROM FinalReceipt", null);
                    //while(itemListC.moveToLast()){
                    c.moveToLast();
                    // int numberIncrement = 1;

                    int readPK = Integer.parseInt(c.getString(0));

                    int incrementPK = readPK + 1;
                    String incrementPKString = String.format("%03d", incrementPK);

                    categoryIDNumber = incrementPKString;
                }catch (Exception ex){
//                    001 001
                    categoryIDNumber="099";
                }
        }
        AddCategory();
    }
    private void DeleteCategory(){




        POSAndroidDB db = new POSAndroidDB(this);
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        // AlertDialog alert = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alerdialog_deletecategory, null);
        builder.setView(alertLayout);

        final TextView categoryName =(TextView) alertLayout.findViewById(R.id.categoryName);
        final TextView SubCategoryName =(TextView) alertLayout.findViewById(R.id.SubCategoryName);
        final TextView itemName =(TextView) alertLayout.findViewById(R.id.itemName);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnSaveAddProduct);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancelAddProduct);
        final Button btnGoBack = (Button) alertLayout.findViewById(R.id.btnGoBack);

        // closeKeyboard();

        final Spinner SubCategorySpinner =(Spinner) alertLayout.findViewById(R.id.SubCategorySpinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CategoryNameList );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubCategorySpinner.setAdapter(arrayAdapter);
        SubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = SubCategorySpinner.getSelectedItem().toString();
                categoryName.setText(text);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });




        // categoryName.requestFocus();
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
                loadCategory();
            }
        });






        alertDialog.show();



    }
    private void loadCategoryDelete(){
        CategoryNameListID.clear();
        CategoryNameListName.clear();
        CategoryNameList.clear();
        //final List<String> list = new ArrayList<String>();
        c = db.rawQuery("select * from CATEGORY", null);


        if (c.getCount() == 0) {
            Toast.makeText(this, "NO CATEGORY FOUND", Toast.LENGTH_SHORT).show();
            loadCategory();
//            int numberIncrement=1;
//            String formatted = String.format("%03d", numberIncrement);
//            categoryIDNumber=formatted;

        }
        else {

            while (c.moveToNext()){
                CategoryNameListID.add(c.getString(0));
                CategoryNameListName.add(c.getString(1));
            }
            loadCategoryNameAndID();
            DeleteCategory();
        }


    }
    private void loadCategoryNameAndID(){
        for (int i =0;i<CategoryNameListID.size();i++){
           // CategoryNameList.add(CategoryNameListName.get(i) + " [" + CategoryNameListID.get(i) + "]");
            CategoryNameList.add(CategoryNameListName.get(i));
        }
    }
    String subCategIDFinal;
    String subCategId;
    private void AddSubCategory(){

        POSAndroidDB db2 = new POSAndroidDB(this);
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_alerdialog_addsubcategory, null);

        builder.setView(alertLayout);
        final Spinner SubCategorySpinner =(Spinner) alertLayout.findViewById(R.id.SubCategorySpinner);
        final EditText subCategoryID =(EditText) alertLayout.findViewById(R.id.subCategoryID);
        final EditText SubCategoryName =(EditText) alertLayout.findViewById(R.id.SubCategoryName);
        final EditText SubCategoryDesc =(EditText) alertLayout.findViewById(R.id.SubCategoryDesc);
        final Button btnSave = (Button) alertLayout.findViewById(R.id.btnSaveAddProduct);
        final Button btnCancel = (Button) alertLayout.findViewById(R.id.btnCancelAddProduct);
        final Button btnAddItem = (Button) alertLayout.findViewById(R.id.btnAdditem);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, CategoryNameList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SubCategorySpinner.setAdapter(arrayAdapter);
        SubCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //check item in sub
              //  subCategoryID.setText(SubCategorySpinner.getSelectedItem().toString());
                subCategId=SubCategorySpinner.getSelectedItem().toString();
                subCategoryID.setBackgroundColor(Color.WHITE);
                c = db.rawQuery("select * from SUBCATEGORY where CatgID='"+subCategId+"'", null);
                if (c.getCount()==0){
                    Toast.makeText(admin_manage_product.this, "EMPTY", Toast.LENGTH_SHORT).show();
                    int numberIncrement=1;
//                    String formatted = String.format("%02d", numberIncrement);
//                    subCategoryID.setText(subCategId+"-["+formatted+"]");
//                    subCategIDFinal=formatted;

                    String formatted = String.format("%02d", numberIncrement);
                  //  subCategoryID.setText(subCategId+"-["+formatted+"]");
                    subCategoryID.setText(subCategId+"_"+formatted);

                }else{
                    Toast.makeText(admin_manage_product.this, "CHECK SUBCATEG", Toast.LENGTH_SHORT).show();
                   // c.moveToLast();
                        int readPK = c.getCount();

                        int incrementPK = readPK + 1;
                        String incrementPKString = String.format("%02d", incrementPK);
//                    subCategoryID.setText(subCategId+"-["+incrementPKString+"]");
                    subCategoryID.setText(subCategId+"_"+incrementPKString);
                    subCategIDFinal=incrementPKString;

                }



            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String subCatgName = SubCategoryName.getText().toString();
                String subCatgDesc = SubCategoryDesc.getText().toString();

                if (subCatgName.equals("")){
                    Toast.makeText(admin_manage_product.this, "EMPTY CATEGORY NAME", Toast.LENGTH_SHORT).show();
                    SubCategoryName.requestFocus();
                }
                else if(subCatgDesc.equals("")){
                    Toast.makeText(admin_manage_product.this, "EMPTY CATEGORY DESCRIPTION", Toast.LENGTH_SHORT).show();
                    SubCategoryDesc.requestFocus();
                }
                else{

                    boolean isInserted = db2.insertSubCategory(
                            subCategoryID.getText().toString(),
                            SubCategoryName.getText().toString(),
                            SubCategoryDesc.getText().toString(),
                            SubCategorySpinner.getSelectedItem().toString()




                    );

                    if (isInserted = true)
                    {
                        Toast.makeText(admin_manage_product.this, "SUBCATEGORY INSERTED", Toast.LENGTH_SHORT).show();
                        // loadCategory();



//                        int readPK = Integer.parseInt(subCategIDFinal);
//
//                        int incrementPK = readPK + 1;
//                        String incrementPKString = String.format("%02d", incrementPK);
                        int readPK = Integer.parseInt(subCategIDFinal);

                        int incrementPK = readPK + 1;
                        String incrementPKString = String.format("%02d", incrementPK);
                        subCategoryID.setText(subCategId+"-["+incrementPKString+"]");
                        subCategIDFinal=incrementPKString;



                        //subCategoryID.setText(subCategId +"-["+incrementPKString+"]");
                        SubCategoryName.setText("");
                        SubCategoryDesc.setText("");
                        SubCategoryName.requestFocus();
                    }
                    else
                    {
                        Toast.makeText(admin_manage_product.this, "SUBCATEGORY NOT INSERTED", Toast.LENGTH_SHORT).show();
                    }

                }




            }
        });

        builder.show();

    }
    private void loadSubCategory(){
        CategoryNameListID.clear();
        CategoryNameListName.clear();
        CategoryNameList.clear();
        //final List<String> list = new ArrayList<String>();
        c = db.rawQuery("select * from CATEGORY", null);


        if (c.getCount() == 0) {
            Toast.makeText(this, "NO CATEGORY FOUND", Toast.LENGTH_SHORT).show();
            loadCategory();
//            int numberIncrement=1;
//            String formatted = String.format("%03d", numberIncrement);
//            categoryIDNumber=formatted;

        }
        else {

            while (c.moveToNext()){
                CategoryNameListID.add(c.getString(0));
                CategoryNameListName.add(c.getString(1));
            }
            loadCategoryNameAndID();
            AddSubCategory();

        }


    }


    private void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
    private static final int SELECT_PICTURE = 0;
    String selectedImageUri;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();
                selectedImageUri  = getPath(uri);
                try {
                    updateDBImage();
                    Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_SHORT).show();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void updateDBImage() throws IOException {







        db = getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        String sql = "Update ITEM set ItemImage='"+selectedImageUri+"'where ItemID='"+FinalItemCode+"'";
        db.execSQL(sql);
      //  showItem();
       refreshRecycleview();
        Toast.makeText(this, "UPDATED", Toast.LENGTH_SHORT).show();
    }
    public void refreshRecycleview(){
        rv_productList = findViewById(R.id.rv_productList);

        rv_productList.setHasFixedSize(true);
        layoutManager2=new LinearLayoutManager(this);
        layoutManager2.removeAllViews();
        rv_productList.setLayoutManager(layoutManager2);
        mAdapterProduct=new admin_manage_product.RecyclerviewAdapter(ProductList,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_productList.setAdapter(mAdapterProduct);


        //invoice item list



    }




//    private RecyclerView.LayoutManager layoutManager;
//    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager layoutManager2;
    private RecyclerView.Adapter mAdapterProduct;
    private RecyclerView.Adapter mAdapterCategory;


    List<admin_manage_product_item> ProductList = new ArrayList<>();
    List<admin_manage_product_item_category> CategoryList = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();


    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_manage_product.RecyclerviewAdapter.MyViewHolder>{
        List<admin_manage_product_item> ProductList;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_manage_product.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        private int selectedPosition = RecyclerView.NO_POSITION;
        public void setSelectedPosition(int position) {
            selectedPosition = position;

//            Log.d("SELECTED ID",headerFooterID.get(position).toString());
            notifyDataSetChanged();
        }

        public RecyclerviewAdapter(List<admin_manage_product_item> ProductList, ArrayList<String> selectList, Context context) {

            this.ProductList = ProductList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_manage_product.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout,parent,false);



            admin_manage_product.RecyclerviewAdapter.MyViewHolder holder = new admin_manage_product.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_manage_product.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

            holder.tv_ItemName.setText(String.valueOf((ProductList.get(position).getItemName())));
            holder.tv_ItemDesc.setText((String.valueOf(ProductList.get(position).getItemDescription())));
            holder.tv_ItemID.setText((String.valueOf(ProductList.get(position).getItemID())));
            holder.tv_ItemPrice.setText((String.valueOf(ProductList.get(position).getItemPrice())));
            holder.tv_ItemQty.setText((String.valueOf(ProductList.get(position).getItemQty())));
            holder.tv_FastMoving.setText((String.valueOf(ProductList.get(position).getFastMovingItem())));
            holder.tv_FastMovingButton.setText((String.valueOf(ProductList.get(position).getFastMovingButton())));
            holder.tv_ItemCode.setText((String.valueOf(ProductList.get(position).getItemCode())));
            holder.tv_ItemBarcode.setText((String.valueOf(ProductList.get(position).getBarcode())));
            holder.tv_VatIndicator.setText((String.valueOf(ProductList.get(position).getVATIndicator())));
            holder.tv_PriceOverride.setText((String.valueOf(ProductList.get(position).getPriceOverride())));

            holder.tv_itemCount.setText(String.valueOf(position+1));






        }

        @Override
        public int getItemCount() {
            return ProductList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_ItemName;
            TextView tv_ItemDesc;
            TextView tv_ItemID;
            TextView tv_ItemPrice;
            TextView tv_ItemQty;
            TextView tv_FastMoving;
            TextView tv_FastMovingButton;
            TextView tv_ItemCode;
            TextView tv_ItemBarcode;

            TextView tv_itemCount;
            LinearLayout ll_linearLayout;
            TextView tv_VatIndicator;
            TextView tv_PriceOverride;

            //CardView parentLayout;




            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_ItemName = itemView.findViewById(R.id.tv_ItemName);
                tv_ItemDesc = itemView.findViewById(R.id.tv_ItemDesc);
                tv_ItemID = itemView.findViewById(R.id.tv_ItemID);
                tv_ItemPrice = itemView.findViewById(R.id.tv_ItemPrice);
                tv_ItemQty = itemView.findViewById(R.id.tv_ItemQty);
                tv_FastMoving = itemView.findViewById(R.id.tv_ItemFastMoving);
                tv_FastMovingButton = itemView.findViewById(R.id.tv_ItemFastMovingButton);
                tv_ItemCode = itemView.findViewById(R.id.tv_ItemCode);
                tv_ItemBarcode = itemView.findViewById(R.id.tv_ItemBarcode);
                tv_itemCount= itemView.findViewById(R.id.tv_itemCount);
                ll_linearLayout=itemView.findViewById(R.id.ll_linearLayout);
                tv_VatIndicator=itemView.findViewById(R.id.tv_VatIndicator);
                tv_PriceOverride= itemView.findViewById(R.id.tv_PriceOverride);






                ll_linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strItemName=tv_ItemName.getText().toString();

                        strItemID=tv_ItemID.getText().toString();
//                        strDescription=tv_ItemDesc.getText().toString();
//                        strPrice=tv_ItemPrice.getText().toString();
//                        strQty=tv_ItemQty.getText().toString();
//                        strCode=tv_ItemCode.getText().toString();
//                        strBarcode=tv_ItemBarcode.getText().toString();
                        strShortKeys=tv_FastMovingButton.getText().toString();




                        c = db.rawQuery("select * from ITEM where ItemID='"+strItemID+"'and ItemName='"+strItemName+"'", null);
                        if (c.getCount()!=0){
                            while(c.moveToNext()){
                                strDescription=c.getString(2);
                                strPrice=c.getString(3);
                                strQty=c.getString(4);
                                strCode=c.getString(8);
                                strBarcode=c.getString(9);
                                strItemSafeStock=c.getString(10);
                                strItemPrice2=c.getString(11);
                                strItemPrice3=c.getString(12);
                                strItemPrice4=c.getString(13);
                                strItemPrice5=c.getString(14);
                                if (c.getString(15)==("") || c.getString(15)==(null)){
                                    VATIndicatorID=1;
                                }

                                else if (c.getString(15).equals("Non-VATable")){
                                    VATIndicatorID=0;

                                } else if (c.getString(15).equals("VATable")) {
                                    VATIndicatorID=1;
                                }

                                VATIndicator=c.getString(15);

                                if (c.getString(10)==null || c.getString(10).equals("") || c.getString(10).length()==0){
                                    strItemSafeStock="0";
                                }
                                if (c.getString(11)==null || c.getString(11).equals("")){
                                    strItemPrice2="0.00";
                                }
                                if (c.getString(12)==null || c.getString(12).equals("")){
                                    strItemPrice3="0.00";
                                }
                                if (c.getString(13)==null|| c.getString(13).equals("")){
                                    strItemPrice4="0.00";
                                }
                                if (c.getString(14)==null|| c.getString(14).equals("")){
                                    strItemPrice5="0.00";
                                }
                                if (c.getString(16)==null|| c.getString(16).equals("") || c.getString(16).equalsIgnoreCase("NO")){
                                    PriceOverride="1";
                                }
                                else if (c.getString(16).equalsIgnoreCase("yes") || c.getString(16).equalsIgnoreCase("YES")){
                                    PriceOverride="0";
                                }



                            }

                        }
                        c.close();






                        AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext());
                       // AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        LayoutInflater inflater = getLayoutInflater();
                        final View alertLayout = inflater.inflate(R.layout.custom_alertdialog_product_update_delete, null);
                        builder.setView(alertLayout);
                        AlertDialog alertDialog = builder.create();

                        ImageButton img_update = alertLayout.findViewById(R.id.img_update);
                        img_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                               // alertDialog.dismiss();



                                AlertDialog.Builder builder  = new AlertDialog.Builder(view.getContext(),android.R.style.Theme_Light);
                                LayoutInflater inflater = getLayoutInflater();
                                final View alertLayout2 = inflater.inflate(R.layout.custom_alertdialog_manage_product_update, null);
                                builder.setView(alertLayout2);
                                AlertDialog alertDialog2 = builder.create();

                                 final EditText et_ItemName = alertLayout2.findViewById(R.id.et_ItemName);
                                EditText et_Description = alertLayout2.findViewById(R.id.et_Description);
                                EditText et_ItemID = alertLayout2.findViewById(R.id.et_ItemID);
                                EditText et_ItemPrice = alertLayout2.findViewById(R.id.et_ItemPrice);
                                EditText et_ItemPrice2 = alertLayout2.findViewById(R.id.et_ItemPrice2);
                                EditText et_ItemPrice3 = alertLayout2.findViewById(R.id.et_ItemPrice3);
                                EditText et_ItemPrice4 = alertLayout2.findViewById(R.id.et_ItemPrice4);
                                EditText et_ItemPrice5 = alertLayout2.findViewById(R.id.et_ItemPrice5);
                                EditText et_ItemQty = alertLayout2.findViewById(R.id.et_ItemQty);
                                EditText et_ItemCode = alertLayout2.findViewById(R.id.et_ItemCode);
                                EditText et_ItemBarcode = alertLayout2.findViewById(R.id.et_ItemBarcode);
                                EditText et_ItemSafeStock = alertLayout2.findViewById(R.id.et_ItemQtySafeStock);
                                Spinner sp_fastMoving = alertLayout2.findViewById(R.id.sp_fastMoving);
                                Spinner sp_shortcutKeys=alertLayout2.findViewById(R.id.sp_shortcutKeys);
                                Spinner sp_vatIndicator=alertLayout2.findViewById(R.id.sp_vatIndicator);
                                Spinner sp_priceOverride=alertLayout2.findViewById(R.id.sp_priceOverride);
                                Button  btn_save = alertLayout2.findViewById(R.id.btn_save);



                                ArrayList<String> arrayList = new ArrayList<>();
                                ArrayList<String> arrayListfinal = new ArrayList<>();
                                ArrayList<String> arrayList2 = new ArrayList<>();
                                ArrayList<String> arrayList3 = new ArrayList<>();
                                ArrayList<String> arrayList4 = new ArrayList<>();

                                int x;

                                for(x=1;x<=13;x++){
//                                    if (x<10){
//                                      //  arrayList.add("D0"+x);
//                                    }
//                                    else{
//                                       // arrayList.add("D"+x);
//                                    }
                                    arrayList.add("ITEM "+x);

                                }
                                arrayList.add("NONE");
                                int arrayLength =  arrayList.size();
//                                for (int x2=0;x2<arrayLength;x2++){
//                                    c = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+arrayList.get(0)+"'", null);
//                                    if (c.getCount()!=0){
//                                        arrayList.remove(arrayList.get(x2));
//                                    }
//                                }



                                arrayList2.add("YES");
                                arrayList2.add("NO");
                                arrayList3.add("Non-VATable");
                                arrayList3.add("VATable");

                                arrayList4.add("YES");
                                arrayList4.add("NO");

//
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, arrayList);
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, arrayList2);
                                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, arrayList3);
                                arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, arrayList4);
                                arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                sp_shortcutKeys.setAdapter(arrayAdapter);
                                sp_fastMoving.setAdapter(arrayAdapter2);
                                sp_vatIndicator.setAdapter(arrayAdapter3);
                                sp_priceOverride.setAdapter(arrayAdapter4);


                                et_ItemName.setText(strItemName);
                                et_Description.setText(strDescription);
                                et_ItemID.setText(strItemID);
                                et_ItemPrice.setText(strPrice);
                                et_ItemQty.setText(strQty);
                                et_ItemCode.setText(strCode);
                                et_ItemBarcode.setText(strBarcode);
                                et_ItemSafeStock.setText(strItemSafeStock);
                                et_ItemPrice2.setText(strItemPrice2);
                                et_ItemPrice3.setText(strItemPrice3);
                                et_ItemPrice4.setText(strItemPrice4);
                                et_ItemPrice5.setText(strItemPrice5);
                                sp_vatIndicator.setSelection(VATIndicatorID);
                                sp_priceOverride.setSelection(Integer.parseInt(PriceOverride));



                                if (!strShortKeys.equals("NONE")) {

//                                    if (strShortKeys.length() == 3) {
//                                        strShortKeys = strShortKeys.replace("D", "");
//                                        strShortKeys = strShortKeys.replace("0", "");
//                                    }

                                    //sp_shortcutKeys.setSelection((Integer.parseInt(strShortKeys.replace("ITEM ",""))-1));
                                   // sp_shortcutKeys=strShortKeys
                                }
                                else{
                                   // sp_shortcutKeys.setSelection(arrayList.size()-1);
                                }

                                btn_save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //update id
                                      //  String strsql = "UPDATE ITEM SET ItemID='" + strItemID + "'  where TransactionID='" + readRefNumber + "' and OrderID='"+String.valueOf(selectList.get(i))+"'";

                                        String strsql = "UPDATE ITEM SET ItemID='" + et_ItemID.getText().toString() +
                                                "',ItemName='" + et_ItemName.getText().toString() +
                                                "',ItemDesc='"+strDescription+
                                                "',ItemPrice='"+et_ItemPrice.getText().toString()+
                                                "',ItemQTY='"+et_ItemQty.getText().toString()+
                                                "',ItemImage='"+selectedImageUri+
                                                "',ItemCode='"+et_ItemCode.getText().toString()+
                                                "',Barcode='"+et_ItemBarcode.getText().toString()+
                                                "',ItemSafeStock='"+et_ItemSafeStock.getText().toString()+
                                                "',ItemPrice2='"+et_ItemPrice2.getText().toString()+
                                                "',ItemPrice3='"+et_ItemPrice3.getText().toString()+
                                                "',ItemPrice4='"+et_ItemPrice4.getText().toString()+
                                                "',ItemPrice5='"+et_ItemPrice5.getText().toString()+
                                                "',VatIndicator='"+sp_vatIndicator.getSelectedItem()+
                                                "',PriceOverride='"+sp_priceOverride.getSelectedItem()+"' where ItemID='"+strItemID+"'";

                                        db.execSQL(strsql);



                                        c = db.rawQuery("select * from ShortCutKeysMap where shortCutKeys='"+sp_shortcutKeys.getSelectedItem().toString()+"'", null);
                                        if (c.getCount()!=0){

                                            String strsql2 = "UPDATE ShortCutKeysMap SET shortCutKeys='" + sp_shortcutKeys.getSelectedItem().toString()+"',shortCutID='"+et_ItemID.getText().toString()+"' where shortCutKeys='"+sp_shortcutKeys.getSelectedItem().toString()+"'";
                                            db.execSQL(strsql2);
                                        }
                                        else{
//                                            POSAndroidDB posAndroidDB = new POSAndroidDB(view.getContext());
//                                            posAndroidDB.insertShortcutKeys(sp_shortcutKeys.getSelectedItem().toString(),et_ItemID.getText().toString());
                                        }

                                        c.close();



                                        alertDialog.dismiss();
                                        alertDialog2.dismiss();
                                     //   showItem();
                                        refreshRecycleview();

                                    }
                                });





//
//                                ImageButton img_update = alertLayout.findViewById(R.id.img_update);
//                                img_update.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        //loadCategory();
//                                    }
//                                });
//
//                                ImageButton img_delete = alertLayout.findViewById(R.id.img_delete);
//                                img_delete.setOnClickListener(new View.OnClickListener() {
//                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                                    @Override
//                                    public void onClick(View view) {
//                                        // loadSubCategory();
//                                    }
//                                });




                                alertDialog2.show();









                            }
                        });

                        ImageButton img_delete = alertLayout.findViewById(R.id.img_delete);
                        img_delete.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View view) {
                               // loadSubCategory();

                                String strsql = "Delete from ITEM where ItemID='"+strItemID+"'";
                                db2.execSQL(strsql);

                               // showItem();
                                refreshRecycleview();



                            }
                        });




                        alertDialog.show();



                    }
                });



            }
        }
    }




    // recyclerview for category content
    //note!!
    private void showItemCategory(){
        CategoryArrayListID.clear();
        CategoryArrayListName.clear();
        SubCategoryArrayListID.clear();
        SubCategoryArrayListName.clear();


        c = db.rawQuery("select * from CATEGORY", null);



        if (c.getCount() == 0) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }


//        while(c.moveToNext()){
//            CategoryNameListID.add(c.getString(0));
//            CategoryArrayListName.add(c.getString(1));
            //Log.e("TEST",c.getString(0));


        //    Cursor c2=db.rawQuery("select * from SUBCATEGORY where CatgID='"+c.getString(0)+"'", null);
            Cursor c2=db.rawQuery("select * from SUBCATEGORY", null);

            if (c2.getCount()!=0){
                admin_manage_product_item_category p0 = null;
                while (c2.moveToNext()){
                    SubCategoryArrayListID.add(c2.getString(0));
                    SubCategoryArrayListName.add(c2.getString(1));
                   p0=new admin_manage_product_item_category(
                           c2.getString(1),c2.getString(0));
                   
                    //Log.e("CATEGORY",c.getString(1));
                    //   c.getString(2),

                    Log.e("TAG", "subCatgID "+c2.getString(0) );
                    Log.e("TAG", "subCatgName "+c2.getString(1) );
                    Log.e("TAG", " " );
                    CategoryList.addAll(Arrays.asList(new admin_manage_product_item_category[]{p0}));
                }
               // p0 = new admin_manage_product_item_category("test");;

//                for(int x=0;x<=SubCategoryArrayListID.size();x++){
//
//
//
//                }
//                CategoryList.addAll(Arrays.asList(new admin_manage_product_item_category[]{p0}));

            }
            else{
                //Log.e("ELSE","NO DATA FOUND");
            }
            c2.close();



            // Toast.makeText(this, c.getString(2), Toast.LENGTH_SHORT).show();



//        }
        c.close();
    }




    //note!!
    public void refreshRecycleviewCategory(){
        rv_categoryContent = findViewById(R.id.rv_categoryContent);

        rv_categoryContent.setHasFixedSize(true);
        layoutManager2=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManager2.removeAllViews();
        rv_categoryContent.setLayoutManager(layoutManager2);
        mAdapterCategory=new admin_manage_product.RecyclerviewAdapter2(CategoryList,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_categoryContent.setAdapter(mAdapterCategory);

        //invoice item list



    }

    String SelectedCategoryID;
    public class RecyclerviewAdapter2 extends RecyclerView.Adapter <admin_manage_product.RecyclerviewAdapter2.MyViewHolder>{
        List<admin_manage_product_item_category> CategoryList;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_manage_product.RecyclerviewAdapter2.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter2(List<admin_manage_product_item_category> CategoryList, ArrayList<String> selectList, Context context) {

            this.CategoryList = CategoryList;
            this.selectList = selectList;
            this.context = context;

        }

        private int selectedPosition = RecyclerView.NO_POSITION;
        public void setSelectedPosition(int position) {
            selectedPosition = position;

//            Log.d("SELECTED ID",headerFooterID.get(position).toString());
            notifyDataSetChanged();
        }

        public void openUpdateDialog(int itemID){
            int id = (itemID);
            for (int x=0;x<CategoryList.size();x++){

              //  Log.d("ID",CategoryList.get(x).toString());



                if (x==id){
                    Log.d("SELECTED ID",CategoryList.get(id).toString());

                    //  Log.e("item id",tv_itemID.getText().toString());
                   // deleteID=Integer.parseInt(CategoryList.get(id).toString());
                   // updateItem();
                    // return true;

                  //  Toast.makeText(admin_manage_product.this, selectedCategoryID, Toast.LENGTH_SHORT).show();
                   showItem(String.valueOf(CategoryList.get(id).getItemSubCategoryID()));
                    refreshRecycleview();


                }
                else{
                    Log.d("SELECTED ID ELSE","".toString());
                }
            }
        }

        @NonNull
        @Override
        public admin_manage_product.RecyclerviewAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_layout,parent,false);



            admin_manage_product.RecyclerviewAdapter2.MyViewHolder holder = new admin_manage_product.RecyclerviewAdapter2.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_manage_product.RecyclerviewAdapter2.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;

          Cursor checkCateg = db.rawQuery("select * from ITEM where SubCatgID='"+(CategoryList.get(position).getItemSubCategoryID())+"'", null);
          if(checkCateg.getCount()!=0){
              holder.btn_category.setText(String.valueOf(CategoryList.get(position).getItemSubCategory()) + " ("+(checkCateg.getCount())+")");
          }
          else{
              holder.btn_category.setText(String.valueOf(CategoryList.get(position).getItemSubCategory()) + " ("+("0")+")");
          }





            holder.selectedCategoryID=String.valueOf(CategoryList.get(position).getItemSubCategoryID());



            if (position == selectedPosition) {
                //  holder.ll_linearLayout1.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
                //  holder.ll_linearLayout2.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
                holder.ll_background.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
//                holder.tv_itemText.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));
//                holder.tv_itemDoubleWeight.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));




            } else {
                //  holder.ll_linearLayout1.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
                // holder.ll_linearLayout2.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                holder.ll_background.setBackgroundColor(ContextCompat.getColor(context, androidx.leanback.R.color.lb_tv_white));
//                holder.tv_itemText.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//                holder.tv_itemDoubleWeight.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }


            holder.btn_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // Toggle background color
                    if (position == position) {
                        // If already blue, make it white

                      //  openUpdateDialog(position);
                        holder.ll_background.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue_600));

                        selectedPosition = position;
                        openUpdateDialog(position);

                        // No item selected
                    } else {
                        // If white, make it blue
                        holder.ll_background.setBackgroundColor(ContextCompat.getColor(context, androidx.leanback.R.color.lb_tv_white));
                       // Update selected position
                        selectedPosition = RecyclerView.NO_POSITION;
                    }

                    // Notify adapter that data has changed to refresh the RecyclerView
                    notifyDataSetChanged();

                   // Log.e("TAG", "onClick: "+position );
                    x=position;

                    // Your existing onClick logic goes here
                    // ...
                }
            });




        }

        @Override
        public int getItemCount() {
            return CategoryList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            Button btn_category;
            String selectedCategoryID;
            LinearLayout ll_background;






            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                btn_category = itemView.findViewById(R.id.btn_category);
                ll_background = itemView.findViewById(R.id.ll_background);

                btn_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Toast.makeText(admin_manage_product.this, String.valueOf(), Toast.LENGTH_SHORT).show();
                        showItem(selectedCategoryID);
                        refreshRecycleview();
                    }
                });








            }
        }
    }







}