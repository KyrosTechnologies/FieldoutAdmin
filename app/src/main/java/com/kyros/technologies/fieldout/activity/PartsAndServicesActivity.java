package com.kyros.technologies.fieldout.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityPartsAndServicesBinding;
import com.kyros.technologies.fieldout.models.DeletePartsAndServicesResponse;
import com.kyros.technologies.fieldout.models.PartsAndServicesResponse;
import com.kyros.technologies.fieldout.models.StockPart;
import com.kyros.technologies.fieldout.models.Tax;
import com.kyros.technologies.fieldout.models.TaxResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.PartsAndServicesViewModel;
import com.kyros.technologies.fieldout.viewmodel.TaxesFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PartsAndServicesActivity extends AppCompatActivity {
    private String TAG=PartsAndServicesActivity.class.getSimpleName();
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private ActivityPartsAndServicesBinding binding;
    private String stockPartId=null;
    private List<Tax>totalTaxList=new ArrayList<>();
    private List<String>taxNameList=new ArrayList<>();
    private int selectedTaxRate=0;
    @Inject
    PartsAndServicesViewModel viewModel;
    @Inject
    TaxesFragmentViewModel taxesFragmentViewModel;
    private ProgressDialog progressDialog;
    private AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showActionBar();
        ((ServiceHandler)getApplication()).getApplicationComponent().injectPartsAndServicesActivity(this);
        store=PreferenceManager.getInstance(getApplicationContext());
        subscription=new CompositeSubscription();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_parts_and_services);
        try {
            StockPart stockPart = new Gson().fromJson(getIntent().getExtras().getString("parts"), StockPart.class);
            Log.d("parts  :  ", TAG + " / / " + stockPart.toString());
            stockPartId=stockPart.getId();
            String category=stockPart.getCategoryName();
            if(category!=null){
                binding.editTextCategoryName.setText(category);
            }
            String name=stockPart.getName();
            if(name!=null){
                binding.editTextPartsServices.setText(name);
            }
            String reference=stockPart.getReference();
            if(reference!=null){
                binding.editTextReference.setText(reference);
            }
            double price=stockPart.getPrice();
            binding.editTextPrice.setText(String.valueOf(price));
            double tax=stockPart.getTaxInfo().getRate();

        }catch (Exception e){
            e.printStackTrace();
        }
        binding.buttonSavePartsServices.setOnClickListener(view -> validateFields());
    }
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void dismissProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void validateFields() {
        if(stockPartId==null){
            addNewOne();
        }else{
            updateOldOne();
        }
    }

    private void updateOldOne() {
        String category=binding.editTextCategoryName.getText().toString();
        String partsName=binding.editTextPartsServices.getText().toString();
        String reference=binding.editTextReference.getText().toString();
        String price=binding.editTextPrice.getText().toString();
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        String taxId=null;
        for(Tax tax:totalTaxList){
            if(taxNameList.get(selectedTaxRate).equals(String.valueOf(tax.getRate()))){
                taxId=tax.getId();
            }
        }
        if(category !=null && !category.isEmpty() && partsName!=null && !partsName.isEmpty() && reference!=null && !reference.isEmpty() && price!=null && !price.isEmpty() && taxId!=null && !taxId.isEmpty()){
            StockPart stockPart=new StockPart();
            stockPart.setCategoryName(category);
            stockPart.setName(partsName);
            stockPart.setReference(reference);
            stockPart.setPrice(Double.parseDouble(price));
            stockPart.setIdDomain(domainId);
            stockPart.setIdTax(taxId);
            initiateUpdatePartsServicesAPI(authKey,stockPart,stockPartId);
        }else{
            showToast("please provide all details");
        }
    }

    private void initiateUpdatePartsServicesAPI(String authKey, StockPart stockPart, String stockPartId) {
        showProgressDialog();
        subscription.add(viewModel.updatePartsAndServicesResponseObservable(authKey,stockPartId,stockPart,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::updateResponse,this::updateErrorResponse,this::updateCompleteResponse));
    }

    private void updateCompleteResponse() {
        dismissProgressDialog();
    }

    private void updateErrorResponse(Throwable throwable) {
        showToast(""+throwable.getMessage());
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        dismissProgressDialog();
    }

    private void updateResponse(PartsAndServicesResponse partsAndServicesResponse) {
        dismissProgressDialog();
        if(partsAndServicesResponse!=null){
            Log.d("Update Response : ",TAG+" / / "+partsAndServicesResponse);
            boolean success=partsAndServicesResponse.isSuccess();
            if(success){
                this.finish();
                showToast("updated succesfully!");
            }
        }else {
            showToast("response is null!");
        }
    }

    private void addNewOne(){
        String category=binding.editTextCategoryName.getText().toString();
        String partsName=binding.editTextPartsServices.getText().toString();
        String reference=binding.editTextReference.getText().toString();
        String price=binding.editTextPrice.getText().toString();
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        String taxId=null;
        for(Tax tax:totalTaxList){
            if(taxNameList.get(selectedTaxRate).equals(String.valueOf(tax.getRate()))){
                taxId=tax.getId();
            }
        }
        if(category !=null && !category.isEmpty() && partsName!=null && !partsName.isEmpty() && reference!=null && !reference.isEmpty() && price!=null && !price.isEmpty() && taxId!=null && !taxId.isEmpty()){
            StockPart stockPart=new StockPart();
            stockPart.setCategoryName(category);
            stockPart.setName(partsName);
            stockPart.setReference(reference);
            stockPart.setPrice(Double.parseDouble(price));
            stockPart.setIdDomain(domainId);
            stockPart.setIdTax(taxId);
            initiateAddPartsServicesAPI(authKey,stockPart);
        }else{
            showToast("please provide all details");
        }
    }

    private void initiateAddPartsServicesAPI(String authKey, StockPart stockPart) {
        showProgressDialog();
        subscription.add(viewModel.addPartsAndServicesResponseObservable(authKey,stockPart,store.getIdDomain())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::addResponse,this::addErrorResponse,this::addCompletedResponse));

    }

    private void addCompletedResponse() {
        dismissProgressDialog();
    }

    private void addErrorResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());
        dismissProgressDialog();
    }

    private void addResponse(PartsAndServicesResponse partsAndServicesResponse) {
        dismissProgressDialog();
        if(partsAndServicesResponse!=null){
            Log.d("Add Response : ",TAG+" / / "+partsAndServicesResponse);
            boolean  success=partsAndServicesResponse.isSuccess();
            if(success){
                this.finish();
                showToast("successfully added!");
            }
        }else {
            showToast("response is null!");
        }

    }

    private void showActionBar(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(authKey!=null && domainId!=null){
            initiateGetTaxAPI(authKey,domainId);
        }else {
            showToast("key is null!");
        }
    }

    private void initiateGetTaxAPI(String authKey, String domainId) {
        subscription.add(taxesFragmentViewModel.getTaxResponseObservable(authKey,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::taxResponse,this::taxError,this::taxCompleted));

    }

    private void taxCompleted() {

    }

    private void taxError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void taxResponse(TaxResponse taxResponse) {
        if(taxResponse!=null){
            Log.d("TaxResponse : ",TAG+" / / "+taxResponse);
            totalTaxList=taxResponse.getTaxes();

            if(totalTaxList!=null && totalTaxList.size()!=0){
                for(Tax tax:totalTaxList){
                    taxNameList.add(String.valueOf(tax.getRate()));
                }
                ArrayAdapter<String> adapterTaxRates=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,taxNameList);
                binding.spinnerTaxList.setPrompt("Tax Rates");
                adapterTaxRates.setDropDownViewResource(android.R.layout.simple_list_item_1);
                binding.spinnerTaxList.setAdapter(adapterTaxRates);
                binding.spinnerTaxList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedTaxRate=i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        }else {
            showToast("tax response is null!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissProgressDialog();
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
                showDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDeleteDialog(){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        deletePartsServices();
                        Log.d("Dialog Yes ","dialog initialization");
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());


        deleteDialog = builder.create();
        deleteDialog.show();
    }

    private void deletePartsServices() {
        if(stockPartId!=null){
            showProgressDialog();
            subscription.add(viewModel.deletePartsAndServicesResponseObservable(store.getToken(),stockPartId,store.getIdDomain())
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::deleteResponse,this::errorDeleteResponse,this::completedDeleteResponse));
        }else{
            showToast("you cannot delete new one!");
        }
    }

    private void completedDeleteResponse() {
        dismissProgressDialog();
    }

    private void errorDeleteResponse(Throwable throwable) {
        dismissProgressDialog();
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void deleteResponse(DeletePartsAndServicesResponse deletePartsAndServicesResponse) {
        dismissProgressDialog();
        if(deletePartsAndServicesResponse!=null){
            Log.d("Delete Response : ",TAG+" / / "+deletePartsAndServicesResponse);
            boolean success=deletePartsAndServicesResponse.isResult();
            if(success){
                showToast("Successfully Deleted!");
                this.finish();
            }
        }else{
            showToast("response is null!");
        }
    }

    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
    }

}
