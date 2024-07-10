package com.abztrakinc.ABZPOS.ADMIN;

public class admin_manage_product_item_category {




   private String ItemCategory;

   public String getItemCategory() {
      return ItemCategory;
   }

   public void setItemCategory(String itemCategory) {
      ItemCategory = itemCategory;
   }

   public String getItemSubCategory() {
      return ItemSubCategory;
   }

   public void setItemSubCategory(String itemSubCategory) {
      ItemSubCategory = itemSubCategory;
   }

   private String ItemSubCategory;

   public String getItemSubCategoryID() {
      return ItemSubCategoryID;
   }

   public void setItemSubCategoryID(String itemSubCategoryID) {
      ItemSubCategoryID = itemSubCategoryID;
   }

   private String ItemSubCategoryID;






   public admin_manage_product_item_category( String itemSubCategory,String itemSubCategoryID){

     // this.ItemCategory=itemCategory;
      this.ItemSubCategory=itemSubCategory;
      this.ItemSubCategoryID=itemSubCategoryID;




   }



}
