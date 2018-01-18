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
import com.example.rohin.fieldoutadmin.activity.AddJobsTypeActivity;
import com.example.rohin.fieldoutadmin.activity.UpdateTeamActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentJobTypesBinding;
import com.example.rohin.fieldoutadmin.models.JobType;
import com.example.rohin.fieldoutadmin.models.JobsTypeResponse;
import com.example.rohin.fieldoutadmin.models.SkilledTradesModel;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.JobsTypesFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Data;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 23-12-2017.
 */

public class JobTypesFragment extends Fragment {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=JobTypesFragment.class.getSimpleName();
    private FragmentJobTypesBinding binding;
    private List<JobType>jobTypes=new ArrayList<>();
    @Inject
    JobsTypesFragmentViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_job_types,container,false);
        store=PreferenceManager.getInstance(getContext());
        subscription=new CompositeSubscription();
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectJobTypesFragment(this);
        View view=binding.getRoot();
        binding.addJobTypeButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddJobsTypeActivity.class)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        callJobsTypeAPI(domainId,authKey);
    }

    private void callJobsTypeAPI(String domainId, String authKey) {
        if(domainId!=null && !domainId.isEmpty() && authKey!=null && !authKey.isEmpty()){
            subscription.add(viewModel.jobsTypeResponseObservable(domainId, authKey)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::responseJobType,this::errorJobType,this::completeJobType));
        }
    }

    private void completeJobType() {

    }

    private void errorJobType(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void responseJobType(JobsTypeResponse jobsTypeResponse) {
        if(jobsTypeResponse!=null){
        Log.d("Response : ",TAG+" / / "+jobsTypeResponse.toString());
              int child=  binding.teamsTableLayout.getChildCount();
              Log.d("child count : ",""+child);
            binding.teamsTableLayout.removeAllViews();
            View view=getLayoutInflater().inflate(R.layout.table_row_default,null);
            binding.teamsTableLayout.addView(binding.tableRowDefault);
            Log.d("jobTypes Size : ",TAG+" / / "+jobTypes.size());
            jobTypes=jobsTypeResponse.getJobTypes();
            if(jobTypes!=null && jobTypes.size()!=0){
                for(JobType jobType:jobTypes){
                    String duration=jobType.getDuration();
                    String id=jobType.getId();
                    boolean defaultvalue=jobType.getIsDefault();
                    String jobReportTemplate=jobType.getJobReportTemplate();
                    String jobTypeName=jobType.getJobTypeName();
                    String priority=jobType.getPriority();
                    SkilledTradesModel skilledTradesModel=jobType.getSkilledTrades();


                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    //TextView jobTypeName
                    TextView jobNameTextView=new TextView(getContext());
                    if(jobTypeName!=null){
                        jobNameTextView.setText(jobTypeName);
                    }
                    jobNameTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowJobTypeNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowJobTypeNameParams.setMargins(10,10,0,10);
                    jobNameTextView.setLayoutParams(tableRowJobTypeNameParams);
                    jobNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    jobNameTextView.setTextColor(getResources().getColor(R.color.light_black));
                    jobNameTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(jobNameTextView);

                    //TextView duration
                    TextView durationTextView=new TextView(getContext());
                    if(duration!=null){
                        durationTextView.setText(duration);
                    }
                    durationTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowDurationParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowDurationParams.setMargins(10,10,0,10);
                    durationTextView.setLayoutParams(tableRowDurationParams);
                    durationTextView.setTextColor(getResources().getColor(R.color.light_black));
                    durationTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    durationTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(durationTextView);


                    //TextView jobReportType
                    TextView jobReportTemplateTextView=new TextView(getContext());
                    if(jobReportTemplate!=null){
                        jobReportTemplateTextView.setText(jobReportTemplate);
                    }
                    jobReportTemplateTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowJobReportTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowJobReportTypeParams.setMargins(10,10,0,10);
                    jobReportTemplateTextView.setLayoutParams(tableRowJobReportTypeParams);
                    jobReportTemplateTextView.setTextColor(getResources().getColor(R.color.light_black));
                    jobReportTemplateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    jobReportTemplateTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(jobReportTemplateTextView);

                    //TextView Priority
                    TextView priorityTextView=new TextView(getContext());
                    if(priority!=null){
                        priorityTextView.setText(priority);
                    }
                    priorityTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowPriorityParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowPriorityParams.setMargins(10,10,0,10);
                    priorityTextView.setLayoutParams(tableRowPriorityParams);
                    priorityTextView.setTextColor(getResources().getColor(R.color.light_black));
                    priorityTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    priorityTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(priorityTextView);

                    //TextView SkilledTraders
                    TextView skilledTradersTextView=new TextView(getContext());
                    List<String>skilledList=skilledTradesModel.getName();
                    String skilledFirstvalue="";
                    if(skilledList!=null && skilledList.size()!=0){
                        skilledFirstvalue=skilledList.get(0);
                    }
                    if(skilledFirstvalue!=null){
                        skilledTradersTextView.setText(skilledFirstvalue);
                    }
                    skilledTradersTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowSkilledTradersParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowSkilledTradersParams.setMargins(10,10,0,10);
                    skilledTradersTextView.setLayoutParams(tableRowSkilledTradersParams);
                    skilledTradersTextView.setTextColor(getResources().getColor(R.color.light_black));
                    skilledTradersTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    skilledTradersTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(skilledTradersTextView);

                    //TextView Default
                    TextView defaultTextView=new TextView(getContext());
                    if(defaultvalue){
                        defaultTextView.setText("Yes");
                    }else{
                        defaultTextView.setText("No");
                    }
                    defaultTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowDefaultParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowDefaultParams.setMargins(10,10,10,10);
                    defaultTextView.setLayoutParams(tableRowDefaultParams);
                    defaultTextView.setTextColor(getResources().getColor(R.color.light_black));
                    defaultTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    defaultTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(defaultTextView);
                    tableRow.setTag(jobType);

                    tableRow.setOnClickListener(view1 ->{
                        Intent intent=new Intent(getContext(),AddJobsTypeActivity.class);
                        intent.putExtra("jobType",new Gson().toJson(view1.getTag()));
                        startActivity(intent);
                    } );
                    binding.teamsTableLayout.addView(tableRow);

                }
            }
        }else {
            showToast("jobtyperesponse is null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
private void showToast(String message){
    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
}
}
