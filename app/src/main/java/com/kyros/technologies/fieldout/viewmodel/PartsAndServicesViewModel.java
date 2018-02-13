package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.DeletePartsAndServicesResponse;
import com.kyros.technologies.fieldout.models.PartsAndServicesResponse;
import com.kyros.technologies.fieldout.models.StockPart;

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
    public Observable<PartsAndServicesResponse>getPartsAndServicesResponseObservable(String authKey,String idDomain){
        return pojoInterface
                .getPartsAndServices(authKey,idDomain)
                .doOnNext(partsAndServicesResponse -> getpartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<PartsAndServicesResponse>getPartsAndServicesResponseObservable(){
        return  getpartsAndServicesResponseBehaviorSubject.asObservable();
    }
    public Observable<PartsAndServicesResponse>addPartsAndServicesResponseObservable(String authKey, StockPart stockPart,String idDomain){
        return pojoInterface
                .addPartsAndServices(authKey,stockPart,idDomain)
                .doOnNext(partsAndServicesResponse -> addpartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<PartsAndServicesResponse>addPartsAndServicesResponseObservable(){
        return  addpartsAndServicesResponseBehaviorSubject.asObservable();
    }
    public Observable<PartsAndServicesResponse>updatePartsAndServicesResponseObservable(String authKey,String stockId, StockPart stockPart,String idDomain){
        return pojoInterface
                .updatePartsAndServices(authKey,stockId,stockPart,idDomain)
                .doOnNext(partsAndServicesResponse -> updatepartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<PartsAndServicesResponse>updatePartsAndServicesResponseObservable(){
        return  updatepartsAndServicesResponseBehaviorSubject.asObservable();
    }
    public Observable<DeletePartsAndServicesResponse>deletePartsAndServicesResponseObservable(String authKey,String stockId,String idDomain){
        return pojoInterface
                .deleteParsAndServices(authKey,stockId,idDomain)
                .doOnNext(partsAndServicesResponse -> deletepartsAndServicesResponseBehaviorSubject.onNext(partsAndServicesResponse));
    }
    public Observable<DeletePartsAndServicesResponse>deletePartsAndServicesResponseObservable(){
        return  deletepartsAndServicesResponseBehaviorSubject.asObservable();
    }
}
