package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.Tax;
import com.kyros.technologies.fieldout.models.TaxDeleteResponse;
import com.kyros.technologies.fieldout.models.TaxResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 03-01-2018.
 */

public class TaxesFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<TaxResponse>addTaxResponseBehaviorSubject=BehaviorSubject.create(new TaxResponse());
    private BehaviorSubject<TaxResponse>getTaxResponseBehaviorSubject=BehaviorSubject.create(new TaxResponse());
    private BehaviorSubject<TaxResponse>updateTaxResponseBehaviorSubject=BehaviorSubject.create(new TaxResponse());
    private BehaviorSubject<TaxDeleteResponse>deleteTaxResponseBehaviorSubject=BehaviorSubject.create(new TaxDeleteResponse());
    @Inject
    public TaxesFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<TaxResponse>getTaxResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getTax(authKey,domainId)
                .doOnNext(taxResponse -> getTaxResponseBehaviorSubject.onNext(taxResponse));
    }
    public Observable<TaxResponse>getTaxResponseObservable(){
        return getTaxResponseBehaviorSubject.asObservable();
    }

    public Observable<TaxResponse>addTaxResponseObservable(String authKey,Tax tax){
        return pojoInterface
                .addTax(authKey,tax)
                .doOnNext(taxResponse -> addTaxResponseBehaviorSubject.onNext(taxResponse));
    }
    public Observable<TaxResponse>addTaxResponseObservable(){
        return addTaxResponseBehaviorSubject.asObservable();
    }

    public Observable<TaxResponse>updateTaxResponseObservable(String authKey,String taxId,Tax tax){
        return pojoInterface
                .updateTax(authKey,taxId,tax)
                .doOnNext(taxResponse -> updateTaxResponseBehaviorSubject.onNext(taxResponse));
    }
    public Observable<TaxResponse>updateTaxResponseObservable(){
        return updateTaxResponseBehaviorSubject.asObservable();
    }

    public Observable<TaxDeleteResponse>deleteTaxResponseObservable(String authKey,String taxId){
        return pojoInterface
                .deleteTaxResponse(authKey,taxId)
                .doOnNext(taxResponse -> deleteTaxResponseBehaviorSubject.onNext(taxResponse));
    }
    public Observable<TaxDeleteResponse>deleteTaxResponseObservable(){
        return deleteTaxResponseBehaviorSubject.asObservable();
    }
}