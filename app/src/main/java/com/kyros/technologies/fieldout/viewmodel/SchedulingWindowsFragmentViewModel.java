package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.ScheduleResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 28-12-2017.
 */

public class SchedulingWindowsFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<ScheduleResponse>scheduleResponseBehaviorSubject=BehaviorSubject.create(new ScheduleResponse());
    @Inject
    public SchedulingWindowsFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<ScheduleResponse>scheduleResponseObservable(String authKey,String domainId){
        return pojoInterface
                .getSchedule(authKey,domainId)
                .doOnNext(scheduleResponse -> scheduleResponseBehaviorSubject.onNext(scheduleResponse));
    }
}
