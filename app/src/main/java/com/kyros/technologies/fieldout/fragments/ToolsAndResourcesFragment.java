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
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.FragmentToolsResourcesBinding;
import com.kyros.technologies.fieldout.models.AddToolsResourceResponse;
import com.kyros.technologies.fieldout.models.DeleteToolsAndResourcesResponse;
import com.kyros.technologies.fieldout.models.GetToolsAndResourcesResponse;
import com.kyros.technologies.fieldout.models.Resource;
import com.kyros.technologies.fieldout.models.UpdateToolsAndResourceResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.ToolsAndResourceFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import uz.shift.colorpicker.LineColorPicker;

/**
 * Created by kyros on 23-12-2017.
 */

public class ToolsAndResourcesFragment extends Fragment {
    private CompositeSubscription subscription;
    private PreferenceManager store;
    private String TAG=ToolsAndResourcesFragment.class.getSimpleName();
    private FragmentToolsResourcesBinding binding;
    private AlertDialog addActivityTypeDialog=null;
    private AlertDialog deleteDialog=null;
    private String colorCode=null;
    private List<Resource>resourcesList=new ArrayList<>();
    @Inject
    ToolsAndResourceFragmentViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_tools_resources,container,false);
        ((ServiceHandler)getActivity().getApplication()).getApplicationComponent().injectToolsAndResourcesFragment(this);
        View view=binding.getRoot();
        subscription=new CompositeSubscription();
        store=PreferenceManager.getInstance(getContext());
        binding.addToolResourceButton.setOnClickListener(view1 -> addActivityTypeMethod(store.getToken(),store.getIdDomain()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getContext());
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        initiateGetToolsAndResourcesApi(authKey,domainId);
    }

    private void initiateGetToolsAndResourcesApi(String authKey, String domainId) {
        if(domainId!=null && !domainId.isEmpty() && authKey!=null && !authKey.isEmpty()){
            subscription.add(viewModel.getToolsAndResourcesResponseObservable(authKey,domainId)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
            .subscribe(this::getToolsResponse,this::getErrorToolsResponse,this::completeGetToolsResponse));
        }else {
            showToast("Key is null!");
        }
    }

    private void completeGetToolsResponse() {

    }

    private void getErrorToolsResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void getToolsResponse(GetToolsAndResourcesResponse getToolsAndResourcesResponse) {
        if(getToolsAndResourcesResponse!=null){
            Log.d("GetToolsResponse : ",TAG+" / / "+getToolsAndResourcesResponse.toString());
            List<Resource>resourcesList=getToolsAndResourcesResponse.getResources();
            binding.toolsResouceTableLayout.removeAllViews();
            View view=getLayoutInflater().inflate(R.layout.table_row_tools_resources,null);
            binding.toolsResouceTableLayout.addView(binding.rowToolsResource);
            if(resourcesList.size()!=0){

                for(Resource resource: resourcesList){
                    String resourceName=resource.getResourceName();
                    String color=resource.getResourceColor();
                    //Table Rows
                    TableRow tableRow=new TableRow(getContext());
                    binding.toolsResouceTableLayout.removeView(tableRow);
                    tableRow.setBackground(getResources().getDrawable(R.color.bg));
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120));

                    //Tools and resources  name
                    TextView toolsNameTextView=new TextView(getContext());
                    if(resourceName!=null){
                        toolsNameTextView.setText(resourceName);
                    }
                    toolsNameTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowToolsNameParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowToolsNameParams.setMargins(15,15,15,15);
                    toolsNameTextView.setLayoutParams(tableRowToolsNameParams);
                    toolsNameTextView.setTextColor(getResources().getColor(R.color.text_light));
                    toolsNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    tableRow.addView(toolsNameTextView);

                    //resource color

                    TextView resourceColorTextView=new TextView(getContext());
//                    if(resourceName!=null){
//                        resourceColorTextView.setText(resourceName);
//                    }
                    resourceColorTextView.setTextSize(24);
                    TableRow.LayoutParams tableRowResourceColorParams=new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 120,50);
                    tableRowResourceColorParams.setMargins(15,15,15,15);
                    resourceColorTextView.setLayoutParams(tableRowResourceColorParams);
                    resourceColorTextView.setTextColor(getResources().getColor(R.color.text_light));
                    resourceColorTextView.setGravity(Gravity.LEFT|Gravity.CENTER);
                    resourceColorTextView.setBackgroundColor(Color.parseColor(color));
                    tableRow.addView(resourceColorTextView);

                    tableRow.setTag(resource);
                    tableRow.setOnClickListener(view1 ->{
                        Object valueId=view1.getTag();
                        try{
                            Resource resource1=(Resource)valueId;
                            updateResourceDialog(resource1);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } );

                    binding.toolsResouceTableLayout.addView(tableRow);
                }


            }
        }else {
            showToast("getTools response is null!");
        }
    }

    private void updateResourceDialog(Resource resource) {
        String authKey=store.getToken();
        String domainId=store.getIdDomain();
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.dialog_add_tools,null);
        EditText tools_resources_name=view.findViewById(R.id.tools_resources_name);
        String resourceNameget=resource.getResourceName();
        if(resourceNameget!=null){
            tools_resources_name.setText(resourceNameget);
        }
        LineColorPicker colorPicker = view. findViewById(R.id.picker_tools);
        String colorget=resource.getResourceColor();
        if(colorget!=null){
            colorCode=colorget;
            colorPicker.setSelectedColor(Color.parseColor(colorCode));
        }
        colorPicker.setColors(new int[] {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW});
        colorPicker.setOnColorChangedListener(c -> colorCode= Integer.toHexString(c));
        builder.setView(view);
        builder.setPositiveButton("OK",(dialog,id)->{
            String resourceName=tools_resources_name.getText().toString();
            if(resourceName!=null && !resourceName.isEmpty() && colorCode!=null && !colorCode.isEmpty()){
                if(!colorCode.contains("#")){
                    colorCode="#"+colorCode;
                }

                initiateUpdateToolsResourcesAPI(tools_resources_name.getText().toString(),colorCode,authKey,domainId,resource.getId());
            }else {
                showToast("Please enter all fields!");
            }
        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissAddToolsResourcesDialog();
        });
        builder.setNeutralButton("Delete",(dialog,id)->{

            showDeleteDialog(resource.getId());
        });
        addActivityTypeDialog=builder.create();
        addActivityTypeDialog.show();
    }

    private void showDeleteDialog(String resourceId) {
        if(deleteDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        initiateDeleteToolsResourceApi(resourceId);
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());


            deleteDialog = builder.create();
        }

        deleteDialog.show();
    }

    private void initiateDeleteToolsResourceApi(String resourceId) {
        subscription.add(viewModel.deleteToolsAndResourcesResponseObservable(store.getToken(),resourceId)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" /  / "+throwable.getMessage()))
        .subscribe(this::deleteToolsResourcesRespone,this::errorDeleteToolsResourcesResponse,this::completedToolsResourcesRespone));
    }

    private void completedToolsResourcesRespone() {


    }

    private void errorDeleteToolsResourcesResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" /  / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void deleteToolsResourcesRespone(DeleteToolsAndResourcesResponse deleteToolsAndResourcesResponse) {
        if(deleteToolsAndResourcesResponse!=null){
            Log.d("delete respone : ",TAG+" / / "+deleteToolsAndResourcesResponse.toString());
            boolean success=deleteToolsAndResourcesResponse.isResult();
            if(success){
                showToast("Deleted successfully!");
                String authKey=store.getToken();
                String domainId=store.getIdDomain();
                initiateGetToolsAndResourcesApi(authKey,domainId);
            }else{
                showToast("delete is not successfull");
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

    private void initiateUpdateToolsResourcesAPI(String resourceName, String colorCode, String authKey, String domainId,String resourceId) {
        if(domainId!=null && !domainId.isEmpty() && authKey!=null && !authKey.isEmpty() ){
            Resource resource=new Resource();
            resource.setResourceName(resourceName);
            resource.setResourceColor(colorCode);
            resource.setIdDomain(domainId);
            callUpdateToolsResourcesAPI(resource,resourceId,authKey);
        }else {
            showToast("Please enter all input fields");
        }
    }

    private void callUpdateToolsResourcesAPI(Resource resource, String resourceId, String authKey) {
        subscription.add(viewModel.updateToolsAndResourceResponseObservable(authKey,resourceId,resource)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
        .subscribe(this::updateToolsResponse,this::updateErrorToolsResponse,this::completeUpdateTools));

    }

    private void completeUpdateTools() {

    }

    private void updateErrorToolsResponse(Throwable throwable) {
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void updateToolsResponse(UpdateToolsAndResourceResponse updateToolsAndResourceResponse) {
        if(updateToolsAndResourceResponse!=null){
            Log.d("update response : ",TAG+" / / "+updateToolsAndResourceResponse.toString());
            boolean success=updateToolsAndResourceResponse.getIsSuccess();
            if(success){
                showToast("Updated Successfully");
                String authKey=store.getToken();
                String domainId=store.getIdDomain();
                initiateGetToolsAndResourcesApi(authKey,domainId);
            }else{
                showToast("update is not successfull");
            }
        }else {
            showToast("update response is null");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscription.clear();
        dismissAddToolsResourcesDialog();
        dismissDeleteDialog();
    }
    private void showToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void addActivityTypeMethod(String authKey, String domainId) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflate=getLayoutInflater();
        View view=layoutInflate.inflate(R.layout.dialog_add_tools,null);
        EditText tools_resources_name=view.findViewById(R.id.tools_resources_name);
        LineColorPicker colorPicker = view. findViewById(R.id.picker_tools);
        colorPicker.setColors(new int[] {Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW});
        colorPicker.setOnColorChangedListener(c -> colorCode= Integer.toHexString(c));
        builder.setView(view);
        builder.setPositiveButton("OK",(dialog,id)->{
            String resourceName=tools_resources_name.getText().toString();
            if(resourceName!=null && !resourceName.isEmpty() && colorCode!=null && !colorCode.isEmpty()){
                if(!colorCode.contains("#")){
                    colorCode="#"+colorCode;
                }
                initiateAddToolsResourcesAPI(tools_resources_name.getText().toString(),colorCode,authKey,domainId);
            }else {
                showToast("Please enter all fields!");
            }
        });
        builder.setNegativeButton("Cancel",(dialog,i)->{
            dismissAddToolsResourcesDialog();
        });
        addActivityTypeDialog=builder.create();
        addActivityTypeDialog.show();
    }

    private void initiateAddToolsResourcesAPI( String resourceName, String colorCode,String authKey, String domainId) {
            if(domainId!=null && !domainId.isEmpty() && authKey!=null && !authKey.isEmpty()){
                Resource resource=new Resource();
                resource.setIdDomain(domainId);
                resource.setResourceColor(colorCode);
                resource.setResourceName(resourceName);
                callAddToolsResourcesAPI(authKey,resource);
            }
    }

    private void callAddToolsResourcesAPI(String authKey, Resource resource) {
        subscription.add(viewModel.addToolsResourceResponseObservable(authKey,resource)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ", TAG+" / /  "+throwable.getMessage()))
                    .subscribe(this::addToolsResponse,this::addToolsError,this::addToolsComplete));
    }

    private void addToolsComplete() {

    }

    private void addToolsError(Throwable throwable) {
        Log.e("Error : ", TAG+" / /  "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }

    private void addToolsResponse(AddToolsResourceResponse addToolsResourceResponse) {
    if(addToolsResourceResponse!=null){
        Log.d("Add  Tools : ",TAG+" / / "+addToolsResourceResponse.toString());
        boolean success=addToolsResourceResponse.getIsSuccess();
        if(success){
            showToast("Tools and Resources added successfully!");
            String authKey=store.getToken();
            String domainId=store.getIdDomain();
            initiateGetToolsAndResourcesApi(authKey,domainId);
        }else{
            showToast("not successfully added");
        }
    }else {
        showToast("add tools resource response is null!");
    }
    }

    private void dismissAddToolsResourcesDialog(){
        if(addActivityTypeDialog!=null && addActivityTypeDialog.isShowing()){
            addActivityTypeDialog.dismiss();
        }
    }
}
