package com.kyros.technologies.fieldout.models;

/**
 * Created by kyros on 14-02-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceResponse {

    @SerializedName("invoice")
    @Expose
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

}
