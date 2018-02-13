package com.kyros.technologies.fieldout.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kyros.technologies.fieldout.R;
import com.kyros.technologies.fieldout.adapters.ManagerListAdapter;
import com.kyros.technologies.fieldout.common.ServiceHandler;
import com.kyros.technologies.fieldout.databinding.ActivityUpdateTeamBinding;
import com.kyros.technologies.fieldout.models.Manager;
import com.kyros.technologies.fieldout.models.ManagerInfo;
import com.kyros.technologies.fieldout.models.ManagersResponse;
import com.kyros.technologies.fieldout.models.SingleTeamResponse;
import com.kyros.technologies.fieldout.models.Tag;
import com.kyros.technologies.fieldout.models.TagInfo;
import com.kyros.technologies.fieldout.models.TagResponse;
import com.kyros.technologies.fieldout.models.TeamDeleteResponse;
import com.kyros.technologies.fieldout.models.TeamsItem;
import com.kyros.technologies.fieldout.models.Technician;
import com.kyros.technologies.fieldout.models.TechnicianInfo;
import com.kyros.technologies.fieldout.models.TechniciansResponse;
import com.kyros.technologies.fieldout.models.UpdateTeamResponse;
import com.kyros.technologies.fieldout.sharedpreference.PreferenceManager;
import com.kyros.technologies.fieldout.viewmodel.AddTeamViewModel;
import com.kyros.technologies.fieldout.viewmodel.UpdateTeamViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class UpdateTeamActivity extends AppCompatActivity {
    private String teamId=null;
    private PreferenceManager store;
    private CompositeSubscription subscription;
    private String TAG= UpdateTeamActivity.class.getSimpleName();
    private ProgressDialog progressDialog=null;
    private AlertDialog deleteDialog=null;
    private ActivityUpdateTeamBinding binding;
    @Inject
    UpdateTeamViewModel viewModel;
    private ManagerListAdapter managerListAdapter;
    private ManagerListAdapter technicianListAdapter;
    private ManagerListAdapter tagListAdapter;
    private List<Manager>managers=new ArrayList<>();
    private List<Technician>technicians=new ArrayList<>();
    private List<String>managersNameList=new ArrayList<>();
    private List<String>technicianNameList=new ArrayList<>();
    private AlertDialog multipleSelectDialog=null;
    private List<String>selectedTechnicianNameList=new ArrayList<>();
    private List<String>selectedManagersNameList=new ArrayList<>();
    private List<Tag>totalTagList=new ArrayList<>();
    @Inject
    AddTeamViewModel addTeamViewModel;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_update_team);
        ((ServiceHandler)getApplication()).getApplicationComponent().injectUpdateTeamActivity(this);
        store=PreferenceManager.getInstance(getApplicationContext());
        subscription=new CompositeSubscription();
        try {
            Bundle bundle=getIntent().getExtras();
            teamId=bundle.getString("teamId");
        }catch (Exception e){
            e.printStackTrace();
        }
        String authKey=store.getToken();
        initiateAPICall(authKey,teamId);
        String domainId=store.getIdDomain();
        initiateTAGAPICall(domainId,authKey);

        binding.saveUpdateTeamButton.setOnClickListener(view -> initiateUpdateAPICall(authKey,domainId));
    }

    private void initiateTAGAPICall(String domainId, String authKey) {
        subscription.add(viewModel.getTags(authKey,domainId)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> showTimber("Error : ","TAG",throwable.getMessage()))
                    .subscribe(this::tagResponse,this::tagError,this::tagCompleted));
    }
    private void tagResponse(TagResponse tagResponse){
        showTimber("TagResponse : ","TAG",tagResponse.toString());
        if(tagResponse!=null){
            totalTagList=tagResponse.getTags();
            //binding.tagsUpdateTeamEditText.setOnClickListener(view -> );
        }else {
            showToast("tag response is null!");
        }

    }
    private void tagError(Throwable throwable){
        showTimber("Error : ","TAG",throwable.getMessage());
        showToast(""+throwable.getMessage());

    }
    private void tagCompleted(){

    }

    private void initiateUpdateAPICall(String authKey, String domainId) {
        String teamName=binding.updateTeamNameEditText.getText().toString();
        String description=binding.descriptionEditTextUpdate.getText().toString();
        List<String>tagsList=tagListAdapter.getManagersList();
        List<String>managerNameList=managerListAdapter.getManagersList();
        List<String>technicianNameList=technicianListAdapter.getManagersList();
        List<String>technicianFinalIdList=new ArrayList<>();
        List<String>tagIdList=new ArrayList<>();
        for(String tagName:tagsList){
            for(Tag tag:totalTagList){
                if(tagName.toLowerCase().equals(tag.getName().toLowerCase())){
                    tagIdList.add(tag.getId());
                }
            }
        }

        for(String technicianName:technicianNameList){
            for(Technician technician:technicians){
                if(technicianName.toLowerCase().equals(technician.getUsername().toLowerCase())){
                    technicianFinalIdList.add(technician.getId());
                }
            }
        }
        List<String>managerFinalIdList=new ArrayList<>();
        for(String managerName:managerNameList){
            for(Manager manager:managers){
                if(managerName.toLowerCase().equals(manager.getUsername().toLowerCase())){
                    managerFinalIdList.add(manager.getId());
                }
            }
        }
        if(teamName!=null && !teamName.isEmpty() && description!=null && !description.isEmpty() && domainId!=null && technicianFinalIdList.size()!=0 && managerFinalIdList.size()!=0){
            TeamsItem teamsItem=new TeamsItem();
            teamsItem.setIdDomain(domainId);
            teamsItem.setManagers(managerFinalIdList);
            teamsItem.setTechnicians(technicianFinalIdList);
            teamsItem.setTags(tagIdList);
            teamsItem.setName(teamName);
            teamsItem.setDescriptions(description);
            Log.d("Input Update : ",TAG + " / / " + teamsItem.toString());
            showDialog();
            callUpdateTeamAPI(authKey,teamsItem);
        }else {
            showToast("Please enter all inputs!");
        }

    }

    private void callUpdateTeamAPI(String authKey, TeamsItem teamsItem) {
        subscription.add(viewModel.updateTeamResponseObservable(authKey,teamId,teamsItem,store.getIdDomain())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable ->Log.e("Error : ",TAG+" /  / "+throwable.getMessage()))
                    .subscribe(this::updateTeamResponse,this::onError,this::onCompleted));
    }
  private void updateTeamResponse(UpdateTeamResponse updateTeamResponse){
        dismissDialog();
    if(updateTeamResponse!=null){
        Log.e("updateTeamResponse : ",TAG+" /  / "+updateTeamResponse.toString());

        boolean isSuccess=updateTeamResponse.getIsSuccess();
        if(isSuccess)
            showToast("Update Team response is Success!");

        UpdateTeamActivity.this.finish();

    }else {
        showToast("Update Team response is empty!");
    }
  }
  private void onError(Throwable throwable){
      dismissDialog();
      showToast(""+throwable.getMessage());
      Log.e("Error : ",TAG+" /  / "+throwable.getMessage());
  }
  private void onCompleted(){
dismissDialog();
  }

    private void initiateAPICall(String authKey, String teamId) {
        subscription.add(viewModel.getSingleTeamObservable(authKey,teamId,store.getIdDomain())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(teamsResponse));
    }
    private Subscriber<SingleTeamResponse>teamsResponse=new Subscriber<SingleTeamResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(SingleTeamResponse singleTeamResponse) {
            validateResponse(singleTeamResponse);
        }



    };
    private void validateResponse(SingleTeamResponse singleTeamResponse) {
        if(singleTeamResponse!=null){
            showTimber("Team Response : ","validate response",singleTeamResponse.toString());
            String teamName=singleTeamResponse.getTeams().getName();
            if(teamName!=null){
                binding.updateTeamNameEditText.setText(teamName);
            }
            String description=singleTeamResponse.getTeams().getDescriptions();
            if(description!=null){
                binding.descriptionEditTextUpdate.setText(description);
            }
            List<ManagerInfo>managerInfos=singleTeamResponse.getTeams().getManagerInfo();
            List<String>managerName=new ArrayList<>();
            for(ManagerInfo managerInfo: managerInfos){
                managerName.add(managerInfo.getUsername());
            }

            if( managerName.size()!=0){
                binding.recyclerManagersUpdate.setVisibility(View.VISIBLE);
                managerListAdapter=new ManagerListAdapter(this,managerName);
                binding.recyclerManagersUpdate.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerManagersUpdate.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerManagersUpdate.setAdapter(managerListAdapter);
                managerListAdapter.notifyDataSetChanged();

            }
            List<TechnicianInfo>technicianInfos=singleTeamResponse.getTeams().getTechnicianInfo();
            List<String>technicianName=new ArrayList<>();
            for(TechnicianInfo technicianInfo:technicianInfos){
                technicianName.add(technicianInfo.getUsername());
            }
            if( technicianName.size()!=0){
                binding.recyclerTechniciansUpdate.setVisibility(View.VISIBLE);
                technicianListAdapter=new ManagerListAdapter(this,technicianName);
                binding.recyclerTechniciansUpdate.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerTechniciansUpdate.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerTechniciansUpdate.setAdapter(technicianListAdapter);
                technicianListAdapter.notifyDataSetChanged();

            }


            List<TagInfo>tagInfos=singleTeamResponse.getTeams().getTagInfo();
            List<String>tagName=new ArrayList<>();
            for(TagInfo  tagInfo:tagInfos){
                tagName.add(tagInfo.getName());
            }
            if( tagName.size()!=0){
                binding.tagsSelectedRecyclerUpdate.setVisibility(View.VISIBLE);
                tagListAdapter=new ManagerListAdapter(this,tagName);
                binding.tagsSelectedRecyclerUpdate.setLayoutManager(new LinearLayoutManager(this));
                binding.tagsSelectedRecyclerUpdate.setItemAnimator(new DefaultItemAnimator());
                binding.tagsSelectedRecyclerUpdate.setAdapter(tagListAdapter);
                tagListAdapter.notifyDataSetChanged();
            }


        }else{
            showToast("Team response is null!");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        store=PreferenceManager.getInstance(getApplicationContext());
        String domainId=store.getIdDomain();
        String authKey=store.getToken();
        initiateTechniciansAPI(domainId,authKey);
        initiateManagersAPI(domainId,authKey);
    }

    private void initiateManagersAPI(String domainId, String authKey) {
        subscription.add(addTeamViewModel.managersResponseObservable(authKey,domainId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(managersResponse));

    }
    private Subscriber<ManagersResponse>managersResponse=new Subscriber<ManagersResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }

        @Override
        public void onNext(ManagersResponse managersResponse) {
            validateManagerResponse(managersResponse);
        }
    };
    private void validateManagerResponse(ManagersResponse managersResponse) {
        if(managersResponse!=null){
            showTimber("Manager Response : ","VMR",managersResponse.toString());
            managers=managersResponse.getManagers();
            for(Manager manager:managers){
                managersNameList.add(manager.getUsername());


            }
            binding.checkBoxAddTeamManagersUpdate.setOnClickListener(view -> checkBoxTeamManagers(binding.checkBoxAddTeamManagersUpdate.isChecked()));
            binding.managerListTextViewUpdate.setOnClickListener(view ->{
                String[] arrayManager = new String[managersNameList.size()];
                boolean[] boolManager=new boolean[managersNameList.size()];
                for(int j =0;j<managersNameList.size();j++){
                    arrayManager[j] = managersNameList.get(j);
                }
             multipleSelectDialogBox("Select Manager",arrayManager,boolManager,binding.managerListTextViewUpdate,"manager");
            });


        }else {
            showToast("ManageResponse is null!");
        }
    }

    private void checkBoxTeamManagers(boolean checked) {
        if(checked){
            binding.recyclerManagersUpdate.setVisibility(View.VISIBLE);
            binding.recyclerManagersUpdate.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerManagersUpdate.setItemAnimator(new DefaultItemAnimator());
            managerListAdapter=new ManagerListAdapter(this,managersNameList);
            binding.recyclerManagersUpdate.setAdapter(managerListAdapter);
            managerListAdapter.notifyDataSetChanged();
        }else{
            binding.recyclerManagersUpdate.setVisibility(View.GONE);
        }
    }

    private void initiateTechniciansAPI(String domainId, String authKey) {
        subscription.add(addTeamViewModel.techniciansResponseObservable(authKey,domainId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                .subscribe(technicianResponse));
    }
    private Subscriber<TechniciansResponse>technicianResponse=new Subscriber<TechniciansResponse>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            showToast(""+e.getMessage());
            Log.e("Error : ",TAG+" / / "+e.getMessage());
        }


        @Override
        public void onNext(TechniciansResponse techniciansResponse) {
            validateTechnicianResponse(techniciansResponse);
        }
    };
    private void validateTechnicianResponse(TechniciansResponse techniciansResponse) {
        if(techniciansResponse!=null){
            Log.d("Technician Response  : ",TAG+" / / "+techniciansResponse.toString());
            technicians=techniciansResponse.getTechnicians();
            for(Technician technician: technicians){
                technicianNameList.add(technician.getUsername());
            }
            binding.technicianListTextViewUpdate.setOnClickListener(view -> {
                String arrayTechnician[] = new String[technicianNameList.size()];
                boolean boolTechnician[]=new boolean[technicianNameList.size()];
                for(int j =0;j<technicianNameList.size();j++){
                    arrayTechnician[j] = technicianNameList.get(j);
                }
                multipleSelectDialogBox("Select Technician",arrayTechnician,boolTechnician,binding.technicianListTextViewUpdate,"technician");
            });
            binding.checkBoxAddTeamUpdate.setOnClickListener(view -> checkBoxTeamTechnicians(binding.checkBoxAddTeamUpdate.isChecked()));


        }else {
            showToast("Technician Response is null!");
        }
    }

    private void checkBoxTeamTechnicians(boolean checked) {
        if(checked){
            binding.recyclerTechniciansUpdate.setVisibility(View.VISIBLE);
            binding.recyclerTechniciansUpdate.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerTechniciansUpdate.setItemAnimator(new DefaultItemAnimator());
            technicianListAdapter=new ManagerListAdapter(this,technicianNameList);
            binding.recyclerTechniciansUpdate.setAdapter(technicianListAdapter);
            technicianListAdapter.notifyDataSetChanged();
        }else{
            binding.recyclerTechniciansUpdate.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        dismissDialog();
        dismissMultipleSelectDialogBox();
        dismissDeleteDialog();
    }
    private void showDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(UpdateTeamActivity.this);
            progressDialog.setMessage("Loading Please wait..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                UpdateTeamActivity.this.finish();
                return true;
            case R.id.action_delete:
                showDeleteDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        if(deleteDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTeamActivity.this);
            builder.setMessage("Do you want to Delete it?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Yes",
                    (dialog, id) ->{
                        initiateDeleteTeamApi();
                        dialog.cancel();
                    });

            builder.setNegativeButton(
                    "No",
                    (dialog, id) -> dialog.cancel());


            deleteDialog = builder.create();
        }

        deleteDialog.show();
    }
    private void dismissDeleteDialog(){
        if(deleteDialog!=null && deleteDialog.isShowing()){
            deleteDialog.dismiss();
        }
    }
    private void initiateDeleteTeamApi() {
        subscription.add(viewModel.teamDeleteResponseObservable(teamId,store.getToken(),store.getIdDomain())
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(throwable -> Log.e("Error : ",TAG+" / / "+throwable.getMessage()))
                    .subscribe(this::deleteTeamResponse,this::deleteTeamError,this::deleteTeamComplete));
    }
    private void deleteTeamResponse(TeamDeleteResponse teamDeleteResponse){
        boolean result=teamDeleteResponse.isResult();
        if(result){
            showToast("Successfully deleted!");
            this.finish();
        }else{
            showToast("Some error occured while deleting");
        }

    }
    private void deleteTeamError(Throwable throwable){
        Log.e("Error : ",TAG+" / / "+throwable.getMessage());
        showToast(""+throwable.getMessage());

    }
    private void deleteTeamComplete(){

    }

    private void dismissDialog(){
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    private void showTimber(String header,String methodName,String message){
        Timber.d(header,TAG+" / " + methodName+" / /  "+message);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_delete, menu);
        return true;
    }
    private void multipleSelectDialogBox(String title, String[] values, boolean[] checkedItems, TextView view, String whichone){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTeamActivity.this);
        builder.setTitle(title);
        builder.setMultiChoiceItems(values, checkedItems, (dialog, which, isChecked) -> {
            if(isChecked){
                if(whichone.equals("technician")){
                    selectedTechnicianNameList.add(values[which]);
                }else if(whichone.equals("manager")){
                    selectedManagersNameList.add(values[which]);
                }
            }
        });

        builder.setPositiveButton("OK", (dialog, which) -> {
            if(whichone.equals("technician")){
                binding.recyclerTechniciansUpdate.setVisibility(View.VISIBLE);
                binding.recyclerTechniciansUpdate.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerTechniciansUpdate.setItemAnimator(new DefaultItemAnimator());
                technicianListAdapter=new ManagerListAdapter(this,selectedTechnicianNameList);
                binding.recyclerTechniciansUpdate.setAdapter(technicianListAdapter);
                technicianListAdapter.notifyDataSetChanged();
            }else if(whichone.equals("manager")){
                binding.recyclerManagersUpdate.setVisibility(View.VISIBLE);
                binding.recyclerManagersUpdate.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerManagersUpdate.setItemAnimator(new DefaultItemAnimator());
                managerListAdapter=new ManagerListAdapter(this,selectedManagersNameList);
                binding.recyclerManagersUpdate.setAdapter(managerListAdapter);
                managerListAdapter.notifyDataSetChanged();
            }
            dismissMultipleSelectDialogBox();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dismissMultipleSelectDialogBox());

        multipleSelectDialog = builder.create();
        multipleSelectDialog.show();


    }
    private void dismissMultipleSelectDialogBox(){
        if(multipleSelectDialog!=null && multipleSelectDialog.isShowing()){
            multipleSelectDialog.dismiss();
        }
    }
}
