package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 30-01-2018.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {

    @SerializedName("customerInfo")
    @Expose
    private CustomerInfo customerInfo;
    @SerializedName("dateString")
    @Expose
    private String dateString;
    @SerializedName("grand_amount")
    @Expose
    private Integer grandAmount;
    @SerializedName("grand_discount")
    @Expose
    private Integer grandDiscount;
    @SerializedName("grand_tax")
    @Expose
    private Integer grandTax;
    @SerializedName("grand_total")
    @Expose
    private Integer grandTotal;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idCustomer")
    @Expose
    private String idCustomer;
    @SerializedName("idSite")
    @Expose
    private String idSite;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("paymentDateString")
    @Expose
    private String paymentDateString;
    @SerializedName("siteInfo")
    @Expose
    private SiteInfo siteInfo;
    @SerializedName("status")
    @Expose
    private String status;

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public Integer getGrandAmount() {
        return grandAmount;
    }

    public void setGrandAmount(Integer grandAmount) {
        this.grandAmount = grandAmount;
    }

    public Integer getGrandDiscount() {
        return grandDiscount;
    }

    public void setGrandDiscount(Integer grandDiscount) {
        this.grandDiscount = grandDiscount;
    }

    public Integer getGrandTax() {
        return grandTax;
    }

    public void setGrandTax(Integer grandTax) {
        this.grandTax = grandTax;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Integer grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(String idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getIdSite() {
        return idSite;
    }

    public void setIdSite(String idSite) {
        this.idSite = idSite;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPaymentDateString() {
        return paymentDateString;
    }

    public void setPaymentDateString(String paymentDateString) {
        this.paymentDateString = paymentDateString;
    }

    public SiteInfo getSiteInfo() {
        return siteInfo;
    }

    public void setSiteInfo(SiteInfo siteInfo) {
        this.siteInfo = siteInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}