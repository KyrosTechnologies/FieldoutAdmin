package com.example.rohin.fieldoutadmin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.rohin.fieldoutadmin.R;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.ActivityAddTaxesBinding;
import com.example.rohin.fieldoutadmin.databinding.FragmentTaxesFragmentBinding;
import com.example.rohin.fieldoutadmin.fragments.TaxesFragment;
import com.example.rohin.fieldoutadmin.models.ProjectType;
import com.example.rohin.fieldoutadmin.models.Tax;
import com.example.rohin.fieldoutadmin.models.TaxDeleteResponse;
import com.example.rohin.fieldoutadmin.models.TaxResponse;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.TaxesFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddTaxesActivity extends AppCompatActivity {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=AddTaxesActivity.class.getSimpleName();
    @Inject
    TaxesFragmentViewModel viewModel;
    private ProgressDialog progressDialog;
    ActivityAddTaxesBinding binding;
    private String taxId=null;
    private List<String>defaultTaxList=new ArrayList<>();
    private int selectedDefaultTax=0;
    private AlertDialog deleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar)));
        actionBar.setDisplayHomeAsUpEnabled(true);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectAddTaxesActivity(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getApplicationContext());
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_taxes);
        try{
            Tax tax=new Gson().fromJson(getIntent().getExtras().getString("taxes"),Tax.class);
            Log.d("taxes  :  ",TAG+" / / "+tax.toString());
            taxId=tax.getId();
            String taxName=tax.getName();
            double rateDouble=tax.getRate();
            String rate=String.valueOf(rateDouble);
            if(taxName!=null){
                binding.editTextTaxName.setText(taxName);
            }
            if(rate!=null){
                binding.editTextRateTax.setText(rate);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        defaultTaxList.add("Yes");
        defaultTaxList.add("No");
        ArrayAdapter<String> adapterDefaultTax=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,defaultTaxList);
        binding.spinnerTaxDefault.setPrompt("Default Tax");
        adapterDefaultTax.setDropDownViewResource(android.R.layout.simple_list_item_1);
        binding.spinnerTaxDefault.setAdapter(adapterDefaultTax);
        binding.spinnerTaxDefault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDefaultTax=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.buttonSaveTax.setOnClickListener(view -> initiateApi());

    }

    private void initiateApi() {
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(taxId==null){
            String name=binding.editTextTaxName.getText().toString();
            String rate=binding.editTextRateTax.getText().toString();
            double rateDouble=Double.parseDouble(rate);
            boolean value;
            if(selectedDefaultTax==0){
                value=true;
            }else{
                value=false;
            }
            Tax tax=new Tax();
            tax.setIdDomain(domainId);
            tax.setIsDefaulttax(value);
            tax.setName(name);
            tax.setRate(rateDouble);
            addAPI(authKey,tax);
        }else{
            String name=binding.editTextTaxName.getText().toString();
            String rate=binding.editTextRateTax.getText().toString();
            double rateDouble=Double.parseDouble(rate);
            boolean value;
            if(selectedDefaultTax==0){
                value=true;
            }else{
                value=false;
            }
            Tax tax=new Tax();
            tax.setIdDomain(domainId);
            tax.setIsDefaulttax(value);
            tax.setName(name);
            tax.setRate(rateDouble);
            updateAPI(authKey,taxId,tax);
        }

    }

    private void addAPI(String authKey, Tax tax) {
        showProgressDialog();
        subscription.add(viewModel.addTaxResponseObservable(authKey,tax)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::response,this::error,this::completed));
    }

    private void completed() {
        dismissProgressDialog();
    }

    private void error(Throwable throwable) {
        dismissProgressDialog();
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void response(TaxResponse taxResponse) {
        if(taxResponse!=null){
            Log.d("Response : ",TAG+" / / "+taxResponse.toString());
            boolean success=taxResponse.isSuccess();
            if(success){
                this.finish();
                showToast("successfully added!");
            }else{
                showToast(" not successfull!");
            }
        }else {
            showToast("response is null!");
        }
    }

    private void updateAPI(String authKey, String taxId, Tax tax) {
        showProgressDialog();
    subscription.add(viewModel.updateTaxResponseObservable(authKey,taxId,tax)
    .subscribeOn(Schedulers.computation())
    .observeOn(AndroidSchedulers.mainThread())
    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
    .subscribe(this::updateResponse,this::errorUpdateResponse,this::completedUpdateResponse));
    }

    private void completedUpdateResponse() {
        dismissProgressDialog();

    }

    private void errorUpdateResponse(Throwable throwable) {
        dismissProgressDialog();
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());

    }

    private void updateResponse(TaxResponse taxResponse) {
    if(taxResponse!=null){
    Log.d("Update Response : ",TAG+" / / "+taxResponse);
    boolean success=taxResponse.isSuccess();
    if(success){
        this.finish();
        showToast("updated successfully!");
    }else{
        showToast("Update not successfull!");
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
        if(deleteDialog==null){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        deleteTaxAPI();
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());
        deleteDialog= builder.create();
        }
        deleteDialog.show();
    }

    private void deleteTaxAPI() {
        if(taxId!=null){
            showProgressDialog();
            subscription.add(viewModel.deleteTaxResponseObservable(store.getToken(),taxId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::deleteResponse,this::errorDeleteResponse,this::completedDeleteResponse));
        }else {
            showToast("Tax id null!");
        }
    }

    private void completedDeleteResponse() {
        dismissProgressDialog();
    }

    private void errorDeleteResponse(Throwable throwable) {
        dismissProgressDialog();
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void deleteResponse(TaxDeleteResponse taxDeleteResponse) {
        if(taxDeleteResponse!=null){
            Log.e("delete Response : ",TAG+" / / "+taxDeleteResponse);
            boolean success=taxDeleteResponse.isResult();
            if(success){
                showToast("deleted successfully!");
                this.finish();
            }else{
                showToast(" not successfull!");
            }
        }else {
            showToast("response is null!");
        }
    }

    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
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
        dismissProgressDialog();
        dismissDeleteDialog();
    }
    private void  showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void dismissProgressDialog() {
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
