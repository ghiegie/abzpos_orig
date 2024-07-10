package com.abztrakinc.ABZPOS;

public class discount_model {





    String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }

    public String getDiscountComputation() {
        return DiscountComputation;
    }

    public void setDiscountComputation(String discountComputation) {
        DiscountComputation = discountComputation;
    }

    public String getDiscountExcludeTax() {
        return DiscountExcludeTax;
    }

    public void setDiscountExcludeTax(String discountExcludeTax) {
        DiscountExcludeTax = discountExcludeTax;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getDiscCategory() {
        return DiscCategory;
    }

    public void setDiscCategory(String discCategory) {
        DiscCategory = discCategory;
    }

    public String getDiscLabel() {
        return DiscLabel;
    }

    public void setDiscLabel(String discLabel) {
        DiscLabel = discLabel;
    }

    public String getMinTransactionAmount() {
        return MinTransactionAmount;
    }

    public void setMinTransactionAmount(String minTransactionAmount) {
        MinTransactionAmount = minTransactionAmount;
    }

    public String getMaxTransactionAmount() {
        return MaxTransactionAmount;
    }

    public void setMaxTransactionAmount(String maxTransactionAmount) {
        MaxTransactionAmount = maxTransactionAmount;
    }

    public String getMaxDiscountAmount() {
        return MaxDiscountAmount;
    }

    public void setMaxDiscountAmount(String maxDiscountAmount) {
        MaxDiscountAmount = maxDiscountAmount;
    }

    public String getSalesExcludeTax() {
        return SalesExcludeTax;
    }

    public void setSalesExcludeTax(String salesExcludeTax) {
        SalesExcludeTax = salesExcludeTax;
    }

    String DiscountID;
    String DiscountName;
    String DiscountAmount;
    String DiscountComputation;
    String DiscountExcludeTax;
    String DiscountType,DiscCategory,DiscLabel,MinTransactionAmount,MaxTransactionAmount,MaxDiscountAmount,SalesExcludeTax;

//    ID
//            DiscountID
//    DiscountName
//            DiscountAmount
//    DiscountComputation
//            DiscountExcludeTax
//    DiscountType
//            DiscCategory
//    DiscLabel
//            MinTransactionAmount
//    MaxTransactionAmount
//            MaxDiscountAmount
//    SalesExcludeTax




    public discount_model(String DiscID,String DiscountID,String DiscountName,String DiscountAmount,String DiscountComputation,
                          String DiscountExcludeTax,String DiscountType,String DiscCategory,String DiscLabel,String MinTransactionAmount,String MaxTransactionAmount,
                          String MaxDiscountAmount,String SalesExcludeTax){

        this.ID=DiscID;
        this.DiscountID=DiscountID;
        this.DiscountName=DiscountName;
        this.DiscountAmount=DiscountAmount;
        this.DiscountComputation=DiscountComputation;
        this.DiscountExcludeTax=DiscountExcludeTax;
        this.DiscountType=DiscountType;
        this.DiscCategory=DiscCategory;
        this.DiscLabel=DiscLabel;
        this.MinTransactionAmount=MinTransactionAmount;
        this.MaxTransactionAmount=MaxTransactionAmount;
        this.MaxDiscountAmount=MaxDiscountAmount;
        this.SalesExcludeTax=SalesExcludeTax;

    }
}
