package com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview;

import java.util.ArrayList;

public class invoice_plu_model {


    public String getButtonID() {
        return ButtonID;
    }

    public void setButtonID(String buttonID) {
        ButtonID = buttonID;
    }

    //    itemCode = new ArrayList<>();
//    itemName = new ArrayList<>();
//    recptDesc = new ArrayList<>();
//    itemPrice = new ArrayList<>();
//    itemQuantity = new ArrayList<>();
//    itemVatIndicator=new ArrayList<>()
    String ButtonID;
    String ItemCode;
    String ItemName;

    public String getButtonPrefix() {
        return ButtonPrefix;
    }

    public void setButtonPrefix(String buttonPrefix) {
        ButtonPrefix = buttonPrefix;
    }

    String ButtonPrefix;

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getRecptDesc() {
        return RecptDesc;
    }

    public void setRecptDesc(String recptDesc) {
        RecptDesc = recptDesc;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemQty() {
        return ItemQty;
    }

    public void setItemQty(String itemQty) {
        ItemQty = itemQty;
    }

    public String getItemVatIndicator() {
        return ItemVatIndicator;
    }

    public void setItemVatIndicator(String itemVatIndicator) {
        ItemVatIndicator = itemVatIndicator;
    }

    String RecptDesc;
    String ItemPrice;
    String ItemQty;
    String ItemVatIndicator;

    public String getPriceOverride() {
        return PriceOverride;
    }

    public void setPriceOverride(String priceOverride) {
        PriceOverride = priceOverride;
    }

    String PriceOverride;



    public  invoice_plu_model(String buttonID,String itemCode,String itemName,String recptDesc,String itemPrice,String itemQty,String itemVatIndicator,String priceOverride){
        this.ButtonID = buttonID;
//        this.ButtonPrefix=buttonPrefix;
        this.ItemCode=itemCode;
        this.ItemName=itemName;
        this.RecptDesc=recptDesc;
        this.ItemPrice=itemPrice;
        this.ItemQty=itemQty;
        this.ItemVatIndicator=itemVatIndicator;
        this.PriceOverride = priceOverride;
    }
}
