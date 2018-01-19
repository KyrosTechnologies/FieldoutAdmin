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
import com.example.rohin.fieldoutadmin.activity.AddTaxesActivity;
import com.example.rohin.fieldoutadmin.activity.PartsAndServicesActivity;
import com.example.rohin.fieldoutadmin.activity.UpdateTeamActivity;
import com.example.rohin.fieldoutadmin.common.ServiceHandler;
import com.example.rohin.fieldoutadmin.databinding.FragmentPartsServicesBinding;
import com.example.rohin.fieldoutadmin.models.PartsAndServicesResponse;
import com.example.rohin.fieldoutadmin.models.StockPart;
import com.example.rohin.fieldoutadmin.models.TagInfo;
import com.example.rohin.fieldoutadmin.models.Tax;
import com.example.rohin.fieldoutadmin.models.TaxInfo;
import com.example.rohin.fieldoutadmin.sharedpreference.PreferenceManager;
import com.example.rohin.fieldoutadmin.viewmodel.PartsAndServicesViewModel;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 04-01-2018.
 */

public class PartsAndServicesFragment extends Fragment {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=PartsAndServicesFragment.class.getSimpleName();
    @Inject
    PartsAndServicesViewModel viewModel;
    private FragmentPartsServicesBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectPartsAndServicesFragment(this);
        store=PreferenceManager.getInstance(getContext());
        subscription=new CompositeSubscription();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_parts_services,container,false);
        View view=binding.getRoot();
        binding.buttonAddPartsServices.setOnClickListener(view1 -> startActivity(new Intent(getContext(),PartsAndServicesActivity.class)));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        if(domainId!=null && authKey!=null){
         loadGetAPI(domainId,authKey);
        }else {
            showToast("id is null!");
        }
    }

    private void loadGetAPI(String domainId, String authKey) {
        subscription.add(viewModel.getPartsAndServicesResponseObservable(authKey,domainId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(this::getResponse,this::getError,this::getCompleted));
    }

    private void getCompleted() {

    }

    private void getError(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
    }

    private void getResponse(PartsAndServicesResponse partsAndServicesResponse) {
        if(partsAndServicesResponse!=null){
            Log.d("Response : ",TAG+" / / "+partsAndServicesResponse);
            List<StockPart>stockPartList=partsAndServicesResponse.getStockParts();
            binding.partsTableLayout.removeAllViews();
            binding.partsTableLayout.addView(binding.rowPartsTable);
            if(stockPartList!=null && stockPartList.size()!=0){
                for(StockPart stockPart:stockPartList){
                    String categoryName=stockPart.getCategoryName();
                    String id=stockPart.getId();
                    String idTax=stockPart.getIdTax();
                    String name=stockPart.getName();
                    double price=stockPart.getPrice();
                    String reference=stockPart.getReference();
                    TaxInfo taxInfo=stockPart.getTaxInfo();

                    //Tables Rows
                    TableRow tableRow=new TableRow(getContext());
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                    //TextView category
                    TextView categoryTextView=new TextView(getContext());
                    if(categoryName!=null){
                        categoryTextView.setText(categoryName);
                    }
                    categoryTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowCategoryParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowCategoryParams.setMargins(15,15,15,15);
                    categoryTextView.setLayoutParams(tableRowCategoryParams);
                    categoryTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    categoryTextView.setTextColor(getResources().getColor(R.color.text_light));
                    tableRow.addView(categoryTextView);

                    //TextView Parts & Services
                    TextView partsServicesTextView=new TextView(getContext());
                    if(name!=null){
                        partsServicesTextView.setText(name);
                    }
                    partsServicesTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowPartsServicesParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowCategoryParams.setMargins(15,15,15,15);
                    partsServicesTextView.setLayoutParams(tableRowPartsServicesParams);
                    partsServicesTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    partsServicesTextView.setTextColor(getResources().getColor(R.color.text_light));
                    tableRow.addView(partsServicesTextView);

                    //TextView reference
                    TextView referenceTextView=new TextView(getContext());
                    if(reference!=null){
                        referenceTextView.setText(reference);
                    }
                    referenceTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowReferenceParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowCategoryParams.setMargins(15,15,15,15);
                    referenceTextView.setLayoutParams(tableRowReferenceParams);
                    referenceTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    referenceTextView.setTextColor(getResources().getColor(R.color.text_light));
                    tableRow.addView(referenceTextView);

                    //TextView price
                    TextView priceTextView=new TextView(getContext());

                    priceTextView.setText(String.valueOf(price));
                    priceTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowPriceParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowCategoryParams.setMargins(15,15,15,15);
                    priceTextView.setLayoutParams(tableRowPriceParams);
                    priceTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    priceTextView.setTextColor(getResources().getColor(R.color.text_light));
                    tableRow.addView(priceTextView);

                    //Tax text view
                    TextView taxTextView=new TextView(getContext());
                    taxTextView.setText(String.valueOf(taxInfo.getRate()));
                    taxTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowTaxParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowCategoryParams.setMargins(15,15,15,15);
                    taxTextView.setLayoutParams(tableRowTaxParams);
                    taxTextView.setTextColor(getResources().getColor(R.color.text_light));
                    taxTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    tableRow.addView(taxTextView);

                    tableRow.setTag(stockPart);
                    tableRow.setOnClickListener(view1 ->{
                        Object valueId=view1.getTag();
                        Log.d("Team Id : ",TAG+" / / "+valueId.toString());
                        Intent intent=new Intent(getContext(),PartsAndServicesActivity.class);
                        intent.putExtra("parts",new Gson().toJson(valueId,StockPart.class));
                        startActivity(intent);
                    } );

                    binding.partsTableLayout.addView(tableRow);
                }
            }
        }else {
            showToast("response is null!");
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
