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
import com.kyros.technologies.fieldout.activity.AddSchedulingWindowsActivity;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentSchedulingWindowsBinding;
import com.kyros.technologies.fieldout.models.ScheduleResponse;
import com.kyros.technologies.fieldout.models.SchedulingWindow;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.SchedulingWindowsFragmentViewModel;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 28-12-2017.
 */

public class SchedulingWindowsFragment extends Fragment {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=SchedulingWindowsFragment.class.getSimpleName();
    private FragmentSchedulingWindowsBinding binding;
    @Inject
    SchedulingWindowsFragmentViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_scheduling_windows,container,false);
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectSchedulingWindowsFragment(this);
        store=PreferenceManager.getInstance(getContext());
        subscription=new CompositeSubscription();
        View view=binding.getRoot();
        binding.addScheduleButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddSchedulingWindowsActivity.class)));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        subscription=new CompositeSubscription();
        callGetScheduleAPI(domainId,authKey);
    }

    private void callGetScheduleAPI(String domainId, String authKey) {
        subscription.add(viewModel.scheduleResponseObservable(authKey,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::responseSchedule,this::errorSchedule,this::completedSchedule));
    }

    private void completedSchedule() {
    }

    private void errorSchedule(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void responseSchedule(ScheduleResponse scheduleResponse) {
        if(scheduleResponse!=null){
            Log.d("Schedule Response : ",TAG+" / / "+scheduleResponse.toString());
            List<SchedulingWindow>schedulingWindows=scheduleResponse.getSchedulingWindows();
            if(schedulingWindows!=null && schedulingWindows.size()!=0){
                binding.scheduleTableLayout.removeAllViews();
                binding.scheduleTableLayout.addView(binding.rowScheduleTable);
                for(SchedulingWindow schedulingWindow:schedulingWindows){
                    String id=schedulingWindow.getId();
                    String timeStart=schedulingWindow.getTimeStart();
                    String timeEnd=schedulingWindow.getTimeEnd();
                    String labelName=schedulingWindow.getName();
                    int isPublic=schedulingWindow.getIsPublic();
                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    //label Name
                    TextView labelNameTextView=new TextView(getContext());
                    if(labelName!=null){
                        labelNameTextView.setText(labelName);
                    }
                    labelNameTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowlabelNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowlabelNameParams.setMargins(10,10,0,10);
                    labelNameTextView.setLayoutParams(tableRowlabelNameParams);
                    labelNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    labelNameTextView.setTextColor(getResources().getColor(R.color.light_black));
                    labelNameTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(labelNameTextView);

                    //time Start
                    TextView timeStartTextView=new TextView(getContext());
                    if(timeStart!=null){
                        timeStartTextView.setText(timeStart);
                    }
                    timeStartTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowTimeStartParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowTimeStartParams.setMargins(10,10,0,10);
                    timeStartTextView.setLayoutParams(tableRowTimeStartParams);
                    timeStartTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    timeStartTextView.setTextColor(getResources().getColor(R.color.light_black));
                    timeStartTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(timeStartTextView);

                    //time end
                    TextView timeEndTextView=new TextView(getContext());
                    if(timeEnd!=null){
                        timeEndTextView.setText(timeEnd);
                    }
                    timeEndTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowTimeEndParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowTimeEndParams.setMargins(10,10,0,10);
                    timeEndTextView.setLayoutParams(tableRowTimeEndParams);
                    timeEndTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    timeEndTextView.setTextColor(getResources().getColor(R.color.light_black));
                    timeEndTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(timeEndTextView);

                    //public text view
                    TextView publicTextView=new TextView(getContext());
                    if(isPublic==0){
                        publicTextView.setText("yes");
                    }else{
                        publicTextView.setText("no");
                    }

                    publicTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowPublicParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowPublicParams.setMargins(10,10,10,10);
                    publicTextView.setLayoutParams(tableRowPublicParams);
                    publicTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    publicTextView.setTextColor(getResources().getColor(R.color.light_black));
                    publicTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(publicTextView);
                    tableRow.setTag(schedulingWindow);
                    tableRow.setOnClickListener(view1 ->{
                        Object valueId=view1.getTag();
                        Log.d("Team Id : ",TAG+" / / "+valueId.toString());
                        Intent intent=new Intent(getContext(),AddSchedulingWindowsActivity.class);
                        intent.putExtra("schedule",new Gson().toJson(view1.getTag(),SchedulingWindow.class));
                        startActivity(intent);

                    } );
                    binding.scheduleTableLayout.addView(tableRow);


                }
            }
        }else {
            showToast("response is null!");
        }
    }

    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
