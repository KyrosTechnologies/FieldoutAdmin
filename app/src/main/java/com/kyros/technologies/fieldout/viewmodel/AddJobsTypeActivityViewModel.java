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
    public Observable<AddJobsTypeResponse>addJobsTypeResponseObservable(String authKey, JobType jobType,String idDomain){
        return pojoInterface
                .addJobsType(authKey,jobType,idDomain)
                .doOnNext(addJobsTypeResponse -> addJobsTypeResponseBehaviorSubject.onNext(addJobsTypeResponse));
    }
    public Observable<AddJobsTypeResponse>addJobsTypeResponseObservable(){
        return addJobsTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<AddJobsTypeResponse>updateJobsTypeResponseObservable(String authKey,String jobTypeId, JobType jobType,String idDomain){
        return pojoInterface
                .updateJobsType(authKey,jobTypeId,jobType,idDomain)
                .doOnNext(addJobsTypeResponse -> updateJobsTypeResponseBehaviorSubject.onNext(addJobsTypeResponse));
    }
    public Observable<AddJobsTypeResponse>updateJobsTypeResponseObservable(){
        return updateJobsTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteJobsTypeResponse>deleteJobsTypeResponseObservable(){
        return deleteJobsTypeResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteJobsTypeResponse>deleteJobsTypeResponseObservable(String authKey,String jobTypeId,String idDomain){
        return pojoInterface
                .deleteJobType(authKey,jobTypeId,idDomain)
                .doOnNext(deleteJobsTypeResponse -> deleteJobsTypeResponseBehaviorSubject.onNext(deleteJobsTypeResponse));
    }

}
