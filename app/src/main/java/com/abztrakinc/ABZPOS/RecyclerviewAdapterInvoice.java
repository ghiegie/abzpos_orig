package com.abztrakinc.ABZPOS;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerviewAdapterInvoice extends RecyclerView.Adapter <RecyclerviewAdapterInvoice.MyViewHolder> {



    List<InvoiceItem> InvoiceItemList;

    ArrayList<String> invoiceSelectedItem = new ArrayList<>();
    String DB_NAME2 = "PosOutputDB.db";

    SQLiteDatabase db2;

    Cursor itemListC;
    DatabaseHandler myDb;
    String TransferId;
    //String order_ID;
   // String order_Name;
    String order_Qty;
   // String order_Price;
    String order_PriceTotal;
    String transaction_ID;
    String transaction_Time;
    String transaction_Date;
    String discount_type;
    String item_remarks;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat2;
    private String date,time;
    String readRefNumber;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private RecyclerView recyclerView2;
    private RecyclerView.Adapter mAdapter2;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;



    private int checkedPosition = 0;

    Context context;


    public RecyclerviewAdapterInvoice(List<InvoiceItem> InvoiceItemList,ArrayList<String> invoiceSelectedItem, Context context) {
        // public RecyclerviewAdapter(List<orderItem> orderItemList,ArrayList<String> selectList, Context context) {
        this.InvoiceItemList = InvoiceItemList;
        this.invoiceSelectedItem = invoiceSelectedItem;
        this.context = context;

    }

    public void SetInvoiceItem(ArrayList<InvoiceItem> invoiceItem){
        this.InvoiceItemList = new ArrayList<>();
        this.InvoiceItemList = invoiceItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item_list,parent,false);



        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }
    int selectedItemPos = -1;
    int lastItemSelectedPos = -1;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final InvoiceItem model = new InvoiceItem(
                InvoiceItemList.get(position).getItemId(),
                InvoiceItemList.get(position).getItemName(),
                InvoiceItemList.get(position).getItemPrice(),
                InvoiceItemList.get(position).getItemQtyAvailable(),
                InvoiceItemList.get(position).getVatIndicator(),
                InvoiceItemList.get(position).getPriceOverride()
        );

        holder.tv_itemName.setText(InvoiceItemList.get(position).getItemName());
        holder.tv_vatIndicator.setText(InvoiceItemList.get(position).getVatIndicator());
//        String id = InvoiceItemList.get(position).getItemId();
       holder.bind(InvoiceItemList.get(position));
       String id = InvoiceItemList.get(position).getItemId();

        String  order_ID = InvoiceItemList.get(position).getItemId();
        String  order_Name=InvoiceItemList.get(position).getItemName();
        String  order_Price=InvoiceItemList.get(position).getItemPrice();


       holder.btn_addItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               // addInvoiceItem();
//               //TransferId = "order ID: " +order_ID;
//               //Toast.makeText(context,order_ID, Toast.LENGTH_SHORT).show();
//               invoiceSelectedItem.add("Asdasdasd");
//               cashier_invoice cashier_invoice = new cashier_invoice();
//               cashier_invoice.showingTestMessage(context);
//              // Toast.makeText(context, invoiceSelectedItem.toString(), Toast.LENGTH_SHORT).show();




           }
       });





    }
    private void refreshview(){

    }




    @Override
    public int getItemCount() {
        return InvoiceItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemName;
        TextView tv_itemPrice;
        TextView tv_ItemAvailable;
        TextView tv_vatIndicator;

        ImageView iv_check;
       // LinearLayout linearRegDisc,linearSCD;
        Button btn_subtractItem,btn_addItem;


        TextView tv_itemSubtotal;
        CardView parentLayout;
        LinearLayout linearLayoutButton;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_itemName = itemView.findViewById(R.id.tv_itemName);
            tv_itemPrice = itemView.findViewById(R.id.tv_itemPrice);
            tv_ItemAvailable = itemView.findViewById(R.id.tv_ItemAvailable);
            tv_vatIndicator = itemView.findViewById(R.id.tv_VatIndicator);
            iv_check = itemView.findViewById(R.id.iv_check);
            parentLayout = itemView.findViewById(R.id.linear_Invoicelist_layout);
            linearLayoutButton = itemView.findViewById(R.id.linearlayoutInsert);
            btn_subtractItem = itemView.findViewById(R.id.btn_subtractItem);
            btn_addItem = itemView.findViewById(R.id.btn_addItem);

        }

        void bind(final InvoiceItem invoiceItem){
            if (checkedPosition == -1){
                    linearLayoutButton.setVisibility(View.GONE);
            }else{
                if (checkedPosition == getAdapterPosition()){
                   // linearLayoutButton.setVisibility(View.VISIBLE);
                }else{
                    linearLayoutButton.setVisibility(View.GONE);
                }
            }
            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linearLayoutButton.setVisibility(View.VISIBLE);
                    if (checkedPosition != getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        //getSelected();
                        checkedPosition = getAdapterPosition();

                    }
                }
            });
        }



    }
    public InvoiceItem getSelected(Context context){
        if (checkedPosition != -1){
            Toast.makeText(context, InvoiceItemList.get(checkedPosition).toString(), Toast.LENGTH_SHORT).show();
            return InvoiceItemList.get(checkedPosition);
        }
        return  null;
    }





    private void readReferenceNumber() {

        //primary key 00000001

        // int readPK;

        db2 = context.openOrCreateDatabase(DB_NAME2, Context.MODE_PRIVATE, null);
        itemListC = db2.rawQuery("select * from InvoiceReceiptTotal ", null);
        if (itemListC.getCount() == 0) {

            int origRefNumber = 1;
            String formatted = String.format("%07d", origRefNumber);
            readRefNumber = formatted;
        } else {

            itemListC = db2.rawQuery("SELECT * FROM InvoiceReceiptTotal", null);
            //while(itemListC.moveToLast()){
            itemListC.moveToLast();
            int origRefNumber = 1;

            int readPK = Integer.parseInt(itemListC.getString(0));

            int incrementPK = readPK + 1;
            String incrementPKString = String.format("%07d", incrementPK);

            readRefNumber = incrementPKString;


            // }
        }
        itemListC.close();
        db2.close();


    }


    private void insertInvoiceReceipt() {





    }



}
