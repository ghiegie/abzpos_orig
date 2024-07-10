package com.abztrakinc.ABZPOS.CASHIER.Invoice.Recyclerview;

import java.util.ArrayList;

public class invoice_discount_model {


//    DiscountAutoIDList= new ArrayList<>();
//    DiscountIDList= new ArrayList<>(); //discountType for getting name/id
//    DiscountNameList= new ArrayList<>();
//    DiscountAmountList= new ArrayList<>();
//    DiscountComputationList= new ArrayList<>();
//    DiscountExcludeTaxList= new ArrayList<>();
//    ProRatedTaxList=new ArrayList<>();
//    DiscountTypeList= new ArrayList<>();
//    DiscountCategoryList = new ArrayList<>();

    public String getDiscountID() {
        return DiscountID;
    }

    public void setDiscountID(String discountID) {
        DiscountID = discountID;
    }

    public String getDiscountName() {
        return DiscountName;
    }

    public void setDiscountName(String discountName) {
        DiscountName = discountName;
    }

    String DiscountID;
    String DiscountName;

    public invoice_discount_model(String discountID, String discountName){
        this.DiscountID = discountID;
        this.DiscountName=discountName;


    }
}
