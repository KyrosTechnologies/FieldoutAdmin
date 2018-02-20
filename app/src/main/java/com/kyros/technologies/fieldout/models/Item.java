package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 14-02-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("discount_c")
    @Expose
    private Integer discountC;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idInvoice")
    @Expose
    private String idInvoice;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("tax")
    @Expose
    private Integer tax;
    @SerializedName("tax_c")
    @Expose
    private Integer taxC;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getDiscountC() {
        return discountC;
    }

    public void setDiscountC(Integer discountC) {
        this.discountC = discountC;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdInvoice() {
        return idInvoice;
    }

    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public Integer getTaxC() {
        return taxC;
    }

    public void setTaxC(Integer taxC) {
        this.taxC = taxC;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

}