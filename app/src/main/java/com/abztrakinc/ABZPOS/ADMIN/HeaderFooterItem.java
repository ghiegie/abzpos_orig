package com.abztrakinc.ABZPOS.ADMIN;

public class HeaderFooterItem {
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getItemDoubleWidth() {
        return itemDoubleWidth;
    }

    public void setItemDoubleWidth(String itemDoubleWidth) {
        this.itemDoubleWidth = itemDoubleWidth;
    }

    private int itemID;
    private String itemText;
    private String itemDoubleWidth;
    private Boolean isSelected=true;

    public HeaderFooterItem(int itemID,String itemText,String itemDoubleWidth){

        this.itemID=itemID;
        this.itemText=itemText;
        this.itemDoubleWidth=itemDoubleWidth;

    }
    public void setSelected(Boolean selected){
        isSelected=selected;
    }
    public boolean isSelected(){
        return  isSelected;
    }
}
