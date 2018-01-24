package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddJobsTypeResponse;
import com.kyros.technologies.fieldout.models.DeleteJobsTypeResponse;
import com.kyros.technologies.fieldout.models.JobType;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 26-12-2017.
 */

public class AddJobsTypeActivityViewModel {
    private BehaviorSubject<AddJobsTypeResponse>addJobsTypeResponseBehaviorSubject=BehaviorSubject.create(new AddJobsTypeResponse());
    private BehaviorSubject<AddJobsTypeResponse>updateJobsTypeResponseBehaviorSubject=BehaviorSubject.create(new AddJobsTypeResponse());
    private BehaviorSubject<DeleteJobsTypeResponse>deleteJobsTypeResponseBehaviorSubject=BehaviorSubject.create(new DeleteJobsTypeResponse());

    private POJOInterface pojoInterface;
    @Inject
    public AddJobsTypeActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<AddJobsTypeResponse>addJobsTypeResponseObservable(String authKey, JobType jobType){
        return pojoInterface
                .addJobsType(authKey,jobType)
                .doOnNext(addJobsTypeResponse -> addJobsTypeResponseBehaviorSubject.onNext(addJobsTypeResponse));
    }
    public Observable<AddJobsTypeResponse>addJobsTypeResponseObservable(){
        return addJobsTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<AddJobsTypeResponse>updateJobsTypeResponseObservable(String authKey,String jobTypeId, JobType jobType){
        return pojoInterface
                .updateJobsType(authKey,jobTypeId,jobType)
                .doOnNext(addJobsTypeResponse -> updateJobsTypeResponseBehaviorSubject.onNext(addJobsTypeResponse));
    }
    public Observable<AddJobsTypeResponse>updateJobsTypeResponseObservable(){
        return updateJobsTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteJobsTypeResponse>deleteJobsTypeResponseObservable(){
        return deleteJobsTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteJobsTypeResponse>deleteJobsTypeResponseObservable(String authKey,String jobTypeId){
        return pojoInterface
                .deleteJobType(authKey,jobTypeId)
                .doOnNext(deleteJobsTypeResponse -> deleteJobsTypeResponseBehaviorSubject.onNext(deleteJobsTypeResponse));
    }

}
