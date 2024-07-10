package com.abztrakinc.ABZPOS;

public class InvoiceItem {



    private String itemName;
    private String itemId;
    private String itemPrice;
    private String itemQtyAvailable;

    public String getPriceOverride() {
        return priceOverride;
    }

    public void setPriceOverride(String priceOverride) {
        this.priceOverride = priceOverride;
    }

    private String priceOverride;

    public String getVatIndicator() {
        return vatIndicator;
    }

    public void setVatIndicator(String vatIndicator) {
        this.vatIndicator = vatIndicator;
    }

    private String vatIndicator;




    private Double itemSubtotal;
    private Boolean isSelected=true;


    public InvoiceItem(String itemId, String itemName, String itemPrice, String itemQtyAvailable,String VatIndicator,String PriceOverride) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemPrice = itemPrice;
        this.itemQtyAvailable = itemQtyAvailable;
        this.vatIndicator=VatIndicator;
        this.priceOverride=PriceOverride;


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

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQtyAvailable() {
        return itemQtyAvailable;
    }

    public void setItemQtyAvailable(String itemQtyAvailable) {
        this.itemQtyAvailable = itemQtyAvailable;
    }


    public void setSelected(Boolean selected){
        isSelected=selected;
    }
    public boolean isSelected(){
        return  isSelected;
    }


    private boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }








}
