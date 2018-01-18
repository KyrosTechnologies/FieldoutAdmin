package com.example.rohin.fieldoutadmin.viewmodel;

import com.example.rohin.fieldoutadmin.interfaceclass.POJOInterface;
import com.example.rohin.fieldoutadmin.models.DeleteTagResponse;
import com.example.rohin.fieldoutadmin.models.Tag;
import com.example.rohin.fieldoutadmin.models.TagResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by kyros on 08-01-2018.
 */

public class TagFragmentViewModel {
    private POJOInterface pojoInterface;
    private BehaviorSubject<TagResponse> tagResponseBehaviorSubject=BehaviorSubject.create(new TagResponse());
    private BehaviorSubject<TagResponse> updateTagResponseBehaviorSubject=BehaviorSubject.create(new TagResponse());
    private BehaviorSubject<DeleteTagResponse> deleteTagResponseBehaviorSubject=BehaviorSubject.create(new DeleteTagResponse());

    @Inject
    public TagFragmentViewModel(POJOInterface pojoInterface) {
        this.pojoInterface = pojoInterface;
    }
    public Observable<TagResponse> getTags(String authKey, String domainId){
        return pojoInterface
                .getTags(domainId,authKey)
                .doOnNext(tagResponse -> tagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<TagResponse> getTags(){
        return tagResponseBehaviorSubject.asObservable();
    }
    public Observable<TagResponse> updateTags(String authKey, String tagid,Tag tag){
        return pojoInterface
                .updateTag(authKey,tagid,tag)
                .doOnNext(tagResponse -> updateTagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<TagResponse> updateTags(){
        return updateTagResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteTagResponse> deleteTags(String authKey, String tagid){
        return pojoInterface
                .deleteTag(authKey,tagid)
                .doOnNext(tagResponse -> deleteTagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<DeleteTagResponse> delteTags(){
        return deleteTagResponseBehaviorSubject.asObservable();
    }
}
