package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.BussinessHoursModel;
import com.kyros.technologies.fieldout.models.BussinessHoursResponse;
import com.kyros.technologies.fieldout.models.DomainResponse;

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
    public Observable<BussinessHoursResponse>bussinessHoursResponseObservable(String authKey,BussinessHoursModel bussinessHoursModel,String idDomain){
        return pojoInterface
                .postBussinessHours(authKey,bussinessHoursModel,idDomain)
                .doOnNext(bussinessHoursResponse->{
                    bussinessHoursResponseBehaviorSubject.onNext(bussinessHoursResponse);
                });
    }
    public Observable<BussinessHoursResponse>getBusinnessHoursResponse(){
        return bussinessHoursResponseBehaviorSubject.asObservable();
    }
    public Observable<DomainResponse>domainResponseObservable(String authKey,String domainId,String idDomain){
        return pojoInterface
                .getDomain(domainId,authKey,idDomain)
                .doOnNext(domainResponse -> domainResponseBehaviorSubject.onNext(domainResponse));
    }
    public Observable<DomainResponse>getDomainResponse(){
        return domainResponseBehaviorSubject.asObservable();
    }
    public Observable<BussinessHoursResponse>getBusinessHourByDomainId(String authKey,String domainId,String idDomain){
        return pojoInterface
                .getBussinessHours(domainId,authKey,idDomain)
                .doOnNext(bussinessHoursResponse -> bussinessHoursResponseBehaviorSubject.onNext(bussinessHoursResponse));
    }
}
