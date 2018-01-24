package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddTagResponse;
import com.kyros.technologies.fieldout.models.AddTeamResponse;
import com.kyros.technologies.fieldout.models.ManagersResponse;
import com.kyros.technologies.fieldout.models.Tag;
import com.kyros.technologies.fieldout.models.TeamsItem;
import com.kyros.technologies.fieldout.models.TechniciansResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 20-12-2017.
 */

public class AddTeamViewModel {
    private BehaviorSubject<TechniciansResponse>techniciansResponseBehaviorSubject=BehaviorSubject.create(new TechniciansResponse());
    private BehaviorSubject<ManagersResponse>managersResponseBehaviorSubject=BehaviorSubject.create(new ManagersResponse());
    private BehaviorSubject<AddTagResponse>addTagResponseBehaviorSubject=BehaviorSubject.create(new AddTagResponse());
    private BehaviorSubject<AddTeamResponse>addTeamResponseBehaviorSubject=BehaviorSubject.create(new AddTeamResponse());
    private POJOInterface pojoInterface;
    @Inject
    public AddTeamViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<TechniciansResponse>techniciansResponseObservable(String domainId,String authKey){
        return pojoInterface.getTechniciansResponse(domainId,authKey)
                .doOnNext(techniciansResponse -> techniciansResponseBehaviorSubject.onNext(techniciansResponse));
    }
    public Observable<TechniciansResponse>getTechniciansResponse(){
        return techniciansResponseBehaviorSubject.asObservable();
    }
    public Observable<ManagersResponse>managersResponseObservable(String domainId,String authKey){
        return pojoInterface
                .getManagersResponse(domainId,authKey)
                .doOnNext(managersResponse -> managersResponseBehaviorSubject.onNext(managersResponse));
    }
    public Observable<ManagersResponse>getManagersResponse(){
        return managersResponseBehaviorSubject.asObservable();
    }
    public Observable<AddTagResponse>addTagResponseObservable(String authKey, Tag tag){
        return pojoInterface
                .addTag(authKey,tag)
                .doOnNext(addTagResponse -> addTagResponseBehaviorSubject.onNext(addTagResponse));
    }
    public Observable<AddTagResponse>addTagResponse(){
        return addTagResponseBehaviorSubject.asObservable();
    }
    public Observable<AddTeamResponse>addTeamResponseObservable(String authKey, TeamsItem teamsItem){
        return pojoInterface
                .addTeam(teamsItem,authKey)
                .doOnNext(addTeamResponse -> addTeamResponseBehaviorSubject.onNext(addTeamResponse));
    }
    public Observable<AddTeamResponse>addTeamResponseObservable(){
        return addTeamResponseBehaviorSubject.asObservable();
    }
}