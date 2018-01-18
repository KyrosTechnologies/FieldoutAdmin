package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.JobsTypeResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 26-12-2017.
 */

public class JobsTypesFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<JobsTypeResponse>jobsTypeResponseBehaviorSubject=BehaviorSubject.create(new JobsTypeResponse());

    @Inject
    public JobsTypesFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<JobsTypeResponse>jobsTypeResponseObservable(String domainId,String authKey){
        return pojoInterface
                .getJobsType(authKey,domainId)
                .doOnNext(jobsTypeResponse -> jobsTypeResponseBehaviorSubject.onNext(jobsTypeResponse));

    }
    public Observable<JobsTypeResponse>jobsTypeResponseObservable(){
        return jobsTypeResponseBehaviorSubject.asObservable();

    }
}
