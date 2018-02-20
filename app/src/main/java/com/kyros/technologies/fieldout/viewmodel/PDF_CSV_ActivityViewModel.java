package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.InvoiceCustomerResponse;
import com.kyros.technologies.fieldout.models.InvoiceResponse;
import com.kyros.technologies.fieldout.models.PartsAndServicesResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 30-01-2018.
 */

public class PDF_CSV_ActivityViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<InvoiceResponse> invoiceCustomerResponseBehaviorSubject=BehaviorSubject.create(new InvoiceResponse());

    @Inject
    public PDF_CSV_ActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<InvoiceResponse> invoiceCustomerResponseObservable(String authKey,String invoiceId,String idDomain){
        return pojoInterface
                .getCustomerInvoice(authKey,invoiceId,idDomain)
                .doOnNext(invoiceCustomerResponse ->  invoiceCustomerResponseBehaviorSubject.onNext(invoiceCustomerResponse));
    }
    public Observable<InvoiceResponse>partsAndServicesResponseObservable(){
        return  invoiceCustomerResponseBehaviorSubject.asObservable();
    }
}
