package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.ChangePasswordResponse;
import com.kyros.technologies.fieldout.models.User;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 18-01-2018.
 */

public class ChangePasswordActivityViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<ChangePasswordResponse>changePasswordBehaviorSubject=BehaviorSubject.create(new ChangePasswordResponse());
    @Inject
    public ChangePasswordActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<ChangePasswordResponse>changePassword(String authKey,String userId,User user){
        return pojoInterface
                .changePassword(authKey,userId,user)
                .doOnNext(users -> changePasswordBehaviorSubject.onNext(users));
    }
    public Observable<ChangePasswordResponse>changePassword(){
        return  changePasswordBehaviorSubject.asObservable();
    }

}
