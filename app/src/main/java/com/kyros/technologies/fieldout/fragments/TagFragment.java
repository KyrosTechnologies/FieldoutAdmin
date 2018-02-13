package com.kyros.technologies.fieldout.fragments;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentTagBinding;
import com.kyros.technologies.fieldout.models.AddTagResponse;
import com.kyros.technologies.fieldout.models.DeleteTagResponse;
import com.kyros.technologies.fieldout.models.Tag;
import com.kyros.technologies.fieldout.models.TagResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddTeamViewModel;
import com.kyros.technologies.fieldout.viewmodel.TagFragmentViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 08-01-2018.
 */

public class TagFragment extends Fragment {
    private String TAG=TagFragment.class.getSimpleName();
    private PreferenceManager store;
    private CompositeSubscription subscription;
    @Inject
    TagFragmentViewModel viewModel;
    private FragmentTagBinding binding;
    private AlertDialog addTagDialog;
    private AlertDialog updateTagDialog;
    @Inject
    AddTeamViewModel addTeamViewModel;
    private AlertDialog deleteDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectTagFragment(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getContext());
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_tag,container,false);
        View view=binding.getRoot();
        binding.buttonAddTag.setOnClickListener(view1 -> showAddTagDialog());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(authKey!=null && domainId!=null){
            initiateTagsAPI(authKey,domainId);
        }
    }

    private void initiateTagsAPI(String authKey, String domainId) {
        subscription.add(viewModel.getTags(authKey,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::getResponse,this::getErrorResponse,this::getCompleted));
    }

    private void getCompleted() {
    }

    private void getErrorResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void getResponse(TagResponse tagResponse) {
        if(tagResponse!=null){
    Log.d("TAG Response : ",TAG+" / / "+tagResponse);
            List<Tag>tagList=tagResponse.getTags();
            if(tagList!=null && tagList.size()!=0){
                binding.tableLayoutTags.removeAllViews();
                binding.tableLayoutTags.addView(binding.tableRowTags);
                for(Tag tag:tagList){
                    String id=tag.getId();
                    String name=tag.getName();
                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));

                    //TextView name
                    TextView nameTextView=new TextView(getContext());
                    if(name!=null){
                        nameTextView.setText(name);
                    }
                    nameTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowuserTextParams.setMargins(15,15,15,15);
                    nameTextView.setLayoutParams(tableRowuserTextParams);
                    nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    nameTextView.setTextColor(getResources().getColor(R.color.text_light));
                    tableRow.addView(nameTextView);
                    tableRow.setTag(tag);
                    tableRow.setOnClickListener(view -> {
                       Object value=view.getTag();
                        showUpdateTagDialog(tag);
                    });
                    binding.tableLayoutTags.addView(tableRow);


                }
            }
        }else {
            showToast("tag response is null!");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
        dismissAddTagDialog();
        dismissDeleteDialog();
        dismissUpdateTagDialog();
    }

    private void dismissDeleteDialog() {
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
    }
    private void showDeleteDialog(int id){

    }
    private void dismissUpdateTagDialog() {
        if(updateTagDialog!=null && updateTagDialog.isShowing()){
            updateTagDialog.dismiss();
        }
    }
    private void showUpdateTagDialog(Tag tag){
        String id=tag.getId();
        String tagNameInput=tag.getName();
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater=getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.dialog_add_tag,null);
        EditText editTextTagName=view.findViewById(R.id.edit_text_tag_name);
        if(tagNameInput!=null){
            editTextTagName.setText(tagNameInput);
        }
        builder.setView(view);
        builder.setPositiveButton("OK",((dialogInterface, i) -> {
            String tagName=editTextTagName.getText().toString();

            if(tagName!=null && !tagName.isEmpty()){
                Tag tags=new Tag();
                tags.setIdDomain(domainId);
                tags.setName(tagName);
                updateTagApi(id,authKey,tags);
            }else{
                showToast("please specify tag name!");
            }
        }));
        builder.setNegativeButton("Cancel",((dialogInterface, i) -> {
            dialogInterface.cancel();
        }));
        builder.setNeutralButton("Delete",((dialogInterface, i) -> {
            if(id!=null){
                showDeleteDialog(id);
            }else{
                showToast("cannot perform this action!");
            }
        }));
        updateTagDialog=builder.create();
        updateTagDialog.show();
    }

    private void updateTagApi(String id, String authKey, Tag tags) {
        subscription.add(viewModel.updateTags(authKey,id,tags,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::updateTagResponse,this::updateTagError,this::updateTagCompleted));
    }

    private void updateTagCompleted() {

    }

    private void updateTagError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void updateTagResponse(TagResponse tagResponse) {
        if(tagResponse!=null){
            Log.d("Update Res : ",TAG+" / / "+tagResponse);
            boolean success=tagResponse.getIsSuccess();
            if(success){
                showToast("Updated successfully");
                initiateTagsAPI(store.getToken(),store.getIdDomain());
            }else{
                showToast("update is not successfull");
            }

        }else{
            showToast("response is null!");
        }
    }

    private void showDeleteDialog(String id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage("Do you want to delete it?");
        builder.setPositiveButton("Delete",((dialogInterface, i) -> {
            initiateDeleteTagAPI(id);
        }));
        builder.setNegativeButton("Cancel",((dialogInterface, i) -> {
            dialogInterface.cancel();
        }));
        deleteDialog=builder.create();
        deleteDialog.show();
    }

    private void initiateDeleteTagAPI(String id) {
        subscription.add(viewModel.deleteTags(store.getToken(),id,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::deleteTagResponse,this::deleteTagError,this::deleteTagCompleted));
    }

    private void deleteTagCompleted() {
    }

    private void deleteTagError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void deleteTagResponse(DeleteTagResponse deleteTagResponse) {
        if(deleteTagResponse!=null){
            Log.d("Delete response : ",TAG+" / / "+deleteTagResponse);
            boolean success=deleteTagResponse.isResult();
            if(success){
                showToast("successfully deleted");
                initiateTagsAPI(store.getToken(),store.getIdDomain());
            }else{
                showToast("delete is not successful");
            }
        }else {
            showToast("response is null!");
        }
    }

    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void showAddTagDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater=getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.dialog_add_tag,null);
        EditText editTextTagName=view.findViewById(R.id.edit_text_tag_name);
        builder.setView(view);
        builder.setPositiveButton("OK",((dialogInterface, i) -> {
            String tagName=editTextTagName.getText().toString();
            String authKey=store.getToken();
            String domainId=store.getIdDomain();
            if(tagName!=null && !tagName.isEmpty()){
                Tag tag=new Tag();
                tag.setIdDomain(domainId);
                tag.setName(tagName);
                addTagAPI(authKey,tag);
            }else{
                showToast("please specify tag name!");
            }
        }));
        builder.setNegativeButton("Cancel",((dialogInterface, i) -> {
            dialogInterface.cancel();
        }));
        addTagDialog=builder.create();
        addTagDialog.show();

    }

    private void addTagAPI(String authKey,Tag tag) {
        subscription.add(addTeamViewModel.addTagResponseObservable(authKey,tag,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::addTagResponse,this::addTagError,this::addTagCompleted));
    }

    private void addTagCompleted() {

    }

    private void addTagError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());


    }

    private void addTagResponse(AddTagResponse addTagResponse) {
        if(addTagResponse!=null){
        Log.d("AddTagResponse : ",TAG+" / / "+addTagResponse);
        boolean success=addTagResponse.getIsSuccess();
        if(success){
            showToast("Tag added successfully!");
            initiateTagsAPI(store.getToken(),store.getIdDomain());

        }else{
            showToast("Tag is not added");
        }
        }else{
            showToast("response is null!");
        }

    }

    private void dismissAddTagDialog(){
        if(addTagDialog!=null && addTagDialog.isShowing()){
            addTagDialog.dismiss();
        }
    }
}
