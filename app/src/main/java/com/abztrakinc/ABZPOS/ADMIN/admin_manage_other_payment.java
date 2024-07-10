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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.R;
import com.abztrakinc.ABZPOS.discount_model;
import com.abztrakinc.ABZPOS.other_payment_model;
import com.abztrakinc.ABZPOS.settingsDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class admin_manage_other_payment extends AppCompatActivity {
    EditText et_PaymentAutoID,et_OtherPayment;
    Spinner sp_type,sp_changeType;
    CheckBox cbox_allowReference,cbox_allowUserDetails;
    ImageButton btn_Save,btn_removeItem;
    String cboxCheck2="NO",cboxCheck3="NO";
    RecyclerView rv_otherPaymentList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_other_payment);

        sp_type= findViewById(R.id.sp_type);
        sp_changeType= findViewById(R.id.sp_changeType);
        getOtherPaymentList();
        getTypeList();
        getChangeList();

        et_PaymentAutoID=findViewById(R.id.et_PaymentAutoID);
        et_OtherPayment = findViewById(R.id.et_OtherPayment);
        et_PaymentAutoID.setText(String.valueOf(ID));
        btn_Save = findViewById(R.id.btn_addItem);
        cbox_allowReference=findViewById(R.id.cbox_allowReference);
        cbox_allowUserDetails=findViewById(R.id.cbox_allowUserDetails);

        rv_otherPaymentList = findViewById(R.id.rv_otherPaymentList);











        buttonSave();
        buttonDelete();

        FillDataRV();
        RefreshRV();











    }


    public void allowReference_clicked(View v) {

        if (cbox_allowReference.isChecked()){
            cboxCheck2="YES";
        }
        else {
            cboxCheck2="NO";
        }

    }
    public void allowUserDetails_clicked(View v) {

        if (cbox_allowUserDetails.isChecked()){
            cboxCheck3="YES";
        }
        else {
            cboxCheck3="NO";
        }

    }

    ArrayList<String> OtherPaymentList=new ArrayList<String>();
    ArrayList<String> TypeList=new ArrayList<String>();
    ArrayList<String> ChangeList=new ArrayList<String>();
    //ArrayList<other_payment_model> ArrayOtherPaymentList = new ArrayList<>();
    List<other_payment_model> ArrayOtherPaymentList = new ArrayList<>();
    int ID;
    private void getOtherPaymentList(){
        OtherPaymentList.clear();
        SQLiteDatabase db = this.openOrCreateDatabase("settings.db", Context.MODE_PRIVATE, null);
        Cursor BankID = db.rawQuery("select ID from OtherPayment", null);
        Cursor BankName = db.rawQuery("select * from OtherPayment", null);
        if (BankID.getCount()!=0){
            if (BankID.moveToLast()){
                ID=BankID.getInt(0)+1;
            }
            while ((BankName.moveToNext())){
                OtherPaymentList.add(BankName.getString(1));
            }




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
    private void getTypeList(){
        TypeList.clear();
        TypeList.add("VOUCHER TYPE");
        TypeList.add("REGULAR TYPE");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,TypeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapter);
        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private void getChangeList(){
        ChangeList.clear();
        ChangeList.add("No Change");
        ChangeList.add("ALLOW CHANGE -> OTHER INCOME");
        ChangeList.add("ALLOW CHANGE -> NORMAL CASH");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ChangeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_changeType.setAdapter(adapter);
        sp_changeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //    EditText et_discountName = alertLayout.findViewById(R.id.et_discountName);
                //   EditText et_discountAmount=alertLayout.findViewById(R.id.et_discountAmount);

                // et_discountName.setText(DiscountListName.get(spinner_bankName.getSelectedItemPosition()));
                // et_discountAmount.setText(DiscountListAmount.get(spinner_bankName.getSelectedItemPosition()));
                //=Integer.valueOf(DiscountList.get(spinner_bankName.getSelectedItemPosition()));




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    private void buttonSave(){
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_OtherPayment.getText().length()==0){
                    Toast.makeText(admin_manage_other_payment.this, "PLEASE INPUT PAYMENT NAME", Toast.LENGTH_SHORT).show();
                    et_OtherPayment.requestFocus();
                }else{

                    Log.d("TAG",String.valueOf(et_OtherPayment.getText().toString()));
                    Log.d("TAG",String.valueOf(sp_type.getSelectedItemId()));
                    Log.d("TAG",String.valueOf(sp_changeType.getSelectedItemId()));
                    Log.d("TAG",String.valueOf(cboxCheck2));
                    Log.d("TAG",String.valueOf(cboxCheck3));




                    settingsDB settingsDB = new settingsDB(getApplicationContext());
                    settingsDB.insertOtherPayment(et_OtherPayment.getText().toString(), cboxCheck2, cboxCheck3,sp_type.getSelectedItemId(),sp_changeType.getSelectedItemId());
                  //  Toast.makeText(this, "Other Payment Added", Toast.LENGTH_SHORT).show();
                    et_OtherPayment.getText().clear();

                   // getOtherPaymentList();
                    FillDataRV();
                    RefreshRV();
                    RefreshID();


                }


            }
        });
    }
    private void buttonDelete(){
        btn_removeItem = findViewById(R.id.btn_removeItem);
        btn_removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePayment(FinalID);

              //  getOtherPaymentList();
                FillDataRV();
                RefreshRV();
                RefreshID();
            }
        });

    }

    String DB_NAME = "settings.db";
    //showing recycle view
    private void FillDataRV(){

       SQLiteDatabase db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        ArrayOtherPaymentList.clear();
        Cursor itemListC = db.rawQuery("select * from OtherPayment", null);

        int numberOfItem = itemListC.getCount();
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){



            //  int ItemNo=itemCounter+1;

            if (itemListC.getCount()!=0){
                while (itemListC.moveToNext()){

                    int ID=itemListC.getInt(0);
                    String PaymentName=itemListC.getString(1);
                    String AllowReference=itemListC.getString(2);
                    String AllowDetails=itemListC.getString(3);
                    int PaymentType=itemListC.getInt(4);
                    int ChangeType=itemListC.getInt(5);
                    Log.d("TAG",String.valueOf(ID));







                    other_payment_model p0=new other_payment_model(ID,PaymentName,AllowReference,AllowDetails,PaymentType,ChangeType);
                    //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

                    ArrayOtherPaymentList.addAll(Arrays.asList(new other_payment_model[]{p0}));

                }
            }



        }
        //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";
        db.close();

    }
    private void RefreshRV(){


        rv_otherPaymentList.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        layoutManager.removeAllViews();
        rv_otherPaymentList.setLayoutManager(layoutManager);
        mAdapter=new admin_manage_other_payment.RecyclerviewAdapter(ArrayOtherPaymentList,this);
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_otherPaymentList.setAdapter(mAdapter);
        rv_otherPaymentList.smoothScrollToPosition(rv_otherPaymentList.getBottom());

    }
    private void deletePayment(int ID){

        SQLiteDatabase db2 = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        String strsql = "DELETE FROM OtherPayment where ID='"+ID+"'";
        db2.execSQL(strsql);
        db2.close();
    }
    private void RefreshID(){
        SQLiteDatabase db = this.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
       // ArrayOtherPaymentList.clear();
        Cursor itemListC = db.rawQuery("select ID from OtherPayment", null);
        if (itemListC.getCount()!=0){
            if (itemListC.moveToLast()){
                et_PaymentAutoID.setText(itemListC.getString(0));
            }
        }

        db.close();

    }


    int FinalID;
    String FinalName;
    String FinalAllowRef;
    String FinalAllowDetails;
    int FinalPaymentType;
    int FinalChangeType;



    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_manage_other_payment.RecyclerviewAdapter.MyViewHolder>{
        List<other_payment_model> ArrayOtherPaymentList;
        Context context;
        ArrayList<String> selectList = new ArrayList<>();
        private admin_manage_other_payment.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<other_payment_model> ArrayOtherPaymentList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.ArrayOtherPaymentList = ArrayOtherPaymentList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_manage_other_payment.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_other_payment,parent,false);



            admin_manage_other_payment.RecyclerviewAdapter.MyViewHolder holder = new admin_manage_other_payment.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }




        @Override
        public void onBindViewHolder(@NonNull admin_manage_other_payment.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
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

            holder.tv_id.setText(String.valueOf(ArrayOtherPaymentList.get(position).getID()));
            holder.tv_paymentName.setText(ArrayOtherPaymentList.get(position).getPaymentName());
            holder.tv_allowReference.setText(ArrayOtherPaymentList.get(position).getAllowReference());
            holder.tv_allowDetails.setText(ArrayOtherPaymentList.get(position).getAllowDetails());
            holder.tv_paymentType.setText(String.valueOf(ArrayOtherPaymentList.get(position).getPaymentType()));
            holder.tv_changeType.setText(String.valueOf(ArrayOtherPaymentList.get(position).getChangeType()));

            holder.opID=((ArrayOtherPaymentList.get(position).getID()));
            holder.opName=(ArrayOtherPaymentList.get(position).getPaymentName());
            holder.opReference=(ArrayOtherPaymentList.get(position).getAllowReference());
            holder.opDetails=(ArrayOtherPaymentList.get(position).getAllowDetails());
            holder.opType=((ArrayOtherPaymentList.get(position).getPaymentType()));
            holder.opChangetype=(ArrayOtherPaymentList.get(position).getChangeType());

















        }

        @Override
        public int getItemCount() {
            return ArrayOtherPaymentList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_id;
            TextView tv_paymentName;
            TextView tv_allowReference;
            TextView tv_allowDetails;
            TextView tv_paymentType;
            TextView tv_changeType;
            LinearLayout ll_otherPaymentSelect;

            private int opID;
            private String opName;
            private String opReference;
            private String opDetails;
            private int opType;
            private int opChangetype;





            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_id = itemView.findViewById(R.id.tv_id);
                tv_paymentName = itemView.findViewById(R.id.tv_name);
                tv_allowReference = itemView.findViewById(R.id.tv_reference);
                tv_allowDetails=itemView.findViewById(R.id.tv_details);
                tv_paymentType=itemView.findViewById(R.id.tv_paymentType);
                tv_changeType=itemView.findViewById(R.id.tv_changeType);
                ll_otherPaymentSelect=itemView.findViewById(R.id.ll_otherPaymentSelect);





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



                ll_otherPaymentSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                         FinalID=opID;
                         FinalName=opName;
                         FinalAllowRef=opReference;
                         FinalAllowDetails=opDetails;
                         FinalPaymentType=opType;
                         FinalChangeType=opChangetype;

                         Log.d("TAG",String.valueOf(FinalID));
                        Log.d("TAG",String.valueOf(FinalName));
                        Log.d("TAG",String.valueOf(FinalAllowRef));
                        Log.d("TAG",String.valueOf(FinalAllowDetails));
                        Log.d("TAG",String.valueOf(FinalPaymentType));
                        Log.d("TAG",String.valueOf(FinalChangeType));





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




}