package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.RegionalSettings;
import com.example.rohin.fieldoutadmin.models.RegionalSettingsResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 03-01-2018.
 */

public class RegionalSettingsFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<RegionalSettingsResponse>getRegionalSettingsResponseBehaviorSubject=BehaviorSubject.create(new RegionalSettingsResponse());
    private BehaviorSubject<RegionalSettingsResponse>addRegionalSettingsResponseBehaviorSubject=BehaviorSubject.create(new RegionalSettingsResponse());

    @Inject
    public RegionalSettingsFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<RegionalSettingsResponse>getRegionalSettingsResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getRegionalSettings(authKey,domainId)
                .doOnNext(regionalSettingsResponse -> getRegionalSettingsResponseBehaviorSubject.onNext(regionalSettingsResponse));
    }
    public Observable<RegionalSettingsResponse>getRegionalSettingsResponseObservable(){
        return getRegionalSettingsResponseBehaviorSubject.asObservable();
    }

    public Observable<RegionalSettingsResponse>addRegionalSettingsResponseObservable(String authKey, RegionalSettings regionalSettings){
        return pojoInterface
                .addRegionalSettings(authKey,regionalSettings)
                .doOnNext(regionalSettingsResponse -> addRegionalSettingsResponseBehaviorSubject.onNext(regionalSettingsResponse));
    }
    public Observable<RegionalSettingsResponse>addRegionalSettingsResponseObservable(){
        return addRegionalSettingsResponseBehaviorSubject.asObservable();
    }
}
