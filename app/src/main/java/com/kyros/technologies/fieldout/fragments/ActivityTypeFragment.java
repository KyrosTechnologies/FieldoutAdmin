package com.kyros.technologies.fieldout.fragments;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentActivityTypeBinding;
import com.kyros.technologies.fieldout.models.ActivityType;
import com.kyros.technologies.fieldout.models.ActivityTypeAddResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeDeleteResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeResponse;
import com.kyros.technologies.fieldout.models.ActivityTypeUpdateResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.ActivityTypeFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import uz.shift.colorpicker.LineColorPicker;

/**
 * Created by kyros on 22-12-2017.
 */

public class ActivityTypeFragment extends Fragment {
    private FragmentActivityTypeBinding binding;
    private View view;
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=ActivityTypeFragment.class.getSimpleName();
    @Inject
    ActivityTypeFragmentViewModel viewModel;
    private AlertDialog updateActivityTypeDialog=null;
    private AlertDialog addActivityTypeDialog=null;
    private AlertDialog deleteDialog=null;
    private String colorCode=null;
    private int selectedConflict=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_activity_type,container,false);
        view=binding.getRoot();
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectActivityTypeFragment(this);
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getContext());
        binding.addActivityTypeButton.setOnClickListener(view->addActivityTypeMethod());
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        initiateActivityTypeAPI(domainId,authKey);
    }

    private void initiateActivityTypeAPI(String domainId, String authKey) {
        subscription.add(viewModel.activityTypeResponseObservable(domainId,authKey)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ", TAG+" / / "+throwable.getMessage()))
                    .subscribe(this::activityTypeResponse,this::activityTypeError,this::activityTypeCompleted));
    }
    private void activityTypeResponse(ActivityTypeResponse activityTypeResponse){
        if(activityTypeResponse!=null){
            Log.d("Act type response : ",TAG+" / / "+activityTypeResponse.toString());
            bindViews(activityTypeResponse);
        }else {
            showToast("activity type is null!");
        }
    }

    private void bindViews(ActivityTypeResponse activityTypeResponse) {
        List<ActivityType>activityTypeList=activityTypeResponse.getActivityTypes();
        binding.activityTypeTableLayout.removeAllViews();
        View view=getLayoutInflater().inflate(R.layout.table_row_activity_type,null);
        binding.activityTypeTableLayout.addView(binding.rowActivityType);
        if(activityTypeList!=null && activityTypeList.size()!=0){
            for(ActivityType activityType:activityTypeList){
                String activityId=activityType.getId();
                String domainId=activityType.getIdDomain();
                String color=activityType.getColor();
                String hasConflict=activityType.getHasConflit();
                String activityName=activityType.getName();

                //Table Rows
                TableRow tableRow=new TableRow(getContext());
                tableRow.setBackground(getResources().getDrawable(R.color.bg));
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));

                //Manages acitivity name
                TextView activityNameTextView=new TextView(getContext());
                if(activityName!=null){
                    activityNameTextView.setText(activityName);
                }
                activityNameTextView.setTextSize(24);
                TableRow.LayoutParams tableRowActivityNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                tableRowActivityNameParams.setMargins(15,15,15,15);
                activityNameTextView.setLayoutParams(tableRowActivityNameParams);
                activityNameTextView.setTextColor(getResources().getColor(R.color.text_light));
                activityNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                tableRow.addView(activityNameTextView);

                //Activity color
                TextView activityColorTextView=new TextView(getContext());
                if(activityName!=null){
                    activityColorTextView.setText(activityName);
                }
                activityColorTextView.setTextSize(24);
                TableRow.LayoutParams tableRowActivityColorParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                tableRowActivityColorParams.setMargins(15,15,15,15);
                activityColorTextView.setLayoutParams(tableRowActivityColorParams);
                activityColorTextView.setTextColor(getResources().getColor(R.color.text_light));
                activityColorTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                activityColorTextView.setBackgroundColor(Color.parseColor(color));
                tableRow.addView(activityColorTextView);

                //generate conflict with jobs
                TextView conflictTextView=new TextView(getContext());
                if(hasConflict!=null){
                    conflictTextView.setText(hasConflict);
                }
                conflictTextView.setTextSize(24);
                TableRow.LayoutParams tableRowtagsParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                tableRowtagsParams.setMargins(15,15,15,15);
                conflictTextView.setLayoutParams(tableRowtagsParams);
                conflictTextView.setTextColor(getResources().getColor(R.color.text_light));
                conflictTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                tableRow.addView(conflictTextView);
                tableRow.setTag(activityType);

                tableRow.setOnClickListener(view1 ->{
                    Object valueId=view1.getTag();
                    try{
                        ActivityType activityType1=(ActivityType)valueId;
                        updateActivityTypeDialog(activityType1);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } );

                binding.activityTypeTableLayout.addView(tableRow);


            }
        }

    }

    private void addActivityTypeMethod() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.activity_update_type,null);
        EditText activity_type_name=view.findViewById(R.id.activity_type_name);
        LineColorPicker colorPicker = view. findViewById(R.id.picker);
        colorPicker.setColors(new int[] {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW});
        colorPicker.setOnColorChangedListener(c -> colorCode= Integer.toHexString(c));

        Spinner spinner_conflict=view.findViewById(R.id.spinner_conflict);

        List<String>conflictType=new ArrayList<>();
        conflictType.add("Yes");
        conflictType.add("No");
        ArrayAdapter<String> adapterconflict=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,
                conflictType);
        adapterconflict.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_conflict.setPrompt("Conflict");
        spinner_conflict.setAdapter(adapterconflict);



        spinner_conflict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    Log.d("Selected Item : ",""+i);
                    selectedConflict=i;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view);

        builder.setPositiveButton("OK",(dialog,id)->{
            initiateAddActivityTypeAPI(activity_type_name.getText().toString(),colorCode,selectedConflict);
        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissupdateActivityTypeDialog();
        });
        addActivityTypeDialog=builder.create();
        addActivityTypeDialog.show();
    }
    private void dismissAddActivityTypeDialog(){
        if(addActivityTypeDialog!=null && addActivityTypeDialog.isShowing()){
            addActivityTypeDialog.dismiss();
        }
    }

    private void initiateAddActivityTypeAPI(String activityTypeName, String colorCode, int selectedConflict) {
        String hasConflict;
        colorCode="#"+colorCode;
        if(selectedConflict==0){
            hasConflict="yes";
        }else{
            hasConflict="no";
        }
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
       ActivityType activityType=new ActivityType();
       activityType.setColor(colorCode);
       activityType.setHasConflit(hasConflict);
       activityType.setName(activityTypeName);
       activityType.setIdDomain(domainId);
       callAddActivityAPI(authKey,activityType);

    }

    private void callAddActivityAPI(String authKey, ActivityType activityType) {
        subscription.add(viewModel.activityTypeAddResponseObservable(authKey,activityType)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(this::addActivityTypeResponse,this::errorAddActivityTypeRespone,this::completedActivityTypeResponse));
    }

    private void completedActivityTypeResponse() {

    }

    private void errorAddActivityTypeRespone(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void addActivityTypeResponse(ActivityTypeAddResponse activityTypeAddResponse) {
        if(activityTypeAddResponse!=null){
            boolean success=activityTypeAddResponse.getIsSuccess();
            Log.d("Activity type res : ",TAG+" / / "+activityTypeAddResponse.toString());
            if(success){
                showToast("Activity Type added successfully!");
                dismissAddActivityTypeDialog();
            }
        }else {
            showToast("Activity type add is null!");
        }
    }

    private void updateActivityTypeDialog(ActivityType activityType) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.activity_update_type,null);
        EditText activity_type_name=view.findViewById(R.id.activity_type_name);
        LineColorPicker colorPicker = view. findViewById(R.id.picker);
        colorPicker.setColors(new int[] {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW});

        Spinner spinner_conflict=view.findViewById(R.id.spinner_conflict);
        String typeName=activityType.getName();
        String color=activityType.getColor();
        String activityTypeId=activityType.getId();
        if(color!=null){
            colorCode=color;
            colorPicker.setSelectedColor(Color.parseColor(colorCode));
        }
        colorPicker.setOnColorChangedListener(c -> colorCode= Integer.toHexString(c));

        List<String>conflictType=new ArrayList<>();
        conflictType.add("Yes");
        conflictType.add("No");
        ArrayAdapter<String> adapterconflict=new  ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,
                conflictType);
        adapterconflict.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_conflict.setPrompt("Conflict");
        spinner_conflict.setAdapter(adapterconflict);

        if(typeName!=null){
            activity_type_name.setText(typeName);
        }

        spinner_conflict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    Log.d("Selected Item : ",""+i);
                    selectedConflict=i;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view);

        builder.setPositiveButton("OK",(dialog,id)->{
            initiateUpdateActivityType(activity_type_name.getText().toString(),colorCode,selectedConflict,activityTypeId);
        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissupdateActivityTypeDialog();
        });
        builder.setNeutralButton("Delete",(dialog,id)->{

            showDeleteDialog(activityTypeId);
        });
        updateActivityTypeDialog=builder.create();
        updateActivityTypeDialog.show();

    }

    private void initiateUpdateActivityType(String activityTypeName, String color, int selectedConflict,String activityTypeId) {
                if(!color.contains("#")){
                    color="#"+color;
                }
                String hasConflict;
                if(selectedConflict==0){
                    hasConflict="yes";
                }else{
                    hasConflict="no";
                }
            if(activityTypeName!=null && !activityTypeName.isEmpty() && color!=null && !color.isEmpty() && activityTypeId!=null && !activityTypeId.isEmpty()){
                ActivityType activityType=new ActivityType();
                activityType.setIdDomain(store.getIdDomain());
                activityType.setColor(color);
                activityType.setName(activityTypeName);
                activityType.setHasConflit(hasConflict);
                callUpdateActivityTypeAPI(activityTypeId,store.getToken(),activityType);

            }else{
                showToast("please enter all details!");
            }
    }

    private void callUpdateActivityTypeAPI(String activityTypeId, String authKey, ActivityType activityType) {
            subscription.add(viewModel.activityTypeUpdateResponseObservable(authKey,activityTypeId,activityType)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                        .subscribe(this::updateActivityTypeResponse,this::errorActivityTypeUpdate,this::completedUpdateActivityType));
    }

    private void completedUpdateActivityType() {

    }

    private void errorActivityTypeUpdate(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void updateActivityTypeResponse(ActivityTypeUpdateResponse activityTypeUpdateResponse) {
        if(activityTypeUpdateResponse!=null){
            Log.d("Activity UPdate : ",TAG+" / / "+activityTypeUpdateResponse.toString());
            boolean success=activityTypeUpdateResponse.getIsSuccess();
            if(success){
                showToast("Activity type is updated successfully!");
                dismissupdateActivityTypeDialog();
            }
        }else {
            showToast("activity update response is null!");
        }
    }

    private  void dismissupdateActivityTypeDialog(){
        if(updateActivityTypeDialog!=null && updateActivityTypeDialog.isShowing()){
            updateActivityTypeDialog.dismiss();
        }
    }

    private void activityTypeError(Throwable throwable){
    Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }
    private void activityTypeCompleted(){

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
        dismissupdateActivityTypeDialog();
        dismissAddActivityTypeDialog();
        dismissDeleteDialog();
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void showDeleteDialog(String activityTypeId) {
        if(deleteDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                       initiateDeleteActivityTypeApi(activityTypeId);
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());


            deleteDialog = builder.create();
        }

        deleteDialog.show();
    }

    private void initiateDeleteActivityTypeApi(String activityTypeId) {
        if(activityTypeId!=null && !activityTypeId.isEmpty()){
            subscription.add(viewModel.activityTypeDeleteResponseObservable(store.getToken(),activityTypeId)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                        .subscribe(this::deleteActivityTypeResponse,this::errorDeleteActivityTypeResponse,this::completedDeleteActivityTypeResponse));
        }else {
            showToast("Id is null!");
        }
    }

    private void errorDeleteActivityTypeResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void completedDeleteActivityTypeResponse() {
    }



    private void deleteActivityTypeResponse(ActivityTypeDeleteResponse activityTypeDeleteResponse) {
        if(activityTypeDeleteResponse!=null){
            Log.d("DeleteResponse : ",TAG+" / / "+activityTypeDeleteResponse.toString());
            boolean success=activityTypeDeleteResponse.isResult();
            if(success){
                showToast("Successfully deleted!");
            }
        }else {
            showToast("delete response is null!");
        }
    }

    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
    }
}
