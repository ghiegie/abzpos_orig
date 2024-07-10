package com.abztrakinc.ABZPOS.ORDERSTATION;

public class orderItemDiscount {

    private String itemName;
    private String itemId;


    private String itemRemarks;



    private String discQty;
    private String discAmt;
    private int itemQty;
    private Double itemSubtotal;
    private Boolean isSelected=true;


    public orderItemDiscount(String discQty) {
        this.discQty = discQty;

    }


    public String getDiscQty() {
        return discQty;
    }

    public void setDiscQty(String discQty) {
        this.discQty = discQty;
    }

    public String getDiscAmt() {
        return discAmt;
    }

    public void setDiscAmt(String discAmt) {
        this.discAmt = discAmt;
    }

    public String getItemRemarks() {
        return itemRemarks;
    }

    public void setItemRemarks(String itemRemarks) {
        this.itemRemarks = itemRemarks;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public Double getItemSubtotal() {
        return itemSubtotal;
    }

    public void setItemSubtotal(Double itemSubtotal) {
        this.itemSubtotal = itemSubtotal;
    }

    public void setSelected(Boolean selected){
        isSelected=selected;
    }
    public boolean isSelected(){
        return  isSelected;
    }






}
