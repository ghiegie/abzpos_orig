package com.abztrakinc.ABZPOS.ADMIN;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview.invoice_plu_model;
import com.abztrakinc.ABZPOS.R;

import java.util.ArrayList;
import java.util.List;


public class admin_stock_end_receiving_recyclerview extends RecyclerView.Adapter<admin_stock_end_receiving_recyclerview.MyViewHolder>{


private ArrayList<invoice_plu_model> ButtonList;
private android.content.Context Context;
ArrayList<String>Selectlist = new ArrayList<String>();
private admin_stock_end_receiving_recyclerview.MyViewHolder holder;
    private OnClickListener onClickListener;
    private int position;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public void setSelectedPosition(int position) {
        selectedPosition = position;

//            Log.d("SELECTED ID",headerFooterID.get(position).toString());
        notifyDataSetChanged();
    }

    public admin_stock_end_receiving_recyclerview(Context context, ArrayList<invoice_plu_model> buttonList, OnClickListener onClickListener) {

    this.Context = context;
    this.ButtonList = buttonList;
    this.onClickListener = onClickListener;


}

@NonNull
@Override
public admin_stock_end_receiving_recyclerview.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(Context).inflate(R.layout.invoice_fragment_plu_layout,parent,false);
    //  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_fragment_button_function_layout,parent,false);
    admin_stock_end_receiving_recyclerview.MyViewHolder holder = new admin_stock_end_receiving_recyclerview.MyViewHolder(view);
    return holder;
}
@Override
public void onBindViewHolder(@NonNull admin_stock_end_receiving_recyclerview.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
    this.holder = holder;
    this.position = position;



    final invoice_plu_model button = ButtonList.get(position);

    String ButtonPrefix = button.getButtonID();
    String ButtonName=ButtonList.get(position).getItemName();
    String PriceOverrides=ButtonList.get(position).getPriceOverride();
    String itemCode=ButtonList.get(position).getItemCode();
    holder.tv_buttonNumber.setText("PLU + "+String.valueOf(ButtonPrefix) + " + Ent");
    holder.tv_buttonDetails.setText(String.valueOf(ButtonName));


    holder.ll_linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Update selectedPosition and notify adapter
            selectedPosition = position;
            notifyDataSetChanged();

            // Pass the clicked item data to the click listener
            if (onClickListener != null) {
              //  onClickListener.onClick(); // Assuming you want to pass the button prefix
                onClickListener.onClick(itemCode);
            }
        }
    });

    if (selectedPosition == position) {
        holder.ll_linearLayout.setBackground(ContextCompat.getDrawable(Context,R.drawable.customborderselect));
    } else {
        holder.ll_linearLayout.setBackground(ContextCompat.getDrawable(Context,R.drawable.customborder)); // Change to your default background color
    }




    //add color for selected linearlayout
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


    LinearLayout ll_linearLayout;
    TextView tv_buttonNumber,tv_buttonDetails;



    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
//
        ll_linearLayout = itemView.findViewById(R.id.ll_linearLayout);
        tv_buttonNumber = itemView.findViewById(R.id.tv_buttonNumber);
        tv_buttonDetails = itemView.findViewById(R.id.tv_buttonDetails);




    }
}


}
