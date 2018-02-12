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
import com.kyros.technologies.fieldout.activity.AddCustomFieldsActivity;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentCustomFieldsBinding;
import com.kyros.technologies.fieldout.models.CustomField;
import com.kyros.technologies.fieldout.models.CustomFieldResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.CustomFieldsFragmentViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kyros on 04-01-2018.
 */

public class CustomFieldsFragment extends Fragment {
    private String TAG=CustomFieldsFragment.class.getSimpleName();
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private FragmentCustomFieldsBinding binding;
    @Inject
    CustomFieldsFragmentViewModel viewModel;
    private List<CustomField>jobsCustomFieldList=new ArrayList<>();
    private List<CustomField>customersCustomFieldList=new ArrayList<>();
    private List<CustomField>sitesCustomFieldList=new ArrayList<>();
    private List<CustomField>equipmentCustomFieldList=new ArrayList<>();
    private List<CustomField>usersCustomFieldList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectCustomFieldsFragment(this);
        store=PreferenceManager.getInstance(getContext());
        subscription=new CompositeSubscription();
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_custom_fields,container,false);
        View view=binding.getRoot();
        bindViews();
        return view;
    }

    private void bindViews() {
        binding.buttonAddCustomers.setOnClickListener(view -> startActivity(new Intent(getContext(), AddCustomFieldsActivity.class).putExtra("type","customers")));
        binding.buttonAddJob.setOnClickListener(view -> startActivity(new Intent(getContext(), AddCustomFieldsActivity.class).putExtra("type","jobs")));
        binding.buttonAddSites.setOnClickListener(view -> startActivity(new Intent(getContext(), AddCustomFieldsActivity.class).putExtra("type","sites")));
        binding.buttonAddEquipment.setOnClickListener(view -> startActivity(new Intent(getContext(), AddCustomFieldsActivity.class).putExtra("type","equipments")));
        binding.buttonAddUsers.setOnClickListener(view -> startActivity(new Intent(getContext(), AddCustomFieldsActivity.class).putExtra("type","users")));
    }

    @Override
    public void onResume() {
        super.onResume();
        store= PreferenceManager.getInstance(getContext());
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        if(authKey != null && domainId != null){
            initiateCustomFieldsGetAPI(authKey,domainId);
        }
    }

    private void initiateCustomFieldsGetAPI(String authKey, String domainId) {
        subscription.add(viewModel.getcustomFieldResponseObservable(authKey,domainId)
       .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::getResponse,this::getErrorResponse,this::getCompletedResponse));
    }

    private void getCompletedResponse() {

    }

    private void getErrorResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void getResponse(CustomFieldResponse customFieldResponse) {
        if(customFieldResponse!=null){
                Log.d("GETResponse : ",TAG+" / / "+customFieldResponse);
            jobsCustomFieldList.clear();
            customersCustomFieldList.clear();
            sitesCustomFieldList.clear();
            equipmentCustomFieldList.clear();
            usersCustomFieldList.clear();
            List<CustomField>totalList=customFieldResponse.getCustomFields();
            if(totalList!=null && totalList.size()!=0){
                for(CustomField customField:totalList){
                    switch (customField.getFormType()){
                        case "jobs":
                            jobsCustomFieldList.add(customField);
                            break;
                        case "customers":
                            customersCustomFieldList.add(customField);
                            break;
                        case "sites":
                            sitesCustomFieldList.add(customField);
                            break;
                        case "users":
                            usersCustomFieldList.add(customField);
                            break;
                        case "equipments":
                            equipmentCustomFieldList.add(customField);
                            break;
                    }
                }
            }

        }else{
            showToast("response is null!");
        }
        bindTablesView();

    }
    private void jobTableView(){

        if(jobsCustomFieldList!=null && jobsCustomFieldList.size()!=0){
            for(CustomField customField:jobsCustomFieldList){
                List<String> choices =customField.getChoices();
                String formType=customField.getFormType();
                String id=customField.getId();
                boolean isPrivate=customField.getIsPrivate();
                String name=customField.getName();
                String typeOfField=customField.getTypeOfField();

                //Tables Rows
                TableRow tableRow=new TableRow(getContext());
                tableRow.setBackground(getResources().getDrawable(R.color.bg));
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));

                //TextView name
                TextView nameTextView=new TextView(getContext());
                if(name!=null){
                    nameTextView.setText(name);
                }
                nameTextView.setTextSize(24);
                TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                tableRowuserTextParams.setMargins(15,15,15,15);
                nameTextView.setLayoutParams(tableRowuserTextParams);
                nameTextView.setGravity(Gravity.CENTER);
                nameTextView.setTextColor(getResources().getColor(R.color.text_light));
                tableRow.addView(nameTextView);

                //TextView type
                TextView typeTextView=new TextView(getContext());
                if(typeOfField!=null){
                    typeTextView.setText(typeOfField);
                }
                typeTextView.setTextSize(24);
                TableRow.LayoutParams tableRowTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                tableRowTypeParams.setMargins(15,15,15,15);
                typeTextView.setLayoutParams(tableRowTypeParams);
                typeTextView.setTextColor(getResources().getColor(R.color.text_light));
                typeTextView.setGravity(Gravity.CENTER);
                tableRow.addView(typeTextView);

                //Technicians list Text View
                TextView privateTextView=new TextView(getContext());
                if(isPrivate){
                    privateTextView.setText("True");
                }else{
                    privateTextView.setText("False");
                }
                privateTextView.setTextSize(24);
                TableRow.LayoutParams tableRowPrivateParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                tableRowPrivateParams.setMargins(15,15,15,15);
                privateTextView.setLayoutParams(tableRowPrivateParams);
                privateTextView.setTextColor(getResources().getColor(R.color.text_light));
                privateTextView.setGravity(Gravity.CENTER);
                tableRow.addView(privateTextView);
                tableRow.setTag(customField);

                tableRow.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddCustomFieldsActivity.class).putExtra("customfield",new Gson().toJson(view1.getTag(),CustomField.class))));

                binding.tableLayoutJobs.addView(tableRow);

            }
        }
    }
    private void customerTableView(){

        if(customersCustomFieldList!=null && customersCustomFieldList.size()!=0){
            for(CustomField customField:customersCustomFieldList){
                List<String> choices =customField.getChoices();
                String formType=customField.getFormType();
                String id=customField.getId();
                boolean isPrivate=customField.getIsPrivate();
                String name=customField.getName();
                String typeOfField=customField.getTypeOfField();

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
                TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowuserTextParams.setMargins(10,10,0,10);
                nameTextView.setLayoutParams(tableRowuserTextParams);
                nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                nameTextView.setTextColor(getResources().getColor(R.color.light_black));
                nameTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(nameTextView);

                //TextView type
                TextView typeTextView=new TextView(getContext());
                if(typeOfField!=null){
                    typeTextView.setText(typeOfField);
                }
                typeTextView.setTextSize(20);
                TableRow.LayoutParams tableRowTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowTypeParams.setMargins(10,10,0,10);
                typeTextView.setLayoutParams(tableRowTypeParams);
                typeTextView.setTextColor(getResources().getColor(R.color.light_black));
                typeTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                typeTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(typeTextView);

                //Technicians list Text View
                TextView privateTextView=new TextView(getContext());
                if(isPrivate){
                    privateTextView.setText("True");
                }else{
                    privateTextView.setText("False");
                }
                privateTextView.setTextSize(20);
                TableRow.LayoutParams tableRowPrivateParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowPrivateParams.setMargins(10,10,10,10);
                privateTextView.setLayoutParams(tableRowPrivateParams);
                privateTextView.setTextColor(getResources().getColor(R.color.light_black));
                privateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                privateTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(privateTextView);
                tableRow.setTag(customField);

                tableRow.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddCustomFieldsActivity.class).putExtra("customfield",new Gson().toJson(view1.getTag(),CustomField.class))));

                binding.tableLayoutCustomer.addView(tableRow);

            }
        }
    }
    private void sitesTableview(){
        if(sitesCustomFieldList!=null && sitesCustomFieldList.size()!=0){
            for(CustomField customField:sitesCustomFieldList){
                List<String> choices =customField.getChoices();
                String formType=customField.getFormType();
                String id=customField.getId();
                boolean isPrivate=customField.getIsPrivate();
                String name=customField.getName();
                String typeOfField=customField.getTypeOfField();

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
                TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowuserTextParams.setMargins(10,10,0,10);
                nameTextView.setLayoutParams(tableRowuserTextParams);
                nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                nameTextView.setTextColor(getResources().getColor(R.color.light_black));
                nameTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(nameTextView);

                //TextView type
                TextView typeTextView=new TextView(getContext());
                if(typeOfField!=null){
                    typeTextView.setText(typeOfField);
                }
                typeTextView.setTextSize(20);
                TableRow.LayoutParams tableRowTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowTypeParams.setMargins(10,10,0,10);
                typeTextView.setLayoutParams(tableRowTypeParams);
                typeTextView.setTextColor(getResources().getColor(R.color.light_black));
                typeTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                typeTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(typeTextView);

                //Technicians list Text View
                TextView privateTextView=new TextView(getContext());
                if(isPrivate){
                    privateTextView.setText("True");
                }else{
                    privateTextView.setText("False");
                }
                privateTextView.setTextSize(20);
                TableRow.LayoutParams tableRowPrivateParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowPrivateParams.setMargins(10,10,10,10);
                privateTextView.setLayoutParams(tableRowPrivateParams);
                privateTextView.setTextColor(getResources().getColor(R.color.light_black));
                privateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                privateTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(privateTextView);
                tableRow.setTag(customField);

                tableRow.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddCustomFieldsActivity.class).putExtra("customfield",new Gson().toJson(view1.getTag(),CustomField.class))));

                binding.tableLayoutSites.addView(tableRow);

            }
        }
    }
    private void equipmentTableView(){
        if(equipmentCustomFieldList!=null && equipmentCustomFieldList.size()!=0){
            for(CustomField customField:equipmentCustomFieldList){
                List<String> choices =customField.getChoices();
                String formType=customField.getFormType();
                String id=customField.getId();
                boolean isPrivate=customField.getIsPrivate();
                String name=customField.getName();
                String typeOfField=customField.getTypeOfField();

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
                TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowuserTextParams.setMargins(10,10,0,10);
                nameTextView.setLayoutParams(tableRowuserTextParams);
                nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                nameTextView.setTextColor(getResources().getColor(R.color.light_black));
                nameTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(nameTextView);

                //TextView type
                TextView typeTextView=new TextView(getContext());
                if(typeOfField!=null){
                    typeTextView.setText(typeOfField);
                }
                typeTextView.setTextSize(20);
                TableRow.LayoutParams tableRowTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowTypeParams.setMargins(10,10,0,10);
                typeTextView.setLayoutParams(tableRowTypeParams);
                typeTextView.setTextColor(getResources().getColor(R.color.light_black));
                typeTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                typeTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(typeTextView);

                //Technicians list Text View
                TextView privateTextView=new TextView(getContext());
                if(isPrivate){
                    privateTextView.setText("True");
                }else{
                    privateTextView.setText("False");
                }
                privateTextView.setTextSize(20);
                TableRow.LayoutParams tableRowPrivateParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowPrivateParams.setMargins(10,10,10,10);
                privateTextView.setLayoutParams(tableRowPrivateParams);
                privateTextView.setTextColor(getResources().getColor(R.color.light_black));
                privateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                privateTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(privateTextView);
                tableRow.setTag(customField);

                tableRow.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddCustomFieldsActivity.class).putExtra("customfield",new Gson().toJson(view1.getTag(),CustomField.class))));

                binding.tableLayoutEquipment.addView(tableRow);

            }
        }
    }
    private void usersTableView(){
        if(usersCustomFieldList!=null && usersCustomFieldList.size()!=0){
            for(CustomField customField:usersCustomFieldList){
                List<String> choices =customField.getChoices();
                String formType=customField.getFormType();
                String id=customField.getId();
                boolean isPrivate=customField.getIsPrivate();
                String name=customField.getName();
                String typeOfField=customField.getTypeOfField();

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
                TableRow.LayoutParams tableRowuserTextParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowuserTextParams.setMargins(10,10,0,10);
                nameTextView.setLayoutParams(tableRowuserTextParams);
                nameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                nameTextView.setTextColor(getResources().getColor(R.color.light_black));
                nameTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(nameTextView);

                //TextView type
                TextView typeTextView=new TextView(getContext());
                if(typeOfField!=null){
                    typeTextView.setText(typeOfField);
                }
                typeTextView.setTextSize(20);
                TableRow.LayoutParams tableRowTypeParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowTypeParams.setMargins(10,10,0,10);
                typeTextView.setLayoutParams(tableRowTypeParams);
                typeTextView.setTextColor(getResources().getColor(R.color.light_black));
                typeTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                typeTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(typeTextView);

                //Technicians list Text View
                TextView privateTextView=new TextView(getContext());
                if(isPrivate){
                    privateTextView.setText("True");
                }else{
                    privateTextView.setText("False");
                }
                privateTextView.setTextSize(20);
                TableRow.LayoutParams tableRowPrivateParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT,50);
                tableRowPrivateParams.setMargins(10,10,10,10);
                privateTextView.setLayoutParams(tableRowPrivateParams);
                privateTextView.setTextColor(getResources().getColor(R.color.light_black));
                privateTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                privateTextView.setPadding(5, 5, 5, 5);
                tableRow.addView(privateTextView);
                tableRow.setTag(customField);

                tableRow.setOnClickListener(view1 -> startActivity(new Intent(getContext(),AddCustomFieldsActivity.class).putExtra("customfield",new Gson().toJson(view1.getTag(),CustomField.class))));

                binding.tableLayoutUsers.addView(tableRow);

            }
        }
    }
    private void bindTablesView() {
        binding.tableLayoutJobs.removeAllViews();
        binding.tableLayoutCustomer.removeAllViews();
        binding.tableLayoutSites.removeAllViews();
        binding.tableLayoutUsers.removeAllViews();
        binding.tableLayoutEquipment.removeAllViews();
        jobTableView();
        customerTableView();
        sitesTableview();
        equipmentTableView();
        usersTableView();
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
