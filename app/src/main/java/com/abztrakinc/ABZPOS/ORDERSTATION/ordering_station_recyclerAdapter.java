package com.abztrakinc.ABZPOS.ORDERSTATION;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abztrakinc.ABZPOS.R;

import java.util.ArrayList;

public class ordering_station_recyclerAdapter extends RecyclerView.Adapter<ordering_station_recyclerAdapter.MyViewHolder> {
    private ArrayList<order_station_product> productList;
    private ArrayList<order_station_product> QtyList;
    private ArrayList<order_station_product> priceList;

    public ordering_station_recyclerAdapter(ArrayList<order_station_product> productList,ArrayList<order_station_product> QtyList,ArrayList<order_station_product> priceList){
        this.productList = productList;
        this.QtyList = QtyList;
        this.priceList = priceList;


    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView productTxt;
        private TextView QtyTxt;
        private TextView priceTxt;

        public MyViewHolder(final View view){
            super(view);
            productTxt = view.findViewById(R.id.productTxt);
            QtyTxt = view.findViewById(R.id.QtyTxt);
            priceTxt = view.findViewById(R.id.priceTxt);


           // ordering_station_main ordering_station_main = new ordering_station_main();


        }
    }

    @NonNull

    @Override
    public ordering_station_recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item,parent,false);
        return new MyViewHolder(itemView);
        
    }

    @Override
    public void onBindViewHolder(@NonNull ordering_station_recyclerAdapter.MyViewHolder holder, int position) {

        String prodName = productList.get(position).getProduct();
        holder.productTxt.setText(prodName);

        String qtyName = QtyList.get(position).getProduct();
        holder.QtyTxt.setText(qtyName);

        String prcName = priceList.get(position).getProduct();
        holder.priceTxt.setText(prcName);


    }

    @Override
    public int getItemCount() {
        return productList.size();

    }
}
