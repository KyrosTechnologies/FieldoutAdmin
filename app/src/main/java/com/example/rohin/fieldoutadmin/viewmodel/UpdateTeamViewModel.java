package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.SingleTeamResponse;
import com.example.rohin.fieldoutadmin.models.Tag;
import com.example.rohin.fieldoutadmin.models.TagResponse;
import com.example.rohin.fieldoutadmin.models.TeamDeleteResponse;
import com.example.rohin.fieldoutadmin.models.TeamsItem;
import com.example.rohin.fieldoutadmin.models.TeamsResponse;
import com.example.rohin.fieldoutadmin.models.UpdateTeamResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 21-12-2017.
 */

public class UpdateTeamViewModel {
    private BehaviorSubject<SingleTeamResponse>teamsResponseBehaviorSubject=BehaviorSubject.create(new SingleTeamResponse());
    private BehaviorSubject<UpdateTeamResponse>updateTeamResponseBehaviorSubject=BehaviorSubject.create(new UpdateTeamResponse());
    private BehaviorSubject<TagResponse>tagResponseBehaviorSubject=BehaviorSubject.create(new TagResponse());
    private BehaviorSubject<TeamDeleteResponse>teamDeleteResponseBehaviorSubject=BehaviorSubject.create(new TeamDeleteResponse());
    private POJOInterface pojoInterface;
    @Inject
    public UpdateTeamViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<SingleTeamResponse>getSingleTeamObservable(String authKey,String teamId){
        return pojoInterface
                .getSingleTeam(teamId,authKey)
                .doOnNext(teamsResponse -> teamsResponseBehaviorSubject.onNext(teamsResponse));
    }
    public Observable<SingleTeamResponse>getSingleTeamObservable(){
        return teamsResponseBehaviorSubject.asObservable();
    }
    public Observable<UpdateTeamResponse>updateTeamResponseObservable(String authKey,String teamId, TeamsItem teamsItem){
        return pojoInterface
                .updateTeam(teamId,authKey,teamsItem)
                .doOnNext(updateTeamResponse -> updateTeamResponseBehaviorSubject.onNext(updateTeamResponse));
    }
    public Observable<UpdateTeamResponse>updateTeamResponseObservable(){
        return updateTeamResponseBehaviorSubject.asObservable();
    }
    public Observable<TagResponse>getTags(String authKey,String domainId){
        return pojoInterface
                .getTags(domainId,authKey)
                .doOnNext(tagResponse -> tagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<TagResponse>upTagResponseObservable(){
        return tagResponseBehaviorSubject.asObservable();
    }
    public Observable<TeamDeleteResponse>teamDeleteResponseObservable(String teamId,String authKey){
        return pojoInterface
                .deleteTeam(teamId,authKey)
                .doOnNext(teamDeleteResponse -> teamDeleteResponseBehaviorSubject.onNext(teamDeleteResponse));
    }
    public Observable<TeamDeleteResponse>teamDeleteResponseObservable(){
        return teamDeleteResponseBehaviorSubject.asObservable();
    }
}
