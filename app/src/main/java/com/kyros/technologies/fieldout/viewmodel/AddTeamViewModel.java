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
    public Observable<TechniciansResponse>techniciansResponseObservable(String authKey,String idDomain){
        return pojoInterface.getTechniciansResponse(authKey,idDomain)
                .doOnNext(techniciansResponse -> techniciansResponseBehaviorSubject.onNext(techniciansResponse));
    }
    public Observable<TechniciansResponse>getTechniciansResponse(){
        return techniciansResponseBehaviorSubject.asObservable();
    }
    public Observable<ManagersResponse>managersResponseObservable(String authKey,String idDomain){
        return pojoInterface
                .getManagersResponse(authKey,idDomain)
                .doOnNext(managersResponse -> managersResponseBehaviorSubject.onNext(managersResponse));
    }
    public Observable<ManagersResponse>getManagersResponse(){
        return managersResponseBehaviorSubject.asObservable();
    }
    public Observable<AddTagResponse>addTagResponseObservable(String authKey, Tag tag,String idDomain){
        return pojoInterface
                .addTag(authKey,tag,idDomain)
                .doOnNext(addTagResponse -> addTagResponseBehaviorSubject.onNext(addTagResponse));
    }
    public Observable<AddTagResponse>addTagResponse(){
        return addTagResponseBehaviorSubject.asObservable();
    }
    public Observable<AddTeamResponse>addTeamResponseObservable(String authKey, TeamsItem teamsItem,String idDomain){
        return pojoInterface
                .addTeam(teamsItem,authKey,idDomain)
                .doOnNext(addTeamResponse -> addTeamResponseBehaviorSubject.onNext(addTeamResponse));
    }
    public Observable<AddTeamResponse>addTeamResponseObservable(){
        return addTeamResponseBehaviorSubject.asObservable();
    }
}
