package com.kyros.technologies.fieldout.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.SkilledTradersAdapter;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityAddCustomFieldsBinding;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.CustomFieldResponse;
import com.kyros.technologies.fieldout.models.DeleteCustomFieldResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.CustomFieldsFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddCustomFieldsActivity extends AppCompatActivity {
    private String TAG=AddCustomFieldsActivity.class.getSimpleName();
    private PreferenceManager store;
    private CompositeSubscription subscription;
    @Inject
    CustomFieldsFragmentViewModel viewModel;
    private ActivityAddCustomFieldsBinding binding;
    private int selecteType=0;
    private String type=null;
    private String []typeArray=new String []{"Text","List Of Values","Date","Numeric","CheckBox","AutoCompleteBox"};
    private AlertDialog listDialog;
    private SkilledTradersAdapter adapter;
    private SkilledTradersAdapter selectedAdapter;
    private List<String>selectedCustomList=new ArrayList<>();
    private List<String>finalSelectedCustomList=new ArrayList<>();
    private ProgressDialog progressDialog;
    private String customFieleId=null;
    private AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddCustomFieldsActivity(this);
        showActionBar();
        store=PreferenceManager.getInstance(getApplicationContext());
        subscription=new CompositeSubscription();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_custom_fields);
        try{
            type=getIntent().getExtras().getString("type");
            Log.d("Type : ",TAG+" / / "+type);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            CustomField customField=new Gson().fromJson(getIntent().getExtras().getString("customfield"),CustomField.class);
            type=customField.getFormType();
            Log.d("Custom Field:  ",TAG+" / / "+customField.toString());
            customFieleId=customField.getId();
            String labelName=customField.getName();
            boolean privateField=customField.getIsPrivate();
            if(labelName!=null){
                binding.editTextCustomFieldLabel.setText(labelName);
            }
            binding.checkboxPrivateField.setChecked(privateField);
        }catch (Exception e){
            e.printStackTrace();
        }

        bindViews();
    }

    private void bindViews() {
        ArrayAdapter<String>adapterType= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeArray);
        adapterType.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinnerTypeCustomField.setAdapter(adapterType);
        binding.spinnerTypeCustomField.setPrompt("Type");
        binding.spinnerTypeCustomField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selecteType=i;
                if(i==1){
                    binding.linearList.setVisibility(View.VISIBLE);
                }else{
                    binding.linearList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.buttonSaveCustomField.setOnClickListener(view -> validateFields());
        binding.textViewAddList.setOnClickListener(view -> setListDialog(new ArrayList<>()));
    }

    private void validateFields() {
        if(customFieleId==null){
            newCustomFieldAPIValidation();
        }else{
            oldCustomFieldAPIValidation();
        }

    }

    private void oldCustomFieldAPIValidation() {
        String fieldLabel=binding.editTextCustomFieldLabel.getText().toString();
        boolean privateCheckbox=binding.checkboxPrivateField.isChecked();
        String typeOfField=typeArray[selecteType];
        String domainId=store.getIdDomain();
        String authKey=store.getToken();

        if(selectedAdapter!=null){
            finalSelectedCustomList=selectedAdapter.getSkilledTradersList();
        }

        if(finalSelectedCustomList==null){
            finalSelectedCustomList=new ArrayList<>();
        }
        if(fieldLabel!=null && !fieldLabel.isEmpty() && domainId!=null){
            CustomField customField=new CustomField();
            customField.setName(fieldLabel);
            if(type==null){
                type="Text";
            }
            customField.setTypeOfField(typeOfField);
            customField.setIdDomain(domainId);
            customField.setFormType(type);
            customField.setChoices(finalSelectedCustomList);
            customField.setIsPrivate(privateCheckbox);
            initiateUpdateAPICall(authKey,customField,customFieleId);
        }else{
            showToast("Please provide all details!");
        }

    }

    private void initiateUpdateAPICall(String authKey, CustomField customField, String customFieleId) {
        showProgressDialog();
        subscription.add(viewModel.updatecustomFieldResponseObservable(authKey,customField,customFieleId,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::updateResponse,this::updateErrorResponse,this::completedUpdateResponse));
    }

    private void completedUpdateResponse() {
        dismissProgressDialog();
    }

    private void updateErrorResponse(Throwable throwable) {
        dismissProgressDialog();
        showToast(""+throwable.getMessage());
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void updateResponse(CustomFieldResponse customFieldResponse) {
        dismissProgressDialog();
        if(customFieldResponse!=null){
            Log.d("Update REspone : ",TAG+" / / "+customFieldResponse);
            boolean success=customFieldResponse.isSuccess();
            if(success){
                showToast("Successfully updated!");
                this.finish();
            }else{
                showToast("Update not successfull!");
            }
        }else{
            showToast("update response is null!");
        }
    }

    private void newCustomFieldAPIValidation() {
        String fieldLabel=binding.editTextCustomFieldLabel.getText().toString();
        boolean privateCheckbox=binding.checkboxPrivateField.isChecked();
        String typeOfField=typeArray[selecteType];
        String domainId=store.getIdDomain();
        String authKey=store.getToken();

        if(selectedAdapter!=null){
            finalSelectedCustomList=selectedAdapter.getSkilledTradersList();
        }

        if(finalSelectedCustomList==null){
            finalSelectedCustomList=new ArrayList<>();
        }
        if(fieldLabel!=null && !fieldLabel.isEmpty() && domainId!=null){
            CustomField customField=new CustomField();
            customField.setName(fieldLabel);
            if(type==null){
                type="Text";
            }
            customField.setTypeOfField(typeOfField);
            customField.setIdDomain(domainId);
            customField.setFormType(type);
            customField.setChoices(finalSelectedCustomList);
            customField.setIsPrivate(privateCheckbox);
            initiateAddAPICall(authKey,customField);
        }else{
            showToast("Please provide all details!");
        }


    }

    private void initiateAddAPICall(String authKey, CustomField customField) {
        showProgressDialog();
        Log.d("Input : ",TAG+" / / "+authKey+" / / "+customField);
        subscription.add(viewModel.addcustomFieldResponseObservable(authKey,customField,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::addResponse,this::addErrorResponse,this::addCompleted));
    }

    private void addCompleted() {
        dismissProgressDialog();
    }

    private void addErrorResponse(Throwable throwable) {
        dismissProgressDialog();
        showToast(""+throwable.getMessage());
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void addResponse(CustomFieldResponse customFieldResponse) {
        dismissProgressDialog();
        if(customFieldResponse!=null){
            Log.d("AddResponse : ",TAG+" / / "+customFieldResponse);
            boolean success=customFieldResponse.isSuccess();
            if(success){
                showToast("successfully added!");
                this.finish();
            }
        }else {
            showToast("response is null!");
        }
    }

    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading....");
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
    private void setListDialog(List<String> skilledName) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.adapter_teams_list,null);
        EditText editText=view.findViewById(R.id.et_skilled);
        editText.setHint("Enter lists");
        Button button=view.findViewById(R.id.button_add_skill);
        RecyclerView recyclerView=view.findViewById(R.id.skilled_traders_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter=new SkilledTradersAdapter(this,skilledName);
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
            selectedCustomList=adapter.getSkilledTradersList();
            if(selectedCustomList.size()!=0){
                binding.linearList.setVisibility(View.VISIBLE);
                selectedAdapter=new SkilledTradersAdapter(this,selectedCustomList);
                binding.recyclerCustomList.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerCustomList.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerCustomList.setAdapter(selectedAdapter);
                selectedAdapter.notifyDataSetChanged();
            }

        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissListDialog();
        });
        listDialog=builder.create();
        listDialog.show();

    }
    private  void dismissListDialog(){
        if(listDialog!=null && listDialog.isShowing()){
            listDialog.dismiss();
        }
    }

    private void showActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                if(customFieleId==null){
                    showToast("You cannot delete new one!");
                }else {
                    showDeleteDialog();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissListDialog();
        dismissProgressDialog();
        dismissDeleteDialog();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void showDeleteDialog(){
        if(deleteDialog==null){
         AlertDialog.Builder   builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to Delete it?");
            builder.setMessage("If you delete this means it will delete all respective column and values in List!!!!");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        deleteCustomField();
                        Log.d("Dialog Yes ","dialog initialization");
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());
            deleteDialog = builder.create();

        }
        deleteDialog.show();
    }

    private void deleteCustomField() {
        if(customFieleId!=null){
            initiateDeleteCustomFieldAPI();
        }else{
            showToast("cannot delete new one!");
        }
    }

    private void initiateDeleteCustomFieldAPI() {
        showProgressDialog();
        subscription.add(viewModel.deleteCustomFieldResponseObservable(store.getToken(),customFieleId,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::deleteRespones,this::deleteErrorResponse,this::deleteCompletedResponse));
    }

    private void deleteCompletedResponse() {
        dismissProgressDialog();
    }

    private void deleteErrorResponse(Throwable throwable) {
        dismissProgressDialog();
        showToast(""+throwable.getMessage());
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void deleteRespones(DeleteCustomFieldResponse deleteCustomFieldResponse) {
        dismissProgressDialog();
    if(deleteCustomFieldResponse!=null){
        Log.d("Delete Response : ",TAG+" / / "+deleteCustomFieldResponse);
        boolean success=deleteCustomFieldResponse.isResult();
        if(success){
            showToast("successfully deleted");
            this.finish();
        }else{
            showToast("not deleted");
        }
    }else{
        showToast("delete response is null!");
        }
    }

    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
    }
}
