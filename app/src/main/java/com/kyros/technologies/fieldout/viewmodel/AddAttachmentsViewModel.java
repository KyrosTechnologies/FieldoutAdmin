package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddAttachments;
import com.kyros.technologies.fieldout.models.AddToolsResourceResponse;
import com.kyros.technologies.fieldout.models.Resource;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 02-02-2018.
 */

public class AddAttachmentsViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<ResponseBody>addResponseBodyBehaviorSubject=BehaviorSubject.create();
    private BehaviorSubject<ResponseBody>addResponseBodyBehaviorSubjectByteStream=BehaviorSubject.create();

    @Inject
    public AddAttachmentsViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<ResponseBody>addResponseBodyObservable(String authKey, Map<String, RequestBody> partMap, MultipartBody.Part file){
        return pojoInterface
                .addAttachments(authKey,partMap,file)
                .doOnNext(addToolsResourceResponse -> addResponseBodyBehaviorSubject.onNext(addToolsResourceResponse));
    }
    public Observable<ResponseBody>addResponseBodyObservable(){
        return addResponseBodyBehaviorSubject.asObservable();
    }

    public Observable<ResponseBody>addResponseBodyObservableByteStream(String authKey, AddAttachments addAttachments){
        return pojoInterface
                .addAttachmentsByteStream(authKey,addAttachments)
                .doOnNext(addToolsResourceResponse -> addResponseBodyBehaviorSubjectByteStream.onNext(addToolsResourceResponse));
    }
    public Observable<ResponseBody>addResponseBodyObservableByteStream(){
        return addResponseBodyBehaviorSubjectByteStream.asObservable();
    }
}
