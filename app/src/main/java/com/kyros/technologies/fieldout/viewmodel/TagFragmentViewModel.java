package com.kyros.technologies.fieldout.viewmodel;

import com.kyros.technologies.fieldout.interfaceclass.POJOInterface;
import com.kyros.technologies.fieldout.models.DeleteTagResponse;
import com.kyros.technologies.fieldout.models.Tag;
import com.kyros.technologies.fieldout.models.TagResponse;

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
    public Observable<TagResponse> getTags(String authKey,String idDomain){
        return pojoInterface
                .getTags(authKey,idDomain)
                .doOnNext(tagResponse -> tagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<TagResponse> getTags(){
        return tagResponseBehaviorSubject.asObservable();
    }
    public Observable<TagResponse> updateTags(String authKey, String tagid,Tag tag,String idDomain){
        return pojoInterface
                .updateTag(authKey,tagid,tag,idDomain)
                .doOnNext(tagResponse -> updateTagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<TagResponse> updateTags(){
        return updateTagResponseBehaviorSubject.asObservable();
    }
    public Observable<DeleteTagResponse> deleteTags(String authKey, String tagid,String idDomain){
        return pojoInterface
                .deleteTag(authKey,tagid,idDomain)
                .doOnNext(tagResponse -> deleteTagResponseBehaviorSubject.onNext(tagResponse));
    }
    public Observable<DeleteTagResponse> delteTags(){
        return deleteTagResponseBehaviorSubject.asObservable();
    }
}
