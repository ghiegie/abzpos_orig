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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class admin_discount_summary extends AppCompatActivity {

    Spinner sp_type;
    Button btn_generate;
    TextView tv_discountType;
    ArrayList<String> DiscountType = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dscount_summary);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DiscountType.add("SCD");
        DiscountType.add("PWD");
        sp_type =findViewById(R.id.sp_type);
        tv_discountType= findViewById(R.id.tv_discountType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,DiscountType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(sp_type.getSelectedItemPosition()==0){
                    tv_discountType.setText("Senior Citizen Sales Book");
                }
                else{
                    tv_discountType.setText("PWD Sales Book");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_generate = findViewById(R.id.btn_generate);
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sp_type.getSelectedItemPosition()==0){
                    CheckItemDatabaseDiscountList();
                    showSummary("SCD");
                }
                else{
                    CheckItemDatabaseDiscountList();
                    showSummary("PWD");
                }


            }
        });


    }

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    List<admin_discount_summary_item> AuditTrailList = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();

    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_discount_summary.RecyclerviewAdapter.MyViewHolder>{
        List<admin_discount_summary_item> AuditTrailList;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_discount_summary.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<admin_discount_summary_item> AuditTrailList, ArrayList<String> selectList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.AuditTrailList = AuditTrailList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_discount_summary.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_summary_layout,parent,false);



            admin_discount_summary.RecyclerviewAdapter.MyViewHolder holder = new admin_discount_summary.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_discount_summary.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

//            holder.tv_No.setText(String.valueOf((AuditTrailList.get(position).getNo())));
//            holder.tv_TransactionNo.setText((String.valueOf(AuditTrailList.get(position).getTransactionNo())));
//            holder.tv_Activity.setText((String.valueOf(AuditTrailList.get(position).getActivity())));
//            holder.tv_User.setText((String.valueOf(AuditTrailList.get(position).getUser())));
//            holder.tv_totalAmount.setText(String.valueOf(AuditTrailList.get(position).getTotalAmount()));
//            holder.tv_Date.setText((String.valueOf(AuditTrailList.get(position).getDate())));
//            holder.tv_Time.setText((String.valueOf(AuditTrailList.get(position).getTime())));
//            holder.tv_UserShift.setText((String.valueOf(AuditTrailList.get(position).getUserShift())));
            holder.tv_ItemNumber.setText(String.valueOf((AuditTrailList.get(position).getNo())));
            holder.tv_date.setText(String.valueOf(AuditTrailList.get(position).getDate()));
            holder.tv_name.setText(String.valueOf(AuditTrailList.get(position).getName()));
            holder.tv_IDNo.setText(String.valueOf(AuditTrailList.get(position).getIDNo()));
            holder.tv_SINo.setText(String.valueOf(AuditTrailList.get(position).getSINo()));
            holder.tv_VATExempt.setText(String.valueOf(AuditTrailList.get(position).getVatExempt()));
            holder.tv_Discount.setText(String.valueOf(AuditTrailList.get(position).getDiscount()));





        }

        @Override
        public int getItemCount() {
            return AuditTrailList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_ItemNumber;
            TextView tv_date;
            TextView tv_name;
            TextView tv_IDNo;
            TextView tv_SINo;
            TextView tv_VATExempt;
            TextView tv_Discount;

            //CardView parentLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_ItemNumber = itemView.findViewById(R.id.tv_ItemNumber);
                tv_date = itemView.findViewById(R.id.tv_Date);
                tv_name = itemView.findViewById(R.id.tv_Name);
                tv_IDNo = itemView.findViewById(R.id.tv_IDNo);
                tv_SINo = itemView.findViewById(R.id.tv_SINo);
                tv_VATExempt = itemView.findViewById(R.id.tv_VATExempt);
                tv_Discount = itemView.findViewById(R.id.tv_Discount);


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


    private void showSummary(String type){
        fillOrderList(type);
        refreshRecycleview();

    }
    private ArrayList<String> TransactionNoList;


    private void fillOrderList(String type) {
        Log.e("TEST",String.valueOf(123));
        TransactionNoList = new ArrayList<>();
        AuditTrailList.clear();

        CheckItemDatabase2();
//        int numberOfItem = itemListC.getCount();
//        int numberOfItem = TransactionNoList.size();
//        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){
//
//
//            int ItemNo=itemCounter+1;
//            Log.e("ITEMNO",String.valueOf(ItemNo));
            SQLiteDatabase db2 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
            SQLiteDatabase settingsDB = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);

            //checking of data in InvoiceReceipt
            Cursor checkData = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where DiscountType like '%"+type+"%'", null);
           // Cursor checkFinalTransactionReport = db2.rawQuery("select * from FinalTransactionReport", null);
            if (checkData.getCount()!=0){
                int ItemNo=0;
                while (checkData.moveToNext()){

                    ItemNo++;
                    Log.e("TRANSCTION",checkData.getString(0));
                    Cursor checkFinalTransactionReport = db2.rawQuery("select * from FinalTransactionReport where TransactionID='"+checkData.getString(0)+"'", null);
                    if (checkFinalTransactionReport.getCount()!=0){
                        while(checkFinalTransactionReport.moveToNext()){
                            admin_discount_summary_item p0=new admin_discount_summary_item(ItemNo,checkFinalTransactionReport.getString(12),checkData.getString(14),checkData.getString(15),checkFinalTransactionReport.getString(1),checkFinalTransactionReport.getString(6),checkData.getString(11));
                            //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;
                            AuditTrailList.addAll(Arrays.asList(new admin_discount_summary_item[]{p0}));
                        }
                    }


                }

            }


       //     admin_discount_summary_item p0=new admin_discount_summary_item(ItemNo,"2","2","2",TransactionNoList.get(itemCounter),"2","2");
            //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;




//
//        }



//        admin_discount_summary_item p0=new admin_discount_summary_item(1,"2","2","2","2","2","2");
//        //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;
//
//        AuditTrailList.addAll(Arrays.asList(new admin_discount_summary_item[]{p0}));
    }
    Cursor  itemListC;

        int finalNumberTransaction=0;
    private void CheckItemDatabase2() {
        TransactionNoList.clear();
        SQLiteDatabase db2 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        for(int x=0;x<DiscountList.size();x++){
          Log.e("DISCOUNT NAME",DiscountList.get(x));
            itemListC = db2.rawQuery("select * from InvoiceReceiptItemFinalWDiscount where DiscountType='"+DiscountList.get(x)+"'", null);
            if (itemListC.getCount() == 0) {
               // Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
            }
            while(itemListC.moveToNext()){
                finalNumberTransaction++;
                TransactionNoList.add(itemListC.getString(0));
            }
            itemListC.close();
        }



        db2.close();


    }
    private ArrayList<String> DiscountList = new ArrayList<>();;
    private void CheckItemDatabaseDiscountList() {
        DiscountList.clear();
        SQLiteDatabase db2 = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor itemlistc2 = db2.rawQuery("select * from DiscountList where DiscCategory='SCD'", null);
        if (itemlistc2.getCount() == 0) {
           // Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        while(itemlistc2.moveToNext()){
            DiscountList.add(itemlistc2.getString(1));
        }
        itemlistc2.close();
        db2.close();


    }

    private String checkUser(String accountNumber) {
        // TransactionNoList.clear();
        String accountName="asd";
        SQLiteDatabase db2 = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from Accountsettings where accountNumber='"+accountNumber+"'", null);
        if (itemListC.getCount() == 0) {
            // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();

        }
        else{
            itemListC.moveToFirst();
            accountName = accountNumber + " - " +itemListC.getString(2);
            Log.d("TAG", accountName);
            // return accountName;

        }
//        while(itemListC.moveToFirst()){
//           // TransactionNoList.add(itemListC.getString(0));
//        }

        itemListC.close();
        db2.close();
        return accountName;



    }



    RecyclerView rv_auditTrailList;
    public void refreshRecycleview(){
        rv_auditTrailList = findViewById(R.id.rv_auditTrailList);
        rv_auditTrailList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        rv_auditTrailList.setLayoutManager(layoutManager);
        mAdapter=new admin_discount_summary.RecyclerviewAdapter(AuditTrailList,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_auditTrailList.setAdapter(mAdapter);

        //invoice item list



    }
}