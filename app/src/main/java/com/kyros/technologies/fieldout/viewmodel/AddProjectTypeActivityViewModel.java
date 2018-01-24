package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddProjectTypeResponse;
import com.kyros.technologies.fieldout.models.DeleteProjectTypeResponse;
import com.kyros.technologies.fieldout.models.ProjectType;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 28-12-2017.
 */

public class AddProjectTypeActivityViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<AddProjectTypeResponse>addProjectTypeResponseBehaviorSubject=BehaviorSubject.create(new AddProjectTypeResponse());
    private BehaviorSubject<DeleteProjectTypeResponse>deleteProjectTypeResponseBehaviorSubject=BehaviorSubject.create(new DeleteProjectTypeResponse());
    private BehaviorSubject<AddProjectTypeResponse>updateProjectTypeResponseBehaviorSubject=BehaviorSubject.create(new AddProjectTypeResponse());

    @Inject
    public AddProjectTypeActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<AddProjectTypeResponse>addProjectTypeResponseObservable(String authKey, ProjectType projectType){
        return pojoInterface
                .addProjecType(authKey,projectType)
                .doOnNext(addProjectTypeResponse -> addProjectTypeResponseBehaviorSubject.onNext(addProjectTypeResponse));
    }
    public Observable<AddProjectTypeResponse>addProjectTypeResponseObservable(){
        return addProjectTypeResponseBehaviorSubject.asObservable();
    }

    public Observable<DeleteProjectTypeResponse>deleteProjectTypeResponseObservable(String authKey, String projectTypeId){
        return pojoInterface
                .deleteProjectType(authKey,projectTypeId)
                .doOnNext(deleteProjectTypeResponse  -> deleteProjectTypeResponseBehaviorSubject.onNext(deleteProjectTypeResponse));
    }
    public Observable<DeleteProjectTypeResponse>deleteProjectTypeResponseObservable(){
        return deleteProjectTypeResponseBehaviorSubject.asObservable();
    }


    public Observable<AddProjectTypeResponse>updateProjectTypeResponseObservable(String authKey,String projectTypeId, ProjectType projectType){
        return pojoInterface
                .updateProjectType(authKey,projectTypeId,projectType)
                .doOnNext(addProjectTypeResponse -> updateProjectTypeResponseBehaviorSubject.onNext(addProjectTypeResponse));
    }
    public Observable<AddProjectTypeResponse>updateProjectTypeResponseObservable(){
        return updateProjectTypeResponseBehaviorSubject.asObservable();
    }
}
