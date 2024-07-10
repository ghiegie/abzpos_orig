package com.abztrakinc.ABZPOS.ADMIN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_plu_model;
import com.abztrakinc.ABZPOS.R;

import java.util.ArrayList;
import java.util.List;

public class activity_admin_bank_payment_recyclerview extends RecyclerView.Adapter<activity_admin_bank_payment_recyclerview.MyViewHolder>{

    private ArrayList<activity_admin_bank_payment_model> ButtonList;
    private android.content.Context Context;
    ArrayList<String>Selectlist = new ArrayList<String>();
    private activity_admin_bank_payment_recyclerview.MyViewHolder holder;

    private int position;

    public activity_admin_bank_payment_recyclerview(android.content.Context context, ArrayList<activity_admin_bank_payment_model> buttonList) {

        this.Context = context;
        this.ButtonList = buttonList;



    }

    @NonNull
    @Override
    public activity_admin_bank_payment_recyclerview.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Context).inflate(R.layout.activity_admin_bank_payment_recyclerview_layout,parent,false);
        //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_fragment_button_function_layout,parent,false);
        activity_admin_bank_payment_recyclerview.MyViewHolder holder = new activity_admin_bank_payment_recyclerview.MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull activity_admin_bank_payment_recyclerview.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        this.holder = holder;
        this.position = position;



        final activity_admin_bank_payment_model button = ButtonList.get(position);

//        String ButtonPrefix = button.getButtonID();
//        String ButtonName=ButtonList.get(position).getItemName();
//        String PriceOverrides=ButtonList.get(position).getPriceOverride();
//        String itemCode=ButtonList.get(position).getItemCode();
//        holder.tv_buttonNumber.setText("PLU + "+String.valueOf(ButtonPrefix) + " + Ent");
//        holder.tv_buttonDetails.setText(String.valueOf(ButtonName));

        holder.tv_idNo.setText(String.valueOf(ButtonList.get(position).IDNo));
        holder.tv_itemName.setText(ButtonList.get(position).ItemName);
        holder.tv_bankName.setText(ButtonList.get(position).BankName);
        holder.tv_cardType.setText(ButtonList.get(position).CardType);




    }

    // Callback function interface
    public interface OnClickListener {
        void onClick(String buttonPrefix); // Updated to accept buttonPrefix value
    }

    @Override
    public int getItemCount() {
        return ButtonList.size();
    }
    public List<String> getList() {
        return Selectlist;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

//
//        LinearLayout ll_linearLayout;
//        TextView tv_buttonNumber,tv_buttonDetails;

        TextView tv_idNo;
        TextView tv_itemName;
        TextView tv_bankName;
        TextView tv_cardType;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//
            tv_idNo = itemView.findViewById(R.id.tv_idNo);
            tv_itemName = itemView.findViewById(R.id.tv_itemName);
            tv_bankName = itemView.findViewById(R.id.tv_bankName);
            tv_cardType = itemView.findViewById(R.id.tv_cardType);
//            ll_linearLayout = itemView.findViewById(R.id.ll_linearLayout);
//            tv_buttonNumber = itemView.findViewById(R.id.tv_buttonNumber);
//            tv_buttonDetails = itemView.findViewById(R.id.tv_buttonDetails);




        }
    }


}
