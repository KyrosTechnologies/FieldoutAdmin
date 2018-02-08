package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddSchedulingResponse;
import com.kyros.technologies.fieldout.models.DeleteScheduleWindowsResponse;
import com.kyros.technologies.fieldout.models.SchedulingWindow;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 28-12-2017.
 */

public class AddSchedulingWindowsViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<AddSchedulingResponse>addSchedulingResponseBehaviorSubject=BehaviorSubject.create(new AddSchedulingResponse());
    private BehaviorSubject<DeleteScheduleWindowsResponse>deleteScheduleWindowsResponseBehaviorSubject=BehaviorSubject.create(new DeleteScheduleWindowsResponse());
    private BehaviorSubject<AddSchedulingResponse>updateSchedulingResponseBehaviorSubject=BehaviorSubject.create(new AddSchedulingResponse());

    @Inject
    public AddSchedulingWindowsViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<AddSchedulingResponse>addSchedulingResponseObservable(String authKey, SchedulingWindow schedulingWindow,String idDomain){
        return pojoInterface
                .addSchedule(authKey,schedulingWindow,idDomain)
                .doOnNext(addSchedulingResponse -> addSchedulingResponseBehaviorSubject.onNext(addSchedulingResponse));
    }
    public Observable<AddSchedulingResponse>addSchedulingResponseObservable(){
        return  addSchedulingResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteScheduleWindowsResponse>deleteScheduleWindowsResponseObservable(String authKey,String scheduleId,String idDomain){
        return pojoInterface
                .deleteSchedule(authKey,scheduleId,idDomain)
                .doOnNext(deleteScheduleWindowsResponse -> deleteScheduleWindowsResponseBehaviorSubject.onNext(deleteScheduleWindowsResponse));
    }
    public Observable<DeleteScheduleWindowsResponse>deleteScheduleWindowsResponseObservable(){
        return  deleteScheduleWindowsResponseBehaviorSubject.asObservable();
    }


    public Observable<AddSchedulingResponse>updateSchedulingResponseObservable(String authKey,String schedulingId, SchedulingWindow schedulingWindow,String idDomain){
        return pojoInterface
                .updateScheduling(authKey,schedulingId,schedulingWindow,idDomain)
                .doOnNext(addSchedulingResponse -> updateSchedulingResponseBehaviorSubject.onNext(addSchedulingResponse));
    }
    public Observable<AddSchedulingResponse>updateSchedulingResponseObservable(){
        return  updateSchedulingResponseBehaviorSubject.asObservable();
    }
}
