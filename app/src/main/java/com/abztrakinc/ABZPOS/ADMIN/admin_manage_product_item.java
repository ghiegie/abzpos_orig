package com.abztrakinc.ABZPOS.ADMIN;

public class admin_manage_product_item {

   public String getItemName() {
      return ItemName;
   }

   public void setItemName(String itemName) {
      ItemName = itemName;
   }

   public String getItemDescription() {
      return ItemDescription;
   }

   public void setItemDescription(String itemDescription) {
      ItemDescription = itemDescription;
   }

   public String getItemID() {
      return ItemID;
   }

   public void setItemID(String itemID) {
      ItemID = itemID;
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

   public String getFastMovingItem() {
      return FastMovingItem;
   }

   public void setFastMovingItem(String fastMovingItem) {
      FastMovingItem = fastMovingItem;
   }

   public String getFastMovingButton() {
      return FastMovingButton;
   }

   public void setFastMovingButton(String fastMovingButton) {
      FastMovingButton = fastMovingButton;
   }

   public String getItemCode() {
      return ItemCode;
   }

   public void setItemCode(String itemCode) {
      ItemCode = itemCode;
   }

   public String getBarcode() {
      return Barcode;
   }

   public void setBarcode(String barcode) {
      Barcode = barcode;
   }

   private String ItemName;
   private String ItemDescription;
   private String ItemID;
   private String ItemPrice;
   private String ItemQty;
   private String FastMovingItem;
   private String FastMovingButton;
   private String ItemCode;
   private String Barcode;

   public String getPriceOverride() {
      return PriceOverride;
   }

   public void setPriceOverride(String priceOverride) {
      PriceOverride = priceOverride;
   }

   private String PriceOverride;

   public String getVATIndicator() {
      return VATIndicator;
   }

   public void setVATIndicator(String VATIndicator) {
      this.VATIndicator = VATIndicator;
   }

   private String VATIndicator;

   public admin_manage_product_item(String itemName,String itemDescription,String itemID,String itemPrice,String itemQty,String fastMovingItem,String fastMovingButton
   ,String itemCode,String barcode,String vATIndicator,String priceOverride){

      this.ItemName=itemName;
      this.ItemDescription=itemDescription;
      this.ItemID=itemID;
      this.ItemPrice=itemPrice;
      this.ItemQty=itemQty;
      this.FastMovingItem=fastMovingItem;
      this.FastMovingButton=fastMovingButton;
      this.ItemCode=itemCode;
      this.Barcode=barcode;
      this.VATIndicator=vATIndicator;
      this.PriceOverride=priceOverride;




   }



}
