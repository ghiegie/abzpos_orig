package com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abztrakinc.ABZPOS.R;

import java.util.ArrayList;

public class invoice_plu_recyclerviewadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<invoice_plu_model> ButtonList;
    private Context Context;

    public invoice_plu_recyclerviewadapter(Context context, ArrayList<invoice_plu_model> buttonList){
        this.Context = context;
        this.ButtonList = buttonList;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(Context).inflate(R.layout.invoice_fragment_plu_layout,parent,false);
        return new MyViewHolder(view);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


//        final ProductModel item = list.get(position);
//        if (holder instanceof MyViewHolder){
//            ((MyViewHolder)holder).tv_productname.setText(item.getItemName());
//            ((MyViewHolder)holder).tv_productAmt.setText(String.valueOf((item.getItemPrice())));
//            int itemID=item.getItemID();
////
////                ((MyViewHolder)holder).cb_check.setOnCheckedChangeListener(null);
////                ((MyViewHolder)holder).cb_check.setSelected(list.get(position).);
////
////
//            ((MyViewHolder)holder).ll_productItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    item.setSelected(!item.isSelected());
//                    if (!item.isSelected()){
//                        Log.d("TAG", "onClick: selected");
//                        ((MyViewHolder) holder).iv_check.setVisibility(View.VISIBLE);
//                        Selectlist.add(String.valueOf(itemID));
//                        Log.d("TAG", "Selectlist"+Selectlist.toString());
//
//                    }
//                    else{
//                        Log.d("TAG", "onClick: unselected");
//                        ((MyViewHolder) holder).iv_check.setVisibility(View.GONE);
//                        Selectlist.remove(String.valueOf(itemID));
//                    }
//                }
//            });
//        }


        final invoice_plu_model button = ButtonList.get(position);
        if (holder instanceof  MyViewHolder){
            ((MyViewHolder)holder).tv_buttonDetails.setText(button.getItemName());
            ((MyViewHolder)holder).tv_buttonNumber.setText(button.getItemCode());

        }



    }

    @Override
    public int getItemCount() {
        return ButtonList.size();
    }

    private static class MyViewHolder
            extends RecyclerView.ViewHolder {

        LinearLayout ll_linearLayout;
        TextView tv_buttonNumber,tv_buttonDetails;

        MyViewHolder(View view)
        {
            super(view);
//            tv_productname = view.findViewById(R.id.tv_productName);
//            tv_productAmt = view.findViewById(R.id.tv_productAmt);
//            iv_check = view.findViewById(R.id.iv_check);
//            // iv_check.setVisibility(View.INVISIBLE);
////            cb_check = view.findViewById(R.id.cb_check);
//            ll_productItem = view.findViewById(R.id.ll_productItem);
//            ll_productItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    iv_check.setVisibility(View.VISIBLE);
//                }
//            });

            ll_linearLayout = view.findViewById(R.id.ll_linearLayout);
            tv_buttonNumber = view.findViewById(R.id.tv_buttonNumber);
            tv_buttonDetails = view.findViewById(R.id.tv_buttonDetails);









        }
    }


}
