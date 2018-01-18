package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.AddSchedulingResponse;
import com.example.rohin.fieldoutadmin.models.DeleteScheduleWindowsResponse;
import com.example.rohin.fieldoutadmin.models.SchedulingWindow;

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
    public Observable<AddSchedulingResponse>addSchedulingResponseObservable(String authKey, SchedulingWindow schedulingWindow){
        return pojoInterface
                .addSchedule(authKey,schedulingWindow)
                .doOnNext(addSchedulingResponse -> addSchedulingResponseBehaviorSubject.onNext(addSchedulingResponse));
    }
    public Observable<AddSchedulingResponse>addSchedulingResponseObservable(){
        return  addSchedulingResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteScheduleWindowsResponse>deleteScheduleWindowsResponseObservable(String authKey,String scheduleId){
        return pojoInterface
                .deleteSchedule(authKey,scheduleId)
                .doOnNext(deleteScheduleWindowsResponse -> deleteScheduleWindowsResponseBehaviorSubject.onNext(deleteScheduleWindowsResponse));
    }
    public Observable<DeleteScheduleWindowsResponse>deleteScheduleWindowsResponseObservable(){
        return  deleteScheduleWindowsResponseBehaviorSubject.asObservable();
    }


    public Observable<AddSchedulingResponse>updateSchedulingResponseObservable(String authKey,String schedulingId, SchedulingWindow schedulingWindow){
        return pojoInterface
                .updateScheduling(authKey,schedulingId,schedulingWindow)
                .doOnNext(addSchedulingResponse -> updateSchedulingResponseBehaviorSubject.onNext(addSchedulingResponse));
    }
    public Observable<AddSchedulingResponse>updateSchedulingResponseObservable(){
        return  updateSchedulingResponseBehaviorSubject.asObservable();
    }
}
