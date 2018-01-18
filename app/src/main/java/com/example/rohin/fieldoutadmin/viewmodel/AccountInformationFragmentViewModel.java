package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.activity.AccountInformationDetailsActivity;
import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.BussinessHoursModel;
import com.example.rohin.fieldoutadmin.models.BussinessHoursResponse;
import com.example.rohin.fieldoutadmin.models.DomainResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 13-12-2017.
 */

public class AccountInformationFragmentViewModel {
    private BehaviorSubject<BussinessHoursResponse>bussinessHoursResponseBehaviorSubject=BehaviorSubject.create(new BussinessHoursResponse());
    private BehaviorSubject<DomainResponse>domainResponseBehaviorSubject=BehaviorSubject.create(new DomainResponse());
    private POJOInterface pojoInterface;
    @Inject
    public AccountInformationFragmentViewModel(POJOInterface pojoInterface){
        this.pojoInterface=pojoInterface;
    }
    public Observable<BussinessHoursResponse>bussinessHoursResponseObservable(String authKey,BussinessHoursModel bussinessHoursModel){
        return pojoInterface
                .postBussinessHours(authKey,bussinessHoursModel)
                .doOnNext(bussinessHoursResponse->{
                    bussinessHoursResponseBehaviorSubject.onNext(bussinessHoursResponse);
                });
    }
    public Observable<BussinessHoursResponse>getBusinnessHoursResponse(){
        return bussinessHoursResponseBehaviorSubject.asObservable();
    }
    public Observable<DomainResponse>domainResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getDomain(domainId,authKey)
                .doOnNext(domainResponse -> domainResponseBehaviorSubject.onNext(domainResponse));
    }
    public Observable<DomainResponse>getDomainResponse(){
        return domainResponseBehaviorSubject.asObservable();
    }
    public Observable<BussinessHoursResponse>getBusinessHourByDomainId(String authKey,String domainId){
        return pojoInterface
                .getBussinessHours(domainId,authKey)
                .doOnNext(bussinessHoursResponse -> bussinessHoursResponseBehaviorSubject.onNext(bussinessHoursResponse));
    }
}
