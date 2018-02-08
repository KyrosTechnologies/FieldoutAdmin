package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.ProjectTypeResponse;

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
    public Observable<ProjectTypeResponse>projectTypeResponseObservable(String authKey,String domainId,String idDomain){
        return pojoInterface
                .getProjectType(authKey,domainId,idDomain)
                .doOnNext(projectTypeResponse -> projectTypeResponseBehaviorSubject.onNext(projectTypeResponse));
    }
    public Observable<ProjectTypeResponse>projectTypeResponseObservable(){
        return  projectTypeResponseBehaviorSubject.asObservable();
    }
}
