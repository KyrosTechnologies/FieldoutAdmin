package com.example.rohin.fieldoutadmin.viewmodel;

import android.util.Log;

import com.example.rohin.fieldoutadmin.activity.AddUserActivity;
import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.AddUserResponse;
import com.example.rohin.fieldoutadmin.models.GetSingleUserResponse;
import com.example.rohin.fieldoutadmin.models.Result;
import com.example.rohin.fieldoutadmin.models.UserUpdateResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 16-12-2017.
 */

public class UpdateUserActivityViewModel {
    private BehaviorSubject<GetSingleUserResponse>singleUserResponseBehaviorSubject=BehaviorSubject.create(new GetSingleUserResponse());
    private BehaviorSubject<UserUpdateResponse>updateUserResponeBehaviourSubject=BehaviorSubject.create(new UserUpdateResponse());
    private POJOInterface pojoInterface;
    @Inject
    public UpdateUserActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<GetSingleUserResponse>getSingleUserResponseObservable(String userId,String authKey){
        return pojoInterface
                .getOneUserResponse(userId,authKey)
                .doOnNext(getSingleUserResponse -> singleUserResponseBehaviorSubject.onNext(getSingleUserResponse))
                .doOnError(throwable -> Log.e("Error : ",""+throwable.getMessage()));

    }
    public Observable<GetSingleUserResponse>getSingleUserResponseObservable(){
        return singleUserResponseBehaviorSubject.asObservable();
    }
    public Observable<UserUpdateResponse>getUpdateUserResponse(){
        return updateUserResponeBehaviourSubject.asObservable();
    }
    public Observable<UserUpdateResponse>updateUserObservable(String userId,String authKey,Result result){
        return pojoInterface.updateUserProfile(authKey,userId,result)
                .doOnNext(userUpdateResponse -> updateUserResponeBehaviourSubject.onNext(userUpdateResponse))
                .doOnError(throwable -> Log.e("Error : ",""+throwable.getMessage()));
    }
}
