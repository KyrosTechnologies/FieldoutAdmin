package com.kyros.technologies.fieldout.viewmodel;

import android.util.Log;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.UsersResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 14-12-2017.
 */

public class UsersFragmentViewModel {
    private BehaviorSubject<UsersResponse>usersResponseBehaviorSubject=BehaviorSubject.create(new UsersResponse());
    private POJOInterface pojoInterface;
    private BehaviorSubject<Boolean>isLoading=BehaviorSubject.create(false);
    @Inject
    public UsersFragmentViewModel(POJOInterface pojoInterface){this.pojoInterface=pojoInterface;}
    public Observable<UsersResponse>getUsersResponse(String domainId,String authKey){
        if(isLoading.getValue()){
            return Observable.empty();
        }
        isLoading.onNext(true);
        return pojoInterface
                .getUsers(domainId,authKey)
                .doOnError(throwable -> Log.e("Error : ","getUsersError"+throwable.getMessage()))
                .doOnNext(usersResponse -> usersResponseBehaviorSubject.onNext(usersResponse))
                .doOnTerminate(()->isLoading.onNext(false));
    }
    public Observable<UsersResponse>getUsersResponseObservable(){
        return usersResponseBehaviorSubject.asObservable();
    }
    public BehaviorSubject<Boolean>getIsLoading(){
        return isLoading;
    }

}
