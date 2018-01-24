package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.User;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 18-01-2018.
 */

public class ChangePasswordActivityViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<User>changePasswordBehaviorSubject=BehaviorSubject.create(new User());
    @Inject
    public ChangePasswordActivityViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<User>changePassword(String authKey,String userId,User user){
        return pojoInterface
                .changePassword(authKey,userId,user)
                .doOnNext(users -> changePasswordBehaviorSubject.onNext(users));
    }
    public Observable<User>changePassword(){
        return  changePasswordBehaviorSubject.asObservable();
    }

}
