package com.example.rohin.fieldoutadmin.fragments;

import android.app.ProgressDialog;
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
import com.example.rohin.fieldoutadmin.activity.AddTaxesActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentTaxesFragmentBinding;
import com.example.rohin.fieldoutadmin.models.ProjectType;
import com.example.rohin.fieldoutadmin.models.Tax;
import com.example.rohin.fieldoutadmin.models.TaxResponse;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.TaxesFragmentViewModel;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 03-01-2018.
 */

public class TaxesFragment extends Fragment {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=TaxesFragment.class.getSimpleName();
    @Inject
    TaxesFragmentViewModel viewModel;
    FragmentTaxesFragmentBinding binding;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectTaxesFragment(this);
        store=PreferenceManager.getInstance(getContext());
        subscription=new CompositeSubscription();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_taxes_fragment,container,false);
        View view=binding.getRoot();
        binding.addTaxButton.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddTaxesActivity.class)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(authKey!=null && domainId!=null){
            Log.d("Domain Id : ",TAG+" / / "+domainId);
            initiateApiCall(authKey,domainId);
        }else {
            showToast("domain id and auth key is null!");
        }
    }

    private void initiateApiCall(String authKey, String domainId) {
        showProgressDialog();
        subscription.add(viewModel.getTaxResponseObservable(authKey,domainId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::response,this::error,this::completed));
    }

    private void completed() {
        dismissProgressDialog();
    }

    private void error(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void response(TaxResponse taxResponse) {
        if(taxResponse!=null){
            Log.d("Response : ",TAG+" / / "+taxResponse.toString());
            List<Tax> taxList=taxResponse.getTaxes();
            binding.taxTableLayout.removeAllViews();
            binding.taxTableLayout.addView(binding.rowTaxTable);

            if(taxList!=null && taxList.size()!=0){
                for(Tax tax:taxList){
                    String id=tax.getId();
                    Boolean isDefaulttax=tax.getIsDefaulttax();
                    String name=tax.getName();
                    Log.d("Name : ",name);
                    double rateD=tax.getRate();
                    String rate=String.valueOf(rateD);

                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    //TextView name
                    TextView nameTextView=new TextView(getContext());
                    if(name!=null){
                        nameTextView.setText(name);
                    }
                    nameTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowNameParams.setMargins(10,10,0,10);
                    nameTextView.setLayoutParams(tableRowNameParams);
                    nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    nameTextView.setTextColor(getResources().getColor(R.color.light_black));
                    nameTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(nameTextView);

                    //TextView Rate
                    TextView rateTextView=new TextView(getContext());
                    if(rate!=null){
                        rateTextView .setText(rate);
                    }
                    rateTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowRateParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowRateParams.setMargins(10,10,0,10);
                    rateTextView.setLayoutParams(tableRowRateParams);
                    rateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    rateTextView.setTextColor(getResources().getColor(R.color.light_black));
                    rateTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(rateTextView);

                    //default tax text view
                    TextView defaultTaxTextView=new TextView(getContext());
                    String tagValue="";
                    if(isDefaulttax){
                        tagValue="Yes";
                    }else{
                        tagValue="No";
                    }
                    defaultTaxTextView.setText(tagValue);
                    defaultTaxTextView.setTextSize(20);
                    TableRow.LayoutParams tableRowDefaultTaxParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                    tableRowDefaultTaxParams.setMargins(10,10,0,10);
                    defaultTaxTextView.setLayoutParams(tableRowDefaultTaxParams);
                    defaultTaxTextView.setTextColor(getResources().getColor(R.color.light_black));
                    defaultTaxTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    defaultTaxTextView.setPadding(5, 5, 5, 5);
                    tableRow.addView(defaultTaxTextView);

                    tableRow.setTag(tax);

                    tableRow.setOnClickListener(view1 ->{
                        Object valueId=view1.getTag();
                        Log.d("Team Id : ",TAG+" / / "+valueId.toString());
                        Intent intent=new Intent(getContext(),AddTaxesActivity.class);
                        intent.putExtra("taxes",new Gson().toJson(valueId,Tax.class));
                        startActivity(intent);
                    } );
                    binding.taxTableLayout.addView(tableRow);
                }
            }
        }else{
            showToast("response is null!");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
        dismissProgressDialog();
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void dismissProgressDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
