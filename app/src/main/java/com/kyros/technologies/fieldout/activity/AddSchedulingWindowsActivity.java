package com.kyros.technologies.fieldout.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityAddSchedulingWindowsBinding;
import com.kyros.technologies.fieldout.models.AddSchedulingResponse;
import com.kyros.technologies.fieldout.models.DeleteScheduleWindowsResponse;
import com.kyros.technologies.fieldout.models.SchedulingWindow;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddSchedulingWindowsViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddSchedulingWindowsActivity extends AppCompatActivity {
    private PreferenceManager store;
    private ActivityAddSchedulingWindowsBinding binding;
    private CompositeSubscription subscription;
    @Inject
    AddSchedulingWindowsViewModel viewModel;
    private TimePickerDialog timePickerDialog;
    private List<String>publicList=new ArrayList<>();
    private int selectedPublic=0;
    private String TAG=AddSchedulingWindowsActivity.class.getSimpleName();
    private SchedulingWindow schedulingWindow;
    private String schedulingId=null;
    private AlertDialog deleteDialog=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddSchedulingWindowsActivity(this);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_scheduling_windows);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
        publicList.add("no");
        publicList.add("yes");
        try{
            schedulingWindow=new Gson().fromJson(getIntent().getExtras().getString("schedule"),SchedulingWindow.class);
            schedulingId=schedulingWindow.getId();
            String labelName=schedulingWindow.getName();
            if(labelName!=null){
                binding.addScheduleLabelEditText.setText(labelName);
            }
            String timeStart=schedulingWindow.getTimeStart();
            if(timeStart!=null){
                binding.addScheduleStartTextView.setText(timeStart);
            }
            String timeEnd=schedulingWindow.getTimeEnd();
            if(timeEnd!=null){
                binding.addScheduleEndTextView.setText(timeEnd);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapterPublic=new  ArrayAdapter<>(this,android.R.layout.simple_spinner_item,publicList);
        binding.spinnerAddSchedule.setPrompt("Public");
        adapterPublic.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinnerAddSchedule.setAdapter(adapterPublic);
        binding.spinnerAddSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPublic=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.addScheduleStartTextView.setOnClickListener(view -> timePicker(binding.addScheduleStartTextView));
        binding.addScheduleEndTextView.setOnClickListener(view -> timePicker(binding.addScheduleEndTextView));
        binding.saveAddScheduleButton.setOnClickListener(view ->{
            if(schedulingId==null){
                validateFields();
            }else {
                updateValidateFields();
            }
                }
               );
    }

    private void updateValidateFields() {
        String labelName=binding.addScheduleLabelEditText.getText().toString();
        String timeStart=binding.addScheduleStartTextView.getText().toString();
        String timeEnd=binding.addScheduleEndTextView.getText().toString();
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(labelName!=null && !labelName.isEmpty() && timeStart!=null && !timeStart.isEmpty() && timeEnd!=null && !timeEnd.isEmpty() && domainId!=null &&!domainId.isEmpty()){
            SchedulingWindow schedulingWindow=new SchedulingWindow();
            schedulingWindow.setIdDomain(domainId);
            schedulingWindow.setIsPublic(selectedPublic);
            schedulingWindow.setName(labelName);
            schedulingWindow.setTimeStart(timeStart);
            schedulingWindow.setTimeEnd(timeEnd);
            callUpdateScheduleAPI(schedulingWindow,authKey,schedulingId);
        }else{
            showToast("Please enter all details!");
        }
    }

    private void callUpdateScheduleAPI(SchedulingWindow schedulingWindow, String authKey, String schedulingId) {
        subscription.add(viewModel.updateSchedulingResponseObservable(authKey,schedulingId,schedulingWindow)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::updateResponse,this::errorUpdateResponse,this::completedUpdateResponse));
    }

    private void completedUpdateResponse() {
    }

    private void errorUpdateResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void updateResponse(AddSchedulingResponse addSchedulingResponse) {
        if(addSchedulingResponse!=null){
            Log.d("update respone : ",TAG+" / / "+addSchedulingResponse.toString());
            boolean success=addSchedulingResponse.getIsSuccess();
            if(success){
                showToast("updated successfully");
                this.finish();
            }else{
                showToast("Update not successfull!");
            }
        }else {
            showToast("update response is null");
        }
    }

    private void validateFields() {
        String labelName=binding.addScheduleLabelEditText.getText().toString();
        String timeStart=binding.addScheduleStartTextView.getText().toString();
        String timeEnd=binding.addScheduleEndTextView.getText().toString();
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(labelName!=null && !labelName.isEmpty() && timeStart!=null && !timeStart.isEmpty() && timeEnd!=null && !timeEnd.isEmpty() && domainId!=null &&!domainId.isEmpty()){
            SchedulingWindow schedulingWindow=new SchedulingWindow();
            schedulingWindow.setIdDomain(domainId);
            schedulingWindow.setIsPublic(selectedPublic);
            schedulingWindow.setName(labelName);
            schedulingWindow.setTimeStart(timeStart);
            schedulingWindow.setTimeEnd(timeEnd);
            callScheduleAPI(schedulingWindow,authKey);
        }else{
            showToast("Please enter all details!");
        }

    }

    private void callScheduleAPI(SchedulingWindow schedulingWindow, String authKey) {
        subscription.add(viewModel.addSchedulingResponseObservable(authKey,schedulingWindow)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ", TAG+" / / "+throwable.getMessage()))
        .subscribe(this::responseAddSchedule,this::errorAddSchedule,this::completedAddSchedule));
    }

    private void completedAddSchedule() {

    }

    private void errorAddSchedule(Throwable throwable) {
        Log.e("Error : ", TAG+" / / "+throwable.getMessage());
    }

    private void responseAddSchedule(AddSchedulingResponse addSchedulingResponse) {
        if(addSchedulingResponse!=null){
            Log.d("Add Sch res : ",TAG+" / / "+addSchedulingResponse.toString());
            boolean success=addSchedulingResponse.getIsSuccess();
            if(success){
                showToast("Added successfully");
                this.finish();
            }else{
                showToast("not successfull!");
            }
        }else {
            showToast("response is null");
        }
    }

    private void timePicker(TextView editText){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(selectedHour,selectedMinute,editText),hour,minute,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
    private void dismissTimePickerDialog(){
        if(timePickerDialog!=null && timePickerDialog.isShowing()){
            timePickerDialog.dismiss();
        }
    }
    private void onTimeSet(int hourOfDay, int minute,TextView textView) {
        String am_pm = "";
        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        datetime.set(Calendar.MINUTE, minute);
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";
        String hours = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
        String finalTimeHour=String.format("%02d",Integer.parseInt(hours));
        String finalTimeMinute=String.format("%02d",datetime.get(Calendar.MINUTE));
        String timeValue=finalTimeHour+":"+finalTimeMinute+" "+am_pm;
        textView.setText(timeValue);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissTimePickerDialog();
        dismissDeleteDialog();
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
                if(schedulingId!=null){
                    showDeleteDialog();
                }else{
                    showToast("You cannot delete the new one");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSchedulingWindowsActivity.this);
        builder.setMessage("Do you want to Delete it?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Yes",
                (dialog, id) ->{
                    deleteSchedule();
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());
         deleteDialog = builder.create();
        deleteDialog.show();

    }

    private void deleteSchedule() {
        if(schedulingId!=null){
            subscription.add(viewModel.deleteScheduleWindowsResponseObservable(store.getToken(),schedulingId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::responseDeleteScheduler,this::errorDeleteScheduler,this::completedDeleteScheduler));
        }else{
            showToast("schedule id is null");
        }
    }

    private void completedDeleteScheduler() {

    }

    private void errorDeleteScheduler(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void responseDeleteScheduler(DeleteScheduleWindowsResponse deleteScheduleWindowsResponse) {
        if(deleteScheduleWindowsResponse!=null){
            Log.d("Delete Schedule : ",TAG+" / / "+deleteScheduleWindowsResponse.toString());
            boolean success=deleteScheduleWindowsResponse.getResult();
            if(success){
                this.finish();
                showToast("Deleted successfully");
            }
        }else {
            showToast("response is null");
        }
    }

    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
    }
}
