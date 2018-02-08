package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.SingleTeamResponse;
import com.kyros.technologies.fieldout.models.TagResponse;
import com.kyros.technologies.fieldout.models.TeamDeleteResponse;
import com.kyros.technologies.fieldout.models.TeamsItem;
import com.kyros.technologies.fieldout.models.UpdateTeamResponse;

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
    public Observable<SingleTeamResponse>getSingleTeamObservable(String authKey,String teamId,String idDomain){
        return pojoInterface
                .getSingleTeam(teamId,authKey,idDomain)
                .doOnNext(teamsResponse -> teamsResponseBehaviorSubject.onNext(teamsResponse));
    }
    public Observable<SingleTeamResponse>getSingleTeamObservable(){
        return teamsResponseBehaviorSubject.asObservable();
    }
    public Observable<UpdateTeamResponse>updateTeamResponseObservable(String authKey,String teamId, TeamsItem teamsItem,String idDomain){
        return pojoInterface
                .updateTeam(teamId,authKey,teamsItem,idDomain)
                .doOnNext(updateTeamResponse -> updateTeamResponseBehaviorSubject.onNext(updateTeamResponse));
    }
    public Observable<UpdateTeamResponse>updateTeamResponseObservable(){
        return updateTeamResponseBehaviorSubject.asObservable();
    }
    public Observable<TagResponse>getTags(String authKey,String domainId,String idDomain){
        return pojoInterface
                .getTags(domainId,authKey,idDomain)
                .doOnNext(tagResponse -> tagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<TagResponse>upTagResponseObservable(){
        return tagResponseBehaviorSubject.asObservable();
    }
    public Observable<TeamDeleteResponse>teamDeleteResponseObservable(String teamId,String authKey,String idDomain){
        return pojoInterface
                .deleteTeam(teamId,authKey,idDomain)
                .doOnNext(teamDeleteResponse -> teamDeleteResponseBehaviorSubject.onNext(teamDeleteResponse));
    }
    public Observable<TeamDeleteResponse>teamDeleteResponseObservable(){
        return teamDeleteResponseBehaviorSubject.asObservable();
    }
}
