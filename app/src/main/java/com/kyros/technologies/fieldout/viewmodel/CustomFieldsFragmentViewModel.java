package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.CustomFieldResponse;
import com.kyros.technologies.fieldout.models.DeleteCustomFieldResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 05-01-2018.
 */

public class CustomFieldsFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<CustomFieldResponse>getcustomFieldResponseBehaviorSubject= BehaviorSubject.create(new CustomFieldResponse());
    private BehaviorSubject<CustomFieldResponse>addcustomFieldResponseBehaviorSubject= BehaviorSubject.create(new CustomFieldResponse());
    private BehaviorSubject<CustomFieldResponse>updatecustomFieldResponseBehaviorSubject= BehaviorSubject.create(new CustomFieldResponse());
    private BehaviorSubject<DeleteCustomFieldResponse>deleteCustomFieldResponseBehaviorSubject= BehaviorSubject.create(new DeleteCustomFieldResponse());

    @Inject
    public CustomFieldsFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<CustomFieldResponse>getcustomFieldResponseObservable(String authKey,String idDomain){
        return pojoInterface
                .getCustomFields(authKey,idDomain)
                .doOnNext(customFieldResponse -> getcustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<CustomFieldResponse>getcustomFieldResponseObservable(){
        return getcustomFieldResponseBehaviorSubject.asObservable();
    }
    public Observable<CustomFieldResponse>addcustomFieldResponseObservable(String authKey,CustomField customField,String idDomain){
        return pojoInterface
                .addCustomFields(authKey,customField,idDomain)
                .doOnNext(customFieldResponse -> addcustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<CustomFieldResponse>addcustomFieldResponseObservable(){
        return addcustomFieldResponseBehaviorSubject.asObservable();
    }
    public Observable<CustomFieldResponse>updatecustomFieldResponseObservable(String authKey,CustomField customField,String customFieldId,String idDomain){
        return pojoInterface
                .updateCustomFields(authKey,customField,customFieldId,idDomain)
                .doOnNext(customFieldResponse -> updatecustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<CustomFieldResponse>updatecustomFieldResponseObservable(){
        return updatecustomFieldResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteCustomFieldResponse>deleteCustomFieldResponseObservable(String authKey,String customFieldId,String idDomain){
        return pojoInterface
                .deleteCustomFields(authKey,customFieldId,idDomain)
                .doOnNext(customFieldResponse -> deleteCustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<DeleteCustomFieldResponse>deleteCustomFieldResponseObservable(){
        return deleteCustomFieldResponseBehaviorSubject.asObservable();
    }
}
