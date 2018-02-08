package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.ActivityType;
import com.kyros.technologies.fieldout.models.ActivityTypeAddResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeDeleteResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeUpdateResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 22-12-2017.
 */

public class ActivityTypeFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<ActivityTypeResponse>activityTypeResponseBehaviorSubject=BehaviorSubject.create(new ActivityTypeResponse());
    private BehaviorSubject<ActivityTypeAddResponse>activityTypeAddResponseBehaviorSubject=BehaviorSubject.create(new ActivityTypeAddResponse());
    private BehaviorSubject<ActivityTypeUpdateResponse>activityTypeUpdateResponseBehaviorSubject=BehaviorSubject.create(new ActivityTypeUpdateResponse());
    private BehaviorSubject<ActivityTypeDeleteResponse>activityTypeDeleteResponseBehaviorSubject=BehaviorSubject.create(new ActivityTypeDeleteResponse());
    @Inject
    public ActivityTypeFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<ActivityTypeResponse>activityTypeResponseObservable(String domainId,String authKey,String idDomain){
        return pojoInterface
                .getActivityType(domainId,authKey,idDomain)
                .doOnNext(activityTypeResponse -> activityTypeResponseBehaviorSubject.onNext(activityTypeResponse));
    }
    public Observable<ActivityTypeResponse>activityTypeResponseObservable(){
        return activityTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<ActivityTypeAddResponse>activityTypeAddResponseObservable(String authKey, ActivityType activityType,String idDomain){
        return pojoInterface
                .addActivityType(authKey,activityType,idDomain)
                .doOnNext(activityTypeAddResponse -> activityTypeAddResponseBehaviorSubject.onNext(activityTypeAddResponse));
    }
    public Observable<ActivityTypeAddResponse>activityTypeAddResponseObservable(){
        return activityTypeAddResponseBehaviorSubject.asObservable();
    }
    public Observable<ActivityTypeUpdateResponse>activityTypeUpdateResponseObservable(String authKey,String activityTypeId,ActivityType activityType,String idDomain){
        return pojoInterface
                .updateActivityType(authKey,activityType,activityTypeId,idDomain)
                .doOnNext(activityTypeUpdateResponse -> activityTypeUpdateResponseBehaviorSubject.onNext(activityTypeUpdateResponse));

    }
    public Observable<ActivityTypeUpdateResponse>activityTypeUpdateResponseObservable(){
        return activityTypeUpdateResponseBehaviorSubject.asObservable();
    }
    public Observable<ActivityTypeDeleteResponse>activityTypeDeleteResponseObservable(String authKey,String activityTypeId,String idDomain){
        return pojoInterface
                .deleteActivityType(authKey,activityTypeId,idDomain)
                .doOnNext(activityTypeDeleteResponse -> activityTypeDeleteResponseBehaviorSubject.onNext(activityTypeDeleteResponse));
    }
    public Observable<ActivityTypeDeleteResponse>activityTypeDeleteResponseObservable(){
        return activityTypeDeleteResponseBehaviorSubject.asObservable();
    }
}
