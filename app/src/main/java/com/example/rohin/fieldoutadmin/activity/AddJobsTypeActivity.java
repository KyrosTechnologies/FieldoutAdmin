package com.example.rohin.fieldoutadmin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.adapters.SkilledTradersAdapter;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.ActivityAddJobsTypeBinding;
import com.example.rohin.fieldoutadmin.models.AddJobsTypeResponse;
import com.example.rohin.fieldoutadmin.models.DeleteJobsTypeResponse;
import com.example.rohin.fieldoutadmin.models.JobType;
import com.example.rohin.fieldoutadmin.models.SkilledTradesModel;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.AddJobsTypeActivityViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddJobsTypeActivity extends AppCompatActivity {
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG=AddJobsTypeActivity.class.getSimpleName();
    private ActivityAddJobsTypeBinding binding;
    @Inject
    AddJobsTypeActivityViewModel viewModel;
    private TimePickerDialog timePickerDialog;
    private List<String>priorityList=new ArrayList<>();
    private List<String>defaultList=new ArrayList<>();
    private int selectedDefault=0;
    private int selectedPriorities=0;
    private AlertDialog skillsTradersDialog;
    private SkilledTradersAdapter adapter;
    private List<String>skilledTradersList=new ArrayList<>();
    private ProgressDialog progressDialog=null;
    private String jobTypeId=null;
    private AlertDialog.Builder builder;
    private AlertDialog deleteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_jobs_type);
        subscription=new CompositeSubscription();
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddJobTypesActivity(this);
        store=PreferenceManager.getInstance(getApplicationContext());
        priorityList.add("low");
        priorityList.add("high");
        priorityList.add("normal");
        defaultList.add("yes");
        defaultList.add("no");
        try{
            JobType jobType = new Gson().fromJson(getIntent().getExtras().getString("jobType") , JobType.class);
            String jobTypeName=jobType.getJobTypeName();
            jobTypeId=jobType.getId();
            if(jobTypeName!=null){
                binding.addJobTypeEditText.setText(jobTypeName);
            }
            String duration=jobType.getDuration();
            if(duration!=null){
                binding.durationTextView.setText(duration);
            }
            String report=jobType.getJobReportTemplate();
            if(report!=null){
                binding.reportAddJobsTypeText.setText(report);
            }
           skilledTradersList=jobType.getSkilledTrades().getName();
            if(skilledTradersList!=null && skilledTradersList.size()!=0){
                RecyclerView recyclerView1=findViewById(R.id.skilled_traders_job_type_recycler);
                    recyclerView1.setVisibility(View.VISIBLE);
                    recyclerView1.setLayoutManager(new LinearLayoutManager(this));
                    recyclerView1.setItemAnimator(new DefaultItemAnimator());
                    adapter=new SkilledTradersAdapter(this,skilledTradersList);
                    recyclerView1.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> adapterPriorities=new  ArrayAdapter<>(this,android.R.layout.simple_spinner_item,priorityList);
        ArrayAdapter<String> adapteDefault=new  ArrayAdapter<>(this,android.R.layout.simple_spinner_item,defaultList);
        binding.spinnerAddJobTypeTextView.setPrompt("Select Default");
        binding.priroritesTextView.setPrompt("Priorities");
        adapterPriorities.setDropDownViewResource(android.R.layout.simple_list_item_1);
        adapteDefault.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinnerAddJobTypeTextView.setAdapter(adapteDefault);
        binding.priroritesTextView.setAdapter(adapterPriorities);
        binding.durationTextView.setOnClickListener(view -> setTimeDurationDialog(binding.durationTextView));
        binding.priroritesTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedPriorities=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.spinnerAddJobTypeTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedDefault=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.skilledAddJobTypeTextView.setOnClickListener(view -> skilledTradesDialog());
        binding.saveAddJobsTypeButton.setOnClickListener(view -> {

                validateFields();

        });
    }
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Loading.....");
        }
        progressDialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_delete, menu);
        return true;
    }

    private void dismissProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void validateFields() {
        String jobTypeName=binding.addJobTypeEditText.getText().toString();
        String jobReportType=binding.reportAddJobsTypeText.getText().toString();
        String selectedPriorityString=priorityList.get(selectedPriorities);
        String selectedDefaultString=defaultList.get(selectedDefault);
        String time=binding.durationTextView.getText().toString();
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        if(jobTypeName!=null && !jobTypeName.isEmpty() && jobReportType!=null && !jobReportType.isEmpty() && selectedPriorityString!=null
                && !selectedPriorityString.isEmpty() && selectedDefaultString!=null && !selectedDefaultString.isEmpty() && time!=null && !time.isEmpty() && skilledTradersList!= null
                && domainId!=null && !domainId.isEmpty()){

            JobType jobType=new JobType();
            jobType.setJobTypeName(jobTypeName);
            jobType.setJobReportTemplate(jobReportType);
            jobType.setPriority(selectedPriorityString);
            boolean value;
            if(selectedDefaultString.equals("yes")){
                value=true;
            }else{
                value=false;
            }
            jobType.setIsDefault(value);
            jobType.setDuration(time);
            SkilledTradesModel skilledTradesModel=new SkilledTradesModel();
            skilledTradesModel.setName(skilledTradersList);
            jobType.setSkilledTrades(skilledTradesModel);
            jobType.setIdDomain(domainId);
            if(jobTypeId!=null){
                callUpdateAPI(authKey,jobType,jobTypeId);
            }else{
                callAPI(authKey,jobType);
            }
        }else{
            showToast("Please enter all details!");
        }


    }

    private void callUpdateAPI(String authKey, JobType jobType, String jobTypeId) {
    subscription.add(viewModel.updateJobsTypeResponseObservable(authKey,jobTypeId,jobType)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(this::responseUpdateJobsType,this::errorUpdateJobsType,this::completedUpdateJobsType));
    }

    private void completedUpdateJobsType() {

    }

    private void errorUpdateJobsType(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void responseUpdateJobsType(AddJobsTypeResponse addJobsTypeResponse) {
        if(addJobsTypeResponse!=null){
            Log.d("UpdateJobTypeRes : ",TAG+" / / "+addJobsTypeResponse.toString());
            boolean success=addJobsTypeResponse.getIsSuccess();
            if(success){
                showToast("Successfully Updated!");
                this.finish();
            }
            showToast("update error !");

        }else{
            showToast("response is null!");
        }
    }

    private void callAPI(String authKey, JobType jobType) {
        showProgressDialog();
        subscription.add(viewModel.addJobsTypeResponseObservable(authKey,jobType)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::responseAddJobType,this::errorAddJobType,this::completedAddJobType));
    }

    private void completedAddJobType() {
        showProgressDialog();
    }

    private void errorAddJobType(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showProgressDialog();
    }

    private void responseAddJobType(AddJobsTypeResponse addJobsTypeResponse) {
        showProgressDialog();
    if(addJobsTypeResponse!=null){
        Log.d("AddJobsTypeResponse : ",TAG+" / / "+addJobsTypeResponse.toString());
        boolean success=addJobsTypeResponse.getIsSuccess();
        if(success){
            this.finish();
            showToast("Successfully added jobtype");
        }
    }else {
        showToast("Add jobstype response is null!");
    }
    }

    private void setTimeDurationDialog(TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog=new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> onTimeSet(timePicker,selectedHour,selectedMinute,textView),hour,minute,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();

    }

    private void onTimeSet(TimePicker timePicker,int selectedHour, int selectedMinute, TextView textView) {
        String finalTimeHour=String.format("%02d",selectedHour);
        String finalTimeMinute=String.format("%02d",selectedMinute);
       String time=finalTimeHour+":"+finalTimeMinute;
        textView.setText(time);

    }

    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());


    }
    private void dismissTimePickerDialog(){
        if(timePickerDialog!=null && timePickerDialog.isShowing()){
            timePickerDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissTimePickerDialog();
        dismissSkilledTradersDialog();
        dismissProgressDialog();
        dismissDeleteDialog();
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                AddJobsTypeActivity.this.finish();
                return true;
            case R.id.action_delete:
                if(jobTypeId!=null){
                    showDeleteDialog();
                }else{
                    showToast("You cannot delete new one!");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDeleteDialog(){
        if(builder==null){
            builder = new AlertDialog.Builder(AddJobsTypeActivity.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        deleteJobTypes();
                        Log.d("Dialog Yes ","dialog initialization");
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());

        }
        deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void deleteJobTypes() {

    subscription.add(viewModel.deleteJobsTypeResponseObservable(store.getToken(),jobTypeId)
    .subscribeOn(Schedulers.computation())
    .observeOn(AndroidSchedulers.mainThread())
    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
    .subscribe(this::deleteJobTypeResponse,this::errorDeleteJobTypeResponse,this::completedDeleteJobTypeResponse));
    }

    private void completedDeleteJobTypeResponse() {

    }

    private void errorDeleteJobTypeResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void deleteJobTypeResponse(DeleteJobsTypeResponse deleteJobsTypeResponse) {
    if(deleteJobsTypeResponse!=null){
        boolean success=deleteJobsTypeResponse.isResult();
        if (success) {
            showToast("Successfully deleted!");
            this.finish();
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

    private void skilledTradesDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
        editText.setHint("Enter skilled trades");
        Button button=view.findViewById(R.id.button_add_skill);
        RecyclerView recyclerView=view.findViewById(R.id.skilled_traders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SkilledTradersAdapter(this,skilledTradersList);
        recyclerView.setAdapter(adapter);
        builder.setView(view);
        button.setOnClickListener(views->{
            String edName=editText.getText().toString();
            if(edName!=null && !edName.isEmpty()){
                adapter.setSkilledTradersName(edName);
                editText.setText("");

            }
        });
        builder.setPositiveButton("OK",(dialog,id)->{
           skilledTradersList=adapter.getSkilledTradersList();
           Log.d("list size : ",""+skilledTradersList.size());
            RecyclerView recyclerView1=findViewById(R.id.skilled_traders_job_type_recycler);
            if(skilledTradersList!=null && skilledTradersList.size()!=0){
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView1.setLayoutManager(new LinearLayoutManager(this));
                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                adapter=new SkilledTradersAdapter(this,skilledTradersList);
                recyclerView1.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissSkilledTradersDialog();
        });
        skillsTradersDialog=builder.create();
        skillsTradersDialog.show();

    }

    private void dismissSkilledTradersDialog() {
        if(skillsTradersDialog!=null && skillsTradersDialog.isShowing()){
            skillsTradersDialog.dismiss();
        }
    }
}
