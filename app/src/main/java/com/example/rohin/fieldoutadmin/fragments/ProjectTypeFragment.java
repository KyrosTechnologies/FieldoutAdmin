package com.example.rohin.fieldoutadmin.fragments;

import android.content.Intent;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.activity.AddProjectTypeActivity;
import com.example.rohin.fieldoutadmin.activity.AddSchedulingWindowsActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentProjectTypeBinding;
import com.example.rohin.fieldoutadmin.models.JobTypeInfo;
import com.example.rohin.fieldoutadmin.models.ProjectType;
import com.example.rohin.fieldoutadmin.models.ProjectTypeResponse;
import com.example.rohin.fieldoutadmin.models.SchedulingWindow;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.ProjectTypeFragmentViewModel;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 28-12-2017.
 */

public class ProjectTypeFragment extends Fragment {
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG=ProjectTypeFragment.class.getSimpleName();
    private FragmentProjectTypeBinding binding;
    @Inject
    ProjectTypeFragmentViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectProjectTypeFragment(this);
      binding= DataBindingUtil.inflate(inflater,R.layout.fragment_project_type,container,false);
      subscription=new CompositeSubscription();
      store=PreferenceManager.getInstance(getContext());
      View view=binding.getRoot();
      binding.addProjectTypeButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddProjectTypeActivity.class)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        initiateAPICall(authKey,domainId);

    }

    private void initiateAPICall(String authKey, String domainId) {
        subscription.add(viewModel.projectTypeResponseObservable(authKey,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::responseProjectType,this::errorProjectType,this::completedProjectType));
    }

    private void completedProjectType() {
    }

    private void errorProjectType(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void responseProjectType(ProjectTypeResponse projectTypeResponse) {
        if(projectTypeResponse!=null){
            Log.d("Project type res : ",TAG+" / / "+projectTypeResponse.toString());
            List<ProjectType> projectTypeList=projectTypeResponse.getProjectTypes();
            if(projectTypeList!=null && projectTypeList.size()!=0){
                binding.scheduleTableLayout.removeAllViews();
                binding.scheduleTableLayout.addView(binding.rowScheduleTable);
                for(ProjectType projectType:projectTypeList){
                    String typeName=projectType.getTypeName();
                    List<String>jobSequences=projectType.getJobSequences();
                    List<JobTypeInfo>jobTypeInfos=projectType.getJobTypeInfo();


                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    //label Name
                    TextView typeTextView=new TextView(getContext());
                    if(typeName!=null){
                        typeTextView.setText(typeName);
                    }
                    typeTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowTypeNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowTypeNameParams.setMargins(10,10,0,10);
                    typeTextView.setLayoutParams(tableRowTypeNameParams);
                    typeTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    typeTextView.setTextColor(getResources().getColor(R.color.light_black));
                    typeTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(typeTextView);

                    //Job Sequence
                    TextView jobSequenceTextView=new TextView(getContext());
                    if(jobTypeInfos!=null && jobTypeInfos.size()!=0){
                        String data=jobTypeInfos.get(0).getJobTypeName();
                        if(data==null){
                            data="";
                        }
                        jobSequenceTextView.setText(data);
                    }
                    jobSequenceTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowJobSequenceParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowJobSequenceParams.setMargins(10,10,10,10);
                    jobSequenceTextView.setLayoutParams(tableRowJobSequenceParams);
                    jobSequenceTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    jobSequenceTextView.setTextColor(getResources().getColor(R.color.light_black));
                    jobSequenceTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(jobSequenceTextView);

                    tableRow.setTag(projectType);
                    tableRow.setOnClickListener(view1 ->{
                        Object valueId=view1.getTag();
                        Log.d("Team Id : ",TAG+" / / "+valueId.toString());
                        Intent intent=new Intent(getContext(),AddProjectTypeActivity.class);
                        intent.putExtra("projecttype",new Gson().toJson(valueId,ProjectType.class));
                        startActivity(intent);

                    } );
                    binding.scheduleTableLayout.addView(tableRow);


                }
            }
        }else {
            showToast("response is null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
    }
    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
}
