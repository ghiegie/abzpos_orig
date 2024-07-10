package com.abztrakinc.ABZPOS.CASHIER;

public class cashier_invoice_masterlist_model {


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

    public String getItemRecptDesc() {
        return ItemRecptDesc;
    }

    public void setItemRecptDesc(String itemRecptDesc) {
        ItemRecptDesc = itemRecptDesc;
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

    public String getVatIndicator() {
        return VatIndicator;
    }

    public void setVatIndicator(String vatIndicator) {
        VatIndicator = vatIndicator;
    }

    String ItemCode;
    String ItemName;
    String ItemRecptDesc;
    String ItemPrice;
    String ItemQty;
    String VatIndicator;

    public String getPriceOverride() {
        return PriceOverride;
    }

    public void setPriceOverride(String priceOverride) {
        PriceOverride = priceOverride;
    }

    String PriceOverride;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    int value;

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    boolean highlight = false;

    public String getPluNumber() {
        return PluNumber;
    }

    public void setPluNumber(String pluNumber) {
        PluNumber = pluNumber;
    }

    String PluNumber;







    public cashier_invoice_masterlist_model(String pluNumber,String itemCode, String itemName, String itemReceptDesc,
                                            String itemPrice, String itemQty, String vatIndicator,String priceOverride){

        this.PluNumber=pluNumber;
        this.ItemCode=itemCode;
        this.ItemName=itemName;
        this.ItemRecptDesc=itemReceptDesc;
        this.ItemPrice=itemPrice;
        this.ItemQty=itemQty;
        this.VatIndicator=vatIndicator;
        this.PriceOverride=priceOverride;



    }





}
