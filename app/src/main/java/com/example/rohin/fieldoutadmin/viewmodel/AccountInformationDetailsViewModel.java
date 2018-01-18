package com.example.rohin.fieldoutadmin.viewmodel;

import android.util.Log;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.Result;
import com.example.rohin.fieldoutadmin.models.UserUpdateResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 14-12-2017.
 */

public class AccountInformationDetailsViewModel {
    private BehaviorSubject<UserUpdateResponse> userInfoBehaviorSubject=BehaviorSubject.create(new UserUpdateResponse());
    private POJOInterface pojoInterface;
    private BehaviorSubject<Boolean>isLoading=BehaviorSubject.create(false);

    @Inject
    public AccountInformationDetailsViewModel(POJOInterface pojoInterface){this.pojoInterface=pojoInterface;}
    public Observable<UserUpdateResponse>updateUserInformation(Result result, String authKey, String userId){
        if(isLoading.getValue()){
            return Observable.empty();
        }
        isLoading.onNext(true);

        return pojoInterface
                .updateUserProfile(authKey,userId, result)
                .doOnError(throwable -> Log.e("Error : ","updateUserInformation : "+throwable.getMessage()))
                .doOnNext(userInfoResponse->userInfoBehaviorSubject.onNext(userInfoResponse))
                .doOnTerminate(()->isLoading.onNext(false));
    }
    public Observable<UserUpdateResponse>getUserUpdateResponseObservable(){
        return userInfoBehaviorSubject.asObservable();
    }
    public BehaviorSubject<Boolean>getIsLoading(){
        return isLoading;
    }

}
