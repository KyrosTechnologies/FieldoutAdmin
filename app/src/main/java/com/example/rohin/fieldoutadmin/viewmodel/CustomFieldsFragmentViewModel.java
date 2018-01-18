package com.example.rohin.fieldoutadmin.viewmodel;

import android.app.backup.BackupHelper;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.CustomField;
import com.example.rohin.fieldoutadmin.models.CustomFieldResponse;
import com.example.rohin.fieldoutadmin.models.DeleteCustomFieldResponse;

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
    public Observable<CustomFieldResponse>getcustomFieldResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getCustomFields(authKey,domainId)
                .doOnNext(customFieldResponse -> getcustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<CustomFieldResponse>getcustomFieldResponseObservable(){
        return getcustomFieldResponseBehaviorSubject.asObservable();
    }
    public Observable<CustomFieldResponse>addcustomFieldResponseObservable(String authKey,CustomField customField){
        return pojoInterface
                .addCustomFields(authKey,customField)
                .doOnNext(customFieldResponse -> addcustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<CustomFieldResponse>addcustomFieldResponseObservable(){
        return addcustomFieldResponseBehaviorSubject.asObservable();
    }
    public Observable<CustomFieldResponse>updatecustomFieldResponseObservable(String authKey,CustomField customField,String customFieldId){
        return pojoInterface
                .updateCustomFields(authKey,customField,customFieldId)
                .doOnNext(customFieldResponse -> updatecustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<CustomFieldResponse>updatecustomFieldResponseObservable(){
        return updatecustomFieldResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteCustomFieldResponse>deleteCustomFieldResponseObservable(String authKey,String customFieldId){
        return pojoInterface
                .deleteCustomFields(authKey,customFieldId)
                .doOnNext(customFieldResponse -> deleteCustomFieldResponseBehaviorSubject.onNext(customFieldResponse));
    }
    public Observable<DeleteCustomFieldResponse>deleteCustomFieldResponseObservable(){
        return deleteCustomFieldResponseBehaviorSubject.asObservable();
    }
}
