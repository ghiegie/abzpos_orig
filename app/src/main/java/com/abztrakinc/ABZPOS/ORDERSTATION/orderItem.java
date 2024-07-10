package com.abztrakinc.ABZPOS.ORDERSTATION;

public class orderItem {

    private String itemName;
    private String itemId;


    private String itemRemarks;

    public String getItemVatIndicator() {
        return itemVatIndicator;
    }

    public void setItemVatIndicator(String itemVatIndicator) {
        this.itemVatIndicator = itemVatIndicator;
    }

    private String itemVatIndicator;



    private String discQty;
    private String discAmt;
    private Double itemQty;
    private String itemSubtotal;
    private Boolean isSelected=true;


    public orderItem(String itemId,String itemName, Double itemQty, String itemSubtotal,String itemRemarks,String ItemVatIndicator) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemQty = itemQty;
        this.itemSubtotal = itemSubtotal;
        this.itemRemarks = itemRemarks;
        this.itemVatIndicator=ItemVatIndicator;

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

    public Double getItemQty() {
        return itemQty;
    }

    public void setItemQty(Double itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemSubtotal() {
        return itemSubtotal;
    }

    public void setItemSubtotal(String itemSubtotal) {
        this.itemSubtotal = itemSubtotal;
    }

    public void setSelected(Boolean selected){
        isSelected=selected;
    }
    public boolean isSelected(){
        return  isSelected;
    }






}
