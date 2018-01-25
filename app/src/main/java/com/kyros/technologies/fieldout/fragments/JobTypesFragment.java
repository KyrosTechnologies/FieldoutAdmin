package com.kyros.technologies.fieldout.fragments;

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

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.activity.AddJobsTypeActivity;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentJobTypesBinding;
import com.kyros.technologies.fieldout.models.JobType;
import com.kyros.technologies.fieldout.models.JobsTypeResponse;
import com.kyros.technologies.fieldout.models.SkilledTradesModel;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.JobsTypesFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
        showToast(""+throwable.getMessage());
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
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));

                    //TextView jobTypeName
                    TextView jobNameTextView=new TextView(getContext());
                    if(jobTypeName!=null){
                        jobNameTextView.setText(jobTypeName);
                    }
                    jobNameTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowJobTypeNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowJobTypeNameParams.setMargins(15,15,15,15);
                    jobNameTextView.setLayoutParams(tableRowJobTypeNameParams);
                    jobNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    jobNameTextView.setTextColor(getResources().getColor(R.color.text_light));
                    tableRow.addView(jobNameTextView);

                    //TextView duration
                    TextView durationTextView=new TextView(getContext());
                    if(duration!=null){
                        durationTextView.setText(duration);
                    }
                    durationTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowDurationParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowDurationParams.setMargins(15,15,15,15);
                    durationTextView.setLayoutParams(tableRowDurationParams);
                    durationTextView.setTextColor(getResources().getColor(R.color.text_light));
                    durationTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    tableRow.addView(durationTextView);


                    //TextView jobReportType
                    TextView jobReportTemplateTextView=new TextView(getContext());
                    if(jobReportTemplate!=null){
                        jobReportTemplateTextView.setText(jobReportTemplate);
                    }
                    jobReportTemplateTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowJobReportTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowJobReportTypeParams.setMargins(15,15,15,15);
                    jobReportTemplateTextView.setLayoutParams(tableRowJobReportTypeParams);
                    jobReportTemplateTextView.setTextColor(getResources().getColor(R.color.light_black));
                    jobReportTemplateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    tableRow.addView(jobReportTemplateTextView);

                    //TextView Priority
                    TextView priorityTextView=new TextView(getContext());
                    if(priority!=null){
                        priorityTextView.setText(priority);
                    }
                    priorityTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowPriorityParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowPriorityParams.setMargins(15,15,15,15);
                    priorityTextView.setLayoutParams(tableRowPriorityParams);
                    priorityTextView.setTextColor(getResources().getColor(R.color.text_light));
                    priorityTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
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
                    skilledTradersTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowSkilledTradersParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowSkilledTradersParams.setMargins(15,15,15,15);
                    skilledTradersTextView.setLayoutParams(tableRowSkilledTradersParams);
                    skilledTradersTextView.setTextColor(getResources().getColor(R.color.text_light));
                    skilledTradersTextView.setGravity(Gravity.LEFT|Gravity.CENTER);

                    tableRow.addView(skilledTradersTextView);

                    //TextView Default
                    TextView defaultTextView=new TextView(getContext());
                    if(defaultvalue){
                        defaultTextView.setText("Yes");
                    }else{
                        defaultTextView.setText("No");
                    }
                    defaultTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowDefaultParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowDefaultParams.setMargins(15,15,15,15);
                    defaultTextView.setLayoutParams(tableRowDefaultParams);
                    defaultTextView.setTextColor(getResources().getColor(R.color.text_light));
                    defaultTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
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
