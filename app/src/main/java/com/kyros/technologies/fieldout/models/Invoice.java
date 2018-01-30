package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 30-01-2018.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {

    @SerializedName("amount")
    @Expose
    private List<Integer> amount = null;
    @SerializedName("customerInfo")
    @Expose
    private CustomerInfo customerInfo;
    @SerializedName("dateString")
    @Expose
    private String dateString;
    @SerializedName("description")
    @Expose
    private List<String> description = null;
    @SerializedName("discount")
    @Expose
    private List<Integer> discount = null;
    @SerializedName("discount_c")
    @Expose
    private List<Integer> discountC = null;
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
    @SerializedName("idDomain")
    @Expose
    private String idDomain;
    @SerializedName("idSite")
    @Expose
    private String idSite;
    @SerializedName("item")
    @Expose
    private List<String> item = null;
    @SerializedName("quantity")
    @Expose
    private List<Integer> quantity = null;
    @SerializedName("siteInfo")
    @Expose
    private SiteInfo siteInfo;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tax")
    @Expose
    private List<Integer> tax = null;
    @SerializedName("tax_c")
    @Expose
    private List<Integer> taxC = null;
    @SerializedName("total")
    @Expose
    private List<Integer> total = null;
    @SerializedName("unit_price")
    @Expose
    private List<Integer> unitPrice = null;

    public List<Integer> getAmount() {
        return amount;
    }

    public void setAmount(List<Integer> amount) {
        this.amount = amount;
    }

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

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<Integer> getDiscount() {
        return discount;
    }

    public void setDiscount(List<Integer> discount) {
        this.discount = discount;
    }

    public List<Integer> getDiscountC() {
        return discountC;
    }

    public void setDiscountC(List<Integer> discountC) {
        this.discountC = discountC;
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

    public String getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(String idDomain) {
        this.idDomain = idDomain;
    }

    public String getIdSite() {
        return idSite;
    }

    public void setIdSite(String idSite) {
        this.idSite = idSite;
    }

    public List<String> getItem() {
        return item;
    }

    public void setItem(List<String> item) {
        this.item = item;
    }

    public List<Integer> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Integer> quantity) {
        this.quantity = quantity;
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

    public List<Integer> getTax() {
        return tax;
    }

    public void setTax(List<Integer> tax) {
        this.tax = tax;
    }

    public List<Integer> getTaxC() {
        return taxC;
    }

    public void setTaxC(List<Integer> taxC) {
        this.taxC = taxC;
    }

    public List<Integer> getTotal() {
        return total;
    }

    public void setTotal(List<Integer> total) {
        this.total = total;
    }

    public List<Integer> getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(List<Integer> unitPrice) {
        this.unitPrice = unitPrice;
    }

}
