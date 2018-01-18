package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.DeletePartsAndServicesResponse;
import com.example.rohin.fieldoutadmin.models.PartsAndServicesResponse;
import com.example.rohin.fieldoutadmin.models.StockPart;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 04-01-2018.
 */

public class PartsAndServicesViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<PartsAndServicesResponse>getpartsAndServicesResponseBehaviorSubject=BehaviorSubject.create(new PartsAndServicesResponse());
    private BehaviorSubject<PartsAndServicesResponse>addpartsAndServicesResponseBehaviorSubject=BehaviorSubject.create(new PartsAndServicesResponse());
    private BehaviorSubject<PartsAndServicesResponse>updatepartsAndServicesResponseBehaviorSubject=BehaviorSubject.create(new PartsAndServicesResponse());
    private BehaviorSubject<DeletePartsAndServicesResponse>deletepartsAndServicesResponseBehaviorSubject=BehaviorSubject.create(new DeletePartsAndServicesResponse());

    @Inject
    public PartsAndServicesViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<PartsAndServicesResponse>getPartsAndServicesResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getPartsAndServices(authKey,domainId)
                .doOnNext(partsAndServicesResponse -> getpartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<PartsAndServicesResponse>getPartsAndServicesResponseObservable(){
        return  getpartsAndServicesResponseBehaviorSubject.asObservable();
    }
    public Observable<PartsAndServicesResponse>addPartsAndServicesResponseObservable(String authKey, StockPart stockPart){
        return pojoInterface
                .addPartsAndServices(authKey,stockPart)
                .doOnNext(partsAndServicesResponse -> addpartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<PartsAndServicesResponse>addPartsAndServicesResponseObservable(){
        return  addpartsAndServicesResponseBehaviorSubject.asObservable();
    }
    public Observable<PartsAndServicesResponse>updatePartsAndServicesResponseObservable(String authKey,String stockId, StockPart stockPart){
        return pojoInterface
                .updatePartsAndServices(authKey,stockId,stockPart)
                .doOnNext(partsAndServicesResponse -> updatepartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<PartsAndServicesResponse>updatePartsAndServicesResponseObservable(){
        return  updatepartsAndServicesResponseBehaviorSubject.asObservable();
    }
    public Observable<DeletePartsAndServicesResponse>deletePartsAndServicesResponseObservable(String authKey,String stockId){
        return pojoInterface
                .deleteParsAndServices(authKey,stockId)
                .doOnNext(partsAndServicesResponse -> deletepartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<DeletePartsAndServicesResponse>deletePartsAndServicesResponseObservable(){
        return  deletepartsAndServicesResponseBehaviorSubject.asObservable();
    }
}
