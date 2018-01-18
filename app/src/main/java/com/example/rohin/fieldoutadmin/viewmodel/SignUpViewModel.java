package com.example.rohin.fieldoutadmin.viewmodel;

import android.util.Log;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.SignUpModel;


import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 12-12-2017.
 */

public class SignUpViewModel {
    private BehaviorSubject<SignUpModel>signUpModelBehaviorSubject=BehaviorSubject.create(new SignUpModel());
    private POJOInterface pojoInterface;
    @Inject
    public SignUpViewModel(POJOInterface pojoInterface){
        this.pojoInterface=pojoInterface;
    }
    public Observable<SignUpModel> signUpModelObservable(SignUpModel model){

                return pojoInterface
                        .getSignUpResponse(model)
                        .doOnError(throwable -> Log.e("Error : ","Signup : "+throwable.getMessage()))
                        .doOnNext(signUpModel->{
                            signUpModelBehaviorSubject.onNext(signUpModel);
                        });
                        //.doOnTerminate();
    }
    public Observable<SignUpModel>getSigninResponse(){
        return signUpModelBehaviorSubject.asObservable();
    }
}
