package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.InvoiceCustomerResponse;
import com.kyros.technologies.fieldout.models.PartsAndServicesResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 30-01-2018.
 */

public class PDF_CSV_ActivityViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<InvoiceCustomerResponse> invoiceCustomerResponseBehaviorSubject=BehaviorSubject.create(new InvoiceCustomerResponse());

    @Inject
    public PDF_CSV_ActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<InvoiceCustomerResponse> invoiceCustomerResponseObservable(String authKey,String invoiceId){
        return pojoInterface
                .getCustomerInvoice(authKey,invoiceId)
                .doOnNext(invoiceCustomerResponse ->  invoiceCustomerResponseBehaviorSubject.onNext(invoiceCustomerResponse));
    }
    public Observable<InvoiceCustomerResponse>partsAndServicesResponseObservable(){
        return  invoiceCustomerResponseBehaviorSubject.asObservable();
    }
}
