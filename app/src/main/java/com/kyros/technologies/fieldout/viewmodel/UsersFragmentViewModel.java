package com.kyros.technologies.fieldout.viewmodel;

import android.util.Log;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.UsersItem;
import com.kyros.technologies.fieldout.models.UsersResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 14-12-2017.
 */

public class UsersFragmentViewModel {
    private BehaviorSubject<List<UsersItem>>usersResponseBehaviorSubject=BehaviorSubject.create(new ArrayList<>());
    private POJOInterface pojoInterface;
    private BehaviorSubject<Boolean>isLoading=BehaviorSubject.create(false);
    @Inject
    public UsersFragmentViewModel(POJOInterface pojoInterface){this.pojoInterface=pojoInterface;}
    public Observable<List<UsersItem>>getUsersResponse(String authKey, String idDomain){
        if(isLoading.getValue()){
            return Observable.empty();
        }
        isLoading.onNext(true);
        return pojoInterface
                .getUsers(authKey,idDomain)
                .doOnError(throwable -> Log.e("Error : ","getUsersError"+throwable.getMessage()))
                .doOnNext(usersResponse -> usersResponseBehaviorSubject.onNext(usersResponse))
                .doOnTerminate(()->isLoading.onNext(false));
    }
    public Observable<List<UsersItem>>getUsersResponseObservable(){
        return usersResponseBehaviorSubject.asObservable();
    }
    public BehaviorSubject<Boolean>getIsLoading(){
        return isLoading;
    }

}
