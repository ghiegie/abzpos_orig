package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.discount_model;
import com.abztrakinc.ABZPOS.settingsDB;

import org.apache.poi.hssf.record.PageBreakRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class admin_pos_settings_discount extends AppCompatActivity {
    ArrayList<String> DiscCategory=new ArrayList<>();
    ArrayList<String>DiscountType=new ArrayList<>();
    ArrayList<String>DiscountLabel=new ArrayList<>();
    ArrayList<String>ReceiptOption=new ArrayList<>();
    String DB_NAME = "settings.db";


    


    List<discount_model> ArrayDiscountList = new ArrayList<>();


    CheckBox cbox_ExcludeTax,cbox_salesExcludeTax,cbox_ProRated,cbox_OpenDiscount;

    EditText et_discAutoID,et_discID,et_discName,et_discountPercent,et_discountComputation;
    EditText et_minTransAmount,et_maxTransAmount,et_maxDiscAmount;
    Spinner sp_category,sp_discountType,sp_Label,sp_receiptOption;
    ImageButton btn_addItem;
    String cboxCheck="NO";
    String cboxCheck2="NO";
    String cboxCheck3="NO";
    String cboxCheck4="NO";

    ArrayList<String> DiscountList=new ArrayList<String>();
    ArrayList<String> DiscountListID=new ArrayList<String>();
    ArrayList<String> DiscountListName=new ArrayList<String>();
    ArrayList<String> DiscountListAmount=new ArrayList<String>();
    SQLiteDatabase db2,db;
    RecyclerView rv_discList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    String FinalCategory,FinalDiscountType,FinalDiscountLabel;


    int id;
    int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pos_settings_discount);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadDiscount();




    }

    private void loadDiscount(){
        DiscCategory.clear();
        DiscountType.clear();
        DiscountLabel.clear();
        ReceiptOption.clear();

        getDiscountList();

        cbox_ExcludeTax = this.findViewById(R.id.cbox_excludeTax);
        cbox_salesExcludeTax=this.findViewById(R.id.cbox_salesExcludeTax);
        cbox_ProRated = this.findViewById(R.id.cbox_ProRated);
        cbox_OpenDiscount = this.findViewById(R.id.cbox_OpenDiscount);
        et_discountPercent = this.findViewById(R.id.et_discountPercent);
        et_discountComputation = this.findViewById(R.id.et_discountComputation);
        et_discAutoID=this.findViewById(R.id.et_discAutoID);
        btn_addItem=this.findViewById(R.id.btn_addItem);
        et_discID=this.findViewById(R.id.et_discID);
        et_discID.setFilters(new InputFilter[]{
                new InputFilter.AllCaps()
        });
        et_discName=this.findViewById(R.id.et_discName);
        rv_discList= this.findViewById(R.id.rv_discList);
        et_discAutoID.setText(String.valueOf(ID));
        sp_category=this.findViewById(R.id.sp_category);

        ImageButton btn_close=this.findViewById(R.id.btn_close);
        ImageButton btn_removeItem=this.findViewById(R.id.btn_removeItem);

        DiscCategory.add("SCD DISCOUNT");
        DiscCategory.add("PWD DISCOUNT");
        DiscCategory.add("REGULAR DISCOUNT");
//        DiscCategory.add("SOLO-PARENT DISCOUNT");
//        DiscCategory.add("ATHLETE DISCOUNT");


        DiscountType.add("NORMAL DISCOUNT");
        DiscountType.add("SPECIAL DISCOUNT");


        DiscountLabel.add("SCD DISCOUNT");
        DiscountLabel.add("PWD DISCOUNT");
        DiscountLabel.add("REGULAR DISCOUNT");
        DiscountLabel.add("SOLO-PARENT DISCOUNT");
        DiscountLabel.add("ATHLETE DISCOUNT");


        ReceiptOption.add("SINGLE RECEIPT");
        ReceiptOption.add("DOUBLE RECEIPT");




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,DiscCategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
//                        EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);
//
//                        et_discountName.setText(DiscountListName.get(s.getSelectedItemPosition()));
//                        et_discountAmount.setText(DiscountListAmount.get(s.getSelectedItemPosition()));
//                        id=Integer.valueOf(DiscountList.get(s.getSelectedItemPosition()));
                // int id=Integer.v;
                if (i==0){
                    FinalCategory="SCD";
                }
                else if (i==1){
                    FinalCategory="PWD";
                }
                else if(i==2){
                    FinalCategory="REG";
                }
//                else if(i==4){
//                    FinalCategory="SOL";
//                }
//                else if(i==5){
//                    FinalCategory="ATH";
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_discountType=this.findViewById(R.id.sp_discountType);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,DiscountType);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_discountType.setAdapter(adapter2);
        sp_discountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_Label=this.findViewById(R.id.sp_label);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,DiscountLabel);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Label.setAdapter(adapter3);

        sp_Label.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        sp_receiptOption=this.findViewById(R.id.sp_receiptOption);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ReceiptOption);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_receiptOption.setAdapter(adapter4);

        sp_receiptOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        et_minTransAmount=this.findViewById(R.id.et_minTransAmount);
        et_maxTransAmount=this.findViewById(R.id.et_maxTransAmount);
        et_maxDiscAmount=this.findViewById(R.id.et_maxDiscAmount);

        btn_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert list



                //check if DiscountID exist

                ExistDiscount();

            }
        });
        btn_removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                        SQLiteDatabase db = view.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
//                        Cursor discountListID = db.rawQuery("select * from DiscountList", null);
//                        if (discountListID.getCount()!=0){
//                            DiscountList.clear();
//                            DiscountListID.clear();
//                            DiscountListName.clear();
//                            DiscountListAmount.clear();
//                            while(discountListID.moveToNext()){
//                                DiscountList.add(discountListID.getString(0));
//                                DiscountListID.add(discountListID.getString(1));
//                                DiscountListName.add(discountListID.getString(2));
//                                DiscountListAmount.add(discountListID.getString(3));
//                            }
//
//
//
//                        }




                Log.e("ID",String.valueOf(id));
                SQLiteDatabase db2 = view.getContext().openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                String strsql = "DELETE FROM DiscountList where ID='"+DiscID+"'";
                db2.execSQL(strsql);
                db2.close();

                FillDataRV();
                RefreshRV();


            }
        });
      

       

        FillDataRV();
        RefreshRV();




    }

    private void ExistDiscount(){

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        //  ArrayDiscountList.clear();
        Cursor itemListC = db.rawQuery("select * from DiscountList where DiscountID='"+et_discID.getText()+"'", null);
        if (itemListC.getCount()!=0){

            Toast.makeText(this, "DISCOUNT EXIST "+ et_discID.getText().toString(), Toast.LENGTH_SHORT).show();

        try {

            String strsql = "UPDATE DiscountList SET " +
                    "DiscountName='" + et_discName.getText().toString() + "'," +
                    "DiscountAmount='" + et_discountPercent.getText().toString() + "'," +
                    "DiscountExcludeTax='" + cboxCheck + "'," +

                    "DiscountType='" + sp_discountType.getSelectedItem().toString() + "'," +
                    "DiscCategory='" + FinalCategory + "'," +
                    "DiscLabel='" + sp_Label.getSelectedItem().toString() + "'," +
                    "MinTransactionAmount='" + et_minTransAmount.getText().toString() + "'," +
                    "MaxTransactionAmount='" + et_maxTransAmount.getText().toString() + "'," +
                    "MaxDiscountAmount='" + et_maxDiscAmount.getText().toString() + "'," +
                    "SalesExcludeTax='" + cboxCheck2 + "'," +
                    "ReceiptOption='" + sp_receiptOption.getSelectedItem().toString() + "'," +
                    "ProRated='" + cboxCheck3 + "'," +
                    "OpenDiscount='" + cboxCheck4 + "'" +
                    " where ID='" + DiscID + "'";
            db.execSQL(strsql);


            FillDataRV();
            RefreshRV();

        }
        catch (Exception ex){
            Log.e("TAG",ex.getMessage().toString());
        }



        }
        else{

            if (et_discID.getText().toString().isEmpty()){
                Toast.makeText(admin_pos_settings_discount.this, "PLEASE INPUT DISC ID", Toast.LENGTH_SHORT).show();
                et_discID.requestFocus();

            }

            else if (et_discName.getText().toString().isEmpty()){
                Toast.makeText(admin_pos_settings_discount.this, "PLEASE INPUT DISC ID", Toast.LENGTH_SHORT).show();
                et_discName.requestFocus();
            }
            else{

                settingsDB settingsDB = new settingsDB(getApplicationContext());
                settingsDB.insertDiscount(
                        et_discID.getText().toString(),//DiscountID
                        et_discName.getText().toString(),//DiscountName
                        et_discountPercent.getText().toString(),//DiscountAmount
                        et_discountComputation.getText().toString(),//DiscountComputation
                        cboxCheck,//DiscExcludeTax
                        sp_discountType.getSelectedItem().toString(),//DiscountType
                        FinalCategory, //  sp_category.getSelectedItem().toString(),//DiscCategory
                        sp_Label.getSelectedItem().toString(),//DiscLabel
                        et_minTransAmount.getText().toString(),//MinTransactionAmount
                        et_maxTransAmount.getText().toString(),//MaxTransactionAmount
                        et_maxDiscAmount.getText().toString(),//MaxDiscountAmount
                        cboxCheck2, //SalesExcludeTax,
                        sp_receiptOption.getSelectedItem().toString(),
                        cboxCheck3, // pro rated
                        cboxCheck4



                );

                // alertDialog.dismiss();

                FillDataRV();
                RefreshRV();


            }

//                        String DiscountID,String DiscountName,String DiscountAmount,String DiscountComputation,String DiscExcludeTax,
//                                String DiscountType,String DiscCategory,String DiscLabel,String MinTransactionAmount,String MaxTransactionAmount,
//                                String MaxDiscountAmount,String SalesExcludeTax



            Log.e("CBOX",cboxCheck);

        }
        db.close();
    }


    private void FillDataRV(){

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ArrayDiscountList.clear();
        Cursor itemListC = db.rawQuery("select * from DiscountList", null);

        int numberOfItem = itemListC.getCount();
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){



            //  int ItemNo=itemCounter+1;

            if (itemListC.getCount()!=0){
                while (itemListC.moveToNext()){

                    String DiscID=itemListC.getString(0);
                    String DiscountID=itemListC.getString(1);
                    String DiscountName=itemListC.getString(2);
                    String DiscountAmount=itemListC.getString(3);
                    String DiscountComputation=itemListC.getString(4);
                    String DiscountExcludeTax=itemListC.getString(5);
                    String DiscountType=itemListC.getString(6);
                    String DiscCategory=itemListC.getString(7);
                    String DiscLabel=itemListC.getString(8);
                    String MinTransactionAmount=itemListC.getString(9);
                    String MaxTransactionAmount=itemListC.getString(10);
                    String MaxDiscountAmount=itemListC.getString(11);
                    String SalesExcludeTax=itemListC.getString(12);






                    discount_model p0=new discount_model(DiscID,DiscountID,DiscountName,DiscountAmount,DiscountComputation,DiscountExcludeTax,DiscountType,DiscCategory,
                            DiscLabel,MinTransactionAmount,MaxTransactionAmount,MaxDiscountAmount,SalesExcludeTax);
                    //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

                    ArrayDiscountList.addAll(Arrays.asList(new discount_model[]{p0}));

                }
            }



        }
        //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";
        db.close();

    }
    private void RefreshRV(){


        rv_discList.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        rv_discList.setLayoutManager(layoutManager);
        mAdapter=new admin_pos_settings_discount.RecyclerviewAdapter(ArrayDiscountList,this);
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_discList.setAdapter(mAdapter);
        rv_discList.smoothScrollToPosition(rv_discList.getBottom());

    }
    
    
    private void getDiscountList(){
        SQLiteDatabase db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        Cursor discountListID = db.rawQuery("select * from DiscountList", null);
        if (discountListID.getCount()!=0){
            discountListID.moveToLast();
            ID=discountListID.getInt(0)+1;


        }
        else{
            ID=1;
        }


//                itemListC = db2.rawQuery("select sum(OrderPriceTotal),sum(OrderQty) from InvoiceReceiptItem", null);
//                String totalItem = "0";
//                String totalSubtract;
//                Cursor itemListC2 = db2.rawQuery("select sum(DiscAmount) from InvoiceReceiptItemFinalWDiscountTemp", null);
        db.close();
    }

    public void checkbox_clicked(View v)
    {

        if (cbox_ExcludeTax.isChecked()){
            cboxCheck="YES";
            cboxCheck3="NO";
            cboxCheck4="NO";
            cbox_ProRated.setChecked(false);
            cbox_OpenDiscount.setChecked(false);

        }
        else {
            cboxCheck="NO";
        }

    }

    public void checkbox2_clicked(View v)
    {

        if (cbox_salesExcludeTax.isChecked()){
            cboxCheck2="YES";
            cboxCheck3="NO";
            cboxCheck4="NO";
            cbox_ProRated.setChecked(false);
            cbox_OpenDiscount.setChecked(false);

        }
        else {
            cboxCheck2="NO";
        }

    }

    public void checkbox3_clicked(View v)
    {

        if (cbox_ProRated.isChecked()){
            cboxCheck3="YES";
            cboxCheck="NO";
            cboxCheck2="NO";
            cboxCheck4="NO";
            cbox_salesExcludeTax.setChecked(false);
            cbox_ExcludeTax.setChecked(false);
            cbox_OpenDiscount.setChecked(false);
        }
        else {
            cboxCheck3="NO";
        }

    }

    public void checkbox4_clicked(View v)
    {

        if (cbox_OpenDiscount.isChecked()){
            cboxCheck3="NO";
            cboxCheck="NO";
            cboxCheck2="NO";
            cboxCheck4="YES";

            cbox_salesExcludeTax.setChecked(false);
            cbox_ExcludeTax.setChecked(false);
            cbox_ProRated.setChecked(false);
            cbox_OpenDiscount.setChecked(true);
        }
        else {
            cboxCheck3="NO";
        }

    }



    String DiscID;
    String DiscountAmount;
    String DiscountComputation;
    String DiscountExcludeTax;
    String DiscountTypeS;
    String DiscLabel;
    String MinTransactionAmount;
    String MaxTransactionAmount;
    String MaxDiscountAmount;
    String SalesExcludeTax;

   
    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_pos_settings_discount.RecyclerviewAdapter.MyViewHolder>{
        List<discount_model> ArrayDiscountList;
        Context context;
        ArrayList<String> selectList = new ArrayList<>();
        private admin_pos_settings_discount.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<discount_model> ArrayDiscountList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.ArrayDiscountList = ArrayDiscountList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_pos_settings_discount.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_discount_layout,parent,false);



            admin_pos_settings_discount.RecyclerviewAdapter.MyViewHolder holder = new admin_pos_settings_discount.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }

       


        @Override
        public void onBindViewHolder(@NonNull admin_pos_settings_discount.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

//            holder.tv_No.setText(String.valueOf((AuditTrailList.get(position).getNo())));
//            holder.tv_TransactionNo.setText((String.valueOf(AuditTrailList.get(position).getTransactionNo())));
//            holder.tv_Activity.setText((String.valueOf(AuditTrailList.get(position).getActivity())));
//            holder.tv_User.setText((String.valueOf(AuditTrailList.get(position).getUser())));
//            holder.tv_Date.setText((String.valueOf(AuditTrailList.get(position).getDate())));
//            holder.tv_Time.setText((String.valueOf(AuditTrailList.get(position).getTime())));
//            holder.tv_UserShift.setText((String.valueOf(AuditTrailList.get(position).getDate())));

            holder.tv_category.setText(ArrayDiscountList.get(position).getDiscCategory());
            holder.tv_discID.setText(ArrayDiscountList.get(position).getDiscountID());
            holder.tv_desc.setText(ArrayDiscountList.get(position).getDiscountName());
            holder.FinalDiscID=ArrayDiscountList.get(position).getID();
            holder.FinalDiscountAmount=ArrayDiscountList.get(position).getDiscountAmount();
            holder.FinalDiscountComputation=ArrayDiscountList.get(position).getDiscountComputation();
            holder.FinalDiscountExcludeTax=ArrayDiscountList.get(position).getDiscountExcludeTax();
            holder.FinalDiscountType=ArrayDiscountList.get(position).getDiscountType();
            holder.FinalDiscLabel=ArrayDiscountList.get(position).getDiscLabel();
            holder.FinalMinTransactionAmount=ArrayDiscountList.get(position).getMinTransactionAmount();
            holder. FinalMaxTransactionAmount=ArrayDiscountList.get(position).getMaxTransactionAmount();
            holder.FinalMaxDiscountAmount=ArrayDiscountList.get(position).getMaxDiscountAmount();
            holder.FinalSalesExcludeTax=ArrayDiscountList.get(position).getSalesExcludeTax();














        }

        @Override
        public int getItemCount() {
            return ArrayDiscountList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_category;
            TextView tv_discID;
            TextView tv_desc;
            LinearLayout ll_discountSelect;

            private String FinalDiscID;
            private String FinalDiscountAmount;
            private String FinalDiscountComputation;
            private String FinalDiscountExcludeTax;
            private String FinalDiscountType;
            private String FinalDiscLabel;
            private String FinalMinTransactionAmount;
            private String FinalMaxTransactionAmount;
            private String FinalMaxDiscountAmount;
            private String FinalSalesExcludeTax;




            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_category = itemView.findViewById(R.id.tv_category);
                tv_discID = itemView.findViewById(R.id.tv_discID);
                tv_desc = itemView.findViewById(R.id.tv_desc);
                ll_discountSelect=itemView.findViewById(R.id.ll_discountSelect);

//             // String DiscID2="";
//                DiscountAmount="";
//                DiscountComputation="";
//                DiscountExcludeTax="";
//                DiscountType="";
//                DiscLabel="";
//                MinTransactionAmount="";
//                MaxTransactionAmount="";
//                MaxDiscountAmount="";
//                SalesExcludeTax="";



                ll_discountSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DiscID=FinalDiscID;
                        DiscountAmount= FinalDiscountAmount;
                        DiscountComputation=FinalDiscountComputation;
                        DiscountExcludeTax=FinalDiscountExcludeTax;
                        DiscountTypeS=FinalDiscountType;
                        DiscLabel=FinalDiscLabel;
                        MinTransactionAmount=FinalMinTransactionAmount;
                        MaxTransactionAmount=FinalMaxTransactionAmount;
                        MaxDiscountAmount=FinalMaxDiscountAmount;
                        SalesExcludeTax=FinalSalesExcludeTax;





                        //loadDiscount using id

                        // et_discName.setText();
                        //et_discName.setText();

                        loadDiscountInfo(Integer.parseInt(DiscID));






                        Log.e("CATEGORY",tv_discID.getText().toString());
                        Log.e("DiscID",DiscID);
                    }
                });



                // parentLayout = itemView.findViewById(R.id.linear_orderlist_layout2);



//                tv_itemText.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e("item id",tv_itemID.getText().toString());
//                        deleteID=Integer.parseInt(tv_itemID.getText().toString());
//                        updateItem();
//
//
//                    }
//                });



            }
        }
    }

    private void loadDiscountInfo(int id){

        db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
      //  ArrayDiscountList.clear();
        Cursor itemListC = db.rawQuery("select * from DiscountList where ID='"+id+"'", null);
        if (itemListC.getCount()!=0){
            while(itemListC.moveToNext()){
                et_discID.setText(itemListC.getString(1));
                et_discName.setText(itemListC.getString(2));
                et_discountPercent.setText(itemListC.getString(3));
                et_minTransAmount.setText(itemListC.getString(9));
                et_maxTransAmount.setText(itemListC.getString(10));
                et_maxDiscAmount.setText(itemListC.getString(11));
                sp_category.setSelection(getIndex(sp_category,itemListC.getString(7)));
                sp_discountType.setSelection(getIndex2(sp_discountType,itemListC.getString(6)));
                sp_Label.setSelection(getIndex3(sp_Label,itemListC.getString(8)));
                sp_receiptOption.setSelection(getIndex4(sp_receiptOption,itemListC.getString(13)));
                if (itemListC.getString(5).equalsIgnoreCase("YES")){
                    cbox_ExcludeTax.setChecked(true);
                }
               if(itemListC.getString(5).equalsIgnoreCase("NO")){
                    cbox_ExcludeTax.setChecked(false);
                }
             if(itemListC.getString(12).equalsIgnoreCase("YES")){
                    cbox_salesExcludeTax.setChecked(true);
                }
               if(itemListC.getString(12).equalsIgnoreCase("NO")){
                    cbox_salesExcludeTax.setChecked(false);
                }
               if(itemListC.getString(14).equalsIgnoreCase("YES")){
                    cbox_ProRated.setChecked(true);
                }
                if(itemListC.getString(14).equalsIgnoreCase("NO")){
                    cbox_ProRated.setChecked(false);
                }
                if(itemListC.getString(15).equalsIgnoreCase("YES")){
                    cbox_OpenDiscount.setChecked(true);
                }
                if(itemListC.getString(15).equalsIgnoreCase("NO")){
                    cbox_OpenDiscount.setChecked(false);
                }



                else{

                }



//                Log.d("TAG", "loadDiscountInfo: "+   sp_category.getCount());
//                Log.d("TAG", "loadDiscountInfo: " + itemListC.getString(7));

            }
        }
        db.close();


    }
    String  myString2;
    private int getIndex(Spinner spinner,String myString){

        if (myString.equalsIgnoreCase("REG")){
            myString2="REGULAR DISCOUNT";
        }
        else{
           myString2 = myString + " DISCOUNT";
        }

        for(int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(myString2)) {
                Log.e("TAG", "getIndex good: "+i);
                return i;

            }
        }
        Log.e("TAG", "getIndex fail: "+myString);
        return 0;
    }
    private int getIndex2(Spinner spinner,String myString){



        for(int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(myString)) {
                Log.e("TAG", "getIndex good: "+i);
                return i;

            }
        }
        Log.e("TAG", "getIndex fail: "+myString);
        return 0;
    }
    private int getIndex3(Spinner spinner,String myString){



        for(int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(myString)) {
                Log.e("TAG", "getIndex good: "+i);
                return i;

            }
        }
        Log.e("TAG", "getIndex fail: "+myString);
        return 0;
    }
    private int getIndex4(Spinner spinner,String myString){



        for(int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(myString)) {
                Log.e("TAG", "getIndex good: "+i);
                return i;

            }
        }
        Log.e("TAG", "getIndex fail: "+myString);
        return 0;
    }


    
}