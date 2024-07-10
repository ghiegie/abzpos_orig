package com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abztrakinc.ABZPOS.R;

import java.util.ArrayList;

public class invoice_button_function_recyclerviewadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<invoice_fragment_button_model> FunctionList;
    ArrayList<String>Selectlist = new ArrayList<String>();
    private Context Context;
    private OnClickListener onClickListener;

    public invoice_button_function_recyclerviewadapter(Context context, ArrayList<invoice_fragment_button_model> functionList,ArrayList<String>selectlist){
        this.Context = context;
        this.FunctionList = functionList;
        this.Selectlist = selectlist;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.invoice_fragment_button_function_layout,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.tv_buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return new MyViewHolder(view);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        final invoice_fragment_button_model button = FunctionList.get(position);
        if (holder instanceof  MyViewHolder){
            ((MyViewHolder)holder).tv_buttonNumber.setText(String.valueOf(button.getButtonID()));
            ((MyViewHolder)holder).tv_buttonDetails.setText(String.valueOf(button.getButtonName()));

            ((MyViewHolder)holder).tv_buttonDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Selectlist.clear();
                    Selectlist.add(String.valueOf(button.getButtonName()));
//                    cashier_invoice cashier_invoice = new cashier_invoice();
//                    cashier_invoice.check(String.valueOf(button.getButtonName()));

                    if (onClickListener != null) {
                        onClickListener.onClick(button.getButtonName());
                    }
                    else{
                        Log.e("TAG", "onClick: error" );
                        onClickListener.onClick(button.getButtonName());
                    }

                }
            });


        }




    }

    @Override
    public int getItemCount() {
        return FunctionList.size();
    }

    private static class MyViewHolder
            extends RecyclerView.ViewHolder {

        LinearLayout ll_linearLayout;
        TextView tv_buttonNumber,tv_buttonDetails;

        MyViewHolder(View view)
        {
            super(view);

            ll_linearLayout = view.findViewById(R.id.ll_linearLayout);
            tv_buttonNumber = view.findViewById(R.id.tv_buttonNumber);
            tv_buttonDetails = view.findViewById(R.id.tv_buttonDetails);

//            ll_linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(itemView.getContext(), "TEST", Toast.LENGTH_SHORT).show();
//
//                }
//            });









        }
    }



    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(String s);
    }



}
