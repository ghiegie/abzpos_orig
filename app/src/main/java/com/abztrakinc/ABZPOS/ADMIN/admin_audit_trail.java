package com.abztrakinc.ABZPOS.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abztrakinc.ABZPOS.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class admin_audit_trail extends AppCompatActivity {

    EditText et_from,et_to;
    Button btn_setDate;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_audit_trail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        et_from = findViewById(R.id.et_from);
//        et_to = findViewById(R.id.et_to);
        btn_setDate = findViewById(R.id.btn_setDate);

       ImageButton ib_return=findViewById(R.id.ib_return);
         ib_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        et_from=(EditText) findViewById(R.id.et_from);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabelFrom();
            }
        };
        et_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(admin_audit_trail.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_to=(EditText) findViewById(R.id.et_to);
        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabelTo();
            }
        };
        et_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(admin_audit_trail.this,date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });











         showSummary();


//        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);
//        ScrollView scrollView = findViewById(R.id.scrollView);
//
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                horizontalScrollView.setScaleX(1.5f);
//                horizontalScrollView.setScaleY(1.5f);
//            }
//        });
//
//// Zoom out
//        scrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                horizontalScrollView.setScaleX(1.0f);
//                horizontalScrollView.setScaleY(1.0f);
//            }
//        });



    }


    private void updateLabelFrom(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_from.setText(dateFormat.format(myCalendar.getTime()));
    }
    private void updateLabelTo(){
        String myFormat="yyyy-MM-dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        et_to.setText(dateFormat.format(myCalendar.getTime()));
    }


    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter mAdapter;
    List<admin_audit_trail_item> AuditTrailList = new ArrayList<>();
    ArrayList<String> selectList = new ArrayList<>();

    public class RecyclerviewAdapter extends RecyclerView.Adapter <admin_audit_trail.RecyclerviewAdapter.MyViewHolder>{
        List<admin_audit_trail_item> AuditTrailList;
        Context context;



        ArrayList<String> selectList = new ArrayList<>();
        private admin_audit_trail.RecyclerviewAdapter.MyViewHolder holder;
        private int position;

        public RecyclerviewAdapter(List<admin_audit_trail_item> AuditTrailList, ArrayList<String> selectList, Context context) {
            // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
            this.AuditTrailList = AuditTrailList;
            this.selectList = selectList;
            this.context = context;

        }

        @NonNull
        @Override
        public admin_audit_trail.RecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audit_trail_layout,parent,false);



            admin_audit_trail.RecyclerviewAdapter.MyViewHolder holder = new admin_audit_trail.RecyclerviewAdapter.MyViewHolder(view);
            return holder;
        }



        @Override
        public void onBindViewHolder(@NonNull admin_audit_trail.RecyclerviewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            this.holder = holder;
            this.position = position;
            //   final HeaderFooterItem model = new HeaderFooterItem(HeaderFooterList.get(position).getItemID(),HeaderFooterList.get(position).getItemText(),HeaderFooterList.get(position).getItemDoubleWidth());

            holder.tv_No.setText(String.valueOf((AuditTrailList.get(position).getNo())));
            holder.tv_TransactionNo.setText((String.valueOf(AuditTrailList.get(position).getTransactionNo())));
            holder.tv_SINo.setText((String.valueOf(AuditTrailList.get(position).getInvoice())));
            holder.tv_Activity.setText((String.valueOf(AuditTrailList.get(position).getActivity())));
            holder.tv_User.setText((String.valueOf(AuditTrailList.get(position).getUser())));
            holder.tv_totalAmount.setText(String.valueOf(AuditTrailList.get(position).getTotalAmount()));
            holder.tv_Date.setText((String.valueOf(AuditTrailList.get(position).getDate())));
            holder.tv_Time.setText((String.valueOf(AuditTrailList.get(position).getTime())));
            holder.tv_UserShift.setText((String.valueOf(AuditTrailList.get(position).getUserShift())));





        }

        @Override
        public int getItemCount() {
            return AuditTrailList.size();
        }
        public List<String> getList() {
            return selectList;
        }



        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_No;
            TextView tv_TransactionNo;
            TextView tv_Activity;
            TextView tv_User;
            TextView tv_totalAmount;
            TextView tv_Date;
            TextView tv_UserShift;
            TextView tv_Time;
            TextView tv_SINo;
            //CardView parentLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_No = itemView.findViewById(R.id.tv_ItemNumber);
                tv_TransactionNo = itemView.findViewById(R.id.tv_transactionNo);
                tv_Activity = itemView.findViewById(R.id.tv_activity);
                tv_User = itemView.findViewById(R.id.tv_user);
                tv_totalAmount = itemView.findViewById(R.id.tv_totalAmount);
                tv_Date = itemView.findViewById(R.id.tv_date);
                tv_Time = itemView.findViewById(R.id.tv_time);
                tv_UserShift = itemView.findViewById(R.id.tv_userShift);
                tv_SINo = itemView.findViewById(R.id.tv_SINo);

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

    private void showSummary(){
        fillOrderList();
        refreshRecycleview();

    }
    private ArrayList<String> TransactionNoList;
//    private ArrayList<String>  ActivityList;
//    private ArrayList<String>  UserList;

    private void fillOrderList() {
        TransactionNoList = new ArrayList<>();

        AuditTrailList.clear();

        CheckItemDatabase2();
        int numberOfItem = itemListC.getCount();
        for (int itemCounter = 0;itemCounter<numberOfItem;itemCounter++){


            int ItemNo=itemCounter+1;
            SQLiteDatabase db2 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);

            String TransactionNo=TransactionNoList.get(itemCounter);
            Cursor checkItem=db2.rawQuery("select * from InvoiceReceiptTotal where TransactionID='"+TransactionNo+"'", null);
            Cursor checkSI=db2.rawQuery("select * from OfficialReceipt where TransactionID='"+TransactionNo+"'", null);
            String SINo="";
            if (checkItem.getCount()!=0){
                while (checkItem.moveToNext()){



                String Activity=checkItem.getString(8).toUpperCase(Locale.ROOT);
                    String User=checkUser(checkItem.getString(11));
                    if(checkSI.getCount()!=0){
                        checkSI.moveToFirst();
                         SINo=checkSI.getString(0);
                    }

                    Log.d("TAG", checkUser(checkItem.getString(11)));
                //String User=checkItem.getString(11);
              //  String totalAmount= checkItem.getString(4);
                String Date=checkItem.getString(9);
                String Time=checkItem.getString(10);
                String Shift=checkItem.getString(12);
                String totalAmt = checkItem.getString(4);
                try{
                    if (Shift.equalsIgnoreCase("null")){
                        Shift="";
                    }
                }catch (Exception ex){
                    Shift="";
                }





                admin_audit_trail_item p0=new admin_audit_trail_item(ItemNo,TransactionNo,SINo,Activity,User,totalAmt,Date,Time,Shift);
                //  finalReceiptDisplay=finalReceiptDisplay+ "\n" + itemText;

                    AuditTrailList.addAll(Arrays.asList(new admin_audit_trail_item[]{p0}));

            }
            }
db2.close();


        }
      //  tv_receiptDisplay.setText(finalReceiptDisplay);
        //finalReceiptDisplay="";

        Toast.makeText(this, "summary", Toast.LENGTH_SHORT).show();

    }
    Cursor  itemListC;


    private void CheckItemDatabase2() {
        TransactionNoList.clear();
        SQLiteDatabase db2 = this.openOrCreateDatabase("PosOutputDB.db", Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptTotal", null);
        if (itemListC.getCount() == 0) {
            // Toast.makeText(getActivity(), "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        while(itemListC.moveToNext()){
            TransactionNoList.add(itemListC.getString(0));
        }
        itemListC.close();
        db2.close();


    }

    private String checkUser(String accountNumber) {
       // TransactionNoList.clear();
        String accountName="";
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
        mAdapter=new admin_audit_trail.RecyclerviewAdapter(AuditTrailList,selectList,this.getApplicationContext());
        // mAdapter=new RecyclerviewAdapter(orderItemList,selectList,getActivity());
        rv_auditTrailList.setAdapter(mAdapter);

        //invoice item list



    }





}
