package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.ProjectTypeResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 28-12-2017.
 */

public class ProjectTypeFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<ProjectTypeResponse>projectTypeResponseBehaviorSubject=BehaviorSubject.create(new ProjectTypeResponse());
    @Inject
    public ProjectTypeFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<ProjectTypeResponse>projectTypeResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getProjectType(authKey,domainId)
                .doOnNext(projectTypeResponse -> projectTypeResponseBehaviorSubject.onNext(projectTypeResponse));
    }
    public Observable<ProjectTypeResponse>projectTypeResponseObservable(){
        return  projectTypeResponseBehaviorSubject.asObservable();
    }
}
