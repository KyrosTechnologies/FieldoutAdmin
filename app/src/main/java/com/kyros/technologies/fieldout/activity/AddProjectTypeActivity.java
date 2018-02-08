package com.kyros.technologies.fieldout.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.ManagerListAdapter;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityAddProjectTypeBinding;
import com.kyros.technologies.fieldout.models.AddProjectTypeResponse;
import com.kyros.technologies.fieldout.models.DeleteProjectTypeResponse;
import com.kyros.technologies.fieldout.models.JobType;
import com.kyros.technologies.fieldout.models.JobTypeInfo;
import com.kyros.technologies.fieldout.models.JobsTypeResponse;
import com.kyros.technologies.fieldout.models.ProjectType;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddProjectTypeActivityViewModel;
import com.kyros.technologies.fieldout.viewmodel.JobsTypesFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddProjectTypeActivity extends AppCompatActivity {
    private ActivityAddProjectTypeBinding binding;
    private CompositeSubscription subscription;
    private String TAG=AddProjectTypeActivity.class.getSimpleName();
    private PreferenceManager store;
    private AlertDialog multipleSelectDialog;
    private List<String>selectedJobTypesList=new ArrayList<>();
    private List<String>selectedJobTypesIdList=new ArrayList<>();
    private ManagerListAdapter adapter;
    @Inject
    JobsTypesFragmentViewModel viewModelJobType;
    private List<JobType>jobTypeList=new ArrayList<>();
    private List<String>jobTypeNameList=new ArrayList<>();
    @Inject
    AddProjectTypeActivityViewModel viewModel;
    private ProgressDialog progressDialog;
    private String projectTypeId=null;
    private AlertDialog deleteDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddProjectTypeActivity(this);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_project_type);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
        try{
        ProjectType projectType=new Gson().fromJson(getIntent().getExtras().getString("projecttype"),ProjectType.class);
        projectTypeId=projectType.getId();
        String projectTypeName=projectType.getTypeName();
        if(projectTypeName!=null){
            binding.addProjectTypeEditText.setText(projectTypeName);
        }
        List<JobTypeInfo>jobTypeInfoList=projectType.getJobTypeInfo();
        if(jobTypeInfoList!=null && jobTypeInfoList.size()!=0){
            jobTypeNameList.clear();
            for(JobTypeInfo jobTypeInfo:jobTypeInfoList){
                jobTypeNameList.add(jobTypeInfo.getJobTypeName());

            }
            if(jobTypeNameList!=null && jobTypeNameList.size()!=0){
                binding.recyclerJobSequence.setVisibility(View.VISIBLE);
                binding.recyclerJobSequence.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerJobSequence.setItemAnimator(new DefaultItemAnimator());
                adapter=new ManagerListAdapter(this,jobTypeNameList);
                binding.recyclerJobSequence.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }


        }catch (Exception e){
            e.printStackTrace();
        }
        binding.saveAddProjectButton.setOnClickListener(view -> {
            if(projectTypeId==null){
                validateFields();
            }else{
                updateValidateFields();
            }
        });
        binding.addProjectTypeJobSequenceTextView.setOnClickListener(view -> {
            String[] arrayManager = new String[jobTypeNameList.size()];
            boolean[] boolManager=new boolean[jobTypeNameList.size()];
            for(int j =0;j<jobTypeNameList.size();j++){
                arrayManager[j] = jobTypeNameList.get(j);
            }
            multipleSelectDialogBox("Select job sequence",arrayManager,boolManager);
        });
    }

    private void updateValidateFields() {
        String typeName=binding.addProjectTypeEditText.getText().toString();
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        List<String>jbList=adapter.getManagersList();
        if(jbList.size()!=0 && jbList!=null){
            for(String jobTypeName:jbList){
                for(JobType jobType:jobTypeList){
                    if(jobTypeName.toLowerCase().equals(jobType.getJobTypeName().toLowerCase())){
                        selectedJobTypesIdList.add(jobType.getId());
                    }
                }
            }
        }
        if(typeName!=null && !typeName.isEmpty()){
            ProjectType projectType=new ProjectType();
            projectType.setIdDomain(domainId);
            projectType.setTypeName(typeName);
            projectType.setJobSequences(selectedJobTypesIdList);
            initiateUpdateAPICall(authKey,projectType);
            showProgressDialog();
        }else{
            showToast("please enter all fields!");
        }
    }

    private void initiateUpdateAPICall(String authKey, ProjectType projectType) {
        subscription.add(viewModel.updateProjectTypeResponseObservable(authKey,projectTypeId,projectType,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::updateResponse,this::errorUpdateResponse,this::completedUpdateResponse));
    }

    private void completedUpdateResponse() {
    }

    private void errorUpdateResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void updateResponse(AddProjectTypeResponse addProjectTypeResponse) {
        if(addProjectTypeResponse!=null){
            Log.d("updateRespone: ",TAG+" / / "+addProjectTypeResponse.toString());
            boolean success=addProjectTypeResponse.getIsSuccess();
            if(success){
                this.finish();
                showToast("updated Successfully");
            }else{
                showToast("Update not successful!");
            }
        }else {
            showToast("update response is null");
        }
    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    private void dismissProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void validateFields() {
        String typeName=binding.addProjectTypeEditText.getText().toString();
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(selectedJobTypesList.size()!=0 && selectedJobTypesList!=null){
            for(String jobTypeName:selectedJobTypesList){
                for(JobType jobType:jobTypeList){
                    if(jobTypeName.toLowerCase().equals(jobType.getJobTypeName().toLowerCase())){
                        selectedJobTypesIdList.add(jobType.getId());
                    }
                }
            }
        }
        if(typeName!=null && !typeName.isEmpty()){
            ProjectType projectType=new ProjectType();
            projectType.setIdDomain(domainId);
            projectType.setTypeName(typeName);
            projectType.setJobSequences(selectedJobTypesIdList);
            initiateAPICall(authKey,projectType);
            showProgressDialog();
        }else{
            showToast("please enter all fields!");
        }


    }

    private void initiateAPICall(String authKey, ProjectType projectType) {
        subscription.add(viewModel.addProjectTypeResponseObservable(authKey,projectType,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::responseAddProjectType,this::errorAddProjectType,this::completedAddProjectType));
    }

    private void completedAddProjectType() {
        dismissProgressDialog();
    }

    private void errorAddProjectType(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        dismissProgressDialog();
        showToast(""+throwable.getMessage());

    }

    private void responseAddProjectType(AddProjectTypeResponse addProjectTypeResponse) {
        dismissProgressDialog();
        if(addProjectTypeResponse!=null){
            Log.d("Project add res: ",TAG+" / / "+addProjectTypeResponse.toString());
            boolean success=addProjectTypeResponse.getIsSuccess();
            if(success){
                this.finish();
                showToast("Successfully added");
            }else{
                showToast("not successfull!");
            }
        }else {
            showToast("response is null");
        }
    }

    private void multipleSelectDialogBox(String title,String[] values,boolean[] checkedItems){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values, checkedItems, (dialog, which, isChecked) -> {
            if(isChecked){
                    selectedJobTypesList.add(values[which]);
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {

                binding.recyclerJobSequence.setVisibility(View.VISIBLE);
                binding.recyclerJobSequence.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerJobSequence.setItemAnimator(new DefaultItemAnimator());
                adapter=new ManagerListAdapter(this,new HashSet<>(selectedJobTypesList));
                binding.recyclerJobSequence.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            dismissMultipleSelectDialogBox();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dismissMultipleSelectDialogBox());

        multipleSelectDialog = builder.create();
        multipleSelectDialog.show();


    }
    private void dismissMultipleSelectDialogBox(){
        if(multipleSelectDialog!=null && multipleSelectDialog.isShowing()){
            multipleSelectDialog.dismiss();
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        callJobTypeAPI(domainId,authKey);
    }

    private void callJobTypeAPI(String domainId, String authKey) {
        subscription.add(viewModelJobType.jobsTypeResponseObservable(authKey,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+ " / / "+throwable.getMessage()))
        .subscribe(this::responseJobTypes,this::errorJobTypes,this::completedJobTypes));
    }

    private void completedJobTypes() {
    }

    private void errorJobTypes(Throwable throwable) {
        Log.e("Error : ",TAG+ " / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    private void responseJobTypes(JobsTypeResponse jobsTypeResponse) {
        if(jobsTypeResponse!=null){
        Log.d("JobTypeResponse : ",TAG+" / / "+jobsTypeResponse.toString());
            jobTypeList=jobsTypeResponse.getJobTypes();
        if(jobTypeList!=null && jobTypeList.size()!=0){
            for(JobType  jobType:jobTypeList){
                String jobTypeName=jobType.getJobTypeName();
                String jobTypeId=jobType.getId();
                jobTypeNameList.add(jobTypeName);
            }
        }
        }else {
            showToast("response is null");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissMultipleSelectDialogBox();
        dismissProgressDialog();
        dismissDeleteDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_delete:
                if(projectTypeId!=null){
                    showDeleteDialog();
                }else{
                    showToast("you cannot delete new one");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Delete it?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                (dialog, id) ->{
                    deleteProjectType();
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());
        deleteDialog = builder.create();
        deleteDialog.show();

    }

    private void deleteProjectType() {
        if(projectTypeId!=null){
            subscription.add(viewModel.deleteProjectTypeResponseObservable(store.getToken(),projectTypeId,store.getIdDomain())
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::responseDeleteProjectType,this::errorDeleteProjectType,this::completeDeleteProjectType));
        }else{
            showToast("project type  id is null");
        }
    }

    private void completeDeleteProjectType() {

    }

    private void errorDeleteProjectType(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
    }

    private void responseDeleteProjectType(DeleteProjectTypeResponse deleteProjectTypeResponse) {
        if(deleteProjectTypeResponse!=null){
            Log.d("Del Projec Typ Res : ",TAG+" / / "+deleteProjectTypeResponse.toString());
            boolean success=deleteProjectTypeResponse.isResult();
            if(success){
                showToast("successfully deleted");
                this.finish();
            }
        }else {
            showToast("delete response is null");
        }
    }

    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
           deleteDialog.dismiss();
        }
    }

}
