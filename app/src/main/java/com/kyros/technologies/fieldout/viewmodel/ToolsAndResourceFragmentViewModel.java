package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.AddToolsResourceResponse;
import com.kyros.technologies.fieldout.models.DeleteToolsAndResourcesResponse;
import com.kyros.technologies.fieldout.models.GetToolsAndResourcesResponse;
import com.kyros.technologies.fieldout.models.Resource;
import com.kyros.technologies.fieldout.models.UpdateToolsAndResourceResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 23-12-2017.
 */

public class ToolsAndResourceFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<AddToolsResourceResponse>addToolsResourceResponseBehaviorSubject=BehaviorSubject.create(new AddToolsResourceResponse());
    private BehaviorSubject<GetToolsAndResourcesResponse>getToolsAndResourcesResponseBehaviorSubject=BehaviorSubject.create(new GetToolsAndResourcesResponse());
    private BehaviorSubject<UpdateToolsAndResourceResponse>updateToolsAndResourceResponseBehaviorSubject=BehaviorSubject.create(new UpdateToolsAndResourceResponse());
    private BehaviorSubject<DeleteToolsAndResourcesResponse>deleteToolsAndResourcesResponseBehaviorSubject=BehaviorSubject.create(new DeleteToolsAndResourcesResponse());
    @Inject
    public ToolsAndResourceFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<AddToolsResourceResponse>addToolsResourceResponseObservable(String authKey, Resource resource,String idDomain){
        return pojoInterface
                .addToolsResource(authKey,resource,idDomain)
                .doOnNext(addToolsResourceResponse -> addToolsResourceResponseBehaviorSubject.onNext(addToolsResourceResponse));
    }
    public Observable<AddToolsResourceResponse>addToolsResourceResponseObservable(){
        return addToolsResourceResponseBehaviorSubject.asObservable();
    }
    public Observable<GetToolsAndResourcesResponse>getToolsAndResourcesResponseObservable(String authKey,String idDomain){
        return pojoInterface
                .getToolsResources(authKey,idDomain)
                .doOnNext(getToolsAndResourcesResponse -> getToolsAndResourcesResponseBehaviorSubject.onNext(getToolsAndResourcesResponse));
    }
    public Observable<GetToolsAndResourcesResponse>getToolsAndResourcesResponseObservable(){
        return getToolsAndResourcesResponseBehaviorSubject.asObservable();
    }
    public Observable<UpdateToolsAndResourceResponse>updateToolsAndResourceResponseObservable(String authKey,String resourceId,Resource resource,String idDomain){
        return pojoInterface
                .updateToolsResource(authKey,resource,resourceId,idDomain)
                .doOnNext(updateToolsAndResourceResponse -> updateToolsAndResourceResponseBehaviorSubject.onNext(updateToolsAndResourceResponse));
    }
    public Observable<UpdateToolsAndResourceResponse>updateToolsAndResourceResponseObservable(){
        return updateToolsAndResourceResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteToolsAndResourcesResponse>deleteToolsAndResourcesResponseObservable(String authKey,String resourceId,String idDomain){
        return pojoInterface
                .deleteToolsResource(authKey,resourceId,idDomain)
                .doOnNext(deleteToolsAndResourcesResponse -> deleteToolsAndResourcesResponseBehaviorSubject.onNext(deleteToolsAndResourcesResponse ));
    }
    public Observable<DeleteToolsAndResourcesResponse>deleteToolsAndResourcesResponseObservable(){
        return deleteToolsAndResourcesResponseBehaviorSubject.asObservable();
    }
}
