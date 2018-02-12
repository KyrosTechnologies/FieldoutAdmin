package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddUserResponse;
import com.kyros.technologies.fieldout.models.TeamsResponse;
import com.kyros.technologies.fieldout.models.UserInfo;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 15-12-2017.
 */

public class AddUserActivityViewModel {
    private BehaviorSubject<TeamsResponse>teamsResponseBehaviorSubject=BehaviorSubject.create(new TeamsResponse());
    private BehaviorSubject<AddUserResponse>addUserResponseBehaviorSubject=BehaviorSubject.create(new AddUserResponse());
    private POJOInterface pojoInterface;
    private BehaviorSubject<Boolean>isLoading=BehaviorSubject.create(false);
    @Inject
    public AddUserActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<TeamsResponse>getTeamResponse(String authKey,String idDomain){
        if(isLoading.getValue()){
            return Observable.empty();
        }
        isLoading.onNext(true);
        return pojoInterface
                .getTeams(authKey,idDomain)
                .doOnNext(teamsResponse -> teamsResponseBehaviorSubject.onNext(teamsResponse))
                .doOnTerminate(()->isLoading.onNext(false));
    }
    public Observable<TeamsResponse>getTeamResponseObservable(){
        return teamsResponseBehaviorSubject.asObservable();
    }
    public BehaviorSubject<Boolean>getIsLoading(){return isLoading;}
    public Observable<AddUserResponse>addUserResponseObservable(UserInfo userInfo, String authKey,String idDomain){
        if(isLoading.getValue()){
            return Observable.empty();
        }
        isLoading.onNext(true);
        return pojoInterface
                .addUser(userInfo,authKey,idDomain)
                .doOnNext(addUserResponse -> addUserResponseBehaviorSubject.onNext(addUserResponse))
                .doOnTerminate(()->isLoading.onNext(false));
    }
    public Observable<AddUserResponse>getUserResponseObservable(){
        return addUserResponseBehaviorSubject.asObservable();
    }
}
