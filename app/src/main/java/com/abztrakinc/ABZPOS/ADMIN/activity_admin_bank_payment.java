package com.abztrakinc.ABZPOS.ADMIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.settingsDB;

import java.util.ArrayList;
import java.util.Arrays;

public class activity_admin_bank_payment extends AppCompatActivity {
    Context context;
    settingsDB settingsDB;
    Spinner spinnerCardType;
    Spinner spinnerBankName;
    SQLiteDatabase db;
    String DB_NAME="settings.db";
    ArrayList<String>CardTypeList = new ArrayList<>();
    ArrayList<String>BankNameList = new ArrayList<>();
    ArrayList<activity_admin_bank_payment_model> ButtonList = new ArrayList<>();
    activity_admin_bank_payment_model po = null;
    RecyclerView rv_bankRecyclerView;
    Button buttonSave;
    EditText et_itemName;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bank_payment);
        context = this.getApplicationContext();
        settingsDB = new settingsDB(context);
        buttonSave = findViewById(R.id.btn_buttonSave);
        et_itemName = findViewById(R.id.et_itemName);
        db = this.getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

//        insertCategory("DEBIT CARD");
//        insertCategory("CREDIT CARD");
//        insertBankName("BDO","CREDIT CARD");

        loadSpinnerCardTypeDB();
        loadSpinnerCardType();

        loadData();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerBankName.getSelectedItem()!=null && et_itemName.getText().length()!=0) {
                    insertBankPayment(et_itemName.getText().toString(),spinnerBankName.getSelectedItem().toString(),spinnerCardType.getSelectedItem().toString());
                    loadData();
                }

            }
        });



    }


    //region inserting Module

    private void insertCategory(String cardType){


        boolean isInserted = settingsDB.insertCardType(
                cardType
        );
        if (isInserted) {
            // Show a success message
            Toast.makeText(this, "successfully processed", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where insertion failed
            Toast.makeText(this, "failed to  process", Toast.LENGTH_SHORT).show();
        }


    }

    private void insertBankName(String BankName,String cardTyp){
        boolean isInserted = settingsDB.insertBankName(
                BankName,cardTyp
        );
        if (isInserted) {
            // Show a success message
            Toast.makeText(this, "successfully processed", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where insertion failed
            Toast.makeText(this, "failed to  process", Toast.LENGTH_SHORT).show();
        }

    }

    private void insertBankPayment(String ItemName,String BankName,String CardType){
        boolean isInserted = settingsDB.insertBankPayment(
                ItemName,BankName,CardType
        );
        if (isInserted) {
            // Show a success message
            Toast.makeText(this, "successfully processed", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where insertion failed
            Toast.makeText(this, "failed to  process", Toast.LENGTH_SHORT).show();
        }
    }


    //endregion

    //region loading of card type
    private void loadSpinnerCardTypeDB(){

        Cursor cursor = db.rawQuery("select * from CardType", null);
        if (cursor.getCount()!=0){
            while (cursor.moveToNext())
            CardTypeList.add(cursor.getString(1));
        }
    }
    private void loadSpinnerCardType(){
        spinnerCardType = findViewById(R.id.sp_spinnerCardType);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CardTypeList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCardType.setAdapter(adapter);

        // Set a listener for item selections
        spinnerCardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
                String selectedValue = CardTypeList.get(position);
                //Toast.makeText(admin_stock_receiving.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
               // itemID=selectedValue;

                loadSpinnerBankNameDB(selectedValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }
    //endregion

    //region loading of Bank name
    private void loadSpinnerBankNameDB(String CardType){
    BankNameList.clear();
        Cursor cursor = db.rawQuery("select * from BankName where CardType='"+CardType+"'", null);
        if (cursor.getCount()!=0){
            while (cursor.moveToNext())
                BankNameList.add(cursor.getString(1));
        }

        loadSpinnerBankName();
    }
    private void loadSpinnerBankName(){
        spinnerBankName = findViewById(R.id.sp_spinnerBankName);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BankNameList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerBankName.setAdapter(adapter);

        // Set a listener for item selections
        spinnerBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item
               // String selectedValue = CardTypeList.get(position);
                //Toast.makeText(admin_stock_receiving.this, "Selected: " + selectedValue, Toast.LENGTH_SHORT).show();
                // itemID=selectedValue;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }
    //endregion


    //region recyclerview

    private void loadData(){
        db = this.getApplicationContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("select * from BankDetails", null);
        if (c.getCount()!=0){
            while (c.moveToNext()){

                po = new activity_admin_bank_payment_model(
                        c.getInt(0),c.getString(1),c.getString(2),c.getString(3)

                );
                ButtonList.addAll(Arrays.asList(new activity_admin_bank_payment_model[]{po}));

            }
        }

        refreshRecyclerview();

    }




    public void  refreshRecyclerview(){
//        createSampleData();


        rv_bankRecyclerView = findViewById(R.id.rv_bankRecyclerView);
        rv_bankRecyclerView.removeAllViews();
        rv_bankRecyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManager.removeAllViews();
        rv_bankRecyclerView.setLayoutManager(layoutManager);
        mAdapter=new activity_admin_bank_payment_recyclerview(context,ButtonList);
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_bankRecyclerView.setAdapter(mAdapter);

        //invoice item list



    }



    //endregion






}